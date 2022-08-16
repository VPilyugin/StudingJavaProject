package edu.SOV.dao;

import edu.SOV.Config.Config;
import edu.SOV.Exeption.DaoException;
import edu.SOV.domain.*;
import edu.SOV.domain.children.Child;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentOrderDaoImpl implements StudentOrderDao {
    private static final String INSERT_ORDER = "INSERT INTO public.jc_student_order("+
            "student_order_status, student_order_date, h_sur_name, h_given_name," +
            " h_patronymic, h_date_of_birth, h_passport_seria, h_passport_number, h_passport_date," +
            " h_passport_office_id, h_post_index, h_street_code, h_building, h_extension, h_apartment, " +
            "h_university_id, h_student_id," +
            " w_sur_name, w_given_name, w_patronymic, w_date_of_birth, w_passport_seria, w_passport_number," +
            " w_passport_date, w_passport_office_id, w_post_index, w_street_code, w_building, w_extension," +
            " w_apartment,w_university_id, w_student_id, certificate_id, register_office_id, marriage_date)" +
            "VALUES (?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_CHILD = "INSERT INTO public.jc_student_child(" +
            "student_order_id, c_sur_name, c_given_name, c_patronymic, " +
            "c_date_of_birth, certificate_number, certificate_date, c_register_office_id, c_post_index," +
            " c_street_code, c_building, c_extension, c_apartment)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SELECT_ORDERS = "SELECT  h_po.p_office_area_id AS h_p_office_area_id,\n" +
            "        h_po.p_office_name AS h_p_office_name,\n" +
            "        w_po.p_office_area_id AS w_p_office_area_id,\n" +
            "        w_po.p_office_name AS w_p_office_name,\n" +
            "        ro.r_office_area_id, \n" +
            "        ro.r_office_name, so.* \n" +
            "FROM jc_student_order AS so\n" +
            "INNER JOIN jc_register_office AS ro ON so.register_office_id = ro.r_office_id \n" +
            "INNER JOIN jc_passport_office AS h_po ON h_po.p_office_id = so.h_passport_office_id\n" +
            "INNER JOIN jc_passport_office AS w_po ON w_po.p_office_id = so.w_passport_office_id\n" +
            "WHERE student_order_status = ? " +
            "ORDER BY student_order_date;";
    private static final String SELECT_CHILD = "SELECT jro.r_office_area_id, jro.r_office_name, jsc.* \n" +
            "FROM jc_student_child AS jsc\n" +
            "INNER JOIN jc_register_office AS jro ON jsc.c_register_office_id = jro.r_office_id\n" +
            "WHERE jsc.student_order_id IN ";
    private Connection getConnectionLocal(){
        //       Class.forName("org.postgresql.Driver");
        Connection con = null;
        try {
            con = DriverManager.getConnection(
                    Config.getProperty(Config.DB_LOCAL_URL),
                    Config.getProperty(Config.DB_LOCAL_LOGIN),
                    Config.getProperty(Config.DB_LOCAL_PASSWORD)
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        Statement statement = con.createStatement();
//        ResultSet rs = statement.executeQuery("SELECT * FROM jc_streets");
//        while (rs.next()){
//            System.out.println(rs.getLong("street_code")+" : "+rs.getString("street_name"));
//        }
        return con;
    }

    @Override
    public Long saveStudentOrder(StudentOrder studentOrder){
        Long result = -1L;
        try(Connection connection = getConnectionLocal()){
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER,new String[]{"student_order_id"});
            try {
                preparedStatement.setInt(1, StudentOrderStatus.START.ordinal());
                preparedStatement.setTimestamp(2,
                        Timestamp.valueOf(LocalDateTime.now()));
                setParamsForAdult(studentOrder.getHusband(),3, preparedStatement);
                setParamsForAdult(studentOrder.getWife(),18,preparedStatement);
                preparedStatement.setString(33,studentOrder.getMarriageCertificate());
                preparedStatement.setLong(34,studentOrder.getMarriageOffice().getOfficeId());
                preparedStatement.setDate(35,
                        Date.valueOf(studentOrder.getMarriageDate()));
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    result = resultSet.getLong("student_order_id");
                }
                saveChildToStudentOrder(connection,studentOrder,result);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public List<StudentOrder> getStudentsOrders() throws DaoException {
        List<StudentOrder> studentOrderList = new ArrayList<>();
        try (Connection connection = getConnectionLocal();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDERS)){
            preparedStatement.setInt(1,StudentOrderStatus.START.ordinal());
            ResultSet resultSet = preparedStatement.executeQuery();
//            List<Long> idList = new ArrayList<>();
            while (resultSet.next()){
                StudentOrder studentOrder = new StudentOrder();
                fillStudentOrder(resultSet,studentOrder);
                fillWedding(resultSet,studentOrder);
                Adult husband = fillAdult(resultSet,"h_");
                Adult wife = fillAdult(resultSet,"w_");
                studentOrder.setHusband(husband);
                studentOrder.setWife(wife);
                studentOrderList.add(studentOrder);
//                idList.add(studentOrder.getStudentOrderID());
            }
            findChildren(connection,studentOrderList);
//            StringBuilder stringBuilder = new StringBuilder("(");
//            for(Long l:idList){
//                stringBuilder.append(((stringBuilder.length()>1)?",":"")+String.valueOf(l));
//            }
//            stringBuilder.append(")");
//            String s = stringBuilder.toString();
//            System.out.println(s);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return studentOrderList;
    }

    private void findChildren(Connection connection, List<StudentOrder> studentOrderList) throws SQLException {
        String s = "("+ studentOrderList.stream()
                            .map(x->String.valueOf(x.getStudentOrderID()))
                            .collect(Collectors.joining(","))+
                            ")";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CHILD+s)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getString("r_office_area_id")+" "+
                        resultSet.getString("r_office_name"));
            }
        }
    }

    private Adult fillAdult(ResultSet resultSet, String prefix) throws SQLException {
        Adult adult = new Adult(
                resultSet.getString(prefix+"sur_name"),
                resultSet.getString(prefix+"given_name"),
                resultSet.getString(prefix+"patronymic"),
                resultSet.getDate(prefix+"date_of_birth").toLocalDate()
        );
        adult.setPassportSeria(resultSet.getString(prefix+"passport_seria"));
        adult.setPassportNumber(resultSet.getString(prefix+"passport_number"));
        adult.setIssueDate(resultSet.getDate(prefix+"passport_date").toLocalDate());
        String pOfficeAreaId = resultSet.getString(prefix+"p_office_area_id");
        String pOfficeName = resultSet.getString(prefix+"p_office_name");
        PassportOffice passportOffice = new PassportOffice(
                resultSet.getLong(prefix+"passport_office_id"),pOfficeAreaId,pOfficeName);
        adult.setIssueDepartment(passportOffice);
        Address address = new Address();
        Street street = new Street(resultSet.getLong(prefix+"street_code"),"");
        address.setStreet(street);
        address.setPostcode(resultSet.getString(prefix+"post_index"));
        address.setBuilding(resultSet.getString(prefix+"building"));
        address.setExtension(resultSet.getString(prefix+"extension"));
        address.setApartment(resultSet.getString(prefix+"apartment"));
        adult.setAddress(address);
        University university = new University(resultSet.getLong(prefix+"university_id"),"");
        adult.setUniversity(university);
        adult.setStudentId(resultSet.getString(prefix+"student_id"));
        return adult;
    }

    private void fillWedding(ResultSet resultSet, StudentOrder studentOrder) throws SQLException {
        studentOrder.setMarriageCertificate(resultSet.getString("certificate_id"));
        studentOrder.setMarriageDate(resultSet.getDate("marriage_date").toLocalDate());
        Long rsOffId = resultSet.getLong("register_office_id");
        String rOfficeAreaId = resultSet.getString("r_office_area_id");
        String rOfficeName = resultSet.getString("r_office_name");
        RegisterOffice registerOffice = new RegisterOffice(rsOffId,rOfficeAreaId,rOfficeName);
        studentOrder.setMarriageOffice(registerOffice);
    }

    private void fillStudentOrder(ResultSet resultSet, StudentOrder studentOrder) throws SQLException {
        studentOrder.setStudentOrderID(resultSet.getLong("student_order_id"));
        studentOrder.setStudentOrderStatus(StudentOrderStatus
                .getStatusById(resultSet.getInt("student_order_status")));
        studentOrder.setStudentOrderDate(resultSet.getTimestamp("student_order_date").toLocalDateTime());

    }

    private void saveChildToStudentOrder(Connection connection, StudentOrder studentOrder,Long soId){
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CHILD)){
            for(Child child:studentOrder.getChild()){
                preparedStatement.setLong(1,soId);
                setParamsForChild(child,preparedStatement);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void setParamsForChild(Child child, PreparedStatement preparedStatement) throws SQLException {
        setParamsForPerson(child,2,preparedStatement);
        preparedStatement.setString(6, child.getCertificateNumber());
        preparedStatement.setDate(7,
                java.sql.Date.valueOf(child.getIssueDate()));
        preparedStatement.setLong(8, child.getIssuedDepartment().getOfficeId());
        setParamsForAddress(child,9,preparedStatement);
    }

    private void setParamsForAdult(Adult adult, int start, PreparedStatement preparedStatement) throws SQLException {
        setParamsForPerson(adult, start, preparedStatement);
        preparedStatement.setString(start+4, adult.getPassportSeria());
        preparedStatement.setString(start+5, adult.getPassportNumber());
        preparedStatement.setDate(start+6,
                java.sql.Date.valueOf(adult.getIssueDate()));
        preparedStatement.setLong(start+7, adult.getIssueDepartment().getOfficeId());
        setParamsForAddress(adult, start+8, preparedStatement);
        preparedStatement.setLong(start+13,adult.getUniversity().getUniversityId());
        preparedStatement.setString(start+14,adult.getStudentId());
    }

    private void setParamsForAddress(Person person, int start, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(start, person.getAddress().getPostcode());
        preparedStatement.setLong(start + 1, person.getAddress().getStreet().getStreetCode());
        preparedStatement.setString(start + 2, person.getAddress().getBuilding());
        preparedStatement.setString(start + 3, person.getAddress().getExtension());
        preparedStatement.setString(start + 4, person.getAddress().getApartment());
    }

    private void setParamsForPerson(Person person, int start, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(start, person.getSurName());
        preparedStatement.setString(start + 1, person.getGivenName());
        preparedStatement.setString(start + 2, person.getPatronymic());
        preparedStatement.setDate(start +3,
                java.sql.Date.valueOf(person.getDateOfBirth()));
    }
}

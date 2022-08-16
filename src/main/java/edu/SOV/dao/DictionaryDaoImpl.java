package edu.SOV.dao;

import edu.SOV.Config.Config;
import edu.SOV.Exeption.DaoException;
import edu.SOV.domain.CountryArea;
import edu.SOV.domain.PassportOffice;
import edu.SOV.domain.RegisterOffice;
import edu.SOV.domain.Street;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DictionaryDaoImpl implements DictionaryDao{
    private static final String GET_STREET ="SELECT street_code, street_name FROM jc_streets\n" +
            "WHERE UPPER(street_name) LIKE UPPER (?)";
    private static final String GET_PASSPORT_OFFICE ="SELECT * FROM jc_passport_office\n" +
            "WHERE p_office_area_id = ?";
    private static final String GET_REGISTER_OFFICE ="SELECT * FROM jc_register_office\n" +
            "WHERE r_office_area_id = ?";
    private static final String GET_AREA ="SELECT * FROM jc_country_struct\n" +
            "WHERE area_id LIKE ? AND area_id NOT LIKE ?";
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
    private Connection getConnectionWEB(){
//        Class.forName("software.aws.rds.jdbc.mysql.Driver");
        Connection con1 = null;
//        Statement statement1 = null;
        try {
            con1 = DriverManager.getConnection(
                    "jdbc:mysql://vh342.timeweb.ru:3306/ck33360_jc",
                    "ck33360_jc",
                    "Qq3969471"
            );
//            statement1 = con1.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        ResultSet rs1 = statement1.executeQuery("SELECT * FROM test");
//        while (rs1.next()){
//            System.out.println(rs1.getLong(1)+" : "+rs1.getString(2));
//        }
        return con1;
    }
    public List<Street> findStreets(String pattern) throws DaoException{
        List<Street> streetList = new ArrayList<>();

        try (Connection connection = getConnectionLocal();
             PreparedStatement statement = connection.prepareStatement(GET_STREET)) {
            statement.setString(1,"%"+pattern+"%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getLong("street_code")+" : "+
                        resultSet.getString("street_name"));
                Street street = new Street(resultSet.getLong("street_code"),
                        resultSet.getString("street_name"));
                streetList.add(street);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return streetList;
    }

    @Override
    public List<PassportOffice> findPassportOffices(String areaId) throws DaoException {
        List<PassportOffice> passportOfficeList = new ArrayList<>();

        try (Connection connection = getConnectionLocal();
             PreparedStatement statement = connection.prepareStatement(GET_PASSPORT_OFFICE)) {
            statement.setString(1,areaId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getString("p_office_area_id")+" : "+
                        resultSet.getString("p_office_name"));
                PassportOffice passportOffice = new PassportOffice(
                        resultSet.getLong("p_office_id"),
                        resultSet.getString("p_office_area_id"),
                        resultSet.getString("p_office_name"));
                passportOfficeList.add(passportOffice);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return passportOfficeList;
    }

    @Override
    public List<RegisterOffice> findRegisterOffices(String areaId) throws DaoException {
        List<RegisterOffice> registerOffices = new ArrayList<>();

        try (Connection connection = getConnectionLocal();
             PreparedStatement statement = connection.prepareStatement(GET_REGISTER_OFFICE)) {
            statement.setString(1,areaId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getString("r_office_area_id")+" : "+
                        resultSet.getString("r_office_name"));
                RegisterOffice registerOffice = new RegisterOffice(
                        resultSet.getLong("r_office_id"),
                        resultSet.getString("r_office_area_id"),
                        resultSet.getString("r_office_name"));
                registerOffices.add(registerOffice);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return registerOffices;
    }
    private String buildParam(String areaId) throws SQLException {
        String result = "";
        if(areaId==null||areaId.trim().isEmpty()){
            return "__0000000000";
        } else if (areaId.endsWith("0000000000")){
            return areaId.substring(0,2)+"___0000000";
        } else if (areaId.endsWith("0000000")) {
            return areaId.substring(0,5)+"___0000";
        } else if (areaId.endsWith("0000")){
            return areaId.substring(0,8)+"____";
        }
        throw new SQLException("Invalid parameter area_id : " + areaId);
    }
    @Override
    public List<CountryArea> findCountryAreas(String areaId) throws DaoException {
        List<CountryArea> countryAreaList = new ArrayList<>();

        try (Connection connection = getConnectionLocal();
             PreparedStatement statement = connection.prepareStatement(GET_AREA)) {
            String param1 = buildParam(areaId);
            String param2 = areaId;
            statement.setString(1,param1);
            statement.setString(2,param2);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
//                System.out.println(resultSet.getString("area_id")+" : "+
//                        resultSet.getString("area_name"));
                CountryArea countryArea = new CountryArea(
                        resultSet.getString("area_id"),
                        resultSet.getString("area_name"));
                countryAreaList.add(countryArea);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return countryAreaList;
    }
}

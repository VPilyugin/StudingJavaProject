package edu.SOV.utils;

import edu.SOV.Exeption.DaoException;
import edu.SOV.dao.DictionaryDaoImpl;
import edu.SOV.dao.StudentOrderDao;
import edu.SOV.dao.StudentOrderDaoImpl;
import edu.SOV.domain.*;
import edu.SOV.domain.children.Child;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SaveStudentOrder {
    public static void main(String[] args) throws DaoException {
//        List<CountryArea> countryAreaList = new DictionaryDaoImpl().findCountryAreas("");
//        for (CountryArea countryArea:countryAreaList){
//            System.out.println(countryArea.getAreaId()+" : "+countryArea.getAreaName());
//        }
//        countryAreaList = new DictionaryDaoImpl().findCountryAreas("020000000000");
//        for (CountryArea countryArea:countryAreaList){
//            System.out.println(countryArea.getAreaId()+" : "+countryArea.getAreaName());
//        }
//        countryAreaList = new DictionaryDaoImpl().findCountryAreas("020010000000");
//        for (CountryArea countryArea:countryAreaList){
//            System.out.println(countryArea.getAreaId()+" : "+countryArea.getAreaName());
//        }
//        countryAreaList = new DictionaryDaoImpl().findCountryAreas("020010010000");
//        for (CountryArea countryArea:countryAreaList){
//            System.out.println(countryArea.getAreaId()+" : "+countryArea.getAreaName());
//        }
//        StudentOrder studentOrder = buildStudentOrder(10L);
        StudentOrderDao studentOrderDao = new StudentOrderDaoImpl();
//        Long id = studentOrderDao.saveStudentOrder(studentOrder);
//        System.out.println(id);
        List<StudentOrder> studentOrderList = studentOrderDao.getStudentsOrders();
        for (StudentOrder studentOrder1:studentOrderList){
            System.out.println(studentOrder1.getStudentOrderID());
        }
    }
    public static long saveStudentOrder(StudentOrder studentOrder){
        long answer = 199L;
        return answer;
    }

    public static StudentOrder buildStudentOrder(long id){
        StudentOrder studentOrder = new StudentOrder();
        studentOrder.setStudentOrderID(id);
        studentOrder.setMarriageCertificate("0123456789"+id);
        studentOrder.setMarriageOffice(new RegisterOffice(1L,"",""));
        studentOrder.setMarriageDate(LocalDate.of(2012,07,07));

        Street street = new Street(1L,"First street");
        Address address = new Address("195000",street,"12","h","112");

        Adult husband = new Adult("Andrii",
                "Petrov",
                "Viktorovich",
                LocalDate.of(1997,04,22));
        Adult wife = new Adult("Marta",
                "Petrova",
                "Viktorovna",
                LocalDate.of(2003,07,12));
        husband.setPassportNumber("1234567"+id);
        husband.setPassportSeria("1000"+id);
        husband.setIssueDate(LocalDate.of(2001,11,11));
        husband.setIssueDepartment(new PassportOffice(1L,"",""));
        husband.setStudentId("1000"+ThreadLocalRandom.current().nextInt(100));
        husband.setAddress(address);
        husband.setUniversity(new University(2L,"MGSU"));

        wife.setPassportNumber("1234567"+id);
        wife.setPassportSeria("1000"+id);
        wife.setIssueDate(LocalDate.of(2001,11,11));
        wife.setIssueDepartment(new PassportOffice(1L,"",""));
        wife.setStudentId("1000"+ThreadLocalRandom.current().nextInt(100));
        wife.setAddress(address);
        wife.setUniversity(new University(2L,"MGSU"));

        Child child1 = new Child("Bogdan","Petrov","Andreevich", LocalDate.of(2018,11,01));
        child1.setCertificateNumber("100"+id);
        child1.setIssueDate(LocalDate.of(2018,11,28));
        child1.setIssuedDepartment(new RegisterOffice(1L,"",""));
        child1.setAddress(address);
        studentOrder.addChild(child1);
        Child child2 = new Child("Alexa","Petrovna","Andreevna", LocalDate.of(2018,11,01));
        child2.setCertificateNumber("110"+id);
        child2.setIssueDate(LocalDate.of(2018,11,28));
        child2.setIssuedDepartment(new RegisterOffice(1L,"",""));
        child2.setAddress(address);
        studentOrder.setHusband(husband);
        studentOrder.setWife(wife);
        studentOrder.addChild(child2);

        return studentOrder;
    }
}

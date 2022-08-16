package edu.SOV;

import edu.SOV.Exeption.DaoException;
import edu.SOV.dao.DictionaryDaoImpl;
import edu.SOV.domain.*;
import edu.SOV.domain.children.AnswerChildren;
import edu.SOV.domain.register.AnswerCityRegister;
import edu.SOV.domain.student.AnswerStudents;
import edu.SOV.domain.wedding.AnswerWedding;
import edu.SOV.mail.MailSender;
import edu.SOV.utils.SaveStudentOrder;
import edu.SOV.validators.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class StudentOrderValidator {

    private ChildrenValidator childrenValidator;
    private CityRegisterValidator cityRegisterValidator;
    private StudentsValidator studentsValidator;
    private WeddingValidator weddingValidator;
    private MailSender mailSender;
    public StudentOrderValidator(){
        childrenValidator = new ChildrenValidator();
        cityRegisterValidator = new CityRegisterValidator();
        weddingValidator = new WeddingValidator();
        mailSender = new MailSender();
        studentsValidator = new StudentsValidator();
    }

    public static void main(String[] args) throws DaoException, SQLException {
        DictionaryDaoImpl directoryDao = new DictionaryDaoImpl();
//        for (int i = 0; i < 1000; i++) {
//            directoryDao.findStreets("th");
//        }
        directoryDao.findStreets("d");
        directoryDao.findPassportOffices("020010010001");
        directoryDao.findRegisterOffices("020010010002");

//        StudentOrderValidator studentOrderValidator = new StudentOrderValidator();
//        studentOrderValidator.checkAll();
    }
    public void checkAll(){
//        StudentOrder[] studentOrdersArray = readStudentOrders();
//        for(StudentOrder studentOrder:studentOrdersArray){
//            checkOneOrder(studentOrder);
//        }
    }
    public void checkOneOrder(StudentOrder studentOrder){

    }
    public List <StudentOrder> readStudentOrders(){
        List<StudentOrder> studentOrders = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            studentOrders.add(SaveStudentOrder.buildStudentOrder(i));
        }
        return studentOrders;
    }
    public AnswerChildren checkChildren(StudentOrder studentOrder){
        return childrenValidator.checkChildren(studentOrder);
    }
    public AnswerCityRegister checkCityRegister(StudentOrder studentOrder){
        return cityRegisterValidator.checkCityRegister(studentOrder);
    }
    public AnswerStudents checkStudent(StudentOrder studentOrder){
        return studentsValidator.checkStudent(studentOrder);
    }
    public AnswerWedding checkWedding(StudentOrder studentOrder){
        return weddingValidator.checkWedding(studentOrder);
    }
    public static StudentOrder readStudentOrder(){
        return null;
    }

    public void sendMail(StudentOrder studentOrder){
        mailSender.sendMail(studentOrder);
    }
}

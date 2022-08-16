package edu.SOV.dao;

import edu.SOV.Exeption.DaoException;
import edu.SOV.domain.StudentOrder;

import java.util.List;

public interface StudentOrderDao {
    Long saveStudentOrder(StudentOrder studentOrder) throws DaoException;
    List<StudentOrder> getStudentsOrders() throws DaoException;
}

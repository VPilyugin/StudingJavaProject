package edu.SOV.validators;

import edu.SOV.domain.student.AnswerStudents;
import edu.SOV.domain.StudentOrder;

public class StudentsValidator {
    public AnswerStudents checkStudent(StudentOrder studentOrder){
        System.out.println("checkStudent");
        AnswerStudents answerStudents = new AnswerStudents();
        return answerStudents;
    }
}

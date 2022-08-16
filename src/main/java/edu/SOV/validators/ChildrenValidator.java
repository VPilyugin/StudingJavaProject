package edu.SOV.validators;

import edu.SOV.domain.children.AnswerChildren;
import edu.SOV.domain.StudentOrder;
public class ChildrenValidator {

    public AnswerChildren checkChildren(StudentOrder studentOrder){
        System.out.println("checkChildren");
        AnswerChildren answerChildren = new AnswerChildren();
        return answerChildren;
    }
}

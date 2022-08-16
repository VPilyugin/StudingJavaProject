package edu.SOV.validators;

import edu.SOV.Exeption.CityRegisterException;
import edu.SOV.Exeption.TransportException;
import edu.SOV.domain.*;
import edu.SOV.domain.children.Child;
import edu.SOV.domain.register.AnswerCityRegister;
import edu.SOV.domain.register.AnswerCityRegisterItem;
import edu.SOV.domain.register.CityRegisterResponse;

import java.util.List;

public class CityRegisterValidator {
    private static final String NO_GRN = "NO GRN";
    CityRegisterChecker cityRegisterChecker;
    public CityRegisterValidator(){
        cityRegisterChecker = new RealCityRegisterChecker();
    }


    public AnswerCityRegister checkCityRegister(StudentOrder studentOrder) {
        AnswerCityRegister answerCityRegister = new AnswerCityRegister();
        answerCityRegister.add(checkPerson(studentOrder.getHusband()));
        answerCityRegister.add(checkPerson(studentOrder.getWife()));
        List<Child> childList = studentOrder.getChild();
        for (Child child : childList) {
            answerCityRegister.add(checkPerson(child));
        }

        return answerCityRegister;
    }
    private AnswerCityRegisterItem checkPerson(Person person){
        AnswerCityRegisterItem.CityError error  = null;
        AnswerCityRegisterItem.CityStatus status = null;
        try {
            CityRegisterResponse cityRegisterResponse = cityRegisterChecker.checkPerson(person);
            status = cityRegisterResponse.isExist()?
                    AnswerCityRegisterItem.CityStatus.YES :
                    AnswerCityRegisterItem.CityStatus.NO;

        } catch (CityRegisterException e) {
            e.printStackTrace();
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(e.getCode(),e.getMessage());
        } catch (TransportException e) {
            e.printStackTrace();
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(NO_GRN,e.getMessage());
        }


        return null;
    }
}

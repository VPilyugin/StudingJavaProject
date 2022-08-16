package edu.SOV.validators;

import edu.SOV.Exeption.CityRegisterException;
import edu.SOV.domain.Adult;
import edu.SOV.domain.register.CityRegisterResponse;
import edu.SOV.domain.Person;

public class FakeCityRegisterChecker implements CityRegisterChecker {
    public CityRegisterResponse checkPerson (Person person) throws CityRegisterException {
        if(person instanceof Adult){
            System.out.println("Adult");
        } else{

        }
        return null;
    }
}

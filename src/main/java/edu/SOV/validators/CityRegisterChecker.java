package edu.SOV.validators;

import edu.SOV.Exeption.CityRegisterException;
import edu.SOV.Exeption.TransportException;
import edu.SOV.domain.register.CityRegisterResponse;
import edu.SOV.domain.Person;

public interface CityRegisterChecker {
    public CityRegisterResponse checkPerson (Person person) throws CityRegisterException, TransportException;
}

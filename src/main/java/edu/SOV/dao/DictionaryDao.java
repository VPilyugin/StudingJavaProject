package edu.SOV.dao;

import edu.SOV.Exeption.DaoException;
import edu.SOV.domain.CountryArea;
import edu.SOV.domain.PassportOffice;
import edu.SOV.domain.RegisterOffice;
import edu.SOV.domain.Street;

import java.util.List;

public interface DictionaryDao {
    List<Street> findStreets(String pattern) throws DaoException;
    List<PassportOffice> findPassportOffices(String areaId) throws DaoException;
    List<RegisterOffice> findRegisterOffices(String areaId) throws DaoException;
    List<CountryArea> findCountryAreas(String areaId) throws DaoException;
}

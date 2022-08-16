package edu.SOV.domain.children;

import edu.SOV.domain.Person;
import edu.SOV.domain.RegisterOffice;

import java.time.LocalDate;

public class Child extends Person {
    public Child(String surName, String givenName, String patronymic, LocalDate dateOfBirth) {
        super(surName, givenName, patronymic, dateOfBirth);
    }
    private String certificateNumber;
    private LocalDate issueDate;
    private RegisterOffice issuedDepartment;

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public RegisterOffice getIssuedDepartment() {
        return issuedDepartment;
    }

    public void setIssuedDepartment(RegisterOffice issuedDepartment) {
        this.issuedDepartment = issuedDepartment;
    }
}

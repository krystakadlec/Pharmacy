package klient.model;

import java.util.List;

public class PatientDTO {
    private  Long id;

    private  String name;

    private  String surname;

    private  Integer birthYear;

    private  List<Long> prescriptionIds;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public List<Long> getPrescriptionIds() {
        return prescriptionIds;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public void setPrescriptionIds(List<Long> prescriptionIds) {
        this.prescriptionIds = prescriptionIds;
    }
}

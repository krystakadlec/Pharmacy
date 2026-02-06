package klient.model;

import java.util.List;

public class MedicineDTO {

    private Long id;

    private String name;

    private int price;

    private int dailyDosage;

    private List<Long> prescriptionIds;
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getDailyDosage() {
        return dailyDosage;
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

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDailyDosage(int dailyDosage) {
        this.dailyDosage = dailyDosage;
    }

    public void setPrescriptionIds(List<Long> prescriptionIds) {
        this.prescriptionIds = prescriptionIds;
    }

}

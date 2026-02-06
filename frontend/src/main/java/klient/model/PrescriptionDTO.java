package klient.model;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrescriptionDTO {
    private  Long id;

    private  int price;

    private  LocalDate date;

    private  boolean discountApply;

    private  Long prescribedPatientId;

    private  List<Long> prescribedMedicineIds;

    public Long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isDiscountApply() {
        return discountApply;
    }

    public Long getPrescribedPatientId() {
        return prescribedPatientId;
    }

    public List<Long> getPrescribedMedicineIds() {
        return prescribedMedicineIds;
    }

    // Setter methods
    public void setId(Long id) {
        this.id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDiscountApply(boolean discountApply) {
        this.discountApply = discountApply;
    }

    public void setPrescribedPatientId(Long prescribedPatientId) {
        this.prescribedPatientId = prescribedPatientId;
    }

    public void setPrescribedMedicineIds(List<Long> prescribedMedicineIds) {
        this.prescribedMedicineIds = prescribedMedicineIds;
    }

    public PrescriptionDTO(Long id, int price, LocalDate date, boolean discountApply, Long prescribedPatientId, List<Long> prescribedMedicineIds) {
        this.id = id;
        this.price = price;
        this.date = date;
        this.discountApply = discountApply;
        this.prescribedPatientId = prescribedPatientId;
        this.prescribedMedicineIds = prescribedMedicineIds;
    }

    public String toString() {
        return "Prescription{" +
                "id='" + id + '\'' +
                ", price='" + price + '\'' +
                ", date='" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + '\'' +
                ", discountApply='" + discountApply + '\'' +
                ", prescribedPatientId='" + prescribedPatientId + '\'' +
                ", prescribedMedicineIds='" + prescribedMedicineIds + '\'' +
                '}';
    }
}

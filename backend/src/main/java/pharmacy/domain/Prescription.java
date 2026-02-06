package pharmacy.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "prescription") //tabulka na jiny nazev a mozne dalsi informace
public class Prescription {
    @Id
    @Column(name = "id_prescription")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "price")
    private int price;

    @Column(name = "discount")
    private boolean discountApply;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Patient.class)
    @JoinColumn(name = "prescribed_patient")
    private Patient prescribedPatient;



    @ManyToMany(targetEntity = Medicine.class)
    @JoinTable(
            name = "medicine_prescription",
            joinColumns = @JoinColumn(name = "id_medicine"),
            inverseJoinColumns = @JoinColumn(name = "id_prescription")

    )
    private List <Medicine> prescribedMedicines;

    public Prescription(Long id, int price, LocalDate date, boolean discountApply, Patient patient, List<Medicine> prescribedMedicines ){
        this.id=id;
        this.price=price;
        this.date=date;
        this.discountApply = discountApply;
        this.prescribedPatient=patient;
        this.prescribedMedicines = prescribedMedicines;

    }

    public Prescription() {}



}


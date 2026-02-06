package pharmacy.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "patient")
public class Patient {
    @Id
    @Column(name = "id_patient")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "surname")
    @NotBlank
    private String surname;

    @Column(name = "birthyear")
    @NotNull
    private Integer birthYear;

    @OneToMany(targetEntity = Prescription.class, mappedBy = "prescribedPatient", fetch = FetchType.LAZY, orphanRemoval = true)
    private List <Prescription> prescriptions;

    public Patient(Long id, String name, String surname, Integer birthYear, List<Prescription> prescriptions){
        this.id=id;
        this.name=name;
        this.surname=surname;
        this.birthYear=birthYear;
        this.prescriptions=prescriptions;
    }

    public Patient() {}

    @Override //override equals kvuli testu
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Patient other = (Patient) obj;
        return id != null && id.equals(other.id) &&
                name != null && name.equals(other.name) &&
                Objects.equals(surname, other.surname) &&
                Objects.equals(birthYear, other.birthYear);
    }

}

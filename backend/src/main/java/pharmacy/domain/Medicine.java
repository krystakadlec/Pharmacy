package pharmacy.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Entity
@Getter
@Setter
@Table(name = "medicine" )
public class Medicine {
    @Id
    @Column(name = "id_medicine")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "price")
    @NotNull
    private int price;

    @Column(name = "daily_dosage")
    @NotNull
    private int dailyDosage;



    @ManyToMany(mappedBy = "prescribedMedicines")
    private List <Prescription> prescriptions;

    public Medicine(Long id, String name, int price, int dailyDosage, List<Prescription> prescriptions){
        this.id=id;
        this.name=name;
        this.price=price;
        this.dailyDosage=dailyDosage;
        this.prescriptions = prescriptions;
    }

    public Medicine() {}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Medicine other = (Medicine) obj;
        return id != null && id.equals(other.id) &&
                name != null && name.equals(other.name) &&
                price == other.price &&
                dailyDosage == other.dailyDosage;
    }
}


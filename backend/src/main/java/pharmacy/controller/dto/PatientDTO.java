package pharmacy.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PatientDTO {
    private final Long id;

    private final String name;

    private final String surname;

    private final Integer birthYear;

    private final List<Long> prescriptionIds;

}

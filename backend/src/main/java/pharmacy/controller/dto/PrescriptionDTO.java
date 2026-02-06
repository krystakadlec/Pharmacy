package pharmacy.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class PrescriptionDTO {
    private final Long id;

    private final int price;

    private final LocalDate date;

    private final boolean discountApply;

    private final Long prescribedPatientId;

    private final List<Long> prescribedMedicineIds;
}

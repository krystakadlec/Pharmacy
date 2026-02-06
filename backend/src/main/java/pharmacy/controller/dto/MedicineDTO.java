package pharmacy.controller.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class MedicineDTO {

    private final Long id;

    private final String name;

    private final int price;

    private final int dailyDosage;

    private final List<Long> prescriptionIds;
}

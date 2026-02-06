package pharmacy.controller.converter;

import pharmacy.business.PrescriptionServiceInterface;
import pharmacy.controller.dto.MedicineDTO;
import pharmacy.domain.Medicine;
import pharmacy.domain.Prescription;
import org.springframework.stereotype.Component;

@Component
public class MedicineDTOConverter implements DTOConverter<MedicineDTO, Medicine>{

    private final PrescriptionServiceInterface prescriptionService;

    public MedicineDTOConverter(PrescriptionServiceInterface prescriptionService){
        this.prescriptionService = prescriptionService;
    }

    @Override
    public MedicineDTO toDTO(Medicine m){
        return new MedicineDTO(
                m.getId(),
                m.getName(),
                m.getPrice(),
                m.getDailyDosage(),
                m.getPrescriptions().stream().map(Prescription::getId).toList()
        );
    }

    @Override
    public Medicine toEntity(MedicineDTO dto){
        return new Medicine(
                dto.getId(),
                dto.getName(),
                dto.getPrice(),
                dto.getDailyDosage(),
                dto.getPrescriptionIds().stream().map(prescriptionService::getPrescriptionById).toList()
        );
    }


}
package pharmacy.controller.converter;

import pharmacy.business.MedicineServiceInterface;
import pharmacy.business.PatientServiceInterface;
import pharmacy.controller.dto.PrescriptionDTO;
import pharmacy.domain.Medicine;
import pharmacy.domain.Prescription;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionDTOConverter implements DTOConverter<PrescriptionDTO, Prescription>{
    private final MedicineServiceInterface medicineService;
    private final PatientServiceInterface patientService;

    public PrescriptionDTOConverter(MedicineServiceInterface medicineService, PatientServiceInterface patientService){
        this.medicineService = medicineService;
        this.patientService = patientService;
    }

    @Override
    public PrescriptionDTO toDTO(Prescription p){
        return new PrescriptionDTO(
                p.getId(),
                p.getPrice(),
                p.getDate(),
                p.isDiscountApply(),
                p.getPrescribedPatient().getId(),
                p.getPrescribedMedicines().stream().map(Medicine::getId).toList()
        );
    }

    @Override
    public Prescription toEntity(PrescriptionDTO dto){
        return new Prescription(
                dto.getId(),
                dto.getPrice(),
                dto.getDate(),
                dto.isDiscountApply(),
                patientService.getPatientById(dto.getPrescribedPatientId()),
                dto.getPrescribedMedicineIds().stream().map(medicineService::getMedicineById).toList()
        );
    }
}

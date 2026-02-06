package pharmacy.controller.converter;

import pharmacy.business.PrescriptionServiceInterface;
import pharmacy.controller.dto.PatientDTO;
import pharmacy.domain.Patient;
import pharmacy.domain.Prescription;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class PatientDTOConverter implements DTOConverter<PatientDTO, Patient> {
    private final PrescriptionServiceInterface prescriptionService;
    public PatientDTOConverter(PrescriptionServiceInterface prescriptionService){
        this.prescriptionService = prescriptionService;
    }

    @Override
    public PatientDTO toDTO(Patient p) {
        if (p != null) {
            return new PatientDTO(
                    p.getId(),
                    p.getName(),
                    p.getSurname(),
                    p.getBirthYear(),
                    Optional.ofNullable(p.getPrescriptions())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(Prescription::getId)
                            .toList()
            );
        } else {
            throw new IllegalArgumentException("Patient entity is null");
        }
    }


    @Override
    public Patient toEntity(PatientDTO dto){
        return new Patient(
                dto.getId(),
                dto.getName(),
                dto.getSurname(),
                dto.getBirthYear(),
                Optional.ofNullable(dto.getPrescriptionIds()).orElse(Collections.emptyList())
                        .stream()
                        .map(prescriptionService::getPrescriptionById)
                        .toList()
        );
    }
}

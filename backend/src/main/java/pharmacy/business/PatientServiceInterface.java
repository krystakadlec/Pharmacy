package pharmacy.business;

import pharmacy.domain.Patient;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;


public interface PatientServiceInterface {
    Patient getPatientById(Long id) throws EntityNotFoundException;

    List<Patient> getPatients();

    Patient createPatient(Patient patient) throws IllegalArgumentException;

    Patient updatePatient(Patient patient) throws IllegalArgumentException;

    void deletePatient(Long id) throws EntityNotFoundException;
}
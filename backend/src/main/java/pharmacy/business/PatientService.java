package pharmacy.business;

import pharmacy.domain.Patient;
import pharmacy.persistent.JPAPatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientService implements PatientServiceInterface {

    final private JPAPatientRepository patientRepository;

    public PatientService(JPAPatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient getPatientById(Long id) throws EntityNotFoundException {
        return patientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Patient with given id" + id + " not found!"));
    }

    @Override
    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient createPatient(Patient patient) throws IllegalArgumentException {
        if (patient.getBirthYear() <= LocalDate.now().getYear()) {
            patient.setPrescriptions(Optional.ofNullable(patient.getPrescriptions()).orElse(Collections.emptyList()));
            return patientRepository.save(patient);
        }
        throw new IllegalArgumentException("Patient's birth date is not valid!");
    }

    @Override
    public Patient updatePatient(Patient patient) throws IllegalArgumentException {
        if (patientRepository.existsById(patient.getId())) {
            return patientRepository.save(patient);
        }
        throw new IllegalArgumentException("Patient with given id: " + patient.getId() + " does not exist!");
    }

    @Override
    public void deletePatient(Long id) throws EntityNotFoundException {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Patient with given id" + id + " not found!");
        }
    }

}

package pharmacy.business;

import pharmacy.domain.Medicine;
import pharmacy.domain.Prescription;
import pharmacy.persistent.JPAMedicineRepository;
import pharmacy.persistent.JPAPatientRepository;
import pharmacy.persistent.JPAPrescriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MedicineService implements MedicineServiceInterface{

    private JPAMedicineRepository medicineRepository;
    private JPAPatientRepository patientRepository;
    private JPAPrescriptionRepository prescriptionRepository;

    public MedicineService(JPAMedicineRepository medicineRepository, JPAPatientRepository patientRepository
            , JPAPrescriptionRepository prescriptionRepository) {

        this.medicineRepository = medicineRepository;
        this.patientRepository = patientRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public Medicine getMedicineById(Long id) throws EntityNotFoundException {
        return medicineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medicine with given id" + id + " not found!"));
    }

    @Override
    public List<Medicine> getMedicines() {
        return medicineRepository.findAll();
    }

    @Override
    public Medicine createMedicine(Medicine medicine) throws IllegalArgumentException {
        if(medicine.getDailyDosage() > 0){
            if(medicine.getPrice() >= 0) {
                return medicineRepository.save(medicine);
            }
            throw new IllegalArgumentException("Medicine's price is faulty!");
        }
        throw new IllegalArgumentException("Medicine's daily dosage is faulty!");
    }

    @Override
    public Medicine updateMedicine(Medicine medicine) throws IllegalArgumentException {
        if (medicineRepository.existsById(medicine.getId())) {
            if(medicine.getDailyDosage() > 0){
                if(medicine.getPrice() <= 0) {
                    return medicineRepository.save(medicine);
                }
                throw new IllegalArgumentException("Medicine's price is faulty!");
            }
            throw new IllegalArgumentException("Medicine's daily dosage is faulty!");
        }
        throw new IllegalArgumentException("Medicine with given id: " + medicine.getId() + " does not exist!");
    }

    @Override
    public void deleteMedicine(Long id) throws EntityNotFoundException {if (medicineRepository.existsById(id)) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medicine with id " + id + " not found"));

        for (Prescription prescription : medicine.getPrescriptions()) {
            prescription.getPrescribedMedicines().remove(medicine);
        }

        prescriptionRepository.saveAll(medicine.getPrescriptions());

        medicineRepository.deleteById(id);
    } else {
        throw new EntityNotFoundException("Medicine with given id: " + id + " does not exist!");
    }
    }

    @Override
    public List<Medicine> findAllMedicinesPatientUsed(Long patientId) throws EntityNotFoundException {
        if(patientRepository.existsById(patientId)){
            return medicineRepository.findAllMedicinesPatientUsed(patientId);
        } else {
            throw new EntityNotFoundException("Patient with given id: " + patientId + " does not exist!");
        }
    }
}
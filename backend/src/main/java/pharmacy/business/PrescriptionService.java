package pharmacy.business;


import pharmacy.domain.Medicine;
import pharmacy.domain.Prescription;
import pharmacy.persistent.JPAMedicineRepository;
import pharmacy.persistent.JPAPatientRepository;
import pharmacy.persistent.JPAPrescriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PrescriptionService implements PrescriptionServiceInterface {

    final private JPAPrescriptionRepository prescriptionRepository;
    final private JPAPatientRepository patientRepository;
    final private JPAMedicineRepository medicineRepository;

    public PrescriptionService(JPAPrescriptionRepository prescriptionRepository, JPAPatientRepository patientRepository, JPAMedicineRepository medicineRepository){
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.medicineRepository = medicineRepository;
    }
    @Override
    public Prescription getPrescriptionById(Long id) throws EntityNotFoundException {
        return prescriptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Prescription with given id: " + id + " not found!"));
    }

    @Override
    public List<Prescription> getPrescriptions() {
        return prescriptionRepository.findAll();
    }

    @Override
    public Prescription createPrescription(Prescription prescription) throws IllegalArgumentException {
        if(prescription.getDate().getYear() >= LocalDate.now().getYear() ){
            //je-li predpis rok stary, nebo na rok dopredu, zamita se
            return prescriptionRepository.save(prescription);
        }
        throw new IllegalArgumentException("Some arguments of the prescription are not legitimate!");
    }

    @Override
    public Prescription updatePrescription(Prescription prescription) throws IllegalArgumentException {
        if (prescriptionRepository.existsById(prescription.getId())) {
            return prescriptionRepository.save(prescription);
        }
        throw new IllegalArgumentException("Prescription with given id: " + prescription.getId() + " does not exist!");
    }

    @Override
    public void deletePrescription(Long id) throws EntityNotFoundException {
        if (prescriptionRepository.existsById(id)) {
            prescriptionRepository.deleteById(id);
        } else {
            System.out.println("Prescription not found with ID: " + id);
            throw new EntityNotFoundException("Prescription with given id: " + id + " does not exist!");
        }
    }

    @Override
    public void addMedicineToPrescription(Long medicineId, Long prescriptionId) throws EntityNotFoundException {
        if(prescriptionRepository.existsById(prescriptionId)){
            Prescription prescription = prescriptionRepository.getReferenceById(prescriptionId);
            if(medicineRepository.existsById(medicineId)){
                Medicine medicine = medicineRepository.getReferenceById(medicineId);
                prescription.getPrescribedMedicines().add(medicine);
                prescription.setPrice(prescription.getPrice() + medicine.getPrice());
                prescriptionRepository.save(prescription);
                return;
            }
            throw new EntityNotFoundException("Medicine with given id: " + medicineId + " does not exist!");
        }
        throw new EntityNotFoundException("Prescription with given id: " + prescriptionId + " does not exist!");
    }

    @Override
    public void removeMedicineFromPrescription(Long medicineId, Long prescriptionId) throws EntityNotFoundException, IllegalArgumentException {
        if(prescriptionRepository.existsById(prescriptionId)){
            if(medicineRepository.existsById(medicineId)){
                Prescription p = prescriptionRepository.getReferenceById(prescriptionId);
                Medicine m = medicineRepository.getReferenceById(medicineId);
                if(p.getPrescribedMedicines().contains(m)){
                    int removeCost=p.getPrice() - m.getPrice();
                    p.getPrescribedMedicines().remove(m);
                    p.setPrice(removeCost);
                    return;
                }
                throw new IllegalArgumentException("Prescription with id: " +prescriptionId+" does not contain medicine with given id: " +medicineId+" !");
            }
            throw new EntityNotFoundException("Medicine with given id: " + medicineId + " does not exist!");
        }
        throw new EntityNotFoundException("Prescription with given id: " + prescriptionId + " does not exist!");
    }

    @Override
    public Prescription addPatientToPrescription(Long patientId, Long prescriptionId) throws EntityNotFoundException, IllegalArgumentException {
        if (prescriptionRepository.existsById(prescriptionId)) {
            if (patientRepository.existsById(patientId)) {
                Prescription prescription = prescriptionRepository.getReferenceById(prescriptionId);
                if (prescription.getPrescribedPatient() == null) {
                    prescription.setPrescribedPatient(patientRepository.getReferenceById(patientId));
                    return prescription;
                }throw new IllegalArgumentException("Prescription with given id: " + prescriptionId + " already has a patient!");
            } throw new EntityNotFoundException("Patient with given id: " + patientId + " was not found!");
        } throw new EntityNotFoundException("Prescription with given id: " + prescriptionId + " was not found!");

    }

    @Override
    public Prescription removePatientFromPrescription(Long prescriptionId) throws EntityNotFoundException {
        if(prescriptionRepository.existsById(prescriptionId)) {
            Prescription p=prescriptionRepository.getReferenceById(prescriptionId);
            p.setPrescribedPatient(null);
            return p;
        } throw new EntityNotFoundException("Prescription with given id: "+prescriptionId+" was not found!");
    }

    @Override
    public Prescription applyDiscount(Prescription prep) throws EntityNotFoundException, IllegalArgumentException {
        if(prescriptionRepository.existsById(prep.getId())){
            if(!prep.isDiscountApply()){
                if(LocalDate.now().getYear() - prep.getPrescribedPatient().getBirthYear()  >= 60) {
                    double newPrice = prep.getPrice() * 0.8;
                    prep.setPrice((int) (newPrice)); //castuje k nule
                    prep.setDiscountApply(true);
                    return prescriptionRepository.save(prep);
                }   throw new IllegalArgumentException(" Given patient is not old enough for discount");
            } throw new IllegalArgumentException(" Discount has already been applied");
        } throw new EntityNotFoundException("Given patient was not found!");
    }

}

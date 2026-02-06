package pharmacy.business;

import pharmacy.domain.Prescription;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;


public interface PrescriptionServiceInterface {
    Prescription getPrescriptionById(Long id) throws EntityNotFoundException;

    List<Prescription> getPrescriptions();

    Prescription createPrescription(Prescription medicine) throws IllegalArgumentException;//prescription prijde jako telo requestu, ktere se zpracuje springem a preda

    Prescription updatePrescription(Prescription medicine) throws IllegalArgumentException;

    void deletePrescription(Long id) throws EntityNotFoundException;

    void addMedicineToPrescription (Long medicineId, Long prescriptionId) throws EntityNotFoundException;

    void removeMedicineFromPrescription (Long medicineId, Long prescriptionId) throws EntityNotFoundException, IllegalArgumentException;

    Prescription addPatientToPrescription (Long patientId, Long prescriptionId) throws EntityNotFoundException, IllegalArgumentException;
    Prescription removePatientFromPrescription (Long prescriptionId) throws EntityNotFoundException;

    Prescription applyDiscount (Prescription prep) throws EntityNotFoundException, IllegalArgumentException;
}
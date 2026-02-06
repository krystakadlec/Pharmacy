package pharmacy.business;

import pharmacy.domain.Medicine;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;


public interface MedicineServiceInterface {
    public Medicine getMedicineById(Long id) throws EntityNotFoundException;

    public List<Medicine> getMedicines();

    public Medicine createMedicine(Medicine medicine) throws IllegalArgumentException;

    public Medicine updateMedicine(Medicine medicine) throws IllegalArgumentException;

    public void deleteMedicine(Long id) throws EntityNotFoundException;

    public List<Medicine> findAllMedicinesPatientUsed (Long patientId) throws EntityNotFoundException;
}

package pharmacy.service;

import pharmacy.business.PrescriptionService;
import pharmacy.domain.Medicine;
import pharmacy.domain.Patient;
import pharmacy.domain.Prescription;
import pharmacy.persistent.JPAMedicineRepository;
import pharmacy.persistent.JPAPatientRepository;
import pharmacy.persistent.JPAPrescriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.ArrayList;
@SpringBootTest
public class PrescriptionIntegrationTest {
    @Autowired
    private JPAPatientRepository patientRepository;
    @Autowired
    private JPAPrescriptionRepository prescriptionRepository;
    @Autowired
    private JPAMedicineRepository medicineRepository;
    @Autowired
    private PrescriptionService prescriptionService;

    Patient patient = new Patient();

    @BeforeEach
    void setUp(){
        patientRepository.deleteAll();
        prescriptionRepository.deleteAll();
        medicineRepository.deleteAll();
        patient.setId(1L);
        patient.setName("Hubert");
        patient.setSurname("Hubicka");
        patient.setBirthYear(1999);
        patient.setPrescriptions(new ArrayList<>());
        patientRepository.save(patient);

        Prescription prescription = new Prescription();
        prescription.setId(1L);
        prescription.setPrice(0);
        prescription.setDate(LocalDate.of(2024,1,1));
        prescription.setDiscountApply(false);
        prescription.setPrescribedMedicines(new ArrayList<>());


        prescription.setPrescribedPatient(patient);
        patient.getPrescriptions().add(prescription);

        prescriptionRepository.save(prescription);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void patientAddMedicineToHisPrescription() {
        // Vytvoření léku
        Medicine medicine = new Medicine();
        medicine.setId(1L);
        medicine.setName("Aspirin");
        medicine.setPrice(250);
        medicine.setDailyDosage(2);
        medicine.setPrescriptions(new ArrayList<>());

        medicineRepository.save(medicine);

        // Načtení existujícího předpisu pomocí getOne
        Prescription prescription = prescriptionRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Prescription not found"));


        // Inicializace prázdného seznamu léků, pokud již není přiřazený
        prescription.setPrescribedMedicines(new ArrayList<>());

        // Ověření, že před přidáním není žádný lék na předpisu
        Assertions.assertEquals(0, prescriptionRepository.getReferenceById(prescription.getId()).getPrescribedMedicines().size());

        // Před testem
        System.out.println("Before adding medicine: " + prescription.getPrescribedMedicines());

        // Přidání léku do předpisu
        prescriptionService.addMedicineToPrescription(medicine.getId(), prescription.getId());

        // Po testu
        System.out.println("After adding medicine: " + prescriptionRepository.getReferenceById(prescription.getId()).getPrescribedMedicines());

        // Ověření, že lék byl přidán
        Assertions.assertFalse(prescriptionRepository.findById(prescription.getId()).isEmpty());
        Assertions.assertEquals(1, prescriptionRepository.getReferenceById(prescription.getId()).getPrescribedMedicines().size());
        Assertions.assertTrue(prescriptionRepository.getReferenceById(prescription.getId()).getPrescribedMedicines().contains(medicine));
    }


    @Test
    @Transactional
    @Rollback(true)
    public void patientDeleteNonExistingMedicineFromPrescription() {
        // Načtení existujícího předpisu
        Prescription prescription = prescriptionRepository.getReferenceById(1L);

        // Získání počtu léků na předpisu před pokusem o odstranění
        int medCnt = prescription.getPrescribedMedicines().size();

        // Ověření, že při pokusu o odstranění neexistujícího léku dojde k výjimce
        Assertions.assertThrows(EntityNotFoundException.class, () -> prescriptionService.removeMedicineFromPrescription(66L, prescription.getId()));

        // Ověření, že počet léků na předpisu se nezměnil
        Assertions.assertEquals(medCnt, prescription.getPrescribedMedicines().size());
    }

}
package pharmacy.service;


import pharmacy.business.PrescriptionService;
import pharmacy.domain.Medicine;
import pharmacy.domain.Patient;
import pharmacy.domain.Prescription;
import pharmacy.persistent.JPAMedicineRepository;
import pharmacy.persistent.JPAPatientRepository;
import pharmacy.persistent.JPAPrescriptionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootTest
public class PrescriptionServiceUnitTest {
    @MockBean
    private JPAPrescriptionRepository prescriptionRepository;

    @MockBean
    private JPAPatientRepository patientRepository;

    @MockBean
    private JPAMedicineRepository medicineRepository;

    @Autowired
    private PrescriptionService prescriptionService;


    @Test
    void testAddMedicinesToPrescription() {

        Prescription mockPrescription = new Prescription();

        mockPrescription.setId(1L);
        mockPrescription.setPrice(0);
        mockPrescription.setDate(LocalDate.of(2024,1,1));
        mockPrescription.setPrescribedPatient(null);
        mockPrescription.setPrescribedMedicines(new ArrayList<>());

        Medicine mockMedicine1 = new Medicine();
        mockMedicine1.setId(1L);
        mockMedicine1.setName("Ibalgin");
        mockMedicine1.setDailyDosage(2);
        mockMedicine1.setPrice(50);

        Medicine mockMedicine2 = new Medicine();
        mockMedicine2.setId(2L);
        mockMedicine2.setName("Martanci");
        mockMedicine2.setDailyDosage(6);
        mockMedicine2.setPrice(300);

        Mockito.when(prescriptionRepository.existsById(1L)).thenReturn(true);
        Mockito.when(medicineRepository.existsById(1L)).thenReturn(true);
        Mockito.when(medicineRepository.existsById(2L)).thenReturn(true);
        Mockito.when(prescriptionRepository.getReferenceById(1L)).thenReturn(mockPrescription);
        Mockito.when(medicineRepository.getReferenceById(1L)).thenReturn(mockMedicine1);
        Mockito.when(medicineRepository.getReferenceById(2L)).thenReturn(mockMedicine2);

        // Test
        prescriptionService.addMedicineToPrescription(mockMedicine1.getId(),mockPrescription.getId());
        prescriptionService.addMedicineToPrescription(mockMedicine2.getId(),mockPrescription.getId());

        // Verify and Assert
        Assertions.assertTrue(mockPrescription.getPrescribedMedicines().contains(mockMedicine1));
        Assertions.assertTrue(mockPrescription.getPrescribedMedicines().contains(mockMedicine2));
        Assertions.assertEquals(2, mockPrescription.getPrescribedMedicines().size());
        Assertions.assertEquals(350, mockPrescription.getPrice());
    }

    @Test
    void testAddMultiplePatientsToPrescription() {

        Prescription prescription = new Prescription();

        prescription.setId(2L);
        prescription.setPrice(0);
        prescription.setDate(LocalDate.of(2024,1,1));
        prescription.setPrescribedPatient(null);
        prescription.setPrescribedMedicines(new ArrayList<>());

        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setName("Ferda");
        patient1.setSurname("Mravenec");
        patient1.setBirthYear(1933);
        patient1.setPrescriptions(new ArrayList<>());

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Brouk");
        patient2.setSurname("Pytlik");
        patient2.setBirthYear(1934);
        patient1.setPrescriptions(new ArrayList<>());

        Mockito.when(prescriptionRepository.existsById(2L)).thenReturn(true);
        Mockito.when(patientRepository.existsById(1L)).thenReturn(true);
        Mockito.when(patientRepository.existsById(2L)).thenReturn(true);
        Mockito.when(prescriptionRepository.getReferenceById(prescription.getId())).thenReturn(prescription);
        Mockito.when(patientRepository.getReferenceById(patient1.getId())).thenReturn(patient1);
        Mockito.when(patientRepository.getReferenceById(patient2.getId())).thenReturn(patient2);
        Assertions.assertEquals(prescription.getId(), 2L);

        prescriptionService.addPatientToPrescription(patient1.getId(), prescription.getId());
        Assertions.assertEquals(prescription.getPrescribedPatient(), patient1);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> prescriptionService.addPatientToPrescription(patient2.getId(), prescription.getId()));

        Assertions.assertEquals("Prescription with given id: 2 already has a patient!", exception.getMessage());
        Assertions.assertEquals(prescription.getPrescribedPatient(), patient1);
        Assertions.assertNotEquals(prescription.getPrescribedPatient(), patient2);

        Mockito.verify(prescriptionRepository, Mockito.times(2)).existsById(prescription.getId());
        Mockito.verify(patientRepository, Mockito.times(1)).getReferenceById(patient1.getId());
        Mockito.verify(patientRepository, Mockito.times(1)).existsById(patient1.getId());
        Mockito.verify(prescriptionRepository, Mockito.times(2)).getReferenceById(prescription.getId());
        Mockito.verifyNoMoreInteractions(prescriptionRepository);
    }
}
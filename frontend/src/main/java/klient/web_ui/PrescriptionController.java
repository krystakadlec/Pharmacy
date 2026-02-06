package klient.web_ui;

import klient.service.MedicineService;
import klient.service.PatientService;
import klient.service.PrescriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/prescriptions")
public class PrescriptionController {
    private PrescriptionService prescriptionService;
    private MedicineService medicineService;
    private PatientService patientService;

    public PrescriptionController(PrescriptionService prescriptionService, MedicineService medicineService, PatientService patientService){
        this.prescriptionService=prescriptionService;
        this.medicineService=medicineService;
        this.patientService=patientService;
    }

    @GetMapping
    public String listPrescriptions(Model model, @RequestParam Long CurrentPatientId){
        var allPrescriptions= prescriptionService.getPrescriptions();
        model.addAttribute("prescriptions", allPrescriptions);
        var allMedicines= medicineService.getMedicines();
        model.addAttribute("medicines", allMedicines);
        var allPatients= patientService.getPatients();
        model.addAttribute("patients", allPatients);
        var currentPatient= patientService.getPatientById(CurrentPatientId);
        model.addAttribute("currentPatient", currentPatient.orElse(null));


        return "prescriptions";
    }
}

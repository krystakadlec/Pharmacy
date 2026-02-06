package klient.web_ui;


import klient.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {
    private PatientService patientService;

    public IndexController(PatientService patientService){
        this.patientService=patientService;
    }

    @GetMapping
    public String index(Model model){
        var allPatients = patientService.getPatients();
        model.addAttribute("allPatients", allPatients);

        return "index";
    }
}

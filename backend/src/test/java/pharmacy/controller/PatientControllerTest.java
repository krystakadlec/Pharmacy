package pharmacy.controller;



import pharmacy.business.MedicineServiceInterface;
import pharmacy.business.PatientService;
import pharmacy.business.PrescriptionServiceInterface;
import pharmacy.controller.converter.PatientDTOConverter;
import pharmacy.controller.dto.PatientDTO;
import pharmacy.domain.Patient;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

@ComponentScan(basePackages = "cz.cvut.fit.tjv.pharmacy")
@ExtendWith(SpringExtension.class)
@WebMvcTest(PatientController.class)
class PatientControllerTest{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;
    @MockBean
    private MedicineServiceInterface medicineService;
    @MockBean
    private PrescriptionServiceInterface prescriptionService;

    @Test
    void create() throws Exception {
        // Create a PatientDTO for input in the request
        PatientDTO patientDTO = new PatientDTO(42L, "John", "Grasshopper", 2000, Collections.emptyList());
        Patient patient = new Patient(42L, "John", "Grasshopper", 2000, Collections.emptyList());
        PatientDTOConverter patientDTOConverter = new PatientDTOConverter(prescriptionService);
        // Mock the service method
        Mockito.when(patientService.createPatient(patient)).thenReturn(patient);

        // Perform the mockMvc request
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/rest/api/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\" : 42,\"name\" : \"John\",\"surname\" : \"Grasshopper\",\"birthYear\" : 2000, \"prescriptionIds\": []}")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id", Matchers.is(42))
        );
    }



}
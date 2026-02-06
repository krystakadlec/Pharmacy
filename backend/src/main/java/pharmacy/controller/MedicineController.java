package pharmacy.controller;

import pharmacy.business.MedicineServiceInterface;
import pharmacy.business.PatientServiceInterface;
import pharmacy.business.PrescriptionServiceInterface;
import pharmacy.controller.converter.DTOConverter;
import pharmacy.controller.dto.MedicineDTO;
import pharmacy.controller.dto.PatientDTO;
import pharmacy.controller.dto.PrescriptionDTO;
import pharmacy.domain.Medicine;
import pharmacy.domain.Patient;
import pharmacy.domain.Prescription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping("/rest/api/medicine")
@RestController
public class MedicineController {

    private final PatientServiceInterface patientService;
    private final MedicineServiceInterface medicineService;
    private final PrescriptionServiceInterface prescriptionService;
    private final DTOConverter<PatientDTO, Patient> patientDTOConverter;
    private final DTOConverter<MedicineDTO, Medicine> medicineDTOConverter;
    private final DTOConverter<PrescriptionDTO, Prescription> prescriptionDTOConverter;



    public MedicineController(PatientServiceInterface patientService, MedicineServiceInterface medicineService,
                              PrescriptionServiceInterface prescriptionService, DTOConverter<PatientDTO, Patient> patientDTOConverter,
                              DTOConverter<MedicineDTO, Medicine> medicineDTOConverter, DTOConverter<PrescriptionDTO, Prescription> prescriptionDTOConverter) {

        this.patientService = patientService;
        this.medicineService = medicineService;
        this.prescriptionService = prescriptionService;
        this.patientDTOConverter = patientDTOConverter;
        this.medicineDTOConverter = medicineDTOConverter;
        this.prescriptionDTOConverter=prescriptionDTOConverter;

    }

    @Operation(summary = "Finds and returns all medicines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all medicines"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping
    public List<MedicineDTO> getMedicines(){
        return medicineService.getMedicines().stream().map(medicineDTOConverter::toDTO).toList();
    }

    @Operation(summary = "Finds and returns a medicine by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the medicine by ID"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Medicine not found")
    })
    @GetMapping(path = "{id}")
    public MedicineDTO getMedicine(@PathVariable("id") Long id){
        return medicineDTOConverter.toDTO(medicineService.getMedicineById(id));
    }

    @Operation(summary = "Finds and returns all prescriptions for a medicine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all prescriptions for the medicine"),
            @ApiResponse(responseCode = "400", description = "Invalid medicine ID supplied"),
            @ApiResponse(responseCode = "404", description = "Medicine not found")
    })
    @GetMapping(path = "{id}/prescriptions")
    public List<PrescriptionDTO> getMedicinesPrescriptions(@PathVariable("id") Long id){
        return medicineService.getMedicineById(id).getPrescriptions().stream().map(prescriptionDTOConverter::toDTO).toList();
    }

    @Operation(summary = "Creates a new medicine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicine successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied")
    })
    @PostMapping
    public MedicineDTO createMedicine(@RequestBody MedicineDTO m) {
        return medicineDTOConverter.toDTO(medicineService.createMedicine(medicineDTOConverter.toEntity(m)));
    }

    @Operation(summary = "Updates an existing medicine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicine successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Medicine not found")
    })
    @PutMapping(path = "{id}")
    public MedicineDTO updateMedicine(@PathVariable Long id, @RequestBody MedicineDTO m){
        Medicine medicine=medicineDTOConverter.toEntity(m);
        medicine.setId(id);
        return medicineDTOConverter.toDTO(medicineService.updateMedicine(medicine));
    }

    @Operation(summary = "Deletes a medicine by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicine successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Medicine not found")
    })
    @DeleteMapping(path = "{id}")
    public void deleteMedicine(@PathVariable Long id){
        medicineService.deleteMedicine(id);
    }

    @Operation(summary = "Adds a prescription to a medicine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription successfully added to the medicine"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Medicine or prescription not found")
    })
    @PostMapping(path = "{id}/prescriptions")
    public List<PrescriptionDTO> addPrescription(@PathVariable("id") Long medicineId, @RequestBody Long preId){
        prescriptionService.addMedicineToPrescription(medicineId, preId);
        return medicineService.getMedicineById(medicineId).getPrescriptions().stream().map(prescriptionDTOConverter::toDTO).toList();
    }

    @Operation(summary = "Removes a prescription from a medicine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription successfully removed from the medicine"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Medicine or prescription not found")
    })
    @DeleteMapping(path = "{id}/prescriptions/{prescriptionId}")
    public List<PrescriptionDTO> removePrescription(@PathVariable("id") Long medicineId, @PathVariable("prescriptionId") Long preId) {
        prescriptionService.removeMedicineFromPrescription(medicineId, preId);
        return medicineService.getMedicineById(medicineId).getPrescriptions().stream().map(prescriptionDTOConverter::toDTO).toList();
    }

}

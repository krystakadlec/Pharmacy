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
@RequestMapping("/rest/api/prescription")
@RestController
public class PrescriptionController {
    private final PatientServiceInterface patientService;
    private final MedicineServiceInterface medicineService;
    private final PrescriptionServiceInterface prescriptionService;
    private final DTOConverter<PatientDTO, Patient> patientDTOConverter;
    private final DTOConverter<MedicineDTO, Medicine> medicineDTOConverter;
    private final DTOConverter<PrescriptionDTO, Prescription> prescriptionDTOConverter;



    public PrescriptionController(PatientServiceInterface patientService, MedicineServiceInterface medicineService,
                                  PrescriptionServiceInterface prescriptionService, DTOConverter<PatientDTO, Patient> patientDTOConverter,
                                  DTOConverter<MedicineDTO, Medicine> medicineDTOConverter, DTOConverter<PrescriptionDTO, Prescription> prescriptionDTOConverter) {

        this.patientService = patientService;
        this.medicineService = medicineService;
        this.prescriptionService = prescriptionService;
        this.patientDTOConverter = patientDTOConverter;
        this.medicineDTOConverter = medicineDTOConverter;
        this.prescriptionDTOConverter=prescriptionDTOConverter;

    }

    @Operation(summary = "Finds and returns all prescriptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all prescriptions"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping
    public List<PrescriptionDTO> getPrescriptions(){
        return prescriptionService.getPrescriptions().stream().map(prescriptionDTOConverter::toDTO).toList();
    }

    @Operation(summary = "Finds and returns a prescription by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the prescription by ID"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Prescription not found")
    })
    @GetMapping(path = "{id}")
    public PrescriptionDTO getPrescription(@PathVariable("id") Long id){
        return prescriptionDTOConverter.toDTO(prescriptionService.getPrescriptionById(id));
    }

    @Operation(summary = "Finds and returns all medicines prescribed in a specific prescription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all medicines prescribed"),
            @ApiResponse(responseCode = "400", description = "Invalid prescription ID supplied"),
            @ApiResponse(responseCode = "404", description = "Prescription not found")
    })
    @GetMapping(path = "{id}/medicines")
    public List<MedicineDTO> getPrescriptionsMedicines(@PathVariable("id") Long id){
        return prescriptionService.getPrescriptionById(id).getPrescribedMedicines().stream().map(medicineDTOConverter::toDTO).toList();
    }

    @Operation(summary = "Finds and returns the patient who is prescribed the given prescription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the patient associated with the prescription"),
            @ApiResponse(responseCode = "400", description = "Invalid prescription ID supplied"),
            @ApiResponse(responseCode = "404", description = "Prescription not found")
    })
    @GetMapping(path = "{id}/patient")
    public PatientDTO getPrescriptionsPatient(@PathVariable("id") Long id){
        return patientDTOConverter.toDTO(prescriptionService.getPrescriptionById(id).getPrescribedPatient());
    }

    @Operation(summary = "Creates a new prescription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied")
    })
    @PostMapping
    public PrescriptionDTO createPrescription(@RequestBody PrescriptionDTO p) {
        return prescriptionDTOConverter.toDTO(prescriptionService.createPrescription(prescriptionDTOConverter.toEntity(p)));
    }

    @Operation(summary = "Updates an existing prescription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Prescription not found")
    })
    @PutMapping(path = "{id}")
    public PrescriptionDTO updatePrescription(@PathVariable Long id, @RequestBody PrescriptionDTO p){
        Prescription prescription=prescriptionDTOConverter.toEntity(p);
        prescription.setId(id);
        return prescriptionDTOConverter.toDTO(prescriptionService.updatePrescription(prescription));
    }
    @Operation(summary = "Applies 20% discount to patients, which are 60 years old or older ")
    @ApiResponses( value  ={
            @ApiResponse(responseCode = "200", description = "Successfully applied discount"),
            @ApiResponse(responseCode = "400", description = "Given entity is not eligible for discount")
    })
    @PutMapping(path="{id}/discount")
    public PrescriptionDTO applyDiscount(@PathVariable Long id, @RequestBody PrescriptionDTO p){
        Prescription prescription=prescriptionDTOConverter.toEntity(p);
        prescriptionService.applyDiscount(prescription);
        return prescriptionDTOConverter.toDTO(prescriptionService.updatePrescription(prescription));
    }


    @Operation(summary = "Deletes a prescription by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Prescription not found")
    })
    @DeleteMapping(path = "{id}")
    public void deletePrescription(@PathVariable Long id){
        prescriptionService.deletePrescription(id);
    }

    @Operation(summary = "Adds a medicine to a prescription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicine successfully added to the prescription"),
            @ApiResponse(responseCode = "404", description = "Prescription or medicine not found")
    })
    @PostMapping(path = "{id}/medicines")
    public List<MedicineDTO> addMedicine(@PathVariable("id") Long prescriptionId, @RequestBody Long medId){
        prescriptionService.addMedicineToPrescription(medId, prescriptionId);
        return prescriptionService.getPrescriptionById(prescriptionId).getPrescribedMedicines().stream().map(medicineDTOConverter::toDTO).toList();
    }

    @Operation(summary = "Removes a medicine from a prescription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicine successfully removed from the prescription"),
            @ApiResponse(responseCode = "404", description = "Prescription or medicine not found")
    })
    @DeleteMapping(path = "{id}/medicines/{medicineId}")
    public List<MedicineDTO> removeMedicine(@PathVariable("id") Long prescriptionId, @PathVariable("medId") Long medId){
        prescriptionService.removeMedicineFromPrescription(medId, prescriptionId);
        return prescriptionService.getPrescriptionById(prescriptionId).getPrescribedMedicines().stream().map(medicineDTOConverter::toDTO).toList();
    }


}

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
@RequestMapping("/rest/api/patient")
@RestController
public class PatientController {

    private final PatientServiceInterface patientService;
    private final MedicineServiceInterface medicineService;
    private final PrescriptionServiceInterface prescriptionService;
    private final DTOConverter<PatientDTO, Patient> patientDTOConverter;
    private final DTOConverter<MedicineDTO, Medicine> medicineDTOConverter;
    private final DTOConverter<PrescriptionDTO, Prescription> prescriptionDTOConverter;



    public PatientController(PatientServiceInterface patientService, MedicineServiceInterface medicineService,
                             PrescriptionServiceInterface prescriptionService, DTOConverter<PatientDTO, Patient> patientDTOConverter,
                             DTOConverter<MedicineDTO, Medicine> medicineDTOConverter, DTOConverter<PrescriptionDTO, Prescription> prescriptionDTOConverter){

        this.patientService = patientService;
        this.medicineService = medicineService;
        this.prescriptionService = prescriptionService;
        this.patientDTOConverter=patientDTOConverter;
        this.medicineDTOConverter=medicineDTOConverter;
        this.prescriptionDTOConverter=prescriptionDTOConverter;
    }


    @Operation(summary = "Finds and returns all patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all patients"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping
    public List<PatientDTO> getPatients(){
        return patientService.getPatients().stream().map(patientDTOConverter::toDTO).toList();
    }

    @Operation(summary = "Finds and returns a patient by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the patient by ID"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping(path = "{id}")
    public PatientDTO getPatient(@PathVariable("id") Long id){
        return patientDTOConverter.toDTO(patientService.getPatientById(id));
    }

    @Operation(summary = "Finds and returns all prescriptions of a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all prescriptions of the patient"),
            @ApiResponse(responseCode = "400", description = "Invalid patient ID supplied"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping(path = "{id}/prescriptions")
    public List<PrescriptionDTO> getPatientsPrescriptions(@PathVariable("id") Long id){
        return patientService.getPatientById(id).getPrescriptions().stream().map(prescriptionDTOConverter::toDTO).toList();
    }

    @Operation(summary = "Creates a new patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied")
    })
    @PostMapping
    public PatientDTO createPatient(@RequestBody PatientDTO p) {
        return patientDTOConverter.toDTO(patientService.createPatient(patientDTOConverter.toEntity(p)));
    }

    @Operation(summary = "Updates an existing patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PutMapping(path = "{id}")
    public PatientDTO updatePatient(@PathVariable Long id, @RequestBody PatientDTO p){
        Patient patient=patientDTOConverter.toEntity(p);
        patient.setId(id);
        return patientDTOConverter.toDTO(patientService.updatePatient(patient));
    }

    @Operation(summary = "Deletes a patient by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @DeleteMapping(path = "{id}")
    public void deletePatient(@PathVariable Long id){
        patientService.deletePatient(id);
    }


    @Operation(summary = "Finds and returns all medicines, that are on the prescriptions registered by patient ")
    @ApiResponses( value  ={
            @ApiResponse(responseCode = "200", description = "Found all medicines used by patient"),
            @ApiResponse(responseCode = "400", description = "Given invalid id")
    })
    @GetMapping(path = "{id}/medicines")
    public List<MedicineDTO> findAllMedicinesPatientUsed( @PathVariable("id") Long patId ){
        return medicineService.findAllMedicinesPatientUsed(patId).stream().map(medicineDTOConverter::toDTO).toList();
    }

    @Operation(summary = "Adds a prescription to a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription successfully added to patient"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Patient or prescription not found")
    })
    @PostMapping(path = "{id}/prescriptions")
    public List<PrescriptionDTO> addPrescriptionToPatient(@PathVariable("id") Long patId, @RequestBody Long prescriptionId){
        prescriptionService.addPatientToPrescription(patId, prescriptionId);
        return patientService.getPatientById(patId).getPrescriptions().stream().map(prescriptionDTOConverter::toDTO).toList();
    }

    @Operation(summary = "Removes a prescription from a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription successfully removed from patient"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Patient or prescription not found")
    })
    @DeleteMapping(path = "{id}/prescriptions/{prescriptionId}")
    public List<PrescriptionDTO> removePrescriptionFromPatient(@PathVariable("id") Long patId, @PathVariable("prescriptionId") Long prescriptionId){
        prescriptionService.removePatientFromPrescription(prescriptionId);
        return patientService.getPatientById(patId).getPrescriptions().stream().map(prescriptionDTOConverter::toDTO).toList();
    }



}
package klient.service;

import klient.api_client.PatientClient;
import klient.model.PatientDTO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PatientService {
    private PatientClient patientClient;

    public PatientService(PatientClient patientClient){
        this.patientClient = patientClient;
    }

    public Optional<PatientDTO> getPatient(){
        return patientClient.getPatient();
    }

    public void setCurrentPatient(Long patientId){
        patientClient.setCurrentPatient(patientId);
    }

    public Collection<PatientDTO> getPatients(){
        return patientClient.getPatients();
    }

    public void createPatient(PatientDTO patient){
        patientClient.createPatient(patient);
    }

    public Optional<PatientDTO> getPatientById(Long currentPatientId) {
        return patientClient.getPatientById(currentPatientId);
    }
}

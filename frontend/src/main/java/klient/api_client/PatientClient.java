package klient.api_client;

import klient.model.PatientDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.UnknownContentTypeException;


import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Component
public class PatientClient {
    private String baseUrl;
    private RestClient patientRestClient;
    private RestClient currentPatientRestClient;

    public PatientClient(@Value("${api.url}") String baseUrl){
        this.baseUrl=baseUrl;
        patientRestClient = RestClient.create(baseUrl + "/patient");
    }

    public void setCurrentPatient(Long patientId) {
        this.currentPatientRestClient = RestClient.builder()
                .baseUrl(baseUrl + "/patient/{id}")
                .defaultUriVariables(Map.of("id", patientId))
                .build();
    }

    public Collection<PatientDTO> getPatients() {
        return Arrays.asList(patientRestClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(PatientDTO[].class)
                .getBody());
    }
    //??
    public Optional<PatientDTO> getPatient() {
        try {
            return Optional.of(
                    currentPatientRestClient.get()
                            .retrieve().toEntity(PatientDTO.class).getBody()
            );
        } catch (UnknownContentTypeException e) {
            return Optional.empty();
        }
    }

    public void createPatient(PatientDTO patient){
        patientRestClient.post()
                .body(patient)
                .retrieve()
                .toBodilessEntity();
    }

    public Optional<PatientDTO> getPatientById(Long currentPatientId) {

        PatientDTO patient = patientRestClient.get()
                .uri("/{id}", currentPatientId)
                .retrieve()
                .toEntity(PatientDTO.class)
                .getBody();

        return Optional.ofNullable(patient);

    }
}

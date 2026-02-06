package klient.api_client;

import klient.model.PrescriptionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Component
public class PrescriptionClient {
    private String baseUrl;
    private RestClient prescriptionRestClient;
    private RestClient currentPrescriptionRestClient;

    public PrescriptionClient(@Value("${api.url}") String baseUrl){
        this.baseUrl=baseUrl;
        prescriptionRestClient=RestClient.create(baseUrl + "/prescription");
    }

    public Optional<PrescriptionDTO> getPrescription(){
        try {
            return Optional.of(
                    currentPrescriptionRestClient.get()
                            .retrieve().toEntity(PrescriptionDTO.class).getBody()
            );
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    public Collection<PrescriptionDTO> getPrescriptions(){
        return  Arrays.asList(
                prescriptionRestClient.get()
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .toEntity(PrescriptionDTO[].class)
                        .getBody());
    }

    public void createPrescription(PrescriptionDTO prescription){
        prescriptionRestClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(prescription)
                .retrieve()
                .toBodilessEntity();
    }

}
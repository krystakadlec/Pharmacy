package klient.api_client;

import klient.model.MedicineDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Collection;

@Component
public class MedicineClient {
    private RestClient medicineRestClient;

    public MedicineClient(@Value("${api.url}") String baseUrl){
        medicineRestClient = RestClient.create(baseUrl + "/medicine");
    }

    public Collection<MedicineDTO> getMedicines(){
        return Arrays.asList(
                medicineRestClient.get()
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .toEntity(MedicineDTO[].class)
                        .getBody());
    }

    public void createMedicine(MedicineDTO medicine){//
        medicineRestClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(medicine)
                .retrieve()
                .toBodilessEntity();
    }
}

package klient.service;


import klient.api_client.MedicineClient;
import klient.model.MedicineDTO;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MedicineService {
    private MedicineClient medicineClient;

    public MedicineService(MedicineClient medicineClient){
        this.medicineClient=medicineClient;
    }

    public Collection<MedicineDTO> getMedicines(){
        return medicineClient.getMedicines();
    }

    public void createMedicine(MedicineDTO medicine){
        medicineClient.createMedicine(medicine);
    }
}

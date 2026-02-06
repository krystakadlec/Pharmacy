package klient.service;

import klient.api_client.PrescriptionClient;
import klient.model.PrescriptionDTO;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PrescriptionService {
    private PrescriptionClient prescriptionClient;

    public PrescriptionService(PrescriptionClient prescriptionClient){
        this.prescriptionClient = prescriptionClient;
    }

    public Collection<PrescriptionDTO> getPrescriptions(){
        return prescriptionClient.getPrescriptions();
    }

    public void createPrescription(PrescriptionDTO prescription){
        prescriptionClient.createPrescription(prescription);
    }
}

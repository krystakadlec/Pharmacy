package pharmacy.persistent;

import pharmacy.domain.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JPAMedicineRepository extends JpaRepository<Medicine, Long> {
    @Query(value = "SELECT DISTINCT m FROM Medicine m JOIN m.prescriptions p JOIN p.prescribedPatient pa WHERE pa.id = :patientId" )
    List<Medicine> findAllMedicinesPatientUsed   (@Param("patientId") Long pId);
}

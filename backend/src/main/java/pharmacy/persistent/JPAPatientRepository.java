package pharmacy.persistent;

import pharmacy.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JPAPatientRepository extends JpaRepository<Patient, Long> {
    public List<Patient> findPatientByName(String name);

}

package pharmacy.persistent;

import pharmacy.domain.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface JPAPrescriptionRepository extends JpaRepository<Prescription, Long> {

}

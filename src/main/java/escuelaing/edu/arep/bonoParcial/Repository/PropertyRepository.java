package escuelaing.edu.arep.bonoParcial.Repository;

import escuelaing.edu.arep.bonoParcial.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Long>{
    
}

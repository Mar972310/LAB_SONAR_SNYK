package escuelaing.edu.arep.bono_parcial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import escuelaing.edu.arep.bono_parcial.model.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Long>{
    
}

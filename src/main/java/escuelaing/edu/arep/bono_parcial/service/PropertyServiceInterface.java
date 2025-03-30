package escuelaing.edu.arep.bono_parcial.service;

import java.util.List;

import escuelaing.edu.arep.bono_parcial.dto.PropertyDTO;
import escuelaing.edu.arep.bono_parcial.exception.PropertyException;


public interface PropertyServiceInterface {

    PropertyDTO createProperty(PropertyDTO propertyDTO) throws PropertyException;

    List<PropertyDTO> getallProperties() ;

    PropertyDTO getProperty(Long id) throws PropertyException;

    PropertyDTO updateProperty(Long id , PropertyDTO propertyDTO) throws PropertyException;

    void deleteProperty(Long id) throws PropertyException;

} 

package escuelaing.edu.arep.bonoParcial.Service;

import java.util.List;

import escuelaing.edu.arep.bonoParcial.DTO.PropertyDTO;
import escuelaing.edu.arep.bonoParcial.Exception.PropertyException;


public interface PropertyServiceInterface {

    PropertyDTO createProperty(PropertyDTO propertyDTO) throws PropertyException;

    List<PropertyDTO> getallProperties() ;

    PropertyDTO getProperty(Long id) throws PropertyException;

    PropertyDTO updateProperty(Long id , PropertyDTO propertyDTO) throws PropertyException;

    void deleteProperty(Long id) throws PropertyException;


    
} 

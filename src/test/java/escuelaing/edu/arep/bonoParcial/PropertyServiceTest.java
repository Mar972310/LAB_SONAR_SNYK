package escuelaing.edu.arep.bonoParcial;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import escuelaing.edu.arep.bonoParcial.DTO.PropertyDTO;
import escuelaing.edu.arep.bonoParcial.Exception.PropertyException;
import escuelaing.edu.arep.bonoParcial.Repository.PropertyRepository;
import escuelaing.edu.arep.bonoParcial.Service.Impl.PropertyService;
import escuelaing.edu.arep.bonoParcial.model.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PropertyService propertyService;

    private Property property;
    private PropertyDTO propertyDTO;

    @BeforeEach
    void setUp() {
        property = new Property(1L, "123 Street", 250000, 120, "Nice house");
        propertyDTO = new PropertyDTO(1L, "123 Street", 250000, 120, "Nice house");
    }


    @Test
    void createProperty_ShouldReturnPropertyDTO_WhenSuccessful() throws PropertyException {
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        PropertyDTO result = propertyService.createProperty(propertyDTO);
        assertNotNull(result);
        assertEquals(property.getAddress(), result.getAddress());
        verify(propertyRepository, times(1)).save(any(Property.class));
    }

  
    @Test
    void createProperty_ShouldThrowException_WhenRepositoryFails() {
        when(propertyRepository.save(any(Property.class))).thenThrow(new RuntimeException());
        PropertyException exception = assertThrows(PropertyException.class, () -> {
            propertyService.createProperty(propertyDTO);
        });
        assertEquals(PropertyException.PROPERTY_NOT_CREATE, exception.getMessage());
    }

 
    @Test
    void getAllProperties_ShouldReturnList_WhenPropertiesExist() {
        when(propertyRepository.findAll()).thenReturn(Arrays.asList(property));
        List<PropertyDTO> result = propertyService.getallProperties();
        assertEquals(1, result.size());
        assertEquals(property.getAddress(), result.get(0).getAddress());
        verify(propertyRepository, times(1)).findAll();
    }

  
    @Test
    void getProperty_ShouldReturnPropertyDTO_WhenFound() throws PropertyException {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        PropertyDTO result = propertyService.getProperty(1L);
        assertNotNull(result);
        assertEquals(property.getAddress(), result.getAddress());
        verify(propertyRepository, times(1)).findById(1L);
    }

    @Test
    void getProperty_ShouldThrowException_WhenIdIsNull() {
        PropertyException exception = assertThrows(PropertyException.class, () -> {
            propertyService.getProperty(null);
        });
        assertEquals(PropertyException.ID_INVALID, exception.getMessage());
    }

    @Test
    void updateProperty_ShouldReturnUpdatedPropertyDTO_WhenSuccessful() throws PropertyException {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        PropertyDTO updatedDTO = new PropertyDTO(1L, "456 Avenue", 300000, 150, "Updated house");
        PropertyDTO result = propertyService.updateProperty(1L, updatedDTO);
        assertNotNull(result);
        assertEquals("456 Avenue", result.getAddress());
        verify(propertyRepository, times(1)).save(any(Property.class));
    }

    @Test
    void updateProperty_ShouldThrowException_WhenPropertyNotFound() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());
        PropertyDTO updatedDTO = new PropertyDTO(1L, "456 Avenue", 300000, 150, "Updated house");
        PropertyException exception = assertThrows(PropertyException.class, () -> {
            propertyService.updateProperty(1L, updatedDTO);
        });
        assertTrue(exception.getMessage().contains("could not be updated"));
    }

    @Test
    void updateProperty_ShouldThrowException_WhenRepositoryFails() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(propertyRepository.save(any(Property.class))).thenThrow(new RuntimeException());
        PropertyDTO updatedDTO = new PropertyDTO(1L, "456 Avenue", 300000, 150, "Updated house");
        PropertyException exception = assertThrows(PropertyException.class, () -> {
            propertyService.updateProperty(1L, updatedDTO);
        });
        assertEquals(PropertyException.PROPERTY_NOT_UPDATE, exception.getMessage());
    }

    @Test
    void deleteProperty_ShouldCallRepositoryDeleteById_WhenSuccessful() throws PropertyException {
        doNothing().when(propertyRepository).deleteById(1L);

        propertyService.deleteProperty(1L);

        verify(propertyRepository, times(1)).deleteById(1L);
    }
}


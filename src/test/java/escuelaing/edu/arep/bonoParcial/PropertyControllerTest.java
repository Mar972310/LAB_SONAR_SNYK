package escuelaing.edu.arep.bonoParcial;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import escuelaing.edu.arep.bonoParcial.Controller.PropertyController;
import escuelaing.edu.arep.bonoParcial.DTO.PropertyDTO;
import escuelaing.edu.arep.bonoParcial.Exception.PropertyException;
import escuelaing.edu.arep.bonoParcial.Service.Impl.PropertyService;

class PropertyControllerTest {

    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private PropertyController propertyController;

    private PropertyDTO sampleProperty;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleProperty = new PropertyDTO();
    }

    @Test
    void createProperty_ShouldReturnCreatedProperty() throws PropertyException {
        when(propertyService.createProperty(any(PropertyDTO.class))).thenReturn(sampleProperty);

        ResponseEntity<?> response = propertyController.createProperty(sampleProperty);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(sampleProperty, response.getBody());
    }

    @Test
    void createProperty_ShouldReturnBadRequest_WhenExceptionOccurs() throws PropertyException {
        when(propertyService.createProperty(any(PropertyDTO.class))).thenThrow(new PropertyException("Error create property"));

        ResponseEntity<?> response = propertyController.createProperty(sampleProperty);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Error create property", response.getBody());
    }

    // 🔹 Prueba para obtener todas las propiedades
    @Test
    void allProperty_ShouldReturnListOfProperties() {
        List<PropertyDTO> properties = Arrays.asList(sampleProperty);
        when(propertyService.getallProperties()).thenReturn(properties);

        ResponseEntity<List<PropertyDTO>> response = propertyController.allProperty();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    // 🔹 Prueba para obtener una propiedad existente
    @Test
    void getProperty_ShouldReturnProperty_WhenExists() throws PropertyException {
        when(propertyService.getProperty(1L)).thenReturn(sampleProperty);

        ResponseEntity<?> response = propertyController.getProperty(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleProperty, response.getBody());
    }

    @Test
    void getProperty_ShouldReturnBadRequest_WhenPropertyNotFound() throws PropertyException {
        when(propertyService.getProperty(99L)).thenThrow(new PropertyException("Property not found"));

        ResponseEntity<?> response = propertyController.getProperty(99L);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Property not found", response.getBody());
    }

    @Test
    void updateProperty_ShouldReturnUpdatedProperty() throws PropertyException {
        when(propertyService.updateProperty(eq(1L), any(PropertyDTO.class))).thenReturn(sampleProperty);

        ResponseEntity<?> response = propertyController.updateProperty(1L, sampleProperty);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleProperty, response.getBody());
    }

    @Test
    void updateProperty_ShouldReturnNotFound_WhenPropertyDoesNotExist() throws PropertyException {
        when(propertyService.updateProperty(eq(99L), any(PropertyDTO.class)))
            .thenThrow(new PropertyException(PropertyException.PROPERTY_NOT_UPDATE));

        ResponseEntity<?> response = propertyController.updateProperty(99L, sampleProperty);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(PropertyException.PROPERTY_NOT_UPDATE, response.getBody());
    }

    @Test
    void deleteProperty_ShouldReturnSuccessMessage() throws PropertyException {
        doNothing().when(propertyService).deleteProperty(1L);

        ResponseEntity<?> response = propertyController.deleteProperty(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Deleted successfully", response.getBody());
    }

    @Test
    void deleteProperty_ShouldReturnNotFound_WhenPropertyDoesNotExist() throws PropertyException {
        doThrow(new PropertyException("Property not found")).when(propertyService).deleteProperty(99L);

        ResponseEntity<?> response = propertyController.deleteProperty(99L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Property not found", response.getBody());
    }
}

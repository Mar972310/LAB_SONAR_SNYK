package escuelaing.edu.arep.bono_parcial;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import escuelaing.edu.arep.bono_parcial.controller.PropertyController;
import escuelaing.edu.arep.bono_parcial.dto.PropertyDTO;
import escuelaing.edu.arep.bono_parcial.exception.PropertyException;
import escuelaing.edu.arep.bono_parcial.service.impl.PropertyService;
import jakarta.servlet.http.HttpSession;

class PropertyControllerTest {

    @Mock
    private PropertyService propertyService;

    @Mock
    private HttpSession session;  // Simulación de la sesión

    @InjectMocks
    private PropertyController propertyController;

    private PropertyDTO sampleProperty;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleProperty = new PropertyDTO();
        when(session.getAttribute("csrfToken")).thenReturn("valid-csrf-token");  // Simulación del token CSRF
    }

    @Test
    void createProperty_ShouldReturnCreatedProperty() throws PropertyException {
        when(propertyService.createProperty(any(PropertyDTO.class))).thenReturn(sampleProperty);

        ResponseEntity<?> response = propertyController.createProperty(sampleProperty, "valid-csrf-token", session);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleProperty, response.getBody());
    }

    @Test
    void createProperty_ShouldReturnBadRequest_WhenExceptionOccurs() throws PropertyException {
        when(propertyService.createProperty(any(PropertyDTO.class)))
            .thenThrow(new PropertyException("Error create property"));

        ResponseEntity<?> response = propertyController.createProperty(sampleProperty, "valid-csrf-token", session);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error create property", response.getBody());
    }

    @Test
    void allProperty_ShouldReturnListOfProperties() {
        List<PropertyDTO> properties = Arrays.asList(sampleProperty);
        when(propertyService.getallProperties()).thenReturn(properties);

        ResponseEntity<List<PropertyDTO>> response = propertyController.allProperty();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getProperty_ShouldReturnProperty_WhenExists() throws PropertyException {
        when(propertyService.getProperty(1L)).thenReturn(sampleProperty);

        ResponseEntity<?> response = propertyController.getProperty(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleProperty, response.getBody());
    }

    @Test
    void getProperty_ShouldReturnNotFound_WhenPropertyNotFound() throws PropertyException {
        when(propertyService.getProperty(99L))
            .thenThrow(new PropertyException("Property not found"));

        ResponseEntity<?> response = propertyController.getProperty(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Property not found", response.getBody());
    }

    @Test
    void updateProperty_ShouldReturnUpdatedProperty() throws PropertyException {
        when(propertyService.updateProperty(eq(1L), any(PropertyDTO.class)))
            .thenReturn(sampleProperty);

        ResponseEntity<?> response = propertyController.updateProperty(1L, sampleProperty, "valid-csrf-token", session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleProperty, response.getBody());
    }

    @Test
    void updateProperty_ShouldReturnNotFound_WhenPropertyDoesNotExist() throws PropertyException {
        when(propertyService.updateProperty(eq(99L), any(PropertyDTO.class)))
            .thenThrow(new PropertyException("Property not updated"));

        ResponseEntity<?> response = propertyController.updateProperty(99L, sampleProperty, "valid-csrf-token", session);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Property not updated", response.getBody());
    }

    @Test
    void deleteProperty_ShouldReturnSuccessMessage() throws PropertyException {
        doNothing().when(propertyService).deleteProperty(1L);

        ResponseEntity<?> response = propertyController.deleteProperty(1L, "valid-csrf-token", session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted successfully", response.getBody());
    }

    @Test
    void deleteProperty_ShouldReturnNotFound_WhenPropertyDoesNotExist() throws PropertyException {
        doThrow(new PropertyException("Property not found"))
            .when(propertyService).deleteProperty(99L);

        ResponseEntity<?> response = propertyController.deleteProperty(99L, "valid-csrf-token", session);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Property not found", response.getBody());
    }
}

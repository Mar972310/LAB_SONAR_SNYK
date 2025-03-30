package escuelaing.edu.arep.bonoParcial.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import escuelaing.edu.arep.bonoParcial.DTO.PropertyDTO;
import escuelaing.edu.arep.bonoParcial.Exception.PropertyException;
import escuelaing.edu.arep.bonoParcial.Service.Impl.PropertyService;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {

    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    /**
     * Creates a new property.
     *
     * @param property PropertyDTO containing property details.
     * @return ResponseEntity with the created property or an error message.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createProperty(@RequestBody PropertyDTO property) {
        try {
            PropertyDTO propertySave = propertyService.createProperty(property);
            return ResponseEntity.status(201).body(propertySave);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error create property");
        }
    }

    /**
     * Retrieves all properties.
     *
     * @return ResponseEntity containing a list of all PropertyDTOs.
     */
    @GetMapping("/all")
    public ResponseEntity<List<PropertyDTO>> allProperty() {
        List<PropertyDTO> properties = propertyService.getallProperties();
        return ResponseEntity.ok(properties);
    }

    /**
     * Retrieves a specific property by its ID.
     *
     * @param id The ID of the property to retrieve.
     * @return ResponseEntity containing the property or an error message.
     */
    @GetMapping("/property/{id}")
    public ResponseEntity<?> getProperty(@PathVariable Long id) {
        try {
            PropertyDTO property = propertyService.getProperty(id);
            return ResponseEntity.ok(property);
        } catch (PropertyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Updates an existing property.
     *
     * @param id The ID of the property to update.
     * @param property PropertyDTO with updated details.
     * @return ResponseEntity containing the updated property or an error message.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProperty(@PathVariable Long id, @RequestBody PropertyDTO property) {
        try {
            PropertyDTO propertyUpdate = propertyService.updateProperty(id, property);
            return ResponseEntity.ok(propertyUpdate);
        } catch (PropertyException e) {
            String error = e.getMessage();
            if (error.equals(PropertyException.PROPERTY_NOT_UPDATE)) {
                return ResponseEntity.status(400).body(e.getMessage());
            } else {
                return ResponseEntity.status(404).body(e.getMessage());
            }
        }
    }

    /**
     * Deletes a property by its ID.
     *
     * @param id The ID of the property to delete.
     * @return ResponseEntity with a success message or an error message.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id) {
        try {
            propertyService.deleteProperty(id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (PropertyException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}

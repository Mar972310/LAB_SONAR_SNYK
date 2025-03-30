package escuelaing.edu.arep.bonoParcial.Controller;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import escuelaing.edu.arep.bonoParcial.DTO.PropertyDTO;
import escuelaing.edu.arep.bonoParcial.Exception.PropertyException;
import escuelaing.edu.arep.bonoParcial.Service.Impl.PropertyService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {

    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/csrf-token")
    public ResponseEntity<?> getCsrfToken(HttpSession session) {
        String csrfToken = UUID.randomUUID().toString();
        session.setAttribute("csrfToken", csrfToken);
        return ResponseEntity.ok().body(csrfToken);
    }


    @PostMapping
    public ResponseEntity<?> createProperty(@RequestBody PropertyDTO property, 
                                            @RequestHeader("X-CSRF-TOKEN") String csrfToken, 
                                            HttpSession session) {
        String sessionToken = (String) session.getAttribute("csrfToken");
        

        if (!csrfToken.equals(sessionToken)) {
            return ResponseEntity.status(403).body("Invalid CSRF token");
        }

        try {
            PropertyDTO propertySave = propertyService.createProperty(property);
            return ResponseEntity.status(201).body(propertySave);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error create property");
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<PropertyDTO>> allProperty() {
        List<PropertyDTO> properties = propertyService.getallProperties();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProperty(@PathVariable Long id) {
        try {
            PropertyDTO property = propertyService.getProperty(id);
            return ResponseEntity.ok(property);
        } catch (PropertyException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProperty(@PathVariable Long id, @RequestBody PropertyDTO property, 
                                            @RequestHeader("X-CSRF-TOKEN") String csrfToken, HttpSession session) {
        if (!csrfToken.equals(session.getAttribute("csrfToken"))) {
            return ResponseEntity.status(403).body("Invalid CSRF token");
        }
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id, @RequestHeader("X-CSRF-TOKEN") String csrfToken, HttpSession session) {
        if (!csrfToken.equals(session.getAttribute("csrfToken"))) {
            return ResponseEntity.status(403).body("Invalid CSRF token");
        }
        try {
            propertyService.deleteProperty(id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (PropertyException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}

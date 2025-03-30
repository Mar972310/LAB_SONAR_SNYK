package escuelaing.edu.arep.bono_parcial.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import escuelaing.edu.arep.bono_parcial.dto.PropertyDTO;
import escuelaing.edu.arep.bono_parcial.exception.PropertyException;
import escuelaing.edu.arep.bono_parcial.service.impl.PropertyService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {

    private static final String CSRF_TOKEN = "csrfToken";
    
    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/csrf-token")
    public ResponseEntity<String> getCsrfToken(HttpSession session) {
        String csrfToken = UUID.randomUUID().toString();
        session.setAttribute(CSRF_TOKEN, csrfToken);
        return ResponseEntity.ok(csrfToken);
    }

    @PostMapping
    public ResponseEntity<PropertyDTO> createProperty(@RequestBody PropertyDTO property, 
                                                      @RequestHeader("X-CSRF-TOKEN") String csrfToken, 
                                                      HttpSession session) {
        String sessionToken = (String) session.getAttribute(CSRF_TOKEN);
        if (!csrfToken.equals(sessionToken)) {
            return ResponseEntity.status(403).build();
        }

        try {
            PropertyDTO propertySave = propertyService.createProperty(property);
            return ResponseEntity.status(201).body(propertySave);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<PropertyDTO>> allProperty() {
        List<PropertyDTO> properties = propertyService.getallProperties();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> getProperty(@PathVariable Long id) {
        try {
            PropertyDTO property = propertyService.getProperty(id);
            return ResponseEntity.ok(property);
        } catch (PropertyException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(@PathVariable Long id, 
                                                      @RequestBody PropertyDTO property, 
                                                      @RequestHeader("X-CSRF-TOKEN") String csrfToken, 
                                                      HttpSession session) {
        if (!csrfToken.equals(session.getAttribute(CSRF_TOKEN))) {
            return ResponseEntity.status(403).build();
        }
        try {
            PropertyDTO propertyUpdate = propertyService.updateProperty(id, property);
            return ResponseEntity.ok(propertyUpdate);
        } catch (PropertyException e) {
            return e.getMessage().equals(PropertyException.PROPERTY_NOT_UPDATE) 
                ? ResponseEntity.status(400).build() 
                : ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id, 
                                               @RequestHeader("X-CSRF-TOKEN") String csrfToken, 
                                               HttpSession session) {
        if (!csrfToken.equals(session.getAttribute(CSRF_TOKEN))) {
            return ResponseEntity.status(403).build();
        }
        try {
            propertyService.deleteProperty(id);
            return ResponseEntity.ok().build();
        } catch (PropertyException e) {
            return ResponseEntity.status(404).build();
        }
    }
}

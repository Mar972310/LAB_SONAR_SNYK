package escuelaing.edu.arep.bono_parcial.controller;

import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CsrfController {
    @GetMapping("/csrf-token")
    public Map<String, String> getCsrfToken() {
        String csrfToken = UUID.randomUUID().toString();
        return Map.of("csrfToken", csrfToken);
    }
}


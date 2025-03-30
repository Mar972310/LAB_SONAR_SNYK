package escuelaing.edu.arep.bono_parcial.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to handle view navigation.
 */
@Controller
public class ViewController {

    /**
     * Maps the "/home" endpoint to return the "inmobiliaria" view.
     *
     * @return The name of the view template to be rendered.
     */
    @GetMapping("/home")
    public String home() {
        return "inmobiliaria";
    }
}

package escuelaing.edu.arep.bono_parcial.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor


public class PropertyDTO {
    private Long id;
    private String address;
    private double price;
    private int size;
    private String description;
  
}

package escuelaing.edu.arep.bonoParcial.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

/**
 * Entity class representing a property.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Property {

    /** Unique identifier for the property. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String address;
    private double price;
    private int size;
    private String description;

    /**
     * Constructor for creating a property without an ID.
     *
     * @param address     The address of the property.
     * @param price       The price of the property.
     * @param size        The size of the property.
     * @param description A short description of the property.
     */
    public Property(String address, double price, int size, String description) {
        this.address = address;
        this.description = description;
        this.price = price;
        this.size = size;
    }
}

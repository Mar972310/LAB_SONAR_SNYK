package escuelaing.edu.arep.bonoParcial.Exception;

/**
 * Custom exception class for property-related errors.
 */
public class PropertyException extends Exception {

    public static final String PROPERTY_NOT_FOUND = "The property hasn't been found";
    public static final String ID_INVALID = "The ID provided is not valid";
    public static final String PROPERTY_NOT_UPDATE = "The property could not be updated";
    public static final String PROPERTY_NOT_CREATE = "The property could not be created";
    public static final String PROPERTY_NOT_DELETE = "The property could not be deleted";

    /**
     * Constructs a new PropertyException with the specified message.
     *
     * @param message The detail message.
     */
    public PropertyException(String message) {
        super(message);
    }

    /**
     * Constructs a new PropertyException with a message related to a specific property ID.
     *
     * @param message The detail message.
     * @param id The ID of the property that caused the exception.
     */
    public PropertyException(String message, String id) {
        super("The requested property with ID: " + id + " hasn't been found");
    }
}

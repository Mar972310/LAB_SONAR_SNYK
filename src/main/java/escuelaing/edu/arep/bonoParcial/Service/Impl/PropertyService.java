package escuelaing.edu.arep.bonoParcial.Service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import escuelaing.edu.arep.bonoParcial.DTO.PropertyDTO;
import escuelaing.edu.arep.bonoParcial.Exception.PropertyException;
import escuelaing.edu.arep.bonoParcial.Repository.PropertyRepository;
import escuelaing.edu.arep.bonoParcial.Service.PropertyServiceInterface;
import escuelaing.edu.arep.bonoParcial.model.Property;

/**
 * Implementation of the PropertyServiceInterface.
 * Provides business logic for managing properties.
 */
@Service
public class PropertyService implements PropertyServiceInterface {

    private final PropertyRepository propertyRepository;

    /**
     * Constructor for PropertyService.
     *
     * @param propertyRepository The repository used for property data access.
     */
    @Autowired
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    /**
     * Creates a new property.
     *
     * @param propertyDTO The property data transfer object.
     * @return The created property as a DTO.
     * @throws PropertyException If the property could not be created.
     */
    @Override
    public PropertyDTO createProperty(PropertyDTO propertyDTO) throws PropertyException {
        try {
            Property property = toEntity(propertyDTO);
            property = propertyRepository.save(property);
            return toDTO(property);
        } catch (Exception e) {
            throw new PropertyException(PropertyException.PROPERTY_NOT_CREATE);
        }
    }

    /**
     * Retrieves all properties.
     *
     * @return A list of PropertyDTOs.
     */
    @Override
    public List<PropertyDTO> getallProperties() {
        List<Property> properties = propertyRepository.findAll();
        return properties.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves a property by ID.
     *
     * @param id The property ID.
     * @return The property as a DTO.
     * @throws PropertyException If the property is not found or the ID is invalid.
     */
    @Override
    public PropertyDTO getProperty(Long id) throws PropertyException {
        if (id == null) {
            throw new PropertyException(PropertyException.ID_INVALID);
        }
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyException(PropertyException.PROPERTY_NOT_FOUND, String.valueOf(id)));
        return toDTO(property);
    }

    /**
     * Updates an existing property.
     *
     * @param id          The ID of the property to update.
     * @param propertyDTO The updated property data.
     * @return The updated property as a DTO.
     * @throws PropertyException If the property does not exist or cannot be updated.
     */
    @Override
    public PropertyDTO updateProperty(Long id, PropertyDTO propertyDTO) throws PropertyException {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyException("The property with ID " + id + " could not be updated because it does not exist"));
        
        property.setAddress(propertyDTO.getAddress());
        property.setDescription(propertyDTO.getDescription());
        property.setPrice(propertyDTO.getPrice());
        property.setSize(propertyDTO.getSize());

        try {
            propertyRepository.save(property);
        } catch (Exception e) {
            throw new PropertyException(PropertyException.PROPERTY_NOT_UPDATE);
        }

        return toDTO(property);
    }

    /**
     * Deletes a property by ID.
     *
     * @param id The ID of the property to delete.
     * @throws PropertyException If the property cannot be deleted.
     */
    public void deleteProperty(Long id) throws PropertyException {
        propertyRepository.deleteById(id);
    }

    /**
     * Converts a PropertyDTO to a Property entity.
     *
     * @param propertyDTO The DTO to convert.
     * @return The corresponding Property entity.
     */
    private Property toEntity(PropertyDTO propertyDTO) {
        return new Property(
            null,
            propertyDTO.getAddress(),
            propertyDTO.getPrice(),
            propertyDTO.getSize(),
            propertyDTO.getDescription()
        );
    }

    /**
     * Converts a Property entity to a PropertyDTO.
     *
     * @param property The entity to convert.
     * @return The corresponding PropertyDTO.
     */
    private PropertyDTO toDTO(Property property) {
        return new PropertyDTO(
            property.getId(),
            property.getAddress(),
            property.getPrice(),
            property.getSize(),
            property.getDescription()
        );
    }
}

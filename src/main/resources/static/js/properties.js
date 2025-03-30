// Function to escape HTML to prevent XSS attacks
function escapeHTML(str) {
    if (typeof str !== "string") return str; // Only process strings
    const div = document.createElement("div");
    div.textContent = str;
    return div.innerHTML;
}

// Function to display error messages
function showErrorMessage() {
    const container = document.querySelector('.properties');
    container.innerHTML = `
        <div class="error-message">
            <h2>¡Ups! Algo salió mal</h2>
            <p>No pudimos cargar las propiedades en este momento. Intenta más tarde.</p>
        </div>`;
}

// Fetch all properties from the backend
async function getAllProperties() {
    try {
        const response = await fetch('/api/v1/properties/all', {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
        });

        if (!response.ok) {
            throw new Error('Error fetching properties');
        }

        const properties = await response.json();
        console.log('Datos recibidos:', properties);
        renderProperties(properties);
    } catch (error) {
        console.error('Error al obtener los datos:', error);
        showErrorMessage();
    }
}

// Render properties dynamically
const renderProperties = (properties) => {
    const container = document.querySelector('.properties');
    container.innerHTML = ''; 

    properties.forEach(property => {
        const propertyElement = document.createElement('div');
        propertyElement.classList.add('property');

        const textSection = document.createElement('div');
        textSection.classList.add('text-section');

        // Creating elements securely
        const title = document.createElement("h2");
        title.textContent = property.address;

        const size = document.createElement("p");
        size.innerHTML = `<strong>Size:</strong> ${property.size} m²`;

        const description = document.createElement("p");
        description.innerHTML = `<strong>Description:</strong> ${escapeHTML(property.description)}`;

        const price = document.createElement("p");
        price.innerHTML = `<strong>Price:</strong> $${property.price.toLocaleString()}`;

        // Buttons
        const calendarDiv = document.createElement("div");
        calendarDiv.classList.add("calendar");

        const updateButton = document.createElement("button");
        updateButton.classList.add("update-button");
        updateButton.dataset.id = property.id;
        updateButton.textContent = "Update";

        const deleteButton = document.createElement("button");
        deleteButton.classList.add("delete-button");
        deleteButton.dataset.id = property.id;
        deleteButton.textContent = "Delete";

        calendarDiv.appendChild(updateButton);
        calendarDiv.appendChild(deleteButton);

        textSection.appendChild(title);
        textSection.appendChild(size);
        textSection.appendChild(description);
        textSection.appendChild(price);
        textSection.appendChild(calendarDiv);

        propertyElement.appendChild(textSection);
        container.appendChild(propertyElement);
    });

    // Reattach event listeners AFTER rendering
    document.querySelectorAll('.update-button').forEach(button => {
        button.addEventListener("click", () => updateProperty(button.dataset.id));
    });

    document.querySelectorAll('.delete-button').forEach(button => {
        button.addEventListener("click", () => deleteProperty(button.dataset.id));
    });
};

// Function to delete a property
async function deleteProperty(propertyId) {
    if (!confirm("¿Estás seguro de que deseas eliminar esta propiedad?")) return;

    try {
        const csrf = await getCsrfToken();

        const response = await fetch(`/api/v1/properties/${propertyId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                "X-CSRF-TOKEN": csrf
            },
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Error al eliminar la propiedad");
        }

        alert("Propiedad eliminada correctamente");
        getAllProperties(); // Refresh list after deletion
    } catch (error) {
        console.error('Error al eliminar propiedad:', error);
        alert('Hubo un problema al eliminar la propiedad. Intenta más tarde.');
    }
}

// Fetch CSRF token
async function getCsrfToken() {
    try {
        const response = await fetch("/api/v1/properties/csrf-token");
        return await response.text();
    } catch (error) {
        console.error("Error fetching CSRF token:", error);
        return null;
    }
}

// Function to update a property
async function updateProperty(propertyId) {  
    const url = `/api/v1/properties/${propertyId}`;
    const formData = new FormData(document.querySelector(".property-form")); 
    const propertyData = {
        address: formData.get("address"),
        price: formData.get("price"),
        size: formData.get("size"),
        description: formData.get("description"),
    };
    try {
        const csrf = await getCsrfToken();

        const response = await fetch(url, {
            method: "PUT",
            headers: { 
                "Content-Type": "application/json",
                "X-CSRF-TOKEN": csrf
            },
            body: JSON.stringify(propertyData)  
        });

        if (!response.ok) throw new Error(`Error updating property: ${response.statusText}`);

        await response.json();  
        getAllProperties(); // Refresh list after update
    } catch (error) {
        console.error("Error updating property:", error);
        alert("There was an error updating the property. Please try again.");
    }
}

// Initialize event listeners on DOM load
document.addEventListener("DOMContentLoaded", () => {
    document.querySelector(".property-form")?.addEventListener("submit", (event) => {
        event.preventDefault();
        create();
    });
    getAllProperties();
});

async function getAllProperties() {
    try {
        const response = await fetch('/api/v1/properties/all', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
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

const renderProperties = (properties) => {
    const container = document.querySelector('.properties');
    container.innerHTML = ''; 

    properties.forEach(property => {
        const propertyElement = document.createElement('div');
        propertyElement.classList.add('property');

        const textSection = document.createElement('div');
        textSection.classList.add('text-section');

        textSection.innerHTML = `
            <h2>${property.address}</h2>
            <p><strong>Size:</strong> ${property.size} m²</p>
            <p><strong>Description:</strong> ${property.description}</p>
            <p><strong>Price:</strong> $${property.price.toLocaleString()}</p>
            <div class="calendar">
                <button class="update-button" data-id="${property.id}">Update</button>
                <button class="delete-button" data-id="${property.id}">Delete</button>
            </div>
        `;

        textSection.querySelector('.update-button').addEventListener('click', () => updateProperty(property.id));
        textSection.querySelector('.delete-button').addEventListener('click', () => deleteProperty(property.id));

        propertyElement.appendChild(textSection);
        container.appendChild(propertyElement);
    });
};

async function deleteProperty(propertyId) {
    if (!confirm("¿Estás seguro de que deseas eliminar esta propiedad?")) return;

    try {
        const response = await fetch(`/api/v1/properties/${propertyId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                "X-CSRF-TOKEN": getCsrfToken()
            },
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Error al eliminar la propiedad");
        }

        alert("Propiedad eliminada correctamente");
        window.location.href = `/home`;
    } catch (error) {
        console.error('Error al eliminar propiedad:', error);
        alert('Hubo un problema al eliminar la propiedad. Intenta más tarde.');
    }
}

async function getCsrfToken() {
    try {
        const response = await fetch("/api/v1/properties/csrf-token");
        return await response.text();
    } catch (error) {
        console.error("Error fetching CSRF token:", error);
        return null;
    }
}


async function create(event) {
    event.preventDefault();  
    const csrf = await getCsrfToken();

    console.log(csrf);
    const formData = new FormData(document.querySelector(".property-form")); 
    const propertyData = {
        address: formData.get("address"),
        price: formData.get("price"),
        size: formData.get("size"),
        description: formData.get("description"),
    };

    try {
        const response = await fetch("/api/v1/properties", {
            method: "POST",
            headers: { 
                "Content-Type": "application/json",
                "X-CSRF-TOKEN": csrf
            },
            body: JSON.stringify(propertyData)  
        });

        if (!response.ok) throw new Error(`Error saving property: ${response.statusText}`);

        await response.json();  
        window.location.href = "/home";  
    } catch (error) {
        console.error("Error saving property:", error);
        alert("There was an error saving the property. Please try again.");
    }
}

async function updateProperty(propertyId) {
    fetchProperty(`/api/v1/properties/property/${propertyId}`, propertyId);
}

async function fetchProperty(url, propertyId) {
    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });
        if (!response.ok) {
            throw new Error('Error fetching properties');
        }
        const property = await response.json();
        renderPropertyForm(property, propertyId);
    } catch (error) {
        console.error('Error al obtener los datos:', error);
        showErrorMessage();
    }
}

async function update(propertyId) {  
    const url = `/api/v1/properties/${propertyId}`;
    const formData = new FormData(document.querySelector(".property-form")); 
    const propertyData = {
        address: formData.get("address"),
        price: formData.get("price"),
        size: formData.get("size"),
        description: formData.get("description"),
    };
    try {
        const response = await fetch(url, {
            method: "PUT",
            headers: { 
                "Content-Type": "application/json",
                "X-CSRF-TOKEN": getCsrfToken()
            },
            body: JSON.stringify(propertyData)  
        });

        if (!response.ok) throw new Error(`Error updating property: ${response.statusText}`);

        await response.json();  
        window.location.href = "/home";  
    } catch (error) {
        console.error("Error updating property:", error);
        alert("There was an error updating the property. Please try again.");
    }
}

function showErrorMessage() {
    const container = document.querySelector('.properties');
    container.innerHTML = `
        <div class="error-message">
            <h2>¡Ups! Algo salió mal</h2>
            <p>No pudimos cargar las propiedades en este momento. Intenta más tarde.</p>
        </div>`;
}

document.addEventListener("DOMContentLoaded", () => {
    document.querySelector(".property-form")?.addEventListener("submit", create);
    getAllProperties();
});

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
        document.querySelector('.properties').innerHTML = `
        <div class="error-message">
            <h2>¡Ups! Algo salió mal</h2>
            <p>No pudimos cargar las propiedades en este momento. Esto podría deberse a un problema con el servidor o la conexión a internet.</p>
            <p><strong>Por favor, intenta nuevamente más tarde.</strong></p>
            <p>Si el problema persiste, contacta al soporte técnico.</p>
        </div>
    `;
    }
}

const renderProperties = (properties) => {
    const container = document.querySelector('.properties');
    container.innerHTML = '';

    properties.forEach(property => {
        const propertyElement = document.createElement('div');
        propertyElement.classList.add('property');
        propertyElement.innerHTML = `
            <div class="text-section">
                <h2>${property.address}</h2>
                <p><strong>Size:</strong> ${property.size} m²</p>
                <p><strong>Description:</strong> ${property.description}</p>
                <p><strong>Price:</strong> $${property.price.toLocaleString()}</p>
                <div class="calendar">
                    <button class="update-button" data-id="${property.id}">Update</button>
                    <button class="delete-button" data-id="${property.id}">Delete</button>
                </div> 
            </div>
        `;

        const updateButton = propertyElement.querySelector('.update-button');
        updateButton.addEventListener('click', (event) => {
            const propertyId = event.target.dataset.id;
            updateProperty(propertyId);
        });


        const deleteButton = propertyElement.querySelector('.delete-button');
        deleteButton.addEventListener('click', (event) => {
            const propertyId = event.target.dataset.id;
            deleteProperty(propertyId) 
        });

        container.appendChild(propertyElement);
    });
};


async function create(event) {
    event.preventDefault();  

    const formData = new FormData(document.querySelector(".property-form")); 
    const propertyData = {
        address: formData.get("address"),
        price: formData.get("price"),
        size: formData.get("size"),
        description: formData.get("description"),
    };

    try {
        
        const response = await fetch("/api/v1/properties/create", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
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

async function updateProperty(propertyId){
    const pathname = window.location.pathname;
    const segments = pathname.split('/');
    const url = `/api/v1/properties/property/${propertyId}`;
    fetchproperty(url,propertyId);

};

    
async function fetchproperty(url,propertyId) {
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
        renderproperty(property,propertyId);

    } catch (error) {
        console.error('Error al obtener los datos:', error);
        document.querySelector('.container').innerHTML = `
        <div class="error-message">
            <h2>¡Ups! Algo salió mal</h2>
            <p>No pudimos cargar el procedimiento en este momento. Esto podría deberse a un problema con el servidor o la conexión a internet.</p>
            <p><strong>Por favor, intenta nuevamente más tarde.</strong></p>
            <p>Si el problema persiste, contacta al soporte técnico.</p>
        </div>
        `;
        return [];
    }
};

// Llenar la informacion del form 
async function renderproperty(property,propertyId) {
    const propertyContainer = document.querySelector('.box-static');
    propertyContainer.innerHTML = '';

    propertyContainer.innerHTML = `
        <form class="property-form">
            <h2>Update a Property</h2>
            <label for="address">Address</label>
            <input type="text" id="address" name="address" value="${property.address}" required>
        
            <label for="price">Price</label>
            <input type="number" id="price" name="price" value="${property.price}" required>
        
            <label for="size">Size</label>
            <input type="number" id="size" name="size" value="${property.size}" required>
        
            <label for="description">Description</label>
            <textarea id="description" name="description" required>${property.description}</textarea>
        
            <div class="button-container">
                <button class="cancel-button">Cancelar</button>
                <button class="accept-button">Aceptar</button>
            </div>
        </form>
    `;
    const acceptButton = document.querySelector('.accept-button');
        acceptButton.addEventListener('click', (event) => {
            event.preventDefault();
            update(propertyId);
        });
    const cancelButton = document.querySelector('.cancel-button');
    cancelButton.addEventListener('click', (event) => {
        event.preventDefault();
        cancelSubmit(propertyId);
    });
};

async function update(propertyId) {  
    const url = `/api/v1/properties/update/${propertyId}`;
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
            headers: { "Content-Type": "application/json" },
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

async function deleteProperty(propertyId) {
    const confirmDelete = confirm("¿Estás seguro de que deseas eliminar esta propiedad?");
    const url = `/api/v1/properties/delete/${propertyId}`;
    if (confirmDelete) {
        try {
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || "Error al eliminar la propiedad");
            
            }
            alert("Procedimiento eliminado correctamente");
            window.location.href = `/home`;
        } catch (error) {
            console.error('Error al eliminar procedimiento:', error);
            showNotification(error.message || 'Hubo un problema al intentar eliminar la propiedad. Intenta más tarde.');
        }
    }
};

document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".property-form");
    form.addEventListener("submit", create);
    getAllProperties();  
});
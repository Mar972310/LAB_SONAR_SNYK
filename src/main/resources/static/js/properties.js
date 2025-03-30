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
        
        const textSection = document.createElement('div');
        textSection.classList.add('text-section');

        const heading = document.createElement('h2');
        heading.textContent = sanitizeText(property.address); // Sanitize the address
        textSection.appendChild(heading);

        const sizePara = document.createElement('p');
        sizePara.textContent = `Size: ${sanitizeText(property.size)} m²`; // Use textContent for dynamic data
        textSection.appendChild(sizePara);

        const descriptionPara = document.createElement('p');
        descriptionPara.textContent = `Description: ${sanitizeText(property.description)}`; // Sanitize description
        textSection.appendChild(descriptionPara);

        const pricePara = document.createElement('p');
        pricePara.textContent = `Price: $${sanitizeText(property.price.toLocaleString())}`; // Sanitize price
        textSection.appendChild(pricePara);

        const calendarDiv = document.createElement('div');
        calendarDiv.classList.add('calendar');

        const updateButton = document.createElement('button');
        updateButton.classList.add('update-button');
        updateButton.dataset.id = property.id;
        updateButton.textContent = 'Update';
        calendarDiv.appendChild(updateButton);

        const deleteButton = document.createElement('button');
        deleteButton.classList.add('delete-button');
        deleteButton.dataset.id = property.id;
        deleteButton.textContent = 'Delete';
        calendarDiv.appendChild(deleteButton);

        textSection.appendChild(calendarDiv);
        propertyElement.appendChild(textSection);

        const updateButtonListener = (event) => {
            const propertyId = event.target.dataset.id;
            updateProperty(propertyId);
        };

        const deleteButtonListener = (event) => {
            const propertyId = event.target.dataset.id;
            deleteProperty(propertyId);
        };

        updateButton.addEventListener('click', updateButtonListener);
        deleteButton.addEventListener('click', deleteButtonListener);

        container.appendChild(propertyElement);
    });
};

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

    const formData = new FormData(document.querySelector(".property-form"));
    const propertyData = {
        address: formData.get("address"),
        price: formData.get("price"),
        size: formData.get("size"),
        description: formData.get("description"),
    };

    try {
        const csrf = await getCsrfToken();
        const response = await fetch("/api/v1/properties", {
            method: "POST",
            headers: { "Content-Type": "application/json",
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

async function updateProperty(propertyId){
    const pathname = window.location.pathname;
    const segments = pathname.split('/');
    const url = `/api/v1/properties/${propertyId}`;
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

async function renderproperty(property,propertyId) {
    const propertyContainer = document.querySelector('.box-static');
    propertyContainer.innerHTML = '';

    const form = document.createElement('form');
    form.classList.add('property-form');

    const heading = document.createElement('h2');
    heading.textContent = 'Update a Property';
    form.appendChild(heading);

    // Address input
    const addressLabel = document.createElement('label');
    addressLabel.setAttribute('for', 'address');
    addressLabel.textContent = 'Address';
    form.appendChild(addressLabel);

    const addressInput = document.createElement('input');
    addressInput.setAttribute('type', 'text');
    addressInput.setAttribute('id', 'address');
    addressInput.setAttribute('name', 'address');
    addressInput.value = sanitizeText(property.address); // Sanitize address
    addressInput.required = true;
    form.appendChild(addressInput);

    // Price input
    const priceLabel = document.createElement('label');
    priceLabel.setAttribute('for', 'price');
    priceLabel.textContent = 'Price';
    form.appendChild(priceLabel);

    const priceInput = document.createElement('input');
    priceInput.setAttribute('type', 'number');
    priceInput.setAttribute('id', 'price');
    priceInput.setAttribute('name', 'price');
    priceInput.value = property.price;
    priceInput.required = true;
    form.appendChild(priceInput);

    // Size input
    const sizeLabel = document.createElement('label');
    sizeLabel.setAttribute('for', 'size');
    sizeLabel.textContent = 'Size';
    form.appendChild(sizeLabel);

    const sizeInput = document.createElement('input');
    sizeInput.setAttribute('type', 'number');
    sizeInput.setAttribute('id', 'size');
    sizeInput.setAttribute('name', 'size');
    sizeInput.value = property.size;
    sizeInput.required = true;
    form.appendChild(sizeInput);

    // Description textarea
    const descriptionLabel = document.createElement('label');
    descriptionLabel.setAttribute('for', 'description');
    descriptionLabel.textContent = 'Description';
    form.appendChild(descriptionLabel);

    const descriptionTextarea = document.createElement('textarea');
    descriptionTextarea.setAttribute('id', 'description');
    descriptionTextarea.setAttribute('name', 'description');
    descriptionTextarea.required = true;
    descriptionTextarea.textContent = sanitizeText(property.description); // Sanitize description
    form.appendChild(descriptionTextarea);

    // Buttons container
    const buttonContainer = document.createElement('div');
    buttonContainer.classList.add('button-container');

    const cancelButton = document.createElement('button');
    cancelButton.classList.add('cancel-button');
    cancelButton.textContent = 'Cancelar';
    buttonContainer.appendChild(cancelButton);

    const acceptButton = document.createElement('button');
    acceptButton.classList.add('accept-button');
    acceptButton.textContent = 'Aceptar';
    buttonContainer.appendChild(acceptButton);

    form.appendChild(buttonContainer);

    // Append the form to the container
    propertyContainer.appendChild(form);

    // Add event listeners for the buttons
    acceptButton.addEventListener('click', (event) => {
        event.preventDefault();
        update(propertyId);
    });

    cancelButton.addEventListener('click', (event) => {
        event.preventDefault();
        cancelSubmit(propertyId);
    });
};

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
        const csrf = await getCsrfToken();

        const response = await fetch(url, {
            method: "PUT",
            headers: { "Content-Type": "application/json",
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

async function deleteProperty(propertyId) {
    const confirmDelete = confirm("¿Estás seguro de que deseas eliminar esta propiedad?");
    const url = `/api/v1/properties/${propertyId}`;
    if (confirmDelete) {
        try {
            const csrf = await getCsrfToken();
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': csrf
                },
            });

            if (!response.ok) {
                throw new Error('Error deleting property');
            }

            alert('Property deleted successfully!');
            window.location.href = '/home';
        } catch (error) {
            console.error('Error deleting property:', error);
            alert('There was an error deleting the property. Please try again.');
        }
    }
}

const sanitizeText = (text) => {
    const element = document.createElement('div');
    if (text) {
        element.innerText = text;
        return element.innerHTML; // This safely escapes the content
    }
    return '';
};

document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".property-form");
    form.addEventListener("submit", create);
    getAllProperties();
});

const BASE_URL = "http://localhost:8080"; // Din backend URL

let selectedItems = []; // Liste over valgte madvarer

// Hent madspildsvarer fra backend
async function fetchWasteFood() {
    try {
        const response = await fetch(BASE_URL + "/api/recipes/wasteFood");

        if (!response.ok) {
            throw new Error(`HTTP-fejl! Status: ${response.status}`);
        }

        const data = await response.json();
        console.log("Hentede data:", data);

        const foodData = data.clearances || []; // Sørg for at foodData er en array
        displayFoodItems(foodData);
    } catch (err) {
        console.log("Der opstod en fejl: " + err);
    }
}

// Funktion til at vise madvarer på siden
// Funktion til at vise madvarer på siden
function displayFoodItems(foodData) {
    const foodList = document.getElementById('foodList');
    foodList.innerHTML = ""; // Tømmer listen før indlæsning af nye data

    foodData.forEach(food => {
        console.log(food); // Log hele food objektet
        const foodItem = document.createElement("div");
        foodItem.classList.add("food-item");

        // Kontroller om billede og priser eksisterer
        const imageSrc = food.product.image ? food.product.image : 'default-image.jpg'; // Sæt en default billede, hvis ikke fundet
        const originalPrice = food.offer && food.offer.originalPrice ? food.offer.originalPrice : 'Ikke tilgængelig';
        const newPrice = food.offer && food.offer.newPrice ? food.offer.newPrice : 'Ikke tilgængelig';
        const discount = food.offer && food.offer.discount ? food.offer.discount : 'Ikke tilgængelig'; // Brug discount direkte fra API-dataen

        // Opret checkbox elementet
        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.id = `checkbox-${food.product.id}`;
        checkbox.value = food.product.id;

        // Tilføj label og checkbox til food-item
        const label = document.createElement("label");
        label.setAttribute("for", checkbox.id);
        label.textContent = food.product.description;

        // Tilføj til food-item
        foodItem.innerHTML = `
            <img src="${imageSrc}" alt="${food.product.name}">
            <p>${food.product.description}</p>
            <p>Original pris: ${originalPrice} DKK</p>
            <p>Ny pris: ${newPrice} DKK</p>
            <p>Spar: ${discount} DKK</p>
        `;

        // Tilføj checkbox og label til madvare item
        foodItem.appendChild(checkbox);
        foodItem.appendChild(label);

        // Event listener for klik på madvare
        foodItem.addEventListener("click", () => toggleCheckbox(checkbox));

        // Event listener for checkbox ændringer
        checkbox.addEventListener("change", () => toggleSelection(food, checkbox));

        // Tilføj madvare til listen
        foodList.appendChild(foodItem);
    });
}

// Funktion til at håndtere checkbox tilstand (flueben eller ikke)
function toggleCheckbox(checkbox) {
    checkbox.checked = !checkbox.checked; // Skift checkbox'ens tilstand
    checkbox.dispatchEvent(new Event('change')); // Trigger change event for at opdatere selectedItems
}

// Funktion til at vælge/afvælge madvarer via checkbox
function toggleSelection(food, checkbox) {
    const isSelected = checkbox.checked;

    if (isSelected) {
        selectedItems.push(food); // Tilføj til listen
    } else {
        selectedItems = selectedItems.filter(item => item.id !== food.id); // Fjern fra listen
    }

    console.log("Valgte madvarer:", selectedItems);
}


async function generateRecipes() {
    const recipeList = document.getElementById('recipeList');
    recipeList.innerHTML = ""; // Tøm opskriftslisten

    if (selectedItems.length === 0) {
        recipeList.innerHTML = "<p>Vælg venligst nogle varer for at generere opskrifter.</p>";
        return;
    }

    // Vis spinner mens vi henter opskrifter
    document.getElementById('spinner').style.display = 'block';

    try {
        // Send de valgte varer til backend
        const selectedNames = selectedItems.map(item => item.product.description).join(',');  // Brug description i stedet for ean
        console.log("Valgte varer:", selectedItems);

        const response = await fetch(`${BASE_URL}/api/recipes/generate?selectedProductNames=${encodeURIComponent(selectedNames)}`, {
            method: 'GET'
        });

        if (!response.ok) {
            throw new Error("Fejl ved hentning af opskrifter");
        }

        const data = await response.json();
        console.log("Backend svar:", data);
        // Tjek om der er opskrifter
        if (data && data) {
            const recipeItem = document.createElement("p");
            recipeItem.textContent = data;  // Brug answer som opskriften
            recipeList.appendChild(recipeItem);
        } else {
            recipeList.innerHTML = "Ingen opskrifter fundet.";
        }
    } catch (error) {
        recipeList.innerHTML = "En fejl opstod, prøv venligst igen.";
        console.error("Fejl:", error);
    } finally {
        document.getElementById('spinner').style.display = 'none';
    }
}

document.getElementById("generateRecipes").addEventListener("click", generateRecipes);


// Initial indlæsning af madspildsvarer
fetchWasteFood();

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
        // Tjek for billeder og priser
        foodItem.innerHTML = `
            <img src="${imageSrc}" alt="${food.product.name}">
            <p>${food.product.description}</p>
            <p>Original pris: ${originalPrice} DKK</p>
            <p>Ny pris: ${newPrice} DKK</p>
            <p>Spar: ${discount} DKK</p>
        `;

        // Tilføj event listener til at vælge eller afvælge madvarer
        foodItem.addEventListener("click", () => toggleSelection(food, foodItem));

        foodList.appendChild(foodItem);
    });
}


// Funktion til at vælge/afvælge madvarer
function toggleSelection(food, foodItem) {
    const isSelected = selectedItems.includes(food);

    if (isSelected) {
        selectedItems = selectedItems.filter(item => item.id !== food.id);
        foodItem.classList.remove("selected");
    } else {
        selectedItems.push(food);
        foodItem.classList.add("selected");
    }
}

// Funktion til at generere opskrifter baseret på valgte varer
async function generateRecipes() {
    const recipeList = document.getElementById('recipeList');
    recipeList.innerHTML = ""; // Tømmer opskriftslisten

    if (selectedItems.length === 0) {
        recipeList.innerHTML = "<p>Vælg venligst nogle varer for at generere opskrifter.</p>";
        return;
    }

    // Vis spinner mens vi henter opskrifter
    document.getElementById('spinner').style.display = 'block';

    try {
        // Send en GET-forespørgsel med de valgte varer
        const selectedNames = selectedItems.map(item => item.product.description).join(',');  // Brug description i stedet for ean
        console.log("Selected items:", selectedItems);
        const response = await fetch(`${BASE_URL}/api/recipes/generate?selectedProductNames=${encodeURIComponent(selectedNames)}`, {
            method: 'GET'
        });

        if (!response.ok) {
            throw new Error("Fejl ved hentning af opskrifter");
        }

        const data = await response.json();
        // Tjek om der er opskrifter
        if (data && data.recipes && data.recipes.length > 0) {
            data.recipes.forEach(recipe => {
                const recipeItem = document.createElement("p");
                recipeItem.textContent = recipe;
                recipeList.appendChild(recipeItem);
            });
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


// Event Listener for knappen til at generere opskrifter
document.getElementById("generateRecipes").addEventListener("click", generateRecipes);

// Initial indlæsning af madspildsvarer
fetchWasteFood();

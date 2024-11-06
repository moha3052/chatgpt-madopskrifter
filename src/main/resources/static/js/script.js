document.getElementById('btn-get-recipe').addEventListener('click', function(event) {
    event.preventDefault();
    const userInput = document.getElementById('recipe-name').value;

    if (userInput.trim() === "") {
        alert("Please enter a recipe name");
        return;
    }

    document.getElementById('spinner').style.display = 'block';

    fetch(`http://localhost:8080/api/recipes/relevant?query=${encodeURIComponent(userInput)}`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            // Check for the 'answer' field in the response
            if (data && data.answer) {
                document.getElementById('result').innerHTML = `<pre>${data.answer}</pre>`;
            } else {
                document.getElementById('result').innerHTML = "No recipe found.";
            }
        })
        .catch(error => {
            document.getElementById('result').innerHTML = "An error occurred. Please try again.";
            console.error("Error:", error);
        })
        .finally(() => {
            document.getElementById('spinner').style.display = 'none';
        });

    document.getElementById('recipe-name').value = '';
});

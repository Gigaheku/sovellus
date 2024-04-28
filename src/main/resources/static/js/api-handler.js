// Function to fetch data from the OMDb API
async function fetchData(title) {
    const apiKey = "40c6525a"; // Your OMDb API key
    const url = `http://www.omdbapi.com/?apikey=${apiKey}&t=${encodeURIComponent(title)}`;

    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        
        // Check if the API returned a movie, and the movie has a title, plot, and director
        if (data && data.Title && data.Plot && data.Director) {
            // Update fields with data
            document.getElementById('movieTitle').textContent = data.Title;
            document.getElementById('movieYear').textContent = data.Year;
            document.getElementById('moviePlot').textContent = data.Plot;
            document.getElementById('movieDirector').textContent = data.Director;
        } else {
            // Set fields to empty if data is incomplete or missing
            document.getElementById('movieTitle').textContent = '';
            document.getElementById('movieYear').textContent = '';
            document.getElementById('moviePlot').textContent = '';
            document.getElementById('movieDirector').textContent = '';
        }
    } catch (error) {
        console.error('Failed to fetch data:', error);
        // Set all fields to empty on error
        document.getElementById('movieTitle').textContent = '';
        document.getElementById('movieYear').textContent = '';
        document.getElementById('moviePlot').textContent = '';
        document.getElementById('movieDirector').textContent = '';
    }
}

// Example usage: Fetch data for "The Matrix"
fetchData("The Matrix");

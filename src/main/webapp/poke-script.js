// grab all the elements from the page to work with
const pokeId = document.getElementById('poke-id');
const respId = document.getElementById('resp-id');
const pokeName = document.getElementById('resp-name');
const pokeImg = document.getElementById('resp-sprite');
const button = document.querySelector('button');

// whenever someone presses enter, they trigger the button to click
pokeId.addEventListener('keyup', function(event) {

    if (event.keyCode == 13) {
        button.click;
    }

})

// create a function to fetch a poke object
function fetchPokemon() {

    // capture the input from the document
    let idNum = pokeId.value; // capturing the value of the input element

    // send a fetch call to the pokeAPI and concatenate the value of the pokemon we want
    fetch(`https://pokeapi.co/api/v2/pokemon/${idNum}`)
        .then(response => response.json()) // parsed into a JS object
        //.then(obj => console.log(obj))
        .then(renderPokemon);

    // chain functions to our promise -> parse the JSON into an object, then call a function on the object
}

// create a function to render the response
function renderPokemon(data) {

    // set all the elements that we captured above, EQUAL TO the
    // properties that we pull from the data
    pokeName.innerHTML = `Name: ${data.name}`;
    respId.innerHTML = `Id: ${data.id}`;

    pokeImg.setAttribute('src', data.sprites.front_default);
    pokeImg.setAttribute('height', 300);
}

// add the event listener to the button
button.addEventListener('click', fetchPokemon);
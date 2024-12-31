const destinations = ["Aurora", "Dallas", "Detroit", "Milwaukee", "Peoria"]
const distancesInMiles = [42, 902, 230, 92, 172];
const costsPerMile = [1.5, 1.2, 1.8, 2.0, 1.3]
const weightMultipliers = [1.0, 1.5, 2.0, 2.5, 3.0];
const weights = ["0-10 lbs", "11-20 lbs", "21-30 lbs", "31-40 lbs", "41 lbs and above"];
let destinationLine = ""
let weightLine = "";

function populate(){
    for(let i = 0; i < destinations.length; i++){
        destinationLine += "<option>";
        destinationLine += destinations[i];
        destinationLine += "</option>";
        weightLine += "<option>";
        weightLine += weights[i];
        weightLine += "</option>";
    }
    document.getElementById("destinationOptions").innerHTML = destinationLine;
    document.getElementById("chooseWeight").innerHTML = weightLine;
}

function calculateCost(){
    let idx = destinations.indexOf(document.getElementById("destinationOptions").value);
    let weightIdx = weights.indexOf(document.getElementById("chooseWeight").value);
    let cost;
    let weight;
    if(idx > -1 && weightIdx > -1){
        weight = weightMultipliers[weightIdx];
        cost = (distancesInMiles[idx]*costsPerMile[idx])*weight;
        cost = cost.toFixed(2);
    }
    else{
        alert("Error in selecting")
    }
    let resObj = document.getElementById("results");
    resObj.innerHTML = "Destination: " + destinations[idx] + " - Total Cost: $" + cost;
}

function reset(){
    const desObj = document.getElementById("destinationOptions");
    const weightObj = document.getElementById("chooseWeight");
    desObj.selectedIndex = 0;
    weightObj.selectedIndex = 0;
    document.getElementById("results").innerHTML = "";
}

window.onload = function(){
    populate();
    const calcButton = document.querySelector("button#calcCost");
    const resButton = document.querySelector("button#reset");
    calcButton.addEventListener("click", calculateCost)
    resButton.addEventListener("click", reset)

}
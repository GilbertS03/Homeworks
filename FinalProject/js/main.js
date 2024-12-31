let game = {
    diceState: [
        {dice : document.getElementById("roll1"), hold: false, num : 1},
        {dice : document.getElementById("roll2"), hold: false, num : 1},
        {dice : document.getElementById("roll3"), hold: false, num : 1},
        {dice : document.getElementById("roll4"), hold: false, num : 1},
        {dice : document.getElementById("roll5"), hold: false, num : 1}
    ],
    scoreUpperButtons: [
        {button : `ones`, html: document.getElementById("ones"), scoreNumHtml: document.getElementById("score1")},
        {button : `twos`, html: document.getElementById("twos"), scoreNumHtml: document.getElementById("score2")},
        {button : `threes`, html: document.getElementById("threes"), scoreNumHtml: document.getElementById("score3")},
        {button : `fours`, html: document.getElementById("fours"), scoreNumHtml: document.getElementById("score4")},
        {button : `fives`, html: document.getElementById("fives"), scoreNumHtml: document.getElementById("score5")},
        {button : `sixes`, html: document.getElementById("sixes"), scoreNumHtml: document.getElementById("score6")},
    ],
    scoreLowerButtons: [
        {button : "3Kind", html: document.getElementById("3Kind"), scoreNumHtml: document.getElementById("score3Kind"), name: "threeOfAKind"},
        {button : "fullHouse", html: document.getElementById("fullHouse"), scoreNumHtml: document.getElementById("scoreFH"), name: "fullHouse"}
    ],
    dieLow: 1,
    dieHigh: 7,
    maxRolls: 3,
    bonus: false,
    bonusNum: 60,
    gameOver: false,
    numRolls: 0,
}

let scores = {
    ones: 0,
    twos: 0,
    threes: 0,
    fours: 0,
    fives: 0,
    sixes: 0,
    bonus: 35,
    threeOfAKind: 0,
    fullHouse: 0,
    rollNumber: 0,
    totalScore: 0,
    numTimesScored: 0,
    scoredones: false,
    scoredtwos: false,
    scoredthrees: false,
    scoredfours: false,
    scoredfives: false,
    scoredsixes: false,
    scoredthreeOfAKind: false,
    scoredfullHouse: false    
};

function save(){
    localStorage.setItem("game", JSON.stringify(game));
    localStorage.setItem("scores", JSON.stringify(scores));
    alert("Saved")
}
function load(){
    let gameStr = localStorage.getItem("game");
    let scoresStr = localStorage.getItem("scores");
    game = JSON.parse(gameStr);
    scores = JSON.parse(scoresStr);

    //Had to reset all of the elements and get their values back
    const resetButton = document.getElementById("reset");

    for(let i = 0; i <game.diceState.length; i++){
        game.diceState[i].dice = document.getElementById(`roll${i + 1}`)
    }
    game.scoreUpperButtons[0].html = document.getElementById("ones");
    game.scoreUpperButtons[1].html = document.getElementById("twos");
    game.scoreUpperButtons[2].html = document.getElementById("threes");
    game.scoreUpperButtons[3].html = document.getElementById("fours");
    game.scoreUpperButtons[4].html = document.getElementById("fives");
    game.scoreUpperButtons[5].html = document.getElementById("sixes");

    for(let i = 0; i < game.scoreUpperButtons.length; i++){
        game.scoreUpperButtons[i].scoreNumHtml = document.getElementById(`score${i+1}`);
    }
    game.scoreLowerButtons[0].html = document.getElementById("3Kind");
    game.scoreLowerButtons[1].html = document.getElementById("fullHouse");

    game.scoreLowerButtons[0].scoreNumHtml = document.getElementById("score3Kind");
    game.scoreLowerButtons[1].scoreNumHtml = document.getElementById("scoreFH");

    const rollButton = document.getElementById("roll");
    const saveButton = document.getElementById("save");
    const loadButton = document.getElementById("load");

    loadHTML();
    alert("Loaded")
}

//Sets everything back to the way it was before when it gets loaded up
function loadHTML(){
    for(let i = 0; i < game.diceState.length; i++){
        if(game.diceState[i].hold)
            game.diceState[i].dice.style.backgroundColor = "#e01a59"
        else{
            game.diceState[i].dice.style.backgroundColor = "#63c1a0"
        }
    }
    //turns off button if score is 0 and turns it on if score is more than 0
    for(let i = 0; i < game.scoreUpperButtons.length; i++){
        if(scores[game.scoreUpperButtons[i].button] >= 0 && scores[`scored${game.scoreUpperButtons[i].button}`]){
            game.scoreUpperButtons[i].html.style.display = "none";
            game.scoreUpperButtons[i].scoreNumHtml.style.display = "inline";
            game.scoreUpperButtons[i].scoreNumHtml.style.color = "black";
            game.scoreUpperButtons[i].scoreNumHtml.innerText = scores[game.scoreUpperButtons[i].button];
        }
        else{
            game.scoreUpperButtons[i].scoreNumHtml.style.display = "none";
            game.scoreUpperButtons[i].html.style.display = "inline"
        }
    }

    for(let i = 0; i < game.scoreLowerButtons.length; i++){
        if(scores[game.scoreLowerButtons[i].name] > 0 && scores[`scored${game.scoreLowerButtons[i].name}`]){
            game.scoreLowerButtons[i].html.style.display = "none";
            game.scoreLowerButtons[i].scoreNumHtml.style.display = "inline";
            game.scoreLowerButtons[i].scoreNumHtml.style.color = "black";
            game.scoreLowerButtons[i].scoreNumHtml.innerText = scores[game.scoreLowerButtons[i].name];
        }
        else{
            game.scoreLowerButtons[i].scoreNumHtml.style.display = "none";
            game.scoreLowerButtons[i].html.style.display = "inline";
        }
    }

    if(scores.totalScore < game.bonusNum){
        document.getElementById("bonus").style.backgroundColor = "#ecb32d"
    }
    else{
        document.getElementById("bonus").style.backgroundColor = "#e01a59"
    }
    checkGame();
    document.getElementById("score").innerText = `Total Score: ${scores.totalScore}, Rolls: ${game.numRolls}, Current Roll: ${scores.rollNumber}`;
    for(let i = 0; i < game.diceState.length; i++){
        game.diceState[i].dice.innerText = game.diceState[i].num;
    }

}

//Finds the type of roll they want to score on and calls a function based on the dice and the number
function calcScore(type){
    dice = [];
    for(let i = 0; i < game.diceState.length; i++){
        dice.push(game.diceState[i].num);
    }
    if(scores.rollNumber !== 0){
        if (type === "ones") {
            scores.ones = scoreUpper(dice, 1);
            scores.totalScore += scores.ones;
            scores.scoredones = true;
            updateUpperHTML(0, scores.ones);
        } 
        else if (type === "twos") {
            scores.twos = scoreUpper(dice, 2);
            scores.totalScore += scores.twos;
            scores.scoredtwos = true;
            updateUpperHTML(1, scores.twos);
        } 
        else if (type === "threes") {
            scores.threes = scoreUpper(dice, 3);
            scores.totalScore += scores.threes;
            scores.scoredthrees = true;
            updateUpperHTML(2,scores.threes);
        } 
        else if (type === "fours") {
            scores.fours = scoreUpper(dice, 4);
            scores.totalScore += scores.fours;
            scores.scoredfours = true;
            updateUpperHTML(3,scores.fours);
        } 
        else if (type === "fives") {
            scores.fives = scoreUpper(dice, 5);
            scores.totalScore += scores.fives;
            scores.scoredfives = true;
            updateUpperHTML(4,scores.fives);
        } 
        else if (type === "sixes") {
            scores.sixes = scoreUpper(dice, 6);
            scores.totalScore += scores.sixes;
            scores.scoredsixes = true;
            updateUpperHTML(5,scores.sixes);
        } 
        else if (type === "3Kind") {
            scores.threeOfAKind = scoreThreeKind(dice);
            scores.totalScore += scores.threeOfAKind;
            scores.scoredthreeOfAKind = true;
            updateLowerHTML(0, scores.threeOfAKind);
        } 
        else if (type === "fullHouse") {
            scores.fullHouse = scoreFullHouse(dice);
            scores.totalScore += scores.fullHouse;
            scores.scoredfullHouse = true;
            updateLowerHTML(1, scores.fullHouse);
        }
        scores.rollNumber = 0;
        checkGame();
        
    }
    else{
        //Will make them roll before scoring and will not allow them to score otherwise
        alert("Must roll before scoring")
    }

}

function updateUpperHTML(idx, score){
    //Hides the score button for the upper html and displays the score they achieved and updates the total and the rolls and the bonus
    game.scoreUpperButtons[idx].html.style.display = "none";
    game.scoreUpperButtons[idx].scoreNumHtml.style.display = "inline";
    game.scoreUpperButtons[idx].scoreNumHtml.style.color = "black";
    game.scoreUpperButtons[idx].scoreNumHtml.innerText = score;
    document.getElementById("score").innerText = `Total Score: ${scores.totalScore}, Rolls: ${game.numRolls}, Current Roll: ${scores.rollNumber}`;
    document.getElementById("bonus").innerText = `Bonus: ${scores.bonus} (Target: ${game.bonusNum}), Pts Needed: ${game.bonusNum - scores.totalScore}`
    for(let i = 0; i < game.diceState.length; i++){
        game.diceState[i].hold = false;
        game.diceState[i].dice.style.backgroundColor = "#63c1a0";
    }    
    //Resets the roll number to make sure they can roll again and adds to the number of the times they have scored so they cannot roll on the first view of the dice
    scores.rollNumber = 0;
    scores.numTimesScored++;
}
//Hides the score button for the lower html and displays the score they achieved and updates the total and the rolls and the bonus
function updateLowerHTML(idx, score){
    game.scoreLowerButtons[idx].html.style.display = "none";
    game.scoreLowerButtons[idx].scoreNumHtml.style.display = "inline";
    game.scoreLowerButtons[idx].scoreNumHtml.style.color = "black";
    game.scoreLowerButtons[idx].scoreNumHtml.innerText = score;
    document.getElementById("score").innerText = `Total Score: ${scores.totalScore}, Rolls: ${game.numRolls}, Current Roll: ${scores.rollNumber}`;
    document.getElementById("bonus").innerText = `Bonus: ${scores.bonus} (Target: ${game.bonusNum}), Pts Needed: ${game.bonusNum - scores.totalScore}`
    for(let i = 0; i < game.diceState.length; i++){
        game.diceState[i].hold = false;
        game.diceState[i].dice.style.backgroundColor = "#63c1a0";
    }    
    //Resets the roll number to make sure they can roll again and adds to the number of the times they have scored so they cannot roll on the first view of the dice
    scores.rollNumber = 0;
    scores.numTimesScored++;
}

function checkGame(){
    //Sees if the bonus has been achieved yet
    if(scores.totalScore >= game.bonusNum && !game.bonus){
        scores.totalScore += scores.bonus;
        game.bonus = true;
        document.getElementById("bonus").style.backgroundColor = "#e01a59"
        document.getElementById("bonus").innerText = `Bonus Achieved! Bonus: ${scores.bonus}`
        document.getElementById("score").innerText = `Total Score: ${scores.totalScore}, Rolls: ${game.numRolls}, Current Roll: ${scores.rollNumber}`;
        
    }
    else if(scores.totalScore >= game.bonusNum && game.bonus){
        document.getElementById("bonus").style.backgroundColor = "#e01a59"
        document.getElementById("bonus").innerText = `Bonus Achieved! Bonus: ${scores.bonus}`
        document.getElementById("score").innerText = `Total Score: ${scores.totalScore}, Rolls: ${game.numRolls}, Current Roll: ${scores.rollNumber}`;
    }
    else{
        document.getElementById("score").innerText = `Total Score: ${scores.totalScore}, Rolls: ${game.numRolls}, Current Roll: ${scores.rollNumber}`;
        document.getElementById("bonus").innerText = `Bonus: ${scores.bonus} (Target: ${game.bonusNum}), Pts Needed: ${game.bonusNum - scores.totalScore}`
    }
    //If all areas have been scored in, then end the game and display final score
    if(scores.numTimesScored === 8){
        alert(`Your Final Score: ${scores.totalScore}`)
        game.gameOver = true;
    }
    //Displays reset button when game over
    if(game.gameOver){
        document.getElementById("reset").style.display = "inline";
    }
}
//Counts the number of times each dice number has appeared. For example, [5,5,5,2,1], 5 has appeared 3 times, 2 one times, 1 one times
function getDiceCounts(dice){
    const counts = [0,0,0,0,0,0]
    dice.forEach(num => counts[num - 1]++);
    return counts;
}
//Counts the number of times a dice has appeared depending on the target and adds up all the types of the number
function scoreUpper(dice, target, scored){
    let total = 0;
    targetNums = dice.filter(num => num === target);
    for(let i = 0; i < targetNums.length; i++){
        total += targetNums[i];
    }
    scored = true;
    return total;
}

//Checks to see if there are three or more of a specific number and then they can score
function scoreThreeKind(dice){
    let total = 0;
    const counts = getDiceCounts(dice);
    let hasThreeKind = false;
    for(let i = 0; i < counts.length; i++){
        if(counts [i] >= 3)
            hasThreeKind = true;
    }
    if (hasThreeKind)
        total = 30
    return total;
}
//Checks to see if 3 exist and 2 exist of a 2 numbers to make sure it is a full house after calling the getDiceCounts function
function scoreFullHouse(dice){
    let total = 0;
    const counts = getDiceCounts(dice);
    const hasThree = counts.includes(3);
    const hasTwo = counts.includes(2);
    if(hasThree && hasTwo){
        total += 40;
    }
    return total;
}

//Cannot change the state of the dice (save or not) if you have not rolled yet. You also cannot change it back once already changed
function changeDiceState(idx, state){
    if(scores.rollNumber === 0){
        alert("Cannot save, you have not rolled yet")
    }
    else{
        if(state){
            alert("Cannot change die once it is being held")
        }
        else{
            game.diceState[idx].hold = true;
            game.diceState[idx].dice.style.backgroundColor = "#e01a59"
        }
    }

}
//Resets everything back to 0 and redisplays everything
function resetGame(){
    for(let i = 0; i < game.diceState.length; i++){
        game.diceState[i].hold = false;
        game.diceState[i].num = 1;
        game.diceState[i].dice.innerText = game.diceState[i].num;
    }
    game.gameOver = false;
    game.bonus = false;
    game.numRolls = 0;
    scores.ones = 0;
    scores.twos = 0;
    scores.threes = 0;
    scores.fours = 0;
    scores.fives = 0;
    scores.sixes = 0;
    scores.fullHouse = 0;
    scores.threeOfAKind = 0;
    scores.rollNumber = 0;
    scores.totalScore = 0;
    scores.numTimesScored = 0;
    scores.scoredones = false;
    scores.scoredtwos = false;
    scores.scoredthrees = false;
    scores.scoredfours = false;
    scores.scoredfives = false;
    scores.scoredsixes = false;
    scores.scoredthreeOfAKind = false;
    scores.scoredfullHouse = false;
    for(let i = 0; i < game.scoreLowerButtons.length; i++){
        game.scoreLowerButtons[i].scoreNumHtml.style.display = "none";
        game.scoreLowerButtons[i].html.style.display = "inline";
    }
    for(let i = 0; i < game.scoreUpperButtons.length; i++){
        game.scoreUpperButtons[i].scoreNumHtml.style.display = "none";
        game.scoreUpperButtons[i].html.style.display = "inline";
    }
    document.getElementById("bonus").style.backgroundColor = "#ecb32d";
    document.getElementById("score").innerText = `Total Score: ${scores.totalScore}, Rolls: ${game.numRolls}, Current Roll: ${scores.rollNumber}`;
    document.getElementById("bonus").innerText = `Bonus: ${scores.bonus} (Target: ${game.bonusNum}), Pts Needed: ${game.bonusNum - scores.totalScore}`

}
//Random number generator from 1-6 for a proper dice
function roll(){
    if(scores.rollNumber > 2){
        alert("Cannot roll, must score")
    }
    else{
        for(let i = 0; i < game.diceState.length; i++){
            if(game.diceState[i].hold === false){
                game.diceState[i].num = Math.floor(Math.random() * (game.dieHigh - game.dieLow) + game.dieLow)
                game.diceState[i].dice.innerText = game.diceState[i].num;
            }
        }
        scores.rollNumber++;
        game.numRolls++;
        document.getElementById("score").innerText = `Total Score: ${scores.totalScore}, Rolls: ${game.numRolls}, Current Roll: ${scores.rollNumber}`;
    }
    
}

document.addEventListener("DOMContentLoaded", function(){
    const resetButton = document.getElementById("reset");
    resetButton.addEventListener("click",resetGame);
    
    for(let i = 0; i < game.diceState.length; i++){
        game.diceState[i].dice.addEventListener("click", ()=> changeDiceState(i, game.diceState[i].hold));
    }

    for(let i = 0; i < game.scoreUpperButtons.length; i++){
        game.scoreUpperButtons[i].html.addEventListener("click", ()=> calcScore(game.scoreUpperButtons[i].button));
    }
    
    game.scoreLowerButtons[0].html.addEventListener("click", ()=> calcScore(game.scoreLowerButtons[0].button));
    game.scoreLowerButtons[1].html.addEventListener("click", ()=> calcScore(game.scoreLowerButtons[1].button));
    
    const rollButton = document.getElementById("roll");
    rollButton.addEventListener("click", roll);

    const saveButton = document.getElementById("save");
    saveButton.addEventListener("click", save);
    const loadButton = document.getElementById("load");
    loadButton.addEventListener("click", load);
    
})

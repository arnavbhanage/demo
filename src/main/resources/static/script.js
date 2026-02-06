let boardState = Array(9).fill("");
let currentPlayer = "X";


const board = document.getElementById("board");
const statusText = document.getElementById("status");
const themeToggle = document.getElementById("themeToggle");
const newGameBtn = document.getElementById("newGameBtn");


init();


function init() {
createBoard();
}


newGameBtn.addEventListener("click", () => {
boardState = Array(9).fill("");
currentPlayer = "X";
statusText.textContent = "Player X's turn";
createBoard();
});


themeToggle.addEventListener("click", () => {
document.body.classList.toggle("dark");
});


function createBoard() {
board.innerHTML = "";
boardState.forEach((_, index) => {
const cell = document.createElement("div");
cell.className = "cell";
cell.addEventListener("click", () => makeMove(index, cell));
board.appendChild(cell);
});
}


function makeMove(index, cell) {
if (boardState[index]) return;


boardState[index] = currentPlayer;
cell.textContent = currentPlayer;


currentPlayer = currentPlayer === "X" ? "O" : "X";
statusText.textContent = `Player ${currentPlayer}'s turn`;
}
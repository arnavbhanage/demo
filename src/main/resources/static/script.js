let gameId = null;
let gameOver = false;

const board = document.getElementById("board");
const statusText = document.getElementById("status");
const themeToggle = document.getElementById("themeToggle");
const newGameBtn = document.getElementById("newGameBtn");

init();

async function init() {
  await startNewGame();
}

newGameBtn.addEventListener("click", async () => {
  await startNewGame();
});

themeToggle.addEventListener("click", () => {
  document.body.classList.toggle("dark");
});

async function startNewGame() {
  gameOver = false;
  statusText.textContent = "Starting game...";

  const res = await fetch("/games", { method: "POST" });
  const game = await res.json();

  gameId = game.id;
  renderBoard(game);
}

async function makeMove(index) {
  if (gameOver) return;

  const res = await fetch(`/games/${gameId}/move/${index}`, { method: "POST" });

  if (!res.ok) return; // cell already taken, ignore

  const game = await res.json();
  renderBoard(game);
}

function renderBoard(game) {
  board.innerHTML = "";

  game.board.split("").forEach((piece, index) => {
    const cell = document.createElement("div");
    cell.className = "cell";

    if (piece !== "-") {
      cell.textContent = piece;
    }

    cell.addEventListener("click", () => makeMove(index));
    board.appendChild(cell);
  });

  switch (game.status) {
    case "IN_PROGRESS":
      statusText.textContent = `Player ${game.currentPlayer}'s turn`;
      break;
    case "X_WON":
      statusText.textContent = "Player X wins! 🎉";
      gameOver = true;
      break;
    case "O_WON":
      statusText.textContent = "Player O wins! 🎉";
      gameOver = true;
      break;
    case "DRAW":
      statusText.textContent = "It's a draw!";
      gameOver = true;
      break;
  }
}
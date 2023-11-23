let gameSettingsFormModal;
let gameEndModal;

let showGameSettingsModal;

function init() {
    gameSettingsFormModal = new bootstrap.Modal(document.getElementById('gameSettingsModal'), {
        keyboard: false
    });

    gameEndModal = new bootstrap.Modal(document.getElementById('gameEndModal'), {
        keyboard: false,
        backdrop: false
    });

    const params = new URLSearchParams(window.location.search);
    showGameSettingsModal = params.get('showSettings');
}

function renderBoard(game) {
    if (game.game_state === "GAME_OVER") {
        showGameEndModal(game.winner);
    }

    const players = game.total_players;
    const pitsPerPlayer = game.board.pits_for_player;
    const board = document.createElement('table');
    board.className = 'board-table';

    const pits = game.board.pits;

    for (let i = players - 1; i >= 0; i--) {
        const row = board.insertRow();
        row.className = `player-row-${i + 1}`;
        const playerPits = pits.slice(pitsPerPlayer * i + i, pitsPerPlayer  * (i + 1) + i + 1);

        if (i % 2 === 0) { // left-to-right for player 1, 3, ...
            appendAlignmentCell(row, game, i);

            for (let j = 0; j < playerPits.length - 1; j++) {
                appendPitCell(row, game, j, i, playerPits[j])
            }

            appendMancalaPit(row, playerPits);

        } else { // right-to-left for player 2, 4, ...
            appendMancalaPit(row, playerPits);

            for (let j = playerPits.length - 2; j >= 0; j--) {
                appendPitCell(row, game, j, i, playerPits[j])
            }

            appendAlignmentCell(row, game, i);
        }
    }

    const gameContainer = document.getElementById('gameContainer');
    gameContainer.innerHTML = '';
    gameContainer.appendChild(board);
}

function appendMancalaPit(row, playerPits) {
    const mancalaCell = row.insertCell();
    const mancalaDiv = document.createElement('div');
    mancalaDiv.className = 'mancala';
    mancalaDiv.textContent = playerPits[playerPits.length - 1];
    mancalaCell.appendChild(mancalaDiv);
}

function appendPitCell(row, game, pitIndex, rowIndex, pitValue) {
    const pitCell= row.insertCell();
    const pitDiv = document.createElement('div');

    let originalPitIndex = rowIndex * (game.board.pits_for_player + 1) + pitIndex;
    pitDiv.addEventListener('click', function() {
        makePitMove(game, originalPitIndex);
    });

    pitDiv.className = 'pit';
    pitDiv.textContent = pitValue;
    pitCell.appendChild(pitDiv);
}

function appendAlignmentCell(row, game, rowIndex) {
    const emptyCell = row.insertCell();

    if (rowIndex === game.current_player) {
        appendBlinkingDot(emptyCell);
    } else {
        emptyCell.appendChild(document.createElement('div'));
    }
}

function appendBlinkingDot(cell) {
    const dot = document.createElement('div');
    dot.className = 'blink-dot';
    cell.appendChild(dot);
}

function makePitMove(game, pitIndex) {
    fetch(`/api/v1/games/${game.id}/play`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            pit_index: pitIndex,
            version: game.version,
        }),
    }).then(response => {
        if (!response.ok) {
            return response.json().then(err => {
                throw err;
            });
        }
        return response.json();
    }).then(data => {
        console.log('Success:', data);
        renderBoard(data);
    }).catch((error) => {
        console.error('Error:', error);
        showErrorMessage(error.message ? error.message : 'Something went wrong. Please try again.');
    });
}

function startNewGame(formData) {
    fetch('/api/v1/games', { // Replace with your actual API endpoint
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: formData ? JSON.stringify(formData) : undefined,
    }).then(response => {
        if (!response.ok) {
            return response.json().then(err => {
                throw err; // This will be your JSON error object
            });
        }
        return response.json();
    }).then(data => {
        gameSettingsFormModal.hide();
        console.log('Success:', data);
        renderBoard(data);
    }).catch((error) => {
        console.error('Error:', error);
        showErrorMessage(error.message ? error.message : 'Something went wrong. Please try again.');
    });
}

function triggerConfetti() {
    const duration = 5 * 1000;
    const animationEnd = Date.now() + duration;
    const defaults = { startVelocity: 30, spread: 360, ticks: 60, zIndex: 0 };

    function randomInRange(min, max) {
        return Math.random() * (max - min) + min;
    }

    const interval = setInterval(function() {
        const timeLeft = animationEnd - Date.now();

        if (timeLeft <= 0) {
            return clearInterval(interval);
        }

        const particleCount = 50 * (timeLeft / duration);
        // Since particles fall down, start a bit higher than random
        confetti(Object.assign({}, defaults, { particleCount, origin: { x: randomInRange(0.1, 0.9), y: Math.random() - 0.2 } }));
    }, 250);
}

function showGameEndModal(winner) {
    const message = (winner !== undefined && winner != null) ? `Winner: Player ${winner + 1} 🎉` : "It's a draw!";
    const modal = document.getElementById('gameEndMessage') ;
    modal.textContent = message;

    gameEndModal.show();

    triggerConfetti();
}

function resetGame() {
    if (showGameSettingsModal) {
        gameSettingsFormModal.show();
    } else {
        startNewGame();
    }
}

function showErrorMessage(message) {
    const errorToast = new bootstrap.Toast(document.getElementById('errorToast'));
    const toastBody = document.querySelector('#errorToast .toast-body');

    toastBody.textContent = message || 'Something went wrong. Please try again.';

    errorToast.show();
}
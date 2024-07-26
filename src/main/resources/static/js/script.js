let from = "";

const cells = document.getElementsByClassName('cell_black');
for (let cell of cells) {
    cell.onclick = createMoveHandler(cell.id);
}

const roomId = +id.textContent;
subscribe(roomId, 0);

function createMoveHandler(cell) {
    return async function() {
        if (from === "") {
            from = cell;
            onCellChecked(cell);
        } else if (from === cell) {
            from = "";
            onCellUnchecked(cell);
        } else {
            const queryParams = new URLSearchParams({
                from,
                to: cell,
                login: login.textContent,
                id: id.textContent,
            });
            await fetch("/game?" + queryParams);
            from = "";
        }
    }
}

function onCellChecked(cell) {
    const cellElement = document.getElementById(cell);
    cellElement.classList.add("cell_checked");
}

function onCellUnchecked(cell) {
    const cellElement = document.getElementById(cell);
    cellElement.classList.remove("cell_checked");
}

async function subscribe(roomId, version) {
    try {
        const response = await fetch(`/room/${roomId}/board?version=${version}`);
        if (response.status === 502) {
            await subscribe(roomId, version);
        } else if (response.status !== 200) {
            console.log(response.statusText);
            await new Promise(resolve => setTimeout(resolve, 1000));
            await subscribe(roomId, version);
        } else {
            const board = await response.json();
            drawPieces(board.pieces);
            await subscribe(roomId, board.version);
        }
    } catch (e) {
        if (e.message = "Game finished") {
            window.location = "/game"
        }
    }
}

function drawPieces(pieces) {
    if (pieces.length === 0) {
        return;
    }

    checkEndgame(pieces);
    clearBoard();
    for (let piece of pieces) {
        const div = createPieceDiv(piece.type, piece.color);
        const cell = document.getElementById(piece.cell);
        cell.append(div);
    }
}

function checkEndgame(pieces) {
    const whitePiecesCount = pieces.filter(piece => piece.color === 'WHITE').length;
    const blackPiecesCount = pieces.filter(piece => piece.color === 'BLACK').length;
    if (whitePiecesCount == 0 || blackPiecesCount == 0) {
        throw new Error("Game finished");
    }
}

function clearBoard() {
    const cells = document.getElementsByClassName("cell");
    for (let cell of cells) {
        cell.classList.remove("cell_checked");
        for (let elem of cell.children) {
            elem.remove();
        }
    }
}

function createPieceDiv(type, color) {
    const div = document.createElement("div");
    div.className = getClassName(type, color);
    return div;
}


function getClassName(type, color) {
    if (type === "MAN") {
        if (color === "WHITE") {
            return "piece piece_white-man";
        } else {
            return "piece piece_black-man";
        }
    } else {
        if (color === "WHITE") {
            return "piece piece_white-king";
        } else {
            return "piece piece_black-king";
        }
    }
}


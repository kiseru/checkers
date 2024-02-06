let from = "";
let to = "";

const cells = document.getElementsByClassName('black-cell');
for (let cell of cells) {
    cell.onclick = createMoveHandler(cell.id);
}

function createMoveHandler(cell) {
    return async function() {
        if (from === "") {
            from = cell;
        } else if (from === cell) {
            from = "";
        } else {
            to = cell;
            const queryParams = new URLSearchParams({
                from,
                to,
                login: login.textContent,
                id: id.textContent,
            });
            await fetch("/game?" + queryParams);
            await drawCheckers();
            from = "";
            to = "";
        }
    }
}

let timerId = setTimeout(() => drawCheckers(), 0);

async function drawCheckers() {
    const roomId = +id.textContent;
    const pieces = await getPieces(roomId);
    if (pieces.length === 0) {
        return;
    }

    clearBoard();
    for (let piece of pieces) {
        const div = createPieceDiv(piece.type, piece.color);
        const cell = document.getElementById(piece.cell);
        cell.append(div);
    }
    timerId = setTimeout(() => drawCheckers(), 1000);
}

async function getPieces(roomId) {
    try {
        const response = await fetch(`/room/${roomId}/piece`);
        return await response.json();
    } catch (e) {
        return [];
    }
}

function clearBoard() {
    const cells = document.getElementsByClassName("cell");
    for (let cell of cells) {
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
            return "piece white-man";
        } else {
            return "piece black-man";
        }
    } else {
        if (color === "WHITE") {
            return "piece white-king";
        } else {
            return "piece black-king";
        }
    }
}


$(document).ready(function () {
    var from = "";
    var to = "";

    $('#a1').click(function () {
        if (from.localeCompare("") === 0) {
            from = "a1";
        } else if (from.localeCompare("a1") === 0) {
            from = "";
        } else {
            to = "a1";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#a3').click(function () {
        if (from.localeCompare("") === 0) {
            from = "a3";
        } else if (from.localeCompare("a3") === 0) {
            from = "";
        } else {
            to = "a3";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#a5').click(function () {
        if (from.localeCompare("") === 0) {
            from = "a5";
        } else if (from.localeCompare("a5") === 0) {
            from = "";
        } else {
            to = "a5";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#a7').click(function () {
        if (from.localeCompare("") === 0) {
            from = "a7";
        } else if (from.localeCompare("a7") === 0) {
            from = "";
        } else {
            to = "a7";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#b2').click(function () {
        if (from.localeCompare("") === 0) {
            from = "b2";
        } else if (from.localeCompare("b2") === 0) {
            from = "";
        } else {
            to = "b2";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#b4').click(function () {
        if (from.localeCompare("") === 0) {
            from = "b4";
        } else if (from.localeCompare("b4") === 0) {
            from = "";
        } else {
            to = "b4";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#b6').click(function () {
        if (from.localeCompare("") === 0) {
            from = "b6";
        } else if (from.localeCompare("b6") === 0) {
            from = "";
        } else {
            to = "b6";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#b8').click(function () {
        if (from.localeCompare("") === 0) {
            from = "b8";
        } else if (from.localeCompare("b8") === 0) {
            from = "";
        } else {
            to = "b8";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#c1').click(function () {
        if (from.localeCompare("") === 0) {
            from = "c1";
        } else if (from.localeCompare("c1") === 0) {
            from = "";
        } else {
            to = "c1";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#c3').click(function () {
        if (from.localeCompare("") === 0) {
            from = "c3";
        } else if (from.localeCompare("c3") === 0) {
            from = "";
        } else {
            to = "c3";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#c5').click(function () {
        if (from.localeCompare("") === 0) {
            from = "c5";
        } else if (from.localeCompare("c5") === 0) {
            from = "";
        } else {
            to = "c5";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#c7').click(function () {
        if (from.localeCompare("") === 0) {
            from = "c7";
        } else if (from.localeCompare("c7") === 0) {
            from = "";
        } else {
            to = "c7";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#d2').click(function () {
        if (from.localeCompare("") === 0) {
            from = "d2";
        } else if (from.localeCompare("d2") === 0) {
            from = "";
        } else {
            to = "d2";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#d4').click(function () {
        if (from.localeCompare("") === 0) {
            from = "d4";
        } else if (from.localeCompare("d4") === 0) {
            from = "";
        } else {
            to = "d4";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#d6').click(function () {
        if (from.localeCompare("") === 0) {
            from = "d6";
        } else if (from.localeCompare("d6") === 0) {
            from = "";
        } else {
            to = "d6";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#d8').click(function () {
        if (from.localeCompare("") === 0) {
            from = "d8";
        } else if (from.localeCompare("d8") === 0) {
            from = "";
        } else {
            to = "d8";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#e1').click(function () {
        if (from.localeCompare("") === 0) {
            from = "e1";
        } else if (from.localeCompare("e1") === 0) {
            from = "";
        } else {
            to = "e1";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#e3').click(function () {
        if (from.localeCompare("") === 0) {
            from = "e3";
        } else if (from.localeCompare("e3") === 0) {
            from = "";
        } else {
            to = "e3";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#e5').click(function () {
        if (from.localeCompare("") === 0) {
            from = "e5";
        } else if (from.localeCompare("e5") === 0) {
            from = "";
        } else {
            to = "e5";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#e7').click(function () {
        if (from.localeCompare("") === 0) {
            from = "e7";
        } else if (from.localeCompare("e7") === 0) {
            from = "";
        } else {
            to = "e7";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#f2').click(function () {
        if (from.localeCompare("") === 0) {
            from = "f2";
        } else if (from.localeCompare("f2") === 0) {
            from = "";
        } else {
            to = "f2";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#f4').click(function () {
        if (from.localeCompare("") === 0) {
            from = "f4";
        } else if (from.localeCompare("f4") === 0) {
            from = "";
        } else {
            to = "f4";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#f6').click(function () {
        if (from.localeCompare("") === 0) {
            from = "f6";
        } else if (from.localeCompare("f6") === 0) {
            from = "";
        } else {
            to = "f6";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#f8').click(function () {
        if (from.localeCompare("") === 0) {
            from = "f8";
        } else if (from.localeCompare("f8") === 0) {
            from = "";
        } else {
            to = "f8";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#g1').click(function () {
        if (from.localeCompare("") === 0) {
            from = "g1";
        } else if (from.localeCompare("g1") === 0) {
            from = "";
        } else {
            to = "g1";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#g3').click(function () {
        if (from.localeCompare("") === 0) {
            from = "g3";
        } else if (from.localeCompare("c3") === 0) {
            from = "";
        } else {
            to = "g3";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#g5').click(function () {
        if (from.localeCompare("") === 0) {
            from = "g5";
        } else if (from.localeCompare("g5") === 0) {
            from = "";
        } else {
            to = "g5";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#g7').click(function () {
        if (from.localeCompare("") === 0) {
            from = "g7";
        } else if (from.localeCompare("g7") === 0) {
            from = "";
        } else {
            to = "g7";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#h2').click(function () {
        if (from.localeCompare("") === 0) {
            from = "h2";
        } else if (from.localeCompare("h2") === 0) {
            from = "";
        } else {
            to = "h2";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#h4').click(function () {
        if (from.localeCompare("") === 0) {
            from = "h4";
        } else if (from.localeCompare("h4") === 0) {
            from = "";
        } else {
            to = "h4";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#h6').click(function () {
        if (from.localeCompare("") === 0) {
            from = "h6";
        } else if (from.localeCompare("h6") === 0) {
            from = "";
        } else {
            to = "h6";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    $('#h8').click(function () {
        if (from.localeCompare("") === 0) {
            from = "h8";
        } else if (from.localeCompare("h8") === 0) {
            from = "";
        } else {
            to = "h8";
            $.get('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            clear();
        }
    });

    function clear() {
        from = "";
        to = "";
        drawCheckers();
    }
});

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


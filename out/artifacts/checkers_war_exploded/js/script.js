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
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#a3').click(function () {
        if (from.localeCompare("") === 0) {
            from = "a3";
        } else if (from.localeCompare("a3") === 0) {
            from = "";
        } else {
            to = "a3";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#a5').click(function () {
        if (from.localeCompare("") === 0) {
            from = "a5";
        } else if (from.localeCompare("a5") === 0) {
            from = "";
        } else {
            to = "a5";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#a7').click(function () {
        if (from.localeCompare("") === 0) {
            from = "a7";
        } else if (from.localeCompare("a7") === 0) {
            from = "";
        } else {
            to = "a7";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#b2').click(function () {
        if (from.localeCompare("") === 0) {
            from = "b2";
        } else if (from.localeCompare("b2") === 0) {
            from = "";
        } else {
            to = "b2";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#b4').click(function () {
        if (from.localeCompare("") === 0) {
            from = "b4";
        } else if (from.localeCompare("b4") === 0) {
            from = "";
        } else {
            to = "b4";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#b6').click(function () {
        if (from.localeCompare("") === 0) {
            from = "b6";
        } else if (from.localeCompare("b6") === 0) {
            from = "";
        } else {
            to = "b6";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#b8').click(function () {
        if (from.localeCompare("") === 0) {
            from = "b8";
        } else if (from.localeCompare("b8") === 0) {
            from = "";
        } else {
            to = "b8";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#c1').click(function () {
        if (from.localeCompare("") === 0) {
            from = "c1";
        } else if (from.localeCompare("c1") === 0) {
            from = "";
        } else {
            to = "c1";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#c3').click(function () {
        if (from.localeCompare("") === 0) {
            from = "c3";
        } else if (from.localeCompare("c3") === 0) {
            from = "";
        } else {
            to = "c3";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#c5').click(function () {
        if (from.localeCompare("") === 0) {
            from = "c5";
        } else if (from.localeCompare("c5") === 0) {
            from = "";
        } else {
            to = "c5";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#c7').click(function () {
        if (from.localeCompare("") === 0) {
            from = "c7";
        } else if (from.localeCompare("c7") === 0) {
            from = "";
        } else {
            to = "c7";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#d2').click(function () {
        if (from.localeCompare("") === 0) {
            from = "d2";
        } else if (from.localeCompare("d2") === 0) {
            from = "";
        } else {
            to = "d2";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#d4').click(function () {
        if (from.localeCompare("") === 0) {
            from = "d4";
        } else if (from.localeCompare("d4") === 0) {
            from = "";
        } else {
            to = "d4";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#d6').click(function () {
        if (from.localeCompare("") === 0) {
            from = "d6";
        } else if (from.localeCompare("d6") === 0) {
            from = "";
        } else {
            to = "d6";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#d8').click(function () {
        if (from.localeCompare("") === 0) {
            from = "d8";
        } else if (from.localeCompare("d8") === 0) {
            from = "";
        } else {
            to = "d8";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#e1').click(function () {
        if (from.localeCompare("") === 0) {
            from = "e1";
        } else if (from.localeCompare("e1") === 0) {
            from = "";
        } else {
            to = "e1";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#e3').click(function () {
        if (from.localeCompare("") === 0) {
            from = "e3";
        } else if (from.localeCompare("e3") === 0) {
            from = "";
        } else {
            to = "e3";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#e5').click(function () {
        if (from.localeCompare("") === 0) {
            from = "e5";
        } else if (from.localeCompare("e5") === 0) {
            from = "";
        } else {
            to = "e5";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#e7').click(function () {
        if (from.localeCompare("") === 0) {
            from = "e7";
        } else if (from.localeCompare("e7") === 0) {
            from = "";
        } else {
            to = "e7";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#f2').click(function () {
        if (from.localeCompare("") === 0) {
            from = "f2";
        } else if (from.localeCompare("f2") === 0) {
            from = "";
        } else {
            to = "f2";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#f4').click(function () {
        if (from.localeCompare("") === 0) {
            from = "f4";
        } else if (from.localeCompare("f4") === 0) {
            from = "";
        } else {
            to = "f4";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#f6').click(function () {
        if (from.localeCompare("") === 0) {
            from = "f6";
        } else if (from.localeCompare("f6") === 0) {
            from = "";
        } else {
            to = "f6";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#f8').click(function () {
        if (from.localeCompare("") === 0) {
            from = "f8";
        } else if (from.localeCompare("f8") === 0) {
            from = "";
        } else {
            to = "f8";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#g1').click(function () {
        if (from.localeCompare("") === 0) {
            from = "g1";
        } else if (from.localeCompare("g1") === 0) {
            from = "";
        } else {
            to = "g1";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#g3').click(function () {
        if (from.localeCompare("") === 0) {
            from = "g3";
        } else if (from.localeCompare("c3") === 0) {
            from = "";
        } else {
            to = "g3";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#g5').click(function () {
        if (from.localeCompare("") === 0) {
            from = "g5";
        } else if (from.localeCompare("g5") === 0) {
            from = "";
        } else {
            to = "g5";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#g7').click(function () {
        if (from.localeCompare("") === 0) {
            from = "g7";
        } else if (from.localeCompare("g7") === 0) {
            from = "";h        } else {
            to = "g7";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#h2').click(function () {
        if (from.localeCompare("") === 0) {
            from = "h2";
        } else if (from.localeCompare("h2") === 0) {
            from = "";
        } else {
            to = "h2";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#h4').click(function () {
        if (from.localeCompare("") === 0) {
            from = "h4";
        } else if (from.localeCompare("h4") === 0) {
            from = "";
        } else {
            to = "h4";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#h6').click(function () {
        if (from.localeCompare("") === 0) {
            from = "h6";
        } else if (from.localeCompare("h6") === 0) {
            from = "";
        } else {
            to = "h6";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });

    $('#h8').click(function () {
        if (from.localeCompare("") === 0) {
            from = "h8";
        } else if (from.localeCompare("h8") === 0) {
            from = "";
        } else {
            to = "h8";
            $.post('/game', {
                'from': from,
                'to': to,
                'login': $('#login').text(),
                'id': $('#id').text()
            });
            location.reload();
        }
    });
});
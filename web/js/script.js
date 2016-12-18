$(document).ready(function () {
    var from = "";
    var to = "";

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
                'login': $('#login').text()
            });
        }
    });

    $('#b4').click(function () {
        if (from.localeCompare("") === 0) {
            from = "b4";
        } else if (from.localeCompare("b4") === 0) {
            from = "";
        } else {
            to = "b4";
            $.post('game', {
                'from': from,
                'to': to,
                'login': $('#login').text()
            });
        }
    });
});
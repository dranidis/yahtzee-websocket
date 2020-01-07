$(document).ready(function () {
    $("#gameControl").hide();
    $('#startBtn').hide();

    var playerName;
    var disabled;
    var stopAnimation = false;
    var end = false;

    function connect() {
        var socket = new SockJS('/gamews');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/players', function (player) {
                showPlayer(JSON.parse(player.body).name);
            });
            stompClient.subscribe('/topic/game', function (game) {
                if (!end)
                    handleGame(JSON.parse(game.body));
            });
            stompClient.subscribe('/topic/end', function (endresponse) {
                end = true;
                endGame(JSON.parse(endresponse.body).message);
            });
            stompClient.subscribe('/topic/rolling', function (msg) {
                rollAnimation(JSON.parse(msg.body).keep);
            });
        });
    }
    connect();
    // TODO: After connecting get players already in the game

    function showPlayer(name) {
        $("#players").append("<div>" + name + "</div>");
    }

    /*
    Handle for all players the game broadcast
    */
    function handleGame(game) {
        $("#startBtn").prop("disabled", true);
        $("#gameControl").show();

        if (game.currentPlayer == playerName) {
            disableControl(false);
            updateScoreSheet(game);
            $('#currentPlayer').html("You");
            $('#keepBtn').prop("disabled", game.roll >= 3);

            if (game.categoryName != "") {
                $('#' + game.categoryName).html(game.score);
            }
        } else {
            disableControl(true);
            $('#currentPlayer').html(game.currentPlayer);
            $('#categoryScored').html(game.categoryName);
            $('#score').html(game.score);
        }
        stopAnimation = true;
        sleep(100).then(() => {
            displayDice(game.dice);
        });

    }

    $("#addPlayerBtn").click(function () {
        var name = $("#playerName").val();
        // remember the player's name
        playerName = name;

        if (name != "") {
            stompClient.send("/app/player", {}, JSON.stringify({ 'name': name }));
            $("#addPlayerBtn").prop("disabled", true);
            $('#nameenter').hide();
            $('#startBtn').show();
        }
    });

    $("#startBtn").click(function () {
        stompClient.send("/app/start", {});
    });


    function succesFun(result) {
        $("#div1").html(result);
    }

    $('.die').click(function () {
        if (disabled) return;
        console.log(this.id);
        if ($('#' + this.id).hasClass('dieKeep')) {
            $('#' + this.id).removeClass('dieKeep');
        } else {
            $('#' + this.id).addClass('dieKeep');
        }
    })

    $("#keepBtn").click(function () {
        var keepArray = [];
        for (i = 1; i < 6; i++) {
            if ($('#die' + i).hasClass('dieKeep')) {
                // if ($("#keep" + i).is(':checked')) {
                keepArray.push(i);
            }
            // var keepValue = keepArray.join();
        }
        stompClient.send("/app/roll", {}, JSON.stringify({ 'name': playerName, 'keep': keepArray }));
    });

    $(".catscore").click(function () {
        if (disabled) return; // not your turn!

        var tid = this.id;

        if ($('#' + tid).html() != '') {
            return;
        }
        stompClient.send("/app/score", {}, JSON.stringify({ 'name': playerName, 'category': this.id }));

        clearKeep();
    });

    function disableControl(boolean) {
        console.log("Disable controls :" + boolean)

        $('#keepBtn').prop("disabled", boolean);
        $('.keep').prop("disabled", boolean);
        disabled = boolean;
    }

    function clearKeep() {
        // $('.keep').prop('checked', false);
        for (i = 1; i <= 5; i++) {
            $('#die' + i).removeClass('dieKeep');
        }
    }

    function updateScoreSheet(game) {
        console.log("UpdateScoreSheet: " + game.sheet.content.UB + ", " + game.sheet.content.US + ", " + game.sheet.content.YB + ", " + game.sheet.content.TS);
        $('#UB').html(game.sheet.content.UB);
        $('#US').html(game.sheet.content.US);
        $('#YB').html(game.sheet.content.YB);
        $('#TS').html(game.sheet.content.TS);
    }

    function displayDice(dice) {
        for (i = 0; i < 5; i++) {
            $('#die' + (i + 1)).html(dice[i]);
        }
    }

    function endGame(message) {
        disableControl(true);
    }

    // sleep time expects milliseconds
    function sleep(time) {
        return new Promise((resolve) => setTimeout(resolve, time));
    }

    function recursiveRollAnimation(keepArray, step) {
        if (stopAnimation) {
            return;
        }
        sleep(50).then(() => {
            if (stopAnimation)
                return;
            for (i = 1; i <= 5 && !stopAnimation; i++) {
                if (!keepArray.includes(i)) {
                    $('#die' + i).html(Math.floor(Math.random() * 5) + 1);
                }
            }
            recursiveRollAnimation(keepArray, step - 1);
        });
    }

    function rollAnimation(keepArray) {
        console.log('disable keep');
        // $('#keepBtn').prop("disabled", true);
        disableControl(true);
        stopAnimation = false;
        recursiveRollAnimation(keepArray, 19)
    }

});
// JavaScript source code

function login() {
    console.log($('#idInput').val());
    console.log($('#pwInput').val());

    var d = {
        "type": "login",
        "id": $('#idInput').val(),
        "password": $('#pwInput').val()
    };

    $.ajax({
        url: 'https://ecseuah8l2.execute-api.ap-northeast-2.amazonaws.com/here/user',
        type: 'POST',
        dataType: "JSON",
        contentType: "application/json",
        data: JSON.stringify(d),
        crossDomain: true,
        cache: false,
        success: function (data) {
            console.log(data);
            localStorage.id = data.id;
            localStorage.password = data.password;
            localStorage.name = data.name;
            localStorage.cash = data.cash;
        },
        error: function (e) {
            console.log(e);
        }
    });
}


function signup() {
    console.log($('#idInput').val());
    console.log($('#pwInput').val());
    console.log($('#pwInput-c').val());

    if ($('#pwInput').val() != $('#pwInput-c').val()) {
        alert("비밀번호를 확인해주세요");
        return;
    }

    var d = {
        "type": "signup",
        "id": $('#idInput').val(),
        "password": $('#pwInput').val(),
        "name": $('#nameInput').val()
    };

    $.ajax({
        url: 'https://ecseuah8l2.execute-api.ap-northeast-2.amazonaws.com/here/user',
        type: 'POST',
        dataType: "JSON",
        contentType: "application/json",
        data: JSON.stringify(d),
        crossDomain: true,
        cache: false,
        success: function (data) {
            console.log(data);
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function charge() {
    var d = {
        "id": localStorage.id,
        "cash": localStorage.cash * 1 + $('#cashInput').val() * 1,
    };

    $.ajax({
        url: 'https://ecseuah8l2.execute-api.ap-northeast-2.amazonaws.com/here/user/sponsor',
        type: 'POST',
        dataType: "JSON",
        contentType: "application/json",
        data: JSON.stringify(d),
        crossDomain: true,
        cache: false,
        success: function (data) {
            console.log(data);
            localStorage.cash = d.cash;
        },
        error: function (e) {
            console.log(e);
        }
    });
}


function getSponsor() {
    $.ajax({
        url: 'https://ecseuah8l2.execute-api.ap-northeast-2.amazonaws.com/here/user/sponsor',
        type: 'GET',
        dataType: "JSON",
        contentType: "application/json",
        crossDomain: true,
        cache: false,
        success: function (data) {
            console.log(data);
            $('#result').text(JSON.stringify(data));
            console.log($('#result').text());
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function support() {
    var d = {
        "project_num": $('#projectNum').val(),
        "id": localStorage.id,
        "cash": $('#cashInput').val()
    };

    console.log(d);

    $.ajax({
        url: 'https://ecseuah8l2.execute-api.ap-northeast-2.amazonaws.com/here/user/sponsor',
        type: 'PUT',
        dataType: "JSON",
        contentType: "application/json",
        data: JSON.stringify(d),
        crossDomain: true,
        cache: false,
        success: function (data) {
            console.log(data);
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function post_alarm() {
    var d = {
        "project_num": $('#projectNum').val(),
        "receiver": $('#receiver').val(),
        "sender": localStorage.id,
        "message": $('#message').val()
    };

    console.log(d);

    $.ajax({
        url: 'https://ecseuah8l2.execute-api.ap-northeast-2.amazonaws.com/here/alert',
        type: 'POST',
        dataType: "JSON",
        contentType: "application/json",
        data: JSON.stringify(d),
        crossDomain: true,
        cache: false,
        success: function (data) {
            console.log(data);
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function todayproject_alarm() {
    $.ajax({
        url: 'https://ecseuah8l2.execute-api.ap-northeast-2.amazonaws.com/here/project',
        type: 'GET',
        dataType: "JSON",
        contentType: "application/json",
        crossDomain: true,
        cache: false,
        success: function (data) {
            console.log(data);
            $('#result').text(JSON.stringify(data));
        },
        error: function (e) {
            console.log(e);
        }
    });
}
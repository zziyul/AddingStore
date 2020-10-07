exports.handler = (event, context, callback) => {
    var mysql = require('mysql');

    var con = mysql.createConnection({
        host: "cloudfunding.cmavyiefeafb.ap-northeast-2.rds.amazonaws.com",
        user: "han",
        password: "han12345",
        database: "cloudfunding"
    });


    if (event.type == 'login') { // 로그인
        con.connect(function (err) {
            if (err) {
                callback(err);
            }

            con.query("select * from user where id=? and password=?", [event.id, event.password], function (err, result) {
                if (err) {
                    callback(err);
                } else {
                    console.log(event.name);

                    callback(null, result[0]);
                }
                con.end();
            });
        });
    } else if (event.type == 'signup') { // 회원가입
        con.connect(function (err) {
            if (err) {
                callback(err);
            }

            con.query("insert into user(id,name,password) values(?,?,?)", [event.id, event.name, event.password], function (err, result) {
                if (err) {
                    callback(err);
                } else {
                    callback(null, true);
                }
                con.end();
            });
        });
    }

};
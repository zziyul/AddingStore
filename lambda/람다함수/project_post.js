exports.handler = (event, context, callback) => {
    var mysql = require('mysql');

    var con = mysql.createConnection({
        host: "cloudfunding.cmavyiefeafb.ap-northeast-2.rds.amazonaws.com",
        user: "han",
        password: "han12345",
        database: "cloudfunding"
    });

    con.connect(function (err) {
        if (err) {
            callback(err);
        }

        con.query('insert into project(name,owner,hfunding, Explanation, ref_num) values(?,?,?, ?, ?)', [event.name, event.owner, event.hfunding, event.Explanation, event.ref_num], function (err, result) {
            if (err) {
                callback(err);
            } else {
                console.log(event.id);

                callback(null, event.name + "이 성공적으로 등록되었습니다!");
            }
            con.end();
        });
    });
};
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

        con.query('update user SET cash = ? where id = ?', [event.cash, event.id], function (err, result) {
            if (err) {
                callback(err);
            } else {
                console.log("update " + event.id + " " + event.cash);

                callback(null, "update cash done");
            }
            con.end();
        });
    });
};
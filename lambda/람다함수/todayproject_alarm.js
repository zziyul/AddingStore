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

        con.query('select * from project order by rand() limit 5', function (err, result) {
            if (err) {
                callback(err);
            } else {
                callback(null, result);
            }
            con.end();
        });
    });
};
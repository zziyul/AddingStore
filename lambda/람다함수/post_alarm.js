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

        con.query('insert into alert(project_num,receiver,sender,message) values(?,?,?,?)', [event.project_num, event.receiver, event.sender, event.message], function (err, result) {
            if (err) {
                callback(err);
            } else {
                console.log(event.id);

                callback(null, result[0]);
            }
            con.end();
        });
    });
};
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

        con.query('insert into scrum(project_num,s_name,state) values(?,?,?)', [event.project_num, event.s_name, event.state], function (err, result) {
            if (err) {
                callback(err);
            } else {
                console.log("post " + event.project_num + " " + event.s_name + " " + event.state);

                callback(null, " ");
            }
            con.end();
        });
    });
};
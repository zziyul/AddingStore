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

        con.query('UPDATE project SET name = ?, hfunding= ?, Explanation = ?,nfunding=?  where project_num = ?', [event.name, event.hfunding, event.Explanation, event.nfunding, event.project_num], function (err, result) {
            if (err) {
                callback(err);
            } else {

                callback(null, "������Ʈ " + event.name + " ������Ʈ �Ϸ�!");
            }
            con.end();
        });
    });
};
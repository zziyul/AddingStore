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

        con.query('delete from sponsor where project_num = ?', [event.project_num], function (err, result) {
            if (err) {
                callback(err);
            } else {
                console.log(event.project_num);

                con.query('delete from alert where project_num = ?', [event.project_num], function (err, result) {
                    if (err) {
                        callback(err);
                    } else {
                        console.log(event.project_num);

                        con.query('delete from scrum where project_num = ?', [event.project_num], function (err, result) {
                            if (err) {
                                callback(err);
                            } else {
                                console.log(event.project_num);

                                con.query('delete from project where project_num = ?', [event.project_num], function (err, result) {
                                    if (err) {
                                        callback(err);
                                    } else {
                                        console.log(event.project_num);
                                        callback(null, event.project_num + "번 프로젝트가 삭제 되었습니다.");
                                    }
                                    con.end();
                                });

                            }
                        });


                    }
                });

            }
        });

    });
};
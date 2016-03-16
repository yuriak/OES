/**
 * Created by S.F.S on 2016/3/6.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {


    /*var token = Cookies.get('token');
    if (token == null || token == '') {
        showErrMsg("未登录，请先登录！");
        location.href = "login.html";
        return false;
    }

    $.ajax({
        type: 'POST',
        url:,
        //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
        contentType: "application/json",
        data: {},
        beforeSend: function () {
            // 禁用按钮防止重复提交

        },
        success: function (data) {
            var code = data.code,
                errMsg = data.errMsg,
                reason = data.reason,
                success = data.success;
            if (code === 1) {
                if (success === true) {
                    //成功后执行的业务


                } else {
                    showErrMsg(reason);
                }
            } else {
                showErrMsg(errMsg);
                window.location.reload(false);
            }
        },
        complete: function () {
        },
        error: function () {
            showServiceError();
        },
        dataType: 'json'
    });
*/
    var list=$("li.my-list");

        for(var i=0;i<3;i++){

            //循环导入文本
    }


});
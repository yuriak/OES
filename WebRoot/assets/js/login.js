document.write("<script src='assets/js/configure.js'></script>");
document.write("<script src='assets/js/surprise.js'></script>");
$(function () {
    //获取可视区域的宽度和高度(无单位)
    var cHeight = document.documentElement.clientHeight;
    var cWidth = document.documentElement.clientWidth;
    //获取整个页面的宽度和高度（无单位）
    var sWidth = document.documentElement.scrollWidth;
    var sHeight = document.documentElement.scrollHeight;

    //固定界面的宽高为屏幕宽高
    $('#indexImg').css({'width': cWidth, 'height': cHeight});
    $('#mask').css({'width': cWidth, 'height': cHeight});

    //元素的高度和宽度(无单位)
    var loginWidth = document.getElementById("loginPanel").offsetWidth;
    var loginHeight = document.getElementById("loginPanel").offsetHeight;

    var logoWidth = document.getElementById("indexLogo").offsetWidth;
    var logoHeight = document.getElementById("indexLogo").offsetHeight;

    $("#loginPanel").css({"top": (cHeight - loginHeight) / 2, "left": (cWidth - loginWidth) / 2});
    $("#indexLogo").css({"top": (cHeight - logoHeight) / 2 - loginHeight / 4 * 3, "left": (cWidth - logoWidth) / 2})
    $("#my-register").css({"top": (cHeight - loginHeight) / 2 + loginHeight + 20, "left": (cWidth - loginWidth) / 2});




    var token = Cookies.get('token');
    if (token != null) {
        var role = parseInt(Cookies.get('role'));
        if(role===0){
            location.href = "studentIndex.html";
        }else if(role ===1){
            location.href = "teacherIndex.html";
        }
    }else{

    }

    //登录时向服务器发送请求
    $('#loginIn').click(function () {
        var userName = $("input[name='userName']").val();
        var password = $("input[name='password']").val();
        if (userName.indexOf(" ") >= 0 || password.indexOf(" ") >= 0) {
            showErrMsg("用户名或密码不能出现空格!")
            return false;
        } else if (userName === null || password === null || userName.length === 0 || password.length === 0) {
            showErrMsg("用户名或密码不能为空!");
            return false;
        }
        $.ajax({
            type: 'POST',
            url: 'API/user/login',
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "userName": userName,
                "password": password
            },
            beforeSend: function () {
                // 禁用按钮防止重复提交
                $("#loginIn").attr("disabled", "disabled");
            },
            success: function (data) {
                var code = data.code,
                    errMsg = data.errMsg,
                    success = data.success,
                    reason = data.reason;
                if (code === 1) {
                    if (success === true) {
                        var token = data.token,
                            role = data.role;
                        Cookies.set('token', token, {expires: 99999, path: '/'});
                        Cookies.set('role', role, {expires: 99999, path: '/'});
                        if (role == 0) {
                            location.href = "studentIndex.html";
                        } else {
                            location.href = "teacherIndex.html";
                        }
                    } else {
                        showErrMsg(reason);
                    }
                } else {
                    showErrMsg(errMsg);
                }

            },
            complete: function () {
                $("#loginIn").removeAttr("disabled");
            },
            error: function () {
                showServiceError();
            },
            dataType: 'json'
        });

    });

});

 

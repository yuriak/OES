/**
 * Created by fei on 2016/3/6.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {
   var token = Cookies.get('token');
     if (token == null || token == '') {
     showErrMsg("未登录，请先登录！");
     window.location.href = "login.html";
     return false;
     }

//声明并取得变量,表单对象
    var oReceiver = $('#msg-user');
    var oTitle = $('#msg-title');
    var oContent = $('#msg-content');

//如果是回复或者再来一条会有用户名信息存在cookie
    var receiverName=Cookies.get('receiver');
    if(receiverName!=null){
        oReceiver.val(receiverName);
        Cookies.remove('receiver');
    }



//加急 1为加急邮件,反之不加急
        level=0;
        $('#my-toggle').click(function () {
            if (level==0) {
                $('#my-i').attr('class', 'am-icon-toggle-on am-icon-sm');
                $('#my-i').attr('style', 'color: #9AFB92');
                level = 1;
            }
            else {
                $('#my-i').attr('class', 'am-icon-toggle-off am-icon-sm');
                $('#my-i').attr('style', 'color:  #fbf8f2');
                level = 0;
            }
        });



//发送按钮的激活与不激活
    oReceiver.bind('input', confirmSubmit);
    oTitle.bind('input', confirmSubmit);
    oContent.bind('input', confirmSubmit);
    function confirmSubmit() {
        var userName = oReceiver.val(),
            msg = oTitle.val(),
            con = oContent.val();
        var _boo = true;
        if (userName.length == 0 || userName.length == null) {
            _boo = false;
        }
        if (msg.length == 0 || msg.length == null) {
            _boo = false;
        }
        if (con.length == 0 || con.length == null) {
            _boo = false;
        }
        if (_boo == true) {
            $('#msg-send').removeAttr('disabled');
        } else {
            $('#msg-send').attr('disabled', 'disabled');
        }

    }



//Ajax交互代码


//新建信息--点击发送按钮，传送数据
    $('#msg-send').click(function () {
        var token = Cookies.get('token');
        receiveName = oReceiver.val();
        title = oTitle.val();
        content = oContent.val();
        emergencyLevel = level;
        console.log(emergencyLevel);
        $.ajax({
            type: 'POST',
            async:false,
            url: SENDMESSAGE,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "token": token,
                "receiverName": receiveName,
                "title": title,
                "content": content,
                "emergencyLevel": emergencyLevel
            },
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
                            window.location.href = "sentMessage.html";
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

    });




});


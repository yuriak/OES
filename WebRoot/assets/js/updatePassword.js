/**
 * Created by fei on 2016/3/6.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {

    var token = Cookies.get('token');
    if (token == null || token == '') {
        showErrMsg("未登录，请先登录！");
        location.href = "login.html";
        return false;
    }
    //表单验证
    $('#doc-vld-msg').validator({
        onValid: function (validity) {
            $(validity.field).closest('.am-form-group').next('.am-alert').hide();
        },
        onInValid: function (validity) {
            var $field = $(validity.field);
            var $group = $field.closest('.am-form-group');
            var $alert = $group.find('.am-alert');
            var $alert1 = $group.next('.am-alert');

            var msg = $field.data('validationMessage');
            if (!$alert1.length) {
                $alert = $('<div class="my-alert am-alert am-alert-danger"></div>').hide().insertAfter($group);
            }
            $alert.html(msg).show();
        }
    });

    //定义表单对象
    var oPwd = $('#oldPassword'),
         pwd=$('#password');
         againPwd=$('#againPassword');


    //"下一步"按钮的状态（禁用或者激活）
    oPwd.bind('input', showNext);
    function showNext() {
        var oInput = $('.my-search input');
        var boo = true;
        //判断所有input是否有值
        for (var i = 0; i < oInput.length; i++) {
            if (oInput[i].value.length == 0 || oInput[i].value.length == null) {
                boo = false;
            }
        }
        if (boo == true) {
            $('#nextStep').removeAttr('disabled');
        } else {
            $('#nextStep').attr('disabled', 'disabled');
        }
    }

    var hid;
    $("#back").click(function(){
        hid=$("#hh").val();
        console.log(hid);
        if(hid==-1){
            window.history.go(-1);
        }else{
            $('.my-search').show();
            $('.protection').hide();
            hid=$("#hh").val("-1");

        }
    });

    //"确认提交"按钮的状态（禁用或者激活）
    pwd.bind('input', confirmSubmit);
    againPwd.bind('input', confirmSubmit);
    function confirmSubmit() {
        var password = pwd.val().trim();
        var againPassword = againPwd.val().trim();
        var _boo = true;

        if (password.length <6 || password.length == null) {
            _boo = false;
        }
        if (againPassword.length <6|| againPassword.length == null) {
            _boo = false;
        }
        if(password!=againPassword){
            _boo = false;
        }
        if (_boo === true) {
            $('#doc-confirm-toggle').removeAttr('disabled');
        } else {
            $('#doc-confirm-toggle').attr('disabled', 'disabled');
        }
    }

    $('#nextStep').click(function () {
        //表单数据
        var userName = oPwd.val().trim();
        var _boo = true;

        if (userName.length < 6 || userName.length == null) {
            _boo = false;
        }
        if (_boo == true) {
            $.ajax({
                type: 'POST',
                url: VERIFYPASSWORD,
                contentType: "application/json",
                data: {
                    "token":Cookies.get('token'),
                    "password": oPwd.val()
                },
                beforeSend: function () {
                    // 禁用按钮防止重复提交
                },
                success: function (data) {
                    var code = data.code,
                        errMsg = data.errMsg,
                        success = data.success,
                        reason = data.reason;
                    if (code === 1) {
                        if (success === true) {
                            Cookies.set('isCorrectPwd', true, {expires: 99999, path: '/'});
                            $('.my-search').hide();
                            $('#search-text').html('设置新密码');
                            $('.protection').show();
                            hid=$("#hh").val("1");
                            //Cookies.set('role', role, {expires: 99999, path: '/'});
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
        }
        else {
            $('#my-alert-modeError').modal();
        }
    })

    //验证答案是否正确
    $('#doc-confirm-toggle').click(function(){
        var isCorrectPwd=Cookies.get('isCorrectPwd');
        console.log(isCorrectPwd);
        if(isCorrectPwd!=null&&isCorrectPwd=="true"){
            $.ajax({
                type: 'POST',
                url: UPDATEPASSWORD,
                contentType: "application/json",
                data: {
                    "token":Cookies.get('token'),
                    "password": pwd.val()
                },
                beforeSend: function () {
                    // 禁用按钮防止重复提交
                },
                success: function (data) {
                    var code = data.code,
                        errMsg = data.errMsg,
                        success = data.success,
                        reason = data.reason;
                    if (code === 1) {
                        if (success === true) {
                            var keys=document.cookie.match(/[^ =;]+(?=\=)/g);
                            for(var i=0;i<keys.length;i++){
                                Cookies.remove(keys[i],{path:'/'});
                            }
                            alert("修改成功");
                            window.location.href="login.html";
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
        }else{
            alert("请先验证旧密码");
            window.location.reload(true);
        }
        //返回一个success

        //var success=true;
        //if(success==true){
        //
        //}
        //else{
        //    $('my-alert-answerError').modal();
        //}
    })




});


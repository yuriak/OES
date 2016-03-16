/**
 * Created by fei on 2016/3/5.
 */

document.write("<script src='assets/js/configure.js'></script>");
$(function () {
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
            // 使用自定义的提示信息
            var msg = $field.data('validationMessage');
            if (!$alert1.length) {
                $alert = $('<div class="my-alert am-alert am-alert-danger"></div>').hide().insertAfter($group);
            }
            $alert.html(msg).show();
        }
    });
});



    $(function() {
        $('#doc-modal-list').find('.am-icon-close').add('#doc-confirm-toggle').
            on('click', function() {
                $('#my-confirm').modal({
                    relatedTarget: this,
                    //确定时的操作
                    onConfirm: function(options) {
                        $.ajax({
                            type: 'POST',
                            url: RESETUSERPASSWORD,
                            contentType: "application/json",
                            data: {
                                "token":Cookies.get("token"),
                                "password": $('#password').val(),
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
                                        alert("修改成功");
                                        var keys=document.cookie.match(/[^ =;]+(?=\=)/g);
                                        for(var i=0;i<keys.length;i++){
                                            Cookies.remove(keys[i],{path:'/'});
                                        }
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
                    },
                    // 当选择取消时，操作
                    onCancel: function() {

                    }
                });
            });
    });


var pwd=$('#password');
    againPwd=$('#againPassword');
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
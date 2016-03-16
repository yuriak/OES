/**
 * Created by fei on 2016/3/8.
 */
document.write("<script src='assets/js/adminConfigure.js'></script>");
$(function(){
    $("#approve").click(function(){
        window.location.href="adminApproveTeacher.jsp";
    });

    $("#set-semester").click(function(){
        window.location.href="adminSetSemester.jsp";
    });

    $('#exit').click(function(){
    	$.post(ADMINLOGOUT,'',function(data){
    		window.location.reload(true);
    	});
    });
    
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
    var oDate=$('#date');

//"确认提交"按钮的状态（禁用或者激活）
    oDate.bind('input', confirmSubmit);
    function confirmSubmit() {
        var date = oDate.val();
        var _boo = true;
        if (date.length !=6) {
            _boo = false;
        }
        if (_boo === true) {
            $('#doc-confirm-toggle').removeAttr('disabled');
        } else {
            $('#doc-confirm-toggle').attr('disabled', 'disabled');
        }
    }
    $('#doc-confirm-toggle').click(function(){
        $.ajax({
            type: 'POST',
            url: ADMINSETSEMESTER,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "semester": oDate.val(),
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
                        alert("设置成功");
                        window.location.reload(true);
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


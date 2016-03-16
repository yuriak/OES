/**
 * Created by fei on 2016/3/8.
 */
document.write("<script src='assets/js/adminConfigure.js'></script>");


//跳转页面
$("#msg").click(function(){
   window.location.href="receiveMessage.html";
});

$("#approve").click(function(){
        window.location.href="adminApproveTeacher.jsp";
});

$("#set-semester").click(function(){
    window.location.href="adminSetSemester.jsp";
});

//全选按钮
$(function(){
    $("input.all").click(function(){
        if(this.value==0){
            var oTeacher=$(".my-teachers").find("input");
            for( i=0;i<oTeacher.length;i++){
                oTeacher[i].checked=false;
            }
            this.value=1;
            $(".all-check .all-text").text("全选");
        }else{
            var oTeacher=$(".my-teachers").find("input");
            for(var i=0;i<oTeacher.length;i++){
                oTeacher[i].checked=true;
            }
            this.value=0;
            $(".all-check .all-text").text("取消全选");
        }
    })
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


$('#exit').click(function(){
	$.post(ADMINLOGOUT,'',function(data){
		window.location.reload(true);
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

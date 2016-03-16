/**
 * Created by fei on 2016/3/8.
 */
document.write("<script src='assets/js/adminConfigure.js'></script>");
//跳转页面
$(function () {
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
	                oTeacher[i].checked=true;
	            }
	            this.value=1;
	            $(".all-check .all-text").text("取消全选");
	        }else{
	            var oTeacher=$(".my-teachers").find("input");
	            for(var i=0;i<oTeacher.length;i++){
	                oTeacher[i].checked=false;
	            }
	            this.value=0;
	            $(".all-check .all-text").text("全选");
	        }
	    })
	});
	
	
	//$(function () {
	//    //表单验证
	//    $('#doc-vld-msg').validator({
	//        onValid: function (validity) {
	//            $(validity.field).closest('.am-form-group').next('.am-alert').hide();
	//        },
	//        onInValid: function (validity) {
	//            var $field = $(validity.field);
	//            var $group = $field.closest('.am-form-group');
	//            var $alert = $group.find('.am-alert');
	//            var $alert1 = $group.next('.am-alert');
	//            // 使用自定义的提示信息
	//            var msg = $field.data('validationMessage');
	//            if (!$alert1.length) {
	//                $alert = $('<div class="my-alert am-alert am-alert-danger"></div>').hide().insertAfter($group);
	//            }
	//            $alert.html(msg).show();
	//        }
	//    });
	//});
	
	$('#approveButton').click(function(){
		var oTeacher=$(".my-teachers").find("input");
		var teacherArray=[{}];
		for( i=0;i<oTeacher.length;i++){
			if(oTeacher[i].checked==true){
				teacherArray[i]={"teacherID":oTeacher.eq(i).val()}
			}
        }
		if(teacherArray[0].teacherID==null){
			return;
		}
		$.ajax({
            type: 'POST',
            url: ADMINSETTEACHERSTATUS,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {"teacherStatus":1,"teacherList":JSON.stringify(teacherArray)},
            beforeSend: function () {
                // 禁用按钮防止重复提交
            },
            success: function (data) {
            	console.log(data);
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

	$('#rejectButton').click(function(){
		var oTeacher=$(".my-teachers").find("input");
		var teacherArray=[{}];
		for( i=0;i<oTeacher.length;i++){
			if(oTeacher[i].checked==true){
				teacherArray[i]={"teacherID":oTeacher.eq(i).val()}
			}
        }
		if(teacherArray[0].teacherID==null){
			return;
		}
		$.ajax({
            type: 'POST',
            url: ADMINSETTEACHERSTATUS,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {"teacherStatus":-1,"teacherList":JSON.stringify(teacherArray)},
            beforeSend: function () {
                // 禁用按钮防止重复提交
            },
            success: function (data) {
            	console.log(data);
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
	
	$('#exit').click(function(){
		$.post(ADMINLOGOUT,'',function(data){
			window.location.reload(true);
		});
	});

});

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

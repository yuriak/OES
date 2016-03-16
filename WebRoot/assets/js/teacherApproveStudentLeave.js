/**
 * Created by S.F.S on 2016/3/6.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {
	
    var token = Cookies.get('token');
    var lessonID=Cookies.get('lessonID');
    if (token == null || token == '') {
        showErrMsg("未登录，请先登录！");
        location.href = "login.html";
        return false;
    }
    
    $.ajax({
        type: 'POST',
        url: GETAPPLYINGLEAVESTUDENTLIST,
        //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
        contentType: "application/json",
        data: {
            "token":Cookies.get('token'),
            "lessonID":lessonID
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
                    var studentList=data.applyingLeaveStudentList;
                    for(var i=0;i<studentList.length;i++){
                        var leaveID=studentList[i].leaveID;
                            userName=studentList[i].userName,
                            studentName=studentList[i].studentName,
                            leaveReason=studentList[i].leaveReason;
                        var template=$('.template-student').children('.my-teacher-course').clone();
                        var studentNameBar=template.find('span.studentName');
                        var studentCheckBox=template.find('input.studentInput');
                        var userNameSpan=template.find('.my-userName');
                        var reasonSpan=template.find('.my-reason');
                        var studentNameSpan=template.find('.my-studentName');
                        studentNameBar.text(studentName);
                        studentCheckBox.val(leaveID);
                        userNameSpan.text(userName);
                        reasonSpan.text(leaveReason);
                        studentNameSpan.text(studentName);
                        $('.my-students').append(template);
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

    $('#rejectButton').click(function(){
        var oStudent=$(".my-students").find("input");
        var studentArray=[{}];
        for(var i=0;i<oStudent.length;i++){
            if(oStudent[i].checked==true){
                studentArray[i]={"leaveID":oStudent.eq(i).val()};
            }
        }
        if(studentArray[0].leaveID==null){
            return;
        }
        $.ajax({
            type: 'POST',
            url: REJECTLEAVE,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "rejectList":JSON.stringify(studentArray),
                "token":Cookies.get('token'),
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
                        alert("拒绝成功！");
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

    $('#approveButton').click(function(){
        var oStudent=$(".my-students").find("input");
        var studentArray=[{}];
        for(var i=0;i<oStudent.length;i++){
            if(oStudent[i].checked==true){
                studentArray[i]={"leaveID":oStudent.eq(i).val()};
            }
        }
        if(studentArray[0].leaveID==null){
            return;
        }
        $.ajax({
            type: 'POST',
            url: APPROVELEAVE,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "approveList":JSON.stringify(studentArray),
                "token":Cookies.get('token'),
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
                        alert("批准成功！");
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

    $('input.all').click(function(){
        if(this.value==-1){
            var oStudents=$('.my-students').find('input');
            for(var i=0;i<oStudents.length;i++){
                oStudents[i].checked = true;
            }
            this.value=-2;
            $('.all-check .all-text').text('取消全选');
        }else{
            var oStudents=$('.my-students').find('input');
            for(var i=0;i<oStudents.length;i++){
                oStudents[i].checked = false;
            }
            this.value=-1;
            $('.all-check .all-text').text('全选');
        }
    });

    //下拉组件
    $(function () {

        //下拉
        $(document).on('click', '.am-accordion-title', slideDown);
        function slideDown() {
            var value = $(this).attr('value');
            var content = $(this).nextAll('dd');

            if (value == 1) {
                content.slideDown('fast');
                $(this).attr('value', 0);
            } else if (value == 0) {
                content.slideUp('fast');
                $(this).attr('value', 1);
            }
        }
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

});
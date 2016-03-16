/**
 * Created by S.F.S on 2016/3/6.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {

    var token = Cookies.get('token');
    var courseID=Cookies.get('currentCourseID');
    if (token == null || token == '') {
        showErrMsg("未登录，请先登录！");
        location.href = "login.html";
        return false;
    }


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
    })



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



    $.ajax({
        type: 'POST',
        url: GETAPPLYINGSTUDENTLIST,
        //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
        contentType: "application/json",
        data: {
            "token":Cookies.get('token'),
            "courseID":courseID
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
                    var studentList=data.applyingStudentList;
                    for(var i=0;i<studentList.length;i++){
                        var studentID=studentList[i].studentID;
                            userName=studentList[i].username,
                            studentName=studentList[i].studentName,
                            collegeName=studentList[i].collegeName,
                            major=studentList[i].major;
                        var template=$('.template-student').children('.my-teacher-course').clone();
                        var studentNameSpan=template.find('span.studentName');
                        var studentCheckBox=template.find('input.studentInput');
                        var userNameSpan=template.find('.my-userName');
                        var collageSpan=template.find('.my-college');
                        var majorSpan=template.find('.my-major');
                        studentNameSpan.text(studentName);
                        studentCheckBox.val(studentID);
                        userNameSpan.text(userName);
                        collageSpan.text(collegeName);
                        majorSpan.text(major);
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

    $('#approveButton').click(function(){
        var oStudent=$(".my-students").find("input");
        var studentArray=[{}];
        for(var i=0;i<oStudent.length;i++){
            if(oStudent[i].checked==true){
                studentArray[i]={"studentID":oStudent.eq(i).val()};
            }
        }
        if(studentArray[0].studentID==null){
            return;
        }

        $.ajax({
            type: 'POST',
            url: APPROVESTUDENT,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "approvedStudentList":JSON.stringify(studentArray),
                "token":Cookies.get('token'),
                "courseID":courseID
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

    $('#rejectButton').click(function(){
        var oStudent=$(".my-students").find("input");
        var studentArray=[{}];
        for(var i=0;i<oStudent.length;i++){
            if(oStudent[i].checked==true){
                studentArray[i]={"studentID":oStudent.eq(i).val()};
            }
        }
        if(studentArray[0].studentID==null){
            return;
        }

        $.ajax({
            type: 'POST',
            url: REJECTSTUDENT,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "rejectedStudentList":JSON.stringify(studentArray),
                "token":Cookies.get('token'),
                "courseID":courseID
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
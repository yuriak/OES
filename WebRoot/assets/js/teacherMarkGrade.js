/**
 * Created by S.F.S on 2016/3/6.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {


    var token = Cookies.get('token');
    if (token == null || token == '') {
        showErrMsg("未登录，请先登录！");
        location.href = "login.html";
        return false;
    }
    var courseID=Cookies.get('courseID');


    /**
     * 获取所有该课程学生的信息
     * */

    var information=new Array();
    $.ajax({
        type: 'POST',
        url:GETCOURSEGRADE,
        //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
        contentType: "application/json",
        data: {
            "token":token,
            "courseID":courseID
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
                    var courseGradeList=data.courseGradeList;


                    for(var i= 0;i<=courseGradeList.length;i++){
                        var studentName=courseGradeList[i].studentName;
                        var electiveID=courseGradeList[i].electiveID;
                        var courseGrade=courseGradeList[i].courseGrade;
                        var collegeName=courseGradeList[i].collegeName;
                        var userName=courseGradeList[i].username;
                        var totalEvaluationNumber=courseGradeList[i].totalEvaluationNumber;
                        var averageJoiningPercentage=courseGradeList[i].averageJoiningPercentage;
                        var myEvaluationNumber=courseGradeList[i].myEvaluationNumber;
                        var averageEvaluationGrade=courseGradeList[i].averageEvaluationGrade;
                        var courseGradeStatus=courseGradeList[i].courseGradeStatus;
                        var leaveNumber=courseGradeList[i].leaveNumber;
                        information[i]=[studentName,electiveID,courseGrade,collegeName,userName,totalEvaluationNumber,
                            averageJoiningPercentage,myEvaluationNumber,averageEvaluationGrade,courseGradeStatus,leaveNumber];
                        //渲染第一个学生的信息
                        if(i==0){
                            $("span.my-studentName").text(studentName);
                            $("span.my-userName").text(userName);
                            $("span.my-collegeName").text(collegeName);
                            $("#totalEvaluationNumber").text(totalEvaluationNumber);
                            $("#leaveNumber").text(leaveNumber);
                            $("#myEvaluationNumber").text(myEvaluationNumber);
                            $("#averageJoiningPercentage").text(averageJoiningPercentage);
                            $("#averageEvaluationGrade").text(averageEvaluationGrade);
                            $("#my-submit").val(electiveID);//选课ID
                            //分数开始为0，不显示
                            if(courseGrade!=0){
                                $("#courseGrade").val(courseGrade);
                            }
                        }
                        //渲染学生按钮
                        $("button.student-select").html(information[0][0]);
                        //渲染下拉选框
                        var nTemplate=$(".template-student").children().clone();
                        nTemplate.find("span.student-name").html(studentName);
                        nTemplate.attr('value',i);
                        if(courseGradeStatus==1){
                            nTemplate.find("a.my-mark-student").append("<span class=\"my-marked am-badge am-badge-danger\">已打分</span>");
                        }
                        $("#my-ul").append(nTemplate);
                    }
                    
                   
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


    /**
     * Ajax API 点击select获取CourseList 根据学期获得不同的列表
     */
    $(document).on('click', '.my-li', function(){ 
        var student = $(this).val();//student==i
        var info=$(this).find("span.student-name");
        $(this).parents(".my-student-dialog").css("display","none");
        
        var text = info.text();
        var m = student;
        $("button.student-select").html(text);
        for (var n = 0; n < information[m].length; n++) {
            switch (n) {
                case 0:
                    $("span.my-studentName").text(information[m][n]);
                    break;
                case 1:
                    $("#my-submit").val(information[m][n]);
                    break;
                case 2:
                    $("#courseGrade").val(information[m][n]);
                    break;
                case 3:
                    $("span.my-collegeName").text(information[m][n]);
                    break;
                case 4:
                    $("span.my-userName").text(information[m][n]);
                    break;
                case 5:
                    $("#totalEvaluationNumber").text(information[m][n]);
                    break;
                case 6:
                    $("#averageJoiningPercentage").text(information[m][n]);
                    break;
                case 7:
                    $("#myEvaluationNumber").text(information[m][n]);
                    break;
                case 8:
                    $("#averageEvaluationGrade").text(information[m][n]);
                    break;
                case 9:
                    break;
                case 10:
                    $("#leaveNumber").text(information[m][n]);
                    break;
                default :
                    break;
            }
        }
    });



    /**
     * 老师对一个学生打分
     * */
    $(document).on('click', '#my-submit', function(){
        var electiveID=$("#my-submit").val();
        var grade=$("#courseGrade").val();
        $.ajax({
            type: 'POST',
            url:UPDATECOURSEGRADE,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "token":token,
                "electiveID":electiveID,
                "grade":grade
            },
            beforeSend: function () {
                // 禁用按钮防止重复提交
//                $("#my-submit").attr("disabled","true");
                $('#my-loading').modal('open');
            },
            success: function (data) {
                var code = data.code,
                    errMsg = data.errMsg,
                    reason = data.reason,
                    success = data.success;
                if (code === 1) {
                    if (success === true) {
                        //成功后执行的业务
                        showSuccessMsg("该学生已被成功打分");
                        window.location.reload(true);
                    } else {
                        showErrMsg(reason);
                    }
                } else {
                    showErrMsg(errMsg);
                }
            },
            complete: function () {
                $('#my-loading').modal('close');
            },
            error: function () {
                showServiceError();
            },
            dataType: 'json'
        });

    });





    var cHeight = document.documentElement.clientHeight;
    var cWidth = document.documentElement.clientWidth;
    var dialogHeight=$('.my-student-dialog').height();
    var top=(cHeight-dialogHeight)/2;

    $('#mask').css({'position':'fixed','width': cWidth, 'height': cHeight,'top':0+'px','left':0+'px','z-index':0});
    if(dialogHeight<cHeight){
        $('.my-student-dialog').css('top',top);
    }else{
        $('.my-student-dialog').css('top',0);
    }


    $('.student-select').click(function(){
        $('.my-student-dialog').css('display','block');
    });
    $('#mask').click(function(){
        $('.my-student-dialog').css('display','none');

    })

});
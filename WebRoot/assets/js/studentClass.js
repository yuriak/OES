/**
 * Created by fei on 2016/3/6.
 */

//加急 true为加急邮件
$(function () {
    var token = Cookies.get('token');
    if (token == null || token == '') {
        showErrMsg("未登录，请先登录！");
        location.href = "login.html";
        return false;
    }

    var courseID = Cookies.get('currentCourseID');
    $.ajax({
        type: 'POST',
        async:false,
        url: GETLESSONLISTBYCLASS,
        contentType: "application/json",
        data: {
            "token": token,
            "courseID": courseID
        },
        beforeSend: function () {
            //$('#my-loading').modal('open');
        },
        success: function (data) {
            var code = data.code,
                errMsg = data.errMsg,
                reason = data.reason,
                success = data.success;
            if (code === 1) {
                if (success === true) {
                    //成功后执行的业务
                    var lessonList = data.lessonList;
                    for (var i = 0; i < lessonList.length; i++) {
                        var lessonID = lessonList[i].lessonID;
                        var lessonStatus = lessonList[i].lessonStatus;
                        var startTime = lessonList[i].startTime;
                        var emergencyLevel = lessonList[i].emergencyLevel;
                        var number = i+1;
                        var template = $('#template-class').children().eq(0).clone();
                        template.find('[number]').text(number);
                        template.find('.course-time-sfs').text(startTime);
                        template.find('[data-lesson-id]').attr('data-lesson-id', lessonID);
                        template.find('[data-leave-lesson-id]').attr('data-leave-lesson-id', lessonID);
                        var leaveBtn=template.find('[data-leave-lesson-id]').eq(0);

                        var oLessonName = template.find('dt').eq(0);
                        if (emergencyLevel == EMERGENCY) {
                            template.find('i').eq(0).css('color', '#E63911');
                        }
                        switch (lessonStatus) {
                            case -1: //课程还未开始
                                leaveBtn.removeAttr('disabled');
                                oLessonName.css('background-color', '#C7C7C7');
                                break;
                            case 0:  //课程正在进行
                                leaveBtn.attr('disabled','disabled');
                                oLessonName.css('background-color', '#1BB8FA');
                                template.find('[data-leave-lesson-id]').attr('disabled','true');
                                break;
                            case 1: //课程已结束
                                oLessonName.css('background-color', '#6FD668');
                                leaveBtn.attr('disabled','disabled');
                                template.find('[data-leave-lesson-id]').attr('disabled','true');

                                break;
                        }

                        $.ajax({
                            type: 'POST',
                            async: false,
                            url: GETNOTICEBYCLASSID,
                            contentType: "application/json",
                            data: {
                                'token': token,
                                'lessonID': lessonID
                            },
                            beforeSend: function () {
                                //$('#my-loading').modal('open');

                            },
                            success: function (data) {
                                var code = data.code,
                                    errMsg = data.errMsg,
                                    reason = data.reason,
                                    success = data.success;
                                if (code === 1) {
                                    if (success === true) {
                                        //成功后执行的业务
                                        var noticeContent = data.noticeContent;
                                        var noticeID = data.noticeID;
                                        template.find('[notice-content]').eq(0).text(noticeContent);

                                        $.ajax({
                                            type: 'POST',
                                            url:GETLEAVEINFO,
                                            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
                                            contentType: "application/json",
                                            data: {
                                                'token':token,
                                                'lessonID':lessonID
                                            },
                                            beforeSend: function () {

                                            },
                                            success: function (data) {
                                                var code = data.code,
                                                    errMsg = data.errMsg,
                                                    reason = data.reason,
                                                    success = data.success;
                                                if (code === 1) {
                                                    if (success === true) {
                                                        //成功后执行的业务
                                                        var leaveID = data.leaveID;
                                                        var leaveReason = data.leaveReason;
                                                        var approveStatus = data.approveStatus;
                                                        if(leaveID!=null){ //已请假
                                                            leaveBtn.attr('disabled','true');
                                                            if(approveStatus==0){//未被批准
                                                                leaveBtn.text('未被批准');
                                                            } else if(approveStatus==1){
                                                                leaveBtn.text('已批准');
                                                                template.find('[data-lesson-id]').attr('disabled', 'disabled');
                                                            }
                                                        }else{ //
                                                            leaveBtn.removeAttr('disabled');
                                                        }

                                                    } else {
                                                        showErrMsg(reason);
                                                    }
                                                } else {
                                                    showErrMsg(errMsg);
                                                }
                                            },
                                            complete: function () {
                                                //$('#my-loading').modal('close');

                                            },
                                            error: function () {
                                                showServiceError();
                                            },
                                            dataType: 'json'
                                        });



                                        $('section').prepend(template);


                                    } else {
                                        showErrMsg(reason);
                                    }
                                } else {
                                    showErrMsg(errMsg);
                                }
                            },
                            complete: function () {
                                //$('#my-loading').modal('close');

                            },
                            error: function () {
                                showServiceError();
                            },
                            dataType: 'json'
                        });


                    }


                } else {
                    showErrMsg(reason);
                }
            } else {
                showErrMsg(errMsg);
            }
        },
        complete: function () {
            //$('#my-loading').modal('close');

        },
        error: function () {
            showServiceError();
        },
        dataType: 'json'
    });


    /**
     *
     * API 请假
     *
     */
    $(document).on('click', 'button[data-leave-lesson-id]', leave);
    function leave() {
        var $this=$(this);
        var lessonID = $(this).attr('data-leave-lesson-id');
        $('#my-prompt').modal({
            relatedTarget: this,
            onConfirm: function (e) {
                if (e.data.length == 0 || e.data.length == null) {
                    $('#my-error').modal();
                } else {
                    $('#my-confirm').modal();

                    $('span[again-confirm]').click(realLeave);

                    function realLeave(){
                        $.ajax({
                            type: 'POST',
                            async: false,
                            url:LEAVE,
                            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
                            contentType: "application/json",
                            data: {
                                'token':token,
                                'lessonID':lessonID,
                                'leaveReason': e.data
                            },
                            beforeSend: function () {
                            },
                            success: function (data) {
                                var code = data.code,
                                    errMsg = data.errMsg,
                                    reason = data.reason,
                                    success = data.success;
                                if (code === 1) {
                                    if (success === true) {
                                        //成功后执行的业务
                                        alert('请假已发送给老师，静静等待老师批准吧！');
                                        $this.attr('disabled','true');
                                        $this.text('待批准...');

                                    } else {
                                        showErrMsg(reason);
                                    }
                                } else {
                                    showErrMsg(errMsg);
                                }
                            },
                            complete: function () {
                                //$('#my-loading').modal('close');
                            },
                            error: function () {
                                showServiceError();
                            },
                            dataType: 'json'
                        });
                    }
                }
            },
            onCancel: function () {
                $('#my-error2').modal();
            }
        });
    }
    /**
     *
     * API 进入评价
     *
     */

    $(document).on('click','[data-lesson-id]',enterEvaluation);
    function enterEvaluation(){
        var currentLessonID=$(this).attr('data-lesson-id');
        Cookies.set('currentLessonID',currentLessonID,{path:'/'});
        window.location.href="studentEvaluation.html";
    }

})


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
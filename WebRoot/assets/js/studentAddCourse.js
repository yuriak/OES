/**
 * Created by fei on 2016/3/7.
 */
$(function () {

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

    var token = Cookies.get('token');
    if (token == null || token == '') {
        showErrMsg("未登录，请先登录！");
        location.href = "login.html";
        return false;
    } else {
        $.ajax({
            type: 'POST',
            url: GETALLAVAILABLECOURSELIST,
            contentType: "application/json",
            data: {
                'token': token
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
                    if (success === true) {  //成功后执行的业务
                        var allCourseList = data.AllAvailableCourseList;

                        if (allCourseList.length <= 0) {
                            $('section').after("<code style='display:block;text-align: center' class='warning'>这学期还没有老师开课=-=</code>");
                        } else {

                            for (var i = 0; i < allCourseList.length; i++) {
                                var courseID = allCourseList[i].courseID;
                                var courseName = allCourseList[i].courseName;
                                var teacherName = allCourseList[i].teacherName;

                                var template = $('#template-available').children('dl').eq(0).clone();

                                template.find('button[data-course-id]').eq(0).attr('data-course-id', courseID);
                                template.find('[teacher-name]').eq(0).text(teacherName);
                                template.find('[course-name]').text(courseName);

                                //获取各个课程的详细信息
                                $.ajax({
                                    type: 'POST',
                                    async: false,
                                    url: GETCOURSEINFO,
                                    //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
                                    contentType: "application/json",
                                    data: {
                                        "token": token,
                                        "courseID": courseID
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
                                                var courseNumber = data.courseNumber;
                                                var teacherID = data.teacherID;
                                                var collegeName = data.collegeName;
                                                var courseOrder = data.courseOrder;

                                                template.find('[data-teacher-id]').eq(0).attr('data-teacher-id', teacherID);
                                                template.find('[course-number]').eq(0).text(courseNumber);

                                                template.find('[college-name]').eq(0).text(collegeName);
                                                template.find('[college-order]').eq(0).text(courseOrder);
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

                                $('section').append(template);
                            }

                        }

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

    }

    /**
     *
     * 查询课程
     *
     */
    $('#search-class').on('click', function () {
        $('#my-prompt').modal({
            relatedTarget: this,
            onConfirm: function (e) {

                    $('section').empty();
                    //$('#my-loading').modal('open');
                    $.ajax({
                        type: 'POST',
                        url: QUERYCOURSE,
                        contentType: "application/json",
                        data: {
                            'token': token,
                            'courseName': e.data
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
                                if (success === true) {  //成功后执行的业务
                                    var allCourseList = data.courseList;

                                    if (allCourseList.length <= 0) {
                                        $('section').after("<code style='display:block;text-align: center' class='warning'>这学期还没有老师开课=-=</code>");
                                    } else {

                                        for (var i = 0; i < allCourseList.length; i++) {
                                            var courseID = allCourseList[i].courseID;
                                            var courseName = allCourseList[i].courseName;
                                            var teacherName = allCourseList[i].teacherName;

                                            var template = $('#template-available').children('dl').eq(0).clone();

                                            template.find('button[data-course-id]').eq(0).attr('data-course-id', courseID);
                                            template.find('[teacher-name]').eq(0).text(teacherName);
                                            template.find('[course-name]').text(courseName);

                                            //获取各个课程的详细信息
                                            $.ajax({
                                                type: 'POST',
                                                async: false,
                                                url: GETCOURSEINFO,
                                                //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
                                                contentType: "application/json",
                                                data: {
                                                    "token": token,
                                                    "courseID": courseID
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
                                                            var courseNumber = data.courseNumber;
                                                            var teacherID = data.teacherID;
                                                            var collegeName = data.collegeName;
                                                            var courseOrder = data.courseOrder;

                                                            template.find('[data-teacher-id]').eq(0).attr('data-teacher-id', teacherID);
                                                            template.find('[course-number]').eq(0).text(courseNumber);

                                                            template.find('[college-name]').eq(0).text(collegeName);
                                                            template.find('[college-order]').eq(0).text(courseOrder);
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

                                            $('section').append(template);
                                        }

                                    }

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
            },
            onCancel: function () {
                //console.log('3');
            }
        });
    });


    /**
     *
     * 添加课程
     *
     */
    $(document).on('click','button[data-course-id]',electCourse);
    function electCourse(){
        var courseID = $(this).attr('data-course-id');
        $.ajax({
            type: 'POST',
            url:ELECTCOURSE,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                'token':token,
                'courseID':courseID
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
                        alert('添加成功,等待老师批准...');
                        window.location.reload();

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
    }
});


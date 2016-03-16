/**
 * Created by S.F.S on 2016/3/5.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {

    var width = $('.am-accordion-item').width();
    $('.my-accordion-button').css('width', width - 45.997);

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
    }


    /**
     *
     * 进入课程
     *
     */

$(document).on('click','button.enter',enterCourse);
    function enterCourse(){
        var courseID=$(this).attr('data-id');
        var courseStatus=$(this).parents('dl').find('code[data-elective-status]').eq(0).attr('data-elective-status');
        if(courseStatus==1){
            Cookies.set('currentCourseID',courseID,{path:'/'});
            window.location.href='studentClass.html';
        }else{
            showErrMsg('你不能进入该课程！！');
        }

    }


    /**
     * Ajax API 获取个人信息
     */
    $.ajax({
        type: 'POST',
        async: false,
        url: GETMYINFOBYTOKEN,
        contentType: "application/json",
        data: {
            "token": token
        },
        beforeSend: function () {
            $('#my-loading').modal('open');
        },
        success: function (data) {
            var code = data.code,
                errMsg = data.errMsg,
                success = data.success,
                reason = data.reason;
            if (code == 1) {
                if (success == true) {
                    var userID = data.userID,
                        userName = data.username,
                        gender = data.gender,
                        currentSemester = data.currentSemester,
                        phone = data.phone,
                        college = data.college,
                        realName = data.realName,
                        major = data.major,
                        email = data.email,
                        semesterList = data.semesterList;
                    Cookies.set('userID', userID, {path: '/'});
                    Cookies.set('collegeName', college, {path: '/'});
                    Cookies.set('gender', gender, {path: '/'});
                    Cookies.set('email', email, {path: '/'});
                    Cookies.set('phone', phone, {path: '/'});
                    Cookies.set('currentSemester', currentSemester, {path: '/'});
                    Cookies.set('realName', realName, {path: '/'});
                    Cookies.set('major', major, {path: '/'});
                    Cookies.set('userName', userName, {path: '/'});
                    for (var i = 0; i < semesterList.length; i++) {
                        var semester = semesterList[i].semester;
                        if (i === 0) {
                            var option = $("<option value='" + semester + "'>本学期</option>");
                        } else {
                            var option = $("<option value='" + semester + "'>" + semester + "学期</option>");
                        }

                        var oSemester = $('#semester');
                        option.appendTo(oSemester);
                    }
                }
                else {
                    showErrMsg(reason);
                }

            } else if (code == -1) {
                showErrMsg(errMsg);
                window.location.reload(false);
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


    var currentSemester = Cookies.get('currentSemester');
    /**
     * End
     */

    /**
     * Ajax API 获取本学期CourseList
     */
    getCourseList();

    /**
     * Ajax API 点击select获取CourseList
     */
    $('#semester').on('change', function () {
        var semester = $(this).children('option:selected').eq(0);
        getCourseList(semester);
    });

    function getCourseList(semester) {
        if (semester != null) {
            currentSemester = semester.val();
            var codes = $('code.warning');
            for (var i = 0; i < codes.length; i++) {
                codes.eq(i).remove();
            }
            $('section').empty();
            var text = semester.text();
            if (text != '本学期') {
                $('.my-add-button').css('display', 'none');
            } else {
                $('.my-add-button').css('display', 'block');
            }

        }
        $.ajax({
            type: 'POST',
            async: false,
            url: GETSTUDENTCOURSELIST,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "token": token,
                "currentSemester": currentSemester
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
                        var courseList = data.courseList;
                        if (courseList.length <= 0) {
                            $('#semester').after("<code style='display:block;text-align: center' class='warning'>这学期怎么会没有课呢？</code>");

                        } else {
                            for (var i = 0; i < courseList.length; i++) {
                                var courseID = courseList[i].courseID;
                                var courseName = courseList[i].courseName;
                                var electiveStatus = courseList[i].electiveStatus;
                                var template = $('#template-course').children('.my-teacher-course').clone();

                                var btn = template.children('.my-accordion-button');
                                var sets = template.children('.my-accordion-title').eq(0);
                                var deleteID = template.find('button[data-id]');
                                btn.text(i + '.' + courseName);

                                deleteID.eq(0).attr('data-id', courseID);
                                deleteID.eq(1).attr('data-id', courseID);
                                var status = template.find('code[data-elective-status]').eq(0);
                                if (electiveStatus == 1) { //教师已经批准上课
                                    status.text('已批准');
                                    status.attr('data-elective-status','1');
                                } else if (electiveStatus == 0) {//等待老师批准
                                    sets.css('color', '#DECD04');

                                    status.text('待批准...');
                                } else if (electiveStatus == -1) {//老师拒绝
                                    sets.css('color', '#FF0000');
                                    status.text('已拒绝');
                                }

                                //获取各个课程的详细信息
                                $.ajax({
                                    type: 'POST',
                                    async: false,
                                    url: GETELECTIVECOURSEINFO,
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
                                                var courseName = data.courseName,
                                                    courseNumber = data.courseNumber,
                                                    courseOrder = data.courseOrder,
                                                    electiveStatus = data.electiveStatus,
                                                    courseGradeStatus = data.courseGradeStatus,
                                                    courseGrade = data.courseGrade,
                                                    teacherName = data.teacherName,
                                                    teacherID = data.teacherID,
                                                    collegeID = data.collegeID,
                                                    collegeName = data.collegeName;

                                                template.find('span[course-name]').eq(0).text(courseName);
                                                template.find('span[course-id]').eq(0).text(courseID);
                                                template.find('span[course-number]').eq(0).text(courseNumber);
                                                template.find('span[teacher-name]').eq(0).text(teacherName);
                                                template.find('a[teacher-id]').eq(0).attr('teacher-id', teacherID);
                                                if (courseGradeStatus == 1) {
                                                    template.find('span[data-grade]').eq(0).text(courseGrade);
                                                } else {
                                                    template.find('span[data-grade]').eq(0).text('未评');
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

                                $('section').append(template);

                            }
                        }

                    }
                    else {
                        showErrMsg(reason);
                    }
                } else {
                    showErrMsg(errMsg);
                    window.location.reload(false);
                }
            }
            ,
            complete: function () {
                $('#my-loading').modal('close');
            }
            ,
            error: function () {
                showServiceError();
            }
            ,
            dataType: 'json'
        });
    }


    /**
     *
     * 删除课程
     *
     */
    $(document).on('click', '.delete-course', function () {
        var courseID = parseInt($(this).attr('data-id'));
        $('.my-delete-dialog').modal({
            onConfirm: function () {
                $.ajax({
                    type: 'POST',
                    async:false,
                    url: DELETESTUDENTCOURSE,
                    //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
                    contentType: "application/json",
                    data: {
                        'token': token,
                        'courseID': courseID
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
                                alert( '删除成功');
                            } else {
                                showErrMsg(reason);
                            }
                        } else {
                            showErrMsg(errMsg);
                        }
                    },
                    complete: function () {
                    },
                    error: function () {
                        showServiceError();
                    },
                    dataType: 'json'
                });
                window.location.reload(false);
            },
            //closeOnConfirm: false,
            onCancel: function () {
                window.location.reload(false);
            }
        });
    });


    /**
     * 点击教师，获取教师信息(待完成)
     *
     */

});
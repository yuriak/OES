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


    var width = $('.am-accordion-item').width();
    $('.my-accordion-button').css('width', width - 45.997);


    //下拉框组件
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


    /**
     *
     * 进入课程
     *
     */

    $(document).on('click','button.enter',enterCourse);
    function enterCourse(){
        var courseID=$(this).val();
        Cookies.set('currentCourseID',courseID,{path:'/'});
        window.location.href='teacherClass.html';
        }


    /**
     * Ajax API 获取个人信息和当前学期和学期列表
     */
    $.ajax({
        type: 'POST',
        async: false,//保持同步
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
                        email = data.email,
                        semesterList = data.semesterList;
                    Cookies.set('userID', userID, {path: '/'});
                    Cookies.set('collegeName', college, {path: '/'});
                    Cookies.set('gender', gender, {path: '/'});
                    Cookies.set('email', email, {path: '/'});
                    Cookies.set('phone', phone, {path: '/'});
                    Cookies.set('currentSemester', currentSemester, {path: '/'});
                    Cookies.set('realName', realName, {path: '/'});
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
                }else{
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

    var currentSemester = Cookies.get('currentSemester');
    /**
     * End
     */




/**
     * Ajax API 获取本学期CourseList
     */
    getCourseList();

    /**
     * Ajax API 点击select获取CourseList 根据学期获得不同的列表
     */
    $('#semester').on('change', function () {
        var semester = $(this).find('option:selected').eq(0);
        getCourseList(semester);
    });

    function getCourseList(semester) {
        if (semester != null) {
            currentSemester = semester.val();
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
            url: GETTEACHERCOURSELIST,
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
                            $('#semester').after("<code style='display:block;text-align: center' class='warning'>这学期没有课</code>");

                        } else {
                            for (var i = 0; i < courseList.length; i++) {
                                var courseID = courseList[i].courseID;
                                var courseName = courseList[i].courseName;
                                var totalStudentNumber=courseList[i].totalStudentNumber;
                                var applyingStudentNumber = courseList[i].applyingStudentNumber;

                                var template = $('#template-course').children('.my-teacher-course').clone();

                                var btn1 = template.find('.my-accordion-button');
                                var btn2 = template.find('.delete-course');
                                var number1 = template.find('span.my-apply-number');
                                var number2 = template.find('span.my-approving-number');

                                //var deleteID = template.find('button[data-id]');

                                btn1.text(courseName);
                                btn1.val(courseID);
                                btn2.val(courseID);
                                number1.text(totalStudentNumber);
                                number2.text(applyingStudentNumber);

                                $('section').append(template);
                                //$("section").trigger("create");
                                //$("section").get(0).parser.parse();

                            }
                        }

                    } else {
                        showErrMsg(reason);
                    }
                } else {
                    showErrMsg(errMsg);
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
        var $this=$(this);
        var courseID =$(this).val();
        var applyingStudentNumber=$(this).parents("div.am-accordion-content").find("span.my-approving-number").text();
        if(applyingStudentNumber!=0){
            showErrMsg("已有学生提交申请，这节课不允许删除！想要删除，必须先让学生退课！");
            return false;
        }
        $('.my-delete-dialog').modal({
            onConfirm: function () {
                $.ajax({
                    type: 'POST',
                    async:false,
                    url: DELETECOURSE,
                    //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
                    contentType: "application/json",
                    data: {
                        'token': token,
                        'courseID': courseID
                    },
                    beforeSend: function () {
                        // 禁用按钮防止重复提交
                        $this.attr("disabled","true");
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
                                window.location.reload(true);
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

            },
            //closeOnConfirm: false,
            onCancel: function () {
            }
        });
    });


    /**
     * 老师添加课程
     *
     */

    $(document).on('click', '.my-add-button', function () {
        //获取学院列表
        $.ajax({
            type: 'POST',
            url: GETALLCOLLEGE,
            contentType: "application/json",
            data: {},
            async: false,
            beforeSend: function () {
                $('#my-loading').modal('open');
            },
            success: function (data) {
                var code = data.code,
                    errMsg = data.errMsg,
                    reason = data.reason,
                    success = data.success;
                if (code == 1) {
                    if (success == true) {
                        var collegeList = data.collegeList;
                        var oSelect = $('.my-college');
                        for (var i = 0; i < collegeList.length; i++) {
                            var option = $("<option value=" + collegeList[i].collegeID + ">" + collegeList[i].collegeName + "</option>");
                            option.appendTo(oSelect);
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
        $("#my-prompt").modal({
            onConfirm: function (){
                 var courseNumber=$("#my-courseNumber").val();
                 var courseOrder=$("#my-courseOrder").val();
                 var courseName=$("#my-courseName").val();
                 var collegeID=$("#doc-select-1").find("option:selected").val();

                var $this=$(this);
                $.ajax({
                    type: 'POST',
                    async:false,
                    url: ADDCOURSE,
                    //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
                    contentType: "application/json",
                    data: {
                        'token': token,
                        'courseNumber': courseNumber,
                        'courseOrder': courseOrder,
                        'courseName': courseName,
                        'collegeID': collegeID
                    },
                    beforeSend: function () {
                        // 禁用按钮防止重复提交
                        $this.attr("disabled","true");
                    },
                    success: function (data) {
                        var code = data.code,
                            errMsg = data.errMsg,
                            reason = data.reason,
                            success = data.success;
                        if (code === 1) {
                            if (success === true) {
                                //成功后执行的业务
                                alert( '新开课程成功');
                                window.location.reload(true);
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
            },
            onCancel:function(){}
        });
    });



    /**
     * 老师打分
     *
     */

    $(document).on('click', '.my-mark', function () {
          var $this=$(this);
          var courseID=$this.parents("div.am-g").find("button.delete-course").val();
          Cookies.set("courseID",courseID,{path: '/'});
          window.location.href="teacherMarkGrade.html";
    });

    /**
     * 老师批准学生
     *
     */
    $(document).on('click', '.my-approve', function () {
        var $this=$(this);
        var courseID=$this.parents("div.am-g").find("button.delete-course").val();
        Cookies.set("currentCourseID",courseID,{path: '/'});
        window.location.href="teacherApproveStudent.html";
    });




});
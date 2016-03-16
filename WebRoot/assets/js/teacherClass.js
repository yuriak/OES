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


    /**
     *
     * 获得课节列表
     *
     */

     //进入课程
     var courseID=Cookies.get('currentCourseID');

                $.ajax({
                    type: 'POST',
                    async:false,
                    url: GETLESSONLISTBYCLASS,
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
                                var lessonList = data.lessonList;
                                for(var i=0;i<lessonList.length;i++){
                                    var lessonID=lessonList[i].lessonID;
                                    var startTime=lessonList[i].startTime;
                                    var lessonStatus=lessonList[i].lessonStatus;
                                    var emergencyLevel=lessonList[i].emergencyLevel;
                                    var hasLeave=lessonList[i].hasLeave;

                                    var template = $('.template-class').children('.my-teacher-course').clone();

                                    //渲染
                                    var btn1 = template.find('.my-code-class');
                                    var btn2 = template.find('.my-code-time');
                                    var number1 = template.find('.delete-class');
                                    var number2=template.find(".my-accordion-button");
                                    var number3=template.find(".my-approve");

                                    btn1.text(i+1);
                                    btn2.text(startTime);
                                    number1.val(lessonID);
                                    number2.val(lessonID);
                                    number3.val(lessonID);
                                    //处理课节状态
                                    var oLessonName = template.find('.my-accordion-button').eq(0);
                                    //请假按钮的渲染
                                    if(hasLeave==true){
                                    template.find(".am-panel-hd").append("<div class=\"am-fr\"><a  href=\"##\" class=\"student-leave-list\">有学生请假</a></div>");
                                    template.find(".my-approve").css("background-color","green");
                                    template.find(".my-approve").css("color","white");
                                    
                                    }else{
                                    	template.find(".my-approve").attr('disabled');
                                    }
                                    
                                    if (emergencyLevel == EMERGENCY) {
                                        template.find('i').eq(0).css('color', '#E63911');
                                    }
                                    switch (lessonStatus) {
                                        case -1: //课程还未开始
                                            oLessonName.css('background-color', '#C7C7C7');
                                            break;
                                        case 0:  //课程正在进行
                                            oLessonName.css('background-color', '#1BB8FA');
                                            template.find('.my-approve').attr('disabled','true');
                                            break;
                                        case 1: //课程已结束
                                            oLessonName.css('background-color', '#6FD668');
                                            template.find('.my-approve').attr('disabled','true');
                                            break;
                                        default :
                                            break;
                                    }

                                    $('section').prepend(template);


                                }
                                $("body").append("<script src=\"assets/js/amazeui.min.js\"></script>");


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


//点击课节--获得公告

    $(document).on('click', '.my-accordion-title', function () {
        var lessonID=$(this).parents("dl").find(".my-accordion-button").val();
        var $this=$(this);
        $.ajax({
            type: 'POST',
            async:false,
            url: GETNOTICEBYCLASSID,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                'token': token,
                'lessonID': lessonID
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
                        //成功后--渲染公告
                       var  point=$this.parents(".my-teacher-course");
                       var  noticeID=data.noticeID;
                       var  noticeContent=data.noticeContent;
                       var  emergencyLevel=data.emergencyLevel;
                       point.find(".my-modify").val(noticeID);
                       point.find(".my-class-content").text(noticeContent);
                       point.find(".my-emergencyLevel").val(emergencyLevel);
                        if (emergencyLevel == EMERGENCY) {
                            point.find('i').eq(0).css('color', '#E63911');
                        }
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

    });






    /**
     *
     * 修改公告后保存
     *
     */
//修改公告

    $(document).on('click', '.my-modify', function () {
            //获得公告内容
        var $this=$(this);
        var parent=$this.parents(".am-accordion-bd");
        var notice=parent.find("span.my-class-content").html();
        var noticeID=$this.val();
        var Level=parent.find(".my-emergencyLevel").val();
            //显示在dialog中
        if(Level==1){
            $("#my-check-box2").attr("checked","true");
        }else{$("#my-check-box2").attr("checked","false");}
            $('#doc-vld-age-3').val(notice);

            $('#my-prompt').modal({
                relatedTarget: this,
                onConfirm: function(e) {
                    //发送公告并在成功后修改公告
                    var emergencyLevel=0;
                    if($("#my-check-box2").get(0).checked){
                        emergencyLevel=1;
                    }
                   var noticeContent=e.data;
                    $.ajax({
                        type: 'POST',
                        async:false,
                        url: UPDATENOTICE,
                        //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
                        contentType: "application/json",
                        data: {
                            'token': token,
                            'noticeID': noticeID,
                            'noticeContent':noticeContent,
                            'emergencyLevel':emergencyLevel
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
                                    //成功后--渲染公告
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
                onCancel: function() {
                }
            });
    });





    /**
     *
     * 删除课程
     *
     */
    $(document).on('click', '.delete-class', function () {
        var $this=$(this);
        var point=$this.parents(".my-teacher-course");
        var lessonID = $(this).val();
        $('.my-delete-dialog').modal({
            onConfirm: function () {
            	console.log(lessonID);
                $.ajax({
                    type: 'POST',
                    async:false,
                    url: DELETELESSON,
                    //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
                    contentType: "application/json",
                    data: {
                        'token': token,
                        'lessonID': lessonID
                    },
                    beforeSend: function () {
                        // 禁用按钮防止重复
                        $this.attr("disabled","true");
                    },
                    success: function (data) {
                        var code = data.code,
                            errMsg = data.errMsg,
                            reason = data.reason,
                            success = data.success;
                        if (code === 1) {
                            if (success === true) {
                            	        point.remove();
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



//点击添加课节
    $(document).on('click', '.my-add-button', function () {
            $("#my-prompt-1").modal({
                onConfirm: function (){
                    var emergencyLevel=0;
                    var noticeContent=$("#doc-vld-age-2").val();
                    if(noticeContent==null||noticeContent==""){
                      noticeContent="";
                    }
                    if($("#my-check-box1").get(0).checked){
                        emergencyLevel=1;
                    }
                    $.ajax({
                        type: 'POST',
                        async:false,
                        url: ADDLESSON,
                        //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
                        contentType: "application/json",
                        data: {
                            'token': token,
                            'courseID': courseID,
                            'noticeContent':noticeContent,
                            'emergencyLevel':emergencyLevel
                        },
                        beforeSend: function () {},
                        success: function (data) {
                            var code = data.code,
                                errMsg = data.errMsg,
                                reason = data.reason,
                                success = data.success;
                            if (code === 1) {
                                if (success === true) {
                                    //成功后执行的业务
                                    //成功后执行的业务
                                    var oTemplate=$(".template-class").children(".my-teacher-course");
                                    var nTemplate=oTemplate.clone();
                                    $("section").prepend(nTemplate);
                                    var width = $('.am-accordion-item').width();
                                    $('.my-accordion-button').css('width', width - 45.997);
                                    $("body").append("<script src=\"assets/js/amazeui.min.js\"></script>");
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
                    //window.location.reload(true);
                },
                onCancel:function(){}
            });
    });


    //批准请假

    $(document).on('click', '.my-approve', function () {
       var lessonID=$(this).val();
       Cookies.set("lessonID",lessonID);
        window.location.href="teacherApproveStudentLeave.html";
    });


    //点击进入课节--设置评价项
    $(document).on('click', '.my-accordion-button', function () {
        var lessonID=$(this).val();
        Cookies.set("lessonID",lessonID);
        window.location.href="teacherEvaluation.html";
    });





});
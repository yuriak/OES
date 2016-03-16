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
 * 下拉组件
 */
   /* $(document).on('click', '.my-accordion-title-evaluation', slideDown);
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


    $(document).on('click', '.my-accordion-title-group', slideDown);
    function slideDown() {
        var value = $(this).attr('value');
        var content = $(this).nextAll('dd');

        if (value == 1) {
            content.slideDown('slow');
            $(this).attr('value', 0);
        } else if (value == 0) {
            content.slideUp('slow');
            $(this).attr('value', 1);
        }

    }
*/


    /**
     *
     * 获得评价项列表
     *
     */
    var lessonID=Cookies.get('lessonID');//课节ID

    $.ajax({
        type: 'POST',
        async:false,
        url: GETEVALUATIONLISTBYCLASS,
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
                    var evaluationList = data.evaluationList;
                    for(var i=0;i<evaluationList.length;i++){

                        var evaluationID=evaluationList[i].evaluationID;//评价项ID
                        var evaluationTitle=evaluationList[i].evaluationTitle;
                        var evaluationStatus=evaluationList[i].evaluationStatus;
                        var isGroupEvaluation=evaluationList[i].isGroupEvaluation;

                        var templateEvaluation = $('.template-evaluation').children('.my-teacher-course').clone();

                        //渲染
                        var btn1 = templateEvaluation.find('.my-accordion-button');
                        btn1.text(evaluationTitle);
                        //写入evaluationID
                        var btn2=templateEvaluation.find('.evaluation-star');
                        var btn3=templateEvaluation.find('.evaluation-stop');
                        var btn4=templateEvaluation.find('.check-group');
                        var btn5=templateEvaluation.find('.delete-evaluation');
                        btn2.val(evaluationID);
                        btn3.val(evaluationID);
                        btn4.val(evaluationID);
                        btn5.val(evaluationID);
                        //状态
                        var inp=templateEvaluation.find('input.my-status-evaluation');
                        inp.val(evaluationStatus);


                        //分组信息--按钮是否变灰
                        if(isGroupEvaluation){btn4.removeAttr("disabled")}

                        //评价状态
                        switch (evaluationStatus) {
                            case -1://评价还未开始
                                templateEvaluation.find('code span').text('评价还未开始');
                                //禁用“停止评价”的按钮
                                btn3.attr('disabled', 'disabled');
                                break;
                            case 0://评价进行中
                                templateEvaluation.find('code span').text('评价进行中');
                                //禁用“开始评价和删除评价”的按钮
                                btn2.attr('disabled', 'disabled');
                                btn5.attr('disabled', 'disabled');
                                break;
                            case 1://评价已结束
                                templateEvaluation.find('code span').text('评价已结束');
                                //禁用“停止评价”的按钮
                                btn3.attr('disabled', 'disabled');
                                btn5.attr('disabled', 'disabled');
                                break;
                        }
                        $('section.my-section-evaluation').append(templateEvaluation);
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


    /**
     * 新建评价项
     */


    /**
     * 开始与结束评价
     */
    //开始
    $(document).on('click', '.evaluation-star', function () {
        var $this=$(this);
        var evaluationID=$(this).val();
        var evaluationStatus=0;
        $.ajax({
            type: 'POST',
            url:SETEVALUATIONSTATUS,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "token":token,
                "evaluationID":evaluationID,
                "evaluationStatus":evaluationStatus
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
    });

    //停止评价
    $(document).on('click', '.evaluation-stop', function () {
        var $this=$(this);
        var evaluationID=$(this).val();
        var evaluationStatus=1;
        $.ajax({
            type: 'POST',
            url:SETEVALUATIONSTATUS,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "token":token,
                "evaluationID":evaluationID,
                "evaluationStatus":evaluationStatus
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
    });





    /**
     * 查看分组信息--保存为组模板
     */
    var evaluationID;
    $(document).on('click', '.check-group', function () {
        var $this=$(this);
        evaluationID=$this.val();
        $(".my-section-group").empty();
        $.ajax({
                    type: 'POST',
                    url:GETGROUPMEMBERINFO,
                    //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
                    contentType: "application/json",
                    data: {
                        "token":token,
                        "evaluationID":evaluationID
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
                                var groupList=data.groupList;

                                for(var i=0;i<groupList.length;i++){
                                    var templateGroup = $('.template-group').children('dl').clone();
                                    var groupID=groupList[i].groupID;
                                    var groupNumber=groupList[i].groupNumber;
                                    var groupMemberList=groupList[i].groupMemberList;
                                    templateGroup.find("dt").html("第"+groupNumber+"组");
                                    templateGroup.find("ul.my-ul").empty();
                                    for(var j=0;j<groupMemberList.length;j++){
                                        var studentName=groupMemberList[j].studentName;
                                        var studentID=groupMemberList[j].studentID;
                                        templateGroup.find("ul.my-ul").eq(0).append("<li class=\"li-list\">"+studentName+"</li>");
                                    }
                                    $(".my-section-group").append(templateGroup);
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
        $('#my-popup').modal({});
      });



    $('.am-close').click(function(){
    	$("#my-prompt").modal("close");
    });
    

    /**
     * 删除评级项
     */
    $(document).on('click', '.delete-evaluation', function () {
            var $this=$(this);
            var evaluationID=$this.val();
            $.ajax({
                type: 'POST',
                url:DELETEEVALUATION,
                //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
                contentType: "application/json",
                data: {
                    "token":token,
                    "evaluationID":evaluationID
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
        });




    $(document).on('click', '.my-add-group', function () {
    	$("#doc-vld-age-3").val("");
        $("#my-prompt").modal({
            onConfirm: function() {
            	if($("#doc-vld-age-3").val().trim()==""){
            		showErrMsg("不可以空着");
            		return false;
            	}
            	if(evaluationID==null){
            		showErrMsg("无效评价项");
            		return false;
            	}
                //保存成功后，给个提示，然后跳回评价项列表
                var groupTemplateName= $("#doc-vld-age-3").val();
                $.ajax({
                    type: 'POST',
                    url:ADDGROUPTEMPLATE,
                    //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
                    contentType: "application/json",
                    data: {
                        "token":token,
                        "evaluationID":evaluationID,
                        "groupTemplateName":groupTemplateName
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
                                showSuccessMsg("添加模板成功");
                                $("#my-prompt").modal("close");
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
            	$("#my-prompt").modal("close");
            }
        });
    });




//点击添加评价项
    $(document).on('click', '.my-add-button', function () {
        Cookies.set("currentLessonID",lessonID);
        window.location.href="teacherAddEvaluation.html";
    });



  //点击评价项--进入查看结果页面
    $(document).on('click', '.my-accordion-button', function () {
        var evaluationID=$(this).parents("dl").find(".evaluation-star").val();
        Cookies.set("evaluationID",evaluationID);
        window.location.href="teacherResult.html";
    });




});
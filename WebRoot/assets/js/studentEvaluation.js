/**
 * Created by fei on 2016/3/6.
 */

$(function () {
    var token = Cookies.get('token');
    var lessonID = Cookies.get('currentLessonID');
    if (token == null || token == '') {
        showErrMsg("未登录，请先登录！");
        location.href = "login.html";
        return false;
    }

    $.ajax({
        type: 'POST',
        url: GETEVALUATIONLISTBYCLASS,
        contentType: "application/json",
        data: {
            'token': token,
            'lessonID': lessonID
        },
        success: function (data) {
            var code = data.code,
                errMsg = data.errMsg,
                reason = data.reason,
                success = data.success;
            if (code === 1) {
                if (success === true) {
                    //成功后执行的业务

                    var evaluationList = data.evaluationList;
                    for (var i = 0; i < evaluationList.length; i++) {
                        templateGroup = $('#template-group').children('dl').eq(0).clone();
                        templatePerson = $('#template-person').children('dl').eq(0).clone();

                        var evaluationID = evaluationList[i].evaluationID,
                            evaluationTitle = evaluationList[i].evaluationTitle,
                            evaluationStatus = evaluationList[i].evaluationStatus,
                            isGroupEvaluation = evaluationList[i].isGroupEvaluation;
                        switch (evaluationStatus) {
                            case -1://评价还未开始
                                templateGroup.find('code span').text('评价还未开始');
                                templatePerson.find('code span').text('评价还未开始');

                                //禁用“评价/查看结果”的按钮
                                templateGroup.find('button[data-evaluation-id]').eq(0).attr('disabled', 'disabled');
                                templatePerson.find('button[data-evaluation-id]').eq(0).attr('disabled', 'disabled');

                                break;
                            case 0://评价进行中
                                templateGroup.find('code span').text('正在评价中');
                                templatePerson.find('code span').text('正在评价中');
                                //可以重新再加组
                                var select = templateGroup.find('select').eq(0);
                                select.removeAttr('disabled');
                                templateGroup.find('.add-group').eq(0).removeAttr('disabled');

                                //启用"设我为主讲人"的按钮
                                templatePerson.find('button[data-lecture-evaluation-id]').eq(0).removeAttr('display');
                                templatePerson.find('button[data-lecture-evaluation-id]').eq(0).css('display', 'block');
                                break;
                            case 1://评价已结束
                                templateGroup.find('code span').text('评价已结束');
                                templatePerson.find('code span').text('评价已结束');

                                //不能再加组了
                                var select = templateGroup.find('select').eq(0);
                                select.attr('disabled', 'disabled');
                                templateGroup.find('.add-group').eq(0).attr('disabled', 'disabled');

                                //禁用设我为主讲人的按钮
                                templatePerson.find('button[data-lecture-evaluation-id]').eq(0).attr('display', 'disabled');
                                templatePerson.find('button[data-lecture-evaluation-id]').eq(0).css('display', 'none');
                                break;
                        }
                        if (isGroupEvaluation == true) {   //组评价
                            templateGroup.find('[data-evaluation-id]').get(0).dataset.evaluationId = evaluationID;

                                $.ajax({
                                    type: 'POST',
                                    async: false,
                                    url: GETGROUPINFO,
                                    contentType: "application/json",
                                    data: {
                                        'token': token,
                                        'evaluationID': evaluationID
                                    },
                                    success: function (data) {
                                        var code = data.code,
                                            errMsg = data.errMsg,
                                            reason = data.reason,
                                            success = data.success;
                                        if (code === 1) {
                                            if (success === true) {
                                                //成功后执行的业务
                                                var allGroupInfo = data.allGroupInfo;
                                                var yourGroupInfo = data.yourGroupInfo;

                                                //var templateGroup = oTemplateGroup.clone();
                                                templateGroup.children('dt').eq(0).text(evaluationTitle);
                                                if (yourGroupInfo.groupID != null) {
                                                    var groupID = yourGroupInfo.groupID;
                                                    var groupNumber = yourGroupInfo.groupNumber;
                                                    var select = templateGroup.find('select').eq(0);
                                                    select.append("<option value='" + groupID + "' selected>" + groupNumber + "</option>");
                                                    select.attr('disabled', 'disabled');
                                                    templateGroup.find('.add-group').eq(0).attr('disabled', 'disabled');
                                                    templateGroup.appendTo($('section'));
                                                } else {
                                                    var oSelect = templateGroup.find('select').eq(0);
                                                    oSelect.append("<option value='' selected>请选择组</option>");
                                                    for (var i = 0; i < allGroupInfo.length; i++) {
                                                        var groupID = allGroupInfo[i].groupID;
                                                        var groupNumber = allGroupInfo[i].groupNumber;
                                                        oSelect.append("<option value='" + groupID + "'>第" + groupNumber + "组</option>");
                                                    }
                                                    templateGroup.appendTo($('section'));
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


                        } else { //不是分组，个人是评价对象
                            var isReceiver= evaluationList[i].isReceiver;
                            var setLecture = templatePerson.find('[data-lecture-evaluation-id]').eq(0);
                            if(isReceiver==true){//已经设自己为主讲人了
                                setLecture.text('我是主讲人');
                                setLecture.attr('disabled','disabled');
                            }
                            templatePerson.find('[data-evaluation-id]').get(0).dataset.evaluationId = evaluationID;
                            setLecture.get(0).dataset.lectureEvaluationId = evaluationID;
                            templatePerson.children('dt').eq(0).text(evaluationTitle);
                            templatePerson.appendTo($('section'));
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

        },
        error: function () {
            showServiceError();
        },
        dataType: 'json'
    });


    /**
     * 设我为评价者
     */
    $(document).on('click', '.lecture', function () {
        var $this = $(this);
        var evaluationID = $this.attr('data-lecture-evaluation-id');
        $('#my-prompt').modal({
            onConfirm: function () {
                $('#my-confirm').find('[data-am-modal-confirm]').get(0).dataset.amModalConfirm = evaluationID;
                $('#my-confirm').modal();  //见“再次确认提交”
            },
            onCancel: function () {
                $('#my-error').modal();
            }
        });
    });

    /**
     * 再次确认提交
     */
    $('#my-confirm').find('[data-am-modal-confirm]').click(function () {
        var evaluationID = $(this).attr('data-am-modal-confirm');
        $.ajax({
            type: 'POST',
            url: SETMEASRECEIVER,
            contentType: "application/json",
            data: {
                'token': token,
                'evaluationID': evaluationID
            },
            success: function (data) {
                var code = data.code,
                    errMsg = data.errMsg,
                    reason = data.reason,
                    success = data.success;
                if (code === 1) {
                    if (success === true) {
                        //成功后执行的业务
                        $(this).attr('disabled', 'disabled');
                        showSuccessMsg('设置成功！');
                        window.location.reload();

                    } else {
                        showErrMsg(reason);
                    }
                } else {
                    showErrMsg(errMsg);
                }
            },
            error: function () {
                showServiceError();
            },
            dataType: 'json'
        });
    });

    /**
     *
     * API 加组
     *
     */
    $(document).on('click', '.add-group', function () {
        var $this = $(this);
        var oSelect = $this.parents('.group-select');
        var oSelected = oSelect.find('select option:selected');
        var groupID = oSelected.val();
        if (groupID == null || groupID == "") {
            showErrMsg('请选择组！');
            return false;
        }
        var groupNumber = oSelected.text();
        var group = $('select.group-select').find("option:selected").text();

        $("#my-group .am-modal-bd").text("确定加入" + groupNumber + "吗？");
        $("#my-group").modal({
            onConfirm: function () {//确定后向后台添加组

                var token = Cookies.get('token');
                $.ajax({
                    type: 'POST',
                    url: ADDGROUP,
                    contentType: "application/json",
                    data: {
                        'token': token,
                        'groupID': groupID
                    },
                    success: function (data) {
                        var code = data.code,
                            errMsg = data.errMsg,
                            reason = data.reason,
                            success = data.success;
                        if (code === 1) {
                            if (success === true) {
                                //成功后执行的业务
                                showSuccessMsg('你已成为' + groupNumber + '的成员！');
                                $this.attr('disabled', 'true');
                                oSelect.attr('disabled', 'true');
                            } else {
                                showErrMsg('加组失败！！你' + reason);
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
            },
            onCancel: function () {//取消操作
                $('#my-error').modal();
            }
        });

    });
    /**
     *
     * 评价/查看结果
     *
     */

    $(document).on('click', 'button[data-evaluation-id]', function () {
        var evaluationID = $(this).attr('data-evaluation-id');
        Cookies.set('currentEvaluationID', evaluationID, {path: '/'});
        window.location.href = "studentEvaluate.html";
    });

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




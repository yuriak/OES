/**
 * Created by S.F.S on 2016/2/14.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {

    /*ok*/
    var token = Cookies.get('token');
    if (token == null || token == '') {
        showErrMsg("未登录，请先登录！");
        window.location.href = "login.html";
        return false;
    }


    //表单验证ok
    $(function() {
        $('#doc-vld-msg').validator({
            onValid: function(validity) {
                $(validity.field).closest('.am-form-group').find('.am-alert').hide();
            },

            onInValid: function(validity) {
                var $field = $(validity.field);
                var $group = $field.closest('.am-form-group');
                var $alert = $group.find('.am-alert');
                // 使用自定义的提示信息 或 插件内置的提示信息
                var msg = $field.data('validationMessage') || this.getValidationMessage(validity);

                if (!$alert.length) {
                    $alert = $('<div class="am-alert am-alert-danger"></div>').hide().
                        appendTo($group);
                }

                $alert.html(msg).show();
            }
        });
    });

    /**
     * 获取教师信息ok
     */
    $.ajax({
        type: 'POST',
        url: GETUSERINFO,
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
                reason = data.reason,
                success = data.success;
            if (code == 1) {
                if (success == true) {
                    var userName = data.userName;
                    var realName = data.realName;
                    var gender = data.gender;
                    var email = data.email;
                    var phone = data.phone;
                    var college = data.collegeName;

                    $(".my-userName").val(userName);
                    $(".my-realName").val(realName);
                    $(".my-email").val(email);
                    $(".my-phone").val(phone);
                    if (gender == 1) {
                        $(".my-man").attr("checked", "true");
                    } else {
                        $(".my-woman").attr("checked", "true");
                    }


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

                    var list = $(".my-college").get(0).options;
                    for (var i = 0; i < list.length; i++) {
                        var val = list[i].text;
                        if (val == college) {
                            list[i].selected=true;
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


    //表单的对象
    var oRealName = $('.my-realName'),
        oPhone = $('.my-phone'),
        oEmail = $('.my-email'),
        oCollege = $('.my-college');

    //保存按钮的禁用和激活ok
    oRealName.bind('input', save);
    oPhone.bind('input', save);
    oEmail.bind('input', save);
    oCollege.bind('input', save);

    function save() {
        var oInput = $('.my-user-info input');
        var boo = true;
        //判断所有input是否有值
        for (var i = 0; i < oInput.length; i++) {
            if (oInput[i].value.length == 0 || oInput[i].value.length == null) {
                boo = false;
            }
        }
        if (boo === true) {
            $('#my-save').removeAttr('disabled');
        } else {
            $('#my-save').attr('disabled', 'disabled');
        }

    }


    //点击保存按钮--form表单格式判断ok

    $('#my-save').click(function () {
        var realName = oRealName.val(),
            phone = oPhone.val(),
            email = oEmail.val(),
            gender = $("input[name='gender']:checked").val(),
            collegeID = $(".my-college").val();

        //检验格式是否正确
        if ( realName.length == null || realName.length == 0) {
            showErrMsg('请填写姓名');
            return false;
        }
        if (checkEmail(email) == false) {
            showErrMsg('邮箱格式错误');
            return false;
        }
        if (checkPhone(phone) == false) {
            showErrMsg('手机格式错误');
            return false;
        }

        /**
         * 验证正则表达式
         *
         */
        function checkPhone(str) {
            var re = /^[0-9]*$/;
            var boo = re.test(str);
            return boo;
        }

        function checkEmail(str) {
            var re = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
            var boo = re.test(str);
            return boo;
        }
        /**
         *
         * Ajax
         *
         * */

        /**
         * 保存ok
         */
        $.ajax({
            type: 'POST',
            url: UPDATEUSERINFO,
            contentType: "application/json",
            data: {
                "token": token,
                "realName": realName,
                "phone": phone,
                "email": email,
                "major": "",
                "gender": gender,
                "collegeID": collegeID
            },
            beforeSend: function () {
                // 禁用按钮防止重复提交
                $("#my-save").attr("disabled", "disabled");
                $('#my-loading').modal('open');
            },
            success: function (data) {
                var code = data.code,
                    errMsg = data.errMsg,
                    reason = data.reason,
                    success = data.success;
                if (code == 1) {
                    if (success == true) {
                        var msg = "修改成功！";
                        $('#my-loading').modal('close');
                        showSuccessMsg(msg);
                    } else {
                        showErrMsg(reason);
                    }
                } else {
                    $('#my-loading').modal('close');
                    showErrMsg(errMsg);
                }
            },
            complete: function () {
                $('#my-loading').modal('close');
            },
            error: function () {
                $('#my-loading').modal('close');
                showServiceError();
            },
            dataType: 'json'
        })
    });


});


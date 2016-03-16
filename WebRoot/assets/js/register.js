/**
 * Created by S.F.S on 2016/2/14.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {
    $('#teacher').click(function () {
        $('#userName').attr('placeholder', '用户名（请输入你的用户名）');
        $('#userName').attr('minlength','0');
        $('.major').css('display','none')
    })
    $('#student').click(function () {
        $('#userName').attr('placeholder', '用户名（请输入你的学号）');
        $('#userName').attr('minlength','8');
        $('.major').css('display','block')
    })
    //自定义下拉框
    $('#select-question').change(function () {
    	if(this.value!='0'){
            $('#input-question').val(this.value);
        }
    })
    //表单验证
    $('#doc-vld-msg').validator({
        onValid: function (validity) {
            $(validity.field).closest('.am-form-group').next('.am-alert').hide();
        },
        onInValid: function (validity) {
            var $field = $(validity.field);
            var $group = $field.closest('.am-form-group');
            var $alert = $group.find('.am-alert');
            var $alert1 = $group.next('.am-alert');
            // 使用自定义的提示信息
            var msg = $field.data('validationMessage');
            if (!$alert1.length) {
                $alert = $('<div class="my-alert am-alert am-alert-danger"></div>').hide().insertAfter($group);
            }
            $alert.html(msg).show();
        }
    });

    //返回按钮的状态
    var backBoo = 0;
    //用户名是否可用
    var userBoo = false;
    //表单的对象
    var oUserName = $('#userName'),
        oPassword = $('#password'),
        oAgainPassword = $('#againPassword'),
        oRealName = $('#realName'),
        oPhone = $('#phone'),
        oEmail = $('#email'),
        oQuestion = $("#input-question"),
        oAnswer = $("#answer"),
        oMajor = $('#major'),
        oCollege = $('#college');


    //"下一步"按钮的状态（禁用或者激活）
    oUserName.bind('input', showNext);
    oAgainPassword.bind('input', showNext);
    oPassword.bind('input', showNext);
    oRealName.bind('input', showNext);
    oPhone.bind('input', showNext);
    oEmail.bind('input', showNext);
    oMajor.bind('input', showNext);
    oCollege.bind('change', showNext);

    function showNext() {
        var oInput = $('.my-register input');
        var boo = true;
        var identity1=$("input[name='identity']:checked").val();
        //判断所有input是否有值

        for (var i = 0; i < oInput.length; i++) {
            if(!(identity1==1 && oInput.eq(i).attr('id')=='major')){
                if (oInput[i].value.length == 0 || oInput[i].value.length == null) {
                    boo = false;
                }
            }
        }
        
        if(oCollege.children('option:selected').attr('value')==0){
        	boo = false;
        }
        if (boo === true && userBoo === true) {
            $('#nextStep').removeAttr('disabled');
        } else {
            $('#nextStep').attr('disabled', 'disabled');
        }

    }


    $('#back').click(back);
    function back() {
        if (backBoo === 0) {
            window.history.go(-1);
        } else {
            $('.my-register').show();
            $('.protection').hide();
            backBoo = 1;//返回按钮可以返回到上一个html
        }
    }

    //"注册"按钮的状态（禁用或者激活）
    oQuestion.bind('input', showSubmit);
    oAnswer.bind('input', showSubmit);
    function showSubmit() {
        var _question = oQuestion.val().trim();
        var _answer = oAnswer.val().trim();
        var _boo = true;

        if (_question.length == 0 || _question.length == null) {
            _boo = false;
        }
        if (_answer.length == 0 || _answer.length == null) {
            _boo = false;
        }
        if (_boo === true) {
            $('#submit').removeAttr('disabled');
        } else {
            $('#submit').attr('disabled', 'disabled');
        }
    }

    //注册"第一步"表单的检测
    $('#nextStep').click(function () {
    	var userName = oUserName.val(),
        password = oPassword.val(),
        againPassword = oAgainPassword.val(),
        realName = oRealName.val(),
        phone = oPhone.val(),
        email = oEmail.val(),
        question = oQuestion.val(),
        answer = oAnswer.val(),
        role = $("input[name='identity']:checked").val(),
        gender = $("input[name='gender']:checked").val(),
        college = $("#college").val();
        //用户名和密码都不能超过20个字符，密码不能有空格
    	var reg=/.* .*/;
        if (userName.length > 20 || password.length > 20 || reg.test(userName) || reg.test(password)) {
        	showErrMsg('用户名和密码不能包含空格，且不能超过20位');
            return false;
        }
        var _boo = true;

       
        if (password.length < 6 || password.length == null) {
            _boo = false;
        }
        if (againPassword != password) {
            _boo = false;
        }
        var identity1=$("input[name='identity']:checked").val();
        if(identity1==0){
            if (major.length === null || major.length == 0 || realName.length == null || realName.length == 0) {
                _boo = false;
                showErrMsg('请填写专业');
                return false;
            }
            if (userName.length < 8 || userName.length == null) {
            	alert('请使用学号注册');
                _boo = false;
            }
        }

        if (checkPhone(phone) == false) {
            _boo = false;
            showErrMsg('手机格式错误');
        	return false;
        }
        if (checkEmail(email) == false) {
            _boo = false;
            showErrMsg('邮箱格式错误');
        	return false;
        }
        if (college == 0) {
        	
            _boo = false;
            showErrMsg('请选择学院');
        	return false;
        }
        if (_boo == true) {
            backBoo = 1;//返回按钮可以返回上一步填写
            $('#my-alert-hint').modal();
            $('.my-register').hide();
            $('#register-text').text('填写密保问题');
            $('.protection').show();

            //下拉框的宽高
            $('#input-question').css('height', $('#select-question').css('height'));
            $('#input-question').css('width', function () {
                return $('.protect').width() - 44 - 62;
            })
        } else {
//            $('#my-alert-inconformity').modal();
        	showErrMsg('用户名或密码有误！');
        	return false;
        }
    });
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
     * 获取学院列表
     */
    $.ajax({
        type: 'POST',
        url: GETALLCOLLEGE,
        contentType: "application/json",
        data: {},
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
                    var oSelect = $('#college');
                    for (var i = 0; i < collegeList.length; i++) {
                        var option = $("<option value=" + collegeList[i].collegeID + ">" + collegeList[i].collegeName + "</option>");
                        option.appendTo(oSelect);
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
            $('#my-loading').modal('close');
        },
        error: function () {
            showServiceError();
        },

        dataType: 'json'
    });

    /**
     *
     * Ajax
     *
     * */
        //用户名：检测是否已注册
    oUserName.blur(function () {
        var _userName = oUserName.val();
        $.ajax({
            type: 'POST',
            url: CHECKUNIQUEUSER,
            contentType: "application/json",
            data: {
                "userName": _userName
            },
            success: function (data) {
                var code = data.code,
                    errMsg = data.errMsg,
                    reason = data.reason,
                    success = data.success;
                if (code == 1) {
                    if (success == true) {//用户名可以使用
                        userBoo=true;
                        return true;
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
        })
    });
    /**
     * 注册
     */
    $('#submit').click(function () {
            var userName = oUserName.val(),
                password = oPassword.val(),
                againPassword = oAgainPassword.val(),
                realName = oRealName.val(),
                phone = oPhone.val(),
                email = oEmail.val(),
                question = oQuestion.val(),
                answer = oAnswer.val(),
                role = $("input[name='identity']:checked").val(),
                gender = $("input[name='gender']:checked").val(),
                major = $("#major").val(),
                collegeID = $("#college").val();

            $.ajax({
                type: 'POST',
                url: REGISTER,
                contentType: "application/json",
                data: {
                    "userName": userName,
                    "password": password,
                    "againPassword": againPassword,
                    "realName": realName,
                    "phone": phone,
                    "email": email,
                    "major":major,
                    "question": question,
                    "answer": answer,
                    "role": role,
                    "gender": gender,
                    "collegeID": collegeID
                },
                beforeSend: function () {
                    // 禁用按钮防止重复提交
                    $("#submit").attr("disabled", "disabled");
                    $('#my-loading').modal('open');
                },
                success: function (data) {
                    var code = data.code,
                        errMsg = data.errMsg,
                        success = data.success,
                        reason = data.reason;
                    if (code == 1) {
                        if (success == true) {
                            if (!success) {//注册失败
                                var msg = "注册失败!请稍后再试";
                                $('#my-loading').modal('close');
                                showErrMsg(msg);
                            } else {//注册成功
                                var msg = "注册成功！请登录";
                                $('#my-loading').modal('close');
                                alert('注册成功');
                                window.location.href = "login.html";
                            }

                        } else {
                            showErrMsg(reason);
                        }
                    } else {
                        $('#my-loading').modal('close');
                        showErrMsg(errMsg);
                        $("#loginIn").removeAttr("disabled");
                    }
                },
                complete: function () {
                    $('#my-loading').modal('close');
                },
                error: function () {
                    $('#my-loading').modal('close');
                    showServiceError();
                    $("#loginIn").removeAttr("disabled");
                },
                dataType: 'json'
            })
        }
    );

});


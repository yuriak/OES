/**
 * Created by fei on 2016/3/5.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {
    //表单验证

    var userID,question;
    $('#doc-vld-msg').validator({
        onValid: function (validity) {
            $(validity.field).closest('.am-form-group').next('.am-alert').hide();
        },
        onInValid: function (validity) {
            var $field = $(validity.field);
            var $group = $field.closest('.am-form-group');
            var $alert = $group.find('.am-alert');
            var $alert1 = $group.next('.am-alert');
            var msg = $field.data('validationMessage');
            if (!$alert1.length) {
                $alert = $('<div class="my-alert am-alert am-alert-danger"></div>').hide().insertAfter($group);
            }
            $alert.html(msg).show();
        }
    });

    //定义表单对象
    var oUserName = $('#userName'),
        oQuestion = $("#question"),
        oAnswer = $("#answer");

    //"下一步"按钮的状态（禁用或者激活）
    oUserName.bind('input', showNext);
    function showNext() {
        var oInput = $('.my-search input');
        var boo = true;
        //判断所有input是否有值
        for (var i = 0; i < oInput.length; i++) {
            if (oInput[i].value.length == 0 || oInput[i].value.length == null) {
                boo = false;
            }
        }
        if (boo == true) {
            $('#nextStep').removeAttr('disabled');
        } else {
            $('#nextStep').attr('disabled', 'disabled');
        }
    }


    //返回按钮定义
    var hid;
    $("#back").click(function(){
        hid=$("#hh").val();
        console.log(hid);
        if(hid==-1){
            window.location.href="login.html";
        }else{
            $('.my-search').show();
            $('.protection').hide();
            hid=$("#hh").val("-1");

        }
    });

    //"提交"按钮的状态（禁用或者激活）
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
        if (_boo == true) {
            $('#submit').removeAttr('disabled');
        } else {
            $('#submit').attr('disabled', 'disabled');
        }
    }

    //"第一步"表单的检测(未完)
    $('#nextStep').click(function () {
        //表单数据
        var userName = oUserName.val().trim();
        var _boo = true;

        if (userName.length < 1 || userName.length == null) {
            _boo = false;
        }
        if (_boo == true) {
            $.ajax({
                type: 'POST',
                url: GETUSERQUESTIONBYUSERNAME,
                contentType: "application/json",
                data: {
                    "userName": userName,
                },
                beforeSend: function () {
                    // 禁用按钮防止重复提交
                },
                success: function (data) {
                    var code = data.code,
                        errMsg = data.errMsg,
                        success = data.success,
                        reason = data.reason;
                    if (code === 1) {
                        if (success === true) {
                            userID = data.userID;
                            question = data.question;
                            $('.my-search').hide();
                            $('#search-text').html('回答问题');
                            $('.protection').show();
                            hid=$("#hh").val("1");
                            $('#question').val(question);
                            $('#question').attr("disabled",true);
                            //Cookies.set('token', token, {expires: 99999, path: '/'});
                            //Cookies.set('role', role, {expires: 99999, path: '/'});
                        } else {
                            showErrMsg(reason);
                        }
                    } else {
                        showErrMsg(errMsg);
                    }

                },
                complete: function () {
                    $("#loginIn").removeAttr("disabled");
                },
                error: function () {
                    showServiceError();
                },
                dataType: 'json'
            });

        }
        else {
            $('#my-alert-modeError').modal();
        }

    })
    
    //验证答案是否正确
     $('#submit').click(function(){
         //返回一个success
         $.ajax({
             type: 'POST',
             url:VERIFYANSWER,
             contentType: "application/json",
             data: {
                 "userID": userID,
                 "answer":$('#answer').val().trim()
             },
             beforeSend: function () {

             },
             success: function (data) {
                 var code = data.code,
                     errMsg = data.errMsg,
                     success = data.success,
                     reason = data.reason;
                 if (code === 1) {
                     if (success === true) {
                         var token= data.token;
                         showSuccessMsg("答案正确");
                         Cookies.set('token', token, {expires: 30, path: '/'});
                         window.location.href='resetPwd.html';
                         //Cookies.set('role', role, {expires: 99999, path: '/'});
                     } else {
                         showErrMsg(reason);
                     }
                 } else {
                     showErrMsg(errMsg);
                 }

             },
             complete: function () {
                 $("#loginIn").removeAttr("disabled");
             },
             error: function () {
                 showServiceError();
             },
             dataType: 'json'
         });
         var success=true;
         if(success==true){
             //进入重置密码页面

         }
         else{
             $('my-alert-answerError').modal();
         }
     })



});


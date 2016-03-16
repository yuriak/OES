/**
 * Created by fei on 2016/3/6.
 */
document.write("<script src='assets/js/configure.js'></script>");
$(function () {
    var token = Cookies.get('token');
     if (token == null || token == '') {
     showErrMsg("未登录，请先登录！");
     location.href = "login.html";
     return false;
     }


/*返回按钮*/
//Ajax交互代码

//进入收信箱页面--获取信息列表
      $.ajax({
          type: 'POST',
          url:GETRECEIVEDMESSAGELIST,
          //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
          contentType: "application/json",
          data: {
              "token": token
          },
          beforeSend: function () {},
          success: function (data) {
              var code = data.code,
                  errMsg = data.errMsg,
                  reason = data.reason,
                  success = data.success;

              if (code === 1) {
                  if (success === true) {
                      //得到列表后执行的业务
                     var receivedMessageList=data.receivedMessageList;
                      for(var i=0;i<receivedMessageList.length;i++){
                          var messageID=receivedMessageList[i].messageID;
                          var senderName=receivedMessageList[i].senderName;
                          var sentTime=receivedMessageList[i].sentTime;
                          var content=receivedMessageList[i].content;
                          var title=receivedMessageList[i].title;
                          var emergencyLevel=receivedMessageList[i].emergencyLevel;
                          var openStatus=receivedMessageList[i].openStatus;
                          //var openTime=receivedMessageList[i].openTime;

                          var oTemplate=$(".template-message").children();
                          var nTemplate=oTemplate.clone();


                          nTemplate.find("span.my-title").html(title);
                          if(emergencyLevel==1){nTemplate.find("dt").append("<i class=\"am-icon-warning\" style=\"color: #bc4441\" ></i>")}
                          if(openStatus==1){
                              nTemplate.find("dt").append(" <i class=\"my-status-new am-icon-eye \" style=\"color: green\"></i>")
                          }else{nTemplate.find("dt").append("<i class=\"my-status-new am-icon-eye-slash \" style=\"color: red\"></i>")}

                          nTemplate.find("li.time").html("接收时间："+sentTime);
                          nTemplate.find("li.receiver").html("发送者："+senderName);
                          nTemplate.find("li.content").html("内容："+content);
                          nTemplate.find("button.my-reply-btn").attr("value",senderName);
                          nTemplate.find("button.my-delete-btn").attr("value",messageID);

                          $("section").prepend(nTemplate);
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



//点击再来一条信息--跳到新建信息页面
    $(document).on('click', '.my-reply-btn', replyMsg);
     function replyMsg(){
          var receiverName=$(this).val();
          Cookies.set('receiver',receiverName,{path:'/'});
          window.location.href="createMessage.html";
      }

//点击删除按钮
    $(document).on('click', '.my-delete-btn', deleteMsg);
   function deleteMsg(){
        var $this=$(this);
        var messageID=$this.val();
        $.ajax({
            type: 'POST',
            url:DELETEMESSAGE,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "token": token,
                "messageID":messageID
            },
            beforeSend: function () {},
            success: function (data) {
                var code = data.code,
                    errMsg = data.errMsg,
                    reason = data.reason,
                    success = data.success;

                if (code === 1) {
                    if (success === true) {
                        //删除信息
                        $this.parents('dl.am-accordion-item').remove;
                        window.location.reload(true);
                    } else {
                        showErrMsg(reason);
                    }
                } else {
                    showErrMsg(errMsg);
                    //window.location.reload(false);
                }
            },
            complete: function () {
            },
            error: function () {
                showServiceError();
            },
            dataType: 'json'
        });


    };

//下拉作用
    $(document).on('click', '.my-accordion-title', slide);
    function slide() {
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
 //改变信息查看状态

    $(document).on('click', '.my-accordion-title', change);
    function change(){
        var $this=$(this);
        var messageID=$this.parent().find("button.my-delete-btn").eq(0).attr("value");
        var bb=$this.find("input.my-input").attr("value");
        $.ajax({
            type: 'POST',
            url:SETMESSAGEOPENSTATUS,
            //url: 'http://127.0.0.1:8080/OES/login', //注意：绝对路径会涉及到跨域问题
            contentType: "application/json",
            data: {
                "token": token,
                "messageID":messageID
            },
            beforeSend: function () {},
            success: function (data) {
                var code = data.code,
                    errMsg = data.errMsg,
                    reason = data.reason,
                    success = data.success;

                if (code === 1) {
                    if (success === true) {
                        //改变是否查看状态
                        if(bb==0) {
                            $this.find("input.my-input").attr("value","1");
                        }else{
                            $this.find("input.my-input").attr("value","0");
                            window.location.reload(true);
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
    };




});



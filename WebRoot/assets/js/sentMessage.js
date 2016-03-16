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

//进入已发送页面--获取已发送信息列表
    msg =new Array();
      $.ajax({
          type: 'POST',
          url:GETSENTMESSAGELIST,
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
                     var sentMsgList=data.sentMessageList;
                      for(var i=0;i<sentMsgList.length;i++){
                          var messageID=sentMsgList[i].messageID;
                          var receiverName=sentMsgList[i].receiverName;
                          var sentTime=sentMsgList[i].sentTime;
                          var  content=sentMsgList[i].content;
                          var   title=sentMsgList[i].title;
                          var  emergencyLevel=sentMsgList[i].emergencyLevel;
                          msg[i]=[messageID,receiverName,sentTime,content,title,emergencyLevel];
                          }
                      
                      for(var j=0;j<msg.length;j++){
                          var oTemplate=$(".template-message").children();
                          var nTemplate=oTemplate.clone();

                    	  for(var m=0;m<msg[j].length;m++){
                    		  console.log(msg[j][m]);
                    		  switch (m){
                              case 0:
                                  nTemplate.find("button.my-delete-btn").attr("value",msg[j][m]);
                                  break;
                              case 1:
                                  nTemplate.find("li.receiver").html("接收者："+msg[j][m]);
                                  $("button.my-again-btn").attr("value",msg[j][m]);
                                  break;
                              case 2:
                                  nTemplate.find("li.time").html("发送时间："+msg[j][m]);
                                  break;
                              case 3:
                                  nTemplate.find("li.content").html("内容："+msg[j][m]);
                                  break;
                              case 4:
                                  nTemplate.find("span.my-title").html(msg[j][m]);
                                  break;
                              case 5:
                                  if(msg[j][m]==1){ nTemplate.find("dt").append("<i class=\"am-icon-warning\" style=\"color: #bc4441\" ></i>")}
                                  break;
                              default :
                                  break;
                          }
                              $("section").prepend(nTemplate);
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
//点击再来一条信息--跳到新建信息页面
    $(document).on('click', '.my-again-btn', againMsg);
     function againMsg(){
          var receiverName=$(this).val();
          Cookies.set('receiver',receiverName,{path:'/'});
          window.location.href="createMessage.html";
      };


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

    $(document).on('click', '.my-accordion-title', slideDown);
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



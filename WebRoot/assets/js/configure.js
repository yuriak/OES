/**
 * configure for myAPP
 */

//role
STUDENT = 0;
TEACHER = 1;

//resultTypeID
VALUE_RESULT = 1;
SINGLE_OPTION_RESULT = 2;
MULTIPLE_OPTION_RESULT = 3;
TEXT_RESULT = 4;

//status
BEFORE = -1;
DOING = 0;
AFTER = 1;

//receiverType
GROUP_RECEIVER = 1;
PERSON_RECEIVER = 0;

//gender
MALE = 1;
FEMALE = 0;

//emergencyLevel
EMERGENCY = 1;
NORMAL = 0;

//deleter
SENDER = 1;
RECEIVER = 0;

//serverStatus
SERVER_ERROR = -1;
SERVER_NORMAL = 0;

/**
 * ajax调用的student API
 */

//course
GETSTUDENTCOURSELIST = "API/student/getStudentCourseList";
GETELECTIVECOURSEINFO = "API/student/getElectiveCourseInfo";
DELETESTUDENTCOURSE = "API/student/deleteStudentCourse";
GETALLAVAILABLECOURSELIST = "API/student/getAllAvailableCourseList";
GETCOURSEINFO = "API/student/getCourseInfo";
QUERYCOURSE = "API/student/queryCourse";
ELECTCOURSE = "API/student/electCourse";
//class
GETCLASSLISTBYCOURSE = "API/student/getClassListByCourse";
GETNOTICEBYCLASSID = "API/student/getNoticeByLessonID";
GETLEAVEINFO = "API/student/getLeaveInfo";
LEAVE = "API/student/leave";

GETLESSONLISTBYCLASS = "API/student/getLessonListByCourse";
//evaluation
GETEVALUATIONLISTBYCLASS = "API/student/getEvaluationListByLesson";
GETGROUPINFO = "API/student/getGroupInfo";
ADDGROUP = "API/student/addGroup";
SETMEASRECEIVER = "API/student/setMeAsReceiver";
GETEVALUATIONSTATUS = "API/student/getEvaluationStatus";
GETEVALUATIONFIELDLIST = "API/student/getEvaluationFieldList";
STUDENTGETRECEIVERLIST = "API/student/studentGetReceiverList";
STUDENTSETRESULT = "API/student/studentSetResult";
STUDENTGETRESULT = "API/student/studentGetResult";
/**
 * ajax调用的teacher API
 */
//teacher:course
GETTEACHERCOURSELIST = "API/teacher/getTeacherCourseList";
GETTEACHERCOURSEINFO = "API/teacher/getTeacherCourseInfo";
GETAPPLYINGSTUDENTLIST = "API/teacher/getApplyingStudentList";
APPROVESTUDENT = "API/teacher/approveStudent";
REJECTSTUDENT = "API/teacher/rejectStudent";
DELETECOURSE = "API/teacher/deleteCourse";
ADDCOURSE = "API/teacher/addCourse";
GETCOURSEGRADE = "API/teacher/getCourseGrade";
UPDATECOURSEGRADE = "API/teacher/updateCourseGrade";

//teacher:class
UPDATENOTICE = "API/teacher/updateNotice";
DELETELESSON = "API/teacher/deleteLesson";
ADDLESSON = "API/teacher/addLesson";
GETAPPLYINGLEAVESTUDENTLIST = 'API/teacher/getApplyingLeaveStudentList';
APPROVELEAVE = 'API/teacher/approveLeave';
REJECTLEAVE = 'API/teacher/rejectLeave';
GETLESSONLISTBYCOURSE = 'API/teacher/getLessonListByCourse';
//teacher:evaluation
GETGROUPTEMPLATEINFO = "API/teacher/getGroupTemplateInfo";
ADDGROUPTEMPLATE = "API/teacher/addGroupTemplate";
GETEVALUATIONTEMPLATEINFO = "API/teacher/getEvaluationTemplateInfo";
ADDEVALUATION = "API/teacher/addEvaluation";
DELETEEVALUATION = "API/teacher/deleteEvaluation";
SETEVALUATIONSTATUS = "API/teacher/setEvaluationStatus";
GETGROUPMEMBERINFO = "API/teacher/getGroupMemberInfo";
//teacher:evaluationField
TEACHERGETRECEIVERLIST = "API/teacher/teacherGetReceiverList";
TEACHERGETRESULT = "API/teacher/teacherGetResult";
GETEVALUATIONGRADE = "API/teacher/getEvaluationGrade";
UPDATEEVALUTIONGRADE = "API/teacher/updateEvalutionGrade";
/**
 * ajax调用的user API
 */
//login/register
GETMYINFOBYTOKEN = "API/user/getMyInfoByToken";
LOGIN = "API/user/login";
CHECKUNIQUEUSER = "API/user/checkUniqueUser";
REGISTER = "API/user/register";
GETUSERQUESTIONBYUSERNAME = "API/user/getUserQuestionByUserName";
VERIFYANSWER = "API/user/verifyAnswer";
RESETUSERPASSWORD = "API/user/resetUserPassword";
GETALLCOLLEGE = "API/user/getAllCollege";
//message
GETRECEIVEDMESSAGELIST = "API/user/getReceivedMessageList";
SETMESSAGEOPENSTATUS = "API/user/setMessageOpenStatus";
DELETEMESSAGE = "API/user/deleteMessage";
GETSENTMESSAGELIST = "API/user/getSentMessageList";
SENDMESSAGE = "API/user/sendMessage";
//person
UPDATEUSERINFO = "API/user/updateUserInfo";
VERIFYPASSWORD = "API/user/verifyPassword";
UPDATEPASSWORD = "API/user/updatePassword";
GETUSERINFO = "API/user/getUserInfo"

/**
 *
 * public component
 *
 */

function showServiceError() {
    $('#my-alert-false .am-modal-bd').text("服务器异常!");
    $('#my-alert-false').modal();
}
function showErrMsg(errMsg) {
    $('#my-alert-false .am-modal-bd').text(errMsg);
    $('#my-alert-false').modal();
}
function showSuccessMsg(msg) {
    $('#my-alert-success .am-modal-bd').text(msg);
    $('#my-alert-success').modal();

}


/**
 *
 * 添加个人中心的realName
 *
 */

var realName = Cookies.get('realName');
if (realName != null) {
    $('#realName').text(realName);
}
/**
 *
 * 返回
 *
 */
$('#back').click(function () {
    window.history.go(-1);
});


/**
 *
 * 退出登录
 *
 */

$('button.my-exit').click(function () {
    var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
    for (var i = 0; i < keys.length; i++) {
        Cookies.remove(keys[i], {path: '/'});
    }
    window.location.href = 'login.html';
});

/**
 *
 * 个人中心的按钮链接
 *
 */

$(document).on('click', '.my-person-menu-top', function () {
});
$(document).on('click', '.my-person-menu', function () {
    var $this = $(this);
    var text = $this.children().text();
    if (text == '消息') {
        window.location.href = 'receiveMessage.html';
    }else if(text=='更改密码'){
    	window.location.href='updatePassword.html'
    }else if (text == '个人设置') {
        var role = Cookies.get('role');
        if (role == TEACHER) {
            window.location.href = 'teacherSetInfo.html';
        } else if (role == STUDENT) {
            window.location.href = 'studentSetInfo.html';
        }
    }else if(text == '首页'){
    	 var role = Cookies.get('role');
         if (role == TEACHER) {
             window.location.href = 'teacherIndex.html';
         } else if (role == STUDENT) {
             window.location.href = 'studentIndex.html';
         }
    }

})
;


$('#to-home').click(function(){
	if(Cookies.get('role')==1){
		window.location.href="teacherIndex.html";
	}else if(Cookies.get('role')==0){
		window.location.href='studentIndex.html';
	}else{
		window.location.href='login.html';
	}
});


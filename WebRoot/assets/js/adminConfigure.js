/**
 * configure for myAPP
 */

//role
STUDENT = 0;
TEACHER = 1;

//resultType
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
 * admin API 
 */
ADMINLOGIN='API/admin/adminLogin';
ADMINSETSEMESTER='API/admin/adminSetSemester';
ADMINSETTEACHERSTATUS='API/admin/adminSetTeacherStatus';
ADMINLOGOUT='API/admin/adminLogout';
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
 * 返回
 *
 */
$('#back').click(function(){
    window.history.go(-1);
});


/**
 *
 * 退出登录
 *
 */


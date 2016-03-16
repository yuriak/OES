package edu.dufe.oes.impl;

import javax.ws.rs.FormParam;

import org.json.JSONArray;

public interface MethodStub {

	//resultType
	public static final int VALUE_RESULT=1;
	public static final int SINGLE_OPTION_RESULT=2;
	public static final int MULTIPLE_OPTION_RESULT=3;
	public static final int TEXT_RESULT=4;
	
	//status
	public static final int BEFORE=-1;
	public static final int DOING=0;
	public static final int AFTER=1;
	
	//receiverType
	public static final int GROUP_RECEIVER=1;
	public static final int PERSON_RECEIVER=0;
	
	//gender
	public static final int MALE=1;
	public static final int FEMALE=0;
	
	//emergencyLevel
	public static final int EMERGENCY=1;
	public static final int NORMAL=1;
	
	//deleter
	public static final int SENDER=1;
	public static final int RECEIVER=0;
	
	
	//serverStatus
	public static final int SERVER_ERROR=-1;
	public static final int SERVER_NORMAL=0;
	
	//approveStatus
	public static final int APPROVED=1;
	public static final int APPROVING=0;
	public static final int REJECTED=-1;
	
	//role
	public static final int STUDENT=0;
	public static final int TEACHER=1;
	
	
	//login
//	public String getMyInfoByToken(@FormParam("token")String token);//{userID,userName,realName,college,gender,email,phone,messageNumber,role,major,currentSemester,semesterList:[{semester}]};code:int;errMsg:String
//	public String login(String userName,String password);//{success,reason,token};code:int;errMsg:String
//	public boolean checkUniqueUser(String userName);//unique:boolean;code:int;errMsg:String
//	public boolean register(String userName,String password,String question,String answer,String realName,int collegeID,int gender,String email,String phone,int role,String major);//success:boolean;code:int;errMsg:String
//	public String getUserQuestionByUserName(String userName);//{question:string,userID:int};code:int;errMsg:String
//	public String verifyAnswer(int userID,String answer);//success:boolean,token:string;code:int;errMsg:String
//	public boolean resetUserPassword(String password,String token);//success:boolean;code:int;errMsg:String
	//course
	public String getStudentCourseList(String token,int currentSemester);//{courseList:[{courseID:int,courseName:String,electiveStatus}]}
	public String getElectiveCourseInfo(int courseID,String token);//{courseID,courseName,courseNumber,courseOrder,semester,collegeID,collegeName,electiveStatus,teacherName,teacherID,courseGrade,courseGradeStatus}
	public boolean deleteStudentCourse(int courseID,String token);//{success}
	public String getAllAvailableCourseList(String token);//{[courseID,courseName,teacherName]}
	public String getCourseInfo(int courseID,String token);//{courseID,courseName,courseNumber,courseOrder,semester,collegeID,collegeName,teacherName,teacherID}
	public String queryCourse(String courseNumber,String courseName,String teacherName,String collegeName,String token);//{courseList:[courseID,courseName,teacherName,teacherID,collegeName,collegeID]}
	public String getAllCollege(String token);//{collegeList:[{collegeID,collegeName}]}
	public boolean electCourse(int courseID,String token);//success
	//class
	public String getClassListByCourse(String token,int courseID);//{classList:[{ startTime, classStatus(-1befor 0now 1done), classID,emergencyLevel}]}
	public String getNoticeByClassID(String ClassID,String Token);//noticeID,noticeContent,EmergencyLevel
	public String getLeaveInfo(String token,String classID);//{leaveID,approveStatus}
	public String leave(String leaveReason,int classID,String token);//success,
	//evaluation
	public String getEvaluationListByClass(int classID,String token);//{evaluationList:[{evaluationID,evaluationTitle,evaluationStatus,isGroup:boolean}]}
	public String getGroupInfo(int evalutaionID,String token);//{allGroupInfo:[{groupID,groupNumber}],yourGroupInfo:{groupID,groupNumber}}
	public String addGroup(String token,int groupID);//success
	public boolean setMeAsReceiver(String token,int evaluationID);//success
	
	//evaluationField\evaluationResult
	public String getEvaluationStatus(int evaluationID,String token);//{evaluationID,evaluationStatus}//进入评价字段页面时调用
	public String getEvaluationFieldList(int evaluationID,String token);//{evaluationFieldList:[{evaluationFieldID,fieldContent,resultTypeID,resultType}],evaluationID}
	public String studentGetReceiverList(int evaluationID,String token);//{receiverType:pserson/group,receiverList:[{studentID/GroupID,groupNumber/studentName,receiverStatus}]}
	public boolean studentSetResult();//incoming:{token,senderid,receiverid,receivertype,resultContent:[{resultTypeID,efid,resultcontent}]};output:{success}
	public boolean studentGetResult(String token,int receiverID);//{resultList:[{evaluationFieldType,evaluationFieldContent,valueResult:{maxValue,minValue,averageValue,evaluatedStudentNumber}},{evaluationFieldType,evaluationFieldContent,singleOptionResult:[{optionKey,optionTitleContent,selectedNumber,selectedPercentage}]},{evaluationFieldType,evaluationFieldContent,multipleOptionResult:[{optionKey,optionTitleContent,selectedNumber}]},{evaluationFieldType,evaluationFieldContent,textResult:[{text}]}]}
	
	/*
	 * teaccher:course
	 */
	public String getTeacherCourseList(String token,int currentSemester);//{courseList:[courseID,courseName,applyingStudentNumber]}
	public String getTeacherCourseInfo(int courseID,String token);////{courseID,courseName,courseNumber,courseOrder,semester,collegeID,collegeName,totalStudentNumber,applyingStudentNumber}
	public String getApplyingStudentList(String token,String courseID);//{applyingStudentList:[{studentID,userName,studentName,collegeName,major}]}
	public String approveStudent(String token,String courseID,String studentList);//input:{approvedStudentList:[{studentID}]}//output:success
	public String rejectStudent(String token,String courseID,String studentList);//input:{rejectedStudentList:[{studentID}]}//output:success
	public String deleteCourse(int courseID,String token);//{success}
	public String addCourse(String courseNumber,String courseOrder,String courseName,String collegeID);//{success,courseID}
	public String getCourseGrade(int courseID,String token);//{courseGradeList:[{electiveID,studentName,courseGrade,collegeName,userName,totalEvaluationNumber,averageEvaluationNumber,myEvaluationNumber,averageEvaluationGrade,courseGradeStatus,leaveNumber}]}
	public String updateCourseGrade(int electiveID,int grade,String token);//{success}
	//teacher:class
	public String updateNotice(int noticeID,String noticeContent,int emergencyLevel,String token);//{success}
	public String deleteClass(int classID,String token);//{success,reason}
	public String addClass(int courseID,String token);//{success,classID,noticeID}
	//teacher:evaluation
	public String getGroupTemplateInfo(int evaluationID,String token);//{templateList:[{templateName,groupList:[{groupTemplateID,groupNumber,memberList:[{studentID,studentName}]}]}]}
	public String addGroupTemplate(int evaluationID,String groupTemplateName,String token);//{succes}
	public String getEvaluationTemplateInfo(String token,String courseID);//{evaluationTemplatList:[{evaluationTemplateName,evaluationFieldList:[{fieldContent,resultType,optionTitleList:[{optionTitleContent,optionValue}]}]}]}
	public String addEvaluation(int courseID,int classID,String token,String evaluationTitle,boolean isGroup,int groupNumber,int groupTemplateID,boolean saveAsEvaluationTemplate,String evaluationTemplateName,String evaluationFieldList);//output:{success,evaluationID}||//input:{evaluationFieldList:[{fieldContent,resultType,optionTitleList:[{optionTitleContent,optionValue}]}]}
	public String deleteEvaluation(int evaluationID,String token);//{success,reason}
	public String setEvaluationStatus(int evaluationID,int evaluationStatus,String token);//{success,reason}
	public String getGroupMemberInfo(int evaluationID,String token);//{isGroupTemplate,groupList:[{groupID,groupNumber,groupMemberList:[{studentID,studentName}]}]}
	//teacher:evaluationField
	public String teacherGetReceiverList(int evaluationID,String token);//{receiverType:pserson/group,receiverList:[{id,number/name}]}
	public String teacherGetResult(int evaluationID,int receiverID,String token);//return:{evaluatingStudentList:[{studentName}],resultList:[{evaluationFieldType,evaluationFieldContent,valueResult:{maxValue,minValue,averageValue,evaluatedStudentNumber}},{evaluationFieldType,evaluationFieldContent,singleOptionResult:[{optionKey,optionTitleContent,selectedNumber,selectedPercentage}]},{evaluationFieldType,evaluationFieldContent,multipleOptionResult:[{optionKey,optionTitleContent,selectedNumber}]},{evaluationFieldType,evaluationFieldContent,textResult:[{text}]}]}
	public String getEvaluationGrade(int receiverID,int receiverType,String token);//{evaluationGradeList:[{receiverID,receiverType,receiverName,evaluationGrade}]}
	public String updateEvalutionGrade(String token,String evaluationGradeList);//input:{evaluationGradeList:[receiverID,receiverType,evaluationGrade]}

	
	
	/*
	 * message
	 */
//	public String getReceivedMessageList(String token);//{receivedMessageList:[{senderID,senderName,title,content,sendTime,emergencyLevel,openStatus,openTime}]}
//	public String setMessageOpenStatus(int messageID,String token,int deleter);//{success}
//	public String deleteMessage(int messageID,String token);//{success}
//	public String getSentMessageList(String token);//{sentMessageList:[{receiverID,receiverName,title,content,sendTime,emergencyLevel,openStatus,openTime}]}
//	public String sendMessage(String receiverName,String title,String content,int emergencyLevel,String token);//{success}
	
	/*person
	 */
	public String updateUserInfo(String token,String realName,String collegeID, String email,int gender,String phone,String major);//{success}
	public String verifyPassword(String password,String token);//{success}
	public String updatePassword(String token,String password);//{success}
	/*
	 * Administrator
	 */
	public boolean administratorLogin(String userName,String password);
	public void setTeacherApproveStatus(int teacherID);
	public void setCurrentSemester(int currentSemester);
	
}

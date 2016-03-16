package edu.dufe.oes.api;

public interface IStudentAPI {

	//course
	public String getStudentCourseList(String token,int currentSemester);//{courseList:[{courseID:int,courseName:String,electiveStatus}]}
	public String getElectiveCourseInfo(int courseID,String token);//{courseID,courseName,courseNumber,courseOrder,semester,collegeID,collegeName,electiveStatus,teacherName,teacherID,courseGrade,courseGradeStatus}
	public String deleteStudentCourse(int courseID,String token);//{success}
	public String getAllAvailableCourseList(String token);//{[courseID,courseName,teacherName]}
	public String getCourseInfo(int courseID,String token);//{courseID,courseName,courseNumber,courseOrder,semester,collegeID,collegeName,teacherName,teacherID}
	public String queryCourse(String courseName,String token);//{courseList:[courseID,courseName,teacherName,teacherID,collegeName,collegeID]}
	public String getAllCollege(String token);//{collegeList:[{collegeID,collegeName}]}
	public String electCourse(int courseID,String token);//success
	//class
	public String getLessonListByCourse(String token,int courseID);//{lessonList:[{ startTime, lessonStatus(-1befor 0now 1done), lessonID,emergencyLevel}]}
	public String getNoticeByLessonID(int lessonID,String token);//noticeID,noticeContent,EmergencyLevel
	public String getLeaveInfo(String token,int lessonID);//{leaveID,approveStatus}
	public String leave(String leaveReason,int lessonID,String token);//success,
	//evaluation
	public String getEvaluationListByLesson(int lessonID,String token);//{evaluationList:[{evaluationID,evaluationTitle,evaluationStatus,isGroup:boolean}]}
	public String getGroupInfo(int evaluationID,String token);//{allGroupInfo:[{groupID,groupNumber}],yourGroupInfo:{groupID,groupNumber}}
	public String addGroup(String token,int groupID);//success
	public String setMeAsReceiver(String token,int evaluationID);//success
	public String getEvaluationStatus(int evaluationID,String token);//{evaluationID,evaluationStatus}//进入评价字段页面时调用
	public String getEvaluationFieldList(int evaluationID,String token);//{evaluationFieldList:[{evaluationFieldID,fieldContent,resultTypeID,resultType}],evaluationID}
	public String studentGetReceiverList(int evaluationID,String token);//{receiverType:pserson/group,receiverList:[{studentID/GroupID,groupNumber/studentName,receiverStatus}]}
	public String studentSetResult(String token,String resultContent,int senderID,int receiverID);//incoming:{resultList:[{resultTypeID,efid,resultcontent}]};output:{success}
	public String studentGetResult(String token,int receiverID);//{resultList:[{evaluationResultType,evaluationFieldContent,valueResult:{maxValue,minValue,averageValue,evaluatedStudentNumber}},{evaluationResultType,evaluationFieldContent,singleOptionResult:[{optionKey,optionTitleContent,selectedNumber,selectedPercentage}]},{evaluationResultType,evaluationFieldContent,multipleOptionResult:[{optionKey,optionTitleContent,selectedNumber}]},{evaluationResultType,evaluationFieldContent,textResult:[{text}]}]}
}

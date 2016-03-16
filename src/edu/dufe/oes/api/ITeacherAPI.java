package edu.dufe.oes.api;

public interface ITeacherAPI {
	/*
	 * teaccher:course
	 */
	public String getTeacherCourseList(String token,int currentSemester);//{courseList:[courseID,courseName,applyingStudentNumber]}
	public String getTeacherCourseInfo(int courseID,String token);////{courseID,courseName,courseNumber,courseOrder,semester,collegeID,collegeName,totalStudentNumber,applyingStudentNumber}
	public String getApplyingStudentList(String token,int courseID);//{applyingStudentList:[{studentID,userName,studentName,collegeName,major}]}
	public String approveStudent(String token,int courseID,String studentList);//input:{approvedStudentList:[{studentID}]}//output:success
	public String rejectStudent(String token,int courseID,String studentList);//input:{rejectedStudentList:[{studentID}]}//output:success
	public String deleteCourse(int courseID,String token);//{success}
	public String addCourse(String courseNumber,String courseOrder,String courseName,int collegeID,String token);//{success,courseID}
	//*
	public String getCourseGrade(int courseID,String token);//{courseGradeList:[{electiveID,studentName,courseGrade,collegeName,userName,totalEvaluationNumber,averageEvaluationNumber,myEvaluationNumber,averageEvaluationGrade,courseGradeStatus,leaveNumber}]}
	
	public String updateCourseGrade(int electiveID,int grade,String token);//{success}
	public String getAllCollege(String token);//{collegeList:[{collegeID,collegeName}]}
	//teacher:lesson
	public String getLessonListByCourse(String token,int courseID);//{lessonList:[{ startTime, lessonStatus(-1befor 0now 1done), lessonID,emergencyLevel}]}
	public String updateNotice(int noticeID,String noticeContent,int emergencyLevel,String token);//{success}
	public String deleteLesson(int lessonID,String token);//{success,reason}
	public String addLesson(int courseID,String token,String noticeContent,int emergencyLevel);//{success,lessonID,noticeID}
	public String getApplyingLeaveStudentList(int lessonID,String token);//{success,applyingLeaveStudentList:[]}
	public String approveLeave(String token, String approveList);//{success}
	public String rejectLeave(String token,String rejectList);//{success}
	public String getNoticeByLessonID(String token,int lessonID);
	//teacher:evaluation
	public String getEvaluationListByLesson(int lessonID,String token);//{evaluationList:[{evaluationID,evaluationTitle,evaluationStatus,isGroup:boolean}]}
	public String getGroupTemplateInfo(int evaluationID,String token);//{templateList:[{templateName,groupList:[{groupTemplateID,groupNumber,memberList:[{studentID,studentName}]}]}]}
	public String addGroupTemplate(int evaluationID,String groupTemplateName,String token);//{succes}
	public String getEvaluationTemplateInfo(String token,int courseID);//{evaluationTemplatList:[{evaluationTemplateName,evaluationFieldList:[{fieldContent,resultType,optionTitleList:[{optionTitleContent,optionKey}]}]}]}
	public String 	addEvaluation(int courseID,int lessonID,String token,String evaluationTitle,boolean isGroup,int groupNumber,int groupTemplateID,boolean saveAsEvaluationTemplate,String evaluationTemplateName,String evaluationFieldList);//output:{success,evaluationID}||//input:{evaluationFieldList:[{fieldContent,resultType,optionTitleList:[{optionTitleContent,optionValue}]}]}
	public String deleteEvaluation(int evaluationID,String token);//{success,reason}
	public String setEvaluationStatus(int evaluationID,int evaluationStatus,String token);//{success,reason}
	public String getGroupMemberInfo(int evaluationID,String token);//{isGroupTemplate,groupList:[{groupID,groupNumber,groupMemberList:[{studentID,studentName}]}]}
	//teacher:evaluationField
	public String teacherGetReceiverList(int evaluationID,String token);//{receiverType:pserson/group,receiverList:[{id,number/name}]}
	public String teacherGetResult(int evaluationID,int receiverID,String token);//return:{evaluatingStudentList:[{studentName}],resultList:[{evaluationFieldType,evaluationFieldContent,valueResult:{maxValue,minValue,averageValue,evaluatedStudentNumber}},{evaluationFieldType,evaluationFieldContent,singleOptionResult:[{optionKey,optionTitleContent,selectedNumber,selectedPercentage}]},{evaluationFieldType,evaluationFieldContent,multipleOptionResult:[{optionKey,optionTitleContent,selectedNumber}]},{evaluationFieldType,evaluationFieldContent,textResult:[{text}]}]}
	public String getEvaluationGrade(int receiverID,String token);//{evaluationGradeList:[{receiverID,receiverType,receiverName,evaluationGrade}]}
	public String updateEvalutionGrade(String token,int receiverID, int grade);//input:{evaluationGradeList:[{receiverID,evaluationGrade}]}

}

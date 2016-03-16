package edu.dufe.oes.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import edu.dufe.oes.bean.CourseDAO;
import edu.dufe.oes.bean.User;
import edu.dufe.oes.config.CommonValues;
import edu.dufe.oes.service.CollegeService;
import edu.dufe.oes.service.CourseService;
import edu.dufe.oes.service.EvaluationResultService;
import edu.dufe.oes.service.EvaluationService;
import edu.dufe.oes.service.GroupService;
import edu.dufe.oes.service.LessonService;
import edu.dufe.oes.service.UserInfoService;
import edu.dufe.oes.util.MyStringUtil;

@Path("teacher")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TeacherAPI extends CommonAPI implements ITeacherAPI {

	CourseService courseService=new CourseService();
	UserInfoService userInfoService=new UserInfoService();
	CollegeService collegeService=new CollegeService();
	LessonService lessonService=new LessonService(); 
	GroupService groupService=new GroupService();
	EvaluationService evaluationService=new EvaluationService();
	EvaluationResultService evaluationResultService=new EvaluationResultService();
	@Override
	@Path("getTeacherCourseList")
	@POST
	public String getTeacherCourseList(@FormParam("token")String token,@FormParam("currentSemester") int currentSemester) {
		try {
			return addServerNormalStatus(courseService.getTeacherCourseList(MyStringUtil.trimAndEscapeSql(token), currentSemester)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("getTeacherCourseInfo")
	@POST
	public String getTeacherCourseInfo(@FormParam("courseID")int courseID, @FormParam("token")String token) {
		try {
			return addServerNormalStatus(courseService.getTeacherCourseInfo(courseID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("getApplyingStudentList")
	@POST
	public String getApplyingStudentList(@FormParam("token")String token,@FormParam("courseID") int courseID) {
		try {
			return addServerNormalStatus(courseService.getApplyingStudentList(MyStringUtil.trimAndEscapeSql(token), courseID)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}
	@Path("approveStudent")
	@POST
	@Override
	public String approveStudent(@FormParam("token")String token, @FormParam("courseID")int courseID,
			@FormParam("approvedStudentList")String approvedStudentList) {
		try {
			return addServerNormalStatus(courseService.approveStudent(MyStringUtil.trimAndEscapeSql(token), courseID, approvedStudentList)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}
	@Path("rejectStudent")
	@POST
	@Override
	public String rejectStudent(@FormParam("token")String token,@FormParam("courseID") int courseID,
			@FormParam("rejectedStudentList")String rejectedStudentList) {
		try {
			return addServerNormalStatus(courseService.rejectStudent(MyStringUtil.trimAndEscapeSql(token), courseID, rejectedStudentList)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}
	@Path("deleteCourse")
	@POST
	@Override
	public String deleteCourse(@FormParam("courseID")int courseID,@FormParam("token") String token) {
		try {
			return addServerNormalStatus(courseService.deleteCourse(courseID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}
	@Path("addCourse")
	@POST
	@Override
	public String addCourse(@FormParam("courseNumber")String courseNumber, @FormParam("courseOrder")String courseOrder,
			@FormParam("courseName")String courseName, @FormParam("collegeID")int collegeID,@FormParam("token")String token) {
		try {
			return addServerNormalStatus(courseService.addCourse(MyStringUtil.trimAndEscapeSql(courseNumber), MyStringUtil.trimAndEscapeSql(courseOrder), MyStringUtil.trimAndEscapeSql(courseName), collegeID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}
	@Path("getCourseGrade")
	@POST
	@Override
	public String getCourseGrade(@FormParam("courseID")int courseID,@FormParam("token") String token) {
		try {
			return addServerNormalStatus(courseService.getCourseGrade(courseID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}
	@Path("updateCourseGrade")
	@POST
	@Override
	public String updateCourseGrade(@FormParam("electiveID")int electiveID,@FormParam("grade") int grade,@FormParam("token") String token) {
		try {
			return addServerNormalStatus(courseService.updateCourseGrade(electiveID, grade, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}
	@Path("getAllCollege")
	@POST
	@Override
	public String getAllCollege(@FormParam("token")String token) {
		try {
			User user = userInfoService.getUserByToken(token);
			if (user==null) {
				return addServerNormalStatus(userInfoService.addSuccessStatus(new JSONObject(), false, CommonValues.ILLEGALUSER_ERR)).toString();
			}
			return addServerNormalStatus(collegeService.getAllColleges()).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}
	@Path("getLessonListByCourse")
	@POST
	@Override
	public String getLessonListByCourse(@FormParam("token")String token,@FormParam("courseID") int courseID) {
		try {
			return addServerNormalStatus(lessonService.getLessonListByCourse(MyStringUtil.trimAndEscapeSql(token), courseID)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}
	@Path("updateNotice")
	@POST
	@Override
	public String updateNotice(@FormParam("noticeID")int noticeID,@FormParam("noticeContent") String noticeContent,
			@FormParam("emergencyLevel")int emergencyLevel, @FormParam("token")String token) {
		try {
			return addServerNormalStatus(lessonService.updateNotice(noticeID, MyStringUtil.trimAndEscapeSql(noticeContent), emergencyLevel, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}
	@Path("deleteLesson")
	@POST
	@Override
	public String deleteLesson(@FormParam("lessonID")int lessonID,@FormParam("token") String token) {
		try {
			return addServerNormalStatus(lessonService.deleteLesson(lessonID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}
	@Path("addLesson")
	@POST
	@Override
	public String addLesson(@FormParam("courseID")int courseID,@FormParam("token") String token,@FormParam("noticeContent")String noticeContent,@FormParam("emergencyLevel")int emergencyLevel) {
		try {
			return addServerNormalStatus(lessonService.addLesson(courseID, MyStringUtil.trimAndEscapeSql(token),MyStringUtil.trimAndEscapeSql(noticeContent),emergencyLevel)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}
	@Path("getApplyingLeaveStudentList")
	@POST
	@Override
	public String getApplyingLeaveStudentList(@FormParam("lessonID")int lessonID,@FormParam("token") String token) {
		try {
			return addServerNormalStatus(lessonService.getApplyingLeaveStudentList(lessonID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Path("approveLeave")
	@POST
	@Override
	public String approveLeave(@FormParam("token") String token,@FormParam("approveList")String approveList) {
		try {
			return addServerNormalStatus(lessonService.approveLeave(MyStringUtil.trimAndEscapeSql(token),approveList)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Path("rejectLeave")
	@POST
	@Override
	public String rejectLeave(@FormParam("token") String token,@FormParam("rejectList")String rejectList ) {
		try {
			return addServerNormalStatus(lessonService.rejectLeave(MyStringUtil.trimAndEscapeSql(token),rejectList)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("getNoticeByLessonID")
	@POST
	public String getNoticeByLessonID(@FormParam("token")String token,@FormParam("lessonID") int lessonID) {
		try {
			return addServerNormalStatus(lessonService.getNoticeByLessonID(lessonID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}
	
	@Path("getEvaluationListByLesson")
	@POST
	@Override
	public String getEvaluationListByLesson(@FormParam("lessonID")int lessonID, @FormParam("token")String token) {
		try {
			return addServerNormalStatus(evaluationService.getEvaluationListByLesson(lessonID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Path("getGroupTemplateInfo")
	@POST
	@Override
	public String getGroupTemplateInfo(@FormParam("courseID")int groupID,@FormParam("token") String token) {
		try {
			return addServerNormalStatus(groupService.getGroupTemplateInfo(groupID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Path("addGroupTemplate")
	@POST
	@Override
	public String addGroupTemplate(@FormParam("evaluationID")int evaluationID,@FormParam("groupTemplateName") String groupTemplateName,
			@FormParam("token")String token) {
		try {
			return addServerNormalStatus(groupService.addGroupTemplate(evaluationID, MyStringUtil.trimAndEscapeSql(groupTemplateName), MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Path("getEvaluationTemplateInfo")
	@POST
	@Override
	public String getEvaluationTemplateInfo(@FormParam("token")String token,@FormParam("courseID") int courseID) {
		try {
			return addServerNormalStatus(evaluationService.getEvaluationTemplateInfo(MyStringUtil.trimAndEscapeSql(token), courseID)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	/**
	 * 
	 * @param courseID
	 * @param lessonID
	 * @param token
	 * @param evaluationTitle
	 * @param isGroup
	 * @param groupNumber
	 * @param groupTemplateID
	 * @param saveAsEvaluationTemplate
	 * @param evaluationTemplateName
	 * @param evaluationFieldList
	 * @return
	 * @throws Exception
	 */
	@Path("addEvaluation")
	@POST
	@Override
	public String addEvaluation(@FormParam("courseID")int courseID,@FormParam("lessonID") int lessonID,@FormParam("token") String token,
			@FormParam("evaluationTitle")String evaluationTitle,@FormParam("isGroup") boolean isGroup,@FormParam("groupNumber") int groupNumber,
			@FormParam("groupTemplateID")int groupTemplateID,@FormParam("saveAsEvaluationTemplate") boolean saveAsEvaluationTemplate,
			@FormParam("evaluationTemplateName")String evaluationTemplateName,@FormParam("evaluationFieldList") String evaluationFieldList) {
		try {
			return addServerNormalStatus(evaluationService.addEvaluation(courseID, lessonID, MyStringUtil.trimAndEscapeSql(token), MyStringUtil.trimAndEscapeSql(evaluationTitle), isGroup, groupNumber, groupTemplateID, saveAsEvaluationTemplate, MyStringUtil.trimAndEscapeSql(evaluationTemplateName), evaluationFieldList)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}


	@Path("deleteEvaluation")
	@POST
	@Override
	public String deleteEvaluation(@FormParam("evaluationID")int evaluationID, @FormParam("token")String token) {
		try {
			return addServerNormalStatus(evaluationService.deleteEvaluation(evaluationID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Path("setEvaluationStatus")
	@POST
	@Override
	public String setEvaluationStatus(@FormParam("evaluationID")int evaluationID,@FormParam("evaluationStatus") int evaluationStatus,
			@FormParam("token")String token) {
		try {
			return addServerNormalStatus(evaluationService.setEvaluationStatus(evaluationID, evaluationStatus, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Path("getGroupMemberInfo")
	@POST
	@Override
	public String getGroupMemberInfo(@FormParam("evaluationID")int evaluationID,@FormParam("token") String token) {
		try {
			return addServerNormalStatus(groupService.getGroupMemberInfo(evaluationID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Path("teacherGetReceiverList")
	@POST
	@Override
	public String teacherGetReceiverList(@FormParam("evaluationID")int evaluationID, @FormParam("token")String token) {
		try {
			return addServerNormalStatus(evaluationResultService.teacherGetReceiverList(evaluationID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Path("teacherGetResult")
	@POST
	@Override
	public String teacherGetResult(@FormParam("evaluationID")int evaluationID,@FormParam("receiverID") int receiverID,
			@FormParam("token")	String token) {
		try {
			return addServerNormalStatus(evaluationResultService.teacherGetResult(evaluationID, receiverID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Path("getEvaluationGrade")
	@POST
	@Override
	public String getEvaluationGrade(@FormParam("receiverID")int receiverID,@FormParam("token") String token) {
		try {
			return addServerNormalStatus(evaluationService.getEvaluationGrade(receiverID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Path("updateEvalutionGrade")
	@POST
	@Override
	public String updateEvalutionGrade(@FormParam("token")String token,@FormParam("receiverID") int receiverID,@FormParam("evaluationGrade") int evaluationGrade) {
		try {
			return addServerNormalStatus(evaluationService.updateEvalutionGrade(MyStringUtil.trimAndEscapeSql(token), receiverID,evaluationGrade)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}






	



}

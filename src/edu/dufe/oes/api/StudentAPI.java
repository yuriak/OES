package edu.dufe.oes.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import sun.net.www.content.image.gif;

import com.sun.org.apache.regexp.internal.recompile;

import edu.dufe.oes.bean.EvaluationResult;
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


@Path("student")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StudentAPI extends CommonAPI implements IStudentAPI {

	CourseService courseService=new CourseService();
	UserInfoService userInfoService=new UserInfoService();
	CollegeService collegeService=new CollegeService();
	LessonService lessonService=new LessonService(); 
	EvaluationService evaluationService=new EvaluationService();
	GroupService groupService=new GroupService();
	EvaluationResultService evaluationResultService =new EvaluationResultService();
	@Override
	@Path("getStudentCourseList")
	@POST
	public String getStudentCourseList(@FormParam("token")String token,@FormParam("currentSemester") int currentSemester) {
		try {
			return addServerNormalStatus(courseService.getStudentCourseList(MyStringUtil.trimAndEscapeSql(token), currentSemester)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			sendErrorMessage(e.getMessage());
		}
		return null;
	}

	@Override
	@Path("getElectiveCourseInfo")
	@POST
	public String getElectiveCourseInfo(@FormParam("courseID")int courseID, @FormParam("token")String token) {
		try {
			return addServerNormalStatus(courseService.getElectiveCourseInfo(courseID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("deleteStudentCourse")
	@POST
	public String deleteStudentCourse(@FormParam("courseID")int courseID, @FormParam("token")String token) {
		try {
			return addServerNormalStatus(courseService.deleteStudentCourse(courseID,MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("getAllAvailableCourseList")
	@POST
	public String getAllAvailableCourseList(@FormParam("token")String token) {
		try {
			return addServerNormalStatus(courseService.getAllAvailableCourseList(MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("getCourseInfo")
	@POST
	public String getCourseInfo(@FormParam("courseID")int courseID, @FormParam("token")String token) {
		try {
			return addServerNormalStatus(courseService.getCourseInfo(courseID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("queryCourse")
	@POST
	public String queryCourse(@FormParam("courseName")String courseName,@FormParam("token") String token) {
		
		try {
			return addServerNormalStatus(courseService.queryCourse(MyStringUtil.trimAndEscapeSql(courseName), MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("getAllCollege")
	@POST
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

	@Override
	@Path("electCourse")
	@POST
	public String electCourse(@FormParam("courseID")int courseID,@FormParam("token") String token) {
		try {
			return addServerNormalStatus(courseService.electCourse(courseID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("getLessonListByCourse")
	@POST
	public String getLessonListByCourse(@FormParam("token")String token, @FormParam("courseID")int courseID) {
		try {
			return addServerNormalStatus(lessonService.getLessonListByCourse(MyStringUtil.trimAndEscapeSql(token), courseID)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("getNoticeByLessonID")
	@POST
	public String getNoticeByLessonID(@FormParam("lessonID")int lessonID,@FormParam("token") String token) {
		try {
			return addServerNormalStatus(lessonService.getNoticeByLessonID(lessonID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("getLeaveInfo")
	@POST
	public String getLeaveInfo(@FormParam("token")String token,@FormParam("lessonID") int lessonID) {
		try {
			return addServerNormalStatus(lessonService.getLeaveInfo(MyStringUtil.trimAndEscapeSql(token),lessonID)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("leave")
	@POST
	public String leave(@FormParam("leaveReason")String leaveReason,@FormParam("lessonID") int lessonID,@FormParam("token") String token) {
		try {
			return addServerNormalStatus(lessonService.leave(MyStringUtil.trimAndEscapeSql(leaveReason), lessonID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("getEvaluationListByLesson")
	@POST
	public String getEvaluationListByLesson(@FormParam("lessonID")int lessonID,@FormParam("token") String token) {
		// TODO Auto-generated method stub
		try {
			return addServerNormalStatus(evaluationService.getEvaluationListByLesson(lessonID,
											MyStringUtil.trimAndEscapeSql(token))).toString();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
		
	}

	@Override
	@Path("getGroupInfo")
	@POST
	public String getGroupInfo(@FormParam("evaluationID")int evaluationID,@FormParam("token") String token) {
		// TODO Auto-generated method stub
		try {
			return addServerNormalStatus(groupService.getGroupInfo(evaluationID,
									MyStringUtil.trimAndEscapeSql(token))).toString();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("addGroup")
	@POST
	public String addGroup(@FormParam("token")String token,@FormParam("groupID") int groupID) {
		// TODO Auto-generated method stub
		
		try {
			return addServerNormalStatus(groupService.addGroup(
							MyStringUtil.trimAndEscapeSql(token), groupID)).toString();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	/**
	 * 
	 * @param token
	 * @param evaluationID
	 * @return
	 * @throws Exception
	 */
	@Override
	@Path("setMeAsReceiver")
	@POST
	public String setMeAsReceiver(@FormParam("token")String token,@FormParam("evaluationID") int evaluationID) {
		// TODO Auto-generated method stub
		try {
			return addServerNormalStatus(evaluationService.setMeAsReceiver(
						MyStringUtil.trimAndEscapeSql(token), evaluationID)).toString();
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	
	@Override
	@Path("getEvaluationStatus")
	@POST
	public String getEvaluationStatus(@FormParam("evaluationID")int evaluationID,@FormParam("token")String token) {
		// TODO Auto-generated method stub
		try {
			return addServerNormalStatus(evaluationService.getEvaluationStatus(evaluationID,
					   MyStringUtil.trimAndEscapeSql(token))).toString();
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	
	@Override
	@Path("getEvaluationFieldList")
	@POST
	public String getEvaluationFieldList(@FormParam("evaluationID")int evaluationID,@FormParam("token") String token) {
		// TODO Auto-generated method stub
		try {
			return addServerNormalStatus(evaluationResultService.getEvaluationFieldList(evaluationID,
							MyStringUtil.trimAndEscapeSql(token))).toString();
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
		
	}

	@Override
	@Path("studentGetReceiverList")
	@POST
	public String studentGetReceiverList(@FormParam("evaluationID")int evaluationID,@FormParam("token") String token) {
		// TODO Auto-generated method stub
		try {
			return addServerNormalStatus(evaluationService.studentGetReceiverList(evaluationID,
					MyStringUtil.trimAndEscapeSql(token))).toString();
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}


	@Override
	@Path("studentGetResult")
	@POST
	public String studentGetResult(@FormParam("token")String token,@FormParam("receiverID") int receiverID) {
		try {
			return addServerNormalStatus(evaluationResultService.studentGetResult(MyStringUtil.trimAndEscapeSql(token), receiverID)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	
	@Override
	@Path("studentSetResult")
	@POST
	public String studentSetResult(@FormParam("token")String token,@FormParam("resultContent") String resultContent,
			@FormParam("senderID")int senderID,@FormParam("receiverID") int receiverID) {
		// TODO Auto-generated method stub
		try {
			return addServerNormalStatus(evaluationResultService.studentSetResult(
					MyStringUtil.trimAndEscapeSql(token), 
					resultContent,
					senderID, receiverID)).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

}

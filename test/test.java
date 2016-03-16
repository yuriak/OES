import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.metamodel.relational.Schema;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import edu.dufe.oes.api.StudentAPI;
import edu.dufe.oes.api.TeacherAPI;
import edu.dufe.oes.api.UserAPI;
import edu.dufe.oes.bean.College;
import edu.dufe.oes.bean.Course;
import edu.dufe.oes.bean.CourseDAO;
import edu.dufe.oes.bean.CourseElectiveDAO;
import edu.dufe.oes.bean.Student;
import edu.dufe.oes.bean.StudentDAO;
import edu.dufe.oes.bean.Teacher;
import edu.dufe.oes.bean.User;
import edu.dufe.oes.bean.UserDAO;
import edu.dufe.oes.service.UserInfoService;


public class test {

	@Test
	public void test() throws Exception {
		
		UserAPI userAPI=new UserAPI();
		StudentAPI studentAPI=new StudentAPI();
		TeacherAPI teacherAPI=new TeacherAPI();
//		System.out.println(userAPI.getAllCollege());
//		System.out.println(userAPI.checkUniqueUser("2012210042"));
//		System.out.println(userAPI.register("10", "sss", "sss", "dsssdd", "sss", 1, 1, "ss", "dsssdd", 0, "ss"));
//		System.out.println(userAPI.getAllCollege());
//		System.out.println(userAPI.login("2012210042", "fff"));
//		System.out.println(userAPI.getUserQuestionByUserName("2012210042"));
//		System.out.println(userAPI.verifyAnswer(7, "ddd"));
//		System.out.println(userAPI.resetUserPassword("fff", "70068b65f1c8cb30a4e2d3d290ac1569"));
//		System.out.println(userAPI.sendMessage("2012210040", "hehe", "dddf", 1, "e844e3be698a7cb539a1a54da4beb34e"));
//		System.out.println(studentAPI.getStudentCourseList("2bf6e171f1ba1f67927b595c0906e861", 201601));
//		System.out.println(studentAPI.getElectiveCourseInfo(1, "2bf6e171f1ba1f67927b595c0906e861"));
//		System.out.println(studentAPI.deleteStudentCourse(1, "2bf6e171f1ba1f67927b595c0906e861"));
//		System.out.println(studentAPI.getAllAvailableCourseList("2bf6e171f1ba1f67927b595c0906e861"));
//		System.out.println(studentAPI.getCourseInfo(1, "2bf6e171f1ba1f67927b595c0906e861"));
//		System.out.println(studentAPI.getAllCollege("2bf6e171f1ba1f67927b595c0906e861"));
//		System.out.println(studentAPI.electCourse(3, "8ac9c20ac6213cef5088a7a1812b7c50"));
//		System.out.println(studentAPI.queryCourse("设计", "2bf6e171f1ba1f67927b595c0906e861"));
//		System.out.println(studentAPI.getLessonListByCourse("2bf6e171f1ba1f67927b595c0906e861", 2));
//		System.out.println(studentAPI.getNoticeByLessonID(1, "2bf6e171f1ba1f67927b595c0906e861"));
//		System.out.println(studentAPI.getLeaveInfo("2bf6e171f1ba1f67927b595c0906e861", 1));
//		System.out.println(studentAPI.leave("dddf", 3, "8ac9c20ac6213cef5088a7a1812b7c50"));
		
		
//		System.out.println(teacherAPI.getTeacherCourseList("e844e3be698a7cb539a1a54da4beb34e", 201601));
//		System.out.println(teacherAPI.getTeacherCourseInfo(1, "e844e3be698a7cb539a1a54da4beb34e"));
//		System.err.println(teacherAPI.getApplyingStudentList("bdf25c024da01dac83ae6c203cbe34b4", 2));
//		JSONObject object=new JSONObject(teacherAPI.getApplyingStudentList("e844e3be698a7cb539a1a54da4beb34e", 1));
//		JSONArray array=object.getJSONArray("applyingStudentList");
//		JSONObject data=new JSONObject();
//		System.out.println(data.put("rejectedStudentList", array));
//		System.out.println(teacherAPI.rejectStudent("e844e3be698a7cb539a1a54da4beb34e", 1, data.toString()));
//		System.out.println(teacherAPI.addCourse("142", "32", "程序设计", 1, "bdf25c024da01dac83ae6c203cbe34b4"));
//		System.out.println(teacherAPI.deleteCourse(3, "e844e3be698a7cb539a1a54da4beb34e"));
//		System.out.println(teacherAPI.updateCourseGrade(2, 98, "bdf25c024da01dac83ae6c203cbe34b4"));
//		System.out.println(teacherAPI.addLesson(2, "bdf25c024da01dac83ae6c203cbe34b4"));
//		System.out.println(teacherAPI.getLessonListByCourse("bdf25c024da01dac83ae6c203cbe34b4", 2));
//		System.out.println(teacherAPI.updateNotice(2, "vvv", 1, "bdf25c024da01dac83ae6c203cbe34b4"));
//		System.out.println(teacherAPI.deleteLesson(2, "bdf25c024da01dac83ae6c203cbe34b4"));
//		System.out.println(teacherAPI.getApplyingLeaveStudentList(3, "bdf25c024da01dac83ae6c203cbe34b4"));
//		System.out.println(teacherAPI.getGroupTemplateInfo(4, "bdf25c024da01dac83ae6c203cbe34b4"));
//		System.out.println(teacherAPI.addGroupTemplate(5, "vvv", "bdf25c024da01dac83ae6c203cbe34b4"));
//		System.out.println(teacherAPI.getEvaluationTemplateInfo("bdf25c024da01dac83ae6c203cbe34b4", 5));
//		JSONObject  jObject=new JSONObject();
//		JSONArray array=new JSONArray();
//		JSONObject fieldObject=new JSONObject();
//		fieldObject.put("fieldContent", "内容质量");
//		fieldObject.put("resultType", 1);
//		
//		JSONObject fObject=new JSONObject();
//		JSONArray oArray=new JSONArray();
//		fObject.put("fieldContent", "单选");
//		fObject.put("resultType", 2);
//		JSONObject object=new JSONObject();
//		object.put("optionKey", "A");
//		object.put("optionTitleContent", "很好");
//		oArray.put(object);
//		fObject.put("optionTitleList", oArray);
//		array.put(fObject);
//		array.put(fieldObject);
//		jObject.put("evaluationFieldList", array);
//		System.out.println(jObject);
//		System.out.println(teacherAPI.addEvaluation(5, 4, "bdf25c024da01dac83ae6c203cbe34b4", "可以的", true, 0, 4, false, "一个模板", jObject.toString()));
//		System.out.println(teacherAPI.deleteEvaluation(7, "bdf25c024da01dac83ae6c203cbe34b4"));
//		System.out.println(teacherAPI.setEvaluationStatus(11, 0, "bdf25c024da01dac83ae6c203cbe34b4"));
//		System.out.println(teacherAPI.approveLeave(3, "bdf25c024da01dac83ae6c203cbe34b4"));
//		System.out.println(teacherAPI.rejectLeave(4, "bdf25c024da01dac83ae6c203cbe34b4"));
//		System.out.println(teacherAPI.getGroupMemberInfo(11, "bdf25c024da01dac83ae6c203cbe34b4"));
		System.out.println(teacherAPI.teacherGetReceiverList(13, "bdf25c024da01dac83ae6c203cbe34b4"));
		
		
		
//		CourseDAO courseDAO=new CourseDAO();
//		Session session = courseDAO.getSession();
//		Query query = session.createQuery("from Course where ");
//		
//		List list = query.list();
//		System.out.println(list.size());\
		
	}
	
}

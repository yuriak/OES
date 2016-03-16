package edu.dufe.oes.api;

import static org.junit.Assert.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class StudentAPITest {

	StudentAPI studentAPI=new StudentAPI();
	@Test
	public void testGetStudentCourseList() {
//		System.out.println(studentAPI.getStudentCourseList(10+"", 201601));
//		System.out.println(studentAPI.deleteStudentCourse(1, "2"));
//		for (int i = 1; i <=10; i++) {
//			System.out.println(studentAPI.getStudentCourseList("2", 201601));
//		}
	}

	@Test
	public void testGetElectiveCourseInfo() {
//		System.out.println(studentAPI.getElectiveCourseInfo(1, 10+""));
//		for (int i = 1; i <=12; i++) {
//			for (int j = 1; j <=8; j++) {
//				System.out.println(studentAPI.getElectiveCourseInfo(j, i+""));
//			}
//		}
	}

	@Test
	public void testDeleteStudentCourse() {
//		System.out.println(studentAPI.deleteStudentCourse(8, 10+""));
//		System.out.println(studentAPI.deleteStudentCourse(8, 12+""));
	}

	@Test
	public void testGetAllAvailableCourseList() {
//		studentAPI.getAllAvailableCourseList("301a299672b7cf1f750a21f4292fd384");
//		for (int i = 1; i <= 14; i++) {
//			System.out.println(studentAPI.getAllAvailableCourseList(i+""));
//		}
	}

	@Test
	public void testGetCourseInfo() {
//		studentAPI.getCourseInfo(1, 10+"");
//		System.out.println(studentAPI.getCourseInfo(1, "1"));
	}

	@Test
	public void testQueryCourse() {
//		System.out.println(studentAPI.queryCourse("1", "1"));
	}

	@Test
	public void testGetAllCollege() {
//		System.out.println(studentAPI.getAllCollege("1"));
	}

	@Test
	public void testElectCourse() {
//		for (int i = 1; i <=14; i++) {
//			for (int j = 1; j <=8; j++) {
//				System.out.println(studentAPI.electCourse(j, i+""));
//			}
//			
//		}
	}

	@Test
	public void testGetLessonListByCourse() {
//		for (int i = 1; i <=14; i++) {
//		for (int j = 1; j <=8; j++) {
//			System.out.println(studentAPI.getLessonListByCourse(i+"", j));
//		}
//		
//	}
	}

	@Test
	public void testGetNoticeByLessonID() {
//		for (int i = 1; i <=36; i++) {
//			for (int j = 1; j <=12; j++) {
//				System.out.println(studentAPI.getNoticeByLessonID(i, j+""));
//			}
//		}
	}

	@Test
	public void testGetLeaveInfo() {
//		for (int i = 1; i < 36; i++) {
//			for (int j =1; j <=12; j++) {
//				System.out.println(studentAPI.getLeaveInfo(j+"", i));
//			}
//		}
	}

	@Test
	public void testLeave() {
//		studentAPI.leave("死了", 37, "c23fe0df7ea004aa6c82bde18d11bf87");
//		for (int i = 34; i <=36; i++) {
//			for (int j = 1; j <=12; j++) {
//				System.out.println(studentAPI.leave(i+"c", i, j+""));
//			}
//		}
	}

	@Test
	public void testGetEvaluationListByLesson() {
//		for (int i = 1; i <=12; i++) {
//			for (int j =1; j <=36; j++) {
//				System.out.println(studentAPI.getEvaluationListByLesson(j, i+""));
//			}
//		}
	}

	@Test
	public void testGetGroupInfo() {
//		System.out.println(studentAPI.getGroupInfo(2, "e30582b0b259a74bbc298101265cac05"));
//		for (int i = 1; i <=99; i++) {
//			for (int j = 1; j <=12; j++) {
//				System.out.println(studentAPI.getGroupInfo(i, j+""));
//			}
//			
//		}
		
	}

	@Test
	public void testAddGroup() {
//		for (int i = 1; i <=97; i+=3) {
//			for (int j = 1; j <=4; j++) {
//				System.out.println(studentAPI.addGroup(j+"", i));
//			}
//			for (int j = 4; j <=8; j++) {
//				System.out.println(studentAPI.addGroup(j+"", i+1));
//			}
//			for (int j = 8; j <=12; j++) {
//				System.out.println(studentAPI.addGroup(j+"", i+2));
//			}
//		}
	}

	@Test
	public void testSetMeAsReceiver() {
//		for (int i = 1; i <=12; i++) {
//			for (int j =1 ; j <=99; j++) {
//				System.out.println(studentAPI.setMeAsReceiver(i+"",j));
//			}
//		}
//		System.out.println(studentAPI.setMeAsReceiver("1", 1));
	}

	@Test
	public void testGetEvaluationStatus() {
//		for (int i = 1; i <=12; i++) {
//			for (int j = 1; j <=99; j++) {
//				System.out.println(studentAPI.getEvaluationStatus(j, i+""));
//			}
//		}
	}

	@Test
	public void testGetEvaluationFieldList() {
//		for (int i = 1; i <=12; i++) {
//			for (int j = 1; j <=99; j++) {
//				System.out.println(studentAPI.getEvaluationFieldList(j,i+"" ));
//			}
//		}
	}

	@Test
	public void testStudentGetReceiverList() {
//		for (int i = 2; i <=2; i++) {
//			for (int j = 1; j <=12; j++) {
//				System.out.println(studentAPI.studentGetReceiverList(i, j+""));
//			}
//		}		
	}

	@Test
	public void testStudentGetResult() {
//		System.out.println(studentAPI.studentGetResult(1+"", 1));
	}

	@Test
	public void testStudentSetResult() throws JSONException {
//

		for (int i = 1; i <=12; i++) {
//			for (int j = 110; j <=120; j++) {
//				JSONObject jsonObject=new JSONObject();
//				JSONArray resultArray=new JSONArray();
//				JSONObject r1=new JSONObject();
//				r1.put("resultTypeID", 1);
//				r1.put("evaluationFieldID", 9);
//				r1.put("resultContent", 100);
//				JSONObject r2=new JSONObject();
//				r2.put("resultTypeID", 2);
//				r2.put("evaluationFieldID", 10);
//				r2.put("resultContent", "");
//				JSONObject r21=new JSONObject();
//				r21.put("resultTypeID", 3);
//				r21.put("evaluationFieldID", 11);
//				r21.put("resultContent", "#1#3");
//				JSONObject r22=new JSONObject();
//				r22.put("resultTypeID", 4);
//				r22.put("evaluationFieldID", 12);
//				r22.put("resultContent", "heh的e");
//				resultArray.put(r1);
//				resultArray.put(r2);
//				resultArray.put(r21);
//				resultArray.put(r22);
//				jsonObject.put("resultContent", resultArray);
//				System.out.println(studentAPI.studentSetResult(i+"", jsonObject.toString(), i, j));
//			}
		}
		
	}

}

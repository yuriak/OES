package edu.dufe.oes.api;

import static org.junit.Assert.*;

import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import edu.dufe.oes.bean.EvaluationReceiver;
import edu.dufe.oes.bean.EvaluationReceiverDAO;

public class TeacherAPITest {

	TeacherAPI teacherAPI=new TeacherAPI();
	@Test
	public void testGetTeacherCourseList() {
//		for (int i = 13; i <=14; i++) {
//			System.out.println(teacherAPI.getTeacherCourseList(i+"", 201601));
//		}
	}

	@Test
	public void testGetTeacherCourseInfo() {
//		for (int i = 1; i <=8; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.getTeacherCourseInfo(i, j+""));				
//			}
//
//		}
	}

	@Test
	public void testGetApplyingStudentList() {
//		for (int i = 13; i <=14; i++) {
//			for (int j = 1; j <=8; j++) {
//				System.out.println(teacherAPI.getApplyingStudentList(i+"", j));
//			}
//		}
	}

	@Test
	public void testApproveStudent() throws JSONException {
//		JSONObject jsonObject=new JSONObject();
//		JSONArray jsonArray=new JSONArray();
//		for (int i = 1; i <= 12; i++) {
//			JSONObject studentObject=new JSONObject();
//			studentObject.put("studentID", i);
//			jsonArray.put(studentObject);
//		}
//		jsonObject.put("approvedStudentList", jsonArray);
//		for (int i = 1; i <=8; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.approveStudent(j+"", i, jsonObject.toString()));
//			}
//			
//		}
	}

	@Test
	public void testRejectStudent() throws JSONException {
//		JSONObject jsonObject=new JSONObject();
//		JSONArray jsonArray=new JSONArray();
//		for (int i = 1; i <= 12; i++) {
//			JSONObject studentObject=new JSONObject();
//			studentObject.put("studentID", i);
//			jsonArray.put(studentObject);
//		}
//		jsonObject.put("rejectedStudentList", jsonArray);
//		for (int i = 1; i <=8; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.rejectStudent(j+"", i, jsonObject.toString()));
//			}
//		}
	}

	@Test
	public void testDeleteCourse() {
//		for (int i = 1; i <=8; i++) {
//		for (int j = 13; j <=14; j++) {
//			System.out.println(teacherAPI.deleteCourse(i, j+""));
//		}
//	}
	}

	@Test
	public void testAddCourse() {
//		for (int i = 13; i <=14; i++) {
//			for (int j = 0; j < 5; j++) {
//				System.out.println(teacherAPI.addCourse((j+1)*i+"", (j+2)+i+"", j*i+1+"", j, i+""));
//			}
//	}
	}

	//waiting
	@Test
	public void testGetCourseGrade() {
//		System.out.println(teacherAPI.getCourseGrade(1, 13+""));
//		System.out.println(teacherAPI.getCourseGrade(1,13+""));
//		for (int i = 1; i <=8; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.getCourseGrade(i, j+""));
//			}
//		}
		
	}

	@Test
	public void testUpdateCourseGrade() {
//		for (int i = 1; i <=97; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.updateCourseGrade(i, 90, j+""));
//			}
//		}
	}

	@Test
	public void testGetAllCollege() {
//		System.out.println(teacherAPI.getAllCollege("13"));
	}

	@Test
	public void testGetLessonListByCourse() {
//		for (int i = 1; i <8; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.getLessonListByCourse(j+"", i));
//			}
//		}
		
	}

	@Test
	public void testUpdateNotice() {
//		for (int i = 1; i <=36; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.updateNotice(i, i+"c", 1, j+""));
//			}
//		}
	}

	@Test
	public void testDeleteLesson() {
//		for (int i = 37; i <=48; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.deleteLesson(i, j+""));
//			}
//		}
	}

	@Test
	public void testAddLesson() {
//		teacherAPI.addLesson(19, "f952c984cb4c1e03ee3cdd6822162244");
//		for (int i = 1; i <=8; i++) {
//			for (int j = 13; j <=14; j++) {
//				for (int j2 = 0; j2 < 1; j2++) {
//					System.out.println(teacherAPI.addLesson(i, j+""));
//				}
//			}
//		}
	}

	@Test
	public void testGetApplyingLeaveStudentList() {
//		for (int i = 1; i <=36; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.getApplyingLeaveStudentList(i, j+""));
//			}
//		}
		
	}

	@Test
	public void testApproveLeave() {
//		for (int i = 1; i <=18; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.approveLeave(i, j+""));
//			}
//		}
	}

	@Test
	public void testRejectLeave() {
//		for (int i = 18; i <=36; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.rejectLeave(i, j+""));
//			}
//		}
	}

	@Test
	public void testGetEvaluationListByLesson() {
//		for (int i = 1; i <=36; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.getEvaluationListByLesson(i, j+""));
//			}
//		}
	}

	@Test
	public void testGetGroupTemplateInfo() {
//		System.out.println(teacherAPI.getGroupTemplateInfo(2, 13+""));
	}

	@Test
	public void testAddGroupTemplate() {
//		teacherAPI.addGroupTemplate(102, "我的第一个模板", "7a650c133d938b26704f872f61b8ba2a");
//		teacherAPI.addGroupTemplate(2, "gt2",13+"");
	}

	@Test
	public void testGetEvaluationTemplateInfo() {
//		for (int i = 1; i <=8; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.getEvaluationTemplateInfo(j+"", i));
//			}
//		}
	}

	@Test
	public void testAddEvaluation() throws JSONException {
		
		
		
		
//		for (int i = 1; i <=8; i++) {
//			for (int j = 1; j <=36; j++) {
//				for (int j2 = 13; j2 <=14; j2++) {
//					for (int k = 1; k <=3; k++) {
//						boolean isgroup=false;
//						boolean save=false;
//						if (k%2==0) {
//							isgroup=true;
//							save=true;
//						}
//						JSONObject jsonObject=new JSONObject();
//						JSONArray evaluationFieldList=new JSONArray();
//						for (int l = 1; l <= 4; l++) {
//							JSONObject fieldObject=new JSONObject();
//							fieldObject.put("fieldContent", l+"字段");
//							fieldObject.put("resultType", l);
//							if (l==2||l==3) {
//								JSONArray optionArray=new JSONArray();
//								for (int m = 1; m <=4; m++) {
//									JSONObject optionObject=new JSONObject();
//									optionObject.put("optionTitleContent", m+"大标题");
//									optionObject.put("optionKey", m+"");
//									optionArray.put(optionObject);
//								}
//								fieldObject.put("optionTitleList", optionArray);
//							}
//							evaluationFieldList.put(fieldObject);
//							evaluationFieldList.put(fieldObject);
//						}
//						jsonObject.put("evaluationFieldList", evaluationFieldList);
//						
//						teacherAPI.addEvaluation(19, 37, "a3ec992242ecdc813cf921fb21e9f030", "个人评价", false, 0, 7, false, "", evaluationFieldList.toString());
//					}
//				}
//			}
//		}
	}

	@Test
	public void testDeleteEvaluation() {
//		for (int i = 100; i <=108; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.deleteEvaluation(i, j+""));
//			}
//		}
	}

	@Test
	public void testSetEvaluationStatus() {
		teacherAPI.setEvaluationStatus(104, 0, "a3ec992242ecdc813cf921fb21e9f030");
//		teacherAPI.setEvaluationStatus(103, 0, "a3ec992242ecdc813cf921fb21e9f030");
//		for (int i = 1; i <=99; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.setEvaluationStatus(i,0, j+""));
//			}
//		}
	}

	@Test
	public void testGetGroupMemberInfo() {
//		for (int i = 1; i <=99; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.getGroupMemberInfo(i, j+""));
//			}
//		}
	}

	@Test
	public void testTeacherGetReceiverList() {
//		for (int i = 1; i <=99; i++) {
//			for (int j = 13; j <=14; j++) {
//				System.out.println(teacherAPI.teacherGetReceiverList(i, j+""));
//			}
//		}
	}

	@Test
	public void testTeacherGetResult() {
//		System.out.println(teacherAPI.teacherGetResult(2, 1, 13+""));
	}

	@Test
	public void testGetEvaluationGrade() {
//		System.out.println(teacherAPI.getEvaluationGrade(1, 13+""));
	}

	@Test
	public void testUpdateEvalutionGrade() throws JSONException {
//		JSONObject jsonObject=new JSONObject();
//		JSONArray jsonArray=new JSONArray();
//		for (int i = 1; i <=6; i++) {
//			JSONObject gradeObject=new JSONObject();
//			gradeObject.put("receiverID", i);
//			gradeObject.put("evaluationGrade", 90+i);
//			jsonArray.put(gradeObject);
//		}
//		jsonObject.put("evaluationGradeList", jsonArray);
////		System.out.println(jsonObject.toString());
//		System.out.println(teacherAPI.updateEvalutionGrade(13+"", jsonObject.toString()));
	}

}

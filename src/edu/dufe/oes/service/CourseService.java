package edu.dufe.oes.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.dufe.oes.bean.College;
import edu.dufe.oes.bean.CollegeDAO;
import edu.dufe.oes.bean.Course;
import edu.dufe.oes.bean.CourseDAO;
import edu.dufe.oes.bean.CourseElective;
import edu.dufe.oes.bean.CourseElectiveDAO;
import edu.dufe.oes.bean.Evaluation;
import edu.dufe.oes.bean.EvaluationReceiver;
import edu.dufe.oes.bean.Leave;
import edu.dufe.oes.bean.Lesson;
import edu.dufe.oes.bean.Semester;
import edu.dufe.oes.bean.SemesterDAO;
import edu.dufe.oes.bean.Student;
import edu.dufe.oes.bean.StudentDAO;
import edu.dufe.oes.bean.SysConfig;
import edu.dufe.oes.bean.SysConfigDAO;
import edu.dufe.oes.bean.Teacher;
import edu.dufe.oes.bean.TeacherDAO;
import edu.dufe.oes.bean.User;
import edu.dufe.oes.bean.UserDAO;
import edu.dufe.oes.config.CommonValues;
import edu.dufe.oes.config.HibernateSessionFactory;
import edu.dufe.oes.util.MySetUtil;

public class CourseService extends CommonService {

	CourseElectiveDAO courseElectiveDAO=new CourseElectiveDAO();
	CourseDAO courseDAO=new CourseDAO();
	SysConfigDAO sysConfigDAO=new SysConfigDAO();
	SemesterDAO semesterDAO=new SemesterDAO();
	StudentDAO studentDAO=new StudentDAO();
	SemesterService semesterService=new SemesterService();
	UserInfoService userInfoService=new UserInfoService();
	CollegeService collegeService=new CollegeService();
	
	
	public JSONObject getStudentCourseList(String token, int currentSemester) throws Exception {
		JSONObject jsonObject=new JSONObject();
		clearSession(studentDAO);
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Student> studentList=new ArrayList<Student>(user.getStudents());
		if (studentList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Student student=studentList.get(0);
		Semester semester = semesterService.getSemesterBySemesterName(currentSemester);
		if (semester==null) {
			return addSuccessStatus(jsonObject, false, "无效学期");
		}
		
		List<CourseElective> courseElectiveList=new ArrayList<CourseElective>(student.getCourseElectives());
		Collections.sort(courseElectiveList);
		JSONArray courseArray=new JSONArray();
		int i=0;
		for (CourseElective courseElective : courseElectiveList) {
			Course course=courseElective.getCourse();
			if (!course.getSemester().equals(semester)) {
				continue;
			}
			JSONObject courseObject=new JSONObject();
			courseObject.put(courseDAO.COURSE_ID, course.getCourseID());
			courseObject.put(CourseDAO.COURSE_NAME, course.getCourseName());
			courseObject.put(CourseElectiveDAO.ELECTIVE_STATUS, courseElective.getElectiveStatus());
			courseArray.put(i,courseObject);
			i++;
		}
		jsonObject.put("courseList",courseArray);
		return addSuccessStatus(jsonObject, true, "");
	}
	
	public JSONObject getElectiveCourseInfo(int courseID,String token) throws Exception{
		clearSession(courseElectiveDAO);
		JSONObject courseInfoObject=new JSONObject();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(courseInfoObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Student> studentList=new ArrayList<Student>(user.getStudents());
		if (studentList.size()<=0) {
			return addSuccessStatus(courseInfoObject, false, "你再这样老师会找你的");
		}
		Student student=studentList.get(0);
		Course course = courseDAO.findById(courseID);
		if (course==null) {
			return addSuccessStatus(courseInfoObject, false, "无效课程");
		}
		Session session = courseElectiveDAO.getSession();
		Query query=session.createQuery("from CourseElective where studentID=? and courseID=?");
		query.setInteger(0, student.getStudentid());
		query.setInteger(1, course.getCourseID());
		List list = query.list();
		if (list.size()<=0) {
			return addSuccessStatus(courseInfoObject, false, "未选该课");
		}
		CourseElective courseElective = (CourseElective) list.get(0);
		courseInfoObject.put(CourseDAO.COURSE_NAME, course.getCourseName());
		courseInfoObject.put(CourseDAO.COURSE_ID,course.getCourseID());
		courseInfoObject.put(CourseDAO.COURSE_ORDER, course.getCourseOrder());
		courseInfoObject.put(CourseDAO.COURSE_NUMBER, course.getCourseNumber());
		courseInfoObject.put("semester", course.getSemester().getSemesterName());
		courseInfoObject.put(CollegeDAO.COLLEGE_ID, course.getCollege().getCollegeID());
		courseInfoObject.put(CollegeDAO.COLLEGE_NAME, course.getCollege().getCollegeName());
		courseInfoObject.put(CourseElectiveDAO.ELECTIVE_STATUS, courseElective.getElectiveStatus());
		courseInfoObject.put(CourseElectiveDAO.COURSE_GRADE, courseElective.getCourseGrade());
		courseInfoObject.put(CourseElectiveDAO.COURSE_GRADE_STATUS, courseElective.getCourseGradeStatus());
		courseInfoObject.put("teacherName", course.getTeacher().getUser().getRealName());
		courseInfoObject.put(TeacherDAO.TEACHER_ID, course.getTeacher().getTeacherID());
		return addSuccessStatus(courseInfoObject, true, "");
	}//{courseID,courseName,courseNumber,courseOrder,semester,collegeID,collegeName,electiveStatus,teacherName,teacherID,courseGrade,courseGradeStatus}
	
	public JSONObject deleteStudentCourse(int courseID,String token) throws Exception{
		clearSession(courseElectiveDAO);
		JSONObject jsonObject=new JSONObject();
		User user = userInfoService.getUserByToken(token);
		
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Student> studentList=new ArrayList<Student>(user.getStudents());
		if (studentList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Student student=studentList.get(0);
		Course course = courseDAO.findById(courseID);
		if (course==null) {
			return addSuccessStatus(jsonObject, false, "无效课程");
		}
		Session session = courseElectiveDAO.getSession();
		Query query=session.createQuery("from CourseElective where studentID=? and courseID=?");
		query.setInteger(0, student.getStudentid());
		query.setInteger(1, course.getCourseID());
		List list = query.list();
		if (list.size()<=0) {
			return addSuccessStatus(jsonObject, false, "未选该课");
		}
		Transaction transaction = courseElectiveDAO.getSession().beginTransaction();
		CourseElective courseElective = (CourseElective) list.get(0);
		courseElectiveDAO.delete(courseElective);
		transaction.commit();
		courseElectiveDAO.getSession().close();
		return addSuccessStatus(jsonObject, true, "");
		
	}//{success}
	
	public JSONObject getAllAvailableCourseList(String token) throws Exception{
		JSONObject jsonObject=new JSONObject();
		clearSession(courseDAO);
		Semester semester=semesterService.getCurrentSemester();
		semester.getSemesterID();
		List<Course> courseList=new ArrayList<Course>(semester.getCourses());
		User user = userInfoService.getUserByToken(token);
		Set<Student> students = user.getStudents();
		if (students.isEmpty()) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Student student=students.iterator().next();
		List<CourseElective> courseElectivelist=new ArrayList<CourseElective>(student.getCourseElectives());
		JSONArray jsonArray=new JSONArray();
		Collections.sort(courseList);
		int i=0;
		for (Course course : courseList) {
			boolean hasSame=false;
			for (CourseElective courseElective : courseElectivelist) {
				if (courseElective.getCourse().equals(course)) {
					hasSame=true;
					break;
				}
			}
			if (hasSame==true) {
				continue;
			}
			JSONObject courseObject=new JSONObject();
			courseObject.put(CourseDAO.COURSE_ID, course.getCourseID());
			courseObject.put(CourseDAO.COURSE_NAME, course.getCourseName());
			courseObject.put("teacherName", course.getTeacher().getUser().getRealName());
			jsonArray.put(i,courseObject);
			i++;
		}
		jsonObject.put("AllAvailableCourseList", jsonArray);
		return addSuccessStatus(jsonObject, true, "");
	}
	
	public JSONObject getCourseInfo(int courseID,String token) throws Exception{
		clearSession(courseDAO);
		JSONObject jsonObject=new JSONObject();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		Course course = courseDAO.findById(courseID);
		if (course==null) {
			return addSuccessStatus(jsonObject, false, "无效课程");
		}
		jsonObject.put(CourseDAO.COURSE_ID, course.getCourseID());
		jsonObject.put(CourseDAO.COURSE_NAME, course.getCourseName());
		jsonObject.put(CourseDAO.COURSE_NUMBER, course.getCourseNumber());
		jsonObject.put(CourseDAO.COURSE_ORDER, course.getCourseOrder());
		jsonObject.put("semester", course.getSemester().getSemesterName());
		jsonObject.put(CollegeDAO.COLLEGE_ID, course.getCollege().getCollegeID());
		jsonObject.put(CollegeDAO.COLLEGE_NAME, course.getCollege().getCollegeName());
		jsonObject.put("teacherName", course.getTeacher().getUser().getUsername());
		jsonObject.put(TeacherDAO.TEACHER_ID, course.getTeacher().getTeacherID());
		return addSuccessStatus(jsonObject, true, "");
		
	}//{courseID,courseName,courseNumber,courseOrder,semester,collegeID,collegeName,teacherName,teacherID}
	
	/*
	 * 
	 */
	public JSONObject queryCourse(String courseName,String token) throws Exception{
		JSONObject jsonObject=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		clearSession(courseDAO);
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		Semester currentSemester=semesterService.getCurrentSemester();
		if (currentSemester==null) {
			return addSuccessStatus(jsonObject, false, "无效学期");
		}
		List<Student> studentList=new ArrayList<Student>(user.getStudents());
		if (studentList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会打你的");
		}
		Student student= studentList.get(0);
		Course course=new Course();
		course.setCourseName(courseName);
		List<Course> courseList = courseDAO.findByExample(course);
		Collections.sort(courseList);
		int i=0;
		for (Course resultCourse : courseList) {
			JSONObject courseObject=new JSONObject();
			if (!resultCourse.getSemester().equals(currentSemester)) {
				continue;
			}
			courseObject.put(CourseDAO.COURSE_ID, resultCourse.getCourseID());
			courseObject.put(CourseDAO.COURSE_NAME, resultCourse.getCourseName());
			courseObject.put(CollegeDAO.COLLEGE_ID, resultCourse.getCollege().getCollegeID());
			courseObject.put(CollegeDAO.COLLEGE_NAME, resultCourse.getCollege().getCollegeName());
			courseObject.put("teacherName",resultCourse.getTeacher().getUser().getRealName());
			courseObject.put(TeacherDAO.TEACHER_ID, resultCourse.getTeacher().getTeacherID());
			jsonArray.put(i,courseObject);
			i++;
		}
		jsonObject.put("courseList", jsonArray);
		return addSuccessStatus(jsonObject, true, "");
	}//{courseList:[courseID,courseName,teacherName,collegeName,collegeID]}
	

	public JSONObject electCourse(int courseID,String token) throws Exception{
		clearSession(courseDAO);
		JSONObject jsonObject=new JSONObject();
		
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		Course course = courseDAO.findById(courseID);
		if (course==null) {
			return addSuccessStatus(jsonObject, false, "无效课程");
		}
		List<Student> studentList=new ArrayList<Student>(user.getStudents());
		if (studentList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会打你的");
		}
		Student student= studentList.get(0);
		List<CourseElective> courseElectiveList=new ArrayList<CourseElective>(student.getCourseElectives());
		for (CourseElective courseElective : courseElectiveList) {
			if (courseElective.getCourse().equals(course)) {
				return addSuccessStatus(jsonObject, false, "你已选过该课");
			}
		}
		CourseElective courseElective=new CourseElective();
		courseElective.setCourse(course);
		courseElective.setCourseGrade(0);
		courseElective.setCourseGradeStatus(CourseElective.NOGRADE);
		courseElective.setStudent(student);
		courseElective.setElectiveStatus(CourseElective.APPROVING);
		courseElective.setElectiveTime(new Timestamp(System.currentTimeMillis()));
		courseElectiveDAO.save(courseElective);
		return addSuccessStatus(jsonObject, true, "");
	}
	
	public JSONObject getTeacherCourseList(String token,int currentSemester) throws Exception{
		JSONObject jsonObject=new JSONObject();
		clearSession(courseDAO);
		Semester semester = semesterService.getSemesterBySemesterName(currentSemester);
		User user=userInfoService.getUserByToken(token);
		
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
		if (teachertList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		if (semester==null) {
			return addSuccessStatus(jsonObject, false, "无效学期");
		}
		Teacher teacher=teachertList.get(0);
		List<Course> courseList = new ArrayList<Course>(teacher.getCourses());
		Collections.sort(courseList);
		JSONArray jsonArray=new JSONArray();
		int i=0;
		for (Course course : courseList) {
			if (!course.getSemester().equals(semester)) {
				continue;
			}
			List<CourseElective> courseElectiveList=new ArrayList<CourseElective>(course.getCourseElectives());
			int applyingStudentNumber=0;
			int totalStudentNumber=0;
			for (CourseElective courseElective : courseElectiveList) {
				if (courseElective.getElectiveStatus()==CourseElective.APPROVING) {
					applyingStudentNumber++;
				}
				totalStudentNumber++;
			}
			JSONObject courseObject=new JSONObject();
			courseObject.put("totalStudentNumber", totalStudentNumber);
			courseObject.put("applyingStudentNumber", applyingStudentNumber);
			courseObject.put(CourseDAO.COURSE_ID, course.getCourseID());
			courseObject.put(CourseDAO.COURSE_NAME, course.getCourseName());
			jsonArray.put(i,courseObject);
			i++;
		}
		jsonObject.put("courseList", jsonArray);
		return addSuccessStatus(jsonObject, true, "");
	}//{courseList:[courseID,courseName,applyingStudentNumber]}
	
	public JSONObject getTeacherCourseInfo(int courseID,String token) throws Exception{
		clearSession(courseDAO);
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
		if (teachertList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Teacher teacher=teachertList.get(0);
		Course course = courseDAO.findById(courseID);
		if (course==null) {
			return addSuccessStatus(jsonObject, false, "无效课程");
		}
		if (!course.getTeacher().equals(teacher)) {
			return addSuccessStatus(jsonObject, false, "非本人课程");
		}
		List<CourseElective> courseElectiveList=new ArrayList<CourseElective>(course.getCourseElectives());
		int totalStudentNumber=0;
		int applyingStudentNumber=0;
		for (CourseElective courseElective : courseElectiveList) {
			if (courseElective.getElectiveStatus()==CourseElective.APPROVING) {
				applyingStudentNumber++;
			}
			if (courseElective.getElectiveStatus()!=CourseElective.REJECTED) {
				totalStudentNumber++;
			}
		}
		jsonObject.put(CourseDAO.COURSE_ID, course.getCourseID());
		jsonObject.put(CourseDAO.COURSE_NAME, course.getCourseName());
		jsonObject.put(CourseDAO.COURSE_NUMBER, course.getCourseNumber());
		jsonObject.put(CourseDAO.COURSE_ORDER, course.getCourseOrder());
		jsonObject.put("semester", course.getSemester().getSemesterName());
		jsonObject.put(CollegeDAO.COLLEGE_ID, course.getCollege().getCollegeID());
		jsonObject.put(CollegeDAO.COLLEGE_NAME, course.getCollege().getCollegeName());
		jsonObject.put("applyingStudentNumber", applyingStudentNumber);
		jsonObject.put("totalStudentNumber", totalStudentNumber);
		return addSuccessStatus(jsonObject, true, "");
	}////{courseID,courseName,courseNumber,courseOrder,semester,collegeID,collegeName,totalStudentNumber,applyingStudentNumber}
	
	
	public JSONObject getApplyingStudentList(String token,int courseID) throws Exception{
		
		clearSession(courseDAO);
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
		if (teachertList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Teacher teacher=teachertList.get(0);
		Course course = courseDAO.findById(courseID);
		if (course==null) {
			return addSuccessStatus(jsonObject, false, "无效课程");
		}
		if (!course.getTeacher().equals(teacher)) {
			return addSuccessStatus(jsonObject, false, "非本人课程");
		}
		List<CourseElective> courseElectiveList=new ArrayList<CourseElective>(course.getCourseElectives());
		Collections.sort(courseElectiveList);
		JSONArray applyingStudentArray=new JSONArray();
		for (CourseElective courseElective : courseElectiveList) {
			JSONObject studentObject=new JSONObject();
			if (courseElective.getElectiveStatus()!=courseElective.APPROVING) {
				continue;
			}
			studentObject.put(StudentDAO.STUDENT_ID, courseElective.getStudent().getStudentid());
			studentObject.put(UserDAO.USERNAME, courseElective.getStudent().getUser().getUsername());
			studentObject.put("studentName", courseElective.getStudent().getUser().getRealName());
			studentObject.put(CollegeDAO.COLLEGE_NAME, courseElective.getStudent().getUser().getCollege().getCollegeName());
			studentObject.put(StudentDAO.MAJOR, courseElective.getStudent().getMajor());
			applyingStudentArray.put(studentObject);
		}
		jsonObject.put("applyingStudentList", applyingStudentArray);
		return addSuccessStatus(jsonObject, true, "");
	}//{applyingStudentList:[{studentID,userName,studentName,collegeName,major}]}
	
	public JSONObject approveStudent(String token,int courseID,String studentList) throws Exception{
		clearSession(courseDAO);
		JSONArray approvedStudentList=new JSONArray(studentList);
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
		if (teachertList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Teacher teacher=teachertList.get(0);
		Course course = courseDAO.findById(courseID);
		if (course==null) {
			return addSuccessStatus(jsonObject, false, "无效课程");
		}
		if (!course.getTeacher().equals(teacher)) {
			return addSuccessStatus(jsonObject, false, "非本人课程");
		}
		for (int i = 0; i < approvedStudentList.length(); i++) {
			JSONObject studentObject=approvedStudentList.getJSONObject(i);
			Student student = studentDAO.findById(studentObject.getInt("studentID"));
			List<CourseElective> courseElectiveList=new ArrayList<CourseElective>(course.getCourseElectives());
			for (CourseElective courseElective : courseElectiveList) {
				if (courseElective.getStudent().equals(student)&&courseElective.getElectiveStatus()==CourseElective.APPROVING) {
					Transaction transaction = courseElectiveDAO.getSession().beginTransaction();
					courseElective.setElectiveStatus(CourseElective.APPROVED);
					courseElectiveDAO.getSession().update(courseElective);
					transaction.commit();
				}
			}
		}
		return addSuccessStatus(jsonObject, true, "");
		
	}//input:{approvedStudentList:[{studentID}]}//output:success
	
	public JSONObject rejectStudent(String token,int courseID,String studentList) throws Exception{
		clearSession(courseDAO);
		JSONArray rejectedStudentList=new JSONArray(studentList);
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
		if (teachertList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Teacher teacher=teachertList.get(0);
		Course course = courseDAO.findById(courseID);
		if (course==null) {
			return addSuccessStatus(jsonObject, false, "无效课程");
		}
		if (!course.getTeacher().equals(teacher)) {
			return addSuccessStatus(jsonObject, false, "非本人课程");
		}
		for (int i = 0; i < rejectedStudentList.length(); i++) {
			JSONObject studentObject=rejectedStudentList.getJSONObject(i);
			Student student = studentDAO.findById(studentObject.getInt("studentID"));
			List<CourseElective> courseElectiveList=new ArrayList<CourseElective>(course.getCourseElectives());
			for (CourseElective courseElective : courseElectiveList) {
				if (courseElective.getStudent().equals(student)&&courseElective.getElectiveStatus()==CourseElective.APPROVING) {
					Transaction transaction = courseElectiveDAO.getSession().beginTransaction();
					courseElective.setElectiveStatus(CourseElective.REJECTED);
					courseElectiveDAO.getSession().update(courseElective);
					transaction.commit();
				}
			}
		}
		return addSuccessStatus(jsonObject, true, "");
	}//input:{rejectedStudentList:[{studentID}]}//output:success
	
	public JSONObject addCourse(String courseNumber,String courseOrder,String courseName,int collegeID,String token) throws Exception{
		clearSession(courseDAO);
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
		if (teachertList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Teacher teacher=teachertList.get(0);
		College college=collegeService.findCollegeByID(collegeID);
		if (college==null) {
			return addSuccessStatus(jsonObject, false, "无效学院");
		}
		if (teacher.getApproveStatus()<1) {
			return addSuccessStatus(jsonObject, false, "请等待管理员批准您开课");
		}
		Semester semester=semesterService.getCurrentSemester();
		Course course=new Course();
		course.setCollege(college);
		course.setCollege(college);
		course.setTeacher(teacher);
		course.setSemester(semester);
		course.setCourseNumber(courseNumber);
		course.setCourseOrder(courseOrder);
		course.setCreateDate(new Timestamp(System.currentTimeMillis()));
		course.setCourseName(courseName);
		courseDAO.save(course);
		jsonObject.put(CourseDAO.COURSE_ID, course.getCourseID());
		return addSuccessStatus(jsonObject, true, "");
	}//{success,courseID}
	
	public JSONObject deleteCourse(int courseID,String token) throws Exception{
		clearSession(courseDAO);
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
		if (teachertList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Teacher teacher=teachertList.get(0);
		Course course = courseDAO.findById(courseID);
		if (course==null) {
			return addSuccessStatus(jsonObject, false, "无效课程");
		}
		if (!course.getTeacher().equals(teacher)) {
			return addSuccessStatus(jsonObject, false, "非本人课程");
		}
		if (!course.getCourseElectives().isEmpty()) {
			return addSuccessStatus(jsonObject, false, "已有学生选课");
		}
		Transaction transaction = courseDAO.getSession().beginTransaction();
		courseDAO.delete(course);
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
	}//{success}
	
	public JSONObject updateCourseGrade(int electiveID,int grade,String token) throws Exception{
		clearSession(courseElectiveDAO);
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
		if (teachertList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Teacher teacher=teachertList.get(0);
		CourseElective courseElective=courseElectiveDAO.findById(electiveID);
		if (courseElective==null) {
			return addSuccessStatus(jsonObject, false, "无效选课记录");
		}
		if (grade>100||grade<0) {
			return addSuccessStatus(jsonObject, false, "无效成绩");
		}
		Transaction transaction = courseElectiveDAO.getSession().beginTransaction();
		courseElective.setCourseGrade(grade);
		courseElective.setCourseGradeStatus(CourseElective.HASGRADE);
		courseElectiveDAO.getSession().update(courseElective);
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
	}//{success}
	
	public JSONObject getCourseGrade(int courseID,String token) throws Exception{
		JSONObject jsonObject=new JSONObject();
		clearSession(courseDAO);
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Object> teachertList=MySetUtil.toList(user.getTeachers());
		if (teachertList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Teacher teacher=(Teacher) teachertList.get(0);
		Course course = courseDAO.findById(courseID);
		if (course==null) {
			return addSuccessStatus(jsonObject, false, "无效课程");
		}
		if (!course.getTeacher().equals(teacher)) {
			return addSuccessStatus(jsonObject, false, "非本人课程");
		}
		List<CourseElective> tmpCourseElectiveList=new ArrayList<CourseElective>(course.getCourseElectives());
		List<CourseElective> courseElectiveList=new ArrayList<CourseElective>();
		for (CourseElective courseElective : tmpCourseElectiveList) {
			if (courseElective.getElectiveStatus()!=CourseElective.APPROVED) {
				continue;
			}
			courseElectiveList.add(courseElective);
		}
		JSONArray courseGradeArray=new JSONArray();
		
		List<Lesson> totalLessonList=new ArrayList<Lesson>(course.getLessons());
		List<Leave> totalLeaveList=new ArrayList<Leave>();
		for (Lesson lesson : totalLessonList) {
			totalLeaveList.addAll(lesson.getLeaves());
		}
		List<Evaluation> totalEvaluationList=new ArrayList<Evaluation>();
		for (Lesson lesson : totalLessonList) {
			List<Evaluation> evaluationInLesson=new ArrayList<Evaluation>(lesson.getEvaluations());
			totalEvaluationList.addAll(evaluationInLesson);
		}
		int totalEvaluationNumber=totalEvaluationList.size();
		List<EvaluationReceiver> totalPersonEvaluationReceiverList=new ArrayList<EvaluationReceiver>();
		for (Evaluation evaluation : totalEvaluationList) {
			List<EvaluationReceiver> evaluationReceiverList=new ArrayList<EvaluationReceiver>(evaluation.getEvaluationReceivers());
			for (EvaluationReceiver evaluationReceiver : evaluationReceiverList) {
				if (evaluationReceiver.getReceiverType()==EvaluationReceiver.PERSON_RECEIVER) {
					totalPersonEvaluationReceiverList.add(evaluationReceiver);
				}
			}
		}
		double averageJonningPercentage=(double)totalPersonEvaluationReceiverList.size()/(double)courseElectiveList.size();
		Collections.sort(courseElectiveList);
		int i=0;
		for (CourseElective courseElective : courseElectiveList) {
			JSONObject courseGradeObject=new JSONObject();
			Student student=courseElective.getStudent();
			courseGradeObject.put("electiveID", courseElective.getCourseElectiveID());
			courseGradeObject.put("studentName", student.getUser().getRealName());
			courseGradeObject.put(CollegeDAO.COLLEGE_NAME, student.getUser().getCollege().getCollegeName());
			courseGradeObject.put(UserDAO.USERNAME, student.getUser().getUsername());
			courseGradeObject.put(CourseElectiveDAO.COURSE_GRADE, courseElective.getCourseGrade());
			courseGradeObject.put(CourseElectiveDAO.COURSE_GRADE_STATUS, courseElective.getCourseGradeStatus());
			courseGradeObject.put("totalEvaluationNumber", totalEvaluationNumber);
			courseGradeObject.put("averageJoiningPercentage", averageJonningPercentage);
			//计算我的评价次数
			int myEvaluationNumber=0;
			double myTotalEvaluationGrade=0;
			for (EvaluationReceiver evaluationReceiver : totalPersonEvaluationReceiverList) {
				if (evaluationReceiver.getStudent().equals(student)) {
					myEvaluationNumber++;
					myTotalEvaluationGrade+=evaluationReceiver.getEvaluationGrade();
				}
			}
			courseGradeObject.put("myEvaluationNumber", myEvaluationNumber);
			//计算我的评价分数
			double averageEvaluationGrade=0;
			if (myEvaluationNumber!=0&&myTotalEvaluationGrade!=0) {
				averageEvaluationGrade=(double)myTotalEvaluationGrade/(double)myEvaluationNumber;
			}
			
			courseGradeObject.put("averageEvaluationGrade", averageEvaluationGrade);
			//计算请假次数
			int myLeaveTimes=0;
			for (Leave leave : totalLeaveList) {
				if (leave.getStudent().equals(student)) {
					myLeaveTimes++;
				}
			}
			courseGradeObject.put("leaveNumber", myLeaveTimes);
			courseGradeArray.put(i,courseGradeObject);
			i++;
		}
		jsonObject.put("courseGradeList", courseGradeArray);
		return addSuccessStatus(jsonObject, true, "");
	}//{courseGradeList:[{electiveID,studentName,courseGrade,collegeName,userName,averageJoiningPercentage,averageJoningPercentage,myEvaluationNumber,averageEvaluationGrade,courseGradeStatus,leaveNumber}]}
}

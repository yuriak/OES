package edu.dufe.oes.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.org.apache.bcel.internal.generic.RET;
import com.sun.org.apache.regexp.internal.recompile;

import edu.dufe.oes.bean.Course;
import edu.dufe.oes.bean.CourseDAO;
import edu.dufe.oes.bean.CourseElective;
import edu.dufe.oes.bean.CourseElectiveDAO;
import edu.dufe.oes.bean.Leave;
import edu.dufe.oes.bean.LeaveDAO;
import edu.dufe.oes.bean.Lesson;
import edu.dufe.oes.bean.LessonDAO;
import edu.dufe.oes.bean.Notice;
import edu.dufe.oes.bean.NoticeDAO;
import edu.dufe.oes.bean.SemesterDAO;
import edu.dufe.oes.bean.Student;
import edu.dufe.oes.bean.SysConfigDAO;
import edu.dufe.oes.bean.Teacher;
import edu.dufe.oes.bean.User;
import edu.dufe.oes.config.CommonValues;
import edu.dufe.oes.util.MySetUtil;

public class LessonService extends CommonService {
	CourseElectiveDAO courseElectiveDAO=new CourseElectiveDAO();
	CourseDAO courseDAO=new CourseDAO();
	SysConfigDAO sysConfigDAO=new SysConfigDAO();
	LessonDAO lessonDAO=new LessonDAO();
	LeaveDAO leaveDAO=new LeaveDAO();
	SemesterDAO semesterDAO=new SemesterDAO();
	NoticeDAO noticeDAO=new NoticeDAO();
	SemesterService semesterService=new SemesterService();
	UserInfoService userInfoService=new UserInfoService();
	
	public JSONObject getLessonListByCourse(String token, int courseID) throws Exception {
		clearSession(leaveDAO);
		JSONObject jsonObject=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Object> teacherList=MySetUtil.toList(user.getTeachers());
		List<Object> studentList=MySetUtil.toList(user.getStudents());
		Student student;
		Teacher teacher;
		if (studentList.size()<=0&&teacherList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会打你的");
		}else if (teacherList.size()<=0) {
			student=(Student) studentList.get(0);
		}else if (studentList.size()<=0) {
			teacher=(Teacher) teacherList.get(0);
		}
		
		Course course = courseDAO.findById(courseID);
		if (course==null) {
			return addSuccessStatus(jsonObject, false, "无效课程");
		}
		Set<Lesson> lessons=course.getLessons();
		Iterator<Lesson> iterator=lessons.iterator();
		List<Lesson> lessonList=new ArrayList<Lesson>();
		while (iterator.hasNext()) {
			Lesson lesson=iterator.next();
			lessonList.add(lesson);
		}
		
//		List<Lesson> lessonList=lessonDAO.getSession().createQuery("from Lesson where CourseID="+courseID).list();
		if (lessonList.size()<=0) {
			jsonObject.put("lessonList", jsonArray);
			return addSuccessStatus(jsonObject, true, "");
		}
		Collections.sort(lessonList);
		int i=0;
		for (Lesson lesson : lessonList) {
			JSONObject lessonObject=new JSONObject();
			Set<Leave> leaves = lesson.getLeaves();
			Iterator<Leave> leaveIterator = leaves.iterator();
			boolean hasLeave=false;
			while (leaveIterator.hasNext()) {
				if (leaveIterator.next().getApproveStatus()==Leave.APPROVING) {
					hasLeave=true;
					break;
				}
			}
			lessonObject.put("hasLeave", hasLeave);
			lessonObject.put("lessonID", lesson.getLessonID());
			lessonObject.put(LessonDAO.LESSON_STATUS, lesson.getLessonStatus());
			lessonObject.put("startTime", lesson.getAddTime());
			List noticeList=MySetUtil.toList(lesson.getNotices());
			if (noticeList.size()<=0) {
				lessonObject.put(CommonValues.EMERGENCY_LEVEL,Notice.NORMAL);
			}else {
				lessonObject.put(CommonValues.EMERGENCY_LEVEL,((Notice)noticeList.get(0)).getEmergencyLevel());
			}
			jsonArray.put(i,lessonObject);
			i++;
		}
		jsonObject.put("lessonList", jsonArray);
		return addSuccessStatus(jsonObject, true, "");
		
	}//{lessonList:[{ startTime, lessonStatus(-1befor 0now 1done), lessonID,emergencyLevel}]}
	
	public JSONObject getNoticeByLessonID(int lessonID,String token) throws Exception{
		clearSession(noticeDAO);
		JSONObject jsonObject=new JSONObject();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Object> studentList=MySetUtil.toList(user.getStudents());
		List<Teacher> teacherList=new ArrayList<Teacher>(user.getTeachers());
		if(teacherList.size()<=0&&studentList.size()<=0){
			return addSuccessStatus(jsonObject, true, "你再这样老师会打你的");
		}
		Lesson lesson = lessonDAO.findById(lessonID);
		if (lesson==null) {
			return addSuccessStatus(jsonObject, false, "无效节次");
		}
		List noticeList=MySetUtil.toList(lesson.getNotices());
		if (noticeList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "无效公告");
		}
		Notice notice=(Notice) noticeList.get(0);
		jsonObject.put("noticeID", notice.getNoticeID());
		jsonObject.put("noticeContent", notice.getContent());
		jsonObject.put(NoticeDAO.EMERGENCY_LEVEL, notice.getEmergencyLevel());
		return addSuccessStatus(jsonObject, true, "");
	}//noticeID,noticeContent,EmergencyLevel
	
	public JSONObject getLeaveInfo(String token,int lessonID) throws Exception{
		clearSession(leaveDAO);
		JSONObject jsonObject=new JSONObject();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Object> studentList=MySetUtil.toList(user.getStudents());
		if (studentList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会打你的");
		}
		Student student=(Student) studentList.get(0);
		Lesson lesson = lessonDAO.findById(lessonID);
		if (lesson==null) {
			return addSuccessStatus(jsonObject, false, "无效节次");
		}
		List<Leave> leaveList=new ArrayList<Leave>(student.getLeaves());
		if (leaveList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "");
		}
		for (Leave leave : leaveList) {
			if (leave.getLesson().equals(lesson)) {
				jsonObject.put("leaveID", leave.getLeaveID());
				jsonObject.put("approveStatus", leave.getApproveStatus());
				jsonObject.put(LeaveDAO.LEAVE_REASON, leave.getLeaveReason());
				return addSuccessStatus(jsonObject, true, "");
			}
		}
		return addSuccessStatus(jsonObject, false, "");
	}//{leaveID,approveStatus}
	
	public JSONObject leave(String leaveReason,int lessonID,String token) throws Exception{
		clearSession(leaveDAO);
		JSONObject jsonObject=new JSONObject();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Object> studentList=MySetUtil.toList(user.getStudents());
		if (studentList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会打你的");
		}
		Student student=(Student) studentList.get(0);
		Lesson lesson = lessonDAO.findById(lessonID);
		if (lesson==null) {
			return addSuccessStatus(jsonObject, false, "无效节次");
		}
		Course course=lesson.getCourse();
		List<CourseElective> courseElectiveList=new ArrayList<CourseElective>(student.getCourseElectives());
		boolean hasCourse=false;
		for (CourseElective courseElective : courseElectiveList) {
			if (courseElective.getCourse().equals(course)) {
				hasCourse=true;
				break;
			}
		}
		if (lesson.getLessonStatus()==Lesson.DOING||lesson.getLessonStatus()==Lesson.AFTER) {
			return addSuccessStatus(jsonObject, false, "课程已开始或结束");
		}
		List list=MySetUtil.toList(student.getLeaves());
		if (list.size()>0) {
			for (Object object : list) {
				Leave leave=(Leave) object;
				if (leave.getLesson().equals(lesson)) {
					return addSuccessStatus(jsonObject, false, "已经请过假");
				}
			}
		}
		Leave leave=new Leave();
		leave.setApproveStatus(Leave.APPROVING);
		leave.setLeaveReason(leaveReason);
		leave.setLeaveTime(new Timestamp(System.currentTimeMillis()));
		leave.setLesson(lesson);
		leave.setStudent(student);
		leaveDAO.save(leave);
		return addSuccessStatus(jsonObject, true, "");
	}//success,
	
	public JSONObject addLesson(int courseID,String token,String noticeContent,int emergencyLevel) throws Exception{
		clearSession(leaveDAO);
		JSONObject jsonObject=new JSONObject();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Object> teacherList=MySetUtil.toList(user.getTeachers());
		if (teacherList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会打你的");
		}
		Teacher teacher=(Teacher) teacherList.get(0);
		Course course = courseDAO.findById(courseID);
		if (course==null) {
			return addSuccessStatus(jsonObject, false, "无效课程");
		}
		List<Course> courseList=new ArrayList<Course>(teacher.getCourses());
		if (!courseList.contains(course)) {
			return addSuccessStatus(jsonObject, false, "不是你的课");
		}
		Lesson lesson=new Lesson();
		lesson.setCourse(course);
		lesson.setLessonStatus(Lesson.BEFORE);
		lesson.setAddTime(new Timestamp(System.currentTimeMillis()));
		lessonDAO.save(lesson);
		Notice notice=new Notice();
		notice.setContent(noticeContent);
		notice.setEmergencyLevel(emergencyLevel);
		notice.setLesson(lesson);
		noticeDAO.save(notice);
		jsonObject.put(LessonDAO.LESSON_ID, lesson.getLessonID());
		jsonObject.put(NoticeDAO.NOTICE_ID, notice.getNoticeID());
		return addSuccessStatus(jsonObject, true, "");
		
	}//{success,lessonID,noticeID}
	
	public JSONObject updateNotice(int noticeID,String noticeContent,int emergencyLevel,String token) throws Exception{
		clearSession(noticeDAO);
		JSONObject jsonObject=new JSONObject();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Object> teacherList=MySetUtil.toList(user.getTeachers());
		if (teacherList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会打你的");
		}
		Teacher teacher=(Teacher) teacherList.get(0);
		Notice notice=noticeDAO.findById(noticeID);
		if (notice==null) {
			return addSuccessStatus(jsonObject, false, "无效公告");
		}
		if (notice.getLesson()==null) {
			return addSuccessStatus(jsonObject, false, "无效节次");
		}
		notice.setContent(noticeContent);
		if (emergencyLevel>1||emergencyLevel<0) {
			return addSuccessStatus(jsonObject, false, "无效紧急状态");
		}
		Course course=notice.getLesson().getCourse();
		if (!teacher.getCourses().contains(course)) {
			return addSuccessStatus(jsonObject, false, "不是你的课");
		}
		notice.setEmergencyLevel(emergencyLevel);
		Transaction transaction = noticeDAO.getSession().beginTransaction();
		noticeDAO.getSession().update(notice);
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
		
	}//{success}
	
	public JSONObject deleteLesson(int lessonID,String token) throws Exception{
		clearSession(leaveDAO);
		JSONObject jsonObject=new JSONObject();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Object> teacherList=MySetUtil.toList(user.getTeachers());
		if (teacherList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会打你的");
		}
		Teacher teacher=(Teacher) teacherList.get(0);
		Lesson lesson=lessonDAO.findById(lessonID);
		if (lesson==null) {
			return addSuccessStatus(jsonObject, false, "无效节次");
		}
		Course course=lesson.getCourse();
		List<Course> courseList=new ArrayList<Course>(teacher.getCourses());
		if (!courseList.contains(course)) {
			return addSuccessStatus(jsonObject, false, "不是你的课");
		}
		if (MySetUtil.toList(lesson.getEvaluations()).size()>0) {
			return addSuccessStatus(jsonObject, false, "已存在评价");
		}
		Transaction transaction = lessonDAO.getSession().beginTransaction();
		lessonDAO.delete(lesson);
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
	}//{success,reason}
	
	public JSONObject getApplyingLeaveStudentList(int lessonID,String token) throws Exception{
		clearSession(leaveDAO);
		JSONObject jsonObject=new JSONObject();
		JSONArray leaveArray=new JSONArray();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Object> teacherList=MySetUtil.toList(user.getTeachers());
		if (teacherList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会打你的");
		}
		Teacher teacher=(Teacher) teacherList.get(0);
		Lesson lesson=lessonDAO.findById(lessonID);
		if (lesson==null) {
			return addSuccessStatus(jsonObject, false, "无效节次");
		}
		List<Leave> leaveList=new ArrayList<Leave>(lesson.getLeaves());
		Collections.sort(leaveList);
		int i=0;
		for (Leave leave : leaveList) {
			if (leave.getApproveStatus()!=Leave.APPROVING) {
				continue;
			}
			JSONObject leaveObject=new JSONObject();
			leaveObject.put(LeaveDAO.LEAVE_ID, leave.getLeaveID());
			leaveObject.put(LeaveDAO.LEAVE_REASON, leave.getLeaveReason());
			leaveObject.put("studentName", leave.getStudent().getUser().getRealName());
			leaveObject.put("userName", leave.getStudent().getUser().getUsername());
			leaveArray.put(i,leaveObject);
			i++;
		}
		jsonObject.put("applyingLeaveStudentList", leaveArray);
		return addSuccessStatus(jsonObject, true, "");
	}//{success,applyingLeaveStudentList:[{leaveID,studentName,leaveReason,userName}]}
	
	public JSONObject approveLeave(String token,String approveList) throws Exception{
		clearSession(leaveDAO);
		JSONArray approveArray=new JSONArray(approveList);
		JSONObject jsonObject=new JSONObject();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		if (approveArray.length()<=0) {
			return addSuccessStatus(jsonObject, false, "无效请假数据");
		}
		List<Object> teacherList=MySetUtil.toList(user.getTeachers());
		if (teacherList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会打你的");
		}
		
		Teacher teacher=(Teacher) teacherList.get(0);
		
		Transaction transaction = leaveDAO.getSession().beginTransaction();
		for (int i = 0; i < approveArray.length(); i++) {
			JSONObject leaveObject=approveArray.getJSONObject(i);
			int leaveID=leaveObject.getInt("leaveID");
			System.out.println(leaveID);
			Leave leave=leaveDAO.findById(leaveID);
			if (leave==null) {
				leaveDAO.getSession().close();
				return addSuccessStatus(jsonObject, false, "无效请假记录");
			}
			if (!teacher.getCourses().contains(leave.getLesson().getCourse())) {
				leaveDAO.getSession().close();
				return addSuccessStatus(jsonObject, false, "不是你的课");
			}
			if (leave.getApproveStatus()!=Leave.APPROVING) {
				leaveDAO.getSession().close();
				return addSuccessStatus(jsonObject, false, "已批准或拒绝");
			}
			leave.setApproveStatus(Leave.APPROVED);
			leaveDAO.getSession().update(leave);
		}
		
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
	}//{success}
	
	public JSONObject rejectLeave(String token,String rejectList) throws Exception{
		clearSession(leaveDAO);
		JSONArray rejectArray=new JSONArray(rejectList);
		JSONObject jsonObject=new JSONObject();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		if (rejectArray.length()<=0) {
			return addSuccessStatus(jsonObject, false, "无效请假数据");
		}
		List<Object> teacherList=MySetUtil.toList(user.getTeachers());
		if (teacherList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会打你的");
		}
		Teacher teacher=(Teacher) teacherList.get(0);
		Transaction transaction = leaveDAO.getSession().beginTransaction();
		for (int i = 0; i < rejectArray.length(); i++) {
			JSONObject leaveObject=rejectArray.getJSONObject(i);
			int leaveID=leaveObject.getInt("leaveID");
			Leave leave=leaveDAO.findById(leaveID);
			if (leave==null) {
				leaveDAO.getSession().close();
				return addSuccessStatus(jsonObject, false, "无效请假记录");
			}
			if (!teacher.getCourses().contains(leave.getLesson().getCourse())) {
				leaveDAO.getSession().close();
				return addSuccessStatus(jsonObject, false, "不是你的课");
			}
			if (leave.getApproveStatus()!=Leave.APPROVING) {
				leaveDAO.getSession().close();
				return addSuccessStatus(jsonObject, false, "已批准或拒绝");
			}
			leave.setApproveStatus(Leave.REJECTED);
			leaveDAO.getSession().update(leave);
		}
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
	}//{success}
}

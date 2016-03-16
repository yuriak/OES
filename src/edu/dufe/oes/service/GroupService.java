package edu.dufe.oes.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.org.apache.bcel.internal.generic.IREM;

import edu.dufe.oes.bean.Course;
import edu.dufe.oes.bean.CourseDAO;
import edu.dufe.oes.bean.CourseElective;
import edu.dufe.oes.bean.CourseElectiveDAO;
import edu.dufe.oes.bean.Evaluation;
import edu.dufe.oes.bean.EvaluationDAO;
import edu.dufe.oes.bean.Group;
import edu.dufe.oes.bean.GroupDAO;
import edu.dufe.oes.bean.GroupMember;
import edu.dufe.oes.bean.GroupMemberDAO;
import edu.dufe.oes.bean.GroupTemplate;
import edu.dufe.oes.bean.GroupTemplateDAO;
import edu.dufe.oes.bean.LeaveDAO;
import edu.dufe.oes.bean.LessonDAO;
import edu.dufe.oes.bean.NoticeDAO;
import edu.dufe.oes.bean.SemesterDAO;
import edu.dufe.oes.bean.Student;
import edu.dufe.oes.bean.StudentDAO;
import edu.dufe.oes.bean.SysConfigDAO;
import edu.dufe.oes.bean.Teacher;
import edu.dufe.oes.bean.User;
import edu.dufe.oes.config.CommonValues;
import edu.dufe.oes.util.MySetUtil;

public class GroupService extends CommonService{
	CourseElectiveDAO courseElectiveDAO=new CourseElectiveDAO();
	CourseDAO courseDAO=new CourseDAO();
	SysConfigDAO sysConfigDAO=new SysConfigDAO();
	LessonDAO lessonDAO=new LessonDAO();
	LeaveDAO leaveDAO=new LeaveDAO();
	SemesterDAO semesterDAO=new SemesterDAO();
	NoticeDAO noticeDAO=new NoticeDAO();
	GroupDAO groupDAO=new GroupDAO();
	GroupTemplateDAO groupTemplateDAO=new GroupTemplateDAO();
	GroupMemberDAO groupMemberDAO=new GroupMemberDAO();
	EvaluationDAO evaluationDAO=new EvaluationDAO();
	SemesterService semesterService=new SemesterService();
	UserInfoService userInfoService=new UserInfoService();
	
	
	public JSONObject getGroupTemplateInfo(int courseID,String token) throws Exception{
		JSONObject jsonObject=new JSONObject();
		clearSession(groupTemplateDAO);
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teacherList=new ArrayList<Teacher>(user.getTeachers());
		if (teacherList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会打你的");
		}
		Teacher teacher=teacherList.get(0);
		Course course=courseDAO.findById(courseID);
		if (course==null) {
			return addSuccessStatus(jsonObject, false, "无效课程");
		}
		Set<GroupTemplate> groupTemplates=course.getGroupTemplates();
		if (groupTemplates.isEmpty()) {
			jsonObject.put("templateList", new JSONArray());
			return addSuccessStatus(jsonObject, true, "");
		}
		List<GroupTemplate> groupTemplateList=new ArrayList<GroupTemplate>(groupTemplates);
		Collections.sort(groupTemplateList);
		int totalGroupNumber=0;
		List<Integer> groupBackupIDList=new ArrayList<Integer>();
		int tempBackupID=0;
		JSONArray templateArray=new JSONArray();
		JSONArray groupArray=new JSONArray();
		JSONObject templateObject=new JSONObject();
		for (GroupTemplate groupTemplate : groupTemplateList) {
			if (!groupBackupIDList.contains(groupTemplate.getGroupBackupID())) {
				groupBackupIDList.add(groupTemplate.getGroupBackupID());
			}
		}
		int i=0;
		for (Integer integer : groupBackupIDList) {
			GroupTemplate lastGroupTemplate=null;
			int j=0;
			for (GroupTemplate groupTemplate : groupTemplateList) {
				if (groupTemplate.getGroupBackupID()==integer) {
					JSONObject groupObject=new JSONObject();
					groupObject.put(GroupTemplateDAO.GROUP_TEMPLATEID, groupTemplate.getGroupTemplateID());
					groupObject.put(GroupDAO.GROUP_NUMBER, groupTemplate.getGroup().getGroupNumber());
					JSONArray memberArray=new JSONArray();
					List<GroupMember> memberList=new ArrayList<GroupMember>(groupTemplate.getGroup().getGroupMembers());
					for (GroupMember groupMember : memberList) {
						JSONObject memberObject=new JSONObject();
						memberObject.put(StudentDAO.STUDENT_ID, groupMember.getStudent().getStudentid());
						memberObject.put("studentName", groupMember.getStudent().getUser().getRealName());
						memberArray.put(memberObject);
					}
					groupObject.put("memberList", memberArray);
					groupArray.put(j,groupObject);
					lastGroupTemplate=groupTemplate;
					j++;
				}
			}
			templateObject.put("groupList", groupArray);
			templateObject.put(GroupTemplateDAO.GROUP_TEMPLATE_NAME, lastGroupTemplate.getGroupTemplateName());
			templateArray.put(i,templateObject);
			templateObject=new JSONObject();
			groupArray=new JSONArray();
			i++;
		}
	jsonObject.put("templateList", templateArray);
	return addSuccessStatus(jsonObject, true, "");
	}//{templateList:[{templateName,groupList:[{groupTemplateID,groupNumber,memberList:[{studentID,studentName}]}]}]}
	
	public JSONObject addGroupTemplate(int evaluationID,String groupTemplateName,String token) throws Exception{
		JSONObject jsonObject=new JSONObject();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teacherList=new ArrayList<Teacher>(user.getTeachers());
		if (teacherList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会打你的");
		}
		Teacher teacher=teacherList.get(0);
		Evaluation evaluation=evaluationDAO.findById(evaluationID);
		if (evaluation==null) {
			return addSuccessStatus(jsonObject, true, "无效评价项");
		}
		if (evaluation.getIsGroupEvaluation()==false) {
			return addSuccessStatus(jsonObject, true, "非分组评价");
		}
		List<Group> groupList=new ArrayList<Group>(evaluation.getGroups());
		GroupTemplate lastGroupTemplateRecode = getLastGroupTemplateRecode();
		int backupID=0;
		if (lastGroupTemplateRecode!=null) {
			backupID=lastGroupTemplateRecode.getGroupBackupID();
		}
		for (Group group : groupList) {
			GroupTemplate groupTemplate=new GroupTemplate();
			groupTemplate.setGroup(group);
			groupTemplate.setBackupTime(new Timestamp(System.currentTimeMillis()));
			groupTemplate.setCourse(evaluation.getLesson().getCourse());
			groupTemplate.setGroupBackupID(backupID+1);
			groupTemplate.setGroupTemplateName(groupTemplateName);
			groupTemplateDAO.save(groupTemplate);
		}
		return addSuccessStatus(jsonObject, true, "");
	}//{succes}
	
	public GroupTemplate getLastGroupTemplateRecode(){
		Query query=groupTemplateDAO.getSession().createQuery("from GroupTemplate order by groupTemplateID desc");
		List list=query.list();
		if (list.size()<=0) {
			return null;
		}
		return (GroupTemplate) list.get(0);
		
	}
	
	public List<GroupTemplate> getGroupsByGroupTemplateID(int templateID){
		GroupTemplate groupTemplate = groupTemplateDAO.findById(templateID);
		List list = groupTemplateDAO.findByGroupBackupID(groupTemplate.getGroupBackupID());
		return list;
	}
	
	public JSONObject getGroupMemberInfo(int evaluationID,String token) throws Exception{
		JSONObject jsonObject=new JSONObject();
		clearSession(groupMemberDAO);
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teacherList=new ArrayList<Teacher>(user.getTeachers());
		if (teacherList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会打你的");
		}
		Teacher teacher=teacherList.get(0);
		Evaluation evaluation=evaluationDAO.findById(evaluationID);
		if (evaluation==null) {
			return addSuccessStatus(jsonObject, true, "无效评价项");
		}
		if (evaluation.getIsGroupEvaluation()==false) {
			return addSuccessStatus(jsonObject, true, "非分组评价");
		}
		List<Group> groupList=new ArrayList<Group>(evaluation.getGroups());
		JSONArray groupArray=new JSONArray();
		boolean isGroupTemplate=false;
		Collections.sort(groupList);
		int i=0;
		for (Group group : groupList) {
			JSONObject groupObject=new JSONObject();
			groupObject.put(GroupDAO.GROUP_ID, group.getGroupID());
			groupObject.put(GroupDAO.GROUP_NUMBER, group.getGroupNumber());
			List<GroupMember> groupMemberList=new ArrayList<GroupMember>(group.getGroupMembers());
			Collections.sort(groupMemberList);
			JSONArray groupMemberArray=new JSONArray();
			int j=0;
			for (GroupMember groupMember : groupMemberList) {
				JSONObject groupMemberObject=new JSONObject();
				groupMemberObject.put(StudentDAO.STUDENT_ID, groupMember.getStudent().getStudentid());
				groupMemberObject.put("studentName", groupMember.getStudent().getUser().getRealName());
				groupMemberArray.put(j,groupMemberObject);
				j++;
			}
			groupObject.put("groupMemberList", groupMemberArray);
			groupArray.put(i,groupObject);
			i++;
		}
		jsonObject.put("groupList", groupArray);
		return addSuccessStatus(jsonObject, true, "");
	}//{groupList:[{groupID,groupNumber,groupMemberList:[{studentID,studentName}]}]}
	
	public JSONObject getGroupInfo(int evaluationID, String token) throws Exception {
		clearSession(groupDAO);
		// TODO Auto-generated method stub
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Student> studentList=new ArrayList<Student>(user.getStudents());
		if (studentList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "谢谢，别瞎闹");
		}
		Student student=studentList.get(0);
		Evaluation evaluation = evaluationDAO.findById(evaluationID);
		List<Group> groupList = new ArrayList<Group>(evaluation.getGroups());
		//还没有设置分组
		if(groupList.size()<=0){
			addSuccessStatus(jsonObject, false, "非分组评价");
		}
		JSONArray groupArray=new JSONArray();
		JSONObject myGroupObject=new JSONObject();
		Collections.sort(groupList);
		int i=0;
		for (Group group : groupList) {
			boolean isMyGroup=false;
			JSONObject otherGroupObject=new JSONObject();
			//得到组号；1，2，3，4……这个循环就是为了找到user的所在组别；
			List<GroupMember> groupMemberList = new ArrayList<GroupMember>(group.getGroupMembers());
			Collections.sort(groupMemberList);
			int j=0;
			for (GroupMember groupMember : groupMemberList) {
				Student member = groupMember.getStudent();
				if(member.equals(student)){
					myGroupObject.put(GroupDAO.GROUP_NUMBER, group.getGroupNumber());
					myGroupObject.put(GroupDAO.GROUP_ID, group.getGroupID());
					groupArray.put(j,myGroupObject);
					j++;
					isMyGroup=true;
					break;
				}
			}
			if(isMyGroup)continue;//如果查到自己的组别，就在otherGroup就不用加了
			otherGroupObject.put(GroupDAO.GROUP_ID, group.getGroupID());
			otherGroupObject.put(GroupDAO.GROUP_NUMBER, group.getGroupNumber());
			groupArray.put(i,otherGroupObject);
			i++;
		}
		
		jsonObject.put("allGroupInfo", groupArray);
		jsonObject.put("yourGroupInfo", myGroupObject);
		return addSuccessStatus(jsonObject, true, "");
	}

	public JSONObject addGroup(String token, int groupID) throws Exception{
		clearSession(groupDAO);
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Student> studentList=new ArrayList<Student>(user.getStudents());
		if (studentList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "谢谢，别瞎闹");
		}
		Student student=studentList.get(0);
		Group group = groupDAO.findById(groupID);
		if (group==null) {
			return addSuccessStatus(jsonObject, false, "无效组");
		}
		if (group.getGroupMembers().contains(student)) {
			return addSuccessStatus(jsonObject, false, "已加入该组");
		}
		boolean hasCourse=false;
		List<CourseElective> courseElectiveList=new ArrayList<CourseElective>(student.getCourseElectives());
		for (CourseElective courseElective : courseElectiveList) {
			if (courseElective.getCourse().equals(group.getEvaluation().getLesson().getCourse())&&courseElective.getElectiveStatus()==CourseElective.APPROVED) {
				hasCourse=true;
				break;
			}
		}
		if (hasCourse==false) {
			return addSuccessStatus(jsonObject, false, "未选该课");
		}
		
		Evaluation evaluation=group.getEvaluation();
		Set<Group> groups=evaluation.getGroups();
		Iterator<Group> groupIterator=groups.iterator();
		while (groupIterator.hasNext()) {
			Group otherGroup=groupDAO.findById(groupIterator.next().getGroupID());
			if (otherGroup.equals(group)) {
				continue;
			}
			Query query=groupMemberDAO.getSession().createQuery("from GroupMember where groupID=?");
			query.setInteger(0, otherGroup.getGroupID());
			List<GroupMember> groupMembers=query.list();
			for (GroupMember groupMember : groupMembers) {
				if (groupMember.getStudent().equals(student)) {
					return addSuccessStatus(jsonObject, false, "已加入第"+otherGroup.getGroupNumber()+"组");
				}
			}
		}
		GroupMember groupMember=new GroupMember();
		groupMember.setGroup(group);
		groupMember.setStudent(student);
		groupMember.setAddTime(new Timestamp(System.currentTimeMillis()));
		groupMemberDAO.save(groupMember);
		return addSuccessStatus(jsonObject, true, "");
	}
}

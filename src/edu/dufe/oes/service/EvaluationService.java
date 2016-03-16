package edu.dufe.oes.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.dufe.oes.bean.Course;
import edu.dufe.oes.bean.CourseDAO;
import edu.dufe.oes.bean.CourseElective;
import edu.dufe.oes.bean.CourseElectiveDAO;
import edu.dufe.oes.bean.Evaluation;
import edu.dufe.oes.bean.EvaluationDAO;
import edu.dufe.oes.bean.EvaluationField;
import edu.dufe.oes.bean.EvaluationFieldDAO;
import edu.dufe.oes.bean.EvaluationReceiver;
import edu.dufe.oes.bean.EvaluationReceiverDAO;
import edu.dufe.oes.bean.EvaluationResult;
import edu.dufe.oes.bean.EvaluationResultDAO;
import edu.dufe.oes.bean.EvaluationTemplate;
import edu.dufe.oes.bean.EvaluationTemplateDAO;
import edu.dufe.oes.bean.Group;
import edu.dufe.oes.bean.GroupDAO;
import edu.dufe.oes.bean.GroupMember;
import edu.dufe.oes.bean.GroupMemberDAO;
import edu.dufe.oes.bean.GroupTemplate;
import edu.dufe.oes.bean.GroupTemplateDAO;
import edu.dufe.oes.bean.LeaveDAO;
import edu.dufe.oes.bean.Lesson;
import edu.dufe.oes.bean.LessonDAO;
import edu.dufe.oes.bean.NoticeDAO;
import edu.dufe.oes.bean.OptionTitle;
import edu.dufe.oes.bean.OptionTitleDAO;
import edu.dufe.oes.bean.ResultType;
import edu.dufe.oes.bean.ResultTypeDAO;
import edu.dufe.oes.bean.SemesterDAO;
import edu.dufe.oes.bean.Student;
import edu.dufe.oes.bean.StudentDAO;
import edu.dufe.oes.bean.SysConfigDAO;
import edu.dufe.oes.bean.Teacher;
import edu.dufe.oes.bean.User;
import edu.dufe.oes.config.CommonValues;
import edu.dufe.oes.util.MySetUtil;
import edu.dufe.oes.util.MyStringUtil;

public class EvaluationService extends CommonService {
	CourseElectiveDAO courseElectiveDAO=new CourseElectiveDAO();
	CourseDAO courseDAO=new CourseDAO();
	SysConfigDAO sysConfigDAO=new SysConfigDAO();
	LessonDAO lessonDAO=new LessonDAO();
	LeaveDAO leaveDAO=new LeaveDAO();
	SemesterDAO semesterDAO=new SemesterDAO();
	NoticeDAO noticeDAO=new NoticeDAO();
	EvaluationDAO evaluationDAO=new EvaluationDAO();
	GroupDAO groupDAO=new GroupDAO();
	GroupMemberDAO groupMemberDAO=new GroupMemberDAO();
	GroupTemplateDAO groupTemplateDAO=new GroupTemplateDAO();
	ResultTypeDAO resultTypeDAO=new ResultTypeDAO();
	EvaluationFieldDAO evaluationFieldDAO=new EvaluationFieldDAO();
	OptionTitleDAO optionTitleDAO=new OptionTitleDAO();
	EvaluationTemplateDAO evaluationTemplateDAO=new EvaluationTemplateDAO();
	EvaluationReceiverDAO evaluationReceiverDAO=new EvaluationReceiverDAO();
	SemesterService semesterService=new SemesterService();
	UserInfoService userInfoService=new UserInfoService();
	GroupService groupService=new GroupService();
	
	
	public JSONObject getEvaluationTemplateInfo(String token,int courseID) throws Exception{
		JSONObject jsonObject=new JSONObject();
		clearSession(groupTemplateDAO);
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
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
		List evaluationTemplateList=MySetUtil.toList(course.getEvaluationTemplates());
		JSONArray evaluationTemplateArray=new JSONArray();
		JSONArray etArray=new JSONArray();
		for (Object etObject : evaluationTemplateList) {
			EvaluationTemplate evaluationTemplate=(EvaluationTemplate) etObject;
			JSONObject evaluationTemplateObject=new JSONObject();
			evaluationTemplateObject.put(EvaluationTemplateDAO.EVALUATION_TEMPLATE_NAME, evaluationTemplate.getEvaluationTemplateName());
			JSONArray evaluationFieldArray=new JSONArray();
			JSONObject evaluationFieldList=new JSONObject();
			List fieldList=MySetUtil.toList(evaluationTemplate.getEvaluation().getEvaluationFields());
			for (Object fobject : fieldList) {
				EvaluationField evaluationField=(EvaluationField) fobject;
				JSONObject evaluationFieldObject=new JSONObject();
				evaluationFieldObject.put(EvaluationFieldDAO.FIELD_CONTENT, evaluationField.getFieldContent());
				evaluationFieldObject.put("resultType", evaluationField.getResultType().getResultTypeID());
				if (evaluationField.getResultType().getResultTypeID()==ResultType.SINGLE_OPTION_RESULT||evaluationField.getResultType().getResultTypeID()==ResultType.MULTIPLE_OPTION_RESULT) {
					JSONArray optionTitleArray=new JSONArray();
					List optionTitleList=MySetUtil.toList(evaluationField.getOptionTitles());
					for (Object otObject : optionTitleList) {
						OptionTitle optionTitle=(OptionTitle) otObject;
						JSONObject optionTitleObject=new JSONObject();
						optionTitleObject.put(OptionTitleDAO.OPTION_TITLE_CONTENT, optionTitle.getOptionTitleContent());
						optionTitleObject.put(OptionTitleDAO.OPTION_KEY, optionTitle.getOptionKey());
						optionTitleArray.put(optionTitleObject);
					}
					evaluationFieldObject.put("optionTitleList", optionTitleArray);
				}
				evaluationFieldArray.put(evaluationFieldObject);
			}
			evaluationTemplateObject.put("evaluationFieldList", evaluationFieldArray);
			evaluationTemplateObject.put("evaluationTemplateID", evaluationTemplate.getEvaluationTemplateID());
			evaluationTemplateArray.put(evaluationTemplateObject);
		}
		jsonObject.put("evaluationTemplateList", evaluationTemplateArray);
		return addSuccessStatus(jsonObject, true, "");
	}//{evaluationTemplatList:[{evaluationTemplateID,evaluationTemplateName,evaluationFieldList:[{fieldContent,resultType,optionTitleList:[{optionTitleContent,optionKey}]}]}]}
	
	
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
	public JSONObject addEvaluation(int courseID,int lessonID,String token,String evaluationTitle,boolean isGroup,int groupNumber,int groupTemplateID,boolean saveAsEvaluationTemplate,String evaluationTemplateName,String evaluationFieldList) throws Exception{
		clearSession(evaluationDAO);
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
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
		Lesson lesson = lessonDAO.findById(lessonID);
		if (!course.equals(lesson.getCourse())) {
			return addSuccessStatus(jsonObject, false, "非本节次");
		}
		Evaluation evaluation=new Evaluation();
		evaluation.setEvaluationStatus(Evaluation.BEFORE);
		evaluation.setLesson(lesson);
		evaluation.setIsGroupEvaluation(isGroup);
		evaluation.setEvaluationTime(new Timestamp(System.currentTimeMillis()));
		evaluation.setEvaluationTitle(evaluationTitle);
		Transaction transaction = evaluationDAO.getSession().beginTransaction();
		evaluationDAO.save(evaluation);
		//分组情况
		if (isGroup==true) {
			if (groupNumber<=0&&groupTemplateID!=0) {
				List<GroupTemplate> groupTemplates = groupService.getGroupsByGroupTemplateID(groupTemplateID);
				if (groupTemplates.size()<=0) {
					evaluationDAO.getSession().close();
					return addSuccessStatus(jsonObject, false, "无效组模板");
				}
				int tmpGroupNumber=1;
				for (GroupTemplate groupTemplate : groupTemplates) {
					Group group=new Group();
					group.setGroupNumber(tmpGroupNumber);
					group.setEvaluation(evaluation);
					groupDAO.save(group);
					EvaluationReceiver evaluationReceiver=new EvaluationReceiver();
					evaluationReceiver.setEvaluation(evaluation);
					evaluationReceiver.setEvaluationGrade(0);
					evaluationReceiver.setReceiverType(EvaluationReceiver.GROUP_RECEIVER);
					evaluationReceiver.setGroup(group);
					evaluationReceiverDAO.save(evaluationReceiver);
					List groupMemberList=MySetUtil.toList(groupTemplate.getGroup().getGroupMembers());
					for (Object gmObject : groupMemberList) {
						GroupMember tmpGroupMember=(GroupMember) gmObject;
						GroupMember groupMember=new GroupMember();
						
						groupMember.setStudent(tmpGroupMember.getStudent());
						groupMember.setGroup(group);
						groupMember.setAddTime(new Timestamp(System.currentTimeMillis()));
						groupMemberDAO.save(groupMember);
						
					}
					tmpGroupNumber++;
				}
			}else if (groupNumber>0&&groupTemplateID==0) {
				for (int i = 1; i <= groupNumber; i++) {
					Group group=new Group();
					group.setGroupNumber(i);
					group.setEvaluation(evaluation);
					groupDAO.save(group);
					EvaluationReceiver evaluationReceiver=new EvaluationReceiver();
					evaluationReceiver.setEvaluation(evaluation);
					evaluationReceiver.setEvaluationGrade(0);
					evaluationReceiver.setGroup(group);
					evaluationReceiver.setReceiverType(EvaluationReceiver.GROUP_RECEIVER);
					evaluationReceiverDAO.save(evaluationReceiver);
				}
			}else {
				evaluationDAO.getSession().close();
				return addSuccessStatus(jsonObject, false, "无效分组设置");
			}
		}
		JSONArray evaluationFieldArray=new JSONArray(evaluationFieldList);
		//字段情况
		if (evaluationFieldArray.length()==0) {
			return addSuccessStatus(jsonObject, false, "未设置评价项");
		}
		for (int i = 0; i < evaluationFieldArray.length(); i++) {
			JSONObject fieldObject=evaluationFieldArray.getJSONObject(i);
			EvaluationField evaluationField=new EvaluationField();
			evaluationField.setEvaluation(evaluation);
			evaluationField.setFieldContent(MyStringUtil.trimAndEscapeSql(fieldObject.getString("fieldContent")));
			ResultType resultType=resultTypeDAO.findById(fieldObject.getInt("resultType"));
			if (resultType==null) {
				evaluationDAO.getSession().close();
				return addSuccessStatus(jsonObject, false, "无效评价模式");
			}
			evaluationField.setResultType(resultType);
			evaluationFieldDAO.save(evaluationField);
			if (resultType.getResultTypeID()==ResultType.SINGLE_OPTION_RESULT||resultType.getResultTypeID()==ResultType.MULTIPLE_OPTION_RESULT) {
				JSONArray optionTitleArray=fieldObject.getJSONArray("optionTitleList");
				for (int j = 0; j < optionTitleArray.length(); j++) {
					JSONObject optionTitleObject=optionTitleArray.getJSONObject(j);
					OptionTitle optionTitle=new OptionTitle();
					optionTitle.setEvaluationField(evaluationField);
					optionTitle.setOptionKey(MyStringUtil.trimAndEscapeSql(optionTitleObject.getString("optionKey")));
					optionTitle.setOptionTitleContent(MyStringUtil.trimAndEscapeSql(optionTitleObject.getString("optionTitleContent")));
					optionTitleDAO.save(optionTitle);
				}
			}
		}
		//评价模板情况
		if (saveAsEvaluationTemplate==true) {
			EvaluationTemplate evaluationTemplate=new EvaluationTemplate();
			evaluationTemplate.setBackupTime(new Timestamp(System.currentTimeMillis()));
			evaluationTemplate.setEvaluation(evaluation);
			evaluationTemplate.setCourse(course);
			evaluationTemplate.setEvaluationTemplateName(evaluationTemplateName);
			evaluationTemplateDAO.save(evaluationTemplate);
		}
		transaction.commit();
		jsonObject.put(EvaluationDAO.EVALUATION_ID, evaluation.getEvaluationID());
		return addSuccessStatus(jsonObject, true, "");
	}//output:{success,evaluationID}||//input:{evaluationFieldList:[{fieldContent,resultType,optionTitleList:[{optionTitleContent,optionKey}]}]}
	
	public JSONObject deleteEvaluation(int evaluationID,String token) throws Exception{
		clearSession(evaluationDAO);
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
		if (teachertList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Teacher teacher=(Teacher) teachertList.get(0);
		Evaluation evaluation=evaluationDAO.findById(evaluationID);
		if (!evaluation.getLesson().getCourse().getTeacher().equals(teacher)) {
			return addSuccessStatus(jsonObject, false, "非本人评价");
		}
		if (evaluation.getEvaluationStatus()>Evaluation.BEFORE) {
			return addSuccessStatus(jsonObject, false, "评价已开始");
		}
		Transaction transaction = evaluationDAO.getSession().beginTransaction();
		evaluationDAO.delete(evaluation);
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
	}//{success,reason}
	
	public JSONObject setEvaluationStatus(int evaluationID,int evaluationStatus,String token) throws Exception{
		clearSession(evaluationDAO);
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
		if (teachertList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Teacher teacher=(Teacher) teachertList.get(0);
		Evaluation evaluation=evaluationDAO.findById(evaluationID);
		if (evaluation==null) {
			return addSuccessStatus(jsonObject, false, "无效评价");
		}
		if (evaluationStatus<0||evaluationStatus>1) {
			return addSuccessStatus(jsonObject, false, "无效状态");
		}
		
		if (!teacher.getCourses().contains(evaluation.getLesson().getCourse())) {
			return addSuccessStatus(jsonObject, false, "非本人课程");
		}
		if (evaluation.getEvaluationStatus()==Evaluation.BEFORE&&evaluationStatus==Evaluation.AFTER) {
			return addSuccessStatus(jsonObject, false, "无效状态");
		}
		if (evaluation.getEvaluationStatus()==evaluationStatus) {
			return addSuccessStatus(jsonObject, false, "状态未改变");
		}
		Lesson lesson=evaluation.getLesson();
		List<Evaluation> evaluations=new ArrayList<Evaluation>(lesson.getEvaluations());
		boolean hasNotFinished=false;
		for (Evaluation evaluation2 : evaluations) {
			if (evaluation2.equals(evaluation)) {
				continue;
			}
			if (evaluation2.getEvaluationStatus()<Evaluation.AFTER) {
				hasNotFinished=true;
			}
		}
		Transaction transaction = evaluationDAO.getSession().beginTransaction();
		if (evaluationStatus==Evaluation.DOING) {
			lesson.setLessonStatus(Lesson.DOING);
		}else if (evaluationStatus==Evaluation.AFTER) {
			if (!hasNotFinished) {
				lesson.setLessonStatus(Lesson.AFTER);
			}
		}
		lessonDAO.getSession().update(lesson);
		evaluation.setEvaluationStatus(evaluationStatus);
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
	}//{success,reason}
	
	
	public JSONObject getEvaluationGrade(int receiverID,String token) throws Exception{
		JSONObject jsonObject=new JSONObject();
		clearSession(evaluationReceiverDAO);
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
		if (teachertList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Teacher teacher=(Teacher) teachertList.get(0);
		EvaluationReceiver evaluationReceiver = evaluationReceiverDAO.findById(receiverID);
		if (evaluationReceiver==null) {
			return addSuccessStatus(jsonObject, false, "无效被评价者");
		}
		jsonObject.put("receiverID", evaluationReceiver.getEvaluationReceiverID());
		jsonObject.put("receiverType", evaluationReceiver.getReceiverType());
		if (evaluationReceiver.getReceiverType()==EvaluationReceiver.GROUP_RECEIVER) {
			jsonObject.put("groupNumber", evaluationReceiver.getGroup().getGroupNumber());
		}else if (evaluationReceiver.getReceiverType()==EvaluationReceiver.PERSON_RECEIVER) {
			jsonObject.put("receiverName", evaluationReceiver.getStudent().getUser().getRealName());
		}
		jsonObject.put("evaluationGrade", evaluationReceiver.getEvaluationGrade());
		return addSuccessStatus(jsonObject, true, "");
	}//{evaluationGrade:{receiverID,receiverType,receiverName,evaluationGrade}}
	
	public JSONObject updateEvalutionGrade(String token,int receiverID,int evaluationGrade) throws Exception{
		clearSession(evaluationDAO);
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
		if (teachertList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Teacher teacher=(Teacher) teachertList.get(0);
		EvaluationReceiver evaluationReceiver=evaluationReceiverDAO.findById(receiverID);
		if (evaluationReceiver==null) {
			evaluationReceiverDAO.getSession().close();
			return addSuccessStatus(jsonObject, false, "无效被评价者");
		}
		if (!teacher.getCourses().contains(evaluationReceiver.getEvaluation().getLesson().getCourse())) {
			evaluationReceiverDAO.getSession().close();
			return addSuccessStatus(jsonObject, false, "非本课程被评价者");
		}
		if (evaluationReceiver.getEvaluation().getEvaluationStatus()==Evaluation.BEFORE) {
			evaluationReceiverDAO.getSession().close();
			return addSuccessStatus(jsonObject, false, "评价还未开始");
		}
		if (evaluationGrade>100||evaluationGrade<0) {
			evaluationReceiverDAO.getSession().close();
			return addSuccessStatus(jsonObject, false, "想改成绩？");
		}
		Transaction transaction = evaluationReceiverDAO.getSession().beginTransaction();
		evaluationReceiver.setEvaluationGrade(evaluationGrade);
		evaluationReceiverDAO.getSession().update(evaluationReceiver);
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
	}//input:{evaluationGradeList:[{receiverID,evaluationGrade}]}
	
	public JSONObject getEvaluationListByLesson(int lessonID, String token) throws Exception {
		// TODO Auto-generated method stub
		clearSession(evaluationDAO);
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		Lesson lesson = lessonDAO.findById(lessonID);
		//判断又没此课节；或者是lesson还没有开
		if(lesson==null){
			addSuccessStatus(jsonObject, false, "无效节次");
		}
		List<Teacher> teachers = new ArrayList<Teacher>(user.getTeachers());
		List<Student> students =new ArrayList<Student>(user.getStudents());
		if(students.size()<=0&&teachers.size()<=0){
			return addSuccessStatus(jsonObject, false,"老师要打人了");
		}
		Teacher teacher=null;
		Student student=null;
		if (teachers.size()>0) {
			teacher=teachers.get(0);
		}else if (students.size()>0) {
			student=students.get(0);
		}else {
			return addSuccessStatus(jsonObject, false,"老师要打人了");
		}
		List<Evaluation> evaluationList=new ArrayList<Evaluation>(lesson.getEvaluations());
		JSONArray evaluationArray=new JSONArray();
		Collections.sort(evaluationList);
		int i=0;
		for (Evaluation evaluation : evaluationList) {
			JSONObject evaluationObject = new JSONObject();
			evaluationObject.put(EvaluationDAO.EVALUATION_ID, 
					evaluation.getEvaluationID());
			evaluationObject.put(EvaluationDAO.EVALUATION_TITLE, 
					evaluation.getEvaluationTitle());
			evaluationObject.put(EvaluationDAO.EVALUATION_STATUS, 
					evaluation.getEvaluationStatus());
			evaluationObject.put(EvaluationDAO.IS_GROUP_EVALUATION,
					evaluation.getIsGroupEvaluation());
			boolean isReceiver=false;
			if (student!=null&&evaluation.getIsGroupEvaluation()==false) {
				Set<EvaluationReceiver> evaluationReceivers = evaluation.getEvaluationReceivers();
				Iterator<EvaluationReceiver> iterator=evaluationReceivers.iterator();
				while (iterator.hasNext()) {
					if (iterator.next().getStudent().equals(student)) {
						isReceiver=true;
						break;
					}
				}
			}
			evaluationObject.put("isReceiver", isReceiver);
			evaluationArray.put(i,evaluationObject);
			i++;
		}
		jsonObject.put("evaluationList", evaluationArray);
		return addSuccessStatus(jsonObject, true, "");
	}
	/**
	 * 
	 * @param token
	 * @param evaluationID
	 * @return
	 * @throws Exception
	 */
	public JSONObject setMeAsReceiver(String token, int evaluationID) throws Exception {
		clearSession(evaluationReceiverDAO);
		// TODO Auto-generated method stub
		JSONObject jsonObject=new JSONObject();
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Student> studentList=new ArrayList<Student>(user.getStudents());
		if (studentList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Student student=(Student) studentList.get(0);
		//得到evaluation；但是需要判断evaluationStatus有没有开放；
		Evaluation evaluation = evaluationDAO.findById(evaluationID);
		if(evaluation==null){
			return addSuccessStatus(jsonObject, false, "无效评价");
		}
		Lesson lesson=evaluation.getLesson();
		Course course = lesson.getCourse();
		List<CourseElective> courseElectives=new ArrayList<CourseElective>(student.getCourseElectives());
		boolean hasCourse=false;
		for (CourseElective courseElective : courseElectives) {
			if (courseElective.getCourse().equals(course)) {
				hasCourse=true;
				break;
			}
		}
		if (hasCourse==false) {
			return addSuccessStatus(jsonObject, false, "未选该课");
		}
		if (evaluation.getIsGroupEvaluation()==true) {
			return addSuccessStatus(jsonObject, false, "该评价为分组评价");
		}
		List<EvaluationReceiver> evaluationReceiverList=new ArrayList<EvaluationReceiver>(evaluation.getEvaluationReceivers());
		for (EvaluationReceiver evaluationReceiver : evaluationReceiverList) {
			if (evaluationReceiver.getStudent().equals(student)) {
				return addSuccessStatus(jsonObject, false, "已是被评价者"); 
			}
		}
		if(evaluation.getEvaluationStatus()>Evaluation.DOING)
		{
			return addSuccessStatus(jsonObject, false, "评价已经结束");
		}
		EvaluationReceiver evaluationReceiver=new EvaluationReceiver();
		evaluationReceiver.setStudent(student);
		evaluationReceiver.setEvaluation(evaluation);
		evaluationReceiver.setReceiverType(EvaluationReceiver.PERSON_RECEIVER);
		evaluationReceiver.setEvaluationGrade(0);
		evaluationReceiverDAO.save(evaluationReceiver);
		return addSuccessStatus(jsonObject, true, "");
	}
	
	public JSONObject getEvaluationStatus(int evaluationID, String token) throws Exception {
		clearSession(evaluationDAO);
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		Evaluation evaluation = evaluationDAO.findById(evaluationID);
		jsonObject.put(EvaluationDAO.EVALUATION_ID, evaluation.getEvaluationID());
		jsonObject.put(EvaluationDAO.EVALUATION_STATUS, evaluation.getEvaluationStatus());
		return addSuccessStatus(jsonObject, true, "");
	}
	
	public JSONObject studentGetReceiverList(int evaluationID, String token) throws Exception {
		JSONObject jsonObject=new JSONObject();
		clearSession(evaluationReceiverDAO);
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Student> studentList=new ArrayList<Student>(user.getStudents());
		if (studentList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "无效学生");
		}
		Student student=studentList.get(0);
		Evaluation evaluation = evaluationDAO.findById(evaluationID);
		Lesson lesson=evaluation.getLesson();
		Course course = lesson.getCourse();
		List<CourseElective> courseElectives=new ArrayList<CourseElective>(student.getCourseElectives());
		boolean hasCourse=false;
		for (CourseElective courseElective : courseElectives) {
			if (courseElective.getCourse().equals(course)) {
				hasCourse=true;
				break;
			}
		}
		if (hasCourse==false) {
			return addSuccessStatus(jsonObject, false, "未选该课");
		}
		JSONArray receiverArray=new JSONArray();
		//false说明是不分组，找student
		ArrayList<EvaluationReceiver> receiverlist=new ArrayList<EvaluationReceiver>(
				evaluation.getEvaluationReceivers());
		Collections.sort(receiverlist);
		int i=0;
		for (EvaluationReceiver evaluationReceiver : receiverlist) {
			List<EvaluationResult> evaluationResultList=new ArrayList<EvaluationResult>(evaluationReceiver.getEvaluationResults());
			int receiverStatus=EvaluationReceiver.NOTEVALUATED;
			for (EvaluationResult evaluationResult : evaluationResultList) {
				if (evaluationResult.getStudent().equals(student)) {
					receiverStatus=EvaluationReceiver.EVALUATED;
					break;
				}
			}
			JSONObject receiverObject = new JSONObject();
			if (evaluation.getIsGroupEvaluation()) {
				boolean isMyGroup=false;
				Set<GroupMember> groupMembers = evaluationReceiver.getGroup().getGroupMembers();
				if (!groupMembers.isEmpty()) {
					Iterator<GroupMember> iterator = groupMembers.iterator();
					while (iterator.hasNext()) {
						if (iterator.next().getStudent().equals(student)) {
							isMyGroup=true;
						}
					}
				}
				receiverObject.put("isMyGroup", isMyGroup);
				receiverObject.put("evaluationReceiverID", evaluationReceiver.getEvaluationReceiverID());
				receiverObject.put(GroupDAO.GROUP_ID, evaluationReceiver.getGroup().getGroupID());
				receiverObject.put(GroupDAO.GROUP_NUMBER,evaluationReceiver.getGroup().getGroupNumber());
				receiverObject.put("receiverStatus", receiverStatus);
				receiverArray.put(i,receiverObject);
			}else {
				boolean isMe=false;
				if (evaluationReceiver.getStudent().equals(student)) {
					isMe=true;
				}
				receiverObject.put("isMe", isMe);
				receiverObject.put("evaluationReceiverID", evaluationReceiver.getEvaluationReceiverID());
				receiverObject.put("studentName", evaluationReceiver.getStudent().getUser().getRealName());
				receiverObject.put(StudentDAO.STUDENT_ID, evaluationReceiver.getStudent().getStudentid());
				receiverObject.put("receiverStatus", receiverStatus);
				receiverArray.put(i,receiverObject);
			}
			i++;
		}
		jsonObject.put("receiverList", receiverArray);
		if (evaluation.getIsGroupEvaluation()) {
			jsonObject.put("receiverType",EvaluationReceiver.GROUP_RECEIVER);
		}else {
			jsonObject.put("receiverType",EvaluationReceiver.PERSON_RECEIVER);
		}
		return addSuccessStatus(jsonObject, true, "");
	}

}

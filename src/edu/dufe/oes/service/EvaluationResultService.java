package edu.dufe.oes.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.javafx.collections.MappingChange.Map;

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
import edu.dufe.oes.bean.EvaluationTemplateDAO;
import edu.dufe.oes.bean.Group;
import edu.dufe.oes.bean.GroupDAO;
import edu.dufe.oes.bean.GroupMember;
import edu.dufe.oes.bean.GroupMemberDAO;
import edu.dufe.oes.bean.GroupTemplateDAO;
import edu.dufe.oes.bean.Leave;
import edu.dufe.oes.bean.LeaveDAO;
import edu.dufe.oes.bean.LessonDAO;
import edu.dufe.oes.bean.MultipleOptionResult;
import edu.dufe.oes.bean.MultipleOptionResultDAO;
import edu.dufe.oes.bean.NoticeDAO;
import edu.dufe.oes.bean.OptionTitle;
import edu.dufe.oes.bean.OptionTitleDAO;
import edu.dufe.oes.bean.ResultType;
import edu.dufe.oes.bean.ResultTypeDAO;
import edu.dufe.oes.bean.SemesterDAO;
import edu.dufe.oes.bean.SingleOptionResult;
import edu.dufe.oes.bean.SingleOptionResultDAO;
import edu.dufe.oes.bean.Student;
import edu.dufe.oes.bean.StudentDAO;
import edu.dufe.oes.bean.SysConfigDAO;
import edu.dufe.oes.bean.Teacher;
import edu.dufe.oes.bean.TextResult;
import edu.dufe.oes.bean.TextResultDAO;
import edu.dufe.oes.bean.User;
import edu.dufe.oes.bean.UserDAO;
import edu.dufe.oes.bean.ValueResult;
import edu.dufe.oes.bean.ValueResultDAO;
import edu.dufe.oes.config.CommonValues;
import edu.dufe.oes.util.MySetUtil;
import edu.dufe.oes.util.MyStringUtil;

public class EvaluationResultService extends CommonService {

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
	EvaluationResultDAO evaluationResultDAO=new EvaluationResultDAO();
	ValueResultDAO valueResultDAO=new ValueResultDAO();
	SingleOptionResultDAO singleOptionResultDAO=new SingleOptionResultDAO();
	MultipleOptionResultDAO multipleOptionResultDAO=new MultipleOptionResultDAO();
	TextResultDAO textResultDAO=new TextResultDAO();
	EvaluationReceiverDAO evaluationReceiverDAO=new EvaluationReceiverDAO();
	UserDAO userDAO=new UserDAO();
	StudentDAO studentDAO=new StudentDAO();
	SemesterService semesterService=new SemesterService();
	UserInfoService userInfoService=new UserInfoService();
	GroupService groupService=new GroupService();
	
	public JSONObject teacherGetReceiverList(int evaluationID,String token) throws Exception{
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
		Teacher teacher=teachertList.get(0);
		Evaluation evaluation=evaluationDAO.findById(evaluationID);
		if (evaluation==null) {
			return addSuccessStatus(jsonObject, false, "无效评价");
		}
		int receiverType=-1;
		
		if (evaluation.getIsGroupEvaluation()==false) {
			receiverType=EvaluationReceiver.PERSON_RECEIVER;
			List<EvaluationReceiver> receiverList=new ArrayList<EvaluationReceiver>(evaluation.getEvaluationReceivers());
			JSONArray receiverArray=new JSONArray();
			Collections.sort(receiverList);
			int i=0;
			for (EvaluationReceiver evaluationReceiver : receiverList) {
				JSONObject receiverObject=new JSONObject();
				receiverObject.put("receiverID", evaluationReceiver.getEvaluationReceiverID());
				receiverObject.put("studentName", evaluationReceiver.getStudent().getUser().getRealName());
				receiverArray.put(i,receiverObject);
				i++;
			}
			jsonObject.put("receiverType", receiverType);
			jsonObject.put("receiverList", receiverArray);
		}else {
			List<EvaluationReceiver> receiverList=new ArrayList<EvaluationReceiver>(evaluation.getEvaluationReceivers());
			JSONArray receiverArray=new JSONArray();
			receiverType=EvaluationReceiver.GROUP_RECEIVER;
			Collections.sort(receiverList);
			int i=0;
			for (EvaluationReceiver evaluationReceiver : receiverList) {
				JSONObject receiverObject=new JSONObject();
				receiverObject.put("receiverID", evaluationReceiver.getEvaluationReceiverID());
				receiverObject.put(GroupDAO.GROUP_NUMBER, evaluationReceiver.getGroup().getGroupNumber());
				receiverArray.put(i,receiverObject);
				i++;
			}
			jsonObject.put("receiverType", receiverType);
			jsonObject.put("receiverList", receiverArray);
		}
		return addSuccessStatus(jsonObject, true, "");
	}//{receiverType:pserson/group,receiverList:[{id,number/name}]}
	
	public JSONObject teacherGetResult(int evaluationID,int receiverID,String token) throws Exception{
		JSONObject jsonObject=new JSONObject();
		clearSession(evaluationResultDAO);
		User user=userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Teacher> teachertList=new ArrayList<Teacher>(user.getTeachers());
		if (teachertList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Teacher teacher=teachertList.get(0);
		Evaluation evaluation=evaluationDAO.findById(evaluationID);
		if (evaluation==null) {
			return addSuccessStatus(jsonObject, false, "无效评价");
		}
		EvaluationReceiver evaluationReceiver = evaluationReceiverDAO.findById(receiverID);
		if (evaluationReceiver==null) {
			return addSuccessStatus(jsonObject, false, "无效被评价者");
		}
		
		List<EvaluationField> evaluationFieldList=new ArrayList<EvaluationField>(evaluation.getEvaluationFields());
		JSONArray evaluatingStudentArray=new JSONArray();
		JSONArray evaluationResultArray=new JSONArray();
		ArrayList<Student> totalStudents=new ArrayList<Student>();
		Set<Student> evaluatingStudentSet=new HashSet<Student>();
		List<CourseElective> courseElectives=new ArrayList<CourseElective>(evaluation.getLesson().getCourse().getCourseElectives());
		List<Leave> leaves=new ArrayList<Leave>(evaluation.getLesson().getLeaves());
		for (CourseElective courseElective : courseElectives) {
			if (courseElective.getElectiveStatus()==courseElective.APPROVED) {
				boolean isLeaveStudent=false;
				for (Leave leave : leaves) {
					if (leave.getStudent().equals(courseElective.getStudent())&&leave.getApproveStatus()==Leave.APPROVED) {
						isLeaveStudent=true;
						break;
					}
				}
				if (!isLeaveStudent) {
					totalStudents.add(courseElective.getStudent());
				}
			}
		}
		
		boolean hasStatistic=false;
		Collections.sort(evaluationFieldList);
		for (EvaluationField evaluationField : evaluationFieldList) {
			JSONObject evaluationResultObject=new JSONObject();
			evaluationResultObject.put("evaluationResultType", evaluationField.getResultType().getResultTypeID());
			evaluationResultObject.put(EvaluationFieldDAO.FIELD_CONTENT, evaluationField.getFieldContent());
			//这个是全部的
			List<EvaluationResult> tmpEvaluationResultList=new ArrayList<EvaluationResult>(evaluationField.getEvaluationResults());
			//这个list是receiver的
			List<EvaluationResult> evaluationResultList=new ArrayList<EvaluationResult>();
			for (EvaluationResult evaluationResult : tmpEvaluationResultList) {
				if (evaluationResult.getEvaluationReceiver().equals(evaluationReceiver)) {
					evaluationResultList.add(evaluationResult);
				}
			}
			
			if (!hasStatistic) {
				ArrayList<Student> evaluatedStudentList=new ArrayList<Student>();
				for (EvaluationResult evaluationResult : evaluationResultList) {
					evaluatedStudentList.add(evaluationResult.getStudent());
				}
				for (Student student : totalStudents) {
					if (!evaluatedStudentList.contains(student)) {
						if (!evaluatingStudentSet.contains(student)) {
							evaluatingStudentSet.add(student);
						}
						
					}
				}
				Iterator<Student> iterator = evaluatingStudentSet.iterator();
				while (iterator.hasNext()) {
					JSONObject studentObject=new JSONObject();
					studentObject.put("studentName", iterator.next().getUser().getRealName());
					evaluatingStudentArray.put(studentObject);
				}
				hasStatistic=true;
			}
			
			ResultType resultType = evaluationField.getResultType();
			switch (resultType.getResultTypeID()) {
//			case ResultType.VALUE_RESULT:
//				JSONObject valueResultObject=new JSONObject();
//				valueResultObject.put("evaluationResultType", ResultType.VALUE_RESULT);
//				valueResultObject.put("evaluationFieldContent", evaluationField.getFieldContent());
//				JSONObject valueObject=new JSONObject();
//				List<ValueResult> valueList=new ArrayList<ValueResult>();
//				for (EvaluationResult evaluationResult : evaluationResultList) {
//					if (evaluationResult.getValueResults().isEmpty()) {
//						continue;
//					}
//					valueList.add(new ArrayList<ValueResult>(evaluationResult.getValueResults()).get(0));
//				}
//				double[] valueResult=getValueStatistic(valueList);
//				valueObject.put("maxValue", valueResult[0]);
//				valueObject.put("minValue", valueResult[1]);
//				valueObject.put("averageValue", valueResult[2]);
//				valueObject.put("evaluatedStudentNumber", valueResult[3]);
//				valueResultObject.put("valueResult", valueObject);
//				evaluationResultArray.put(valueResultObject);
//				break;
//				
//			case ResultType.SINGLE_OPTION_RESULT:
//				JSONObject singleOptionResultObject=new JSONObject();
//				singleOptionResultObject.put("evaluationResultType", ResultType.SINGLE_OPTION_RESULT);
//				singleOptionResultObject.put("evaluationFieldContent", evaluationField.getFieldContent());
//				JSONArray singleOptionReslutArray=new JSONArray();
//				List<SingleOptionResult> singleOptionResultList=new ArrayList<SingleOptionResult>();
//				for (EvaluationResult evaluationResult : evaluationResultList) {
//					if (evaluationResult.getValueResults().isEmpty()) {
//						continue;
//					}
//					singleOptionResultList.add(new ArrayList<SingleOptionResult>(evaluationResult.getSingleOptionResults()).get(0));
//				}
//				double singleTotalEvaluatedStudentNumber=singleOptionResultList.size();
//				for (OptionTitle optionTitle : new ArrayList<OptionTitle>(evaluationField.getOptionTitles())) {
//					JSONObject singleOptionObject=new JSONObject();
//					singleOptionObject.put(OptionTitleDAO.OPTION_KEY, optionTitle.getOptionKey());
//					singleOptionObject.put(OptionTitleDAO.OPTION_TITLE_CONTENT, optionTitle.getOptionTitleContent());
//					double selectedNumber=0;
//					for (SingleOptionResult singleOptionResult : singleOptionResultList) {
//						if (singleOptionResult.getOptionValue().equals(optionTitle.getOptionKey())) {
//							selectedNumber++;
//						}
//					}
//					singleOptionObject.put("selectedNumber", selectedNumber);
//					singleOptionObject.put("selectedPercentage", (selectedNumber/singleTotalEvaluatedStudentNumber)*100);
//					singleOptionReslutArray.put(singleOptionObject);
//				}
//				singleOptionResultObject.put("singleOptionResult", singleOptionReslutArray);
//				evaluationResultArray.put(singleOptionResultObject);
//				break;
//			case ResultType.MULTIPLE_OPTION_RESULT:
//				JSONObject multipleOptionResultObject=new JSONObject();
//				multipleOptionResultObject.put("evaluationFieldType", ResultType.SINGLE_OPTION_RESULT);
//				multipleOptionResultObject.put("evaluationFieldContent", evaluationField.getFieldContent());
//				JSONArray multipleOptionReslutArray=new JSONArray();
//				List<MultipleOptionResult> multipleOptionResultList=new ArrayList<MultipleOptionResult>();
//				for (EvaluationResult evaluationResult : evaluationResultList) {
//					if (evaluationResult.getValueResults().isEmpty()) {
//						continue;
//					}
//					multipleOptionResultList.add(new ArrayList<MultipleOptionResult>(evaluationResult.getMultipleOptionResults()).get(0));
//				}
//				double multipleTotalEvaluatedStudentNumber=multipleOptionResultList.size();
//				int index=0;
//				for (OptionTitle optionTitle : new ArrayList<OptionTitle>(evaluationField.getOptionTitles())) {
//					JSONObject multipleOptionObject=new JSONObject();
//					multipleOptionObject.put(OptionTitleDAO.OPTION_KEY, optionTitle.getOptionKey());
//					multipleOptionObject.put(OptionTitleDAO.OPTION_TITLE_CONTENT, optionTitle.getOptionTitleContent());
//					double selectedNumber=0;
//					for (MultipleOptionResult multipleOptionResult : multipleOptionResultList) {
//						if (multipleOptionResult.getOptionValue().charAt(index)==new OptionTitle().getOptionKey().charAt(0)) {
//							selectedNumber++;
//						}
//					}
//					multipleOptionObject.put("selectedNumber", selectedNumber);
//					multipleOptionReslutArray.put(multipleOptionObject);
//				}
//				multipleOptionResultObject.put("multipleOptionResult", multipleOptionResultObject);
//				evaluationResultArray.put(multipleOptionReslutArray);
//				break;
//			case ResultType.TEXT_RESULT:
//				JSONObject textResultObject=new JSONObject();
//				textResultObject.put("evaluationFieldType", ResultType.TEXT_RESULT);
//				textResultObject.put("evaluationFieldContent", evaluationField.getFieldContent());
//				JSONArray textReslutArray=new JSONArray();
//				List<TextResult> textResultList=new ArrayList<TextResult>();
//				for (EvaluationResult evaluationResult : evaluationResultList) {
//					if (evaluationResult.getValueResults().isEmpty()) {
//						continue;
//					}
//					textResultList.add(new ArrayList<TextResult>(evaluationResult.getTextResults()).get(0));
//				}
//				for (TextResult textResult : textResultList) {
//					JSONObject textObject=new JSONObject();
//					textObject.put("text", textResult.getResultText());
//					textReslutArray.put(textObject);
//				}
//				textResultObject.put("textResult", textReslutArray);
//				evaluationResultArray.put(textResultObject);
//				break;
//			default:
//				break;
//			}
			case ResultType.VALUE_RESULT:
				JSONObject valueResultObject=new JSONObject();
				List<ValueResult> valueResults=new ArrayList<ValueResult>();
				for (EvaluationResult evaluationResult : evaluationResultList) {
					if (evaluationResult.getValueResults().isEmpty()) {
						continue;
					}
					ValueResult valueResult=(new ArrayList<ValueResult>(evaluationResult.getValueResults())).get(0);
					valueResults.add(valueResult);
				}
				double[] valueResult=getValueStatistic(valueResults);
				valueResultObject.put("maxValue", valueResult[0]);
				valueResultObject.put("minValue", valueResult[1]);
				valueResultObject.put("averageValue", valueResult[2]);
				valueResultObject.put("evaluatedStudentNumber", valueResult[3]);
				evaluationResultObject.put("valueResult", valueResultObject);
				break;
			
			case ResultType.SINGLE_OPTION_RESULT:
				JSONArray singleOptionResultArray=new JSONArray();
				List<SingleOptionResult> singleOptionResultList=new ArrayList<SingleOptionResult>();
				for (EvaluationResult evaluationResult : evaluationResultList) {
					if (evaluationResult.getSingleOptionResults().isEmpty()) {
						continue;
					}
					singleOptionResultList.add(new ArrayList<SingleOptionResult>(evaluationResult.getSingleOptionResults()).get(0));
				}
				double singleTotalEvaluatedStudentNumber=singleOptionResultList.size();
				List<OptionTitle> soptionTitleList=new ArrayList<OptionTitle>(evaluationField.getOptionTitles());
				Collections.sort(soptionTitleList);
				int i=0;
				for (OptionTitle optionTitle : soptionTitleList) {
					JSONObject singleOptionObject=new JSONObject();
					singleOptionObject.put(OptionTitleDAO.OPTION_KEY, optionTitle.getOptionKey());
					singleOptionObject.put(OptionTitleDAO.OPTION_TITLE_CONTENT, optionTitle.getOptionTitleContent());
					double selectedNumber=0;
					for (SingleOptionResult singleOptionResult : singleOptionResultList) {
						if (singleOptionResult.getOptionValue().equals(optionTitle.getOptionKey())) {
							selectedNumber++;
						}
					}
					
					double selectedPercentage=0;
					if (singleTotalEvaluatedStudentNumber!=0) {
						selectedPercentage=selectedNumber/singleTotalEvaluatedStudentNumber;
					}
					singleOptionObject.put("selectedNumber", selectedNumber);
					singleOptionObject.put("selectedPercentage", (selectedPercentage)*100);
					singleOptionResultArray.put(i,singleOptionObject);
					i++;
				}
				evaluationResultObject.put("singleOptionResult", singleOptionResultArray);
				break;
			
			case ResultType.MULTIPLE_OPTION_RESULT:
				JSONArray multipleOptionResultArray=new JSONArray();
				List<MultipleOptionResult> multipleOptionResultList=new ArrayList<MultipleOptionResult>();
				for (EvaluationResult evaluationResult : evaluationResultList) {
					if (evaluationResult.getMultipleOptionResults().isEmpty()) {
						continue;
					}
					multipleOptionResultList.add(new ArrayList<MultipleOptionResult>(evaluationResult.getMultipleOptionResults()).get(0));
				}
				double multipleTotalEvaluatedStudentNumber=multipleOptionResultList.size();
				List<OptionTitle> moptionTitleList=new ArrayList<OptionTitle>(evaluationField.getOptionTitles());
				Collections.sort(moptionTitleList);
				int j=0;
				for (OptionTitle optionTitle : moptionTitleList) {
					JSONObject multipleOptionObject=new JSONObject();
					multipleOptionObject.put(OptionTitleDAO.OPTION_KEY, optionTitle.getOptionKey());
					multipleOptionObject.put(OptionTitleDAO.OPTION_TITLE_CONTENT, optionTitle.getOptionTitleContent());
					double selectedNumber=0;
					for (MultipleOptionResult multipleOptionResult : multipleOptionResultList) {
						if (multipleOptionResult.getOptionValue().contains(optionTitle.getOptionKey())) {
							selectedNumber++;
						}
					}
					multipleOptionObject.put("selectedNumber", selectedNumber);
					multipleOptionResultArray.put(j,multipleOptionObject);
					j++;
				}
				evaluationResultObject.put("multipleOptionResult", multipleOptionResultArray);
				break;
				
			case ResultType.TEXT_RESULT: 
				JSONArray textResultArray=new JSONArray();
				for (EvaluationResult evaluationResult : evaluationResultList) {
					JSONObject textResultObject=new JSONObject();
					if (evaluationResult.getTextResults().isEmpty()) {
						continue;
					}
					textResultObject.put("text", new ArrayList<TextResult>(evaluationResult.getTextResults()).get(0).getResultText());
					textResultArray.put(textResultObject);
				}
				evaluationResultObject.put("textResult", textResultArray);
				break;
			default:
				break;
			}
			evaluationResultArray.put(evaluationResultObject);
		}
		jsonObject.put("evaluatingStudentList", evaluatingStudentArray);
		jsonObject.put("resultList", evaluationResultArray);
		return addSuccessStatus(jsonObject, true, "");
		
	}//return:{evaluatingStudentList:[{studentName}],resultList:[{evaluationReslutType,evaluationFieldContent,valueResult:{maxValue,minValue,averageValue,evaluatedStudentNumber}},{evaluationReslutType,evaluationFieldContent,singleOptionResult:[{optionKey,optionTitleContent,selectedNumber,selectedPercentage}]},{evaluationReslutType,evaluationFieldContent,multipleOptionResult:[{optionKey,optionTitleContent,selectedNumber}]},{evaluationReslutType,evaluationFieldContent,textResult:[{text}]}]}
	
	
	
	
	
	public JSONObject studentGetResult(String token,int receiverID) throws Exception{
		JSONObject jsonObject=new JSONObject();
		clearSession(evaluationResultDAO);
		JSONArray evaluationResultArray=new JSONArray();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Student> studentList=new ArrayList<Student>(user.getStudents());
		if (studentList.size()<=0) {
			return addSuccessStatus(jsonObject, false, "你再这样老师会找你的");
		}
		Student me=studentList.get(0);
		
		boolean isMe=false;
		boolean isGroupEvaluation=false;
		Group groupReceiver=null;
		Student studentReceiver=null;
		EvaluationReceiver evaluationReceiver = evaluationReceiverDAO.findById(receiverID);
		if (evaluationReceiver==null) {
			return addSuccessStatus(jsonObject, false, "无效被评价者");
		}
		
		if (evaluationReceiver.getReceiverType()==EvaluationReceiver.GROUP_RECEIVER) {
			isGroupEvaluation=true;
			groupReceiver=evaluationReceiver.getGroup();
			for (GroupMember groupMember : new ArrayList<GroupMember>(groupReceiver.getGroupMembers())) {
				if (groupMember.getStudent().equals(me)) {
					isMe=true;
					break;
				}
			}
		}else if (evaluationReceiver.getReceiverType()==EvaluationReceiver.PERSON_RECEIVER) {
			isGroupEvaluation=false;
			studentReceiver=evaluationReceiver.getStudent();
			if (me.equals(studentReceiver)) {
				isMe=true;
			}
		}
		
		Evaluation evaluation=evaluationReceiver.getEvaluation();
		List<EvaluationField> evaluationFieldList=new ArrayList<EvaluationField>(evaluation.getEvaluationFields());
		Collections.sort(evaluationFieldList);
		for (EvaluationField evaluationField : evaluationFieldList) {
			JSONObject evaluationResultObject=new JSONObject();
			evaluationResultObject.put("evaluationResultType", evaluationField.getResultType().getResultTypeName());
			evaluationResultObject.put("fieldType", evaluationField.getResultType().getResultTypeID());
			evaluationResultObject.put(EvaluationFieldDAO.FIELD_CONTENT, evaluationField.getFieldContent());
			List<EvaluationResult> tmpEvaluationResultList=new ArrayList<EvaluationResult>(evaluationField.getEvaluationResults());
			List<EvaluationResult> evaluationResultList=new ArrayList<EvaluationResult>();
			for (EvaluationResult evaluationResult : tmpEvaluationResultList) {
				if (isMe) {
					if (isGroupEvaluation) {
						if (evaluationResult.getEvaluationReceiver().getGroup().equals(groupReceiver)) {
							evaluationResultList.add(evaluationResult);
						}
					}else {
						if (evaluationResult.getEvaluationReceiver().getStudent().equals(studentReceiver)) {
							evaluationResultList.add(evaluationResult);
						}
					}
				}else {
					if (isGroupEvaluation) {
						if (evaluationResult.getStudent().equals(me)&&evaluationResult.getEvaluationReceiver().getGroup().equals(groupReceiver)) {
							evaluationResultList.add(evaluationResult);
							break;
						}
					}else {
						if (evaluationResult.getStudent().equals(me)&&evaluationResult.getEvaluationReceiver().getStudent().equals(studentReceiver)) {
							evaluationResultList.add(evaluationResult);
							break;
						}
					}
				}
			}
			switch (evaluationField.getResultType().getResultTypeID()) {
			case ResultType.VALUE_RESULT:
				JSONObject valueResultObject=new JSONObject();
				List<ValueResult> valueResults=new ArrayList<ValueResult>();
				for (EvaluationResult evaluationResult : evaluationResultList) {
					if (evaluationResult.getValueResults().isEmpty()) {
						continue;
					}
					ValueResult valueResult=(new ArrayList<ValueResult>(evaluationResult.getValueResults())).get(0);
					valueResults.add(valueResult);
				}
				double[] valueResult=getValueStatistic(valueResults);
				valueResultObject.put("maxValue", valueResult[0]);
				valueResultObject.put("minValue", valueResult[1]);
				valueResultObject.put("averageValue", valueResult[2]);
				valueResultObject.put("evaluatedStudentNumber", valueResult[3]);
				evaluationResultObject.put("valueResult", valueResultObject);
				break;
			case ResultType.SINGLE_OPTION_RESULT:
				JSONArray singleOptionResultArray=new JSONArray();
				List<SingleOptionResult> singleOptionResultList=new ArrayList<SingleOptionResult>();
				for (EvaluationResult evaluationResult : evaluationResultList) {
					if (evaluationResult.getSingleOptionResults().isEmpty()) {
						continue;
					}
					singleOptionResultList.add(new ArrayList<SingleOptionResult>(evaluationResult.getSingleOptionResults()).get(0));
				}
				double singleTotalEvaluatedStudentNumber=singleOptionResultList.size();
				List<OptionTitle> sOptionTitles=new ArrayList<OptionTitle>(new ArrayList<OptionTitle>(evaluationField.getOptionTitles()));
				Collections.sort(sOptionTitles);
				int i=0;
				for (OptionTitle optionTitle : sOptionTitles) {
					JSONObject singleOptionObject=new JSONObject();
					singleOptionObject.put(OptionTitleDAO.OPTION_KEY, optionTitle.getOptionKey());
					singleOptionObject.put(OptionTitleDAO.OPTION_TITLE_CONTENT, optionTitle.getOptionTitleContent());
					double selectedNumber=0;
					for (SingleOptionResult singleOptionResult : singleOptionResultList) {
						if (singleOptionResult.getOptionValue().equals(optionTitle.getOptionKey())) {
							selectedNumber++;
						}
					}
					double selectedPercentage=0;
					if (singleTotalEvaluatedStudentNumber!=0) {
						selectedPercentage=selectedNumber/singleTotalEvaluatedStudentNumber;
					}
					singleOptionObject.put("selectedNumber", selectedNumber);
					singleOptionObject.put("selectedPercentage", selectedPercentage*100);
					singleOptionResultArray.put(i,singleOptionObject);
					i++;
				}
				evaluationResultObject.put("singleOptionResult", singleOptionResultArray);
				break;
			
			case ResultType.MULTIPLE_OPTION_RESULT:
				JSONArray multipleOptionResultArray=new JSONArray();
				List<MultipleOptionResult> multipleOptionResultList=new ArrayList<MultipleOptionResult>();
				for (EvaluationResult evaluationResult : evaluationResultList) {
					if (evaluationResult.getMultipleOptionResults().isEmpty()) {
						continue;
					}
					multipleOptionResultList.add(new ArrayList<MultipleOptionResult>(evaluationResult.getMultipleOptionResults()).get(0));
				}
				double multipleTotalEvaluatedStudentNumber=multipleOptionResultList.size();
				List<OptionTitle> moptionTitles=new ArrayList<OptionTitle>(new ArrayList<OptionTitle>(evaluationField.getOptionTitles()));
				Collections.sort(moptionTitles);
				int j=0;
				for (OptionTitle optionTitle : moptionTitles) {
					JSONObject multipleOptionObject=new JSONObject();
					multipleOptionObject.put(OptionTitleDAO.OPTION_KEY, optionTitle.getOptionKey());
					multipleOptionObject.put(OptionTitleDAO.OPTION_TITLE_CONTENT, optionTitle.getOptionTitleContent());
					double selectedNumber=0;
					for (MultipleOptionResult multipleOptionResult : multipleOptionResultList) {
						if (multipleOptionResult.getOptionValue().contains(optionTitle.getOptionKey())) {
							selectedNumber++;
						}
					}
					multipleOptionObject.put("selectedNumber", selectedNumber);
					multipleOptionResultArray.put(j,multipleOptionObject);
					j++;
				}
				evaluationResultObject.put("multipleOptionResult", multipleOptionResultArray);
				break;
			case ResultType.TEXT_RESULT: 
				JSONArray textResultArray=new JSONArray();
				for (EvaluationResult evaluationResult : evaluationResultList) {
					JSONObject textResultObject=new JSONObject();
					if (evaluationResult.getTextResults().isEmpty()) {
						continue;
					}
					textResultObject.put("text", new ArrayList<TextResult>(evaluationResult.getTextResults()).get(0).getResultText());
					textResultArray.put(textResultObject);
				}
				evaluationResultObject.put("textResult", textResultArray);
				break;
			default:
				break;
			}
			evaluationResultArray.put(evaluationResultObject);
		}
		jsonObject.put("resultList", evaluationResultArray);
		return addSuccessStatus(jsonObject, true, "");
	}//{resultList:[{evaluationResultType,evaluationFieldContent,valueResult:{maxValue,minValue,averageValue,evaluatedStudentNumber}},{evaluationResultType,evaluationFieldContent,singleOptionResult:[{optionKey,optionTitleContent,selectedNumber,selectedPercentage}]},{evaluationResultType,evaluationFieldContent,multipleOptionResult:[{optionKey,optionTitleContent,selectedNumber}]},{evaluationResultType,evaluationFieldContent,textResult:[{text}]}]}
	

	public JSONObject getEvaluationFieldList(int evaluationID, String token) throws Exception {
		// TODO Auto-generated method stub
		JSONObject jsonObject=new JSONObject();
		clearSession(evaluationResultDAO);
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		Evaluation evaluation = evaluationDAO.findById(evaluationID);
		if(evaluation==null){
			return addSuccessStatus(jsonObject, false, "无效评价项");
		}
		List<EvaluationField> evaluationFieldList=new ArrayList<EvaluationField>(evaluation.getEvaluationFields());
		JSONArray evaluationFieldArray=new JSONArray();
		Collections.sort(evaluationFieldList);
		int i=0;
		for (EvaluationField evaluationField : evaluationFieldList) {
			JSONObject evaluationFieldObject=new JSONObject();
			evaluationFieldObject.put(EvaluationFieldDAO.FIELD_CONTENT, evaluationField.getFieldContent());
			evaluationFieldObject.put(EvaluationFieldDAO.EVALUATIONFIELD_ID, evaluationField.getEvaluationFieldID());
			evaluationFieldObject.put(ResultTypeDAO.RESULT_TYPE_ID,evaluationField.getResultType().getResultTypeID());
			evaluationFieldObject.put(ResultTypeDAO.RESULT_TYPE_NAME, evaluationField.getResultType().getResultTypeName());
			if (evaluationField.getResultType().getResultTypeID()==ResultType.SINGLE_OPTION_RESULT||evaluationField.getResultType().getResultTypeID()==ResultType.MULTIPLE_OPTION_RESULT) {
				JSONArray optionArray=new JSONArray();
				List<OptionTitle> optionTitleList=new ArrayList<OptionTitle>(evaluationField.getOptionTitles());
				Collections.sort(optionTitleList);
				int j=0;
				for (OptionTitle optionTitle : optionTitleList) {
					JSONObject optionObject=new JSONObject();
					optionObject.put(OptionTitleDAO.OPTION_KEY, optionTitle.getOptionKey());
					optionObject.put(OptionTitleDAO.OPTION_TITLE_CONTENT, optionTitle.getOptionTitleContent());
					optionArray.put(j,optionObject);
					j++;
				}
				evaluationFieldObject.put("optionTitleList", optionArray);
			}
			evaluationFieldArray.put(i,evaluationFieldObject);
			i++;
		}
		jsonObject.put("evaluationFieldList", evaluationFieldArray);
		jsonObject.put(EvaluationDAO.EVALUATION_ID, evaluationID);
		return addSuccessStatus(jsonObject, true, "");
	}

	//需要解析json格式；
	//incoming:{resultContent:[{resultTypeID,evaluationFieldID,resultcontent}]};
	//output:{success}
	public JSONObject studentSetResult(String token, String resultContentData,
			int senderID, int receiverID) throws Exception {
		System.out.println(senderID);
		clearSession(evaluationResultDAO);
		// TODO Auto-generated method stub
		JSONObject jsonObject=new JSONObject();
		User user = userInfoService.getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		//验证sender是否是这个token的
		User student=userDAO.findById(senderID);
		Set<Student> senders=student.getStudents();
		if (senders.isEmpty()) {
			return addSuccessStatus(jsonObject, false, "无效评价者");
		}
		Student sender=senders.iterator().next();
		if(!sender.getUser().equals(user)){
			return addSuccessStatus(jsonObject, false, "无效评价者");
		}
		EvaluationReceiver evaluationReceiver=evaluationReceiverDAO.findById(receiverID);
		if (evaluationReceiver==null) {
			return addSuccessStatus(jsonObject, false, "无效被评价者");
		}
		
		//把传过来的字符串 解析成json 先转换成jsonobject---->jsonArray；
		//getJSONArray（key）  中是key-value中的key值；利用key来找到value中的jsonArray；类似于map
		JSONArray resultContentJSONArray=new JSONArray(resultContentData);
		//传来的数据是否为空，
		if(resultContentJSONArray.length()<=0){
			return addSuccessStatus(jsonObject, false, "无效评价数据");
		}
		List<EvaluationField> evaluationFields=new ArrayList<EvaluationField>(evaluationReceiver.getEvaluation().getEvaluationFields());
		if (resultContentJSONArray.length()!=evaluationFields.size()) {
			return addSuccessStatus(jsonObject, false, "评价数据不完整");
		}
		Transaction transaction = evaluationResultDAO.getSession().beginTransaction();//更新/set必须要提交事务；
		for(int i=0;i<resultContentJSONArray.length();i++){
			JSONObject resultContentFieldObject=resultContentJSONArray.getJSONObject(i);
			//用来盛放每一个评价字段的，
			EvaluationResult evaluationResult=new EvaluationResult();
			Integer evaluationFieldID = (Integer) resultContentFieldObject.get("evaluationFieldID");
			EvaluationField evaluationField = evaluationFieldDAO.findById(evaluationFieldID);
			if (evaluationField==null) {
				evaluationResultDAO.getSession().close();
				return addSuccessStatus(jsonObject, false, "无效评价字段");
			}
			if (!evaluationField.getEvaluation().equals(evaluationReceiver.getEvaluation())) {
				evaluationResultDAO.getSession().close();
				return addSuccessStatus(jsonObject, false, "无效评价");
			}
			Evaluation evaluation=evaluationField.getEvaluation();
			if (evaluation.getEvaluationStatus()>Evaluation.DOING) {
				evaluationResultDAO.getSession().close();
				return addSuccessStatus(jsonObject, false, "评价已结束");
			}
			List<EvaluationResult> evaluationResults=new ArrayList<EvaluationResult>(evaluationField.getEvaluationResults());
			for (EvaluationResult tmpEvaluationResult : evaluationResults) {
				if (tmpEvaluationResult.getEvaluationReceiver().equals(evaluationReceiver)&&tmpEvaluationResult.getStudent().equals(sender)) {
					evaluationResultDAO.getSession().close();
					return addSuccessStatus(jsonObject, false, "已评价过该评价");
				}
			}
			if (evaluation.getIsGroupEvaluation()==false) {
				if (evaluationReceiver.getStudent().equals(sender)) {
					evaluationResultDAO.getSession().close();
					return addSuccessStatus(jsonObject, false, "不能给自己评价");
				}
			}else {
				List<GroupMember> groupMembers=new ArrayList<GroupMember>(evaluationReceiver.getGroup().getGroupMembers());
				for (GroupMember groupMember : groupMembers) {
					if (groupMember.getStudent().equals(sender)) {
						evaluationResultDAO.getSession().close();
						return addSuccessStatus(jsonObject, false, "不能给自己组评价");
					}
				}
				
			}
			
			//设置评价字段；
			evaluationResult.setEvaluationField(evaluationField);
			evaluationResult.setEvaluationReceiver(evaluationReceiver);
			evaluationResult.setStudent(sender);//设置发送者；
			evaluationResultDAO.save(evaluationResult);
			//switch语句，判断是单选/多选/文本……
			Integer resultTypeID=(Integer) resultContentFieldObject.get("resultTypeID");
			ResultType resultType = resultTypeDAO.findById(resultTypeID);
			if (resultType==null) {
				evaluationResultDAO.getSession().close();
				return addSuccessStatus(jsonObject, false, "无效结果类型");
			}
			switch (resultTypeID) {
			case ResultType.VALUE_RESULT:
				ValueResultDAO valueResultDAO=new ValueResultDAO();
				ValueResult valueResult=new ValueResult();
				valueResult.setResultValue(resultContentFieldObject.getInt("resultContent"));
				valueResult.setEvaluationResult(evaluationResult);
				valueResultDAO.save(valueResult);
				break;
			case ResultType.SINGLE_OPTION_RESULT:
				SingleOptionResultDAO singleOptionResultDAO=new SingleOptionResultDAO();
				SingleOptionResult singleOptionResult=new SingleOptionResult();
				singleOptionResult.setEvaluationResult(evaluationResult);
				singleOptionResult.setOptionValue(resultContentFieldObject.getString("resultContent"));
				singleOptionResultDAO.save(singleOptionResult);
				break;
			case ResultType.MULTIPLE_OPTION_RESULT:
				MultipleOptionResultDAO multipleOptionResultDAO=new MultipleOptionResultDAO();
				MultipleOptionResult multipleOptionResult=new MultipleOptionResult();
				multipleOptionResult.setEvaluationResult(evaluationResult);
				multipleOptionResult.setOptionValue(MyStringUtil.trimAndEscapeSql(resultContentFieldObject.getString("resultContent")));
				multipleOptionResultDAO.save(multipleOptionResult);
				break;
			default:
				TextResultDAO textResultDAO=new TextResultDAO();
				TextResult textResult=new TextResult();
				textResult.setEvaluationResult(evaluationResult);
				textResult.setResultText(MyStringUtil.trimAndEscapeSql(resultContentFieldObject.getString("resultContent")));
				textResultDAO.save(textResult);
				break;
			}
			
		}
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
	}
	
	
	
	private double[] getValueStatistic(List<ValueResult> valueResults){
		double maxValue=0;
		double minValue=100;
		double averageValue=0;
		double totalValue=0;
		double length=0;
		for (ValueResult valueResult : valueResults) {
			if (valueResult.getResultValue()>maxValue) {
				maxValue=valueResult.getResultValue();
			}
			if (valueResult.getResultValue()<minValue) {
				minValue=valueResult.getResultValue();
			}
			totalValue+=valueResult.getResultValue();
			length++;
		}
		if (length!=0) {
			averageValue=totalValue/length;
			BigDecimal bigDecimal=new BigDecimal(averageValue);
			averageValue=bigDecimal.setScale(2, BigDecimal.ROUND_UP).doubleValue();
		}else {
			minValue=0;
		}
		return new double[]{maxValue,minValue,averageValue,length};
	}
}

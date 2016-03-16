package edu.dufe.oes.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jasper.tagplugins.jstl.core.If;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.dufe.oes.bean.College;
import edu.dufe.oes.bean.CollegeDAO;
import edu.dufe.oes.bean.Message;
import edu.dufe.oes.bean.MessageDAO;
import edu.dufe.oes.bean.Semester;
import edu.dufe.oes.bean.Student;
import edu.dufe.oes.bean.StudentDAO;
import edu.dufe.oes.bean.Teacher;
import edu.dufe.oes.bean.TeacherDAO;
import edu.dufe.oes.bean.User;
import edu.dufe.oes.bean.UserDAO;
import edu.dufe.oes.config.CommonValues;
import edu.dufe.oes.config.HibernateSessionFactory;
import edu.dufe.oes.util.MySetUtil;
import edu.dufe.oes.util.MyStringUtil;

public class UserInfoService extends CommonService {
	//一旦要set/update的话就需要调用事务，进行commit，不然写不到数据库中；hiberbate

	private CollegeService collegeService=new CollegeService();
	private UserDAO userDAO=new UserDAO();
	private StudentDAO studentDAO=new StudentDAO();
	private TeacherDAO teacherDAO=new TeacherDAO();
	private MessageDAO messageDAO=new MessageDAO();
	
	SemesterService semesterService=new SemesterService();
	
	public JSONObject getMyInfoByToken(String token) throws Exception{
		clearSession(userDAO);
		JSONObject jsonObject=new JSONObject();
		User user=getUserByToken(token);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.NULLUSER_ERR);
		}
		jsonObject.put(UserDAO.USER_ID, user.getUserid());
		jsonObject.put(UserDAO.USERNAME, user.getUsername());
		jsonObject.put(CollegeDAO.COLLEGE_NAME,user.getCollege().getCollegeName());
		jsonObject.put(UserDAO.GENDER, user.getGender());
		jsonObject.put(UserDAO.EMAIL, user.getEmail());
		jsonObject.put(UserDAO.PHONE, user.getPhone());
		jsonObject.put(UserDAO.ROLE, user.getRole());
		jsonObject.put(UserDAO.REAL_NAME, user.getRealName());
		List<Student> studentList=new ArrayList<Student>(user.getStudents());
		if (studentList.size()>0) {
			Student student=studentList.get(0);
			jsonObject.put(StudentDAO.MAJOR, student.getMajor());
		}
		jsonObject.put("currentSemester", semesterService.getCurrentSemester().getSemesterName());
		JSONArray semesterArray=new JSONArray();
		List<Semester> allSemester = semesterService.getAllSemester();
		Collections.sort(allSemester);
		int i=0;
		for (Semester semester : allSemester) {
			JSONObject semesterObject=new JSONObject();
			semesterObject.put("semester", semester.getSemesterName());
			semesterArray.put(i,semesterObject);
			i++;
		}
		jsonObject.put("semesterList", semesterArray);
		return addSuccessStatus(jsonObject, true, "");
		
		
	}
	
	//检查是否是单一用户
	public JSONObject checkUniqueUser(String userName) throws Exception{
		clearSession(userDAO);
		JSONObject jsonObject=new JSONObject();
		List userList = userDAO.findByUsername(userName);
		if (userList.size()>0) {
			return addSuccessStatus(jsonObject, false, "重复用户");
		}
		return addSuccessStatus(jsonObject, true, "");
	}
	//注册
	public JSONObject register(String userName, String password, String question,
			String answer, String realName, int collegeID, int gender,
			String email, String phone, int role, String major) throws Exception{
		clearSession(userDAO);
		JSONObject jsonObject=new JSONObject();
		JSONObject checkUniqueUser=checkUniqueUser(userName);
		if(checkUniqueUser.getBoolean("success")==false){
			return checkUniqueUser;
		}
		
		User user=new User();
		user.setUsername(userName);
		user.setPlainPassword(password);
		String encryptPassword=MyStringUtil.toMD5(password);
		user.setEncryptPassword(encryptPassword);
		College college = collegeService.findCollegeByID(collegeID);
		if (college==null) {
			return addSuccessStatus(jsonObject, false, "无效学院");
		}
		if (role<0||role>1) {
			return addSuccessStatus(jsonObject, false, "无效角色");
		}
		if (gender<0||gender>1) {
			return addSuccessStatus(jsonObject, false, "无效性别");
		}
		user.setCollege(college);
		user.setPasswordQuestion(question);
		user.setPasswordAnswer(answer);
		user.setEmail(email);
		user.setPhone(phone);
		user.setRealName(realName);
		user.setRole(role);
		user.setGender(gender);
		user.setRegisterTime(new Timestamp(System.currentTimeMillis()));
		//token=用户名+当前时间+seed；
		String token=MyStringUtil.toMD5(userName+System.currentTimeMillis()+CommonValues.SEED);
		user.setToken(token);
		userDAO.save(user);
		//判断user角色
		if (role==User.STUDENT) {
			Student student=new Student();
			student.setMajor(major);
			student.setUser(user);
			studentDAO.save(student);
		}else if (role==User.TEACHER) {
			Teacher teacher=new Teacher();
			teacher.setApproveStatus(Teacher.APPROVING);
			teacher.setUser(user);
			teacherDAO.save(teacher);
		}
		addSuccessStatus(jsonObject, true, "");
		return jsonObject;
	}
	
	public JSONObject login(String userName,String password) throws Exception{
		clearSession(userDAO);
		JSONObject object=new JSONObject();
		List users = userDAO.findByUsername(userName);
		if (users.size()==0) {
			return addSuccessStatus(object, false, CommonValues.NULLUSER_ERR);
		}
		String encryptPassword=MyStringUtil.toMD5(password);
		Session session = HibernateSessionFactory.getSession();
		Query query=session.createQuery("from User where userName= :u and encryptPassword= :p");
		query.setString("u", userName);
		query.setString("p", encryptPassword);
		users=query.list();
		if (users.size()>0) {
			Transaction transaction = userDAO.getSession().beginTransaction();
			User user=(User) users.get(0);
			String token=MyStringUtil.toMD5(userName+System.currentTimeMillis()+CommonValues.SEED);
			user.setToken(token);
			user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
			addSuccessStatus(object, true, "");
			object.put("token", token);
			object.put("role", user.getRole());
			userDAO.getSession().update(user);
			transaction.commit();
			return object;
		}else {
			addSuccessStatus(object, false, "用户名或密码错误");
			return object;
		}
	}
	
	public JSONObject getUserQuestion(String userName) throws Exception{
		clearSession(userDAO);
		JSONObject jsonObject=new JSONObject();
		List<User> users = userDAO.findByUsername(userName);//用户有多个相同的username的话，只取第一个；
		if (users.size()==0) {
			return addSuccessStatus(jsonObject, false, CommonValues.NULLUSER_ERR);
		}else {
			User user=users.get(0);
			jsonObject.put("question", user.getPasswordQuestion());
			jsonObject.put("userID", user.getUserid());
			return addSuccessStatus(jsonObject, true, "");
		}
	}
	
	public JSONObject verifyAnswer(int userID, String answer) throws Exception{
		clearSession(userDAO);
		JSONObject jsonObject=new JSONObject();
		User user = userDAO.findById(userID);
		if (user==null) {
			return addSuccessStatus(jsonObject, false, CommonValues.NULLUSER_ERR);
		}
		if (user.getPasswordAnswer().equals(answer)) {
			jsonObject.put("token", user.getToken());
			return addSuccessStatus(jsonObject, true, "");
		}else {
			return addSuccessStatus(jsonObject, false, "答案错误");
		}
	}
	//重置密码；
	public JSONObject resetUserPassword(String password, String token) throws Exception {
		clearSession(userDAO);
		JSONObject jsonObject=new JSONObject();
		Transaction transaction = userDAO.getSession().beginTransaction();
		User user=getUserByToken(token);
		if (user==null) {
			userDAO.getSession().close();
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		user.setPlainPassword(password);
		user.setEncryptPassword(MyStringUtil.toMD5(password));
		userDAO.getSession().update(user);
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
	}
	
	
	//通过用户名查数据库 是否有这个用户对象；
	public  User getUserByUserName(String userName){
		List users = userDAO.findByUsername(userName);
		if (users.size()==0) {
			return null;
		}else {
			return (User) users.get(0);
		}
	}
	//通过token 
	public  User getUserByToken(String token){
		List users = userDAO.findByToken(token);
		if (users.size()==0) {
			return null;
		}else {
			return (User) users.get(0);
		}
	}
	//更新用户信息；
	public JSONObject updateUserInfo(String token, String realName,
			Integer collegeID, String email, int gender, String phone,
			String major) throws Exception {
		clearSession(userDAO);
		// TODO Auto-generated method stub
		JSONObject jsonObject=new JSONObject();
		Transaction transaction=new UserDAO().getSession().beginTransaction();
		College college=collegeService.findCollegeByID(collegeID);
		if (college==null) {
			userDAO.getSession().close();
			return addSuccessStatus(jsonObject, false,"无效学院");
		}
		User user=getUserByToken(token);
		if(user==null){
			userDAO.getSession().close();
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		user.setRealName(realName);
		user.setCollege(college);
		user.setEmail(email);
		user.setGender(gender);
		user.setPhone(phone);
		List<Student> studentList=new ArrayList<Student>(user.getStudents());
		if (studentList.size()>0) {
			Student student=studentList.get(0);
			student.setMajor(major);
		}
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
	}
	/*
	 * 验证密码；
	 */
	public JSONObject verifyPassword(String password, String token) throws Exception {
		clearSession(userDAO);
		// TODO Auto-generated method stub
		User user=getUserByToken(token);
		JSONObject jsonObject=new JSONObject();
		//判断user有几个；
		if(user==null){
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		//验证加密密码；
		String encryptPassword=MyStringUtil.toMD5(password);
		if(!user.getEncryptPassword().equals(encryptPassword)){
			return addSuccessStatus(jsonObject, false, "密码错误");
		}
		return addSuccessStatus(jsonObject, true, "");
	}
	
	public JSONObject updatePassword(String token, String password) throws Exception {
		clearSession(userDAO);
		// TODO Auto-generated method stub
		JSONObject jsonObject=new JSONObject();
		//一旦要set/update的话就需要调用事务，进行commit，不然写不到数据库中；
		Transaction transaction=userDAO.getSession().beginTransaction();
		
		User user = getUserByToken(token);
		if(user==null){
			userDAO.getSession().close();
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		user.setPlainPassword(password);
		user.setEncryptPassword(MyStringUtil.toMD5(password));
		String newToken=user.getUsername()+System.currentTimeMillis()+CommonValues.SEED;
		user.setToken(newToken);
		userDAO.getSession().update(user);
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
	}
	
	public JSONObject getUserInfo(String token) throws Exception{
		clearSession(userDAO);
		User user=getUserByToken(token);
		JSONObject userInfoObject=new JSONObject();
		if(user==null){
			addSuccessStatus(userInfoObject, false, "用户不存在");
		}
		userInfoObject.put("userName", user.getUsername());
		userInfoObject.put("realName", user.getRealName());
		userInfoObject.put("gender", user.getGender());
		userInfoObject.put("email", user.getEmail());
		userInfoObject.put("phone",user.getPhone());
		userInfoObject.put("role",user.getRole());
		//专业：college
		userInfoObject.put("collegeName", user.getCollege().getCollegeName());
		//学生
		JSONObject jsonObject=new JSONObject();
		if(user.getRole()==User.STUDENT){
			Set<Student> students = user.getStudents();//看学生有没有空；
			if(students.isEmpty()){
				return addSuccessStatus(jsonObject, false, "没有这个学生");
			}
			Student student=students.iterator().next();
			userInfoObject.put("major", student.getMajor());
			return addSuccessStatus(userInfoObject, true, "");
		}
		if(user.getRole()==User.TEACHER){
			Set<Teacher> teachers=user.getTeachers();
			if(teachers.isEmpty()){
				addSuccessStatus(jsonObject, false, "没有这个老师");
			}
			return addSuccessStatus(userInfoObject, true, "");
		}
		//如果role不等于0/1
		return addSuccessStatus(jsonObject, false, "对不起，您的角色不正确");
		
	}

	
}

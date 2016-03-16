package edu.dufe.oes.service;

import java.util.List;

import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.dufe.oes.bean.Administrator;
import edu.dufe.oes.bean.AdministratorDAO;
import edu.dufe.oes.bean.Semester;
import edu.dufe.oes.bean.SemesterDAO;
import edu.dufe.oes.bean.SysConfig;
import edu.dufe.oes.bean.SysConfigDAO;
import edu.dufe.oes.bean.Teacher;
import edu.dufe.oes.bean.TeacherDAO;
import edu.dufe.oes.bean.UserDAO;
import edu.dufe.oes.config.CommonValues;
import edu.dufe.oes.util.MyStringUtil;

public class AdministratorService extends CommonService {

	AdministratorDAO administratorDAO=new AdministratorDAO();
	SemesterService semesterService=new SemesterService();
	SemesterDAO semesterDAO=new SemesterDAO();
	SysConfigDAO sysConfigDAO=new SysConfigDAO();
	TeacherDAO teacherDAO=new TeacherDAO();
	UserDAO userDAO=new UserDAO();
	public Administrator getAdministratorByToken(String token){
		clearSession(administratorDAO);
		List<Administrator> administrators=administratorDAO.findByToken(MyStringUtil.trimAndEscapeSql(token));
		if (administrators.size()<=0) {
			return null;
		}
		return administrators.get(0);
	}
	
	public JSONObject administratorLogin(String username,String password) throws Exception{
		JSONObject jsonObject=new JSONObject();
		clearSession(administratorDAO);
		List<Administrator> administrators=administratorDAO.getSession().createQuery("from Administrator where userName='"+MyStringUtil.trimAndEscapeSql(username)+"' and password='"+MyStringUtil.trimAndEscapeSql(password)+"'").list();
		if (administrators.size()<=0) {
			return addSuccessStatus(jsonObject, false, "登录失败");
		}
		Administrator administrator=administrators.get(0);
		Transaction transaction = administratorDAO.getSession().beginTransaction();
		administrator.setToken(MyStringUtil.toMD5(username+System.currentTimeMillis()+CommonValues.SEED));
		administratorDAO.getSession().update(administrator);
		transaction.commit();
		administratorDAO.getSession().close();
		jsonObject.put("token", administrator.getToken());
		return addSuccessStatus(jsonObject, true, "");
	}
	
	public JSONObject administratorSetSemester(String token,int semesterName) throws Exception{
		JSONObject jsonObject=new JSONObject();
		clearSession(semesterDAO);
		Administrator administrator=getAdministratorByToken(token);
		if (administrator==null) {
			return addSuccessStatus(jsonObject, false, "非法管理员");
		}
		if (semesterName<200000||semesterName>300000) {
			return addSuccessStatus(jsonObject, false, "无效学期");
		}
		List<Semester> semesterNames = semesterDAO.findBySemesterName(semesterName);
		boolean hasSameSemester=false;
		if (semesterNames.size()>0) {
			hasSameSemester=true;
		}
		Transaction transaction = semesterDAO.getSession().beginTransaction();
		if (!hasSameSemester) {
			Semester semester=new Semester();
			semester.setSemesterName(semesterName);
			semesterDAO.save(semester);
		}
		List<SysConfig> sysConfigs = sysConfigDAO.findByConfigKey("currentSemester");
		if (sysConfigs.size()==0) {
			SysConfig sysConfig=new SysConfig();
			sysConfig.setConfigKey("currentSemester");
			sysConfig.setConfigValue(semesterName+"");
			sysConfigDAO.save(sysConfig);
		}else {
			SysConfig sysConfig=sysConfigs.get(0);
			sysConfig.setConfigValue(semesterName+"");
			sysConfigDAO.getSession().update(sysConfig);
		}
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
	}
	
	public List<Teacher> getApplyingTeachers(){
		clearSession(teacherDAO);
		List<Teacher> teacherList = teacherDAO.findByApproveStatus(Teacher.APPROVING);
		return teacherList;
	}
	
	public JSONObject administratorSetTeacherStatus(String token,int teacherStatus,String teacherList) throws Exception{
		JSONObject jsonObject=new JSONObject();
		clearSession(semesterDAO);
		Administrator administrator=getAdministratorByToken(token);
		if (administrator==null) {
			return addSuccessStatus(jsonObject, false, "非法管理员");
		}
		if (teacherStatus>1||teacherStatus<-1) {
			return addSuccessStatus(jsonObject, false, "无效批准状态");
		}
		JSONArray teacherArray=new JSONArray(teacherList);
		Transaction transaction = teacherDAO.getSession().beginTransaction();
		for (int i = 0; i < teacherArray.length(); i++) {
			JSONObject teacherObject=teacherArray.getJSONObject(i);
			Teacher teacher=teacherDAO.findById(teacherObject.getInt("teacherID"));
			if (teacher!=null) {
				if (teacherStatus==Teacher.REJECTED) {
					userDAO.delete(teacher.getUser());
				}else if (teacherStatus==Teacher.APPROVED) {
					teacher.setApproveStatus(Teacher.APPROVED);
					teacherDAO.getSession().update(teacher);
				}
			}
		}
		transaction.commit();
		teacherDAO.getSession().close();
		return addSuccessStatus(jsonObject, true, "");
		
	}
}

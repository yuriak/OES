package edu.dufe.oes.service;

import java.util.List;

import org.json.JSONObject;

import edu.dufe.oes.bean.Semester;
import edu.dufe.oes.bean.SemesterDAO;
import edu.dufe.oes.bean.SysConfig;
import edu.dufe.oes.bean.SysConfigDAO;

public class SemesterService extends CommonService {

	SemesterDAO semesterDAO=new SemesterDAO();
	private SysConfigDAO sysConfigDAO=new SysConfigDAO();
	public Semester getSemesterBySemesterName(int semesterName){
		List semesters = semesterDAO.findBySemesterName(semesterName);
		if (semesters.size()==0) {
			return null;
		}
		return (Semester) semesters.get(0);
	}
	
	
	public List<Semester> getAllSemester(){
		return semesterDAO.findAll();
	}
	
	public Semester getCurrentSemester(){
		List configList = sysConfigDAO.findByConfigKey("currentSemester");
		Semester semester;
		if (configList.size()<=0) {
			List backupSemesterlist = semesterDAO.findAll();
			if (backupSemesterlist.size()<=0) {
				return null;
			}else {
				semester=(Semester) backupSemesterlist.get(backupSemesterlist.size()-1);
				return semester;
			}
		}
		List semesters = semesterDAO.findBySemesterName(Integer.valueOf(((SysConfig) configList.get(0)).getConfigValue()));
		if (semesters.size()<=0) {
			List backupSemesterlist = semesterDAO.findAll();
			if (backupSemesterlist.size()<=0) {
				return null;
			}else {
				semester=(Semester) backupSemesterlist.get(backupSemesterlist.size()-1);
				return semester;
			}
		}
		return (Semester) semesters.get(0);
		
	}
}

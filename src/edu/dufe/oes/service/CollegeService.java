package edu.dufe.oes.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.dufe.oes.bean.College;
import edu.dufe.oes.bean.CollegeDAO;

public class CollegeService extends CommonService {

	
	private CollegeDAO collegeDAO=new CollegeDAO();
	public JSONObject getAllColleges() throws Exception{
		clearSession(collegeDAO);
		JSONObject object=new JSONObject();
		collegeDAO=new CollegeDAO();
		List<College> colleges=collegeDAO.findAll();
		JSONArray collegeArray=new JSONArray();
		for (College college : colleges) {
			JSONObject collegeObject=new JSONObject();
			collegeObject.put("collegeID", college.getCollegeID());
			collegeObject.put("collegeName",college.getCollegeName());
			collegeArray.put(collegeObject);
		}
		object.put("collegeList", collegeArray);
		addSuccessStatus(object, true, "");
		return object;
	}
	
	public College findCollegeByID(int id){
		return collegeDAO.findById(id);
	}
}

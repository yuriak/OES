package edu.dufe.oes.service;

import org.json.JSONException;
import org.json.JSONObject;

import edu.dufe.oes.bean.BaseHibernateDAO;
import edu.dufe.oes.config.CommonValues;

public class CommonService {

	public JSONObject addSuccessStatus(JSONObject jsonObject,boolean isSuccess,String reason) throws Exception{
		return jsonObject.put(CommonValues.SUCCESS, isSuccess).put(CommonValues.REASON, reason);
	}
	
	public void clearSession(BaseHibernateDAO baseHibernateDAO){
		baseHibernateDAO.getSession().clear();
	}
}

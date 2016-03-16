package edu.dufe.oes.api;

import org.json.JSONException;
import org.json.JSONObject;

import edu.dufe.oes.bean.BaseHibernateDAO;
import edu.dufe.oes.config.CommonValues;

public class CommonAPI {

	public String sendErrorMessage(String errorMessage){
		JSONObject object=new JSONObject();
		BaseHibernateDAO baseHibernateDAO=new BaseHibernateDAO();
		baseHibernateDAO.getSession().close();
		try {
			if (errorMessage==null) {
				errorMessage="";
			}
			object.put(CommonValues.CODE, CommonValues.SERVER_ERROR);
			object.put(CommonValues.ERRMSG, errorMessage);
			return object.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public JSONObject addServerNormalStatus(JSONObject jsonObject) throws JSONException{
		jsonObject.put(CommonValues.CODE, CommonValues.SERVER_NORMAL);
		jsonObject.put(CommonValues.ERRMSG, "");
		System.out.println(jsonObject.toString());
		return jsonObject;
	}
}

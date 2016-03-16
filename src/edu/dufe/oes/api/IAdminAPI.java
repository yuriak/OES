package edu.dufe.oes.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;

import org.json.JSONArray;

public interface IAdminAPI {

	public String adminLogin(@Context HttpServletRequest request,@FormParam("userName") String userName,@FormParam("password") String password);
	public String adminSetSemester(@Context HttpServletRequest request,@FormParam("semester")int semester);
	public String adminSetTeaherStatus(@Context HttpServletRequest request,@FormParam("teacherStatus")int teacherStatus, @FormParam("teacherList") String teacherList);
	public String adminLogout(@Context HttpServletRequest request);
}

package edu.dufe.oes.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.dufe.oes.service.AdministratorService;
import edu.dufe.oes.util.MyStringUtil;

@Path("admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminAPI extends CommonAPI implements IAdminAPI {
	AdministratorService administratorService=new AdministratorService();
	
	@Override
	@Path("adminLogin")
	@POST
	public String adminLogin(@Context HttpServletRequest request,@FormParam("userName") String userName,@FormParam("password") String password){
		try {
			JSONObject jsonObject=administratorService.administratorLogin(userName, password);
			if (jsonObject.getBoolean("success")==true) {
				request.getSession().setAttribute("token", jsonObject.getString("token"));
			}
			return addServerNormalStatus(jsonObject).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("adminSetSemester")
	@POST
	public String adminSetSemester(@Context HttpServletRequest request,@FormParam("semester")int semester){
		try {
			String token=request.getSession().getAttribute("token").toString();
			return addServerNormalStatus(administratorService.administratorSetSemester(token, semester)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("adminSetTeacherStatus")
	@POST
	public String adminSetTeaherStatus(@Context HttpServletRequest request,
			@FormParam("teacherStatus")int teacherStatus,@FormParam("teacherList")String teacherList) {
		try {
			String token=request.getSession().getAttribute("token").toString();
			return addServerNormalStatus(administratorService.administratorSetTeacherStatus(token, teacherStatus, teacherList)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("adminLogout")
	@POST
	public String adminLogout(@Context HttpServletRequest request) {
		request.getSession().invalidate();
		return null;
	}
}

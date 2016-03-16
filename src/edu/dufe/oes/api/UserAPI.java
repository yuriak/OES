package edu.dufe.oes.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringEscapeUtils;
import org.jboss.logging.Field;
import org.json.JSONException;
import org.json.JSONObject;

import edu.dufe.oes.bean.Message;
import edu.dufe.oes.bean.MessageDAO;
import edu.dufe.oes.bean.User;
import edu.dufe.oes.bean.UserDAO;
import edu.dufe.oes.config.CommonValues;
import edu.dufe.oes.service.CollegeService;
import edu.dufe.oes.service.MessageService;
import edu.dufe.oes.service.UserInfoService;
import edu.dufe.oes.util.MyStringUtil;
@Path("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserAPI extends CommonAPI implements IUserAPI {
	//api层 防止sql注入以及去掉空格； escapeSql;trim
	//利用jaxrs需要在上面写注解，
	

	UserInfoService userInfoService=new UserInfoService();
	MessageService messageService=new MessageService();
	private CollegeService collegeService;
	@Override
	@Path("getMyInfoByToken")
	@POST
	public String getMyInfoByToken(@FormParam("token")String token) {
		try {
			return addServerNormalStatus(userInfoService.getMyInfoByToken(MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("login")
	@POST
	public String login(@FormParam("userName")String userName,@FormParam("password") String password) {
		try {
			return addServerNormalStatus(userInfoService.login(StringEscapeUtils.escapeSql(userName).trim(), StringEscapeUtils.escapeSql(password).trim())).toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("checkUniqueUser")
	@POST
	public String checkUniqueUser(@FormParam("userName")String userName) {
		try {
			return addServerNormalStatus(userInfoService.checkUniqueUser(MyStringUtil.trimAndEscapeSql(userName))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("register")
	@POST
	public String register(@FormParam("userName")String userName,@FormParam("password") String password,@FormParam("question") String question,
			@FormParam("answer")String answer, @FormParam("realName")String realName, @FormParam("collegeID")int collegeID,@FormParam("gender") int gender,
			@FormParam("email")String email, @FormParam("phone")String phone,@FormParam("role") int role,@FormParam("major") String major) {
		try{
			return addServerNormalStatus(userInfoService.register(
					MyStringUtil.trimAndEscapeSql(userName), 
					MyStringUtil.trimAndEscapeSql(password), 
					MyStringUtil.trimAndEscapeSql(question), 
					MyStringUtil.trimAndEscapeSql(answer), 
					MyStringUtil.trimAndEscapeSql(realName), 
					collegeID, 
					gender, 
					MyStringUtil.trimAndEscapeSql(email), 
					MyStringUtil.trimAndEscapeSql(phone),
					role,
					MyStringUtil.trimAndEscapeSql(major))).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("getUserQuestionByUserName")
	@POST
	public String getUserQuestionByUserName(@FormParam("userName")String userName) {
		try {
			return addServerNormalStatus(userInfoService.getUserQuestion(MyStringUtil.trimAndEscapeSql(userName))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("verifyAnswer")
	@POST
	public String verifyAnswer(@FormParam("userID")int userID, @FormParam("answer")String answer) {
		
		try {
			return addServerNormalStatus(userInfoService.verifyAnswer(userID, MyStringUtil.trimAndEscapeSql(answer))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("resetUserPassword")
	@POST
	public String resetUserPassword(@FormParam("password")String password,@FormParam("token") String token) {
		try {
			return addServerNormalStatus(userInfoService.resetUserPassword(MyStringUtil.trimAndEscapeSql(password), MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}
//	收信箱;
	@Override
	@Path("getReceivedMessageList")
	@POST
	public String getReceivedMessageList(@FormParam("token")String token) {
		// TODO Auto-generated method stub
		try {
			return addServerNormalStatus(messageService.getReceivedMessageList(MyStringUtil.trimAndEscapeSql(token))).toString();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	//token:接收者;
	//接收者 打开查看信息；
	@Override
	@Path("setMessageOpenStatus")
	@POST
	public String setMessageOpenStatus(@FormParam("messageID")int messageID, @FormParam("token")String token) {
		// TODO Auto-generated method stub
		try {
			return addServerNormalStatus(messageService.setMessageOpenStatus(MyStringUtil.trimAndEscapeSql(token), messageID)).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	//删除信息，只是把标识位更改了；
	@Override
	@Path("deleteMessage")
	@POST
	public String deleteMessage(@FormParam("messageID")int messageID,@FormParam("token") String token) {
		// TODO Auto-generated method stub
		try {
			return addServerNormalStatus(messageService.deleteMessage(messageID, MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	//相当于发信箱；
	@Override
	@Path("getSentMessageList")
	@POST
	public String getSentMessageList(@FormParam("token")String token) {
		try {
			return addServerNormalStatus(messageService.getSentMessageList(MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		} 
	}

	@Override
	@Path("sendMessage")
	@POST
	public String sendMessage(@FormParam("receiverName")String receiverName, @FormParam("title")String title,
			@FormParam("content")String content,@FormParam("emergencyLevel") int emergencyLevel, @FormParam("token")String token) {
		try {
			return addServerNormalStatus(messageService.sendMessage(MyStringUtil.trimAndEscapeSql(receiverName), MyStringUtil.trimAndEscapeSql(title),MyStringUtil.trimAndEscapeSql(content) , emergencyLevel, MyStringUtil.trimAndEscapeSql(token))).toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("updateUserInfo")
	@POST
	public String updateUserInfo(@FormParam("token")String token,@FormParam("realName") String realName,
			@FormParam("collegeID")int collegeID, @FormParam("email")String email, @FormParam("gender")int gender,@FormParam("phone") String phone,
			@FormParam("major")String major) {
		// TODO Auto-generated method stub
		try {
			return addServerNormalStatus(userInfoService.updateUserInfo(MyStringUtil.trimAndEscapeSql(token),
					MyStringUtil.trimAndEscapeSql(realName), collegeID, MyStringUtil.trimAndEscapeSql(email), 
					gender, MyStringUtil.trimAndEscapeSql(phone), MyStringUtil.trimAndEscapeSql(major))).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("verifyPassword")
	@POST
	public String verifyPassword(@FormParam("password")String password, @FormParam("token")String token) {
		// TODO Auto-generated method stub
		try {
			return addServerNormalStatus(userInfoService.verifyPassword(MyStringUtil.trimAndEscapeSql(password),
									MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("updatePassword")
	@POST
	public String updatePassword(@FormParam("token")String token, @FormParam("password")String password) {
		// TODO Auto-generated method stub
		try {
			return addServerNormalStatus(userInfoService.updatePassword(
					MyStringUtil.trimAndEscapeSql(token),
					MyStringUtil.trimAndEscapeSql(password))).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}		
	 
	}

	@Override
	@Path("getAllCollege")
	@POST
	public String getAllCollege() {
		collegeService = new CollegeService();
		try {
			return addServerNormalStatus(collegeService.getAllColleges()).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}

	@Override
	@Path("getUserInfo")
	@POST
	public String getUserInfo(@FormParam("token")String token) {
		try {
			return addServerNormalStatus(userInfoService.getUserInfo(MyStringUtil.trimAndEscapeSql(token))).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorMessage(e.getMessage());
		}
	}


}

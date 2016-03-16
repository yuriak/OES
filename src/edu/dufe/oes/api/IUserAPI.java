package edu.dufe.oes.api;

import javax.ws.rs.FormParam;

public interface IUserAPI {

	/*
	 * login/register
	 */
	
	public String getMyInfoByToken(String token);//{userID,userName,realName,college,gender,email,phone,messageNumber,role,major,currentSemester,semesterList:[{semester}]};code:int;errMsg:String
	public String login(String userName,String password);//{success,reason,token，role};code:int;errMsg:String
	public String checkUniqueUser(String userName);//unique:boolean;code:int;errMsg:String
	public String register(String userName,String password,String question,String answer,String realName,int collegeID,int gender,String email,String phone,int role,String major);//success:boolean;code:int;errMsg:String
	public String getUserQuestionByUserName(String userName);//{question:string,userID:int};code:int;errMsg:String
	public String verifyAnswer(int userID,String answer);//success:boolean,token:string;code:int;errMsg:String
	public String resetUserPassword(String password,String token);//success:boolean;code:int;errMsg:String
	public String getAllCollege();//{collegeList:[{collegeID,collegeName}]}
	/*
	 * message
	 */
	public String getReceivedMessageList(String token);//{receivedMessageList:[{senderID,senderName,title,content,sendTime,emergencyLevel,openStatus,openTime}]}
	public String setMessageOpenStatus(int messageID,String token);//{success}
	public String deleteMessage(int messageID,String token);//{success}
	public String getSentMessageList(String token);//{sentMessageList:[{receiverID,receiverName,title,content,sendTime,emergencyLevel,openStatus,openTime}]}
	public String sendMessage(String receiverName,String title,String content,int emergencyLevel,String token);//{success}
	/*
	 * person
	 */
	public String updateUserInfo(String token,String realName,int collegeID, String email,int gender,String phone,String major);//{success}
	public String verifyPassword(String password,String token);//{success}
	public String updatePassword(String token,String password);//{success}
	public String getUserInfo(String token);//{username,realname,college,gender,email,phone,major,role}
	
}

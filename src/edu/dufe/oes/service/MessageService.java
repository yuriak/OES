package edu.dufe.oes.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.dufe.oes.bean.College;
import edu.dufe.oes.bean.CollegeDAO;
import edu.dufe.oes.bean.Message;
import edu.dufe.oes.bean.MessageDAO;
import edu.dufe.oes.bean.Student;
import edu.dufe.oes.bean.StudentDAO;
import edu.dufe.oes.bean.User;
import edu.dufe.oes.bean.UserDAO;
import edu.dufe.oes.config.CommonValues;
import edu.dufe.oes.util.MySetUtil;

public class MessageService extends CommonService{
	MessageDAO messageDAO=new MessageDAO();
	UserDAO userDAO=new UserDAO();
	UserInfoService userInfoService=new UserInfoService();
	public JSONObject sendMessage(String receiverName, String title,
			String content, int emergencyLevel, String token) throws Exception {
		clearSession(messageDAO);
		JSONObject object=new JSONObject();
		User sender;
		User receiver;
		if ((sender=userInfoService.getUserByToken(token))==null) {
			return userInfoService.addSuccessStatus(object, false, CommonValues.ILLEGALUSER_ERR);
		}
		if ((receiver=userInfoService.getUserByUserName(receiverName))==null) {
			return userInfoService.addSuccessStatus(object, false, CommonValues.NULLUSER_ERR);
		}
		if (emergencyLevel<0||emergencyLevel>1) {
			return userInfoService.addSuccessStatus(object, false, "无效紧急级别");
		}
		Message message=new Message();
		message.setEmergencyLevel(emergencyLevel);
		message.setUserBySenderid(sender);
		message.setUserByReceiverid(receiver);
		message.setContent(content);
		message.setTitle(title);
		message.setSentTime(new Timestamp(System.currentTimeMillis()));
		message.setReceiverDeleteStatus(Message.NOTDELETED);
		message.setSenderDeleteStatus(Message.NOTDELETED);
		message.setOpenStatus(Message.NOTOPENED);
		messageDAO.save(message);
		return userInfoService.addSuccessStatus(object, true, "");
	}

	//发信箱
	public JSONObject getSentMessageList(String token) throws Exception{
		clearSession(messageDAO);
		JSONObject jsonObject=new JSONObject();
		JSONArray messageArray=new JSONArray();
		User sender=userInfoService.getUserByToken(token);
		if(sender==null){
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Message> messageList=new ArrayList<Message>(sender.getMessagesForSenderid());
		if (messageList.size()<=0) {
			jsonObject.put("sendMessageList", messageArray);
			return addSuccessStatus(jsonObject, true, "");
		}
		Collections.sort(messageList);
		int i=0;
		for (Message message : messageList) {
		//如果删除状态为1，即表示sender看不到这条信息，这条信息被sender删除了，但是仍然保存在数据库里；
			if (message.getSenderDeleteStatus() ==Message.DELETED) {
				continue;
			}
			JSONObject messageObject = new JSONObject();
			messageObject.put("messageID", message.getMessageID());
			messageObject.put("receiverID", message.getUserByReceiverid()
					.getUserid());
			messageObject.put("receiverName", message.getUserByReceiverid()
					.getUsername());
			messageObject.put(messageDAO.TITLE, message.getTitle());
			messageObject.put(messageDAO.CONTENT, message.getContent());
			messageObject.put(messageDAO.SENTTIME, message.getSentTime());
			messageObject.put(messageDAO.EMERGENCY_LEVEL,
					message.getEmergencyLevel());
			messageObject.put(messageDAO.OPEN_STATUS, message.getOpenStatus());
			messageObject.put(messageDAO.OPENTIME, message.getOpenTime());
			messageArray.put(i,messageObject);
			i++;
		}	
		jsonObject.put("sentMessageList", messageArray);
		return addSuccessStatus(jsonObject, true, "");
		
		
	}
	//收信箱
	public JSONObject getReceivedMessageList(String token)throws Exception {
		clearSession(messageDAO);
		JSONObject jsonObject=new JSONObject();
		JSONArray messageArray=new JSONArray();
		User receiver=userInfoService.getUserByToken(token);
		if(receiver==null){
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		List<Message> messageList=new ArrayList<Message>(receiver.getMessagesForReceiverid());
		int i=0;
		Collections.sort(messageList);
		for (Message message : messageList) {
			//如果删除状态为1，即表示receiver看不到这条信息，这条信息被receiver删除了，但是仍然保存在数据库里；
			if (message.getReceiverDeleteStatus() ==Message.DELETED) {
				continue;
			}
			JSONObject messageObject = new JSONObject();
			messageObject.put("messageID", message.getMessageID());
			messageObject.put("senderID", message.getUserBySenderid()
					.getUserid());
			messageObject.put("senderName", message.getUserBySenderid()
					.getUsername());
			messageObject.put(messageDAO.TITLE, message.getTitle());
			messageObject.put(messageDAO.CONTENT, message.getContent());
			messageObject.put(messageDAO.SENTTIME, message.getSentTime());
			messageObject.put(messageDAO.EMERGENCY_LEVEL,
					message.getEmergencyLevel());
			messageObject.put(messageDAO.OPEN_STATUS, message.getOpenStatus());
			messageObject.put(messageDAO.OPENTIME, message.getOpenTime());
			messageArray.put(i,messageObject);
			i++;
		}
		jsonObject.put("receivedMessageList", messageArray);
		return addSuccessStatus(jsonObject, true, "");
	}
	
	public JSONObject deleteMessage(int messageID, String token) throws Exception{
		clearSession(messageDAO);
		JSONObject jsonObject=new JSONObject();
		Message message=messageDAO.findById(messageID);
		if (message==null) {
			return addSuccessStatus(jsonObject, false, "无效信息");
		}
		//通过message得到接收者  发送者;
		User user=userInfoService.getUserByToken(token);
		if(user==null){
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		User senderUser=message.getUserBySenderid();
		User receiverUser=message.getUserByReceiverid();
		
		if (receiverUser.equals(user)||senderUser.equals(user)) {
			Transaction transaction = messageDAO.getSession().beginTransaction();
			if(senderUser.equals(user)){
				//删除-----设置senderstatus为1；
				message.setSenderDeleteStatus(Message.DELETED);
				messageDAO.getSession().update(message);
			}
			if(receiverUser.equals(user)){
				message.setReceiverDeleteStatus(Message.DELETED);
				messageDAO.getSession().update(message);
			}
			transaction.commit();
			return addSuccessStatus(jsonObject, true, "");
		}
		
		messageDAO.getSession().close();
		return addSuccessStatus(jsonObject, false, "不是你的信息");
		//如果匹配不到，就说明用户不存在；
		
	}
	public JSONObject setMessageOpenStatus(String token,int messageID) throws Exception{
		clearSession(messageDAO);
		JSONObject jsonObject=new JSONObject();
		
		Message message=messageDAO.findById(messageID);
		User receiver=userInfoService.getUserByToken(token);
		if(receiver==null){
			messageDAO.getSession().close();
			return addSuccessStatus(jsonObject, false, CommonValues.ILLEGALUSER_ERR);
		}
		Transaction transaction=messageDAO.getSession().beginTransaction();
		if(message.getUserByReceiverid().equals(receiver)){
			message.setOpenTime(new Timestamp(System.currentTimeMillis()));
			message.setOpenStatus(Message.OPENED);
		}else {
			messageDAO.getSession().close();
			return addSuccessStatus(jsonObject, false, "不是你的邮件");
		}
		transaction.commit();
		return addSuccessStatus(jsonObject, true, "");
	}

}

package edu.dufe.oes.bean;

import java.sql.Timestamp;

/**
 * Message entity. @author MyEclipse Persistence Tools
 */

public class Message implements Comparable<Message>,java.io.Serializable {

	// Fields

	private Integer messageID;
	private User userBySenderid;
	private User userByReceiverid;
	private String content;
	private Timestamp sentTime;
	private Timestamp openTime;
	private Integer openStatus;
	private Integer senderDeleteStatus;
	private Integer receiverDeleteStatus;
	private String title;
	private Integer emergencyLevel;
	//emergencyLevel
	public static final int EMERGENCY=1;
	public static final int NORMAL=1;
	
	//deleteStatus
	public static final int NOTDELETED=0;
	public static final int DELETED=1;
	
	public static final int NOTOPENED=0;
	public static final int OPENED=1;

	// Constructors

	/** default constructor */
	public Message() {
	}

	/** full constructor */
	public Message(User userBySenderid, User userByReceiverid, String content,
			Timestamp sentTime, Timestamp openTime, Integer openStatus,
			Integer senderDeleteStatus, Integer receiverDeleteStatus,
			String title,Integer emergencyLevel) {
		this.userBySenderid = userBySenderid;
		this.userByReceiverid = userByReceiverid;
		this.content = content;
		this.sentTime = sentTime;
		this.openTime = openTime;
		this.openStatus = openStatus;
		this.senderDeleteStatus = senderDeleteStatus;
		this.receiverDeleteStatus = receiverDeleteStatus;
		this.title = title;
		this.emergencyLevel=emergencyLevel;
	}

	// Property accessors

	public Integer getMessageID() {
		return this.messageID;
	}

	public void setMessageID(Integer messageID) {
		this.messageID = messageID;
	}

	public User getUserBySenderid() {
		return this.userBySenderid;
	}

	public void setUserBySenderid(User userBySenderid) {
		this.userBySenderid = userBySenderid;
	}

	public User getUserByReceiverid() {
		return this.userByReceiverid;
	}

	public void setUserByReceiverid(User userByReceiverid) {
		this.userByReceiverid = userByReceiverid;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getSentTime() {
		return this.sentTime;
	}

	public void setSentTime(Timestamp sentTime) {
		this.sentTime = sentTime;
	}

	public Timestamp getOpenTime() {
		return this.openTime;
	}

	public void setOpenTime(Timestamp openTime) {
		this.openTime = openTime;
	}

	public Integer getOpenStatus() {
		return this.openStatus;
	}

	public void setOpenStatus(Integer openStatus) {
		this.openStatus = openStatus;
	}

	public Integer getSenderDeleteStatus() {
		return this.senderDeleteStatus;
	}

	public void setSenderDeleteStatus(Integer senderDeleteStatus) {
		this.senderDeleteStatus = senderDeleteStatus;
	}

	public Integer getReceiverDeleteStatus() {
		return this.receiverDeleteStatus;
	}

	public void setReceiverDeleteStatus(Integer receiverDeleteStatus) {
		this.receiverDeleteStatus = receiverDeleteStatus;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getEmergencyLevel() {
		return emergencyLevel;
	}

	public void setEmergencyLevel(Integer emergencyLevel) {
		this.emergencyLevel = emergencyLevel;
	}

	@Override
	public int compareTo(Message o) {
		if (o.getSentTime().after(this.getSentTime())) {
			return -1;
		}else if (o.getSentTime().before(this.getSentTime())) {
			return 1;
		}
		return 0;
	}

}
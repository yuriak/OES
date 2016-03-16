package edu.dufe.oes.bean;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	private Integer userid;
	private College college;
	private Integer role;
	private String username;
	private Integer gender;
	private String plainPassword;
	private String encryptPassword;
	private String passwordQuestion;
	private String passwordAnswer;
	private String email;
	private String phone;
	private Timestamp registerTime;
	private Timestamp lastLoginTime;
	private String token;
	private String messageNumber;
	private String realName;
	private Set students = new HashSet(0);
	private Set messagesForSenderid = new HashSet(0);
	private Set messagesForReceiverid = new HashSet(0);
	private Set teachers = new HashSet(0);

	
	public static final int STUDENT=0;
	public static final int TEACHER=1;
	
	public static final int SENDER=1;
	public static final int RECEIVER=0;
	
	//gender
	public static final int MALE=1;
	public static final int FEMALE=0;
	// Constructors

	/** default constructor */
	public User() {
	}

	/** full constructor */
	public User(College college, Integer role, String username, Integer gender,
			String plainPassword, String encryptPassword,
			String passwordQuestion, String passwordAnswer, String email,
			String phone, Timestamp registerTime, Timestamp lastLoginTime,
			String token, String messageNumber, String realName, Set students,
			Set messagesForSenderid, Set messagesForReceiverid, Set teachers) {
		this.college = college;
		this.role = role;
		this.username = username;
		this.gender = gender;
		this.plainPassword = plainPassword;
		this.encryptPassword = encryptPassword;
		this.passwordQuestion = passwordQuestion;
		this.passwordAnswer = passwordAnswer;
		this.email = email;
		this.phone = phone;
		this.registerTime = registerTime;
		this.lastLoginTime = lastLoginTime;
		this.token = token;
		this.messageNumber = messageNumber;
		this.realName = realName;
		this.students = students;
		this.messagesForSenderid = messagesForSenderid;
		this.messagesForReceiverid = messagesForReceiverid;
		this.teachers = teachers;
	}

	// Property accessors

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public College getCollege() {
		return this.college;
	}

	public void setCollege(College college) {
		this.college = college;
	}

	public Integer getRole() {
		return this.role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getGender() {
		return this.gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getPlainPassword() {
		return this.plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getEncryptPassword() {
		return this.encryptPassword;
	}

	public void setEncryptPassword(String encryptPassword) {
		this.encryptPassword = encryptPassword;
	}

	public String getPasswordQuestion() {
		return this.passwordQuestion;
	}

	public void setPasswordQuestion(String passwordQuestion) {
		this.passwordQuestion = passwordQuestion;
	}

	public String getPasswordAnswer() {
		return this.passwordAnswer;
	}

	public void setPasswordAnswer(String passwordAnswer) {
		this.passwordAnswer = passwordAnswer;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Timestamp getRegisterTime() {
		return this.registerTime;
	}

	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}

	public Timestamp getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMessageNumber() {
		return this.messageNumber;
	}

	public void setMessageNumber(String messageNumber) {
		this.messageNumber = messageNumber;
	}

	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Set getStudents() {
		return this.students;
	}

	public void setStudents(Set students) {
		this.students = students;
	}

	public Set getMessagesForSenderid() {
		return this.messagesForSenderid;
	}

	public void setMessagesForSenderid(Set messagesForSenderid) {
		this.messagesForSenderid = messagesForSenderid;
	}

	public Set getMessagesForReceiverid() {
		return this.messagesForReceiverid;
	}

	public void setMessagesForReceiverid(Set messagesForReceiverid) {
		this.messagesForReceiverid = messagesForReceiverid;
	}

	public Set getTeachers() {
		return this.teachers;
	}

	public void setTeachers(Set teachers) {
		this.teachers = teachers;
	}

}
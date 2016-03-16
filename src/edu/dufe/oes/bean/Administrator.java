package edu.dufe.oes.bean;

/**
 * Administrator entity. @author MyEclipse Persistence Tools
 */

public class Administrator implements java.io.Serializable {

	// Fields

	private Integer adminID;
	private String password;
	private String userName;
	private String token;

	// Constructors

	/** default constructor */
	public Administrator() {
	}

	/** full constructor */
	public Administrator(String password, String userName, String token) {
		this.password = password;
		this.userName = userName;
		this.token = token;
	}

	// Property accessors

	public Integer getAdminID() {
		return this.adminID;
	}

	public void setAdminID(Integer adminID) {
		this.adminID = adminID;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
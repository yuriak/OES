package edu.dufe.oes.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * Teacher entity. @author MyEclipse Persistence Tools
 */

public class Teacher implements java.io.Serializable {

	// Fields

	private Integer teacherID;
	private User user;
	private Integer approveStatus;
	private Set courses = new HashSet(0);
	
	public static final int APPROVED=1;
	public static final int APPROVING=0;
	public static final int REJECTED=-1;

	// Constructors

	/** default constructor */
	public Teacher() {
	}

	/** full constructor */
	public Teacher(User user, Integer approveStatus, Set courses) {
		this.user = user;
		this.approveStatus = approveStatus;
		this.courses = courses;
	}

	// Property accessors

	public Integer getTeacherID() {
		return this.teacherID;
	}

	public void setTeacherID(Integer teacherID) {
		this.teacherID = teacherID;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getApproveStatus() {
		return this.approveStatus;
	}

	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Set getCourses() {
		return this.courses;
	}

	public void setCourses(Set courses) {
		this.courses = courses;
	}

}
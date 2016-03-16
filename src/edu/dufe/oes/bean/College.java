package edu.dufe.oes.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * College entity. @author MyEclipse Persistence Tools
 */

public class College implements java.io.Serializable {

	// Fields

	private Integer collegeID;
	private String collegeName;
	private Set courses = new HashSet(0);
	private Set users = new HashSet(0);

	// Constructors

	/** default constructor */
	public College() {
	}

	/** full constructor */
	public College(String collegeName, Set courses, Set users) {
		this.collegeName = collegeName;
		this.courses = courses;
		this.users = users;
	}

	// Property accessors

	public Integer getCollegeID() {
		return this.collegeID;
	}

	public void setCollegeID(Integer collegeID) {
		this.collegeID = collegeID;
	}

	public String getCollegeName() {
		return this.collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public Set getCourses() {
		return this.courses;
	}

	public void setCourses(Set courses) {
		this.courses = courses;
	}

	public Set getUsers() {
		return this.users;
	}

	public void setUsers(Set users) {
		this.users = users;
	}

}
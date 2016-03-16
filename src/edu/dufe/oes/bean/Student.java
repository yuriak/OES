package edu.dufe.oes.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * Student entity. @author MyEclipse Persistence Tools
 */

public class Student implements java.io.Serializable {

	// Fields

	private Integer studentid;
	private User user;
	private String major;
	private Set leaves = new HashSet(0);
	private Set courseElectives = new HashSet(0);
	private Set evaluationReceivers = new HashSet(0);
	private Set evaluationResults = new HashSet(0);
	private Set groupMembers = new HashSet(0);

	// Constructors

	/** default constructor */
	public Student() {
	}

	/** full constructor */
	public Student(User user, String major, Set leaves, Set courseElectives,
			Set evaluationReceivers, Set evaluationResults, Set groupMembers) {
		this.user = user;
		this.major = major;
		this.leaves = leaves;
		this.courseElectives = courseElectives;
		this.evaluationReceivers = evaluationReceivers;
		this.evaluationResults = evaluationResults;
		this.groupMembers = groupMembers;
	}

	// Property accessors

	public Integer getStudentid() {
		return this.studentid;
	}

	public void setStudentid(Integer studentid) {
		this.studentid = studentid;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMajor() {
		return this.major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public Set getLeaves() {
		return this.leaves;
	}

	public void setLeaves(Set leaves) {
		this.leaves = leaves;
	}

	public Set getCourseElectives() {
		return this.courseElectives;
	}

	public void setCourseElectives(Set courseElectives) {
		this.courseElectives = courseElectives;
	}

	public Set getEvaluationReceivers() {
		return this.evaluationReceivers;
	}

	public void setEvaluationReceivers(Set evaluationReceivers) {
		this.evaluationReceivers = evaluationReceivers;
	}

	public Set getEvaluationResults() {
		return this.evaluationResults;
	}

	public void setEvaluationResults(Set evaluationResults) {
		this.evaluationResults = evaluationResults;
	}

	public Set getGroupMembers() {
		return this.groupMembers;
	}

	public void setGroupMembers(Set groupMembers) {
		this.groupMembers = groupMembers;
	}

}
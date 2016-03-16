package edu.dufe.oes.bean;

import java.sql.Timestamp;

/**
 * GroupMember entity. @author MyEclipse Persistence Tools
 */

public class GroupMember implements Comparable<GroupMember>,java.io.Serializable {

	// Fields

	private Integer groupMemberID;
	private Group group;
	private Student student;
	private Timestamp addTime;

	// Constructors

	/** default constructor */
	public GroupMember() {
	}

	/** full constructor */
	public GroupMember(Group group, Student student, Timestamp addTime) {
		this.group = group;
		this.student = student;
		this.addTime = addTime;
	}

	// Property accessors

	public Integer getGroupMemberID() {
		return this.groupMemberID;
	}

	public void setGroupMemberID(Integer groupMemberID) {
		this.groupMemberID = groupMemberID;
	}

	public Group getGroup() {
		return this.group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Timestamp getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}

	@Override
	public int compareTo(GroupMember o) {
		if (o.getAddTime().after(this.getAddTime())) {
			return -1;
		}else if (o.getAddTime().before(this.getAddTime())) {
			return 1;
		}
		return 0;
	}

}
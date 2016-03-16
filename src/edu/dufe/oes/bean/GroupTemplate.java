package edu.dufe.oes.bean;

import java.sql.Timestamp;

/**
 * GroupTemplate entity. @author MyEclipse Persistence Tools
 */

public class GroupTemplate implements Comparable<GroupTemplate>,java.io.Serializable {

	// Fields

	private Integer groupTemplateID;
	private Course course;
	private Group group;
	private String groupTemplateName;
	private Integer groupBackupID;
	private Timestamp backupTime;

	// Constructors

	/** default constructor */
	public GroupTemplate() {
	}

	/** full constructor */
	public GroupTemplate(Course course, Group group, String groupTemplateName,
			Integer groupBackupID, Timestamp backupTime) {
		this.course = course;
		this.group = group;
		this.groupTemplateName = groupTemplateName;
		this.groupBackupID = groupBackupID;
		this.backupTime = backupTime;
	}

	// Property accessors

	public Integer getGroupTemplateID() {
		return this.groupTemplateID;
	}

	public void setGroupTemplateID(Integer groupTemplateID) {
		this.groupTemplateID = groupTemplateID;
	}

	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Group getGroup() {
		return this.group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getGroupTemplateName() {
		return this.groupTemplateName;
	}

	public void setGroupTemplateName(String groupTemplateName) {
		this.groupTemplateName = groupTemplateName;
	}

	public Integer getGroupBackupID() {
		return this.groupBackupID;
	}

	public void setGroupBackupID(Integer groupBackupID) {
		this.groupBackupID = groupBackupID;
	}

	public Timestamp getBackupTime() {
		return this.backupTime;
	}

	public void setBackupTime(Timestamp backupTime) {
		this.backupTime = backupTime;
	}

	@Override
	public int compareTo(GroupTemplate o) {
		if (o.getGroupTemplateID()>this.getGroupTemplateID()) {
			return -1;
		}else if (o.getGroupTemplateID()<this.getGroupTemplateID()) {
			return 1;
		}
		return 0;
	}

}
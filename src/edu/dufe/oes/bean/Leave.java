package edu.dufe.oes.bean;

import java.sql.Timestamp;

/**
 * Leave entity. @author MyEclipse Persistence Tools
 */

public class Leave implements Comparable<Leave>,java.io.Serializable {

	// Fields

	private Integer leaveID;
	private Lesson lesson;
	private Student student;
	private Timestamp leaveTime;
	private String leaveReason;
	private Integer ApproveStatus;

	public static final int APPROVED=1;
	public static final int APPROVING=0;
	public static final int REJECTED=-1;
	// Constructors

	/** default constructor */
	public Leave() {
	}

	/** full constructor */
	public Leave(Lesson lesson, Student student, Timestamp leaveTime,
			String leaveReason, Integer ApproveStatus) {
		this.lesson = lesson;
		this.student = student;
		this.leaveTime = leaveTime;
		this.leaveReason = leaveReason;
		this.ApproveStatus = ApproveStatus;
	}

	// Property accessors

	public Integer getLeaveID() {
		return this.leaveID;
	}

	public void setLeaveID(Integer leaveID) {
		this.leaveID = leaveID;
	}

	public Lesson getLesson() {
		return this.lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Timestamp getLeaveTime() {
		return this.leaveTime;
	}

	public void setLeaveTime(Timestamp leaveTime) {
		this.leaveTime = leaveTime;
	}

	public String getLeaveReason() {
		return this.leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

	public Integer getApproveStatus() {
		return this.ApproveStatus;
	}

	public void setApproveStatus(Integer ApproveStatus) {
		this.ApproveStatus = ApproveStatus;
	}

	@Override
	public int compareTo(Leave o) {
		if (o.getLeaveTime().after(this.getLeaveTime())) {
			return -1;
		}else if (o.getLeaveTime().before(this.getLeaveTime())) {
			return 1;
		}
		return 0;
	}

}
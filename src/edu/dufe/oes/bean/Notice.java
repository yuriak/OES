package edu.dufe.oes.bean;

/**
 * Notice entity. @author MyEclipse Persistence Tools
 */

public class Notice implements java.io.Serializable {

	// Fields

	private Integer noticeID;
	private Lesson lesson;
	private String content;
	private Integer emergencyLevel;
	
	//emergencyLevel
	public static final int EMERGENCY=1;
	public static final int NORMAL=1;

	// Constructors

	/** default constructor */
	public Notice() {
	}

	/** full constructor */
	public Notice(Lesson lesson, String content, Integer emergencyLevel) {
		this.lesson = lesson;
		this.content = content;
		this.emergencyLevel = emergencyLevel;
	}

	// Property accessors

	public Integer getNoticeID() {
		return this.noticeID;
	}

	public void setNoticeID(Integer noticeID) {
		this.noticeID = noticeID;
	}

	public Lesson getLesson() {
		return this.lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getEmergencyLevel() {
		return this.emergencyLevel;
	}

	public void setEmergencyLevel(Integer emergencyLevel) {
		this.emergencyLevel = emergencyLevel;
	}

}
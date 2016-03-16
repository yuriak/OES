package edu.dufe.oes.bean;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Lesson entity. @author MyEclipse Persistence Tools
 */

public class Lesson implements Comparable<Lesson>,java.io.Serializable {

	// Fields

	private Integer lessonID;
	private Course course;
	private Timestamp addTime;
	private Integer lessonStatus;
	private Set leaves = new HashSet(0);
	private Set notices = new HashSet(0);
	private Set evaluations = new HashSet(0);
	
	//status
	public static final int BEFORE=-1;
	public static final int DOING=0;
	public static final int AFTER=1;

	// Constructors

	/** default constructor */
	public Lesson() {
	}

	/** full constructor */
	public Lesson(Course course, Timestamp addTime, Integer lessonStatus,
			Set leaves, Set notices, Set evaluations) {
		this.course = course;
		this.addTime = addTime;
		this.lessonStatus = lessonStatus;
		this.leaves = leaves;
		this.notices = notices;
		this.evaluations = evaluations;
	}

	// Property accessors

	public Integer getLessonID() {
		return this.lessonID;
	}

	public void setLessonID(Integer lessonID) {
		this.lessonID = lessonID;
	}

	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Timestamp getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}

	public Integer getLessonStatus() {
		return this.lessonStatus;
	}

	public void setLessonStatus(Integer lessonStatus) {
		this.lessonStatus = lessonStatus;
	}

	public Set getLeaves() {
		return this.leaves;
	}

	public void setLeaves(Set leaves) {
		this.leaves = leaves;
	}

	public Set getNotices() {
		return this.notices;
	}

	public void setNotices(Set notices) {
		this.notices = notices;
	}

	public Set getEvaluations() {
		return this.evaluations;
	}

	public void setEvaluations(Set evaluations) {
		this.evaluations = evaluations;
	}

	@Override
	public int compareTo(Lesson o) {
		if (o.getAddTime().after(this.getAddTime())) {
			return -1;
		}else if (o.getAddTime().before(this.getAddTime())) {
			return 1;
		}
		return 0;
	}

}
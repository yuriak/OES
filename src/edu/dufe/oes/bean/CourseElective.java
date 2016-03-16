package edu.dufe.oes.bean;

import java.sql.Timestamp;

/**
 * CourseElective entity. @author MyEclipse Persistence Tools
 */

public class CourseElective implements java.io.Serializable, Comparable<CourseElective> {

	// Fields

	private Integer courseElectiveID;
	private Course course;
	private Student student;
	private Timestamp electiveTime;
	private Integer electiveStatus;
	private Integer courseGrade;
	private Integer courseGradeStatus;
	
	public static final int APPROVED=1;
	public static final int APPROVING=0;
	public static final int REJECTED=-1;
	
	
	public static final int HASGRADE=1;
	public static final int NOGRADE=0;

	// Constructors

	/** default constructor */
	public CourseElective() {
	}

	/** full constructor */
	public CourseElective(Course course, Student student,
			Timestamp electiveTime, Integer electiveStatus, Integer courseGrade,
			Integer courseGradeStatus) {
		this.course = course;
		this.student = student;
		this.electiveTime = electiveTime;
		this.electiveStatus = electiveStatus;
		this.courseGrade = courseGrade;
		this.courseGradeStatus = courseGradeStatus;
	}

	// Property accessors

	public Integer getCourseElectiveID() {
		return this.courseElectiveID;
	}

	public void setCourseElectiveID(Integer courseElectiveID) {
		this.courseElectiveID = courseElectiveID;
	}

	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Timestamp getElectiveTime() {
		return this.electiveTime;
	}

	public void setElectiveTime(Timestamp electiveTime) {
		this.electiveTime = electiveTime;
	}

	public Integer getElectiveStatus() {
		return this.electiveStatus;
	}

	public void setElectiveStatus(Integer electiveStatus) {
		this.electiveStatus = electiveStatus;
	}

	public Integer getCourseGrade() {
		return this.courseGrade;
	}

	public void setCourseGrade(Integer courseGrade) {
		this.courseGrade = courseGrade;
	}

	public Integer getCourseGradeStatus() {
		return this.courseGradeStatus;
	}

	public void setCourseGradeStatus(Integer courseGradeStatus) {
		this.courseGradeStatus = courseGradeStatus;
	}

	@Override
	public int compareTo(CourseElective o) {
		if (o.getElectiveTime().after(this.getElectiveTime())) {
			return 1;
		}else if (o.getElectiveTime().before(this.getElectiveTime())) {
			return -1;
		}
		return 0;
	}

}
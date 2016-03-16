package edu.dufe.oes.bean;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Course entity. @author MyEclipse Persistence Tools
 */

public class Course implements Comparable<Course>,java.io.Serializable {

	// Fields

	private Integer courseID;
	private Semester semester;
	private College college;
	private Teacher teacher;
	private String courseNumber;
	private String courseOrder;
	private Timestamp createDate;
	private String courseName;
	private Set lessons = new HashSet(0);
	private Set groupTemplates = new HashSet(0);
	private Set courseElectives = new HashSet(0);
	private Set evaluationTemplates = new HashSet(0);

	// Constructors

	/** default constructor */
	public Course() {
	}

	/** full constructor */
	public Course(Semester semester, College college, Teacher teacher,
			String courseNumber, String courseOrder, Timestamp createDate,
			String courseName, Set lessons, Set groupTemplates,
			Set courseElectives, Set evaluationTemplates) {
		this.semester = semester;
		this.college = college;
		this.teacher = teacher;
		this.courseNumber = courseNumber;
		this.courseOrder = courseOrder;
		this.createDate = createDate;
		this.courseName = courseName;
		this.lessons = lessons;
		this.groupTemplates = groupTemplates;
		this.courseElectives = courseElectives;
		this.evaluationTemplates = evaluationTemplates;
	}

	// Property accessors

	public Integer getCourseID() {
		return this.courseID;
	}

	public void setCourseID(Integer courseID) {
		this.courseID = courseID;
	}

	public Semester getSemester() {
		return this.semester;
	}

	public void setSemester(Semester semester) {
		this.semester = semester;
	}

	public College getCollege() {
		return this.college;
	}

	public void setCollege(College college) {
		this.college = college;
	}

	public Teacher getTeacher() {
		return this.teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getCourseNumber() {
		return this.courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public String getCourseOrder() {
		return this.courseOrder;
	}

	public void setCourseOrder(String courseOrder) {
		this.courseOrder = courseOrder;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getCourseName() {
		return this.courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Set getLessons() {
		return this.lessons;
	}

	public void setLessons(Set lessons) {
		this.lessons = lessons;
	}

	public Set getGroupTemplates() {
		return this.groupTemplates;
	}

	public void setGroupTemplates(Set groupTemplates) {
		this.groupTemplates = groupTemplates;
	}

	public Set getCourseElectives() {
		return this.courseElectives;
	}

	public void setCourseElectives(Set courseElectives) {
		this.courseElectives = courseElectives;
	}

	public Set getEvaluationTemplates() {
		return this.evaluationTemplates;
	}

	public void setEvaluationTemplates(Set evaluationTemplates) {
		this.evaluationTemplates = evaluationTemplates;
	}

	@Override
	public int compareTo(Course o) {
		if (o.getCreateDate().after(this.getCreateDate())) {
			return -1;
		}else if (o.getCreateDate().before(this.getCreateDate())) {
			return 1;
		}
		return 0;
	}

}
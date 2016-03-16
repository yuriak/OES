package edu.dufe.oes.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * Semester entity. @author MyEclipse Persistence Tools
 */

public class Semester implements Comparable<Semester>,java.io.Serializable {

	// Fields

	private Integer semesterID;
	private Integer semesterName;
	private Set courses = new HashSet(0);

	// Constructors

	/** default constructor */
	public Semester() {
	}

	/** full constructor */
	public Semester(Integer semesterName, Set courses) {
		this.semesterName = semesterName;
		this.courses = courses;
	}

	// Property accessors

	public Integer getSemesterID() {
		return this.semesterID;
	}

	public void setSemesterID(Integer semesterID) {
		this.semesterID = semesterID;
	}

	public Integer getSemesterName() {
		return this.semesterName;
	}

	public void setSemesterName(Integer semesterName) {
		this.semesterName = semesterName;
	}

	public Set getCourses() {
		return this.courses;
	}

	public void setCourses(Set courses) {
		this.courses = courses;
	}

	@Override
	public int compareTo(Semester o) {
		if (o.getSemesterName()>this.getSemesterName()) {
			return 1;
		}else if (o.getSemesterName()<this.getSemesterName()) {
			return -1;
		}
		return 0;
	}

}
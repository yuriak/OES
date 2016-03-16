package edu.dufe.oes.bean;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Evaluation entity. @author MyEclipse Persistence Tools
 */

public class Evaluation implements Comparable<Evaluation>,java.io.Serializable {

	// Fields

	private Integer evaluationID;
	private Lesson lesson;
	private Timestamp evaluationTime;
	private String evaluationTitle;
	private Integer evaluationStatus;
	private Boolean isGroupEvaluation;
	private Set groups = new HashSet(0);
	private Set evaluationTemplates = new HashSet(0);
	private Set evaluationFields = new HashSet(0);
	private Set evaluationReceivers = new HashSet(0);
	
	//status
	public static final int BEFORE=-1;
	public static final int DOING=0;
	public static final int AFTER=1;

	// Constructors

	/** default constructor */
	public Evaluation() {
	}

	/** full constructor */
	public Evaluation(Lesson lesson, Timestamp evaluationTime,
			String evaluationTitle, Integer evaluationStatus,
			Boolean isGroupEvaluation, Set groups, Set evaluationTemplates,
			Set evaluationFields, Set evaluationReceivers) {
		this.lesson = lesson;
		this.evaluationTime = evaluationTime;
		this.evaluationTitle = evaluationTitle;
		this.evaluationStatus = evaluationStatus;
		this.isGroupEvaluation = isGroupEvaluation;
		this.groups = groups;
		this.evaluationTemplates = evaluationTemplates;
		this.evaluationFields = evaluationFields;
		this.evaluationReceivers = evaluationReceivers;
	}

	// Property accessors

	public Integer getEvaluationID() {
		return this.evaluationID;
	}

	public void setEvaluationID(Integer evaluationID) {
		this.evaluationID = evaluationID;
	}

	public Lesson getLesson() {
		return this.lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public Timestamp getEvaluationTime() {
		return this.evaluationTime;
	}

	public void setEvaluationTime(Timestamp evaluationTime) {
		this.evaluationTime = evaluationTime;
	}

	public String getEvaluationTitle() {
		return this.evaluationTitle;
	}

	public void setEvaluationTitle(String evaluationTitle) {
		this.evaluationTitle = evaluationTitle;
	}

	public Integer getEvaluationStatus() {
		return this.evaluationStatus;
	}

	public void setEvaluationStatus(Integer evaluationStatus) {
		this.evaluationStatus = evaluationStatus;
	}

	public Boolean getIsGroupEvaluation() {
		return this.isGroupEvaluation;
	}

	public void setIsGroupEvaluation(Boolean isGroupEvaluation) {
		this.isGroupEvaluation = isGroupEvaluation;
	}

	public Set getGroups() {
		return this.groups;
	}

	public void setGroups(Set groups) {
		this.groups = groups;
	}

	public Set getEvaluationTemplates() {
		return this.evaluationTemplates;
	}

	public void setEvaluationTemplates(Set evaluationTemplates) {
		this.evaluationTemplates = evaluationTemplates;
	}

	public Set getEvaluationFields() {
		return this.evaluationFields;
	}

	public void setEvaluationFields(Set evaluationFields) {
		this.evaluationFields = evaluationFields;
	}

	public Set getEvaluationReceivers() {
		return this.evaluationReceivers;
	}

	public void setEvaluationReceivers(Set evaluationReceivers) {
		this.evaluationReceivers = evaluationReceivers;
	}

	@Override
	public int compareTo(Evaluation o) {
		if (o.getEvaluationTime().after(this.getEvaluationTime())) {
			return -1;
		}else if (o.getEvaluationTime().before(this.evaluationTime)) {
			return 1;
		}
		return 0;
	}

}
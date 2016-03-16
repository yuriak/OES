package edu.dufe.oes.bean;

import java.sql.Timestamp;

/**
 * EvaluationTemplate entity. @author MyEclipse Persistence Tools
 */

public class EvaluationTemplate implements Comparable<EvaluationTemplate>, java.io.Serializable {

	// Fields

	private Integer evaluationTemplateID;
	private Evaluation evaluation;
	private Course course;
	private String evaluationTemplateName;
	private Timestamp backupTime;

	// Constructors

	/** default constructor */
	public EvaluationTemplate() {
	}

	/** full constructor */
	public EvaluationTemplate(Evaluation evaluation, Course course,
			String evaluationTemplateName, Timestamp backupTime) {
		this.evaluation = evaluation;
		this.course = course;
		this.evaluationTemplateName = evaluationTemplateName;
		this.backupTime = backupTime;
	}

	// Property accessors

	public Integer getEvaluationTemplateID() {
		return this.evaluationTemplateID;
	}

	public void setEvaluationTemplateID(Integer evaluationTemplateID) {
		this.evaluationTemplateID = evaluationTemplateID;
	}

	public Evaluation getEvaluation() {
		return this.evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getEvaluationTemplateName() {
		return this.evaluationTemplateName;
	}

	public void setEvaluationTemplateName(String evaluationTemplateName) {
		this.evaluationTemplateName = evaluationTemplateName;
	}

	public Timestamp getBackupTime() {
		return this.backupTime;
	}

	public void setBackupTime(Timestamp backupTime) {
		this.backupTime = backupTime;
	}

	@Override
	public int compareTo(EvaluationTemplate o) {
		if (o.getBackupTime().after(this.getBackupTime())) {
			return -1;
		}else if (o.getBackupTime().before(this.getBackupTime())) {
			return 1;
		}
		return 0;
	}

}
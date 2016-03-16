package edu.dufe.oes.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * EvaluationResult entity. @author MyEclipse Persistence Tools
 */

public class EvaluationResult implements Comparable<EvaluationResult>,java.io.Serializable {

	// Fields

	private Integer evaluationResultID;
	private EvaluationField evaluationField;
	private EvaluationReceiver evaluationReceiver;
	private Student student;
	private Set multipleOptionResults = new HashSet(0);
	private Set singleOptionResults = new HashSet(0);
	private Set valueResults = new HashSet(0);
	private Set textResults = new HashSet(0);

	// Constructors

	/** default constructor */
	public EvaluationResult() {
	}

	/** full constructor */
	public EvaluationResult(EvaluationField evaluationField,
			EvaluationReceiver evaluationReceiver, Student student,
			Set multipleOptionResults, Set singleOptionResults,
			Set valueResults, Set textResults) {
		this.evaluationField = evaluationField;
		this.evaluationReceiver = evaluationReceiver;
		this.student = student;
		this.multipleOptionResults = multipleOptionResults;
		this.singleOptionResults = singleOptionResults;
		this.valueResults = valueResults;
		this.textResults = textResults;
	}

	// Property accessors

	public Integer getEvaluationResultID() {
		return this.evaluationResultID;
	}

	public void setEvaluationResultID(Integer evaluationResultID) {
		this.evaluationResultID = evaluationResultID;
	}

	public EvaluationField getEvaluationField() {
		return this.evaluationField;
	}

	public void setEvaluationField(EvaluationField evaluationField) {
		this.evaluationField = evaluationField;
	}

	public EvaluationReceiver getEvaluationReceiver() {
		return this.evaluationReceiver;
	}

	public void setEvaluationReceiver(EvaluationReceiver evaluationReceiver) {
		this.evaluationReceiver = evaluationReceiver;
	}

	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Set getMultipleOptionResults() {
		return this.multipleOptionResults;
	}

	public void setMultipleOptionResults(Set multipleOptionResults) {
		this.multipleOptionResults = multipleOptionResults;
	}

	public Set getSingleOptionResults() {
		return this.singleOptionResults;
	}

	public void setSingleOptionResults(Set singleOptionResults) {
		this.singleOptionResults = singleOptionResults;
	}

	public Set getValueResults() {
		return this.valueResults;
	}

	public void setValueResults(Set valueResults) {
		this.valueResults = valueResults;
	}

	public Set getTextResults() {
		return this.textResults;
	}

	public void setTextResults(Set textResults) {
		this.textResults = textResults;
	}

	@Override
	public int compareTo(EvaluationResult o) {
		if (o.getEvaluationResultID()>this.getEvaluationResultID()) {
			return -1;
		}else if (o.getEvaluationResultID()<this.getEvaluationResultID()) {
			return 1;
		}
		return 0;
	}

}
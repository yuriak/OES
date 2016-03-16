package edu.dufe.oes.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * EvaluationReceiver entity. @author MyEclipse Persistence Tools
 */

public class EvaluationReceiver implements Comparable<EvaluationReceiver>,java.io.Serializable {

	// Fields

	public static final int PERSON_RECEIVER = 0;
	public static final int GROUP_RECEIVER = 1;
	public static final int EVALUATED = 1;
	public static final int NOTEVALUATED = 0;
	
	private Integer evaluationReceiverID;
	private Evaluation evaluation;
	private Group group;
	private Student student;
	private Integer evaluationGrade;
	private Integer receiverType;
	private Set evaluationResults = new HashSet(0);

	// Constructors

	/** default constructor */
	public EvaluationReceiver() {
	}

	/** full constructor */
	public EvaluationReceiver(Evaluation evaluation, Group group,
			Student student, Integer evaluationGrade, Integer receiverType,
			Set evaluationResults) {
		this.evaluation = evaluation;
		this.group = group;
		this.student = student;
		this.evaluationGrade = evaluationGrade;
		this.receiverType = receiverType;
		this.evaluationResults = evaluationResults;
	}

	// Property accessors

	public Integer getEvaluationReceiverID() {
		return this.evaluationReceiverID;
	}

	public void setEvaluationReceiverID(Integer evaluationReceiverID) {
		this.evaluationReceiverID = evaluationReceiverID;
	}

	public Evaluation getEvaluation() {
		return this.evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	public Group getGroup() {
		return this.group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Integer getEvaluationGrade() {
		return this.evaluationGrade;
	}

	public void setEvaluationGrade(Integer evaluationGrade) {
		this.evaluationGrade = evaluationGrade;
	}

	public Integer getReceiverType() {
		return this.receiverType;
	}

	public void setReceiverType(Integer receiverType) {
		this.receiverType = receiverType;
	}

	public Set getEvaluationResults() {
		return this.evaluationResults;
	}

	public void setEvaluationResults(Set evaluationResults) {
		this.evaluationResults = evaluationResults;
	}

	@Override
	public int compareTo(EvaluationReceiver o) {
		if (o.getEvaluationReceiverID()>this.getEvaluationReceiverID()) {
			return -1;
		}else if (o.getEvaluationReceiverID()<this.getEvaluationReceiverID()) {
			return 1;
		}
		return 0;
	}

}
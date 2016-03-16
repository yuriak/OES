package edu.dufe.oes.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * EvaluationField entity. @author MyEclipse Persistence Tools
 */

public class EvaluationField implements Comparable<EvaluationField>,java.io.Serializable {

	// Fields

	private Integer evaluationFieldID;
	private Evaluation evaluation;
	private ResultType resultType;
	private String fieldContent;
	private Set evaluationResults = new HashSet(0);
	private Set optionTitles = new HashSet(0);

	// Constructors

	/** default constructor */
	public EvaluationField() {
	}

	/** full constructor */
	public EvaluationField(Evaluation evaluation, ResultType resultType,
			String fieldContent, Set evaluationResults, Set optionTitles) {
		this.evaluation = evaluation;
		this.resultType = resultType;
		this.fieldContent = fieldContent;
		this.evaluationResults = evaluationResults;
		this.optionTitles = optionTitles;
	}

	// Property accessors

	public Integer getEvaluationFieldID() {
		return this.evaluationFieldID;
	}

	public void setEvaluationFieldID(Integer evaluationFieldID) {
		this.evaluationFieldID = evaluationFieldID;
	}

	public Evaluation getEvaluation() {
		return this.evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	public ResultType getResultType() {
		return this.resultType;
	}

	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

	public String getFieldContent() {
		return this.fieldContent;
	}

	public void setFieldContent(String fieldContent) {
		this.fieldContent = fieldContent;
	}

	public Set getEvaluationResults() {
		return this.evaluationResults;
	}

	public void setEvaluationResults(Set evaluationResults) {
		this.evaluationResults = evaluationResults;
	}

	public Set getOptionTitles() {
		return this.optionTitles;
	}

	public void setOptionTitles(Set optionTitles) {
		this.optionTitles = optionTitles;
	}

	@Override
	public int compareTo(EvaluationField o) {
		if (o.getEvaluationFieldID()>this.getEvaluationFieldID()) {
			return -1;
		}else if (o.getEvaluationFieldID()<this.getEvaluationFieldID()) {
			return 1;
		}
		return 0;
	}

}
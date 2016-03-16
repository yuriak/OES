package edu.dufe.oes.bean;

/**
 * ValueResult entity. @author MyEclipse Persistence Tools
 */

public class ValueResult implements java.io.Serializable {

	// Fields

	private Integer valueResultID;
	private EvaluationResult evaluationResult;
	private Integer resultValue;

	// Constructors

	/** default constructor */
	public ValueResult() {
	}

	/** full constructor */
	public ValueResult(EvaluationResult evaluationResult, Integer resultValue) {
		this.evaluationResult = evaluationResult;
		this.resultValue = resultValue;
	}

	// Property accessors

	public Integer getValueResultID() {
		return this.valueResultID;
	}

	public void setValueResultID(Integer valueResultID) {
		this.valueResultID = valueResultID;
	}

	public EvaluationResult getEvaluationResult() {
		return this.evaluationResult;
	}

	public void setEvaluationResult(EvaluationResult evaluationResult) {
		this.evaluationResult = evaluationResult;
	}

	public Integer getResultValue() {
		return this.resultValue;
	}

	public void setResultValue(Integer resultValue) {
		this.resultValue = resultValue;
	}

}
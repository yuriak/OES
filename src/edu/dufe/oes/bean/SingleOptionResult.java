package edu.dufe.oes.bean;

/**
 * SingleOptionResult entity. @author MyEclipse Persistence Tools
 */

public class SingleOptionResult implements java.io.Serializable {

	// Fields

	private Integer singleOptionResultID;
	private EvaluationResult evaluationResult;
	private String optionValue;

	// Constructors

	/** default constructor */
	public SingleOptionResult() {
	}

	/** full constructor */
	public SingleOptionResult(EvaluationResult evaluationResult,
			String optionValue) {
		this.evaluationResult = evaluationResult;
		this.optionValue = optionValue;
	}

	// Property accessors

	public Integer getSingleOptionResultID() {
		return this.singleOptionResultID;
	}

	public void setSingleOptionResultID(Integer singleOptionResultID) {
		this.singleOptionResultID = singleOptionResultID;
	}

	public EvaluationResult getEvaluationResult() {
		return this.evaluationResult;
	}

	public void setEvaluationResult(EvaluationResult evaluationResult) {
		this.evaluationResult = evaluationResult;
	}

	public String getOptionValue() {
		return this.optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

}
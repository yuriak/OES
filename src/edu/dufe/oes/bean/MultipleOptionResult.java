package edu.dufe.oes.bean;

/**
 * MultipleOptionResult entity. @author MyEclipse Persistence Tools
 */

public class MultipleOptionResult implements java.io.Serializable {

	// Fields

	private Integer multipleOptionResultID;
	private EvaluationResult evaluationResult;
	private String optionValue;

	// Constructors

	/** default constructor */
	public MultipleOptionResult() {
	}

	/** full constructor */
	public MultipleOptionResult(EvaluationResult evaluationResult,
			String optionValue) {
		this.evaluationResult = evaluationResult;
		this.optionValue = optionValue;
	}

	// Property accessors

	public Integer getMultipleOptionResultID() {
		return this.multipleOptionResultID;
	}

	public void setMultipleOptionResultID(Integer multipleOptionResultID) {
		this.multipleOptionResultID = multipleOptionResultID;
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
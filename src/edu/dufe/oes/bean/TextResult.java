package edu.dufe.oes.bean;

/**
 * TextResult entity. @author MyEclipse Persistence Tools
 */

public class TextResult implements java.io.Serializable {

	// Fields

	private Integer textResultID;
	private EvaluationResult evaluationResult;
	private String resultText;

	// Constructors

	/** default constructor */
	public TextResult() {
	}

	/** full constructor */
	public TextResult(EvaluationResult evaluationResult, String resultText) {
		this.evaluationResult = evaluationResult;
		this.resultText = resultText;
	}

	// Property accessors

	public Integer getTextResultID() {
		return this.textResultID;
	}

	public void setTextResultID(Integer textResultID) {
		this.textResultID = textResultID;
	}

	public EvaluationResult getEvaluationResult() {
		return this.evaluationResult;
	}

	public void setEvaluationResult(EvaluationResult evaluationResult) {
		this.evaluationResult = evaluationResult;
	}

	public String getResultText() {
		return this.resultText;
	}

	public void setResultText(String resultText) {
		this.resultText = resultText;
	}

}
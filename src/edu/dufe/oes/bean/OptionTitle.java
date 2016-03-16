package edu.dufe.oes.bean;

/**
 * OptionTitle entity. @author MyEclipse Persistence Tools
 */

public class OptionTitle implements Comparable<OptionTitle>,java.io.Serializable {

	// Fields

	private Integer optionTitleID;
	private EvaluationField evaluationField;
	private String optionKey;
	private String optionTitleContent;

	// Constructors

	/** default constructor */
	public OptionTitle() {
	}

	/** full constructor */
	public OptionTitle(EvaluationField evaluationField, String optionKey,
			String optionTitleContent) {
		this.evaluationField = evaluationField;
		this.optionKey = optionKey;
		this.optionTitleContent = optionTitleContent;
	}

	// Property accessors

	public Integer getOptionTitleID() {
		return this.optionTitleID;
	}

	public void setOptionTitleID(Integer optionTitleID) {
		this.optionTitleID = optionTitleID;
	}

	public EvaluationField getEvaluationField() {
		return this.evaluationField;
	}

	public void setEvaluationField(EvaluationField evaluationField) {
		this.evaluationField = evaluationField;
	}

	public String getOptionKey() {
		return this.optionKey;
	}

	public void setOptionKey(String optionKey) {
		this.optionKey = optionKey;
	}

	public String getOptionTitleContent() {
		return this.optionTitleContent;
	}

	public void setOptionTitleContent(String optionTitleContent) {
		this.optionTitleContent = optionTitleContent;
	}

	@Override
	public int compareTo(OptionTitle o) {
		if (o.getOptionKey().charAt(0)>this.getOptionKey().charAt(0)) {
			return -1;
		}else if (o.getOptionKey().charAt(0)<this.getOptionKey().charAt(0)) {
			return 1;
		}
		return 0;
	}

}
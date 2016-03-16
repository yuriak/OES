package edu.dufe.oes.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * ResultType entity. @author MyEclipse Persistence Tools
 */

public class ResultType implements java.io.Serializable {

	// Fields

	private Integer resultTypeID;
	private String resultTypeName;
	private Set evaluationFields = new HashSet(0);
	
	public static final int VALUE_RESULT=1;
	public static final int SINGLE_OPTION_RESULT=2;
	public static final int MULTIPLE_OPTION_RESULT=3;
	public static final int TEXT_RESULT=4;

	// Constructors

	/** default constructor */
	public ResultType() {
	}

	/** full constructor */
	public ResultType(String resultTypeName, Set evaluationFields) {
		this.resultTypeName = resultTypeName;
		this.evaluationFields = evaluationFields;
	}

	// Property accessors

	public Integer getResultTypeID() {
		return this.resultTypeID;
	}

	public void setResultTypeID(Integer resultTypeID) {
		this.resultTypeID = resultTypeID;
	}

	public String getResultTypeName() {
		return this.resultTypeName;
	}

	public void setResultTypeName(String resultTypeName) {
		this.resultTypeName = resultTypeName;
	}

	public Set getEvaluationFields() {
		return this.evaluationFields;
	}

	public void setEvaluationFields(Set evaluationFields) {
		this.evaluationFields = evaluationFields;
	}

}
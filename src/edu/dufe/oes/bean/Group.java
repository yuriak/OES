package edu.dufe.oes.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * Group entity. @author MyEclipse Persistence Tools
 */

public class Group implements Comparable<Group>,java.io.Serializable {

	// Fields

	private Integer groupID;
	private Evaluation evaluation;
	private Integer groupNumber;
	private Set evaluationReceivers = new HashSet(0);
	private Set groupMembers = new HashSet(0);
	private Set groupTemplates = new HashSet(0);

	// Constructors

	/** default constructor */
	public Group() {
	}

	/** full constructor */
	public Group(Evaluation evaluation, Integer groupNumber,
			Set evaluationReceivers, Set groupMembers, Set groupTemplates) {
		this.evaluation = evaluation;
		this.groupNumber = groupNumber;
		this.evaluationReceivers = evaluationReceivers;
		this.groupMembers = groupMembers;
		this.groupTemplates = groupTemplates;
	}

	// Property accessors

	public Integer getGroupID() {
		return this.groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public Evaluation getEvaluation() {
		return this.evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	public Integer getGroupNumber() {
		return this.groupNumber;
	}

	public void setGroupNumber(Integer groupNumber) {
		this.groupNumber = groupNumber;
	}

	public Set getEvaluationReceivers() {
		return this.evaluationReceivers;
	}

	public void setEvaluationReceivers(Set evaluationReceivers) {
		this.evaluationReceivers = evaluationReceivers;
	}

	public Set getGroupMembers() {
		return this.groupMembers;
	}

	public void setGroupMembers(Set groupMembers) {
		this.groupMembers = groupMembers;
	}

	public Set getGroupTemplates() {
		return this.groupTemplates;
	}

	public void setGroupTemplates(Set groupTemplates) {
		this.groupTemplates = groupTemplates;
	}

	@Override
	public int compareTo(Group o) {
		if (o.getGroupID()>this.getGroupID()) {
			return -1;
		}else if (o.getGroupID()<this.getGroupID()) {
			return 1;
		}
		return 0;
	}

}
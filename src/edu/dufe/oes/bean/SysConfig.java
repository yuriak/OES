package edu.dufe.oes.bean;

/**
 * SysConfig entity. @author MyEclipse Persistence Tools
 */

public class SysConfig implements java.io.Serializable {

	// Fields

	private Integer configID;
	private String configKey;
	private String configValue;

	// Constructors

	/** default constructor */
	public SysConfig() {
	}

	/** full constructor */
	public SysConfig(String configKey, String configValue) {
		this.configKey = configKey;
		this.configValue = configValue;
	}

	// Property accessors

	public Integer getConfigID() {
		return this.configID;
	}

	public void setConfigID(Integer configID) {
		this.configID = configID;
	}

	public String getConfigKey() {
		return this.configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return this.configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

}
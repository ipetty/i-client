/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.update;

/**
 *
 * @author Administrator
 */
public class UpdateVO {

	private Integer versionCode; // 数值版本号,用于版本更新比较
	private String versionName; // 字符串版本号，呈现给用户界面
	private String versionDescription; // 更新内容
	private String downloadUrl; // app下载地址（完整路径）
	private Boolean forceUpdate; //是否强制升级

	/**
	 * @return the versionCode
	 */
	public Integer getVersionCode() {
		return versionCode;
	}

	/**
	 * @param versionCode the versionCode to set
	 */
	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

	/**
	 * @return the versionName
	 */
	public String getVersionName() {
		return versionName;
	}

	/**
	 * @param versionName the versionName to set
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/**
	 * @return the versionDescription
	 */
	public String getVersionDescription() {
		return versionDescription;
	}

	/**
	 * @param versionDescription the versionDescription to set
	 */
	public void setVersionDescription(String versionDescription) {
		this.versionDescription = versionDescription;
	}

	/**
	 * @return the downloadUrl
	 */
	public String getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 * @param downloadUrl the downloadUrl to set
	 */
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	/**
	 * @return the forceUpdate
	 */
	public Boolean getForceUpdate() {
		return forceUpdate;
	}

	/**
	 * @param forceUpdate the forceUpdate to set
	 */
	public void setForceUpdate(Boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

}

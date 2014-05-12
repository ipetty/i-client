package net.ipetty.android.sdk.domain;

/**
 * 应用升级信息
 *
 * @author xiaojinghai
 */
public class IpetAppUpdate {

    // 数值版本号,用于版本更新比较
    private Integer versionCode;

    // 字符串版本号，呈现给用户界面
    private String versionName;

    // 更新内容
    private String updateText;

    // app下载地址（完整路径）
    private String appDownloadUrl;

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getAppDownloadUrl() {
        return appDownloadUrl;
    }

    public void setAppDownloadUrl(String appDownloadUrl) {
        this.appDownloadUrl = appDownloadUrl;
    }

    /**
     * @return the updateText
     */
    public String getUpdateText() {
        return updateText;
    }

    /**
     * @param updateText the updateText to set
     */
    public void setUpdateText(String updateText) {
        this.updateText = updateText;
    }

}

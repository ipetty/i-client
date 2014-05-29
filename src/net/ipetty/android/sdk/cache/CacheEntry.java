/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.cache;

import org.apaches.commons.codec.digest.DigestUtils;
import org.springframework.util.Assert;

/**
 * 缓存实体
 *
 * @author xiao
 */
public class CacheEntry {

    private String id;			//MD5(URI)
    private String url;       	//请求的URI
    private String value;     	//返回值
    private String etag;		//Etag
    private Long hits;   		//命中次数
    private Long putOn;   		//记录插入时间戳（毫秒数）
    private Long expireOn;   	//过期时间戳（毫秒数）
    private String classType;   //value的Java类型

    public CacheEntry(String url) {
        Assert.notNull(url, "URL不能为空");
        this.id = DigestUtils.md5Hex(url);
        this.url = url;
        this.hits = 0l;
        this.expireOn = 0l;
    }

    public CacheEntry(String key, String value, String etag, Long expireOn, String classType) {
        this(key);
        Assert.notNull(value, "value不能为空");
        Assert.notNull(etag, "etag不能为空");
        Assert.notNull(expireOn, "expireOn不能为空");
        Assert.notNull(classType, "classType不能为空");
        this.value = value;
        this.etag = etag;
        this.expireOn = expireOn;
        this.classType = classType;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public Long getPutOn() {
        return putOn;
    }

    public void setPutOn(Long putOn) {
        this.putOn = putOn;
    }

    public Long getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(Long expireOn) {
        this.expireOn = expireOn;
    }

    /**
     * @return the classType
     */
    public String getClassType() {
        return classType;
    }

    /**
     * @param classType the classType to set
     */
    public void setClassType(String classType) {
        this.classType = classType;
    }

}

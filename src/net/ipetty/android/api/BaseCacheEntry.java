/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.api;

/**
 * 缓存实体
 *
 * @author xiao
 */
public class BaseCacheEntry<T> {

    private Integer id;		//id

    private T data;     	//返回值

    private Long putOn;   	//记录插入时间戳（毫秒数）

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * @return the putOn
     */
    public Long getPutOn() {
        return putOn;
    }

    /**
     * @param putOn the putOn to set
     */
    public void setPutOn(Long putOn) {
        this.putOn = putOn;
    }

}

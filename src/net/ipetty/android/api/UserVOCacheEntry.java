/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.api;

import net.ipetty.vo.UserVO;

/**
 *
 * @author Administrator
 */
public class UserVOCacheEntry {

    private Integer id;		//id

    private UserVO data;     	//返回值

    private Long expireOn;   	//记录插入时间戳（毫秒数）

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
    public UserVO getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(UserVO data) {
        this.data = data;
    }

    /**
     * @return the putOn
     */
    public Long getExpireOn() {
        return expireOn;
    }

    /**
     * @param putOn the putOn to set
     */
    public void setExpireOn(Long expireOn) {
        this.expireOn = expireOn;
    }
}

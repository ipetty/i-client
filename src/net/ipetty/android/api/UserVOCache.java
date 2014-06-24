/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.api;

import java.util.Collections;
import java.util.Map;
import net.ipetty.android.sdk.cache.LRULinkedHashMap;
import net.ipetty.vo.UserVO;

/**
 *
 * @author Administrator
 */
public class UserVOCache {

    private final Map<Integer, UserVOCacheEntry> data;
    private final Integer expireIn = 60 * 1000; //过期时间(毫秒)

    public UserVOCache(int maxNum) {
        data = Collections.synchronizedMap(new LRULinkedHashMap<Integer, UserVOCacheEntry>(maxNum));
    }

    public void put(UserVO user) {
        //不重复更新
        if (data.containsKey(user.getId())) {
            return;
        }

        UserVOCacheEntry entry = new UserVOCacheEntry();
        entry.setId(user.getId());
        entry.setData(user);
        entry.setExpireOn(System.currentTimeMillis() + expireIn);
        data.put(user.getId(), entry);
    }

    public UserVO get(Integer id) {
        //有缓存
        if (data.containsKey(id)) {
            UserVOCacheEntry entry = data.get(id);
            //已超时
            if (System.currentTimeMillis() > entry.getExpireOn()) {
                data.remove(id);
                return null;
            } else {
                return entry.getData();
            }
        }
        return null;
    }

    public void remove(Integer id) {
        data.remove(id);
    }
}

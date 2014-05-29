/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.cache;

import java.util.Collections;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class Cache4Mem {
	
	
    private final Map<String, CacheEntry> data;

    public Cache4Mem(int maxNum){
    	data
        = Collections.synchronizedMap(new LRULinkedHashMap<String, CacheEntry>(maxNum));
    }
    
    public void put(CacheEntry e) {

        data.put(e.getId(), e);
    }
    
    public CacheEntry get(String url) {
    	CacheEntry ret = get(new CacheEntry(url));
        return ret;
    }

    private CacheEntry get(CacheEntry e) {

        return data.get(e.getId());
    }

    public void remove(CacheEntry e) {

        data.remove(e.getId());
    }
}

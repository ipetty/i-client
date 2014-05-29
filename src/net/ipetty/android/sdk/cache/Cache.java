/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.cache;

import android.content.Context;

/**
 * 二级缓存
 *
 * @author Administrator
 */
public class Cache {

    private final Cache4Mem l1;
    private final Cache4DB l2;

    public Cache(Context cxt, int maxNumL1, int maxNumL2) {
        l1 = new Cache4Mem(maxNumL1);
        l2 = new Cache4DB(cxt, maxNumL2);
    }

    public void put(CacheEntry e) {
        l1.put(e);
        l2.put(e);
    }

    public CacheEntry get(String url) {
        CacheEntry e = l1.get(url);
        if (null == e) {
            e = l2.get(url);
        }
        return e;
    }

    public void remove(CacheEntry e) {
        l1.remove(e);
        l2.remove(e);
    }

}

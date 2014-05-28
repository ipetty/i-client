/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.cache;

import android.content.Context;

/**
 *
 * @author Administrator
 */
public class Cache4DB {

    private final int maxNum;
    private final CacheDAO dao;

    public Cache4DB(Context cxt, int maxNum) {
        dao = new CacheDAO(cxt);
        this.maxNum = maxNum;
    }

    public synchronized void put(CacheEntry e) {
        //如果找到则更新
        if (dao.find(e) != null) {
            dao.update(e);
            return;
        }
        //插入前，若超过数量限制则删除最久未使用记录
        int num = dao.getCount();
        if (num >= maxNum) {
            CacheEntry entry = dao.getLRU();
            dao.remove(entry);
        }
        dao.insert(e);
    }

    public synchronized CacheEntry get(String url) {
        CacheEntry ret = get(new CacheEntry(url));
        if (null != ret) {
            ret.setHits(ret.getHits() + 1);
            dao.update(ret);
        }
        return ret;
    }

    public synchronized void remove(CacheEntry e) {
        dao.remove(e);
    }

    private CacheEntry get(CacheEntry e) {
        return dao.find(e);
    }
}

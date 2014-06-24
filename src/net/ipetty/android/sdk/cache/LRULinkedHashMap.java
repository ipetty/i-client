/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU哈希链表 LinkedHashMap有个特性，get以后元素会上移到第一位，所以超过容量限制则删除最后一位即可。 LinkedHashMap
 * 默认实现不移除最老元素，所以这里重写removeEldestEntry方法实现
 *
 * @author Administrator
 */
public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {

    private final int capacity;   //定义缓存的容量
    private static final long serialVersionUID = 1L;

    public LRULinkedHashMap(int capacity) {
        // initialCapacity   初始容量
        //loadFactor    加载因子，一般是 0.75f
        //设置成 true 时，为最近最少使用（LRU）算法实现, 设置成 false 时，为先进入先过期
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    //超过容量限制删除最老的元素
    @Override
    public boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ipetty.android.sdk.cache;

import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class LRULinkedHashMapTest {
    
     public static void main(String[] args) throws Exception {
        //指定缓存最大容量为4
        Map<Integer, Integer> map = new LRULinkedHashMap<Integer, Integer>(4);
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        map.put(5, 5);  //此时1被删除
        map.get(2);     //此时2被移动到最前
        map.put(6, 6);  //此时3被删除
        //遍历结果应为：4,5,2,6
        for (Iterator<Map.Entry<Integer, Integer>> it = map.entrySet().iterator(); it.hasNext();) {
            System.out.println(it.next().getKey());
        }
    }
}

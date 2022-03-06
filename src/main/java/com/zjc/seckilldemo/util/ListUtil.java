package com.zjc.seckilldemo.util;

import java.util.*;

public class ListUtil {
    public static String findDuplicate(List<String> list1, List<String> list2) {
        List<String> duplicate = new ArrayList<String>();
        Map<String, Integer> map = new HashMap<String, Integer>();
        //1.去重
        List<String> list3 = new ArrayList<String>(new HashSet<String>(list1));
        List<String> list4 = new ArrayList<String>(new HashSet<String>(list2));
        //2.将list中的元素加入map中并进行统计
        for (String s1 : list3) {
            map.put(s1, 1);
        }
        //3.将list中的元素加入map中并进行统计
        for (String s2 : list4) {
            map.put(s2, (map.get(s2) == null ? 1 : 2));
        }
        //4.将重复的元素放入list中
        for (Map.Entry<String, Integer> m : map.entrySet()) {
            if (m.getValue() == 2) {
                duplicate.add(m.getKey());
            }
        }
        return duplicate.toString();
    }




}

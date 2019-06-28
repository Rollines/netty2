package com.cjl.netty.hashmap;

import java.util.HashMap;

/**
 * @author : junlinchen
 * @date: 2019/6/26 9:47
 * @version: 1.0
 * @Des : HashMap.containsKey()程序1：将字符串值映射到整数键。
 */
public class Hash_Map_Demo {
    public static void main(String[] args) {
        HashMap<Integer,String> hashMap = new HashMap<>();
        hashMap.put(10, "Geeks");
        hashMap.put(15, "4");
        hashMap.put(20, "Geeks");
        hashMap.put(25, "Welcomes");
        hashMap.put(30, "You");

        System.out.println("Initial Mappings are: " + hashMap);

        System.out.println("Is the key '20' present? " + hashMap.containsKey(20));

        System.out.println("Is the key '5' present? " + hashMap.containsKey(5));
    }
}

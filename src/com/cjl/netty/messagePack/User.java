package com.cjl.netty.messagePack;

import org.msgpack.annotation.Message;

/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-12 08:40
 * @Description:
 */
@Message
public class User {
    private String name;
    private int age;
    private String id;
    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", id='" + id + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}

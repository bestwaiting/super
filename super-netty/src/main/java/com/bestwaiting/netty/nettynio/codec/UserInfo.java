package com.bestwaiting.netty.nettynio.codec;

import org.msgpack.annotation.Message;

/**
 * Created by bestwaiting on 17/9/8.
 */
@Message
public class UserInfo {
    private String name;
    private int age;

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
}

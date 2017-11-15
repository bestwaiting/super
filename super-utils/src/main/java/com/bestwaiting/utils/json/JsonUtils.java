package com.bestwaiting.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.junit.Assert;

import java.util.Map;

/**
 * Created by bestwaiting on 17/5/12.
 */
public class JsonUtils {
    public static void main(String[] args) {
        Group group = new Group();

        User guestUser = new User();
        guestUser.setId(2L);
        guestUser.setName("guest");

        User rootUser = new User();
        rootUser.setId(3L);
        rootUser.setName("root");

        group.getUsers().add(guestUser);
        group.getUsers().add(rootUser);

        group.getData().put("1",guestUser);
        group.getData().put("2",rootUser);

        String jsonString = JSON.toJSONString(group);

        System.out.println(jsonString);
        JSONObject jsonObject=JSON.parseObject(jsonString);

        System.out.println(jsonObject.get("data"));
        Map<String,User> ll=JSON.parseObject(jsonObject.get("data").toString(),new TypeReference<Map<String, User>>(){});
        System.out.println(ll.get("1"));
        Group group2 = JSON.parseObject(jsonString, Group.class);

        System.out.println(group2.getUsers().get(0).getName());
        System.out.println(group2.getData().get("1").getId());
        Assert.assertEquals("fdd",2,group2.getUsers().size());

    }
}

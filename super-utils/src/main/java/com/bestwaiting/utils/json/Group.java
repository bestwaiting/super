package com.bestwaiting.utils.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bestwaiting on 17/5/12.
 */
public class Group {
    private List<User> users = new ArrayList<User>();

    private Map<String,User> data=new HashMap<>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Map<String, User> getData() {
        return data;
    }

    public void setData(Map<String, User> data) {
        this.data = data;
    }
}

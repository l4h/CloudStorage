package com.gb.cs.server;

import java.util.HashMap;

public class SimpleAuthService implements Auth<String>{
    HashMap<String,String> users;

    @Override
    public boolean loginSuccess(String [] cred) {
        boolean success=false;
        if(cred.length>1)
            success=(users.get(cred[0]).equals(cred[1]));

        return success;

    }

    public SimpleAuthService() {
        this.users = new HashMap<>();
        initUsersDB();
    }

    private void initUsersDB(){
        users.put("happy-cloudman","123");
        users.put("cloudman1","123");
        users.put("cloudman2","123");
        users.put("cloudman3","123");
    }
}

package com.oa.core.bean;


import com.oa.core.bean.user.UserManager;

import java.io.Serializable;

public class Loginer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id = null;
    private String name = null;
    private String ip = null;
    private String did = null;
    private String dname = null;

    private String photo = null;

    private Object loginer = null;
    private boolean user = false;

    public Loginer(Object idObject) {
        loginer = idObject;
        user = true;
        UserManager obj = (UserManager) loginer;
        id = obj.getUserName();
        name = obj.getUserName();
        ip = "";
        if (obj.getName() != null) {
            name = obj.getName();
        }
        if (obj.getComputerIP() != null) {
            ip = obj.getComputerIP();
        }
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getLoginer() {
        return loginer;
    }

    public void setLoginer(Object loginer) {
        this.loginer = loginer;
    }

    public boolean isUser() {
        return user;
    }

    public void setUser(boolean user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}

package com.easemob.server.example.comm;

import io.swagger.client.ApiException;

/**
 * Created by easemob on 2017/3/16.
 */
public interface EasemobAPI {
    Object invokeEasemobAPI() throws ApiException;

    public static String ORG_NAME = "1125190528168696";
    public static String APP_NAME = "zjy-oa";
    public static String GRANT_TYPE = "client_credentials";
    public static String CLIENT_ID = "YXA6rcjR8IGvEemgJLk2IZSr-g";
    public static String CLIENT_SECRET = "YXA6p1EfMIU-DTicSM3VxyQmQIvOrxw";
}
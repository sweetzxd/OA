package com.oa.core.scada.common;

public interface Common {

    String SYSTEM = "http://";
    /**
     * 描述：门禁服务IP
     */
    String SYSTEM_URL = "27.185.2.130";

    /**
     * 描述：门禁服务端口
     */
    String SYSTEM_PORT = "9000";

    /**
     * 描述：门禁服务连接地址
     */
    String WS_URL = "ws://"+SYSTEM_URL+":"+SYSTEM_PORT+"/video?url=";

    /**
     * 描述：门禁服务摄像头用户名
     */
    String CAMERA_USERNAME = "admin";
    /**
     * 描述：门禁服务摄像头用户密码
     */
    String CAMERA_PASSWORD = "admin123456";
    /**
     * 描述：门禁服务摄像头IP
     */
    String CAMERA_IP = "192.168.1.64";
    /**
     * 描述：门禁服务摄像头地址
     */
    String CAMERA_URL = "rtsp://"+CAMERA_USERNAME+":"+CAMERA_PASSWORD+"@"+CAMERA_IP+"/h264/ch1/main/av_stream";
    /**
     * 描述：门禁服务登录名
     */
    String SYSTEM_USERNAME = "test@megvii.com";

    /**
     * 描述：门禁服务登陆密码
     */
    String SYSTEM_PASSWORD = "123456";
    /**
     * 类型：POST
     * 描述：登陆
     */
    String LOGIN = ":12345/auth/login";

    /**
     * 类型：GET
     * 描述：获取所有用户列表
     */
    String ALL_USER = ":12345/mobile-admin/subjects";

    /**
     * 类型：GET
     * 描述：获取用户列表（分页分类可搜索）
     */
    String ALL_USER_PAGE = ":12345/mobile-admin/subjects/list/";

    /**
     * 类型：GET
     * 描述：查询用户
     */
    String SELECT_USER = ":12345/subject/";

    /**
     * 类型：POSG
     * 描述：创建用户，支持直接上传图片
     */
    String ADD_USER = ":12345/subject/file";

    /**
     * 类型：POSG
     * 描述：上传识别照片
     */
    String POST_PHOTO = ":12345/subject/photo";

    /**
     * 类型：POSG
     * 描述：入库图片质量判断接口
     */
    String POST_PHOTO_CHECK = ":12345/subject/photo/check";

    /**
     * 类型：POSG
     * 描述：上传头像接口
     */
    String UPDATE_AVATAR = ":12345/subject/avatar";

    /**
     * 类型：GET
     * 描述：历史识别记录
     */
    String EVENT_USER_LOG = ":12345/event/events";

}

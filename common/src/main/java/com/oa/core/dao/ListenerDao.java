package com.oa.core.dao;

import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.system.RoleDefines;
import com.oa.core.bean.user.UserManager;

import java.util.List;

public interface ListenerDao {

    /**
     * 字段定义
     * */
    public List<FieldData> listenerField();
    /**
     * 表定义
     * */
    public List<TableData> listenerTable();
    /**
     * 任务定义
     * */
    public List<TaskData> listenerTask();
    /**
     * 账户信息
     * */
    public List<UserManager> listenerUser();
    /**
     * 角色信息
     * */
    public List<RoleDefines> listenerRole();
}

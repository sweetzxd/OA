package com.oa.core.service;

import com.oa.core.bean.dd.FieldData;
import com.oa.core.bean.dd.TableData;
import com.oa.core.bean.dd.TaskData;
import com.oa.core.bean.system.RoleDefines;
import com.oa.core.bean.user.UserManager;
import com.oa.core.dao.ListenerDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("listenerService")
public class ListenerServiceImpl implements ListenerService {
    @Resource
    private ListenerDao listenerDao;

    /**
     * 字段定义
     */
    @Override
    public List<FieldData> listenerField() {
        return listenerDao.listenerField();
    }

    /**
     * 表定义
     */
    @Override
    public List<TableData> listenerTable() {
        return listenerDao.listenerTable();
    }

    /**
     * 任务定义
     */
    @Override
    public List<TaskData> listenerTask() {
        return listenerDao.listenerTask();
    }

    /**
     * 账户信息
     * */
    @Override
    public List<UserManager> listenerUser() {
        return listenerDao.listenerUser();
    }

    /**
     * 角色信息
     * */
    @Override
    public List<RoleDefines> listenerRole() {
        return listenerDao.listenerRole();
    }

}

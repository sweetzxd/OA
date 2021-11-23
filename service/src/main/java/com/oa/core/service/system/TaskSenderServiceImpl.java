package com.oa.core.service.system;

import com.oa.core.bean.system.TaskSender;
import com.oa.core.dao.system.TaskSenderDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:TaskSenderServiceImpl
 * @author:zxd
 * @Date:2018/09/27
 * @Time:上午 9:17
 * @Version V1.0
 * @Explain
 */
@Service("taskSenderService")
public class TaskSenderServiceImpl implements TaskSenderService {
    @Resource
    private TaskSenderDao taskSenderDao;

    @Override
    public void insert(TaskSender taskSender) {
        taskSenderDao.insert(taskSender);
    }

    @Override
    public void delete(TaskSender taskSender) {
        taskSenderDao.delete(taskSender);
    }

    @Override
    public void deletes(String workOrderNO) {
        taskSenderDao.deletes(workOrderNO);
    }

    @Override
    public void update(TaskSender taskSender) {
        taskSenderDao.update(taskSender);
    }

    @Override
    public void cleanTask(Map<String, String> map) {
        taskSenderDao.cleanTask(map);
    }

    @Override
    public List<TaskSender> selectAll() {
        return taskSenderDao.selectAll();
    }

    @Override
    public List<TaskSender> selectMsgStatus(int msgStatus) {
        return taskSenderDao.selectMsgStatus(msgStatus);
    }

    @Override
    public List<TaskSender> selectUser(String users) {
        return taskSenderDao.selectUser(users);
    }

    @Override
    public List<TaskSender> selectProc(String procId) {
        return taskSenderDao.selectProc(procId);
    }

    @Override
    public List<TaskSender> selectTerms(TaskSender taskSender) {
        return taskSenderDao.selectTerms(taskSender);
    }

    @Override
    public TaskSender selectById(String workOrderNO) {
        return taskSenderDao.selectById(workOrderNO);
    }

    @Override
    public int selectAllCount() {
        return taskSenderDao.selectAllCount();
    }

    @Override
    public int selectTermsCount(TaskSender taskSender) {
        return taskSenderDao.selectTermsCount(taskSender);
    }

    @Override
    public int selectByIdCount(String workOrderNO) {
        return taskSenderDao.selectByIdCount(workOrderNO);
    }

    @Override
    public int selectNextTask(Map<String, String> map) {
        return taskSenderDao.selectNextTask(map);
    }

    @Override
    public List<TaskSender> select4MsgStatus(TaskSender taskSender) {
        return taskSenderDao.select4MsgStatus(taskSender);
    }

    @Override
    public void update0MsgStatus(TaskSender taskSender) {
        taskSenderDao.update0MsgStatus(taskSender);
    }

    @Override
    public List<TaskSender> selectByHome(String accepter) {
        return taskSenderDao.selectByHome(accepter);
    }

    @Override
    public TaskSender selectJoblogTask(TaskSender taskSender) {
        return taskSenderDao.selectJoblogTask(taskSender);
    }
}

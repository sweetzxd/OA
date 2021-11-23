package com.oa.core.service.system;

import com.oa.core.bean.system.TaskSender;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TaskSenderService {
    void insert(TaskSender taskSender);
    void delete(TaskSender taskSender);
    void deletes(String workOrderNO);
    void update(TaskSender taskSender);
    void cleanTask(Map<String, String> map);
    List<TaskSender> selectAll();
    List<TaskSender> selectMsgStatus(int msgStatus);
    List<TaskSender> selectUser(String users);
    List<TaskSender> selectProc(String procId);
    List<TaskSender> selectTerms(TaskSender taskSender);
    TaskSender selectById(String workOrderNO);
    int selectAllCount();
    int selectTermsCount(TaskSender taskSender);
    int selectByIdCount(String workOrderNO);

    int selectNextTask(Map<String, String> map);

    List<TaskSender> select4MsgStatus(TaskSender taskSender);
    void update0MsgStatus(TaskSender taskSender);

    List<TaskSender> selectByHome(@Param("accepter")String accepter);

    TaskSender selectJoblogTask(TaskSender taskSender);
}

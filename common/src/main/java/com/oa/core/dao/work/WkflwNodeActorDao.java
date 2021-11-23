package com.oa.core.dao.work;

import com.oa.core.bean.work.WkflwNodeActor;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public interface WkflwNodeActorDao {

    void insert(WkflwNodeActor wkflwNodeActor);
    void delete(WkflwNodeActor wkflwNodeActor);
    void deletes(List nodeActorId);
    void deleteByNode(String nodeId);
    void update(WkflwNodeActor wkflwNodeActor);
    List<WkflwNodeActor> selectAll();
    List<WkflwNodeActor> selectTerms(WkflwNodeActor wkflwNodeActor);
    List<WkflwNodeActor> selectByIds(String nodeActorIds);
    WkflwNodeActor selectById(String nodeActorId);
    List<WkflwNodeActor> selectByNodeId(String nodeId);
    ArrayList<String> selectListActor(String nodeId);

    List<WkflwNodeActor> selectwkflwId(@Param("wkflwId") String wkflwId);
}

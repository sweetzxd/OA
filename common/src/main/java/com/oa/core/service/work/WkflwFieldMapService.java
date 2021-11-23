package com.oa.core.service.work;


import com.oa.core.bean.work.WkflwFieldMap;

import java.util.List;

public interface WkflwFieldMapService {

    void insert(WkflwFieldMap wkflwFieldMap);
    void delete(WkflwFieldMap wkflwFieldMap);
    void deletes(WkflwFieldMap wkflwFieldMap);
    void update(WkflwFieldMap wkflwFieldMap);
    WkflwFieldMap selectWkflwFieldMapById(String wkflwfieldmapId);
    List<WkflwFieldMap> selectWkflwFieldMapByWkflwId(String wkflwId);
    List<WkflwFieldMap> selectWkflwFieldMap(WkflwFieldMap wkflwFieldMap);
    int selectWkflwFieldMapByCount(WkflwFieldMap wkflwFieldMap);
    WkflwFieldMap selectWkflwFieldMapByFormFieldName(String formFieldName);
}

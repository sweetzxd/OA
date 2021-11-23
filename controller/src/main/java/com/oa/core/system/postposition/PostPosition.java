package com.oa.core.system.postposition;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:PostPosition
 * @author:zxd
 * @Date:2018/10/12
 * @Time:上午 9:57
 * @Version V1.0
 * @Explain 后置调用
 */
public class PostPosition {

    private String type = null;
    private String tableName = null;
    private String saveType = null;
    private String workOrderNO = null;
    private String wkflwId = null;
    private String procId = null;
    private String recorderNO = null;
    private Map tableMap = null;

    public PostPosition() {
    }

    public PostPosition(String type) {
        setType(type);
    }

    public boolean getPostPosition(String name, String workOrderNO, String wkflwId, String recorderNO) {
        return getPostPosition(name, workOrderNO, wkflwId, recorderNO, new HashMap());
    }

    public boolean getPostPosition(String name, String workOrderNO, String wkflwId, String recorderNO, Map tableMap) {
        setWorkOrderNO(workOrderNO);
        setWkflwId(wkflwId);
        setRecorderNO(recorderNO);
        setTableMap(tableMap);
        return getPostPosition(name);
    }

    public boolean getPostPosition(String name) {
        boolean postposition = false;
        try {
            newPostPosition(name);
            postposition = true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            return postposition;
        }
    }

    public Object newPostPosition(String name) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, InstantiationException {
        String className = "com.oa.core.system.postposition." + getType() + name;
        Class c = Class.forName(className);
        Object object = null;
        if(getType().equals("form")) {
            Method m = c.getMethod("execuTables", String.class, String.class, String.class, Map.class);
            object = m.invoke(c.newInstance(), getTableName(), getSaveType(), getRecorderNO(), getTableMap());
        }else{
            Method m = c.getMethod("execuWorkFlow", String.class, String.class, String.class, String.class);
            object = m.invoke(c.newInstance(), getWorkOrderNO(), getWkflwId(), getRecorderNO(), getProcId());
        }
        return object;
    }

    public String getType() {
        if (type != null && type != "") {
            return type + ".";
        } else {
            return "";
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    public String getWorkOrderNO() {
        return workOrderNO;
    }

    public void setWorkOrderNO(String workOrderNO) {
        this.workOrderNO = workOrderNO;
    }

    public String getWkflwId() {
        return wkflwId;
    }

    public void setWkflwId(String wkflwId) {
        this.wkflwId = wkflwId;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getRecorderNO() {
        return recorderNO;
    }

    public void setRecorderNO(String recorderNO) {
        this.recorderNO = recorderNO;
    }

    public Map getTableMap() {
        return tableMap;
    }

    public void setTableMap(Map tableMap) {
        if (tableMap != null) {
            tableMap.remove("recorderNO");
        }
        this.tableMap = tableMap;
    }
}


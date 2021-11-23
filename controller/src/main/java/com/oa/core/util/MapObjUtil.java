package com.oa.core.util;

/**
 * @ClassName:MapObjUtil
 * @author:zxd
 * @Date:2019/07/04
 * @Time:下午 4:19
 * @Version V1.0
 * @Explain
 */

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapObjUtil {

    /**
     * 将JavaBean转换成Map
     *
     * @param obj
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Map beanToMap(Object obj) throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        // 创建map集合
        Map map = new HashMap();
        // 获取JavaBean中所有属性
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field fie : fields) {

            // 将属性第一个字母转换成大写
            String frist = fie.getName().substring(0, 1).toUpperCase();
            // 获取属性的类型
            Class<?> type = fie.getType();
            // 封装属性的get
            String getter = "";
            if ("boolean".equals(type.getName())) {
                getter = "is" + frist + fie.getName().substring(1);
            } else {
                getter = "get" + frist + fie.getName().substring(1);
            }
            // 获取JavaBean的方法
            Method method = obj.getClass().getMethod(getter, new Class[]{});
            // 调用方法,并接收返回值
            Object objec = method.invoke(obj, new Object[]{});

            // 判断返回值不为空
            if (objec != null) {
                map.put(fie.getName(), objec);
            } else {
                map.put(fie.getName(), "");
            }
        }

        return map;
    }

    /**
     * 将Map转换为JavaBean
     *
     * @param map
     * @param clazz
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {

        // 获取JavaBean中的所有属性
        Field[] field = clazz.getDeclaredFields();
        T ct = clazz.newInstance();
        for (Field fi : field) {
            // 判断key值是否存在
            if (map.containsKey(fi.getName())) {
                // 获取key的value值
                String value = String.valueOf(map.get(fi.getName()));
                // 将属性的第一个字母转换为大写
                String frist = fi.getName().substring(0, 1).toUpperCase();
                // 属性封装set方法
                String setter = "set" + frist + fi.getName().substring(1);
                // 获取当前属性类型
                Class<?> type = fi.getType();
                // 获取JavaBean的方法,并设置类型
                Method method = clazz.getDeclaredMethod(setter, type);
                switch (type.getName()) {
                    case "int":
                        method.invoke(ct, Integer.parseInt(value==null?"0":value));
                        break;
                    default:
                        method.invoke(ct, value==null?"":value);
                        break;
                }
            }
        }
        return ct;
    }

    /**
     * 将List<Map<String,Object>>转换成List<javaBean>
     *
     * @param listm
     * @param clazz
     * @return
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static <T> List<T> ListMapToListBean(List<Map<String, Object>> listm, Class<T> clazz) {

        List<T> list = new ArrayList<>();
        // 循环遍历出map对象
        for (Map<String, Object> m : listm) {
            // 调用将map转换为JavaBean的方法
            try {
                T t = mapToBean(m, clazz);
                // 添加进list集合
                list.add(t);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

        }

        return list;
    }

    /**
     * 将list<javabean>转换为List<Map>
     *
     * @param list
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static List<Map<String, Object>> ListBeanToListMap(List<Object> list) throws NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();

        for (Object ob : list) {

            listmap.add(beanToMap(ob));
        }

        return listmap;
    }
}
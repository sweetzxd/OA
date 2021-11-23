package com.oa.core.bean.system;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName:Menu
 * @author:zxd
 * @Date:2019/04/25
 * @Time:上午 9:27
 * @Version V1.0
 * @Explain 菜单实体类
 */
public class Menu implements Serializable {

    private String id;
    private String pid;
    private String fid;
    private String title;
    private String url;
    private String img;
    private int type;
    private int num;
    private List<Menu> menus;

    public void setDataForm(String id,String title,String url,String img){
        this.id = id;
        this.title = title;
        this.url = url;
        this.img = img;
    }
    public void setDataNode(String id,String title,List<Menu> menus,String img){
        this.id = id;
        this.title = title;
        this.menus = menus;
        this.img = img;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
}

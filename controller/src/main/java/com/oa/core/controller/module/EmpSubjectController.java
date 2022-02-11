package com.oa.core.controller.module;

import com.oa.core.bean.Loginer;
import com.oa.core.helper.DateHelper;
import com.oa.core.helper.StringHelper;
import com.oa.core.listener.InitDataListener;
import com.oa.core.listener.WebSocketListener;
import com.oa.core.scada.common.Common;
import com.oa.core.scada.system.Subject;
import com.oa.core.scada.websocket.MyWebSocketClient;
import com.oa.core.service.util.TableService;
import com.oa.core.system.ziputil.PackageUtil;
import com.oa.core.tag.UserDict;
import com.oa.core.util.ConfParseUtil;
import com.oa.core.util.MySqlUtil;
import com.oa.core.util.PrimaryKeyUitl;
import com.oa.core.util.ToNameUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @ClassName:EmpSubjectController
 * @author:zxd
 * @Date:2019/06/26
 * @Time:上午 8:51
 * @Version V1.0
 * @Explain 视频监控系统接口类
 */
@Controller
@RequestMapping("/empsub")
public class EmpSubjectController {

    @Autowired
    TableService tableService;

    /**
     * @method: setSubject
     * @param:
     * @return:
     * @author: zxd
     * @date: 2019/06/29
     * @description: 更新全部人员到监控系统内
     */
    @RequestMapping(value = "/setsubject", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String setSubject(HttpServletRequest request) {
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userId = loginer.getId();
        String type = request.getParameter("type");
        String sql = "select recorderNO,gh19062100001 as id,xm19062100001 as name,yhlx190621001 as subject_type,bm19062100001 as department,tx19062100001 as avatar,sblb190621001 as photo_ids from afxtx19062101 where curStatus=2";
        if (!"0".equals(type)) {
            sql = "select recorderNO,lfrID19062601 as id,lfr1906260001 as name,yhlx190626001 as subject_type,lfdw190626001 as department,tx19062600001 as avatar,sbzp190626001 as photo_ids,lfrq190626001 as start_time,lkrq190626001 as end_time from wbryx19062601 where curStatus=2";
        }
        List<Map<String, Object>> maps = tableService.selectSqlMapList(sql);
        for (Map<String, Object> map : maps) {
            try {
                setOneSubject(map, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        new WebSocketListener("attmap");
        return "";
    }

    /**
     * @method: setOneSubject
     * @param: recno 表ID
     * @return:
     * @author: zxd
     * @date: 2019/06/29
     * @description: 更新指定人员到监控系统内
     */
    @RequestMapping(value = "/setonesubject", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String setOneSubject(HttpServletRequest request) {
        String recno = request.getParameter("recno");
        String userId = request.getParameter("userid");
        String type = request.getParameter("type");
        String sql = "select recorderNO,gh19062100001 as id,xm19062100001 as name,yhlx190621001 as subject_type,bm19062100001 as department,tx19062100001 as avatar,sblb190621001 as photo_ids from afxtx19062101 where curStatus=2 and recorderNO='" + recno + "'";
        if (!"0".equals(type)) {
            sql = "select recorderNO,lfrID19062601 as id,lfr1906260001 as name,yhlx190626001 as subject_type,lfdw190626001 as department,tx19062600001 as avatar,sbzp190626001 as photo_ids,lfrq190626001 as start_time,lkrq190626001 as end_time from wbryx19062601 where curStatus=2 and recorderNO='" + recno + "'";
        }
        Map<String, Object> map = tableService.selectSqlMap(sql);
        try {
            setOneSubject(map, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new WebSocketListener("attmap");
        return "";
    }

    public void setOneSubject(Map<String, Object> map, String userId) throws Exception {
        String url = Common.SYSTEM + Common.SYSTEM_URL;
        String recorderNO = (String) map.get("recorderNO");
        String id = (String) map.get("id");
        String name = (String) map.get("name");
        String subject_type = (String) map.get("subject_type");
        String department = (String) map.get("department");
        String avatar = (String) map.get("avatar");
        String photo_ids = (String) map.get("photo_ids");
        JSONObject json = new JSONObject();
        //如果subject_type不等于0，必须要指定start_time，end_time
        json.put("subject_type", Integer.parseInt(subject_type));
        json.put("name", UserDict.getName(name));
        json.put("job_number", name);
        if (!"0".equals(subject_type)) {
            Date start_time = (Date) map.get("start_time");
            Date end_time = (Date) map.get("end_time");
            Long st = start_time.getTime();
            Long et = end_time.getTime() + (24 * 60 * 60 * 1000 - 1);
            json.put("start_time", st / 1000);
            json.put("end_time", et / 1000);
        }
        if (photo_ids != null && !photo_ids.equals("")) {
            Vector<String> photo = StringHelper.string2Vector(photo_ids, "\\|");
            ConfParseUtil cpu = new ConfParseUtil();
            String imgurl = cpu.getProperty("upload_file") + photo.get(0);
            //String imageStr = BaseImageUtil.getImageStr(imgurl);
            int userinfo = 0;
            if (!id.equals("0")) {
                String subjecturl = url + Common.SELECT_USER + id;
                userinfo = Subject.getSubject(WebSocketListener.cookieStore, subjecturl);
            }
            String photourl = url + Common.POST_PHOTO;
            if (userinfo == 1) {
                int photoid = Subject.photo(WebSocketListener.cookieStore, photourl, imgurl, Integer.parseInt(id));
                List<Integer> photos = new ArrayList<>();
                photos.add(photoid);
                json.put("photo_ids", photos);
                //json.put("avatar", imageStr);
                String upsubjecturl = url + Common.SELECT_USER + id;
                Subject.updateSubject(WebSocketListener.cookieStore, upsubjecturl, json);
                Subject.avatar(WebSocketListener.cookieStore, url + Common.UPDATE_AVATAR, imgurl, Integer.parseInt(id));
            } else {
                String userurl = url + Common.ADD_USER;
                int subject = Subject.subject(WebSocketListener.cookieStore, userurl, json);
                Subject.avatar(WebSocketListener.cookieStore, url + Common.UPDATE_AVATAR, imgurl, subject);
                if (subject > 0) {
                    Subject.photo(WebSocketListener.cookieStore, photourl, imgurl, subject);
                    String table = "afxtx19062101";
                    String setval1 = " tx19062100001='" + photo.get(0) + "',gh19062100001='" + subject + "',";
                    String setval2 = " gh19062100001='" + subject + "',";
                    if (!"0".equals(subject_type)) {
                        table = "wbryx19062601";
                        setval1 = " tx19062600001='" + photo.get(0) + "',lfrID19062601='" + subject + "',";
                        setval2 = " lfrID19062601='" + subject + "',";
                    }
                    if (avatar == null || avatar.equals("")) {
                        tableService.updateSqlMap("update " + table + " set " + setval1 + " modifyName='" + userId + "',modifyTime='" + DateHelper.now() + "' where recorderNO='" + recorderNO + "'");
                    } else {
                        tableService.updateSqlMap("update " + table + " set " + setval2 + " modifyName='" + userId + "',modifyTime='" + DateHelper.now() + "' where recorderNO='" + recorderNO + "'");
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/verifyimg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String verifyImg(HttpServletRequest request) {
        String imgurl = request.getParameter("imgurl");
        String url = Common.SYSTEM + Common.SYSTEM_URL;
        try {
            Subject.photoCheck(WebSocketListener.cookieStore, url + Common.POST_PHOTO_CHECK, imgurl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        return json.toString();
    }

    @RequestMapping(value = "/getusersubject", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getUserSubject(HttpServletRequest request) {
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        JSONObject json = new JSONObject();
        try {
            String sql = "SELECT gh19062100001 as id, staffName, deptName, tx19062100001 as photo, e.sex as sex FROM afxtx19062101 a JOIN employees e ON a.xm19062100001 = e.userName JOIN department d ON a.bm19062100001 = d.deptId WHERE a.curStatus = 2 AND e.curStatus =2 AND d.curStatus =2 and gh19062100001='" + id + "'";
            if (!"0".equals(type)) {
                sql = "SELECT lfrID19062601 as id,lfr1906260001 as staffName, lfdw190626001 as deptName, tx19062600001 as photo, '男' as sex FROM wbryx19062601 WHERE curStatus = 2 and lfrID19062601='" + id + "'";
            }
            Map<String, Object> map = tableService.selectSqlMap(sql);
            json.put("data", map);
        } catch (Exception e) {
            e.getLocalizedMessage();
            Map<String, Object> map = new HashMap<>();
            map.put("id", "0");
            map.put("staffName", "陌生人");
            map.put("deptName", "陌生访客");
            map.put("photo", "");
            map.put("sex", "男");
            json.put("data", map);
        }
        json.put("msg", "");
        json.put("success", 1);
        return json.toString();
    }

    /**
     * @method: synchronizedId
     * @param:
     * @return:
     * @author: zxd
     * @date: 2019/06/29
     * @description: 从监控系统内同步人员信息
     */
    @RequestMapping(value = "/synchronizedid", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void synchronizedId(HttpServletRequest request) {
        String file = PackageUtil.getFile();
        String folder = "upload/common/" + DateHelper.getYearMonth() + "/";
        PrimaryKeyUitl pk = new PrimaryKeyUitl();
        String url = Common.SYSTEM + Common.SYSTEM_URL + ":12345";
        try {
            JSONArray array = Subject.mobileAdminSubjects(WebSocketListener.cookieStore, url + Common.ALL_USER);
            for (int i = 0; i < array.length(); i++) {
                JSONObject userInfo = (JSONObject) array.get(i);
                int type = userInfo.getInt("subject_type");
                int id = userInfo.getInt("id");
                String name = userInfo.getString("name");
                name = name.replaceAll(" ","");
                Map<String, Object> empSubMap;
                String[] fields = new String[2];
                if (type == 0) {
                    empSubMap = tableService.selectSqlMap("select recorderNO AS rid,gh19062100001 as id,tx19062100001 as tx,sblb190621001 as phos from afxtx19062101 where curStatus=2 and (gh19062100001='" + id + "' or reserveField='" + name + "')");
                    fields[0] = "tx19062100001";
                    fields[1] = "sblb190621001";
                } else {
                    empSubMap = tableService.selectSqlMap("select recorderNO AS rid,lfrID19062601 as id,tx19062600001 as tx,sbzp190626001 as phos from wbryx19062601 where curStatus=2 and (lfrID19062601='" + id + "' or lfr1906260001='" + name + "')");
                    fields[0] = "tx19062600001";
                    fields[1] = "sbzp190626001";
                }

                String update = "", tx = "", phos = "";
                boolean b_type=true,b_tx = true, b_phos = true;
                if(type == 0){
                    if(empSubMap==null) {
                        b_type = false;
                    }
                }
                if(b_type) {
                    String e_tx = null;
                    String e_phos = null;
                    if(empSubMap!=null){
                        e_tx = (String)empSubMap.get("tx");
                        e_phos = (String)empSubMap.get("phos");
                    }
                    if(e_tx!=null && !e_tx.equals("")){
                        b_tx = false;
                    }else {
                        if (!userInfo.isNull("avatar")) {
                            String avatar = userInfo.getString("avatar");
                            if (!avatar.equals("")) {
                                tx = folder + System.currentTimeMillis() + "-" + name + "_头像.jpg";
                                downloadPicture(file + folder, url + avatar, file + tx);
                            } else {
                                b_tx = false;
                            }
                        } else {
                            b_tx = false;
                        }
                    }
                    if(e_phos!=null && !e_phos.equals("")){
                        b_phos = false;
                        if(!b_tx && (e_tx==null || e_tx.equals(""))){
                            JSONArray photos = userInfo.getJSONArray("photos");
                            if (photos.length() > 0) {
                                JSONObject jsonPhoto = photos.getJSONObject(0);
                                String photourl = jsonPhoto.getString("url");
                                if (!photourl.equals("")) {
                                    tx = folder + System.currentTimeMillis() + "-" + name + "_照片_" + 0 + ".jpg";
                                    downloadPicture(file + folder, url + photourl, file + tx);
                                    b_tx = true;
                                }
                            }
                        }
                    }else {
                        if (!userInfo.isNull("photos")) {
                            JSONArray photos = userInfo.getJSONArray("photos");
                            if (photos.length() > 0) {
                                for (int n = 0, len = photos.length(); n < len; n++) {
                                    JSONObject jsonPhoto = photos.getJSONObject(n);
                                    String photourl = jsonPhoto.getString("url");
                                    if (!photourl.equals("")) {
                                        String pho = folder + System.currentTimeMillis() + "-" + name + "_照片_" + n + ".jpg";
                                        downloadPicture(file + folder, url + photourl, file + pho);
                                        if (n == 0 && !b_tx) {
                                            tx = pho;
                                            b_tx = true;
                                        }
                                        phos = phos + pho + "|";
                                    }
                                }
                            } else {
                                b_phos = false;
                            }

                        } else {
                            b_phos = false;
                        }
                    }
                    if (b_tx) {
                        update += ", " + fields[0] + "='" + tx + "'";
                    }
                    if (b_phos) {
                        update += ", " + fields[1] + "='" + phos + "'";
                    }
                }
                if (type == 0) {
                    if (empSubMap != null && empSubMap.size() > 0) {
                        tableService.updateSqlMap("update afxtx19062101 set gh19062100001='" + id + "'" + update + ", modifyName='admin',modifyTime='" + DateHelper.now() + "' where reserveField='" + name + "'");
                    }/*else{
                        Map map = new HashMap();
                        map.put("recorderNO", pk.getNextId("afxtx19062101", "recorderNO"));
                        map.put("gh19062100001", id);
                        map.put("reserveField", name);
                        map.put("yhlx190621001", type);
                        map.put("tx19062100001", tx);
                        map.put("sblb190621001", phos);
                        map.put("recordName", "admin");
                        map.put("recordTime", DateHelper.now());
                        map.put("modifyName", "admin");
                        map.put("modifyTime", DateHelper.now());
                        String afxtx19062101 = MySqlUtil.getInsertSql("afxtx19062101", map);
                        tableService.insertSqlMap(afxtx19062101);
                    }*/
                } else {
                    if (empSubMap != null && empSubMap.size() > 0) {
                        tableService.updateSqlMap("update wbryx19062601 set lfrID19062601='" + id + "',lfr1906260001='" + name + "'" + update + ",modifyName='admin',modifyTime='" + DateHelper.now() + "' where reserveField='" + name + "'");
                    } else {
                        Map map = new HashMap();
                        map.put("recorderNO", pk.getNextId("wbryx19062601", "recorderNO"));
                        map.put("lfrID19062601", id);
                        map.put("lfr1906260001", name);
                        map.put("yhlx190626001", type);
                        map.put("tx19062600001", tx);
                        map.put("sbzp190626001", phos);
                        map.put("recordName", "admin");
                        map.put("recordTime", DateHelper.now());
                        map.put("modifyName", "admin");
                        map.put("modifyTime", DateHelper.now());
                        String wbryx19062601 = MySqlUtil.getInsertSql("wbryx19062601", map);
                        tableService.insertSqlMap(wbryx19062601);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new WebSocketListener("attmap");
    }

    @RequestMapping(value = "/getvideofile", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getVideoFile() {
        String upfile = "upload/video/";
        String file = PackageUtil.getFile();
        String folderPath = file + upfile;
        ArrayList<String> scanFiles = new ArrayList<String>();
        File directory = new File(folderPath);
        JSONObject json = new JSONObject();
        if (!directory.isDirectory()) {
            json.put("data", scanFiles);
            json.put("msg", "找不到文件");
            json.put("success", 0);

        } else if (directory.isDirectory()) {
            File[] filelist = directory.listFiles();
            for (int i = 0; i < filelist.length; i++) {
                /**如果当前是文件夹，进入递归扫描文件夹**/
                if (!filelist[i].isDirectory()) {
                    scanFiles.add(upfile + filelist[i].getName());
                }
            }
            json.put("data", scanFiles);
            json.put("msg", "");
            json.put("success", 1);
        }
        return json.toString();
    }

    /**
     * @method: downloadPicture
     * @param:
     * @return:
     * @author: zxd
     * @date: 2019/06/29
     * @description: 链接url下载图片
     */
    private static void downloadPicture(String folder, String urlList, String path) {
        URL url = null;
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            File file = new File(folder);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            BASE64Encoder encoder = new BASE64Encoder();
            String encode = encoder.encode(buffer);//返回Base64编码过的字节数组字符串
            System.out.println(encode);
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getFileExists(String file, String path) {
        if (path == null || path.equals("")) {
            return true;
        }
        File testFile = new File(file + path);
        if (!testFile.exists()) {
            System.out.println("文件不存在");
            return true;
        } else {
            System.out.println("文件存在");
            return false;
        }
    }

    public JSONObject getEmpSubject(int id) {
        String url = Common.SYSTEM + Common.SYSTEM_URL;
        String subjecturl = url + Common.SELECT_USER + id;
        JSONObject subjectVal = null;
        try {
            subjectVal = Subject.getSubjectVal(WebSocketListener.cookieStore, subjecturl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjectVal;
    }

    /**
     * @method:
     * @param:
     * @return:
     * @author: zxd
     * @date: 2019/07/24
     * @description: 方法说明
     */
    @RequestMapping(value = "/facerecognition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void faceRecognition(HttpServletRequest request) {
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
            System.out.println(jsonObject);
            if (!jsonObject.isNull("subject_id")) {
                int subject_id = jsonObject.getInt("subject_id");
                if(subject_id>=0) {
                    JSONObject subject = getEmpSubject(jsonObject.getInt("subject_id"));
                    if(subject!=null){
                        Long time = jsonObject.getLong("timestamp");
                        String timestamp = String.valueOf(time);
                        System.out.println(timestamp.length());
                        if(timestamp.length()< 13){
                            Long tt = Long.valueOf(time)*1000;
                            subject.put("timestamp", tt);
                        }else{
                            subject.put("timestamp", time);
                        }
                        MyWebSocketClient.settable(jsonObject, subject);
                    }else{
                        String userName = WebSocketListener.getData(String.valueOf(jsonObject.getInt("subject_id")));
                        String name = ToNameUtil.getName("user",userName);
                        jsonObject.put("id",jsonObject.getInt("subject_id"));
                        jsonObject.put("job_number",userName);
                        jsonObject.put("name",name);
                        MyWebSocketClient.settable(jsonObject, jsonObject);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @method:
     * @param:
     * @return:
     * @author: zxd
     * @date: 2019/07/24
     * @description: 方法说明
     */
    @RequestMapping(value = "/downloadevents", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void getEvents(HttpServletRequest request){
        Integer start = Integer.parseInt(request.getParameter("start"));
        Integer end = Integer.parseInt(request.getParameter("end"));
        JSONObject json = new JSONObject();
        json.put("start",start);
        json.put("end",end);
        json.put("user_role",0);
        json.put("size",1);
        json.put("size",1000);
        String url = Common.SYSTEM + Common.SYSTEM_URL;
        String subjecturl = url + Common.EVENT_USER_LOG;
        try {
            JSONArray events = Subject.getEvents(WebSocketListener.cookieStore, subjecturl, json);
            int e = events.length();
            if(e>0){
                for(int i=0;i<e;i++){
                    JSONObject event = events.getJSONObject(i);
                    JSONObject data = event.getJSONObject("data");
                    event.remove("data");
                    MyWebSocketClient.settable(data, event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.oa.core.scada.websocket;

/**
 * @ClassName:MyWebSocketClient
 * @author:zxd
 * @Date:2019/06/15
 * @Time:下午 3:30
 * @Version V1.0
 * @Explain
 */

import com.oa.core.helper.DateHelper;
import com.oa.core.listener.WebSocketListener;
import com.oa.core.scada.common.Common;
import com.oa.core.scada.util.URIEncoder;
import com.oa.core.service.util.TableService;
import com.oa.core.util.*;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyWebSocketClient extends WebSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketClient.class);
    private static TableService tableService = (TableService) SpringContextUtil.getBean("tableService");

    public MyWebSocketClient(String url) throws URISyntaxException {
        super(new URI(url));
    }

    @Override
    public void onOpen(ServerHandshake shake) {
        System.out.println("握手...");
        for (Iterator<String> it = shake.iterateHttpFields(); it.hasNext(); ) {
            String key = it.next();
            System.out.println(key + ":" + shake.getFieldValue(key));
        }
    }

    @Override
    public void onMessage(String paramString) {
        JSONObject json = null;
        try {
            json = new JSONObject(paramString);
            if (!json.isNull("person")) {
                JSONObject person = json.getJSONObject("person");
                System.out.println(person);
                settable(person);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("接收到消息：" + paramString);
    }

    /**
     * @method: settable
     * @param: person 门禁系统的websocket信息
     * @return:
     * @author: zxd
     * @date: 2019/07/22
     * @description: 写入门禁系统采集的打卡信息
     */
    public static void settable(JSONObject person) {
        settable(null,person);
    }
    public static void settable(JSONObject jsonObject,JSONObject person) {
        if(person.isNull("timestamp")) {
            person.put("timestamp", System.currentTimeMillis());
        }
        if (jsonObject == null) {
            jsonObject = person;
        }
        ConfParseUtil cpu = new ConfParseUtil();
        String amtoworku = cpu.getSchedule("amtoworku");
        String amtowork = cpu.getSchedule("amtowork");
        String amoffwork = cpu.getSchedule("amoffwork");
        String amoffworkd = cpu.getSchedule("amoffworkd");
        String pmtoworku = cpu.getSchedule("pmtoworku");
        String pmtowork = cpu.getSchedule("pmtowork");
        String pmoffwork = cpu.getSchedule("pmoffwork");
        String pmoffworkd = cpu.getSchedule("pmoffworkd");

        String userName = WebSocketListener.getData(String.valueOf(person.getInt("id")));
        if (userName == null || userName.equals("")) {
            userName = String.valueOf(person.get("job_number"));
        }
        Map<String, Object> map = new HashMap<>();
        PrimaryKeyUitl pk = new PrimaryKeyUitl();
        map.put("recorderNO", pk.getNextId("rlsbj19072001", "recorderNO"));
        map.put("sbrbh19072001", person.getInt("id"));
        map.put("sbry190720001", userName);
        map.put("ryxm190720001", person.getString("name"));
        Long timestamp = person.getLong("timestamp");
        Long timese;
        String tt = String.valueOf(timestamp);
        if(tt.length()< 13){
            timese = Long.valueOf(timestamp)*1000;
        }else{
            timese = timestamp;
        }
        Date dateOld = new Date(timese);
        Timestamp specifiedTime = DateHelper.Long2Timestamp(timese, true);

        map.put("sbsj190720001", specifiedTime);
        map.put("fksj190720001", jsonObject);
        map.put("recordName", "admin");
        map.put("recordTime", DateHelper.now());
        map.put("modifyName", "admin");
        map.put("modifyTime", DateHelper.now());
        String rlsbj19072001 = MySqlUtil.getInsertSql("rlsbj19072001", map);
        tableService.insertSqlMap(rlsbj19072001);

        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        String[] datetime = sp.format(dateOld).split("-");
        String year = datetime[0];
        String month = datetime[1];
        String day = datetime[2];
        String ymd = year + "-" + month + "-" + day;

        String[] time = new String[]{ymd + " " + amtoworku, ymd + " " + amtowork, ymd + " " + amoffwork, ymd + " " + amoffworkd, ymd + " " + pmtoworku, ymd + " " + pmtowork, ymd + " " + pmoffwork, ymd + " " + pmoffworkd};
        StringBuffer sql = new StringBuffer();
        sql.append("select userName,staffName,ifnull(t.num,'0000') as num from userinfo u left join (select qdr,CONCAT(num1,num2,num3,num4) as num from (");
        sql.append("select qdr,if(sum(num1)>0,1,0) as num1,if(sum(num2)>0,1,0) as num2,if(sum(num3)>0,1,0) as num3,if(sum(num4)>0,1,0) as num4 from ");
        sql.append("(select sbry190720001 as qdr,count(*) as num1,0 as num2,0 as num3,0 as num4 from rlsbj19072001 where curStatus=2 and sbsj190720001 BETWEEN '" + time[0] + "' and '" + time[1] + "' GROUP BY sbrbh19072001 UNION ");
        sql.append(" select sbry190720001 as qdr,0 as num1,count(*) as num2,0 as num3,0 as num4 from rlsbj19072001 where curStatus=2 and sbsj190720001 BETWEEN '" + time[2] + "' and '" + time[3] + "' GROUP BY sbrbh19072001 UNION ");
        sql.append(" select sbry190720001 as qdr,0 as num1,0 as num2,count(*) as num3,0 as num4 from rlsbj19072001 where curStatus=2 and sbsj190720001 BETWEEN '" + time[4] + "' and '" + time[5] + "' GROUP BY sbrbh19072001 UNION ");
        sql.append(" select sbry190720001 as qdr,0 as num1,0 as num2,0 as num3,count(*) as num4 from rlsbj19072001 where curStatus=2 and sbsj190720001 BETWEEN '" + time[6] + "' and '" + time[7] + "' GROUP BY sbrbh19072001 )");
        sql.append(" att group by qdr");
        sql.append(" ) temptable) t on u.userName = t.qdr where userName='" + userName + "'");
        Map<String, Object> maps = tableService.selectSqlMap(sql.toString());
        if (maps != null){
            tableService.insertSqlMap("delete from attendancesum where year='" + year + "' and month='" + month + "' and day='" + day + "' and user='" + userName + "'");
            String num = (String) maps.get("num");
            Map<String, Object> attendancesum = new HashMap();
            attendancesum.put("user", userName);
            attendancesum.put("year", year);
            attendancesum.put("month", month);
            attendancesum.put("day", day);
            double[] attencetype = AttenceUtil.attencetype(num);
            attendancesum.put("num", new JSONArray(attencetype));
            tableService.insertSqlMap(MySqlUtil.getInsertSql("attendancesum", attendancesum));
        }
    }

    public void jsonResolver(JSONObject json) throws JSONException {
        String status = "gone";
        if (jsonIsNull(json, "status")) {
            status = json.getString("status");
        }
        long timestamp = System.currentTimeMillis();
        ;
        if (jsonIsNull(json, "timestamp")) {
            timestamp = json.getLong("timestamp");
        }
        String imgname = "";
        if (!json.isNull("person")) {
            JSONObject person = json.getJSONObject("person");
            System.out.println(person + "   status:" + status);
            if (jsonIsNull(person, "id")) {
                imgname = timestamp + "_" + person.getInt("id");
            }
        }
        if (jsonIsNull(json, "face")) {
            JSONObject face = json.getJSONObject("face");
            if (jsonIsNull(face, "image")) {
                String image = face.getString("image");
                boolean b = GenerateImage(image, status, imgname);
                if (b) {
                    System.out.println("照片");
                } else {
                    System.out.println("没有");
                }
            }
        }

    }

    public boolean jsonIsNull(JSONObject json, String key) {
        if (json.isNull(key)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClose(int paramInt, String paramString, boolean paramBoolean) {
        System.out.println("关闭...");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("异常" + e);

    }

    public void restart(){

        clintsocket();
    }

    public static boolean GenerateImage(String base64str, String type, String savepath) {   //对字节数组字符串进行Base64解码并生成图片
        String url = "C:\\Users\\Administrator\\Desktop\\jk\\" + type + "\\";
        if (base64str == null) { //图像数据为空
            System.out.println("图像数据为空");
            return false;
        }
        // System.out.println("开始解码");
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(base64str);
            //  System.out.println("解码完成");
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            // System.out.println("开始生成图片");
            //生成jpeg图片
            OutputStream out = new FileOutputStream(url + savepath + ".png");
            out.write(b);
            out.flush();
            out.close();
            System.out.println("开始生成图片");
            return true;
        } catch (Exception e) {
            System.out.println("生成图片失败");
            return false;
        }
    }


    public static void clintsocket() {
        String url = Common.WS_URL + URIEncoder.encodeURIComponent(Common.CAMERA_URL);
        try {
            System.out.println(url);
            MyWebSocketClient client = new MyWebSocketClient(url);
            client.connect();
            while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                System.out.println("还没有打开");
            }
            System.out.println("建立websocket连接");
            client.send("connection");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            try {
                new MyWebSocketClient(url);
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        }
    }
}
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<%@ page import="com.oa.core.bean.Loginer" %>
<%@ page import="com.oa.core.service.util.TableService" %>
<%@ page import="com.oa.core.util.SpringContextUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oa.core.helper.DateHelper" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="com.oa.core.listener.InitDataListener" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<%--jspBegin--%>
<%
    TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
    List<String> festivals = tableService.selectSql("SELECT startTime FROM festival where curStatus=2 AND type=3 AND festivalName like('%调休') AND (YEAR(startTime) = '" + DateHelper.getYear() + "' OR YEAR(endTime) = '" + DateHelper.getYear() + "') GROUP BY startTime");
    JSONArray tx = new JSONArray(festivals);
    request.setAttribute("jjrtx", tx);
    List<String> holiday = InitDataListener.getData("holiday");
    request.setAttribute("holiday", new JSONArray(holiday));//法定节假日休息日期
%>
<%--jspEnd--%>
<html>
<head>
    <title>月度考勤统计查看</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/zjyoa-pc/resources/css/tablebox.css" type="text/css"/>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>
    <script src="/zjyoa-pc/resources/js/system/manager.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>
    <script src="/zjyoa-pc/resources/js/system/flow.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
    <%--scriptBegin--%>
    <%--scriptEnd--%>
    <script type="text/javascript">
        let runIndex, newHtmlString;
        function starscript(pageid, tableid, formid) {
                loadindex = layui.layer.load(0, {shade: [0.1, '#fff'], offset: '100px'});
                   var deptid='${bm}';
                    var year='${year}';
                    var month='${month}';
                    var lastyear='${lastyear}';
                    var lastmonth='${lastmonth}';
                    var dename='${bmname}';
                getvalue({deptid, year, month, lastyear, lastmonth, dename}).then(res => {
                    if (res.isRealy) {
                        loadindex &&　layer.close(loadindex);
                    }
                });
            <%--functionBegin--%>
            <%--functionEnd--%>
        }
        <%--jsBegin--%>
        function getvalue(params) {
            return new Promise((resove, reject) => {
                var year = params.year;
                var lastyear = params.lastyear;
                var month = params.month;
                var lastmonth = 12;
                var truemonth = params.month;
                var num = 0;
                if (month < 10) {
                    truemonth = "0" + month;
                }
                if (month !== 1&&month != '01') {
                    lastmonth = month - 1;
                    lastyear = year;
                }
                var truelastnonth = lastmonth;
                if (lastmonth < 10) {
                    truelastnonth = "0" + lastmonth;
                }
                $.ajax({
                    url: '/zjyoa-pc/ydkqgl/kqjl2019112700001new.do',
                    type: "POST",
                    dataType: "json",
                    data: {
                        year: params ? params.year : year,
                        lastyear: params ? params.lastyear : lastyear,
                        month: params ? params.month:truemonth,
                        lastmonth:params ? params.lastmonth:truelastnonth,
                        dept: params ? params.deptid : deptid
                    },
                    success: function (data) {
                        runIndex++
                        !params && $('#trr').html("");
                        for (var pn in data) {
                            num++;
                            var htmltd1 = "";
                            var htmltd2 = "";
                            var value = data[pn];
                            var html = "<tr>";
                            html += "<td>" + num + "</td>";
                            html += "<td width='100px'>" + (params ?params.dename:dename) + "</td>";
                            html += "<td>" + oa.decipher('user', pn) + "</td>";
                            var a = 0;
                            var b = 0;
                            var c = 0;
                            var d = 0;
                            var e = 0;
                            var f = 0;

                            for (var i = 21; i <= 31; i++) {
                                var date = lastyear + "-" + truelastnonth + "-" + i;
                                if (isInArray2(date) && (isNotInArray2(date) || weekday(date))) {
                                    a++;
                                    const ii = i - 1;
                                    htmltd1 += "<td>" + typetext(value[ii][0]) + "</td>";
                                    if (value[ii][0] == 0 || value[ii][0] == 10 || value[ii][0] == 12) {
                                        b += 0.5;
                                    }
                                    htmltd1 += "<td>" + typetext(value[ii][1]) + "</td>";
                                    if (value[ii][1] == 0 || value[ii][1] == 10 || value[ii][0] == 12) {
                                        b += 0.5;
                                    }
                                    var  zhi0=isjqdate(c,d,e,f,value[ii]);
                                    c=zhi0[0];
                                    d=zhi0[1];
                                    e=zhi0[2];
                                    f=zhi0[3];
                                } else {
                                    htmltd1 += "<td></td>";
                                    htmltd1 += "<td></td>";
                                }
                            }
                            for (var i = 1; i <= 20; i++) {
                                var da = i < 10 ? '0'+i : i;
                                var date = year + "-" + month + "-" + da;
                                if (isInArray2(date) && (isNotInArray2(date) || weekday(date))) {
                                    a++;
                                    const ii = i-1;
                                    htmltd2 += "<td>" + typetext(value[ii][0]) + "</td>";
                                    if (value[ii][0] == 0 || value[ii][0] == 10 || value[ii][0] == 12) {
                                        b += 0.5;
                                    }
                                    htmltd2 += "<td>" + typetext(value[ii][1]) + "</td>";
                                    typetext(value[ii][1])
                                    if (value[ii][1] == 0 || value[ii][1] == 10|| value[ii][0] == 12 ) {
                                        b += 0.5;
                                    }
                                    var  zhi0=isjqdate(c,d,e,f,value[ii]);
                                    c=zhi0[0];
                                    d=zhi0[1];
                                    e=zhi0[2];
                                    f=zhi0[3];
                                } else {
                                    htmltd2 += "<td></td>";
                                    htmltd2 += "<td></td>";
                                }
                            }
                            html += htmltd1 + htmltd2;
                            html += "<td>" + a + "</td>";
                            html += "<td>" + b + "</td>";
                            html += "<td>" + c + "</td>";
                            html += "<td>" + d + "</td>";
                            html += "<td>" + e + "</td>";
                            html += "<td>" + f + "</td>";
                            html += "</tr>";
                            $('#trr').append(html);
                        }
                        resove({isRealy: true, index: runIndex, dename: params ?params.dename:dename})
                    },
                    error: function (error) {
                        runIndex++
                        reject(params)
                    }
                });
            })
        }

        function weekday(myDate) {
            var weekDay = ["星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
            var day = weekDay[new Date(myDate).getDay()];
            if (day === "星期天" || day === "星期六") {
                return false;
            } else {
                return true;
            }
        }

        //在不在法定节假日之内，在的话返回false，不在返回true
        function isInArray2(myDate) {
            var arr = ${holiday};
            var index = $.inArray(myDate, arr);
            if (index >= 0) {
                return false;
            }
            return true;
        }

        function isjqdate(c,d,e,f,value) {  //后补的假总数
            var zhi=[c,d,e,f];
            if(value[0]==1){
                c += 0.5;
            }else if(value[0]==2){
                d += 0.5;
            }else if(value[0]==3){
                e += 0.5;
            }else if(value[0]==4){
                f += 0.5;
            }
            if(value[1]==1){
                c += 0.5;
            }else if(value[1]==2){
                d += 0.5;
            }else if(value[1]==3){
                e += 0.5;
            }else if(value[1]==4){
                f += 0.5;
            }
            zhi=[c,d,e,f];
            return zhi;
        }


        function isNotInArray2(myDate) {
            var arr = ${jjrtx};
            var index = $.inArray(myDate, arr);
            if (index >= 0) {
                return true;
            }
            return false;
        }

        function typetext(num) {
            var text = "";
            switch (num) {
                case 0:
                    text = '✔';
                    break;
                case 1:
                    text = '✖';
                    break;
                case 2:
                    text = '※';
                    break;
                case 3:
                    text = '☐';
                    break;
                case 4:
                    text = '○';
                    break;
                case 5:
                    text = '■';
                    break;
                case 6:
                    text = '●';
                    break;
                case 7:
                case 8:
                    text = '#';
                    break;
                case 11:
                    text = '○';
                    break;
                case 12:
                    text = '⊙';
                    break;
                case 13:
                    text = '▲';
                    break;
                case 14:
                    text = '★';
                    break;
				case 18:
					text = '❤';
					break;
				case 19:
					text = '☀';
					break;
                default:
                    text = '';
                    break;
            }
            return text;
        }
        <%--jsEnd--%>
    </script>
</head>
<body onload="starscript('${pageid}','ydkqgl2021070500002','${formid}')">

<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
            <div class="layui-card layui-card-body">
                <div class="layui-container">
                    <p><div style="font-size: 20px;padding-bottom: 30px"><b>${bmname} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ${kqdate}</b></div></p>
                    <h3>考勤记录</h3>
                    <label style="padding-top: 10px">标记符号:正常上下班✔ 出差⊙ 迟到▲ 早退★ 加班◆ 事假※ 病假☐ 年休假○ 旷工✖ 产假(护理假)■ 探亲假● 婚、丧假#老人护理假❤育儿假☀</label>
                    <div id="sealdiv" style="height: auto; width: 100%;margin-bottom: 50px">
                        <table lay-filter="parse-table" style="height: 5%; width: 100%" border="1px">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>部门</th>
                                <th>姓名</th>
                                <th colspan="2">21号</th>
                                <th colspan="2">22号</th>
                                <th colspan="2">23号</th>
                                <th colspan="2">24号</th>
                                <th colspan="2">25号</th>
                                <th colspan="2">26号</th>
                                <th colspan="2">27号</th>
                                <th colspan="2">28号</th>
                                <th colspan="2">29号</th>
                                <th colspan="2">30号</th>
                                <th colspan="2">31号</th>
                                <th colspan="2">01号</th>
                                <th colspan="2">02号</th>
                                <th colspan="2">03号</th>
                                <th colspan="2">04号</th>
                                <th colspan="2">05号</th>
                                <th colspan="2">06号</th>
                                <th colspan="2">07号</th>
                                <th colspan="2">08号</th>
                                <th colspan="2">09号</th>
                                <th colspan="2">10号</th>
                                <th colspan="2">11号</th>
                                <th colspan="2">12号</th>
                                <th colspan="2">13号</th>
                                <th colspan="2">14号</th>
                                <th colspan="2">15号</th>
                                <th colspan="2">16号</th>
                                <th colspan="2">17号</th>
                                <th colspan="2">18号</th>
                                <th colspan="2">19号</th>
                                <th colspan="2">20号</th>
                                <th>应出勤</th>
                                <th>实出勤</th>
                                <th>旷工</th>
                                <th>事假</th>
                                <th>病假</th>
                                <th>年休假</th>
                            </tr>
                            </thead>
                            <tbody id="trr">
                            </tbody>
                        </table>
                    </div>
                    <p><b>未打卡说明:</b></p>
                    <p> <textarea id="wdkde2" name="wdkde2" disabled="disabled" style="resize: none;padding-bottom: 10px" class="layui-textarea"> ${wdkdesc}</textarea></p>
                    <p><b>考勤员: ${kqy}</b></p>
                    <p><b>审批人: ${bmdirector}</b></p>
                    <p><b>审批时间: ${spdata}</b></p>
            </div>
        </div>
    </div>
</div>
<%--divBegin--%>
<%--divEnd--%>
</body>
</html>

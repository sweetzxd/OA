<%--
  User: zxd
  Date: 2020/02/18
  Time: 下午 2:08
  Explain: 说明
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.springframework.util.Base64Utils" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.oa.core.util.SpringContextUtil" %>
<%@ page import="com.oa.core.service.util.TableService" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="com.oa.core.listener.InitDataListener" %>
<%@ page import="com.oa.core.helper.DateHelper" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<%
    TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
    List<String> festivals = tableService.selectSql("SELECT startTime FROM festival where curStatus=2 AND type=3 AND festivalName like('%调休') AND (YEAR(startTime) = '" + DateHelper.getYear() + "' OR YEAR(endTime) = '" + DateHelper.getYear() + "') GROUP BY startTime");
    JSONArray tx = new JSONArray(festivals);
    request.setAttribute("jjrtx", tx);
    List<String> holiday = InitDataListener.getData("holiday");
    request.setAttribute("holiday", new JSONArray(holiday));//法定节假日休息日期
%>

<html>
<head>
    <title></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" type="text/css" href="/zjyoa-pc/resources/css/print.css"/>
    <link rel="stylesheet" href="/zjyoa-pc/resources/layuiadmin/layui/css/layui.css" media="all">
    <style>
        .icon-box {
            float: left;
            margin-left: 30px;
            height: 25px;
            line-height: 25px;
        }

        .layui-size {
            padding: 5px 0 5px 0;
            font-size: 15px;
        }
    </style>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" type="text/javascript"
            charset="utf-8"></script>
    <script src="/zjyoa-pc/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
    <script src="/zjyoa-pc/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
    <%--导出word引入的js--%>
    <script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.js"></script>
    <script type="text/javascript" src="/zjyoa-pc/resources/js/printword/FileSaver.js"></script>
    <script type="text/javascript" src="/zjyoa-pc/resources/js/printword/jquery.wordexport.js"></script>
    <script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>
    <script type="text/javascript">
        function starscript(name,year, lastyear, truemonth, truelastnonth) {
            console.log("jinlaile ");
            $.ajax({
                url: '/zjyoa-pc/Kqjl/kqjlckeckk.do',
                type: "POST",
                dataType: "json",
                data: {
                    name: name,
                    year: year,
                    lastyear: lastyear,
                    month: truemonth,
                    lastmonth: truelastnonth
                },
                success: function (data) {
                    console.log(data);
                    console.log("7777");
                    var num = 0;
                    $('#trr').html("");
                    for (var pn in data) {
                        num++;
                        var htmltd1 = "";
                        var htmltd2 = "";
                        var value = data[pn];
                        var html = "<tr>";
                        html += "<td>日期</td>";
                        html += "<td>姓名</td>";
                        html += "<td>上午</td>";
                        html += "<td>下午</td>";
                        //html += "<td>" + dename + "</td>";
                        //html += "<td>" + pn + "</td>";
                        var a = 0;
                        var b = 0;
                        var c = 0;
                        var d = 0;
                        var e = 0;
                        var f = 0;
                        for (var i = 21; i <=31; i++) {
                            var date = lastyear + "-" + truelastnonth + "-" + i;
                            if (isInArray2(date) && (isNotInArray2(date) || weekday(date))) {
                                a++;
                                const ii = i - 1;
                                htmltd1 += "<tr><td>"+date+"</td><td>"+pn+"</td><td style='height: 20px'>" + typetext(value[ii][0]) + "</td>";
                                if (value[ii][0] == 0 || value[ii][0] == 10 || value[ii][0] == 12 ) {
                                    b += 0.5;
                                }
                                htmltd1 += "<td style='height: 20px'>" + typetext(value[ii][1]) + "</td></tr>";
                                if (value[ii][1] == 0 || value[ii][1] == 10 || value[ii][0] == 12 ) {
                                    b += 0.5;
                                }
                                var  zhi0=isjqdate(c,d,e,f,value[ii]);
                                c=zhi0[0];
                                d=zhi0[1];
                                e=zhi0[2];
                                f=zhi0[3];
                            } else {
                                htmltd1 += "<tr><td>"+date+"</td><td>"+pn+"</td><td style='height: 20px'></td>";
                                htmltd1 += "<td style='height: 20px'></td></tr>";
                            }
                        }
                        for (var i = 1; i <= 20; i++) {
                            if (i < 10) {
                                var da = "0" + i;
                            }else {
                                 da = i;
                            }
                            var date = year + "-" + truemonth + "-" + da;
                            if (isInArray2(date) && (isNotInArray2(date) || weekday(date))) {
                                a++;
                                const ii = i - 1;
                                htmltd2 += "<tr><td>"+date+"</td><td>"+pn+"</td><td style='height: 20px'>" + typetext(value[ii][0]) + "</td>";
                                if (value[ii][0] == 0 || value[ii][0] == 10 || value[ii][0] == 12 ) {
                                    b += 0.5;
                                }
                                htmltd2 += "<td style='height: 20px'>" + typetext(value[ii][1]) + "</td></tr>";
                                typetext(value[ii][1])
                                if (value[ii][1] == 0 || value[ii][1] == 10 || value[ii][0] == 12 ) {
                                    b += 0.5;
                                }
                                var  zhi0=isjqdate(c,d,e,f,value[ii]);
                                c=zhi0[0];
                                d=zhi0[1];
                                e=zhi0[2];
                                f=zhi0[3];
                            } else {
                                htmltd2 += "<tr><td>"+date+"</td><td>"+pn+"</td><td style='height: 20px'></td>";
                                htmltd2 += "<td style='height: 20px'></td></tr>";
                            }
                        }
                        html += htmltd1 + htmltd2;
                        html += "<td>应出勤</td>";
                        html += "<td>" + a + "</td>";
                        html += "<td>实出勤</td>";
                        html += "<td>" + b + "</td> </tr>";
                        html += "<tr><td>旷工</td>";
                        html += "<td>" + c + "</td>";
                        html += "<td>事假</td>";
                        html += "<td>" + d + "</td></tr>";
                        html += "<tr><td>病假</td>";
                        html += "<td>" + e + "</td>";
                        html += "<td>年休假</td>";
                        html += "<td>" + f + "</td>";
                        html += "</tr>";
                        $('#trr').append(html);
                    }
                },
                error: function (error) {
                }
            });
        }
        function typetext(num){
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
                default:
                    text = '';
                    break;
            }
            return text;
        }

        function weekday(myDate) {
            var weekDay = ["星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
            var day = weekDay[new Date(myDate).getDay()];
            if (day == "星期天" || day == "星期六") {
                return false;
            } else {
                return true;
            }
        }

        //在不在法定节假日之内，在的话返回false，不在返回true
        function isInArray2(myDate) {
            var arr = ${holiday};
            var index = $.inArray(myDate, arr);
            console.log(myDate, index)
            if (index >= 0) {
                return false;
            }
            return true;
        }

        function isNotInArray2(myDate) {
            var arr = ${jjrtx};
            var index = $.inArray(myDate, arr);
            if (index >= 0) {
                return true;
            }
            return false;
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

        function preview(oper) {
            if (oper < 10) {
                bdhtml = window.document.body.innerHTML;
                sprnstr = "<!--startprint" + oper + "-->";
                eprnstr = "<!--endprint" + oper + "-->";
                prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 18);
                prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));
                window.document.body.innerHTML = prnhtml;
                window.print();
                window.document.body.innerHTML = bdhtml;
            } else {
                window.print();
            }
        }

        var idTmr;

        //兼容的浏览器
        function getExplorer() {
            var explorer = window.navigator.userAgent;
            //ie
            if (explorer.indexOf("MSIE") >= 0) {
                return 'ie';
            }
            //firefox
            else if (explorer.indexOf("Firefox") >= 0) {
                return 'Firefox';
            }
            //Chrome
            else if (explorer.indexOf("Chrome") >= 0) {
                return 'Chrome';
            }
            //Opera
            else if (explorer.indexOf("Opera") >= 0) {
                return 'Opera';
            }
            //Safari
            else if (explorer.indexOf("Safari") >= 0) {
                return 'Safari';
            }
        }

        //导出excel
        function exportexcelseal(tableid) {
            if (getExplorer() == 'ie') {
                var curTbl = document.getElementById(tableid);
                var oXL = new ActiveXObject("Excel.Application");
                var oWB = oXL.Workbooks.Add();
                var xlsheet = oWB.Worksheets(1);
                var sel = document.body.createTextRange();
                sel.moveToElementText(curTbl);
                sel.select();
                sel.execCommand("Copy");
                xlsheet.Paste();
                oXL.Visible = true;

                try {
                    var fname = oXL.Application.GetSaveAsFilename("Excel.xls", "Excel Spreadsheets (*.xls), *.xls");
                } catch (e) {
                    print("Nested catch caught " + e);
                } finally {
                    oWB.SaveAs(fname);
                    oWB.Close(savechanges = false);
                    oXL.Quit();
                    oXL = null;
                    idTmr = window.setInterval("Cleanup();", 1);
                }
            }
            else {
                tableToExcel(tableid)
            }
        }

        function Cleanup() {
            window.clearInterval(idTmr);
            CollectGarbage();
        }

        var tableToExcel = (function () {
            var uri = 'data:application/vnd.ms-excel;base64,',
                template = '<html><head><meta charset="UTF-8"></head><body><table>{table}</table></body></html>',
                base64 = function (s) {
                    return window.btoa(unescape(encodeURIComponent(s)))
                },
                format = function (s, c) {
                    return s.replace(/{(\w+)}/g,
                        function (m, p) {
                            return c[p];
                        })
                }
            return function (table, name) {
                if (!table.nodeType) table = document.getElementById(table)
                var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
                window.location.href = uri + base64(format(template, ctx))
            }
        })()

        //导出word
        function exportwordseal() {
            $("#sealdiv").wordExport('培训实施记录表')
        }


    </script>
</head>
<body onload="starscript('${name}','${year}','${lastyear}','${month}','${lastmonth}')">
<div>
    <%--<input type=button name='button_export' value='打印' onclick=preview(1)>
    <input type=button name='button_export' value='导出excel' onclick="exportexcelseal('sealdiv')">
    <input type=button name='button_export' value='导出word' onclick="exportwordseal();">--%>
</div>
<div>
    <label style="text-align: left">部门：${dept}</label>
    <table border="1px">
        <thead>
        <%--<tr>--%>
            <%--<td>日期</td>--%>
            <%--<td>姓名</td>--%>
            <%--<td>上午出勤情况</td>--%>
            <%--<td>下午出勤情况</td>--%>
        <%--</tr>--%>
        </thead>
        <tbody id="trr">
        </tbody>
    </table>
</div>
<script>
</script>
</body>
</html>
<%--
  Created by IntelliJ IDEA.
  User: xingyu
  Date: 2020/1/16
  Time: 10:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.oa.core.util.SpringContextUtil" %>
<%@ page import="com.oa.core.service.util.TableService" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.oa.core.service.module.DepartmentService" %>
<%@ page import="com.oa.core.bean.module.Department" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.oa.core.util.CommonUtil" %>
<%@ page import="com.oa.core.util.ToNameUtil" %>
<%@ page import="com.oa.core.helper.DateHelper" %>
<%
    String type = request.getParameter("type");
    String year = request.getParameter("year");
    int lastyear = Integer.parseInt(year)-1;
    String bet = "";
    if(year==null || year.equals("")){
        year = DateHelper.getYear();
    }
    switch (type) {
        case "1":
            bet = "BETWEEN '"+lastyear+"-12-21' AND '"+year+"-03-20'";
            break;
        case "2":
            bet = "BETWEEN '"+year+"-03-21' AND '"+year+"-06-20'";
            break;
        case "3":
            bet = "BETWEEN '"+year+"-06-21' AND '"+year+"-09-20'";
            break;
        case "4":
            bet = "BETWEEN '"+year+"-09-21' AND '"+year+"-12-20'";
            break;
        default:
            break;
    }
    TableService t = (TableService) SpringContextUtil.getBean("tableService");
    DepartmentService d = (DepartmentService) SpringContextUtil.getBean("departmentService");
    List<String> deptIdList = t.selectSql("SELECT useridtodeptid(recordName) as deptId FROM bmpxj19121901 WHERE curStatus=2 AND pxksr19121901 "+bet+" GROUP BY recordName");
    Map<String, List<Map<String, Object>>> map = new HashMap();
    Map<String, List<String>> maplist = new HashMap<>();
    for (String deptId : deptIdList) {
        List<Map<String, Object>> maps = t.selectSqlMapList("SELECT useridstoname(cjry191219001) as 参加人员,dd19121900001 as 地点,jfly191219001 as 经费来源,mdmb191219001 as 目的目标,pxdx191219001 as 培训对象,pxfs191219001 as 培训方式,pxjf191219001 as 培训经费,pxjhb19123101 as 培训计划编号,pxksr19121901 as 培训日期,rs19121900001 as 人数,skls191219001 as 授课教师,xgpjf19121901 as 效果评价方式,xmmc191219001 as 项目名称,xmnr191219001 as 项目内容,xs19121900001 as 学时 FROM bmpxj19121901 WHERE curStatus=2 AND pxksr19121901 "+bet+" AND useridtodeptid(recordName)='" + deptId + "'");
        Department department = d.selectById(deptId);
        List<String> l = new ArrayList<>();
        l.add(0, department.getDeptName());
        String a = "<img class='signature' src=\"/upload/signature/";
        String b = ".png\">";
        l.add(1, a+department.getDeptHead()+b);
        l.add(2, a+CommonUtil.usertoLeadership(deptId)+b);
        map.put(deptId, maps);
        maplist.put(deptId, l);
    }
    request.setAttribute("deptIdList", deptIdList);
    request.setAttribute("mapVals", map);
    request.setAttribute("maplist", maplist);
%>
<html>
<head>
    <title>部门培训打印</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <style>
        .tables-valuebox, .tables-valuebox tr th, .tables-valuebox tr td {
            border: 1px solid #383838;
        }

        .tables-valuebox {
            width: 1086px;
            min-height: 25px;
            line-height: 25px;
            text-align: center;
            border-collapse: collapse;
        }
        .signature{
            width: 120px;
            height: 50px;
        }
    </style>
    <script type="text/javascript">
        function preview(oper) {
            console.log(oper)
            if (oper < 20) {
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
            } else {
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
            $("#sealdiv").wordExport('培训签到表')
        }


    </script>
</head>
<body>
    <div>
        <input type=button name='button_export' value='打印' onclick=preview('${requestScope.offset+status1.index}')>
        <input type=button name='button_export' value='导出excel' onclick="exportexcelseal('sealdiv')">
        <%--<input type=button name='button_export' value='导出word' onclick="exportwordseal();">--%>
    </div>
    <div id="sealdiv" style="width: 1086px;margin-bottom: 50px;">
        <c:forEach items="${deptIdList}" var="deptid" varStatus="status1">
            <!--startprint${requestScope.offset+status1.index}-->
                <table width="1086px">
                    <thead>
                    <tr>
                        <th colspan="15">
                            <div style="width: 1086px;font-size:15px;height:30px" align='left' valign='middle' colspan="4">
                                ZJY-GS-602Bc-2019/0
                            </div>
                        </th>
                    </tr>
                    <tr>
                        <th colspan="15">
                            <div style="width: 1086px;font-size:30px;height:70px" align='center' valign='middle' colspan="4">
                                部门培训计划表
                            </div>
                        </th>
                    </tr>
                    <tr>
                        <th colspan="1" style="width:70px;text-align: left;">部门：</th>
                        <th colspan="4" style="width:290px;text-align: left;">${maplist.get(deptid).get(0)}</th>
                        <th colspan="1" style="width:100px;text-align: left;">部门负责人：</th>
                        <th colspan="4" style="width:270px;text-align: left;">${maplist.get(deptid).get(1)}</th>
                        <th colspan="1" style="width:100px;text-align: left;">主管院领导：</th>
                        <th colspan="4" style="width:270px;text-align: left;">${maplist.get(deptid).get(2)}</th>
                    </tr>
                    </thead>
                </table>
                <table class="tables-valuebox">
                    <thead>
                    <tr>
                        <th>编号</th>
                        <th>项目名称</th>
                        <th>项目内容</th>
                        <th>培训对象</th>
                        <th>参加人员</th>
                        <th>人数</th>
                        <th>学时</th>
                        <th>培训日期</th>
                        <th>地点</th>
                        <th>目的目标</th>
                        <th>授课教师</th>
                        <th>培训费用</th>
                        <th>经费来源</th>
                        <th>效果评价方式</th>
                        <th>培训方式</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${mapVals.get(deptid)}" var="map" varStatus="status">
                        <tr>
                            <td>${requestScope.offset+status.index+1}</td>
                            <td>${map.get("项目名称")}</td>
                            <td>${map.get("项目内容")}</td>
                            <td>${map.get("培训对象")}</td>
                            <td>${map.get("参加人员")}</td>
                            <td>${map.get("人数")}</td>
                            <td>${map.get("学时")}</td>
                            <td>${map.get("培训日期")}</td>
                            <td>${map.get("地点")}</td>
                            <td>${map.get("目的目标")}</td>
                            <td>${map.get("授课教师")}</td>
                            <td>${map.get("培训费用")}</td>
                            <td>${map.get("经费来源")}</td>
                            <td>${map.get("效果评价方式")}</td>
                            <td>${map.get("培训方式")}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            <!--endprint${requestScope.offset+status1.index}-->
        </c:forEach>
    </div>
</body>
</html>

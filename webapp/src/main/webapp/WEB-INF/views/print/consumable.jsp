<%--
  User: zxd
  Date: 2020/03/27
  Time: 下午 2:30
  Explain: 说明
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.springframework.util.Base64Utils" %>
<%@ page import="com.oa.core.service.util.TableService" %>
<%@ page import="com.oa.core.util.SpringContextUtil" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<%
    String recorderNO = request.getParameter("recorderNO");
    String linkRecNo = request.getParameter("linkRecNo");
    TableService t = (TableService) SpringContextUtil.getBean("tableService");
    List<Map<String, Object>> maps = t.selectSqlMapList("SELECT *,useridstoname(syr2003270001) AS sqr FROM hccgj20032701  WHERE curStatus=2 AND linkRecorderNO='"+linkRecNo+"'");
    if (maps != null && maps.size()>0) {
        request.setAttribute("maplist", maps);
        request.setAttribute("mapsize", maps.size());
    }else{
        request.setAttribute("maplist", null);
        request.setAttribute("mapsize", 0);
    }
    Map<String, Object> hmap = t.selectSqlMap("select * from hccgt20032701 where curStatus=2 and recorderNO='" + recorderNO + "'");
    String sqrq = hmap.get("sqrq200327003").toString();
    request.setAttribute("title", sqrq.substring(0,4)+"年"+sqrq.substring(6,7)+"月实验室耗材采购计划申请/验收表");
    request.setAttribute("hmap", hmap);
    System.out.println(hmap);
    if(hmap!=null) {
        Map<String, Object> dept = t.selectSqlMap("select * from department where curStatus=2 and deptId='" + hmap.get("sqbm200327003") + "'");
        System.out.println(dept);
        request.setAttribute("dept", dept.get("deptHead"));
    }
%>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" type="text/css" href="/zjyoa-pc/resources/css/print.css"/>

<jsp:include page="/common/js.jsp"></jsp:include>

<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>
<script type="text/javascript" src="/zjyoa-pc/resources/js/printword/FileSaver.js"></script>
<script type="text/javascript" src="/zjyoa-pc/resources/js/printword/jquery.wordexport.js"></script>
<script type="text/javascript">
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
    function  getExplorer() {
        var explorer = window.navigator.userAgent ;
        //ie
        if (explorer.indexOf("MSIE") >= 0) {
            return 'ie';
        }
        //firefox
        else if (explorer.indexOf("Firefox") >= 0) {
            return 'Firefox';
        }
        //Chrome
        else if(explorer.indexOf("Chrome") >= 0){
            return 'Chrome';
        }
        //Opera
        else if(explorer.indexOf("Opera") >= 0){
            return 'Opera';
        }
        //Safari
        else if(explorer.indexOf("Safari") >= 0){
            return 'Safari';
        }
    }

    //导出excel
    function exportexcelfw(tableid) {
        if(getExplorer()=='ie')
        {
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
        else
        {
            tableToExcel(tableid)
        }
    }
    function Cleanup() {
        window.clearInterval(idTmr);
        CollectGarbage();
    }
    var tableToExcel = (function() {
        var uri = 'data:application/vnd.ms-excel;base64,',
            template = '<html><head><meta charset="UTF-8"></head><body><table>{table}</table></body></html>',
            base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) },
            format = function(s, c) {
                return s.replace(/{(\w+)}/g,
                    function(m, p) { return c[p]; }) }
        return function(table, name) {
            if (!table.nodeType) table = document.getElementById(table)
            var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
            window.location.href = uri + base64(format(template, ctx))
        }
    })()

    //导出word
    function exportwordfw() {
        $("#fwdiv").wordExport('${title}')
    }
</script>
<html>
<head>
    <title>${title}</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick=preview(1)>
    <input type=button name='button_export' value='导出excel' onclick="exportexcelfw('fwdiv')">
    <input type=button name='button_export' value='导出word' onclick="exportwordfw()">
</div>
<div id="fwdiv" style="width: 756px;height: 1086px;">
    <!--startprint1-->
    <table style="width: 756px;height:100px">
        <tr>
            <td style="width: 756px;" align='left' valign='middle' colspan="4">
                ZJY-GS-604Bq-2019/0
            </td>
        </tr>
        <tr>
            <td style="width: 756px;font-size:30px;height:50px" align='center' valign='middle' colspan="4">
                ${title}
            </td>
        </tr>
        <tr>
            <td colspan="2">
                部门名称：<s:dic type="dept" value="${hmap.sqbm200327003}"></s:dic>
            </td>
            <td colspan="2">
                编制日期：${hmap.sqrq200327003}
            </td>
        </tr>
    </table>
    <table border="1" cellspacing="0" style="width: 756px;height:1086px">
        <thead>
            <tr>
                <th>序号</th>
                <th>物品名称</th>
                <th>规格编号</th>
                <th>用途</th>
                <th>质量要求</th>
                <th>单位</th>
                <th>数量</th>
                <th>制造商</th>
                <th>使用人</th>
                <th>符合性验收</th>
                <th>验收日期</th>
                <th>验收人</th>
            </tr>
        </thead>
        <tbody>
        <c:if test="${!empty maplist}">
            <c:forEach items="${maplist}" var="map" varStatus="status">
                <tr style="height: 30px;">
                    <td>${ status.index + 1}</td>
                    <td>${map.wpmc200327001}</td>
                    <td>${map.ggxh200327001}</td>
                    <td>${map.yt20032700001}</td>
                    <td>${map.zlyq200327001}</td>
                    <td>${map.dw20032700001}</td>
                    <td>${map.sl20032700001}</td>
                    <td>${map.zzs2003270001}</td>
                    <td>${map.sqr}</td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </c:forEach>
        </c:if>
        <c:forEach varStatus="status" begin="${mapsize}" end="14">
            <tr style="height: 30px;">
                <td>${ status.index + 1}</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </c:forEach>
        <tr style="height: 100px;">
            <td colspan="12">对上月采购的质量及服务的意见和建议：${hmap.yj20032700005}</td>
        </tr>
        <tr style="height: 50px;">
            <td colspan="6">低值易耗品管理员：<s:dic type="userimg" value="jiajie"></s:dic></td>
            <td colspan="6">部门主任：<s:dic type="userimg" value="${dept}"></s:dic></td>
        </tr>
        </tbody>
    </table>
    <!--endprint1-->
</div>
</body>
</html>

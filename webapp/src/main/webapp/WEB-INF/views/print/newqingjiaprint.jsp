<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/5/21
  Time: 14:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.springframework.util.Base64Utils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" type="text/css" href="/zjyoa-pc/resources/css/print.css"/>

<jsp:include page="/common/js.jsp"></jsp:include>
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
<%
    String decodeUrl = request.getParameter("url");
    if (decodeUrl != null) {
        String url = new String(Base64Utils.decode(decodeUrl.getBytes()));
        request.setAttribute("url", url);
    }
%>
<%--导出word引入的js--%>
<script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.js"></script>
<script type="text/javascript" src="/zjyoa-pc/resources/js/printword/FileSaver.js"></script>
<script type="text/javascript" src="/zjyoa-pc/resources/js/printword/jquery.wordexport.js"></script>
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>
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
    function exportexcelseal(tableid) {
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
    function exportwordseal() {
        $("#sealdiv").wordExport('省质检研究院请假审批单')
    }


</script>
<html>
<head>
    <title>省质检研究院请假审批单</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick=preview(1)>
    <input type=button name='button_export' value='导出excel' onclick="exportexcelseal('sealdiv')">
    <input type=button name='button_export' value='导出word' onclick="exportwordseal();">
</div>
<div id="sealdiv" style="width: 756px;height: 586px;">
<!--startprint1-->
<table style="width: 756px;height: 100px">
    <tr>
        <td style="width: 756px;font-size:30px;height:100px" align='center' valign='middle' colspan="4">省质检研究院请假审批单
        </td>
    </tr>
</table>
<table border="1" cellspacing="0" style="width: 794px;height: 450px">
    <tr style="height: 20px;">
        <td style="width: 100px;text-align: center">部门</td>
        <td style="width: 100px;text-align: center"colspan="2">
            <c:if test="${data.get('tab').size()>0 && data.get('tab') != null}">
                <c:forEach items="${data.get('tab')}" var="da" varStatus="status">
                    <s:dic type="dept" value="${da.get('bm')}"></s:dic>
                </c:forEach>
            </c:if>
        </td>
        <td style="width: 100px; text-align: center">申请人</td>
        <td style="width: 100px; text-align: center"colspan="2">
            <c:if test="${data.get('tab').size()>0 && data.get('tab') != null}">
                <c:forEach items="${data.get('tab')}" var="da" varStatus="status">
                    ${da.get("sqr")}
                </c:forEach>
            </c:if>
        </td>

    </tr>
    <tr style="height: 20px;">
        <td style="width: 100px;text-align: center">参加工作时间</td>
        <td style="width: 100px;text-align: center"colspan="2">
            <c:if test="${data.get('tab').size()>0 && data.get('tab') != null}">
                <c:forEach items="${data.get('tab')}" var="da" varStatus="status">
                    <s:dic type="dept" value="${da.get('cjgzs')}"></s:dic>
                </c:forEach>
            </c:if>
        </td>
        <td style="width: 100px; text-align: center">请假类别</td>
        <td style="width: 100px; text-align: center"colspan="2">
            <c:if test="${data.get('tab').size()>0 && data.get('tab') != null}">
                <c:forEach items="${data.get('tab')}" var="da" varStatus="status">
                    ${da.get("qjlb")}
                </c:forEach>
            </c:if></td>

    </tr>
    <tr style="height:50px" aria-rowspan="2">
        <td style="width: 100px;text-align: center">事由</td>
        <td style="width: 620px;text-align: center"colspan="5">
            <c:if test="${data.get('tab').size()>0 && data.get('tab') != null}">
                <c:forEach items="${data.get('tab')}" var="da" varStatus="status">
                    ${da.get("qjsy")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 50px;">
        <td style="width: 100px;text-align: center">起止时间</td>
        <td style="width: 620px;text-align: center"colspan="5">
            <p style=" ">
                <label>工作岗位：<span style="margin-left: 80px"></span></label>
                <label>休假期间工作由<span style="margin-left: 80px"></span>负责</label>
            </p><br>
            <c:if test="${data.get('tab').size()>0 && data.get('tab') != null}">
                <c:forEach items="${data.get('tab')}" var="da" varStatus="status">
                   ${da.get("qjkss")}至${da.get("qjjss")}，共${da.get("qjts")}天
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 50px;" aria-rowspan="2">
        <td style="width: 100px;text-align: center">部门负责人意见</td>
        <td style="width: 620px;text-align: center"colspan="5">
            <c:if test="${data.get('bmyj').size()>0 && data.get('bmyj') != null}">
                <c:forEach items="${data.get('bmyj')}" var="da" varStatus="status">
                    ${da.get("yj")}
                    ${da.get("NAME")}
                    ${da.get("time")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height:50px" aria-rowspan="2">
        <td style="width: 100px;text-align: center">人力资源部意见</td>
        <td style="width: 620px;text-align: center"colspan="5">
            <c:if test="${data.get('rlzy').size()>0 && data.get('rlzy') != null}">
                <c:forEach items="${data.get('rlzy')}" var="da" varStatus="status">
                    ${da.get("yj")}
                    ${da.get("NAME")}
                    ${da.get("time")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height:50px" aria-rowspan="2">
        <td style="width: 100px;text-align: center">主管院长意见</td>
        <td style="width: 620px;text-align: center"colspan="5">
            <c:if test="${data.get('zgyz').size()>0 && data.get('zgyz') != null}">
                <c:forEach items="${data.get('zgyz')}" var="da" varStatus="status">
                    ${da.get("yj")}
                    ${da.get("NAME")}
                    ${da.get("time")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height:50px"aria-rowspan="2">
        <td style="width: 100px;text-align: center">院长意见</td>
        <td style="width: 620px;text-align: center"colspan="5">
            <c:if test="${data.get('yzyj').size()>0 && data.get('yzyj') != null}">
                <c:forEach items="${data.get('yzyj')}" var="da" varStatus="status">
                    ${da.get("yj")}
                    ${da.get("NAME")}
                    ${da.get("time")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
</table>
<!--endprint1-->
</div>
</body>
</html>

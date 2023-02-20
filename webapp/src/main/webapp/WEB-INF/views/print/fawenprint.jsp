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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<%
    String decodeUrl = request.getParameter("url");
    if (decodeUrl != null) {
        String url = new String(Base64Utils.decode(decodeUrl.getBytes()));
        request.setAttribute("url", url);
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
        $("#fwdiv").wordExport('发文审批笺')
    }
</script>
<html>
<head>
    <title>打印发文页</title>
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
        <td style="width: 756px;font-size:30px;height:50px" align='center' valign='middle' colspan="4">
            河北省产品质量监督检验研究院发文审批笺
        </td>
    </tr>
</table>
<table border="1" cellspacing="0" style="width: 756px;height:1086px">
    <tr style="height: 300px">
        <td style="width: 100px;text-align: center">领导批示意见</td>
        <td style="width: 656px;text-align: center" colspan="5">
            <c:forEach items="${data}" var="da" varStatus="status">
                <c:if test="${fn:contains(da,'yzsm')}">
                    <div class="msg-box">
                        <label class="title">${da.yzsm}</label>
                        <label class="name">${da.yzname}</label>
                        <label class="time">${da.yztime}</label>
                    </div>
                </c:if>
            </c:forEach>
        </td>
    </tr>
    <tr style="height: 250px">
        <td style="width: 100px;text-align: center">主管领导意见</td>
        <td style="width: 656px;text-align: center" colspan="5">
            <c:forEach items="${data}" var="da" varStatus="status">
                <c:if test="${fn:contains(da,'zgysm')}">
                    <div class="msg-box">
                        <label class="title">${da.zgysm}</label>
                        <label class="name">${da.zgyname}</label>
                        <label class="time">${da.zgytime}</label>
                    </div>
                </c:if>
            </c:forEach>
        </td>
    </tr>
    <tr style="height: 150px">
        <td style="width: 100px;text-align: center">办公室审核意见</td>
        <td style="width: 656px;text-align: center" colspan="5">
            <c:forEach items="${data}" var="da" varStatus="status">
                <c:if test="${fn:contains(da,'bgssm')}">
                    <div class="msg-box">
                        <label class="title">${da.bgssm}</label>
                        <label class="name">${da.bgsname}</label>
                        <label class="time">${da.bgstime}</label>
                    </div>
                </c:if>
            </c:forEach>
        </td>
    </tr>
    <tr style="height:80px">
        <td style="width: 100px;text-align: center">文件标题</td>
        <td style="width: 656px;text-align: center" colspan="5">${data.get(0).get('title')}</td>
    </tr>
    <tr style="height: 100px">
        <td style="width: 100px;text-align: center">部门意见</td>
        <td style="width: 306px;text-align: center" colspan="3">
            <c:forEach items="${data}" var="da" varStatus="status">
                <c:if test="${fn:contains(da,'cbsm')}">
                    <div class="msg-box">
                        <label class="title">${da.cbsm}</label>
                        <label class="name">${da.cbname}</label>
                        <label class="time">${da.cbtime}</label>
                    </div>
                </c:if>
            </c:forEach>
        </td>
        <td style="width: 150px;text-align: center">份数</td>
        <td style="width: 150px;text-align: center">${data.get(0).get('numb')}</td>
    </tr>
    <tr style="height: 100px">
        <td style="width: 150px;text-align: center">起草部门</td>
        <td style="width: 150px;text-align: center">${data.get(0).get('dept')}</td>
        <td style="width: 100px;text-align: center">起草人</td>
        <td style="width: 100px;text-align: center">${data.get(0).get('people')}</td>
        <td style="width: 100px;text-align: center">电话</td>
        <td style="width: 100px;text-align: center">${data.get(0).get('phone')}</td>
    </tr>
</table>
<!--endprint1-->
</div>
</body>
</html>

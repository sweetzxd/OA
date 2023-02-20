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
        $("#fwdiv").wordExport('发文稿纸')
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
        <td style="width: 756px;font-size:30px;height:50px" align='center'  colspan="5">
            河北省产品质量监督检验院发文稿纸
        </td>
    </tr>
</table>


    <table frame="hsides" width="756px" border="1px" cellspacing="0" >
        <tr style="height: 20px">
            <td colspan="3" style="text-align: left;border-left-style:none" >签发意见：</td>
            <td  style="text-align: left;border-right-style:none">文&nbsp件&nbsp&nbsp字：</td>
            <td  style="text-align: left;border-left-style:none;border-right-style:none">${data.get("sendNO")}</td>
        </tr>
        <tr style="height: 20px">
            <td colspan="3" style="text-align: left;border-left-style:none;border-bottom-style: none" >${data.get("yzyjsm")}</td>
            <td  style="text-align: left;border-right-style:none">拟稿单位：</td>
            <td  style="text-align: left;border-left-style:none;border-right-style:none">${data.get("senddept")}</td>
        </tr>
        <tr style="height: 20px">
            <td colspan="3" style="text-align: right;border-left-style:none;border-top-style:none;border-bottom-style: none" >${data.get("yzname")}${data.get("yztime")}</td>
            <td  style="text-align: left;border-right-style:none">拟&nbsp&nbsp稿&nbsp&nbsp人：</td>
            <td  style="text-align: left;border-left-style:none;border-right-style:none">${data.get("sendman")}</td>
        </tr>
        <tr style="height: 20px">
            <td colspan="3" style="text-align: left;border-left-style:none;border-top-style:none;border-bottom-style: none" >${data.get("zgyj")}${data.get("zgyjsm")}</td>
            <td  style="text-align: left;border-right-style:none">电&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp话：</td>
            <td  style="text-align: left;border-left-style:none;border-right-style:none"></td>
        </tr>
        <tr style="height: 20px">
            <td colspan="3" style="text-align: right;border-left-style:none;border-top-style:none;border-bottom-style: none" >${data.get("zgname")}${data.get("zgtime")}</td>
            <td  style="text-align: left;border-right-style:none">保密期限：</td>
            <td  style="text-align: left;border-left-style:none;border-right-style:none">${data.get("bmqx")}</td>
        </tr>
        <tr style="height: 20px">
            <td colspan="3" style="text-align: left;border-left-style:none;border-top-style:none;border-bottom-style: none" >${data.get("bzyj")}${data.get("bzyjsm")}</td>
            <td  style="text-align: left;border-right-style:none">信息公开：</td>
            <td  style="text-align: left;border-left-style:none;border-right-style:none">${data.get("xxgk")}</td>
        </tr>
        <tr style="height: 20px">
            <td colspan="3" style="text-align: right;border-left-style:none;border-top-style:none" >${data.get("bzname")}${data.get("bztime")}</td>
            <td  style="text-align: left;border-right-style:none">密&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp级：&nbsp&nbsp${data.get("mj")}</td>
            <td colspan="2" style="text-align: left;border-left-style:none;border-right-style:none">缓急：&nbsp&nbsp${data.get("hj")}</td>
        </tr>
        <tr style="height: 30px">
            <td colspan="5" style="text-align: left;border-left-style:none;border-right-style:none">标&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp题&nbsp&nbsp${data.get("title")}</td>
        </tr>
        <tr style="height: 30px">
            <td colspan="5" style="text-align: left;border-left-style:none;border-right-style:none">主送单位&nbsp&nbsp${data.get("zsdw")}</td>
        </tr>
        <tr style="height: 30px">
            <td colspan="5" style="text-align: left;border-left-style:none;border-right-style:none">抄送单位&nbsp&nbsp${data.get("csdw")}</td>
        </tr>
        <tr style="height: 30px">
            <td colspan="5" style="text-align: left;border-left-style:none;border-right-style:none">局内发送&nbsp&nbsp${data.get("jnfs")}</td>
        </tr>
        <tr style="height: 30px">
            <td colspan="5" style="text-align: left;border-left-style:none;border-right-style:none">附&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp件&nbsp&nbsp${data.get("file")}</td>
        </tr>
        <tr style="height: 20px">
            <td colspan="5" style="text-align: left;border-left-style:none;border-right-style:none">主&nbsp题&nbsp&nbsp词&nbsp&nbsp${data.get("ztc")}</td>
        </tr>
        <tr style="height: 20px">
            <td colspan="3" style="text-align: right;border-left-style:none" >正文共&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp页</td>
            <td  style="text-align: left;border-right-style:none"></td>
            <td colspan="2" style="text-align: right;border-left-style:none;border-right-style:none">共印&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp份</td>
        </tr>
    </table>

<!--endprint1-->
</div>
</body>
</html>

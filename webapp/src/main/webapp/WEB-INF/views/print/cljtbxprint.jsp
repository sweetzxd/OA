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
<style>
    @media print{
        @page{
            margin:0;
            size:auto;
        }
    }
</style>
<jsp:include page="/common/js.jsp"></jsp:include>
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
<%--导出word引入的js--%>
<script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.js"></script>
<script type="text/javascript" src="/zjyoa-pc/resources/js/printword/FileSaver.js"></script>
<script type="text/javascript" src="/zjyoa-pc/resources/js/printword/jquery.wordexport.js"></script>
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>
<script type="text/javascript">
    // 禁用右键默认行为
    document.oncontextmenu = function (evt) {
        evt.preventDefault();
    };

    // 属性返回false
    document.oncontextmenu = function(evt) {
        evt.returnValue = false;
    }

    // 或者直接返回整个事件
    document.oncontextmenu = function(){
        return false;
    }

    document.oncopy = function(evt) {
        evt.returnValue = false;
    }

    // 整个事件直接返回false
    document.oncopy = function(){
        return false;
    }

    document.onselectstart = function(evt) {
        evt.returnValue = false;
    }

    // 整个事件直接返回false
    document.onselectstart = function(){
        return false;
    }
    function preview(oper) {
        if (oper < 10) {
            bdhtml = window.document.body.innerHTML;
            sprnstr = "<!--startprint" + oper + "-->";
            eprnstr = "<!--endprint" + oper + "-->";
            prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 18);
            prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));
            window.document.body.innerHTML = prnhtml;
            document.getElementsByTagName('body')[0].style.zoom=0.70;
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
        $("#sealdiv").wordExport('差旅/交通报销汇总单')
    }


</script>
<html>
<head>
    <title>现金借款延期归还申请单</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick=preview(1)>
<c:if test="${sessionScope.loginer.id == 'admin'}">
    <input type=button name='button_export' value='导出excel' onclick="exportexcelseal('sealdiv')">
    <input type=button name='button_export' value='导出word' onclick="exportwordseal();">
</c:if>
</div>
<!--startprint1-->
<div id="sealdiv" style="width: 756px;height: 506px;margin: 0 auto;">
<table style="width: 756px;height: 90px;padding-top: 35px">
    <tr>
        <td style="width: 756px;font-size:30px;height:90px" align='center' valign='middle' colspan="4">差旅/交通报销汇总单
        </td>
    </tr>
</table>

<table border="0" cellspacing="0" style="width: 756px;height: 20px; overflow-y:scroll">
    <tr>
        <td style="text-align: left" width="25%">申请人：${data.get(0).get("sqr")}</td>
        <td style="text-align: left" width="25%">部门：${data.get(0).get("sqbm")}</td>
        <td style="text-align: left" colspan="2" width="50%">申请时间：${data.get(0).get("bxsjX201117004")}</td>
    </tr>
</table>
<table border="0" cellspacing="0" style="width: 756px;height: 30px; overflow-y:scroll">
        <tr>
            <td style="text-align: left">差旅费 (元)</td>
        </tr>
    </table>

<table border="1" cellspacing="0" style="width: 756px;height: 200px; overflow-y:scroll">
    <tr>
        <td style="text-align: center" width="20%">住宿费</td>
        <td style="text-align: center">${data.get(0).get("zsfyX201117004")}</td>
        <td style="text-align: center" width="20%">车船费/机票</td>
        <td style="text-align: center">${data.get(0).get("ccfjpX20111704")}</td>

    </tr>
      <tr>
          <td style="text-align: center" width="20%">伙食补助费</td>
          <td style="text-align: center">${data.get(0).get("hsbzfX20111704")}</td>
        <td width="20%" style="text-align: center">市内交通费</td>
        <td style="text-align: center">${data.get(0).get("snjtfX20111704")}</td>
    </tr>
    <tr>
        <td style="text-align: center" width="20%">其他</td>
        <td style="text-align: center">${data.get(0).get("qtX20111700007")}</td>
        <td style="text-align: center" width="20%">差旅费合计</td>
        <td style="text-align: center">${data.get(0).get("clfhjX20111704")}</td>
    </tr>

</table>
    <table border="0" cellspacing="0" style="width: 756px;height: 30px; overflow-y:scroll">
        <tr>
            <td style="text-align: left">交通费 (元)</td>
        </tr>
    </table>
    <table border="1" cellspacing="0" style="width: 756px;height: 200px; overflow-y:scroll">
        <tr>
            <td style="text-align: center" width="20%">汽油费</td>
            <td style="text-align: center">${data.get(0).get("qyfyX201117004")}</td>
            <td style="text-align: center" width="20%">停车费</td>
            <td style="text-align: center">${data.get(0).get("tcfyX201117004")}</td>

        </tr>
        <tr>
            <td style="text-align: center" width="20%">过桥费</td>
            <td style="text-align: center">${data.get(0).get("gqfyX201117004")}</td>
            <td width="20%" style="text-align: center">汽车保险费</td>
            <td style="text-align: center">${data.get(0).get("qcbxfX20111704")}</td>
        </tr>
        <tr>
            <td style="text-align: center" width="20%">市内租车费</td>
            <td style="text-align: center">${data.get(0).get("snzcfX20111704")}</td>
            <td style="text-align: center" width="20%">修理费</td>
            <td style="text-align: center">${data.get(0).get("xlfyX201117004")}</td>
        </tr>
        <tr>
            <td style="text-align: center" width="20%">其他</td>
            <td style="text-align: center">${data.get(0).get("qtX20111700008")}</td>
            <td style="text-align: center" width="20%">交通费用合计</td>
            <td style="text-align: center">${data.get(0).get("jtfhjX20111707")}</td>
        </tr>

    </table>

    <table border="1" cellspacing="0" style="width: 756px;height: 50px;margin-top: 20px;margin-bottom: 20px; overflow-y:scroll">
        <tr>
            <td style="text-align: center" width="20%">金额合计(元)</td>
            <td style="text-align: center">${data.get(0).get("bxzjeX20111704")}</td>
            <td style="text-align: center" width="20%">金额大写</td>
            <td style="text-align: center">${data.get(0).get("bxjedX20111704")}</td>

        </tr>
    </table>

<table border="1" cellspacing="0" style="width: 756px;height: 450px; overflow-y:scroll">
    <tr>
        <td style="text-align: center" width="20%">部门负责人</td>
        <td style="text-align: center" colspan="3">
            ${data.get(1).get("yijian")}
        </td>
    </tr>

    <tr>
        <td style="text-align: center" width="20%">财务监督</td>
        <td style="text-align: center" colspan="3">
            ${data.get(2).get("yijian")}
        </td>
    </tr>
    <tr>
        <td style="text-align: center" width="20%">财务复核</td>
        <td style="text-align: center" colspan="3">
            ${data.get(3).get("yijian")}
        </td>
    </tr>

    <tr>
        <td style="text-align: center" width="20%">部门主管院长</td>
        <td style="text-align: center" colspan="3">
            ${data.get(4).get("yijian")}
        </td>
    </tr>
    <tr>
        <td style="text-align: center" width="20%">财务主管院长</td>
        <td style="text-align: center" colspan="3">
            ${data.get(5).get("yijian")}
        </td>
    </tr>
    <tr>
        <td style="text-align: center" width="20%">院长</td>
        <td style="text-align: center" colspan="3">
            ${data.get(6).get("yijian")}
        </td>
    </tr>


</table>
<table style="width: 756px;height: 20px; overflow-y:scroll">
    <tr>
        <td style="text-align: left" width="30%">涂抹及修改无效</td>
    </tr>
</table>
<!--endprint1-->
</div>

</body>
</html>

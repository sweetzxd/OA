<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/5/21
  Time: 14:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.springframework.util.Base64Utils" %>
<%@ page import="com.oa.core.service.util.TableService" %>
<%@ page import="com.oa.core.util.SpringContextUtil" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oa.core.util.ToNameUtil" %>
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

<jsp:include page="/common/js.jsp"></jsp:include>
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
        $("#sealdiv").wordExport('外部培训审批单')
    }


</script>
<html>
<head>
    <title>持续能力评价检验人员</title>
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
<div id="sealdiv" style="width: 100%;height: 986px;">
    <!--startprint1-->
    <table style="width:100%;height: 30px">
        <tr>
            <td style="width: 756px;font-size:16px;height:10px;" align='left' valign='middle' colspan="4">ZJY-GS-602Ab/0</td>
        </tr>
        <tr>
            <td style="width: 100%;font-size:25px;height:20px;" align='center' valign='middle' colspan="4">技术人员持续能力评价表</td>
        </tr>
        <tr>
            <td style="width: 100%;font-size:25px;height:20px;" align='left' valign='middle' colspan="4">年度：${tzmap.get("nd")}</td>
        </tr>
    </table>
    <table border="1" cellspacing="0" style="width: 794px;height: 950px" align='center'>
        <tr style="height: 23px;">
            <td style="width: 100px;text-align: center">姓名</td>
            <td style="width: 100px;text-align: center"> ${tzmap.get("sqr")}</td>
            <td style="width: 100px; text-align: center">所属部门</td>
            <td style="width: 100px; text-align: center">${tzmap.get("sqbm")}</td>
            <td style="width: 100px; text-align: center">职务/职称</td>
            <td style="width: 100px; text-align: center;">${tzmap.get("zwzc")}</td>
        </tr>
        <tr style="height: 13px;">
            <td style="width: 620px;text-align: center"colspan="6">持续能力项目</td>
        </tr>
        <tr style="height: 33px;">
            <td style="width: 620px;text-align: center"colspan="6">
              <p style="text-align: left">1、抽样领域:${tzmap.get("cyly")}</p>
              <p style="text-align: left">2、检验能力范围:${tzmap.get("jynl")}</p>
              <p style="text-align: left">3、特定仪器设备操作:${tzmap.get("tdyq")}</p>
              <p style="text-align: left">4、关键岗位:${tzmap.get("gjgw")}(${tzmap.get("jdly")})</p>
            </td>
        </tr>
        <tr style="height: 13px;">
            <td style="width: 620px;text-align: center;"colspan="6">年度技术工作自我评述</td>
        </tr>
        <tr style="height: 83px;">
            <td style="width: 100px;text-align: left;">重大检验工作<br>能力验证工作</td>
            <td style="width: 520px;text-align: left;"colspan="5">${tzmap.get("zdjyg")}</td>
        </tr>
        <tr style="height: 83px;">
            <td style="width: 100px;text-align: left;">质量工作</td>
            <td style="width: 520px;text-align: left;"colspan="5">${tzmap.get("zlgz")}</td>
        </tr>
        <tr style="height: 83px;">
            <td style="width: 100px;text-align: left;">科研项目创新及标准制修订工作</td>
            <td style="width: 520px;text-align: left;"colspan="5">${tzmap.get("kyxmc")}</td>
        </tr>
        <tr style="height: 83px;">
            <td style="width: 100px;text-align: left;">培训经历</td>
            <td style="width: 520px;text-align: left;"colspan="5">${tzmap.get("pxjl")}</td>
        </tr>
        <tr style="height: 83px;">
            <td style="width: 100px;text-align: left;">其他业绩</td>
            <td style="width: 520px;text-align: left;"colspan="5">${tzmap.get("qtyj")}</td>
        </tr>
        <tr style="height: 113px;">
            <td style="width: 620px;"colspan="6">
                <p style="text-align: left" valign="top">部门评定意见:</p>
                <p>${bzsqmap.get("spyj")}</p>
                <p style="text-align: right">部门负责人:${bzsqmap.get("modifyName")}${bzsqmap.get("modifyTime")}</p>
            </td>
        </tr>
        <tr style="height: 113px;">
            <td style="width: 620px;"colspan="6">
                <p style="text-align: left" valign="top">专业技术负责人评定意见:</p>
                <p>${zyjsmap.get("spyj")}</p>
                <p style="text-align: right">专业技术负责人:${zyjsmap.get("modifyName")}${zyjsmap.get("modifyTime")}</p>
            </td>
        </tr>
    </table>
    <span>注：该单据手写无效</span>
    <!--endprint1-->
</div>
</body>
</html>

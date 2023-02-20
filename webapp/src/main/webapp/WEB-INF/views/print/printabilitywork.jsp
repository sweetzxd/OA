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
        $("#sealdiv").wordExport('上岗能力确认评价表')
    }


</script>
<html>
<head>
    <title>上岗能力确认评价表</title>
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
            <td style="width: 756px;font-size:16px;height:10px;" align='left' valign='middle' colspan="4">ZJY-GS-602Aa/0</td>
        </tr>
        <tr>
            <td style="width: 100%;font-size:25px;height:20px;" align='center' valign='middle' colspan="4">上岗能力确认评价表</td>
        </tr>
    </table>
    <table border="1" cellspacing="0" style="width: 794px;height: 950px" align='center'>
        <tr style="height: 23px;">
            <td style="width: 100px;text-align: center">申请人</td>
            <td style="width: 100px;text-align: center"> ${sqmap.get("sqrX2204140001")}</td>
            <td style="width: 100px; text-align: center">出生年月</td>
            <td style="width: 100px; text-align: center">${sqmap.get("csnyX220414001")}</td>
            <td style="width: 100px; text-align: center">所属部门</td>
            <td style="width: 100px; text-align: center;">${sqmap.get("sqbmX220414001")}</td>
        </tr>
        <tr style="height: 23px;">
            <td style="width: 100px;text-align: center">学历</td>
            <td style="width: 100px;text-align: center">${sqmap.get("xlX22041400001")}</td>
            <td style="width: 100px; text-align: center">所学专业</td>
            <td style="width: 100px; text-align: center">${sqmap.get("sxzyX220414001")}</td>
            <td style="width: 100px; text-align: center">毕业院校</td>
            <td style="width: 100px; text-align: center;">${sqmap.get("byyxX220414001")}</td>
        </tr>
        <tr style="height: 23px;">
            <td style="width: 100px;text-align: center">入职时间</td>
            <td style="width: 100px;text-align: center"> ${sqmap.get("rzsjX220414001")}</td>
            <td style="width: 100px; text-align: center">工作年限</td>
            <td style="width: 100px; text-align: center">${sqmap.get("gznxX220414001")}</td>
            <td style="width: 100px; text-align: center">职务/职称</td>
            <td style="width: 100px; text-align: center;">${sqmap.get("zwzcX220414001")}</td>
        </tr>
        <tr style="height: 13px;">
            <td style="width: 620px;text-align: center"colspan="6">申请上岗项目</td>
        </tr>
        <tr style="height: 33px;">
            <td style="width: 620px;text-align: center"colspan="6">
              <p style="text-align: left">1、抽样领域:${sqmap.get("cylyX220414001")}</p>
              <p style="text-align: left">2、检验能力范围:${sqmap.get("jynlfX22041401")}</p>
              <p style="text-align: left">3、特定仪器设备操作:${sqmap.get("tdyqsX22041401")}</p>
              <p style="text-align: left">4、关键岗位:${sqmap.get("gjgwX220414001")}(${sqmap.get("jdly220424001")})</p>
            </td>
        </tr>
        <tr style="height: 13px;">
            <td style="width: 620px;text-align: center;"colspan="6">主要技术工作经历</td>
        </tr>
        <tr style="height: 83px;">
            <td style="width: 620px;vertical-align: text-top;padding: 10px;"colspan="6">${sqmap.get("zyjsgX22041401")}</td>
        </tr>
        <tr style="height: 113px;">
            <td style="width: 620px;vertical-align: text-top;padding: 10px;"colspan="6">
                <p style="text-align: left">培训内容及能力评价:</p>
                <p>${djlsmaps.get("pxnrjX")}${sqmap.get("nbpxX220414001")}${sqmap.get("wbpxX220414001")}</p>
                <p>带教老师:${djlsmaps.get("djteaimg")}${djlsmaps.get("modifyTime")}</p>
                <p>部门负责人:${bmfzrmap.get("modifyName")}${bmfzrmap.get("modifyTime")}</p>
            </td>
        </tr>
    </table>
    <table border="1" cellspacing="0" style="width: 794px;height: 950px;margin-top: 200px" align='center'>
        <tr style="height: 63px;">
            <td style="width: 620px;vertical-align: text-top;">
                <p style="text-align: left;padding: 10px;">获得专业证书情况:${sqmap.get("zyzsqX22041401")}</p>
            </td>
        </tr>
        <tr style="height: 13px;">
            <td style="width: 620px;text-align: center">考核与评价</td>
        </tr>
        <tr style="height: 13px;">
            <td style="width: 620px;text-align: center">
                考核方式:
                <input type="checkbox" style="margin: 10px 10px;" name="twovalue" value=''
                       <c:if test="${khfsone=='操作考核' || khfstwo=='操作考核'}">checked</c:if>>操作考核
                <input type="checkbox" style="margin: 10px 5px;" name="twovalue" value=''
                       <c:if test="${khfsone=='笔试' || khfstwo=='笔试'}">checked</c:if>>笔试
                <input type="checkbox" style="margin: 10px 5px;" name="twovalue" value=''
                       <c:if test="${khfsone=='审核' || khfstwo=='审核'}">checked</c:if>>审核
                <input type="checkbox" style="margin: 10px 5px;" name="twovalue" value=''
                       <c:if test="${khfsone=='其他' || khfstwo=='其他'}">checked</c:if>>其他
                (${zyjsmap.get("qtsm")}${xgjslymap.get("qtsm")})
            </td>
        </tr>
        <tr style="height: 63px;">
            <td style="width: 620px;vertical-align: text-top;">
                <p style="text-align: left;padding: 10px;">考核纪要及结论:${zyjsmap.get("khjl")}${xgjslymap.get("khjl")}</p>
                <p style="text-align: left">考核人员:${zyjsmap.get("modifyName")}${xgjslymap.get("modifyName")}${xgjslymap.get("sptime")}</p>
            </td>
        </tr>
        <tr style="height: 63px;">
            <td style="width: 620px;vertical-align: text-top;">
                <p style="text-align: left">上岗签发意见:${kyjsbmap.get("spyjX220414002")} ${kyjsbmap.get("spyjX220414005")}</p>
                <p style="text-align: left">签发人:${kyjsbzrmap.get("modifyName")}${kyjsbzrmap.get("modifyTime")}</p>
            </td>
        </tr>
    </table>
    <table border="0" cellspacing="0" style="width: 794px;height: 250px" align='center'>
        <div><p>备注:1、此表试用于抽样、检验检测(从事开发、修改、验证和确认方法)、特定仪器设备操作、检验(含资深检验员)、分析结果(包含符合性声明或意见和解释)、报告审查的人员、
            质量监督员、设备管理员、内审员、化学领域样品保管员、生物安全责任人、金属材料检测领域样品制备人员等上岗能力确认，其中抽样、检验检测(从事开发、修改、验证和确认方法)、
            特定仪器设备操作、检验(含资深检验员)人员需经考核后批准，并提供相关考核记录，其他岗位无需经过考核直接按《人员管理程序》要求进行能力确认。
        </p>
            <p>2、此表双面打印</p>
        </div>
    </table>
    <span>注：该单据手写无效</span>
    <!--endprint1-->
</div>
</body>
</html>

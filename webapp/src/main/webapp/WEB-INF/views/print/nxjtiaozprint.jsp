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
        $("#sealdiv").wordExport('河北省质检研究院工作人员年休假调整审批表')
    }


</script>
<html>
<head>
    <title>河北省质检研究院工作人员年休假调整审批表</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick=preview(1)>
    <input type=button name='button_export' value='导出excel' onclick="exportexcelseal('sealdiv')">
    <input type=button name='button_export' value='导出word' onclick="exportwordseal();">
</div>
<div id="sealdiv" style="width: 756px;height: 1086px;">
<!--startprint1-->
<table style="width: 756px;height: 100px">
    <tr>
        <td style="width: 756px;font-size:30px;height:100px" align='center' valign='middle' colspan="4">河北省质检研究院工作人员年休假调整审批表
        </td>
    </tr>
</table>
<table border="1" cellspacing="0" style="width: 756px;height: 750px">
    <tr style="height: 10%">
        <td style="width: 25%;text-align: center">部门</td>
        <td style="width: 25%;text-align: center">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
            <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                ${da.get("sqbm190802005")}
            </c:forEach>
        </c:if>
        </td>
        <td style="width: 25%; text-align: center">申请人</td>
        <td style="width: 25%; text-align: center">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
            <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                ${da.get("sqr1908020005")}
            </c:forEach>
        </c:if></td>
    </tr>
    <tr style="height: 10%">
        <td style="width: 25%;text-align: center">原计划休假时间</td>
        <td style="width: 75%;text-align: center" colspan="4">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get("yjhks19080202")}至 ${da.get("yjhjs19080202")},共${da.get("yjhts19080202")}天
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 10%">
        <td style="width: 25%;text-align: center">调整后休假时间</td>
        <td style="width: 75%;text-align: center" colspan="4">
          <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    <p>已休${da.get("dzxjt19080202")}天，未休天</p>
                    <p>补休计划：${da.get("dzhks19080202")}起${da.get("dzhjs19080202")}止,共计${da.get("dzxjt19080202")}天</p>
                </c:forEach>
            </c:if>
        </td>
    </tr>

    <tr style="height: 10%">
        <td style="width: 20%;text-align: center">部门负责人意见</td>
        <td style="width: 80%;text-align: center" colspan="4">
            <c:if test="${data.get(1).size()>0 && data.get(1) != null}">
                <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                    <div class="msg-box">
                        <label class="title">${da.get("zgyyijian")}</label>
                        <label class="name">${da.get("zgyname")}</label>
                        <label class="time">${da.get("zgytime")}</label>
                    </div>
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 10%">
        <td style="width: 20%;text-align: center">人力资源部意见</td>
        <td style="width: 80%;text-align: center" colspan="4">
            <c:if test="${data.get(1).size()>0 && data.get(1) != null}">
                <c:forEach items="${data.get(2)}" var="da" varStatus="status">
                    <div class="msg-box">
                        <label class="title">${da.get("zgyyijian")}</label>
                        <label class="name">${da.get("zgyname")}</label>
                        <label class="time">${da.get("zgytime")}</label>
                    </div>
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 10%">
        <td style="width: 20%;text-align: center">主管院长意见</td>
        <td style="width: 80%;text-align: center" colspan="4">
            <c:if test="${data.get(1).size()>0 && data.get(1) != null}">
                <c:forEach items="${data.get(3)}" var="da" varStatus="status">
                    <div class="msg-box">
                        <label class="title">${da.get("zgyyijian")}</label>
                        <label class="name">${da.get("zgyname")}</label>
                        <label class="time">${da.get("zgytime")}</label>
                    </div>
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 10%">
        <td style="width: 20%;text-align: center">院长意见</td>
        <td style="width: 80%;text-align: center" colspan="4">
            <c:if test="${data.get(1).size()>0 && data.get(1) != null}">
                <c:forEach items="${data.get(4)}" var="da" varStatus="status">
                    <div class="msg-box">
                        <label class="title">${da.get("zgyyijian")}</label>
                        <label class="name">${da.get("zgyname")}</label>
                        <label class="time">${da.get("zgytime")}</label>
                    </div>
                </c:forEach>
            </c:if>
        </td>
    </tr>
</table>
    <p>注： 1、申请表批准后交人力资源部备案。休假结束后，返回工作岗位当天应及时到人力资源部办理销假手续。</p>
    <p>    2、部门负责人调整休假时间，逐级审批至院长；副部长(含)以下人员调整休假时间，逐级审批至主管副院长</p>
<!--endprint1-->
</div>
</body>
</html>

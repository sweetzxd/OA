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
<style>
    .msg-box{
        padding: 10px;
    }
    .msg-box .title{
        text-align: left;
        width: 50% !important;
        display:inline-block;
        word-wrap: break-word;
    }
    .msg-box .name{
        width: 13%!important;
        display:inline-block;
    }
    .msg-box .time{
        width: 35%!important;
        display:inline-block;
    }
</style>
<jsp:include page="/common/js.jsp"></jsp:include>
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>

<script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.js"></script>
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
    function exportexcelopinioin(tableid) {
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

    function exportwordopinion() {
        $("#opiondiv").wordExport('工作请示')
    }

</script>
<html>
<head>
    <title>工作请示打印页</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick="preview(1)">
    <input type=button name='button_export' value='导出excel' onclick="exportexcelopinioin('opiondiv')">
    <input type=button name='button_export' value='导出word' onclick="exportwordopinion()">
</div>
<!--startprint1-->
<div id="opiondiv" style="width: 756px;height: 1000px;">
    <div style="text-align: center"></div>
    <div style="text-align: right"></div>
    <table style="width: 756px;height: 150px">
        <tr>
            <td style="width: 756px;font-size:30px;height:100px" align='center' valign='middle' colspan="2">工作请示
            </td>
        </tr>
        <tr>
            <td style="width: 756px;font-size:20px;height:100px;padding-left: 20px">河北省产品质量监督检验研究院
            </td>
            <td style="width:756px;font-size:20px;height:100px;" align='right'>
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get("yztime")}
                </c:forEach>
            </c:if>
            </td>
        </tr>
    </table>
    <table border="1" cellspacing="0" style="width: 756px;height:850px">
        <tr style="height: 425px">
            <td style="width: 150px;text-align: center">
                领导批示
            </td>
            <td style="width: 600px;text-align: center">
                <c:if test="${data.size()>1 && data.get(2) != null && data.get(2).size()>0}">
                    <c:forEach items="${data.get(2)}" var="da" varStatus="status">
                        <div class="msg-box">
                            <label class="title">${da.get("bgsyijian")}</label>
                            <label class="name">${da.get("bgsyname")}</label>
                            <label class="time">${da.get("bgstime")}</label>
                        </div>
                    </c:forEach>
                </c:if>
                <c:if test="${data.size()>1 && data.get(1).size()>0 && data.get(1) != null}">
                    <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                        <div class="msg-box">
                            <label class="title">${da.get("zgyyijian")}</label>
                            <label class="name">${da.get("zgyname")}</label>
                            <label class="time">${da.get("zgytime")}</label>
                        </div>
                    </c:forEach>
            </c:if>
                <c:if test="${data.size()>1 && data.get(3).size()>0 && data.get(3) != null}">
                    <c:forEach items="${data.get(3)}" var="da" varStatus="status">
                        <div class="msg-box">
                            <label class="title">${da.get("yzyijian")}</label>
                            <label class="name">${da.get("yzname")}</label>
                            <label class="time">${da.get("yztime")}</label>
                        </div>
                    </c:forEach>
                </c:if>
            </td>
        </tr>
        <tr style="height: 425px">
            <td style="width: 150px;text-align: center">
                请示内容
            </td>
            <td style="width: 600px;text-align: center">
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get("content")}
                </c:forEach>
                </c:if>
            </td>
        </tr>

    </table>
    <!--endprint1-->
</div>
</body>
</html>

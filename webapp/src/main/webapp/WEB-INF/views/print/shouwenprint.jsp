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
<jsp:include page="/common/js.jsp"></jsp:include>

<%--导出word引入的js--%>
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
    function exportexcelsw(tableid) {
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
    function exportwordsw() {
        $("#swdiv").wordExport('收文承办单')
    }

</script>
<html>
<head>
    <title>打印收文页</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick=preview(1)>
    <input type=button name='button_export' value='导出excel' onclick="exportexcelsw('swdiv');">
    <input type=button name='button_export' value='导出word' onclick="exportwordsw();">
</div>
<div id="swdiv" style="width: 756px;height: 1086px;">
<!--startprint1-->
<table style="width: 756px;height: 150px">
    <tr>
        <td style="width: 756px;font-size:30px;height:100px;" align='center' valign='middle' colspan="4">河北省产品质量监督检验研究院收文承办单
        </td>
    </tr>
    <tr>
        <td style="width: 450px; height:50px;" align='left'>院收文号：</td>
        <td></td>
        <td style="width: 200px; height:50px;" align='right'>日期：</td>
        <td style="width: 100px; height:50px;" align='right'>${data[0].get('time')}</td>
    </tr>
</table>
<table border="1" cellspacing="0" style="width: 756px;height:1086px;">
    <tr style="height: 80px">
        <td style="width: 100px;text-align: center">来文单位</td>
        <td style="width: 250px;text-align: center">${data.get(0).get("lwdept")}</td>
        <td style="width: 150px; text-align: center">文号</td>
        <td style="width: 150px; text-align: center">${data.get(0).get("wenhao")}</td>
        <td style="width: 50px; text-align: center">份数</td>
        <td style="width: 50px; text-align: center">${data.get(0).get("number")}</td>
    </tr>
    <tr style="height: 80px">
        <td style="width: 100px;text-align: center">来文内容</td>
        <td style="width: 656px;text-align: center" colspan="5">${data.get(0).get("title")}</td>
    </tr>
    <tr style="height:180px">
        <td style="width: 100px;text-align: center">办公室拟办意见</td>
        <td style="width: 656px;text-align: center" colspan="5">
            <c:if test="${data.size()>1 && data.get(1) != null}">
                <div class="msg-box">
                    <label class="title">${data.get(1).get("bgssyj")}</label>
                    <label class="name">${data.get(1).get("bgsname")}</label>
                    <label class="time">${data.get(1).get("bgstime")}</label>
                </div>
            </c:if>
        </td>
    </tr>
    <tr style="height: 320px">
        <td style="width: 100px;text-align: center">领导批示</td>
        <td style="width: 656px;text-align: center" colspan="5">
            <c:forEach items="${data}" var="da" varStatus="status">
                <c:if test="${fn:contains(da,'spyj')}">
                   <div class="msg-box">
                       <label class="title">${da.spyj}</label>
                       <label class="name">${da.zgyname}</label>
                       <label class="time">${da.zgytime}</label>
                   </div>
                </c:if>
          </c:forEach>
        </td>
        <td style="width: 656px;text-align: center" colspan="5">
            <c:forEach items="${data}" var="da" varStatus="status">
                <c:if test="${fn:contains(da,'yzspyj')}">
                    <div class="msg-box">
                        <label class="title">${da.yzspyj}</label>
                        <label class="name">${da.yzname}</label>
                        <label class="time">${da.yztime}</label>
                    </div>
                </c:if>
            </c:forEach>
        </td>
    </tr>
    <tr style="height:150px">
        <td style="width: 100px;text-align: center">办理情况和结果</td>
        <td style="width: 656px;text-align: center" colspan="5">
            <c:forEach items="${data}" var="dafc" varStatus="status">
                <c:if test="${fn:contains(dafc,'wfbyj')}">
                    <div class="msg-box">
                        <span class="title">${dafc.wfbyj}</span>
                        <span class="name">${dafc.fbname}</span>
                        <span class="time">${dafc.fbtime}</span>
                    </div><br>
                </c:if>
                <c:if test="${fn:contains(dafc,'wcbyj')}">
                    <div class="msg-box">
                        <span class="title">${dafc.wcbyj}</span>
                        <span class="name">${dafc.cbname}</span>
                        <span class="time">${dafc.cbtime}</span>
                    </div><br>
                </c:if>
            </c:forEach>
           <%-- ${fbyj}${fbname}${fbtime}${cbyj}${cbname}${cbtime}--%>
        </td>
    </tr>
    <tr style="height: 50px">
        <td style="width: 100px;text-align: center">备注</td>
        <td style="width: 656px;text-align: center" colspan="5"></td>
    </tr>
    <tr style="height: 50px">
        <td style="width: 656px;text-align: center" colspan="6"></td>
    </tr>
</table>
<!--endprint1-->
</div>
</body>
</html>

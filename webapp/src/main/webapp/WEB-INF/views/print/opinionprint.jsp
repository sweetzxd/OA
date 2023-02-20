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
<style>
    .msg-box{
        float: left;
        width: 50%;
        height: 80px;
    }
    .name-box{
        float: left;
        width: 20%;
        height: 75px;
        margin-top: 25px;
    }
    .date-box{
        float: left;
        height: 80px;
        margin-left: 20px;
        padding: 10px;
        line-height: 100px;
    }
</style>
<jsp:include page="/common/js.jsp"></jsp:include>
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>
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
    function exportexcelopinioin(tableid) {
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
    function exportwordopinion() {
            $("#opiondiv").wordExport('建议表')
    }

    //当前日期
    function newDate() {
        var time = new Date();
        var year = time.getFullYear();
        var month = time.getMonth();
        var date = time.getDate();
        var minutes = time.getMinutes();
        //month<10?month='0'+month:month;
        month = month + 1;
        var now_date = year + '年' + month + '月' + date+"日";
        document.getElementById('newdate').innerHTML = now_date;
    }

</script>
<html>
<head>
    <title>征求意见打印页</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick="preview(1)">
    <input type=button name='button_export' value='导出excel' onclick="exportexcelopinioin('opiondiv')">
    <input type=button name='button_export' value='导出word' onclick="exportwordopinion()">
</div>
<!--startprint1-->
<div id="opiondiv" style="width: 756px;height: 1086px;">
<table style="width: 756px;height: 1px">
    <tr>
        <td style="width: 756px;" align='center' valign='middle' colspan="4">
            <c:if test="${type}">
                <div style="font-size:25px;padding: 10px;">河北省产品质量监督检验研究院征求意见表</div>
                <div style="font-size:15px;text-align: right" id="newdate"></div>
            </c:if>
            <c:if test="${!type}">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <label style="font-size:25px;">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get("title")}
                </c:forEach>
            </c:if>
            建议表</label>
            </c:if>
        </td>
    </tr>
</table>
<table border="1" cellspacing="0" style="width: 756px;height: 886px">
    <tr style="height: 120px">
        <td style="width: 756px;text-align: center" colspan="2">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get("yjdesc")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 50px">
        <td style="width: 100px;text-align: center">建议人</td>
        <td style="width: 656px;text-align: center">意见或建议内容</td>
    </tr>
    <c:if test="${type}">
        <tr style="height:100px">
            <td style="width: 100px;text-align: center">
                <label>董占伟</label>
            </td>
            <td style="width: 656px;text-align: center">
                <c:if test="${datamsg.get('dongzhanwei')!=null}">
                <div class="msg-box">${datamsg.get('dongzhanwei').get("otheryj")}</div>
                <div class="name-box">${datamsg.get('dongzhanwei').get("othernamei")}</div>
                <div class="date-box">${datamsg.get('dongzhanwei').get("othertime")}</div>
                </c:if>
            </td>
        </tr>
        <tr style="height:100px">
            <td style="width: 100px;text-align: center">
                <label>龙冬梅</label>
            </td>
            <td style="width: 656px;text-align: center">
                <c:if test="${datamsg.get('longdongmei')!=null}">
                <div class="msg-box">${datamsg.get('longdongmei').get("otheryj")}</div>
                <div class="name-box">${datamsg.get('longdongmei').get("othernamei")}</div>
                <div class="date-box">${datamsg.get('longdongmei').get("othertime")}</div>
                </c:if>
            </td>
        </tr>
        <tr style="height:100px">
            <td style="width: 100px;text-align: center">
                <label>田旭</label>
            </td>
            <td style="width: 656px;text-align: center">
                <c:if test="${datamsg.get('tianxu')!=null}">
                <div class="msg-box">${datamsg.get('tianxu').get("otheryj")}</div>
                <div class="name-box">${datamsg.get('tianxu').get("othernamei")}</div>
                <div class="date-box">${datamsg.get('tianxu').get("othertime")}</div>
                </c:if>
            </td>
        </tr>
        <tr style="height:100px">
            <td style="width: 100px;text-align: center">
                <label>张远恒</label>
            </td>
            <td style="width: 656px;text-align: center">
                <c:if test="${datamsg.get('zhangyuanheng')!=null}">
                <div class="msg-box">${datamsg.get('zhangyuanheng').get("otheryj")}</div>
                <div class="name-box">${datamsg.get('zhangyuanheng').get("othernamei")}</div>
                <div class="date-box">${datamsg.get('zhangyuanheng').get("othertime")}</div>
                </c:if>
            </td>
        </tr>
        <tr style="height:100px">
            <td style="width: 100px;text-align: center">
                <label>罗强</label>
            </td>
            <td style="width: 656px;text-align: center">
                <c:if test="${datamsg.get('luoqiang')!=null}">
                <div class="msg-box">${datamsg.get('luoqiang').get("otheryj")}</div>
                <div class="name-box">${datamsg.get('luoqiang').get("othernamei")}</div>
                <div class="date-box">${datamsg.get('luoqiang').get("othertime")}</div>
                </c:if>
            </td>
        </tr>
        <tr style="height:100px">
            <td style="width: 100px;text-align: center">
                <label>戴志强</label>
            </td>
            <td style="width: 656px;text-align: center">
                <c:if test="${datamsg.get('daizhiqiang')!=null}">
                <div class="msg-box">${datamsg.get('daizhiqiang').get("otheryj")}</div>
                <div class="name-box">${datamsg.get('daizhiqiang').get("othernamei")}</div>
                <div class="date-box">${datamsg.get('daizhiqiang').get("othertime")}</div>
                </c:if>
            </td>
        </tr>
        <tr style="height:100px">
            <td style="width: 100px;text-align: center">
                <label>候都兴</label>
            </td>
            <td style="width: 656px;text-align: center">
                <c:if test="${datamsg.get('houduxing')!=null}">
                <div class="msg-box">${datamsg.get('houduxing').get("otheryj")}</div>
                <div class="name-box">${datamsg.get('houduxing').get("othernamei")}</div>
                <div class="date-box">${datamsg.get('houduxing').get("othertime")}</div>
                </c:if>
            </td>
        </tr>
        <tr style="height:100px">
            <td style="width: 100px;text-align: center">
                <label>李欣</label>
            </td>
            <td style="width: 656px;text-align: center">
                <c:if test="${datamsg.get('lixin')!=null}">
                <div class="msg-box">${datamsg.get('lixin').get("otheryj")}</div>
                <div class="name-box">${datamsg.get('lixin').get("othernamei")}</div>
                <div class="date-box">${datamsg.get('lixin').get("othertime")}</div>
                </c:if>
            </td>
        </tr>
    </c:if>
    <c:if test="${!type}">
    <tr style="height:600px">
        <td style="width: 100px;text-align: center">
            <c:if test="${data.size() >1 && data.get(1).size()>0}">
                <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                    <div class="msg-box">
                        <label class="name" style="width: 100% !important;"> ${da.get("othername")}</label>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${data.size() >2 && data.get(2).size()>0}">
                <c:forEach items="${data.get(2)}" var="da" varStatus="status">
                    <div class="msg-box">
                        <label class="name" style="width: 100% !important;">${da.get("fwname")}</label>
                    </div>
                </c:forEach>
            </c:if>
            <%--<c:if test="${data.size() >3 && data.get(3).size()>0}">
                <c:forEach items="${data.get(3)}" var="da" varStatus="status">
                 <div class="msg-box">
                    <label class="name" style="width: 100% !important;">${da.get("gdname")}</label>
                 </div>
                </c:forEach>
            </c:if>--%>
       </td>
        <td style="width: 656px;text-align: left">
            <c:if test="${data.size() >1 && data.get(1).size()>0}">
                <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                    <div class="msg-box">${da.get("otheryj")}</div>
                    <div class="name-box">${da.get('othernamei')}</div>
                    <div class="date-box">${da.get("othertime")}</div>
                </c:forEach>
            </c:if>
            <c:if test="${data.size() >2 && data.get(2).size()>0}">
                <c:forEach items="${data.get(2)}" var="da" varStatus="status">
                    <div class="msg-box">${da.get("yijian")}</div>
                    <div class="name-box">${da.get('fwnamei')}</div>
                    <div class="date-box">${da.get("ftime")}</div>
                </c:forEach>
            </c:if>
            <%--<c:if test="${data.size() >3 && data.get(3).size()>0}">
                <c:forEach items="${data.get(3)}" var="da" varStatus="status">

                    <div class="msg-box">${da.get("gdyj")}</div>
                    <div class="name-box">${da.get('gdnamei')}</div>
                    <div class="date-box">${da.get("ztime")}</div>
                </c:forEach>
            </c:if>--%>
        </td>
    </tr>
    </c:if>

</table>
<!--endprint1-->
</div>
<script>
    $(document).ready(function(){
        newDate();
    });
</script>
</body>
</html>

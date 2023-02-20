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
    <title>外部培训审批单</title>
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
<div id="sealdiv" style="width: 756px;height: 986px;">
<!--startprint1-->
<table style="width: 756px;height: 30px">
    <tr>
        <td style="width: 756px;font-size:16px;height:10px;" align='left' valign='middle' colspan="4">ZJY-GS-602Bi-2019/0</td>
    </tr>
    <tr>
        <td style="width: 756px;font-size:25px;height:20px;" align='center' valign='middle' colspan="4">外部培训审批单</td>
    </tr>
</table>
<table border="1" cellspacing="0" style="width: 794px;height: 950px">
    <tr style="height: 83px;">
        <td style="width: 100px;text-align: center">部门</td>
        <td style="width: 100px;text-align: center">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
            <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                <s:dic type="dept" value="${da.get('department')}"></s:dic>
            </c:forEach>
        </c:if>
        </td>
        <td style="width: 100px; text-align: center">姓名</td>
        <td style="width: 100px; text-align: center">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
            <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                ${da.get("NAME")}
            </c:forEach>
        </c:if></td>
        <td style="width: 100px; text-align: center">起止时间</td>
        <td style="width: 260px; text-align: center;" colspan="3">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get("starttime")}至<br>${da.get("finishtime")}
                </c:forEach>
            </c:if></td>
    </tr>
    <tr style="height: 63px;">
        <td style="width: 100px;text-align: center">组织单位</td>
        <td style="width: 620px;text-align: center"colspan="7">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                   ${da.get("dw")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 63px;">
        <td style="width: 100px;text-align: center">文件及名称</td>
        <td style="width: 620px;text-align: center"colspan="7">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(7)}" var="da" varStatus="status">
                    ${da.get("wj")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height:63px"aria-rowspan="2">
        <td style="width: 100px;text-align: center">培训内容</td>
        <td style="width: 620px;text-align: center"colspan="7">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get("pxnr")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 63px" >
        <td rowspan="2" style="width:100px;text-align: center">费用</td>
        <td style="width: 100px;text-align: center">培训费</td>
        <td style="width: 175px;text-align: center" colspan="2">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                  ${da.get("pxf")}
                </c:forEach>
            </c:if>
        </td>
        <td style="width: 100px; text-align: center">往返车（机）票</td>
        <td style="width: 265px; text-align: center" colspan="3">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                   ${da.get("wfcjp")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 63px">
        <td style="width: 100px; text-align: center">出差补助</td>
        <td style="width: 175px; text-align: center" colspan="2">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                 ${da.get("ccbz")}
                </c:forEach>
            </c:if></td>
        <td style="width: 100px; text-align: center">合计</td>
        <td style="width: 265px; text-align: center" colspan="3">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                   ${da.get("hj")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 63px"aria-rowspan="2">
        <td style="width: 100px;text-align: center">部门意见</td>
        <td style="width: 470px;text-align: center;border-right: 0"colspan="6">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                    ${da.get("bmyj")}
                    ${da.get("NAME")}
                </c:forEach>
            </c:if>
        </td>
        <%--<td style="width: 470px;text-align: center;border-right: 0"colspan="3">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                    ${da.get("NAME")}
                </c:forEach>
            </c:if>
        </td>--%>
        <td style="width: 150px;text-align: center;padding-top: 5%;border-left: 0">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                    ${da.get("time")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 63px"aria-rowspan="2">
        <%-- <td style="width: 100px;text-align: center">人力资源部审核意见</td>--%>

<c:if test="${fn:contains(data.get(0).get(0).get('starttime'),'2022')&&data.get(0).get(0).get('pxlx')!='专业技术能力提升培训'&&data.get(0).get(0).get('pxlx')!='综合培训'}">
            <td style="width: 100px;text-align: center">科研技术部(技术委员会)</td>
        </c:if>
        <c:if test="${fn:contains(data.get(0).get(0).get('starttime'),'2022')&& data.get(0).get(0).get('pxlx')=='专业技术能力提升培训'}">
            <td style="width: 100px;text-align: center">科研技术部(技术委员会)</td>
        </c:if>
        <c:if test="${fn:contains(data.get(0).get(0).get('starttime'),'2022')&& data.get(0).get(0).get('pxlx')=='综合培训'}">
            <td style="width: 100px;text-align: center">人力资源部</td>
        </c:if>


        <td style="width: 470px;text-align: center;border-right: 0"colspan="6">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(2)}" var="da" varStatus="status">
                   ${da.get("rlzy")}
                    ${da.get("NAME")}
                </c:forEach>
            </c:if>
        </td>
        <%--<td style="width: 470px;text-align: center;border-right: 0"colspan="6">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(2)}" var="da" varStatus="status">
                    ${da.get("NAME")}

                </c:forEach>
            </c:if>
        </td>--%>
        <td style="width: 150px;text-align: center;padding-top: 5%;border-left: 0">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                    ${da.get("time")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 83px"aria-rowspan="2">
        <td style="width: 100px;text-align: center">主管院领导意见</td>
        <td style="width: 470px;text-align: center;border-right: 0"colspan="6">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(4)}" var="da" varStatus="status">
                    ${da.get("zgy")}
                    ${da.get("NAME")}
                </c:forEach>
            </c:if>
        </td>
        <%--<td style="width: 470px;text-align: center;border-right: 0"colspan="6">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(4)}" var="da" varStatus="status">
                    ${da.get("NAME")}
                </c:forEach>
            </c:if>
        </td>--%>
        <td style="width: 150px;text-align: center;padding-top: 5%;border-left: 0">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                    ${da.get("time")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 83px"aria-rowspan="2">
        <td style="width: 100px;text-align: center">分管院领导意见</td>
        <td style="width: 470px;text-align: center;border-right: 0"colspan="6">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(3)}" var="da" varStatus="status">
                  ${da.get("fgy")}
                    ${da.get("NAME")}
                </c:forEach>
            </c:if>
        </td>
        <%--<td style="width: 470px;text-align: center;border-right: 0"colspan="6">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(3)}" var="da" varStatus="status">
                    ${da.get("NAME")}
                </c:forEach>
            </c:if>
        </td>--%>
        <td style="width: 150px;text-align: center;padding-top: 5%;border-left: 0">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                    ${da.get("time")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 83px">
        <td style="width: 100px;text-align: center">实际费用</td>
        <td style="width: 620px;text-align: center"colspan="7">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                   ${da.get("sjfy")}圓
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 63px"aria-rowspan="2">
        <td style="width: 100px;text-align: center">培训总结</td>
        <td style="width: 620px;text-align: center"colspan="7">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(6)}" var="da" varStatus="status">
                  ${da.get("zj")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 43px">
        <td style="width: 100px;text-align: center">证书编号</td>
        <td style="width: 620px;text-align: center"colspan="7">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                  ${da.get("zsbh")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
</table>
<span>注：该单据手写无效</span>
<!--endprint1-->
</div>
</body>
</html>

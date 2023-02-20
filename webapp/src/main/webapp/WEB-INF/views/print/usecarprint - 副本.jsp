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
<jsp:include page="/common/js.jsp"></jsp:include>
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
        $("#opiondiv").wordExport('河北省产品质量监督检验研究院业务车辆派车单')
    }

</script>
<html>
<head>
    <title>河北省产品质量监督检验研究院业务车辆派车单</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick="preview(1)">
    <c:if test="${sessionScope.loginer.id == 'admin'}">
    <input type=button name='button_export' value='导出excel' onclick="exportexcelopinioin('opiondiv')">
    <input type=button name='button_export' value='导出word' onclick="exportwordopinion()">
    </c:if>
</div>
<!--startprint1-->
<div id="opiondiv">
    <div style="text-align: center"></div>
    <div style="text-align: right"></div>
    <table style="width: 100%;height: 20%">
        <tr>
            <td style="width: 100%;font-size:30px;height:2%" align='center' valign='middle' colspan="3">河北省产品质量监督检验研究院业务车辆派车单
            </td>
        </tr>
        <tr>
            <td style="width: 15%;font-size:20px;height:5% ;padding-left: 20px">派车人：
            </td>
            <td style="width: 35%;font-size:20px;height:5%  ;" align='left'>
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("carpeople")}
                    </c:forEach>
                </c:if>
            </td>
            <td style="width: 50%;font-size:20px;height:5%;" align='left'>
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("datytime")}
                    </c:forEach>
                </c:if>
            </td>
        </tr>
    </table>
    <table border="1" cellspacing="0" style="width: 100%;height: 70%">
        <tr style="height: 10%">
            <td style="width: 20%;text-align: center">车号</td>
            <td style="width: 20%;text-align: center" >
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("number")}
                    </c:forEach>
                </c:if>
            </td>
            <td style="width: 20%; text-align: center">驾驶人姓名</td>
            <td style="width: 20%; text-align: center" >
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("people")}
                    </c:forEach>
                </c:if>
            </td>
            <td style="width: 20%; text-align: center">备注</td>
        </tr>
        <tr style="height: 10%">
            <td style="width: 20%;text-align: center">用车部门</td>
            <td style="width: 20%;text-align: center" >
                <c:if test="${data.get(1).size()>0 && data.get(1) != null}">
                    <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                        ${da.get("dept")}
                    </c:forEach>
                </c:if>
            </td>
            <td style="width: 20%; text-align: center">联系用车人</td>
            <td style="width: 20%; text-align: center" >
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("people")}
                    </c:forEach>
                </c:if>
            </td>
            <td style="width: 20%; text-align: center" rowspan="6">
                    <p>1、业务用车由办公室统一调派，填写派车单。返回时凭派车单报销费用</p>
                    <p>2、业务车辆未经允许不准出车，派车单内容填写不全者，不予确认。</p>
            </td>
        </tr>
        <tr style="height: 10%">
            <td style="width: 20%;text-align: center">出发时间</td>
            <td style="width: 20%;text-align: center" >
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("begintime")}
                    </c:forEach>
                </c:if>
            </td>
            <td style="width: 20%; text-align: center">用车天数</td>
            <td style="width: 20%; text-align: center" >
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("days")}
                    </c:forEach>
                </c:if>
            </td>
        </tr>
        <tr style="height: 10%">
            <td style="width: 20%;text-align: center">抽样县市</td>
            <td style="width: 20%;text-align: center" >
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("address")}
                    </c:forEach>
                </c:if>
            </td>
            <td style="width: 20%; text-align: center">事由</td>
            <td style="width: 20%; text-align: center" >
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("descsm")}
                    </c:forEach>
                </c:if>
            </td>
        </tr>
        <tr style="height: 10%">
            <td style="width: 20%;text-align: center">起止里程表数</td>
            <td style="width: 20%;text-align: center" ></td>
            <td style="width: 20%; text-align: center">行驶公里</td>
            <td style="width: 20%; text-align: center" ></td>
        </tr>
        <tr style="height: 10%">
            <td style="width: 20%;text-align: center">返回时间</td>
            <td style="width: 20%;text-align: center" >
            </td>
            <td style="width: 20%; text-align: center">带车人签字</td>
            <td style="width: 20%; text-align: center" >
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("people")}
                    </c:forEach>
                </c:if>
            </td>
        </tr>
        <tr style="height: 10%">
            <td style="width: 20%;text-align: center">用油升数</td>
            <td style="width: 20%;text-align: center" ></td>
            <td style="width: 20%; text-align: center">百公里油耗</td>
            <td style="width: 20%; text-align: center" ></td>
        </tr>
    </table>
    <!--endprint1-->
</div>
</body>
</html>

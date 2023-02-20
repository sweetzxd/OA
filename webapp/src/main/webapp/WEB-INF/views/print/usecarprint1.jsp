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
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" type="text/css" href="/zjyoa-pc/resources/css/print.css"/>
<jsp:include page="/common/js.jsp"></jsp:include>
<script src="/zjyoa-pc/resources/js/system/system.js" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
<%
    String decodeUrl = request.getParameter("url");
    if (decodeUrl != null) {
        String url = new String(Base64Utils.decode(decodeUrl.getBytes()));
        request.setAttribute("url", url);
    }
%>
<script src="/zjyoa-pc/resources/js/system/system.js" charset="utf-8"></script>

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
  <%--  <input type=button name='button_export' value='导出excel' onclick="exportexcelopinioin('opiondiv')">
    <input type=button name='button_export' value='导出word' onclick="exportwordopinion()">--%>
</div>
    <c:if test="${type=='1'}">
        <!--startprint1-->
        <div id="opiondiv" style="width:794px;height: 1090px">
            <div  style="width:794px;height: 45%">
                <table style="width: 100%;height: 100px">
                    <tr>
                        <td style="width: 100%;font-size:30px;height:70px" align='center' valign='middle' colspan="3">河北省产品质量监督检验研究院业务车辆申请单
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 30%;font-size:15px;height:30px;" align='left'>
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                             申请人：&nbsp;${data.get(0).get(0).get("sqr1911180001")}
                            </c:if>
                        </td>
                        <td style="width: 30%;font-size:15px;height:30px;" align='center'>
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                部门：&nbsp;${data.get(0).get(0).get("sqbm191118001")}
                            </c:if>
                        </td>
                        <td style="width: 40%;font-size:15px;height:30px;" align='right'>
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                时间：&nbsp;${data.get(0).get(0).get("recordTime")}
                            </c:if>
                        </td>
                    </tr>
                </table>
                <table border="1" cellspacing="0" style="width: 100%;height: 350px;font-size:15px;" >
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center">用车路线</td>
                        <td style="width: 27%;text-align: center;" colspan="2">
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                               ${data.get(0).get(0).get("yclx191118002")}
                            </c:if>
                        </td>
                        <td style="width: 15%; text-align: center">用车类型</td>
                        <td style="width: 18%; text-align: center" >
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("yclx191118001")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center">用车事由</td>
                        <td style="width: 27%;text-align: center" colspan="2" >
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("ycsy191118001")}
                            </c:if>
                        </td>
                        <td style="width: 15%; text-align: center">目的地</td>
                        <td style="width: 18%; text-align: center" >
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("mdd1911180001")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center">预计起止时间</td>
                        <td style="width: 27%;text-align: center" colspan="2" >
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("kssj191118001")}
                            </c:if>
                            ——
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("jssj191118001")}
                            </c:if>
                        </td>
                        <td style="width: 15%; text-align: center">预计天数</td>
                        <td style="width: 18%; text-align: center" >
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("yjts191118001")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center">备注</td>
                        <td style="width: 27%;text-align: center" colspan="2">
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("bz19111900001")}
                            </c:if>
                        </td>
                        <td style="width: 15%; text-align: center">申请车型</td>
                        <td style="width: 18%; text-align: center" >
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("sqcx191118001")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center" rowspan="2">审批意见</td>
                        <td style="width: 27%;text-align: center">
                            <c:if test="${data.get(1).size()>0 && data.get(1) != null}">
                                ${data.get(1).get(0).get("spyj")}
                                ${data.get(1).get(0).get("spsm")}
                                ${data.get(1).get(0).get("recordName")}
                                <br/>
                                ${data.get(1).get(0).get("recordTime")}
                            </c:if>
                        </td>
                        <td style="width: 27%;text-align: center">
                            <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                                ${data.get(2).get(0).get("spyj191115003")}
                                ${data.get(2).get(0).get("spsm191115003")}
                                ${data.get(2).get(0).get("recordName")}
                                <br/>
                                ${data.get(2).get(0).get("recordTime")}
                            </c:if>
                        </td>
                        <td style="width: 15%;text-align: center">派车数量</td>
                        <td style="width: 18%;text-align: center">
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("pcsl191118001")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 27%;text-align: center">部门主任</td>
                        <td style="width: 27%;text-align: center">车辆管理员</td>
                        <td style="width: 15%;text-align: center">同乘人数</td>
                        <td style="width: 18%;text-align: center">
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("tcrs191118001")}
                            </c:if>
                        </td>
                    </tr>
                </table>
            </div>
            <div  style="width:794px;height: 55%">
                <table style="width: 100%;height: 100px">
                    <tr>
                        <td style="width: 100%;font-size:30px;height:80px" align='center' valign='middle' colspan="3">河北省产品质量监督检验研究院业务车辆派车单
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 30%;font-size:15px;height:20px;" align='left'>
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                申请人：&nbsp;${data.get(0).get(0).get("sqr1911180001")}
                            </c:if>
                        </td>
                        <td style="width: 30%;font-size:15px;height:20px;" align='center'>
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                部门：&nbsp;${data.get(0).get(0).get("sqbm191118001")}
                            </c:if>
                        </td>
                        <td style="width: 40%;font-size:15px;height:20px;" align='right'>
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                时间：&nbsp;${data.get(0).get(0).get("recordTime")}
                            </c:if>
                        </td>
                    </tr>
                </table>
                <table border="1" cellspacing="0" style="width: 100%;height: 450px;font-size:15px;" >
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center">车型/车牌号</td>
                        <td style="width: 27%;text-align: center;" colspan="2">
                            <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                                ${data.get(2).get(0).get("sqcx191115003")}
                            </c:if>/
                            <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                                ${data.get(2).get(0).get("cph1911180002")}
                            </c:if>
                            <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                                ${data.get(2).get(0).get("cph1911150001")}
                            </c:if>
                        </td>
                        <td style="width: 15%; text-align: center">车辆归属</td>
                        <td style="width: 18%; text-align: left" >
                            <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                                ${data.get(2).get(0).get("clgs191115001")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center">用车路线</td>
                        <td style="width: 27%;text-align: center" colspan="2" >
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("yclx191118002")}
                            </c:if>
                        </td>
                        <td style="width: 15%; text-align: center">司机姓名</td>
                        <td style="width: 18%; text-align: left" >
                            <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                                ${data.get(2).get(0).get("sjxm191115001")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center">用车事由</td>
                        <td style="width: 27%;text-align: center" colspan="2" >
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("ycsy191118001")}
                            </c:if>
                        </td>
                        <td style="width: 15%; text-align: center">同乘人数</td>
                        <td style="width: 18%; text-align: center" >
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("tcrs191118001")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center">实际起止时间</td>
                        <td style="width: 27%;text-align: center" colspan="2">
                            <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                ${data.get(3).get(0).get("kssj191115003")}
                            </c:if>
                            ——
                            <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                ${data.get(3).get(0).get("jssj191115003")}
                            </c:if>
                        </td>
                        <td style="width: 15%; text-align: center">实际用车天数</td>
                        <td style="width: 18%; text-align: center" >
                            <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                ${data.get(3).get(0).get("sjyct19111501")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center">起止公里数</td>
                        <td style="width: 27%;text-align: center" colspan="2">
                            <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                ${data.get(3).get(0).get("ksgls19111501")}
                            </c:if>
                            ——
                            <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                ${data.get(3).get(0).get("jsgls19111501")}
                            </c:if>
                        </td>
                        <td style="width: 15%; text-align: center">行驶公里数</td>
                        <td style="width: 18%; text-align: center" >
                            <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                ${data.get(3).get(0).get("xsgls19111501")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center">备注</td>
                        <td style="width: 27%;text-align: center" colspan="2">
                            <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                ${data.get(3).get(0).get("bz19111500003")}
                            </c:if>
                        </td>
                        <td style="width: 15%; text-align: center">目的地</td>
                        <td style="width: 18%; text-align: center" >
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("mdd1911180001")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 100px">
                        <td style="width: 15%;text-align: center" rowspan="2">审批意见</td>
                        <td style="width: 27%;text-align: center">
                            <c:if test="${data.get(1).size()>0 && data.get(1) != null}">
                                ${data.get(1).get(0).get("spyj")}
                                ${data.get(1).get(0).get("spsm")}
                                ${data.get(1).get(0).get("recordName")}
                                <br/>
                                ${data.get(1).get(0).get("recordTime")}
                            </c:if>
                        </td>
                        <td style="width: 27%;text-align: center">
                            <c:if test="${data.get(4).size()>0 && data.get(4) != null}">
                                ${data.get(4).get(0).get("spyj191115004")}
                                ${data.get(4).get(0).get("spsm191115004")}
                                ${data.get(4).get(0).get("recordName")}
                                <br/>
                                ${data.get(4).get(0).get("recordTime")}
                            </c:if>
                        </td>
                        <td style="width: 15%;text-align: center">加油升数</td>
                        <td style="width: 18%;text-align: center">
                            <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                ${data.get(3).get(0).get("jyss191115001")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 27%;text-align: center">部门主任</td>
                        <td style="width: 27%;text-align: center">车辆管理员</td>
                        <td style="width: 15%;text-align: center">油耗</td>
                        <td style="width: 18%;text-align: center">
                            <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                ${data.get(3).get(0).get("yh19111500001")}
                            </c:if>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <!--endprint1-->
    </c:if>
    <c:if test="${type=='2'}">
        <!--startprint1-->
        <div id="opiondiv" style="width:794px;height: 1090px">
            <c:forEach items="${datali}" var="data" varStatus="status" begin="0" end="2">
            <div  style="width:794px;height: 545px">
                    <table style="width: 100%;height: 125px">
                        <tr>
                            <td style="width: 100%;font-size:30px;height:95px" align='center' valign='middle' colspan="3">河北省产品质量监督检验研究院业务车辆申请单
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 30%;font-size:15px;height:30px;" align='left'>
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    申请人：&nbsp;${data.get(0).get(0).get("sqr1911180001")}
                                </c:if>
                            </td>
                            <td style="width: 30%;font-size:15px;height:30px;" align='center'>
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    部门：&nbsp;${data.get(0).get(0).get("sqbm191118001")}
                                </c:if>
                            </td>
                            <td style="width: 40%;font-size:15px;height:30px;" align='right'>
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    时间：&nbsp;${data.get(0).get(0).get("recordTime")}
                                </c:if>
                            </td>
                        </tr>
                    </table>
                    <table border="1" cellspacing="0" style="width: 100%;height: 420px;font-size:15px;" >
                        <tr style="height: 50px">
                            <td style="width: 15%;text-align: center">用车路线</td>
                            <td style="width: 27%;text-align: center;" colspan="2">
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("yclx191118002")}
                                </c:if>
                            </td>
                            <td style="width: 15%; text-align: center">用车类型</td>
                            <td style="width: 18%; text-align: center" >
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("yclx191118001")}
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height: 50px">
                            <td style="width: 15%;text-align: center">用车事由</td>
                            <td style="width: 27%;text-align: center" colspan="2" >
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("ycsy191118001")}
                                </c:if>
                            </td>
                            <td style="width: 15%; text-align: center">目的地</td>
                            <td style="width: 18%; text-align: center" >
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("mdd1911180001")}
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height: 50px">
                            <td style="width: 15%;text-align: center">预计起止时间</td>
                            <td style="width: 27%;text-align: center" colspan="2" >
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("kssj191118001")}
                                </c:if>
                                ——
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("jssj191118001")}
                                </c:if>
                            </td>
                            <td style="width: 15%; text-align: center">预计天数</td>
                            <td style="width: 18%; text-align: center" >
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("yjts191118001")}
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height: 50px">
                            <td style="width: 15%;text-align: center">备注</td>
                            <td style="width: 27%;text-align: center" colspan="2">
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("bz19111900001")}
                                </c:if>
                            </td>
                            <td style="width: 15%; text-align: center">申请车型</td>
                            <td style="width: 18%; text-align: center" >
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("sqcx191118001")}
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height: 50px">
                            <td style="width: 15%;text-align: center" rowspan="2">审批意见</td>
                            <td style="width: 27%;text-align: center">
                                <c:if test="${data.get(1).size()>0 && data.get(1) != null}">
                                    ${data.get(1).get(0).get("spyj")}
                                    ${data.get(1).get(0).get("spsm")}
                                    ${data.get(1).get(0).get("recordName")}
                                    <br/>
                                    ${data.get(1).get(0).get("recordTime")}
                                </c:if>
                            </td>
                            <td style="width: 27%;text-align: center">
                                <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                                    ${data.get(2).get(0).get("spyj191115003")}
                                    ${data.get(2).get(0).get("spsm191115003")}
                                    ${data.get(2).get(0).get("recordName")}
                                    <br/>
                                    ${data.get(2).get(0).get("recordTime")}
                                </c:if>
                            </td>
                            <td style="width: 15%;text-align: center">派车数量</td>
                            <td style="width: 18%;text-align: center">
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("pcsl191118001")}
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height: 50px">
                            <td style="width: 27%;text-align: center">部门主任</td>
                            <td style="width: 27%;text-align: center">车辆管理员</td>
                            <td style="width: 15%;text-align: center">同乘人数</td>
                            <td style="width: 18%;text-align: center">
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("tcrs191118001")}
                                </c:if>
                            </td>
                        </tr>
                    </table>
                </div>
            </c:forEach>
        </div>
    <!--endprint1-->
</c:if>
    <c:if test="${type=='3'}">
        <!--startprint1-->
        <div id="opiondiv" style="width:794px;height: 1090px;padding-top: 5%">
            <c:forEach items="${datali}" var="data" varStatus="status" begin="0" end="2">
             <div  style="width:1000px;height: 545px">
                    <table style="width: 100%;height: 95px;">
                        <tr>
                            <td style="width: 100%;font-size:30px;height:75px" align='center' valign='middle' colspan="3">河北省产品质量监督检验研究院业务车辆派车单
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 30%;font-size:15px;height:20px;" align='left'>
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    申请人：&nbsp;${data.get(0).get(0).get("sqr1911180001")}
                                </c:if>
                            </td>
                            <td style="width: 30%;font-size:15px;height:20px;" align='center'>
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    部门：&nbsp;${data.get(0).get(0).get("sqbm191118001")}
                                </c:if>
                            </td>
                            <td style="width: 40%;font-size:15px;height:20px;" align='right'>
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    时间：&nbsp;${data.get(0).get(0).get("recordTime")}
                                </c:if>
                            </td>
                        </tr>
                    </table>
                    <table border="1" cellspacing="0" style="width: 100%;height: 450px;font-size:15px;" >
                        <tr style="height: 50px">
                            <td style="width: 15%;text-align: center">车型/车牌号</td>
                            <td style="width: 27%;text-align: center;" colspan="2">
                                <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                                    ${data.get(2).get(0).get("sqcx191115003")}
                                </c:if>/
                                <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                                    ${data.get(2).get(0).get("cph1911180002")}
                                </c:if>
                                <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                                    ${data.get(2).get(0).get("cph1911150001")}
                                </c:if>
                            </td>
                            <td style="width: 15%; text-align: center">车辆归属</td>
                            <td style="width: 18%; text-align: left" >
                                <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                                    ${data.get(2).get(0).get("clgs191115001")}
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height: 50px">
                            <td style="width: 15%;text-align: center">用车路线</td>
                            <td style="width: 27%;text-align: center" colspan="2" >
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("yclx191118002")}
                                </c:if>
                            </td>
                            <td style="width: 15%; text-align: center">司机姓名</td>
                            <td style="width: 18%; text-align: left" >
                                <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                                    ${data.get(2).get(0).get("sjxm191115001")}
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height: 50px">
                            <td style="width: 15%;text-align: center">用车事由</td>
                            <td style="width: 27%;text-align: center" colspan="2" >
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("ycsy191118001")}
                                </c:if>
                            </td>
                            <td style="width: 15%; text-align: center">同乘人数</td>
                            <td style="width: 18%; text-align: center" >
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("tcrs191118001")}
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height: 50px">
                            <td style="width: 15%;text-align: center">实际起止时间</td>
                            <td style="width: 27%;text-align: center" colspan="2">
                                <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                    ${data.get(3).get(0).get("kssj191115003")}
                                </c:if>
                                ——
                                <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                    ${data.get(3).get(0).get("jssj191115003")}
                                </c:if>
                            </td>
                            <td style="width: 15%; text-align: center">实际用车天数</td>
                            <td style="width: 18%; text-align: center" >
                                <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                    ${data.get(3).get(0).get("sjyct19111501")}
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height: 50px">
                            <td style="width: 15%;text-align: center">起止公里数</td>
                            <td style="width: 27%;text-align: center" colspan="2">
                                <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                    ${data.get(3).get(0).get("ksgls19111501")}
                                </c:if>
                                ——
                                <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                    ${data.get(3).get(0).get("jsgls19111501")}
                                </c:if>
                            </td>
                            <td style="width: 15%; text-align: center">行驶公里数</td>
                            <td style="width: 18%; text-align: center" >
                                <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                    ${data.get(3).get(0).get("xsgls19111501")}
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height: 50px">
                            <td style="width: 15%;text-align: center">备注</td>
                            <td style="width: 27%;text-align: center" colspan="2">
                                <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                    ${data.get(3).get(0).get("bz19111500003")}
                                </c:if>
                            </td>
                            <td style="width: 15%; text-align: center">目的地</td>
                            <td style="width: 18%; text-align: center" >
                                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                    ${data.get(0).get(0).get("mdd1911180001")}
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height: 100px">
                            <td style="width: 15%;text-align: center" rowspan="2">审批意见</td>
                            <td style="width: 27%;text-align: center">
                                <c:if test="${data.get(1).size()>0 && data.get(1) != null}">
                                    ${data.get(1).get(0).get("spyj")}
                                    ${data.get(1).get(0).get("spsm")}
                                    ${data.get(1).get(0).get("recordName")}
                                    <br/>
                                    ${data.get(1).get(0).get("recordTime")}
                                </c:if>
                            </td>
                            <td style="width: 27%;text-align: center">
                                <c:if test="${data.get(4).size()>0 && data.get(4) != null}">
                                    ${data.get(4).get(0).get("spyj191115004")}
                                    ${data.get(4).get(0).get("spsm191115004")}
                                    ${data.get(4).get(0).get("recordName")}
                                    <br/>
                                    ${data.get(4).get(0).get("recordTime")}
                                </c:if>
                            </td>
                            <td style="width: 15%;text-align: center">加油升数</td>
                            <td style="width: 18%;text-align: center">
                                <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                    ${data.get(3).get(0).get("jyss191115001")}
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height: 50px">
                            <td style="width: 27%;text-align: center">部门主任</td>
                            <td style="width: 27%;text-align: center">车辆管理员</td>
                            <td style="width: 15%;text-align: center">油耗</td>
                            <td style="width: 18%;text-align: center">
                                <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                                    ${data.get(3).get(0).get("yh19111500001")}
                                </c:if>
                            </td>
                        </tr>
                    </table>
             </div>
            </c:forEach>
        </div>
        <!--endprint1-->
    </c:if>


</body>
</html>

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
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" type="text/javascript"
        charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>

<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>

<%--<script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.js"></script>--%>
<script type="text/javascript" src="/zjyoa-pc/resources/js/printword/FileSaver.js"></script>
<script type="text/javascript" src="/zjyoa-pc/resources/js/printword/jquery.wordexport.js"></script>


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
        } else {
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
<body style="zoom:0.7">
<%--<div>
    <input type=button name='button_export' value='打印' onclick="preview(1)">
</div>--%>
    <!--startprint1-->
    <div id="opiondiv" style="width:794px;height: 1000px;">
            <div style="width:794px;height: 545px;margin-top:35px">
                <table style="width: 100%;height: 125px">
                    <tr>
                        <td style="width: 100%;font-size:30px;height:95px" align='center' valign='middle' colspan="3">
                            河北省产品质量监督检验研究院业务车辆申请单
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 30%;font-size:15px;height:30px;" align='left'>
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                申请人:${data.get(0).get(0).get("sqr1911180001")}
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
                <table border="1" cellspacing="0" style="width: 100%;height: 420px;font-size:15px;">
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center">用车路线</td>
                        <td style="width: 27%;text-align: center;" colspan="2">
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("yclx191118002")}
                            </c:if>
                        </td>
                        <td style="width: 15%; text-align: center">用车类型</td>
                        <td style="width: 18%; text-align: center">
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("yclx191118002")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center">用车事由</td>
                        <td style="width: 27%;text-align: center" colspan="2">
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("ycsy191118001")}
                            </c:if>
                        </td>
                        <td style="width: 15%; text-align: center">目的地</td>
                        <td style="width: 18%; text-align: center">
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("mdd1911180001")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center">预计起止时间</td>
                        <td style="width: 27%;text-align: center" colspan="2">
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("kssj191118001")}
                            </c:if>
                            ——
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("jssj191118001")}
                            </c:if>
                        </td>
                        <td style="width: 15%; text-align: center">预计天数</td>
                        <td style="width: 18%; text-align: center">
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
                        <td style="width: 18%; text-align: center">
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("sqcx191118001")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 15%;text-align: center" rowspan="2">审批意见</td>
                        <td style="width: 27%;text-align: center" colspan="2">
                            <div style="font-size: 18px;text-align: left"></div>
                            <div style="text-align: left;font-size: 12px">
                                <c:if test="${data.get(1).size()>0 && data.get(2) != null}">
                                    ${data.get(1).get(0).get("spyj")}
                                    ${data.get(1).get(0).get("recordName")}
                                    ${data.get(1).get(0).get("recordTime")}
                                </c:if>
                            </div>
                        </td>
                        <td style="width: 15%;text-align: center">派车数量</td>
                        <td style="width: 18%;text-align: center">
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("pcsl191118001")}
                            </c:if>
                        </td>
                    </tr>
                    <tr style="height: 50px">
                        <td style="width: 27%;text-align: center" colspan="2">
                            <div style="font-size: 18px; text-align: left">车辆管理员</div>
                            <div style="text-align: left;font-size: 12px">
                                <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                                    ${data.get(2).get(0).get("spyj191115003")}
                                    ${data.get(2).get(0).get("recordName")}
                                    ${data.get(2).get(0).get("recordTime")}
                                </c:if>
                            </div>
                        </td>
                        <td style="width: 15%;text-align: center">同乘人数</td>
                        <td style="width: 18%;text-align: center">
                            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                                ${data.get(0).get(0).get("tcrs191118001")}
                            </c:if>
                        </td>
                    </tr>
                </table>
                <span>注：该单据手写无效</span>
            </div>
    </div>
    <!--endprint1-->
</body>
</html>

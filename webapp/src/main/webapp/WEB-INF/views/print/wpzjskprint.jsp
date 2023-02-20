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
<style>
    @media print{
        @page{
            margin:0;
            size:auto;
        }
    }
</style>
<jsp:include page="/common/js.jsp"></jsp:include>
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
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
            document.getElementsByTagName('body')[0].style.zoom=0.70;
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
        $("#sealdiv").wordExport('出差审批单')
    }


</script>
<html>
<head>
    <title>外聘专家授课审批表</title>
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


<div id="sealdiv" style="width: 756px;height: 1086px;margin: 0 auto;">
<!--startprint1-->
    <div style="width: 856px;margin: 0 auto;">
        <table style="width: 856px;height: 90px">
            <tr align="left">
                <td>ZJY-GS-602Bj/0</td>
            </tr>
            <tr>
                <td style="width: 756px;font-size:30px;height:90px" align='center' valign='middle' colspan="4">外聘专家授课审批表</td>
            </tr>
        </table>
        <table border="1" cellspacing="0" style="width: 856px;height:990px; overflow-y:scroll">
            <tr style="height: 8%">
                <td style="width: 20%;text-align: center">申请部门</td>
                <td style="width: 20%;text-align: center" colspan="2">
                    <c:if test="${data.get('onemap')!=null}">
                        ${data.get("onemap").get("sqrbm200525001")}
                    </c:if>
                </td>
                <td style="width: 20%; text-align: center">申请人</td>
                <td style="width: 20%; text-align: center">
                    <c:if test="${data.get('onemap')!=null}">
                        ${data.get("onemap").get("sqrxm200525001")}
                    </c:if>
                </td>
            </tr>
            <tr style="height: 8%">
                <td style="width: 20%;text-align: center">培训起止时间</td>
                <td style="width: 20%;text-align: center" colspan="2">
                    <c:if test="${data.get('onemap')!=null}">
                        ${data.get("onemap").get("kssj")}--${data.get("onemap").get("jssj")}
                    </c:if>
                </td>
                <td style="width: 20%; text-align: center">地点</td>
                <td style="width: 20%; text-align: center">
                    <c:if test="${data.get('onemap')!=null}">
                        ${data.get("onemap").get("dd200525000002")}
                    </c:if>
                </td>
            </tr>
            <tr style="height: 8%">
                <td style="width: 20%;text-align: center">授课老师</td>
                <td style="width: 20%;text-align: center" colspan="2">
                    <c:if test="${data.get('onemap')!=null}">
                        ${data.get("onemap").get("skls2005250002")}
                    </c:if>
                </td>
                <td style="width: 20%; text-align: center">所在单位</td>
                <td style="width: 20%; text-align: center">
                    <c:if test="${data.get('onemap')!=null}">
                        ${data.get("onemap").get("szdw2005250002")}
                    </c:if>
                </td>
            </tr>
            <tr style="height: 8%">
                <td style="width: 20%;text-align: center">申请理由</td>
                <td style="width: 80%;text-align: left;" colspan="4">
                    <c:if test="${data.get('onemap')!=null}">
                        ${data.get("onemap").get("sqly2005250002")}
                    </c:if>
                </td>
            </tr>
            <tr style="height:11%">
                <td style="width: 20%;text-align: center">培训内容</td>
                <td style="width: 80%;text-align: left;" colspan="4">
                    <c:if test="${data.get('onemap')!=null}">
                        ${data.get("onemap").get("pxnr2005250002")}
                    </c:if>
                </td>
            </tr>
            <tr style="height:4%">
                <td style="width: 20%;text-align: center" rowspan="2">计划费用</td>
                <td style="width: 20%;text-align: center;">讲课费</td>
                <td style="width: 20%;text-align: center;">
                    <c:if test="${data.get('onemap')!=null}">
                        ${data.get("onemap").get("jkf20052500002")}
                    </c:if>
                </td>
                <td style="width: 20%;text-align: center;">往返车（机）票</td>
                <td style="width: 20%;text-align: center;">
                    <c:if test="${data.get('onemap')!=null}">
                        ${data.get("onemap").get("wfcjp200525002")}
                    </c:if>
                </td>
            </tr>
            <tr style="height:4%">
                <td style="width: 20%;text-align: center;">食宿</td>
                <td style="width: 20%;text-align: center;">
                    <c:if test="${data.get('onemap')!=null}">
                            ${data.get("onemap").get("ss200525000002")}
                    </c:if>
                </td>
                <td style="width: 20%;text-align: center;">合计</td>
                <td style="width: 20%;text-align: center;">
                    <c:if test="${data.get('onemap')!=null}">
                        ${data.get("onemap").get("hjje2005250002")}
                    </c:if>
                </td>
            </tr>
            <tr style="height:10%">
                <td style="width: 20%;text-align: center">部门意见</td>
                <td style="width: 80%;text-align: center" colspan="4">
                    <c:if test="${data.get('bmmap')!=null}">
                        <div class="msg-box">
                            <label class="title">${data.get("bmmap").get("spyj")}</label>
                            <label class="name" style="width: 120px;height: 50px">${data.get("bmmap").get("spname")}</label>
                            <label class="time">${data.get("bmmap").get("sptime")}</label>
                        </div>
                    </c:if>
                </td>
            </tr>
            <tr style="height:10%">
                <td style="width: 20%;text-align: center">主管院领导意见</td>
                <td style="width: 80%;text-align: center;" colspan="4">
                    <c:if test="${data.get('zjmap')!=null}">
                        <div class="msg-box">
                            <label class="title">${data.get("zjmap").get("spyj")}</label>
                            <label class="name" style="width: 120px;height: 50px">${data.get("zjmap").get("spname")}</label>
                            <label class="time">${data.get("zjmap").get("sptime")}</label>
                        </div>
                    </c:if>
                </td>
            </tr>
            <tr style="height:10%">
                <td style="width: 20%;text-align: center">人力资源部审核意见</td>
                <td style="width: 80%;text-align: center;" colspan="4">
                    <c:if test="${data.get('hrmap')!=null}">
                        <div class="msg-box">
                            <label class="title">${data.get("hrmap").get("spyj")}</label>
                            <label class="name" style="width: 120px;height: 50px">${data.get("hrmap").get("spname")}</label>
                            <label class="time">${data.get("hrmap").get("sptime")}</label>
                        </div>
                    </c:if>
                </td>
            </tr>
            <tr style="height:10%">
                <td style="width: 20%;text-align: center">分管院领导意见</td>
                <td style="width: 80%;text-align: center;" colspan="4">
                     <c:if test="${data.get('fgyldmap')!=null}">
                        <div class="msg-box">
                            <label class="title">${data.get("fgyldmap").get("spyj")}</label>
                            <label class="name" style="width: 120px;height: 50px">${data.get("fgyldmap").get("spname")}</label>
                            <label class="time">${data.get("fgyldmap").get("sptime")}</label>
                        </div>
                     </c:if>
                </td>
            </tr>
        </table>
        <table style="width: 856px;height: 90px">
            <tr align="right">
                <td> <span>注：该单据手写无效</span></td>
            </tr>
        </table>
    </div>

<!--endprint1-->
</div>

</body>
</html>

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
        $("#sealdiv").wordExport('质量考核表')
    }


</script>
<html>
<head>
    <title>质量考核表</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick=preview(1)>
</div>


<div id="sealdiv" style="width: 856px;height: 606px;margin: 0 auto;">
<!--startprint1-->
    <div style="width: 856px;margin: 0 auto;">
        <table style="width: 856px;height: 90px;padding-top: 7%;padding-bottom: 3%">
            <tr>
                <td style="width: 856px;font-size:25px;height:90px" align='center' valign='middle' colspan="4">河北省产品质量监督检验研究院 检验检测工作质量考核表</td>
            </tr>
        </table>
        <table border="1" cellspacing="0" style="width: 856px;height:800px; overflow-y:scroll">
            <tr style="height: 50px">
                <td style="width: 25%;text-align: center">考核时限</td>
                <td colspan="2" style="width: 25%;text-align: center">年季度</td>
            </tr>
            <tr style="height: 50px">
                <td style="width: 20%;text-align: center" rowspan="14">考核结果</td>
                <td style="width: 40%;text-align: center">部门</td>
                <td style="width: 40%;text-align: center">分值</td>
            </tr>
            <tr style="height: 50px">
                <td style="width: 40%;text-align: center">体育用品检验中心</td>
                <td style="width: 40%;text-align: center">
                  <c:if test="${datali.size()>0 && datali != null}">
                    <c:forEach items="${datali}" var="da" varStatus="status">
                        ${da.get("D2019042800025")}
                    </c:forEach>
                </c:if>
                </td>
            </tr>
            <tr style="height: 50px">
                <td style="width: 40%;text-align: center">电线电缆检验中心</td>
                <td style="width: 40%;text-align: center">
                <c:if test="${datali.size()>0 && datali != null}">
                <c:forEach items="${datali}" var="da" varStatus="status">
                    ${da.get("D2019042800026")}
                </c:forEach>
                </c:if>
                </td>
            </tr>
            <tr style="height: 50px">
                <td style="width: 40%;text-align: center">钢铁产品检验中心</td>
                <td style="width: 40%;text-align: center">
                     <c:if test="${datali.size()>0 && datali != null}">
                    <c:forEach items="${datali}" var="da" varStatus="status">
                        ${da.get("D2019042800027")}
                    </c:forEach>
                    </c:if>
                </td>
            </tr>
            <tr style="height: 50px">
                <td style="width: 40%;text-align: center">汽车配件检验中心</td>
                <td style="width: 40%;text-align: center">
                <c:if test="${datali.size()>0 && datali != null}">
                    <c:forEach items="${datali}" var="da" varStatus="status">
                        ${da.get("D2019042800028")}
                    </c:forEach>
                </c:if>
                </td>
            </tr>
            <tr style="height: 50px">
                <td style="width: 40%;text-align: center">家具检验中心</td>
                <td style="width: 40%;text-align: center">
                <c:if test="${datali.size()>0 && datali != null}">
                    <c:forEach items="${datali}" var="da" varStatus="status">
                        ${da.get("D2019042800029")}
                    </c:forEach>
                </c:if>
                </td>
            </tr>
            <tr style="height: 50px">
                <td style="width: 40%;text-align: center">公安消防产品检验部</td>
                <td style="width: 40%;text-align: center">
                <c:if test="${datali.size()>0 && datali != null}">
                    <c:forEach items="${datali}" var="da" varStatus="status">
                        ${da.get("D2019042800030")}
                    </c:forEach>
                 </c:if>
                </td>
            </tr>
            <tr style="height: 50px">
                <td style="width: 40%;text-align: center">机电产品检验部</td>
                <td style="width: 40%;text-align: center">
                 <c:if test="${datali.size()>0 && datali != null}">
                    <c:forEach items="${datali}" var="da" varStatus="status">
                        ${da.get("D2019042800031")}
                    </c:forEach>
                 </c:if>
                </td>
            </tr>
            <tr style="height: 50px">
                <td style="width: 40%;text-align: center">化工产品检验部</td>
                <td style="width: 40%;text-align: center">
                <c:if test="${datali.size()>0 && datali != null}">
                    <c:forEach items="${datali}" var="da" varStatus="status">
                        ${da.get("D2019042800032")}
                    </c:forEach>
                </c:if>
                </td>
            </tr>
            <tr style="height: 50px">
                <td style="width: 40%;text-align: center">节能环保产品检验部</td>
                <td style="width: 40%;text-align: center">
                 <c:if test="${datali.size()>0 && datali != null}">
                    <c:forEach items="${datali}" var="da" varStatus="status">
                        ${da.get("D2019042800033")}
                    </c:forEach>
                </c:if>
                </td>
            </tr>
            <tr style="height: 50px">
                <td style="width: 40%;text-align: center">轻工产品检验部</td>
                <td style="width: 40%;text-align: center">
                <c:if test="${datali.size()>0 && datali != null}">
                    <c:forEach items="${datali}" var="da" varStatus="status">
                        ${da.get("D2019042800034")}
                    </c:forEach>
              </c:if>
                </td>
            </tr>
            <tr style="height: 50px">
                <td style="width: 40%;text-align: center">建材产品检验部</td>
                <td style="width: 40%;text-align: center">
                <c:if test="${datali.size()>0 && datali != null}">
                    <c:forEach items="${datali}" var="da" varStatus="status">
                        ${da.get("D2019042800035")}
                    </c:forEach>
                </c:if>
                </td>
            </tr>
            <tr style="height: 50px">
                <td style="width: 40%;text-align: center">基础检验部</td>
                <td style="width: 40%;text-align: center">
                  <c:if test="${datali.size()>0 && datali != null}">
                    <c:forEach items="${datali}" var="da" varStatus="status">
                        ${da.get("D2019042800036")}
                    </c:forEach>
                   </c:if>
                </td>
            </tr>
            <tr style="height: 50px">
                <td style="width: 40%;text-align: center">业务发展中心</td>
                <td style="width: 40%;text-align: center">
                <c:if test="${datali.size()>0 && datali != null}">
                    <c:forEach items="${datali}" var="da" varStatus="status">
                        ${da.get("D2019042800037")}
                    </c:forEach>
                </c:if>
                </td>
            </tr>
            <tr style="height: 100px">
                <td style="width: 20%;text-align: center">质量技术部</td>
                <td style="width: 80%;text-align: center" colspan="2">
                    <c:if test="${datali.size()>0 && datali != null}">
                        <c:forEach items="${datali}" var="da" varStatus="status">
                            ${da.get("spsmbz")} ${da.get("bzname")} ${da.get("bztime")}
                        </c:forEach>
                    </c:if>
                </td>
            </tr>
            <tr style="height: 100px">
                <td style="width: 20%;text-align: center">主管院长</td>
                <td style="width: 80%;text-align: center" colspan="2">
                    <c:if test="${datali.size()>0 && datali != null}">
                        <c:forEach items="${datali}" var="da" varStatus="status">
                            ${da.get("spsmzg")} ${da.get("zgname")} ${da.get("zgtime")}
                        </c:forEach>
                    </c:if>
                </td>
            </tr>

        </table>
    </div>
<!--endprint1-->
</div>

</body>
</html>

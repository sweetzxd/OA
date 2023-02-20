<%--
  User: zxd
  Date: 2020/02/18
  Time: 下午 2:08
  Explain: 说明
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

<html>
<head>
    <title>培训实施记录表</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" type="text/css" href="/zjyoa-pc/resources/css/print.css"/>
    <link rel="stylesheet" href="/zjyoa-pc/resources/layuiadmin/layui/css/layui.css" media="all">
    <style>
        .icon-box{
            float:left;
            margin-left: 30px;
            height: 25px;
            line-height: 25px;
        }
        .layui-size{
            padding: 5px 0 5px 0;
            font-size: 15px;
        }
        table tr td{
            width: 94px;
            text-align: center
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
            $("#sealdiv").wordExport('培训实施记录表')
        }


    </script>
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick=preview(1)>
    <input type=button name='button_export' value='导出excel' onclick="exportexcelseal('sealdiv')">
    <input type=button name='button_export' value='导出word' onclick="exportwordseal();">
</div>
<div id="sealdiv" style="width: 756px;height: 1086px;">
    <!--startprint1-->
    <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
        <c:set var="recno" value="${data.get(0).get(0).get('recno')}"></c:set>
    </c:if>
    <table style="width: 756px;height: 100px">
        <tr>
            <td style="width: 756px;font-size:15px;height:30px;text-align:left;" align='left' valign='middle' colspan="4">ZJY-GS-602Bf-2019/0
            </td>
        </tr>
        <tr>
            <td style="width: 756px;font-size:30px;height:70px" align='center' valign='middle' colspan="4">员工教育培训卡
            </td>
        </tr>
    </table>

    <table border="1" cellspacing="0" style="width: 100%;height: 900px;" >
        <tr style="height:50px;">
            <td style="">所在部门</td>
            <td style="" colspan="2">${emp.department}</td>
            <td style="">姓名</td>
            <td style="">${emp.staffName}</td>
            <td style="">岗位</td>
            <td style="" colspan="2">${emp.post}</td>
        </tr>
        <%--<tr style="min-height:180px;">
            <td style="position:relative;" colspan="8">
                <span style="position:absolute;left:10px;top:5px;">转岗经历：</span>
                <span style="position:absolute;left:50px;top:30px;padding-right:50px;">
                    <c:forEach items="${ygzg}" var="zg" varStatus="status">
                        <fmt:formatDate pattern="yyyy年MM月dd日" value="${zg.zgrq200222001}"/>转至  <c:out value="${zg.zrbm200222001}"/>  部门  <c:out value="${zg.zhgw200222001}"/>  岗位
                    </c:forEach>
                </span>
            </td>
        </tr>--%>
        <tr style="height:50px;">
            <td style="position:relative;" colspan="8">培训记录</td>
        </tr>
        <tr style="height:50px;">
            <td style="">培训日期</td>
            <td style="" colspan="2">培训内容</td>
            <td style="">学时</td>
            <td style="">培训教师</td>
            <td style="">培训类别</td>
            <td style="">培训形式</td>
            <td style="">成绩</td>
        </tr>
        <c:forEach items="${pxss}" var="da" varStatus="status">
            <tr style="height:50px;">
                <td style="">${da.pxrq200218002}</td>
                <td style="" colspan="2">${da.pxnr200218001}</td>
                <td style="">${da.xs20021800001}</td>
                <td style="">${da.skjs200218002}</td>
                <td style="">${da.pxlb200218002}</td>
                <td style="">${da.pxxs200218003}</td>
                <td style="">${da.cj20022200001}</td>
            </tr>
        </c:forEach>
        <c:if test="${pxsize>0}">
            <c:forEach var="i" begin="1" end="${pxsize}">
                <tr style="height:50px;">
                    <td style=""></td>
                    <td style="" colspan="2"></td>
                    <td style=""></td>
                    <td style=""></td>
                    <td style=""></td>
                    <td style=""></td>
                    <td style=""></td>
                </tr>
            </c:forEach>
        </c:if>

    </table>
    <!--endprint1-->
</div>
<script>

</script>
</body>
</html>
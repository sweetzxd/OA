<%--
  User: zxd
  Date: 2020/02/18
  Time: 下午 2:08
  Explain: 说明
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.springframework.util.Base64Utils" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.oa.core.util.SpringContextUtil" %>
<%@ page import="com.oa.core.service.util.TableService" %>
<%@ page import="java.util.List" %>
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
    <title></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
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

        function start() {
            var type="${type}";
            console.log(type);
            console.log("66666666666666");
            if(type==0){
                $("#buts").attr("style","display:none;");
            }else {
                $("#buts").attr("style","display:block;");
            }

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
            $("#sealdiv").wordExport('耗材明细表')
        }


    </script>
</head>
<body onload="start()">

<div id="buts">
    <input type=button name='button_export' value='打印' onclick=preview(1)>
    <input type=button name='button_export' value='导出excel' onclick="exportexcelseal('sealdiv')">
    <input type=button name='button_export' value='导出word' onclick="exportwordseal();">
</div>
<div id="sealdiv">
    <table border="1px" style="margin-left: 17%;align-content: center;margin-bottom: 20px;margin-top: 10px">
        <tr><td colspan="5"> 耗材和低值设备采购预算审批</td></tr>
        <tr>
            <td>类别</td>
            <td><a href="javascript:void(0)" onclick="turnup('实验耗材','${date}')">实验耗材（元）</a></td>
            <td><a href="javascript:void(0)" onclick="turnup('办公耗材','${date}')">办公耗材（元）</a></td>
            <td><a href="javascript:void(0)" onclick="turnup('低值设备','${date}')">低值设备（元）</a></td>
            <td>合计（元）</td>
        </tr>
        <tr>
            <td>金额（元）</td>
            <td>${syhcbzwz}</td>
            <td>${bghc}</td>
            <td>${dzsb}</td>
            <td>${hj1}</td>
        </tr>
    </table>

    <table border="1px" style="margin-left: 20%;align-content: center;margin-bottom: 20px;margin-top: 10px">
        <tr><td colspan="6"> 各部门耗材费用汇总表</td></tr>
        <tr>
            <td>序号</td>
            <td>部门</td>
            <td>办公耗材</td>
            <td>实验耗材</td>
            <td>标准物质</td>
            <td>总额</td>
        </tr>
        <c:forEach items="${dept}" var="data" varStatus="status">
            <tr>
                <td>${status.index+1}</td>
                <td><a href="javascript:void(0)" onclick="turnupdept('${data.get("deptid")}','${date}')">${data.get("deptname")}</a></td>
                <td>${data.get("c")}</td>
                <td>${data.get("a")}</td>
                <td>${data.get("b")}</td>
                <td>${data.get("hj")}</td>
            </tr>
        </c:forEach>
        <tr>
            <td>合计</td>
            <td></td>
            <td>${bghc}</td>
            <td>${syhc}</td>
            <td>${bzwz}</td>
            <td>${hj2}</td>
        </tr>
    </table>
    <input  id="type" value="${type}" type="hidden">

</div>


<script>
    function turnup(type,date){
        parent.layer.open({
            type: 2,
            title: false,
            shade: [0],
            area: ['900px', '300px'],
            offset: 'auto', //右下角弹出
            content: '/zjyoa-pc/Hcgl/selectmingxi.do?type='+type+'&date='+date, //iframe的url，no代表不显示滚动条
        });
    }

    function turnupdept(type,date){
        parent.layer.open({
            type: 2,
            title: false,
            shade: [0],
            area: ['900px', '300px'],
            offset: 'auto', //右下角弹出
            content: '/zjyoa-pc/Hcgl/selectbydept.do?dept='+type+'&date='+date, //iframe的url，no代表不显示滚动条
        });
    }
</script>
</body>
</html>
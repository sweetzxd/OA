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
<%--导出word引入的js--%>
<style>
    table {
        table-layout:fixed;
        WORD-BREAK:break-all;
    }
</style>
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
</script>
<html>
<head>
    <title>部门培训需求调研表</title>
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
<!--startprint1-->
    <div style="width: 756px;margin: 0 auto;">
        <table style="width: 756px;height: 90px">
            <tr>
                <td style="padding-left: 10px" colspan="6">ZJY-GS-602Ba-2019/0</td>
            </tr>
            <tr>
                <td style="width: 756px;font-size:30px;height:90px" align='center' valign='middle' colspan="6">部门培训需求调研表</td>
            </tr>
            <tr>
                <td align='center'>部门:</td>
                <td align='center'>${bm}</td>
                <td align='center'>部门负责人:</td>
                <td align='center'>${bz}</td>
                <td align='center'>主管院领导:</td>
                <td align='center'>${zgbz}</td>
            </tr>
            <tr><td colspan="6"></td></tr>
        </table>
        <table border="1" cellspacing="0" style="width: 756px;height: 20%;">
            <tr style="text-align:center; height: 10%;width: 756px">
                <td style="width: 30px">序号</td>
                <td style="width: 100px">培训内容</td>
                <td style="width: 50px">对象</td>
                <td style="width: 50px">形式</td>
                <td style="width: 70px">时间</td>
                <td style="width: 50px">地点</td>
                <td style="width:100px">目的目标</td>
                <td style="width: 80px">授课老师</td>
            </tr>
            <c:forEach items="${data}" var="list" varStatus="status">
                <tr style="text-align: center; height:20%;width: 756px">
                    <td style="width: 30px">${status.index+1}</td>
                    <td style="width: 100px">${list.pxnr2006120001}</td>
                    <td style="width: 50px">${list.dx200612000001}</td>
                    <td style="width: 50px">${list.xs200612000001}</td>
                    <td style="width: 70px">${list.sj200612000001}</td>
                    <td style="width: 50px">${list.dd200612000001}</td>
                    <td style="width: 100px">${list.mdmb2006120001}</td>
                    <td style="width: 80px">${list.skls2006120001}</td>
                </tr>
            </c:forEach>
        </table>
    <span>注：该单据手写无效</span>
  </div>
<!--endprint1-->
</body>
</html>

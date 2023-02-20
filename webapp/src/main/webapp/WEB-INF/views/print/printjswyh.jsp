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
<%
    String decodeUrl = request.getParameter("url");
    if (decodeUrl != null) {
        String url = new String(Base64Utils.decode(decodeUrl.getBytes()));
        request.setAttribute("url", url);
    }
%>
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>
<%--<script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.js"></script>--%>
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
</script>
<html>
<head>
    <title>技术委员会意见</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick="preview(1)">
</div>
<c:if test="${empty data}">
    <div id="opiondiv" style="width:100%;height: 550px;padding-top: 30px">
        <table style="width: 850px;height: 50px;margin:auto">
            <tr>
                <td style="width: 100%;font-size:30px;height:50px;color: red" align='center' valign='middle'>
                   无技术委员会意见
                </td>
            </tr>
        </table>
    </div>
</c:if>
<!--startprint1-->
<c:forEach items="${data}" var="da" varStatus="status">
    <div id="opiondiv" style="width:100%;height: 550px;padding-top: 30px">
        <table style="width: 850px;height: 50px;margin:auto">
            <tr>
                <td style="width: 100%;font-size:30px;height:50px" align='center' valign='middle'>
                    技术委员会意见
                </td>
            </tr>
        </table>
        <div style="width: 850px;height: 400px;font-size:15px;margin:auto;border: 1px #000 solid">
            <div style="height: 300px">
                <p style=" text-align: left;text-indent: 25px;margin: auto 25px;">
                        ${da.get("yjsm")}
                </p>
            </div>
        </div>
        <table style="width: 850px;height: 50px;margin:auto">
            <tr>
                <td style="width: 100%;height:30px" align='left' valign='middle'>
                    负责人:<span style="padding-left: 20px">${da.get("recordName")}</span>
                </td>
            </tr>
            <tr>
                <td style="width: 100%;height:20px" align='left' valign='middle'>
                    提交时间:<span style="padding-left: 20px">${da.get("time")}</span>
                </td>
            </tr>
            <tr>
                <td style="width: 100%;padding-top: 5px" align='left' valign='middle'>注：该单据手写无效</td>
            </tr>
        </table>
    </div>
</c:forEach>
<!--endprint1-->
</body>
</html>

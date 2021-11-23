<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/09/26
  Time: 下午 5:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<html>
<head>
    <meta charset="utf-8">
    <title>所有流程</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/resources/flow/workflow.css" type="text/css"/>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
</head>

<body onload="newflow()">
<table id="savetask" class="table table-hover table-bordered table-layout:fixed" style="padding:50px;">
    <thead>
    <tr style="text-align: center">
        <th style="text-align: center" width="40%">流程定义号</th>
        <th style="text-align: center" width="40%">流程名称</th>
        <th style="text-align: center" width="20%">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${!empty wkflw}">
        <c:forEach items="${wkflw}" var="wkf" varStatus="status">
            <tr id="${wkf.wkflwID}" style="text-align: center">
                <td><a href="javascript:void(0)" onclick="goto('${wkf.wkflwID}')">${wkf.wkflwID}</a></td>
                <td>${wkf.wkfName}</td>
                <td><a href="/updatepack/exportfolwupdate.do?wkflwId=${wkf.wkflwID}">获取更新包</a></td>
            </tr>
        </c:forEach>
    </c:if>
    </tbody>
</table>
</body>
<jsp:include page="/common/js.jsp"></jsp:include>
<script type="text/javascript">
    function newflow() {

    }
    function goto(id) {
        window.parent.location.href="/workflow/newworkflow.do?id="+id;
    }

</script>
</html>

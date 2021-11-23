<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/10/18
  Time: 下午 2:55
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
    <title>消息详情</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
</head>
<body>
<div class="layui-container">
    <div class="layui-row">
        <div class="layui-col-xs12">
            <form class="layui-form layui-form-pane">
                <div class="layui-form-item" style="margin-top: 30px;">
                    <h3 style="text-align:center;">${message.msgTitle}</h3>
                </div>
                <div class="layui-form-item" style="margin-top: 30px;border: 1px solid #e6e6e6;">
                    <div style="min-height: 300px;padding: 10px;">${message.msgText}</div>
                </div>
                <div class="layui-form-item">
                    <a>附件：</a>
                    <c:if test="${fileLsit.size()==0}">
                        <a>无</a>
                    </c:if>
                    <c:if test="${fileLsit.size()!=0}">
                        <c:forEach items="${fileLsit}" var ="m">
                            <a style="color:blue" href="/${m.file}" target="_blank">${m.fileName}</a>&nbsp;&nbsp;&nbsp;&nbsp;
                        </c:forEach>
                    </c:if>
                </div>
                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <a style="margin-left: 80%">${msgSendUserName}</a>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <a style="margin-left: 75%">${msgSendTimeStr}</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="/resources/js/system/manager.js" charset="utf-8"></script>
<script type="text/javascript">

</script>
</body>
</html>

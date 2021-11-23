<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/10/13
  Time: 上午 10:36
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
    <title>流程属性</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/resources/css/flowpage.css" type="text/css"/>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript">
        var layer;
        function starscript() {
            layui.use(['form', 'layer','layedit'], function () {
                var form = layui.form;
                layer = layui.layer;
                var layedit = layui.layedit;
                form.render();
            });
        }
    </script>
<body onload="starscript()">
<fieldset class="layui-elem-field">
    <div class="layui-form layui-form-pane">
        <div class="layui-container">
            <div class="layui-row">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <c:if test="${!empty wkflwlog}">
                        <c:forEach items="${wkflwlog}" var="log" varStatus="status">
                            ${log.inParams }
                        </c:forEach>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</fieldset>
</body>
</html>

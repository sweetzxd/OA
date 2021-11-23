<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/12/24
  Time: 下午 2:29
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
    <title>关联菜单</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <style>
        .layui-breadcrumb a:focus {
             color: red !important
        }
    </style>
    <script type="text/javascript">
        function starscript(){
            layui.use(['form','element'], function(){
                var element = layui.element;
                var form = layui.form;
                form.render();
                element.init();
            });
        }
        function change(url){
            document.getElementById("option").src=url;
        }
    </script>
</head>
<body onload="starscript()" style="overflow: hidden;">
<div width="100%" style="padding: 10px;">
    <span class="layui-breadcrumb" lay-separator="|">
        <c:forEach items="${list}" var="l">
            <a href="javascript:change('${l.contextmenu}');">${l.pageTitle}</a>
        </c:forEach>
    </span>
</div>
<iframe data-frameid="home" id="option" name="option" src="${src}" frameborder="no" width="100%" height="97%"></iframe>
</body>

</html>

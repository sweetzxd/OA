<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/08/15
  Time: 下午 6:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="row" style="height: 25px;margin-left: -15px;margin-right: -15px;">
    <div class="col-md-12 text-right rgs-top-nav-box">
        <c:if test="${sessionScope.loginer!=null}">
            <c:if test="${sessionScope.loginer.user}">
                <span style="padding-right:30px;color:#5c5c5c;">欢迎您!${sessionScope.loginer.name}</span>
            </c:if>
            <a class="btn-sm top-nav" href="platform/login/logout.do">退出</a>
        </c:if>
        <c:if test="${sessionScope.loginer==null}">
            <a class="btn-sm top-nav" href="login.do">登录</a>
        </c:if>
    </div>
</div>
<div class="container-fluid" style="padding-bottom:5px;">
    <div class="row">
        <div class="col-md-4">
            <a href="home.do">这是首页面</a>
        </div>
        <div class="col-md-4">

        </div>

        <div class="col-md-4">

        </div>
    </div>
</div>

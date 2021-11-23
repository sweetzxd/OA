<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="user" value="${sessionScope.loginer.name}"/>

<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
<%--
            <a class="navbar-brand" href="#">网站名</a>
--%>
        </div>
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav navbar-left">
                <li class="active"><a href="#">主页</a></li>
                <li id="userManager"><a href="/title/system.do">系统维护</a></li>
                <li id="roleManager"><a href="/user/roleManager.do">权限管理</a></li>
                <li id="menuManager"><a href="/user/menuManager.do">菜单管理</a></li>
                <li id="processPage"><a href="/platform/admin/formedit.do">表单定制</a></li>
            </ul>
        </div>

    </div>
</nav>

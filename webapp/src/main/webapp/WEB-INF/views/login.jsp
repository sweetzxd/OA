<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/08/07
  Time: 上午 10:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.springframework.util.Base64Utils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <meta charset="utf-8">
    <title>腾叶智慧办公系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/resources/css/login.css">
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
    <script type="text/javascript" src="/resources/js/jquery/jquery.particleground.min.js"></script>
</head>
<body>
<div id="particles">
    <div class="intro">
        <div class="header"><img src="/images/logo.png" alt=""/></div>
        <form action="/login.do" id="login_form" method="post">
            <ul>
                <li>
                    <div class="lt">
                        <i class="layui-icon layui-icon-username" style="font-size: 20px;"></i>
                    </div>
                    <div class="rt">
                        <input type="text" id="userName" name="userName" placeholder="请输入账号" value="${username}"/>
                    </div>
                </li>
                <li>
                    <div class="lt">
                        <i class="layui-icon layui-icon-password" style="font-size: 20px;"></i>
                    </div>
                    <div class="rt">
                        <input type="password" id="password" name="password" placeholder="请输入密码" value=""/>
                    </div>
                </li>
            </ul>
            <p style="color: sandybrown">${error}</p>
            <input type="hidden" name="referer" value="${url}">
        </form>
        <div class="xuan">
            <input id="rmbUser" class="chat-button-location-radio-input" type="checkbox" value="remember-me"/>
            <label for="rmbUser"></label><span class="sp" style="margin-left: 25px;">记住一周</span>
            <input id="subUser" class="chat-button-location-radio-input" type="checkbox" value="remember-me"/>
            <label for="subUser"></label><span style="margin-left: 25px;">自动登录</span>
        </div>
        <div class="deng"><input type="button" value="登录" onclick="loginSubmit()"/></div>
    </div>
</div>

<script type="text/javascript" src="/resources/js/jquery/jquery.cookie.js"></script>
<script>
    $(function () {
        $('#particles').particleground();
        $('#your-element').particleground({
            dotColor: '#2856b2',
            lineColor: '#2856b2'
        });
        login.initPage();
        //初始化页面时验证是否记住了密码
        if ($.cookie("rmbUser") == "true") {
            $("#rmbUser").attr("checked", true);
            $("#userName").val($.cookie("userName"));
            $("#password").val($.cookie("password"));

        }
        if ($.cookie("subUser") == "true") {
            $("#subUser").attr("checked", true);
            //loginSubmit();
        }
    });
    var login = {
        initPage: function () {
            if (window.top != window.self) {
                top.location.href = location.href;
            }
        },
    };

    function loginSubmit() {
        saveUserInfo();
        document.getElementById('login_form').submit();
    }

    $(document).keyup(function (event) {
        if (event.keyCode == 13) {
            document.getElementById('login_form').submit();
        }
    });

    //保存用户信息
    function saveUserInfo() {
        // 存储一个带14天期限的 cookie
        if (document.getElementById("rmbUser").checked) {
            var userName = $("#userName").val();
            var password = $("#password").val();
            $.cookie("rmbUser", "true", {expires: 7});
            $.cookie("userName", userName, {expires: 7});
            $.cookie("password", password, {expires: 7});
        } else {
            $.cookie("rmbUser", "false", {expires: -1});
            $.cookie("userName", '', {expires: -1});
            $.cookie("password", '', {expires: -1});
        }
        if (document.getElementById("subUser").checked) {
            $.cookie("subUser", "true", {expires: 7});
        } else {
            $.cookie("subUser", "false", {expires: -1});
        }
    }
</script>
</body>
</html>
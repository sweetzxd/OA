<%@ page import="com.oa.core.bean.Loginer" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.oa.core.util.ToNameUtil" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/7/23
  Time: 9:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>年休假确认</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" type="text/css" href="/zjyoa-pc/resources/css/print.css"/>
    <link rel="stylesheet" href="/zjyoa-pc/resources/layuiadmin/layui/css/layui.css" media="all">
    <style>
        iframe{
            width: 100%;
            height: 90%;
        }
    </style>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <%
        Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
        String userid = loginer.getId();
        String  did=loginer.getDid();
        if(userid.equals(ToNameUtil.getName("depttodeptattendance",did))||userid.equals("admin")){
            request.setAttribute("isacce", true);
        }else {
            request.setAttribute("isacce", false);
        }
    %>
</head>
<body>
<div class="layui-tab">
    <ul class="layui-tab-title">

            <li class="layui-this">请假记录</li>
            <li>年休假查看</li>
            <li>年计休假划调整</li>
            <c:if test="${isacce==true}">
                <li>部门请假</li>
                <li>部门出差</li>
            </c:if>
        
    </ul>
    <div class="layui-tab-content">

            <div class="layui-tab-item layui-show">
                <iframe src="/zjyoa-pc/userpage/viewpage/Q2019101400001.do?formid=qjsqjl2019112100001" frameborder="0" class="layadmin-iframe"></iframe>
            </div>
            <div class="layui-tab-item">
                <iframe src="/zjyoa-pc/userpage/viewpage/R2020050800012.do?formid=nxjqd2020050800013" frameborder="0" class="layadmin-iframe"></iframe>
            </div>
            <div class="layui-tab-item">
                <iframe src="/zjyoa-pc/userpage/viewpage/r2020050800001.do?formid=nxjjhdz2020050800001" frameborder="0" class="layadmin-iframe"></iframe>
            </div>
        <c:if test="${isacce==true}">
            <div class="layui-tab-item">
                <iframe src="/zjyoa-pc/userpage/viewpage/Q2019101400001.do?formid=qjsqjl2019112100001" frameborder="0" class="layadmin-iframe"></iframe>
            </div>
            <div class="layui-tab-item">
                <iframe src="/zjyoa-pc/userpage/viewpage/Z2019082300016.do?formid=ccsptz2019041800001" frameborder="0" class="layadmin-iframe"></iframe>
            </div>
        </c:if>
      


    </div>
</div>

<script>
    //注意：选项卡 依赖 element 模块，否则无法进行功能性操作
    layui.use('element', function(){
        var element = layui.element;
    });
</script>
</body>
</html>

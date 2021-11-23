<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/08/31
  Time: 下午 3:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>后台维护</title>
    <script type="text/javascript" src="/resources/js/jquery/jquery-3.3.1.js"></script>
    <link rel="stylesheet" href="/resources/layui/css/layui.css">
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo">后台维护</div>
        <ul class="layui-nav layui-layout-left">
            <li class="layui-nav-item"><a href="/home.do">返回主页</a></li>
        </ul>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <c:if test="${sessionScope.loginer!=null}">
                    <c:if test="${sessionScope.loginer.user}">
                        <a href="javascript:;">
                            ${sessionScope.loginer.name}
                        </a>
                        <dl class="layui-nav-child">
                            <dd><a href="javascript:;" id="modimyemp">基本资料</a></dd>
                            <dd><a href="javascript:;" id="pwupdate">安全设置</a></dd>
                        </dl>
                    </c:if>
                </c:if>
            </li>
            <c:if test="${sessionScope.loginer==null}">
                <li class="layui-nav-item"><a class="btn-sm top-nav" href="login.do">登录</a></li>
            </c:if>
            <c:if test="${sessionScope.loginer!=null}">
                <li class="layui-nav-item"><a class="btn-sm top-nav" href="logout.do">退出</a></li>
            </c:if>
        </ul>
    </div>

    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
            <ul class="layui-nav layui-nav-tree" lay-shrink="all" lay-filter="test">
                <li class="layui-nav-item layui-nav-itemed">
                    <a class="" href="javascript:;">用户管理</a>
                    <dl class="layui-nav-child">
                        <dd><a href="/user/gotousermanager.do" target="option">人员管理</a></dd>
                        <dd><a href="/role/roledefines.do" target="option">角色管理</a></dd>
                            <dd><a href="/myurl/myurlregist.do" target="option">菜单管理</a></dd>
                        <dd><a href="/access/accessrights.do"  target="option">权限管理</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;">基础数据管理</a>
                    <dl class="layui-nav-child">
                        <dd><a href="dictionary/fieldlist.do" target="option">字段定义</a></dd>
                        <dd><a href="dictionary/tablelist.do" target="option">表定义</a></dd>
                        <dd><a href="dictionary/tasklist.do" target="option">任务定义</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;">系统设置</a>
                    <dl class="layui-nav-child">
                        <dd><a href="config/configoapage.do?type=qt" target="option">系统设置</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;">定制开发</a>
                    <dl class="layui-nav-child">
                        <dd><a href="/form_custom/form_custom_made.do" target="option">表单定制</a></dd>
                        <dd><a href="/workflow/newworkflow.do" target="option">流程定制</a></dd>
                        <dd><a href="/workflow/workflowproc.do?id=rcsp20180925000001" target="option">流程查看</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;">消息通知</a>
                    <dl class="layui-nav-child">
                        <dd><a href="/userpage/viewpage/Z2018101600001.do?formid=xxtz2018101600001" target="option">消息通知</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;">文件管理</a>
                    <dl class="layui-nav-child">
                        <dd><a href="/filetype/gotofiletype.do" target="option">文件类型管理</a></dd>
                    </dl>
                </li>
            </ul>
        </div>
    </div>
    <div class="layui-body" id="rigthPage">
        <div style="padding: 15px;height:95%">
            <iframe id="option" name="option" src="" style="overflow: visible;" scrolling="aoto" frameborder="no" width="100%" height="100%"></iframe>
        </div>
    </div>
</div>
<script src="/resources/layui/layui.js"></script>
<script>
    //JavaScript代码区域
    layui.use('element', function () {
        var element = layui.element;

    });
    var PageClick = function (menuUrl) {
        $("#rigthPage").load(menuUrl);
    };
</script>
</body>
</html>

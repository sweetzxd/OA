<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/09/27
  Time: 上午 9:46
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
    <title>我的任务</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
</head>
<body>
<table class="layui-hide" lay-filter="tasksender"
       lay-data="{height: 'full-100',cellMinWidth: 100,toolbar:'true',defaultToolbar: ['filter']}">
    <thead>
    <tr>
        <%--<th lay-data="{field:'workOrderNO',width:220, fixed: 'left',sort: true}">任务主键</th>--%>
        <th lay-data="{field:'taskTitle',toolbar: '#listbar'}">任务名称</th>
        <th lay-data="{field:'modifyName',width:220, sort: true}">发出人</th>
        <th lay-data="{field:'modifyTime',width:220,fixed: 'right'}">发送时间</th>
    </tr>
    </thead>
</table>
<script type="text/html" id="listbar">
<%--
    <a href="javascript:;" onclick="parent.addtab('{{d.refLinkUrl}}','{{d.workOrderNO}}','任务处理')" class="nav-active">{{d.taskTitle}}</a>
--%>
    <a href="javascript:;" onclick="window.location.href='{{d.refLinkUrl}}'" class="nav-active">{{d.taskTitle}}</a>
</script>
<div class="layui-form">
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">检索：</label>
        </div>
        <div class="layui-inline">
            <div class="layui-input-inline">
                <select id="option" name="option">
                    <option value="taskTitle">任务名称</option>
                    <option value="modifyName">发出人</option>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <div class="layui-input-inline">
                <input class="layui-input" id="inputval" name="inputval" autocomplete="off">
            </div>
        </div>
        <div class="layui-inline">
            <a class="layui-btn layui-btn-xs"  onclick="selectss()">搜索</a>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
    var tableOptions = {
        url: '/task/selectuser.do',
        method: 'POST',
        id: 'tasksender',
        page: true,
        limit: 10
    };
    var tabless;
    layui.use(['form', 'layer', 'table'], function () {
        var form = layui.form;
        var layer = layui.layer;
        var table = layui.table;
        form.render('select');
        tabless= table.init('tasksender', tableOptions);

    });
    function selectss() {
        tabless.reload("tasksender", {
            where: {
                option: jQuery("#option  option:selected").val(),
                inputval: $("#inputval").val(),
            }
        });
    }
</script>
</html>

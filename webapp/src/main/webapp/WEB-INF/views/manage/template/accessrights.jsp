<%--
  User: zxd
  Date: 2019/04/24
  Time: 下午 2:57
  Explain: 说明
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <meta charset="utf-8">
    <title>权限管理页面</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
</head>
<body class="layui-layout-body">
<form class="layui-form">
    <div class="layui-form-item">
        <label class="layui-form-label">查看</label>
        <div class="layui-input-block">
            <input type="checkbox" id="acc_info" name="acc_info" lay-skin="switch" lay-text="ON|OFF" <c:if test="${data.acc_info !='0'}">checked</c:if>>
        </div>
    </div>
    <c:if test="${data.formType!=0}">
        <div class="layui-form-item">
            <label class="layui-form-label">添加</label>
            <div class="layui-input-block">
                <input type="checkbox" id="acc_add" name="acc_add" lay-skin="switch" lay-text="ON|OFF" <c:if test="${data.acc_add !='0'}">checked</c:if>>
            </div>
        </div>
    </c:if>
    <c:if test="${data.formType!=0}">
        <div class="layui-form-item">
            <label class="layui-form-label">修改</label>
            <div class="layui-input-block">
                <input type="checkbox" id="acc_modi" name="acc_modi" lay-skin="switch" lay-text="ON|OFF" <c:if test="${data.acc_modi !='0'}">checked</c:if>>
            </div>
        </div>
    </c:if>
    <c:if test="${data.formType!=0}">
        <div class="layui-form-item">
            <label class="layui-form-label">删除</label>
            <div class="layui-input-block">
                <input type="checkbox" id="acc_delete" name="acc_delete" lay-skin="switch" lay-text="ON|OFF" <c:if test="${data.acc_delete !='0'}">checked</c:if>>
            </div>
        </div>
    </c:if>
    <c:if test="${data.formType!=0}">
        <div class="layui-form-item">
            <label class="layui-form-label">导入</label>
            <div class="layui-input-block">
                <input type="checkbox" id="acc_import" name="acc_import" lay-skin="switch" lay-text="ON|OFF" <c:if test="${data.acc_import !='0'}">checked</c:if>>
            </div>
        </div>
    </c:if>
    <c:if test="${data.formType!=0}">
        <div class="layui-form-item">
            <label class="layui-form-label">导出</label>
            <div class="layui-input-block">
                <input type="checkbox" id="acc_export" name="acc_export" lay-skin="switch" lay-text="ON|OFF" <c:if test="${data.acc_export !='0'}">checked</c:if>>
            </div>
        </div>
    </c:if>
    <c:if test="${data.formType!=0}">
        <div class="layui-form-item">
            <label class="layui-form-label">发送</label>
            <div class="layui-input-block">
                <input type="checkbox" id="acc_send" name="acc_send" lay-skin="switch" lay-text="ON|OFF" <c:if test="${data.acc_info !='0'}">checked</c:if>>
            </div>
        </div>
    </c:if>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit="" lay-filter="save">立即提交</button>
        </div>
    </div>
</form>
<jsp:include page="/common/js.jsp"></jsp:include>
<script type="text/javascript">
    layui.use(['form', 'layedit', 'laydate'], function () {
        var form = layui.form;
        form.render();
        //监听提交
        form.on('submit(save)', function (data) {
            var jsons = JSON.stringify(data.field);
            $.ajax({
                type: "POST",
                url: "/access/insertaccess.do",
                contentType: "application/json; charset=utf-8",
                data: {datas: jsons},
                dataType: "html",
                success: function (data) {
                    layer.closeAll();
                }
            });
            return false;
        });
    });
</script>
</body>
</html>

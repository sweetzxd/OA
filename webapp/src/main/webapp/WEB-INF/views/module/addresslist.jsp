<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/10/29
  Time: 下午 5:16
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
    <title>通讯录</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript">
        function starscript(){
            layui.use(['form','element','table','tree'], function(){
                var element = layui.element;
                var form = layui.form;
                var table = layui.table;
                form.render();
                element.init();
                var createTree = function () {
                    var node = "";
                    $.ajax({
                        type: "POST",
                        url: "/department/depttree.do",
                        contentType: "application/json; charset=utf-8",
                        dataType: "html",
                        async: false,
                        success: function (data) {
                            node = eval(data);
                        },
                        error: function (message) {
                            layer.msg("组织结构获取失败！");
                        }
                    });
                    return node;
                };
                layui.tree({
                    elem: '#depttree'
                    , target: '_blank'
                    , nodes: createTree()
                    , click: function (item) {
                        var id = item.id;
                        console.log(id)
                        table.reload("EmployeesTable", {
                            where: {
                                option: "department",
                                inputval: id,
                            }
                        });
                    }
                });
                table.init('EmployeesTable', {
                    url: '/employees/select.do',
                    method: 'POST',
                    id: 'EmployeesTable',
                    page: true,
                    limit: 10
                });
            });
        }
    </script>
</head>
<body onload="starscript()">
<div class="layui-side" style="margin-top: 30px">
    <div class="layui-side-scroll">
        <ul id="depttree"></ul>
    </div>
</div>
<div class="layui-body" id="rigthPage">
    <div style="padding: 15px;height:95%">
        <table class="layui-hide" lay-filter="EmployeesTable"
               lay-data="{id:'EmployeesTable',height: 'full-100',cellMinWidth: 100,toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
            <thead>
            <tr>
                <th lay-data="{type:'numbers', fixed: 'left',field:'staffId'}">序号</th>
                <th lay-data="{field:'staffName', sort: true}">员工姓名</th>
                <th lay-data="{field:'department',templet:function(d){return oa.decipher('dept',d.department);}}">部门</th>
                <th lay-data="{field:'phone', sort: true}">联系电话</th>
                <th lay-data="{field:'mailbox'}">邮箱地址</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
</body>
</html>

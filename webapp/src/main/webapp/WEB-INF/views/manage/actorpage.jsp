<%--
  Created by IntelliJ WebSocketListenerEA.
  User: Administrator
  Date: 2018/10/09
  Time: 下午 3:20
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
    <title>权限选择</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/resources/css/flowpage.css" type="text/css"/>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript">
        var dag = window.parent;

        function starscript() {
            layui.use(['form', 'table', 'layer', 'layedit', 'element'], function () {
                var form = layui.form, table = layui.table, layer = layui.layer, layedit = layui.layedit,
                    element = layui.element;
                form.render('select');
                table.init('roleactor', {
                    height: 255
                    ,page :true
                });
                table.on('tool(roleactor)', function (obj) {
                    var data = obj.data
                    dag.insertActor('roleactor', data.roleId,data.roleTitle);
                });
                element.on('tab(actor)', function () {
                    var tableid = this.getAttribute('lay-id');
                    table.init(tableid, {
                        height: 255
                        ,page :true
                    });
                    table.on('tool(' + tableid + ')', function (obj) {
                        var data = obj.data
                        if(tableid=="rolelist"){
                            dag.insertActor('roleactor', data.roleId,data.roleTitle);
                        }else if(tableid=="useractor"){
                            dag.insertActor('useractor', data.userName,data.name);
                        }else if(tableid=="deptactor"){
                            var val = $('#deptrole').find("option:selected").val();
                            var text = $('#deptrole').find("option:selected").text();
                            dag.insertActor('deptactor', data.deptId+"_"+val,data.deptName+"_"+text);
                        }else if(tableid=="nodeactor"){
                                dag.insertActor('nodeactor', data.actorId,data.actorName);
                        }
                    });
                });
                form.on('select(nodeselect)', function (data) {
                    var id = data.value;
                    if (id != null && id != "") {
                        table.init('nodeactor', {
                            url: '/workflow/selectnodeactor.do?nodeId=' + id,
                            method: 'POST',
                            id: 'nodeactor',
                            height: 255,
                            page: false,
                        });
                    }
                });
            });
        }
    </script>
<body onpaste="return false" onload="starscript()">
<div class="layui-tab layui-tab-card" lay-filter="actor">
    <ul class="layui-tab-title">
        <c:if test="${!empty rolelist}">
            <li class="layui-this" lay-id="rolelist">角色定义</li>
        </c:if>
        <c:if test="${!empty userlist}">
            <li lay-id="useractor">账户角色</li>
        </c:if>
        <c:if test="${!empty deptlist}">
            <li lay-id="deptactor">部门角色</li>
        </c:if>
        <c:if test="${!empty nodeIds}">
            <li lay-id="nodeactor">流程角色</li>
        </c:if>
    </ul>
    <div class="layui-tab-content" style="height: 82%;">
        <c:if test="${!empty rolelist}">
            <div class="layui-tab-item layui-show">
                <table lay-filter="roleactor">
                    <thead>
                    <tr>
                        <th lay-data="{field:'roleId',width:80,toolbar: '#rolebar'}"></th>
                        <th lay-data="{field:'roleTitle'}">角色</th>
                        <th lay-data="{field:'userName'}">角色人员</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${rolelist}" var="role" varStatus="status">
                        <tr>
                            <td>${role.roleId}</td>
                            <td>${role.roleTitle}</td>
                            <td><s:dic type="users" value="${role.userName}"></s:dic></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <script type="text/html" id="rolebar">
                    <div class="layui-btn-container">
                        <a class="layui-btn layui-btn-xs" lay-event="send">选择</a>
                    </div>
                </script>
            </div>
        </c:if>
        <c:if test="${!empty userlist}">
            <div class="layui-tab-item">
                <table lay-filter="useractor">
                    <thead>
                    <tr>
                        <th lay-data="{field:'userName',width:65,toolbar: '#userbar'}">选择</th>
                        <th lay-data="{field:'name'}">姓名</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${userlist}" var="user" varStatus="status">
                        <tr>
                            <td>${user.userName}</td>
                            <td>${user.name}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <script type="text/html" id="userbar">
                    <div class="layui-btn-container">
                        <a class="layui-btn layui-btn-xs" lay-event="send">选择</a>
                    </div>
                </script>
            </div>
        </c:if>
        <c:if test="${!empty deptlist}">
            <div class="layui-tab-item">
                <div class="layui-form">
                    <div class="layui-form-item">
                        <label class="layui-form-label">部门角色</label>
                        <div class="layui-input-block">
                            <select id="deptrole">
                                <option value="deptHead">部门负责人</option>
                                <option value="deputyDeptHead">部门副职</option>
                                <option value="deptAllemp">部门全体人员</option>
                            </select>
                        </div>
                    </div>
                </div>
                <table lay-filter="deptactor">
                    <thead>
                    <tr>
                        <th lay-data="{field:'deptId',width:65,toolbar: '#deptbar'}">选择</th>
                        <th lay-data="{field:'deptName'}">部门名称</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${deptlist}" var="dept" varStatus="status">
                        <tr>
                            <td>${dept.deptId}</td>
                            <td>${dept.deptName}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <script type="text/html" id="deptbar">
                    <div class="layui-btn-container">
                        <a class="layui-btn layui-btn-xs" lay-event="send">选择</a>
                    </div>
                </script>
            </div>
        </c:if>
        <c:if test="${!empty nodeIds}">
            <div class="layui-tab-item">
                <div class="layui-form">
                    <div class="layui-form-item">
                        <label class="layui-form-label">节点</label>
                        <div class="layui-input-block">
                            <select name="nodeselect" lay-filter="nodeselect">
                                <option value=""></option>
                                <c:forEach items="${nodeIds}" var="nodeId" varStatus="status">
                                    <option value="${nodeId[0]}">${nodeId[1]}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <table lay-filter="nodeactor">
                    <thead>
                    <tr>
                        <th lay-data="{field:'actorId',width:65,toolbar: '#nodebar'}">选择</th>
                        <th lay-data="{field:'actorName'}">字段名称</th>
                    </tr>
                    </thead>
                </table>
                <script type="text/html" id="nodebar">
                    <div class="layui-btn-container">
                        <a class="layui-btn layui-btn-xs" lay-event="send">选择</a>
                    </div>
                </script>
            </div>
        </c:if>
    </div>
</div>
</body>
</html>

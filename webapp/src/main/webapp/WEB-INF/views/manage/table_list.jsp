<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/08/07
  Time: 上午 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <jsp:include page="/common/var.jsp"></jsp:include>
    <jsp:include page="/common/meta.jsp"></jsp:include>
    <title>用户列表</title>
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
    <style>
        .table-div-outer{
            border:1px #e6e6e6 solid !important;
            min-width: 300px;
            height:500px;
        }
        .table-div-in{
            width:100%;
            height:100%;
            overflow-y:scroll;
            text-align:left;
        }
        .table-field-p {
            padding-left:20px;
            text-align:left;
        }
        .table-field-p span {
            text-decoration: none !important;
            float:right;
            padding-right:20px;
            text-align: left;
            color:#1E9FFF;
        }
        .table-field-p a {
            text-decoration: none !important;
            float:right;
            padding-right:20px;
            text-align: left;
            color:#1E9FFF;
        }
    </style>
</head>
<body>
<c:if test="${type=='list'}">
    <table class="layui-hide" lay-filter="tableTable" lay-data="{id:'tableTable',height: 'full-100',cellMinWidth: 150,toolbar:'#toolbar'}">
        <thead>
        <tr>
            <th lay-data="{field:'tableName', width:150}">数据表</th>
            <th lay-data="{field:'tableTitle', width:180}">表名称</th>
            <th lay-data="{field:'tableField'}">表字段</th>
            <th lay-data="{field:'modTime', width:180, templet:function(d){return oa.decipher('datetime',d.modTime);}}">修改时间</th>
            <th lay-data="{fixed:'right', width:110, align:'center', toolbar: '#listbar'}">管理</th>
        </tr>
        </thead>
    </table>
    <script type="text/html" id="listbar">
        <div class="layui-btn-container">
            <a class="layui-btn layui-btn-xs" lay-event="modi">修改</a>
        </div>
    </script>
    <script type="text/html" id="toolbar">
        <div class="layui-btn-container">
            <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="add">添加</a>
            <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="load">刷新内存</a>
        </div>
    </script>
    <div class="layui-form">
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">检索：</label>
            </div>
            <div class="layui-inline">
                <div class="layui-input-inline">
                    <select id="option" name="option">
                        <option value="tableTitle">表名称</option>
                    </select>
                </div>
            </div>
            <div class="layui-inline">
                <div class="layui-input-inline">
                    <input class="layui-input" id="inputval" name="inputval" autocomplete="off">
                </div>
            </div>
            <div class="layui-inline">
                <a class="layui-btn layui-btn-xs" lay-submit lay-filter="reload">搜索</a>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        layui.use(['table', 'form', 'element'], function () {
            var table = layui.table;
            var form = layui.form;
            var element = layui.element;
            table.init('tableTable', {
                url: "/dictionary/selecttable.do",
                method: 'POST',
                id: 'tableTable',
                page: true,
                limit: 14
            });
            table.on('toolbar(tableTable)', function (obj) {
                var checkStatus = table.checkStatus(obj.config.id);
                var data = checkStatus.data;
                var othis = $(this);
                var method = othis.data('method');
                switch (obj.event) {
                    case 'add':
                        oa.gotourl("/dictionary/inserttable.do");
                        break;
                    case 'load':
                        $.post("/dictionary/loadtable.do");
                        break;
                }
            });
            table.on('tool(tableTable)', function (obj) {
                var data = obj.data
                console.log(data)
                switch (obj.event) {
                    case 'modi':
                        var tableName = data.tableName;
                        oa.gotourl("/dictionary/tablemodi/"+tableName+".do");
                        break;
                    case 'del':
                        //empDelete(data.staffId);
                        break;
                }
            });
            form.render();
            form.on('submit(reload)', function (data) {
                table.reload("tableTable", {
                    where: {
                        option: jQuery("#option  option:selected").val(),
                        inputval: $('#inputval').val(),
                    }
                });
                return false;
            });
        });

    </script>
</c:if>
<c:if test="${type!='list'}">
    <div class="layui-fluid">
        <div class="layui-row layui-col-space10">
            <div class="layui-col-xs4" style="margin-top: 10px;">
                <form class="layui-form layui-form-pane" id="tableform" action="/dictionary/savetable/${type}.do" method="post" enctype="application/x-www-form-urlencoded">
                    <div class="layui-form-item">
                        <label class="layui-form-label">数据表</label>
                        <div class="layui-input-block">
                            <input type="text" class="layui-input" name="tableName" value="${tableinfo.tableName}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">数据表名称</label>
                        <div class="layui-input-block">
                            <input type="text" class="layui-input" name="tableTitle" value="${tableinfo.tableTitle}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">表内字段</label>
                        <div class="layui-input-block">
                            <div class="table-div-outer" id="tableField">
                                <div class="table-div-in">
                                    <c:if test="${!empty tablefieldlist}">
                                        <c:forEach items="${tablefieldlist}" var="field" varStatus="status">
                                            <p class="table-field-p" data-id="${field.fieldName}">${field.fieldTitle}<span>[${field.fieldName}]</span></p>
                                        </c:forEach>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                    <input type="hidden" id="instableField" name="tableField" value="${tableinfo.tableField}"/>
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="formsubmit">立即提交</button>
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="layui-col-xs8">

                <table class="layui-hide" lay-filter="fieldTable" lay-data="{id:'fieldTable',height: 'full-100',cellMinWidth: 150}">
                    <thead>
                    <tr>
                        <th lay-data="{width:110, align:'center', toolbar: '#fieldbar'}">管理</th>
                        <th lay-data="{field:'fieldName'}">字段名</th>
                        <th lay-data="{field:'fieldTitle'}">字段名称</th>
                        <th lay-data="{field:'fieldType', templet:function(d){return oa.getTypeName(d.fieldType);}}">字段类型</th>
                        <th lay-data="{field:'modTime', templet:function(d){return oa.decipher('datetime',d.modTime);}}">修改时间</th>
                    </tr>
                    </thead>
                </table>
                <script type="text/html" id="fieldbar">
                    <div class="layui-btn-container">
                        <a class="layui-btn layui-btn-xs" lay-event="insert">插入</a>
                    </div>
                </script>
                <div class="layui-form">
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">检索：</label>
                        </div>
                        <div class="layui-inline">
                            <div class="layui-input-inline">
                                <select id="foption" name="option">
                                    <option value="fieldTitle">字段名称</option>
                                    <option value="fieldType">字段类型</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <div class="layui-input-inline">
                                <input class="layui-input" id="finputval" name="inputval" autocomplete="off">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <a class="layui-btn layui-btn-xs" lay-submit lay-filter="reload">搜索</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        layui.use(['table', 'form', 'element'], function () {
            var table = layui.table;
            var form = layui.form;
            var element = layui.element;
            form.render();
            table.init('fieldTable', {
                url: "/dictionary/selectfield.do",
                method: 'POST',
                id: 'fieldTable',
                page: true,
                limit: 14
            });
            table.on('tool(fieldTable)', function (obj) {
                var data = obj.data
                var val = $("#instableField").attr("value");
                if(val.indexOf(data.fieldName)<0){
                    var span = "<p class='table-field-p' data-id='"+data.fieldName+"'>"+data.fieldTitle+" ["+data.fieldName+"]<a href='javascript:void(0)' onclick='removeField(this)'>删除</a></p>";
                    $("#tableField").find(".table-div-in").append(span);
                    val = val + data.fieldName + ";";
                    $("#instableField").attr("value", val);
                }else{
                    layer.msg("所选字段已存在");
                }
            });
            form.on('submit(reload)', function (data) {
                table.reload("fieldTable", {
                    where: {
                        option: jQuery("#foption  option:selected").val(),
                        inputval: $('#finputval').val(),
                    }
                });
                return false;
            });
            form.on('submit(formsubmit)', function(data){
                $('#tableform').submit();
                return false;
            });
        });
        function removeField(obj){
            $(obj).parent().remove();
            var val = "";
            $("#tableField").find(".table-div-in p").each(function() {
                val += $(this).attr("data-id") + ";";
            });
            $("#instableField").attr("value", val);
        }
    </script>
</c:if>
</body>
</html>
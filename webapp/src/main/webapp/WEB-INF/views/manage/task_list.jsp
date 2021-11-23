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
    <title>任务定义</title>
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
    <%--<script type="text/javascript" src="/resources/js/admin/tasklist.js"></script>--%>
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
            min-height: 20px;
            margin: 5px;
            padding-left:20px;
            text-align:left;
            background-color: #e6e6e6;
        }
        .table-field-p a {
            text-decoration: none !important;
            float:right;
            padding-right:10px;
            text-align: left;
            color:#1E9FFF;
        }
    </style>
</head>
<body>
<c:if test="${type=='list'}">
    <table class="layui-hide" lay-filter="taskTable" lay-data="{id:'taskTable',height: 'full-100',cellMinWidth: 150,toolbar:'#toolbar'}">
        <thead>
        <tr>
            <th lay-data="{field:'taskName', width:150}">任务主键</th>
            <th lay-data="{field:'taskTitle', width:180}">任务名称</th>
            <th lay-data="{field:'taskType', width:80}">类型</th>
            <th lay-data="{field:'tableName', width:180}">数据表</th>
            <th lay-data="{field:'taskField'}">任务字段</th>
            <th lay-data="{field:'modTime', width:180, templet:function(d){return oa.decipher('datetime',d.modTime);}}">修改时间</th>
            <th lay-data="{fixed:'right', width:110, align:'center', toolbar: '#listbar'}">管理</th>
        </tr>
        </thead>
    </table>
    <script type="text/html" id="listbar">
        <div class="layui-btn-container">
            <a class="layui-btn layui-btn-xs" lay-event="modi">修改</a>
            <a class="layui-btn layui-btn-xs" lay-event="del">删除</a>
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
                        <option value="taskTitle">任务名称</option>
                        <option value="taskName">任务主键</option>
                        <option value="taskType">任务类型</option>
                        <option value="tableName">数据表</option>
                        <option value="taskField">任务字段</option>
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
            table.init('taskTable', {
                url: "/dictionary/selecttask.do",
                method: 'POST',
                id: 'taskTable',
                page: true,
                limit: 14
            });
            table.on('toolbar(taskTable)', function (obj) {
                var checkStatus = table.checkStatus(obj.config.id);
                var data = checkStatus.data;
                var othis = $(this);
                var method = othis.data('method');
                switch (obj.event) {
                    case 'add':
                        oa.gotourl("/dictionary/gotoinserttask.do");
                        break;
                    case 'load':
                        $.post("/dictionary/loadtask.do");
                        break;
                }
            });
            table.on('tool(taskTable)', function (obj) {
                var data = obj.data
                console.log(data)
                switch (obj.event) {
                    case 'modi':
                        oa.gotourl("/dictionary/gotomoditask.do?taskName="+data.taskName);
                        break;
                    case 'del':
                        $.ajax({
                            type: "POST",//方法类型
                            url: "/dictionary/deltask/"+data.taskName+".do",
                            contentType: "application/x-www-form-urlencoded; charset=utf-8",
                            dataType: "html",
                            cache: false,
                            success: function (data) {
                                console.log(data);
                                if (data == "1") {
                                    oa.gotourl("/dictionary/tasklist.do");
                                } else if (data == "0") {
                                    layer.msg("删除失败");
                                }
                            },
                            error: function (error) {
                                console.log(error);
                                layer.msg("系统错误");
                            }
                        });
                        break;
                }
            });
            form.render();
            form.on('submit(reload)', function (data) {
                table.reload("taskTable", {
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
<c:if test="${type!='list' and type!='table'}">
    <div class="layui-fluid">
        <div class="layui-row layui-col-space10">
            <div class="layui-col-xs4" style="margin-top: 10px;">
                <form class="layui-form layui-form-pane" id="taskform" action="/dictionary/savetask/${type}.do" method="post" enctype="application/x-www-form-urlencoded">
                    <div class="layui-form-item">
                        <label class="layui-form-label">任务主键</label>
                        <div class="layui-input-block">
                            <input type="text" class="layui-input" name="taskName" value="${task.taskName}" <c:if test="${type=='modi'}">readonly unselectable="on" </c:if>>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">任务名称</label>
                        <div class="layui-input-block">
                            <input type="text" class="layui-input" name="taskTitle" value="${task.taskTitle}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">任务表</label>
                        <div class="layui-input-block">
                            <input type="text" class="layui-input" id="t_tableName" name="tableName" value="${task.tableName}" readonly unselectable="on" >
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">选择框</label>
                        <div class="layui-input-block">
                            <select name="taskType" lay-verify="required">
                                <option value="表单" <c:if test="${task.taskType=='表单'}">selected</c:if>>表单</option>
                                <option value="列表" <c:if test="${task.taskType=='列表'}">selected</c:if>>列表</option>
                                <option value="查询" <c:if test="${task.taskType=='查询'}">selected</c:if>>查询</option>
                                <option value="携带" <c:if test="${task.taskType=='携带'}">selected</c:if>>携带</option>
                            </select>
                        </div>
                    </div>

                    <div class="layui-form-item layui-form-text">
                        <label class="layui-form-label">表内字段</label>
                        <div class="table-div-outer" id="taskField">
                            <div class="table-div-in">
                                <c:if test="${!empty taskfieldlist}">
                                    <c:forEach items="${taskfieldlist}" var="tfield" varStatus="status">
                                        <p class="table-field-p" data-id="${tfield.fieldName}" data-name="${tfield.fieldTitle}">${tfield.fieldTitle}
                                            <a class="del" href="javascript:" onclick="del(this)">[删除]</a>
                                            <a class="down" href="javascript:" onclick="down(this)">[下移]</a>
                                            <a class="up" href="javascript:" onclick="up(this)">[上移]</a>
                                        </p>
                                    </c:forEach>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <input type="hidden" id="instableField" name="taskField" value="${task.taskField}"/>
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="formsubmit">立即提交</button>
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="layui-col-xs4 layui-form layui-form-pane" style="margin-top: 10px;">
                <div class="layui-form-item">
                    <label class="layui-form-label">数据表</label>
                    <div class="layui-input-block">
                        <c:if test="${type=='ins'}">
                            <input type="text" class="layui-input" id="tableName" name="tableName" value="" onclick="openselect()" readonly unselectable="on" >
                        </c:if>
                        <c:if test="${type=='modi'}">
                            <input type="text" class="layui-input" name="tableName" value="${table.tableName}" readonly unselectable="on" >
                        </c:if>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">数据表名称</label>
                    <div class="layui-input-block">
                        <input type="text" class="layui-input" id="tableTitle" name="tableTitle" value="${table.tableTitle}" readonly unselectable="on" >
                    </div>
                </div>
                <div class="layui-form-item  layui-form-text">
                    <label class="layui-form-label">表内字段</label>
                    <div class="table-div-outer" id="tableField">
                        <div class="table-div-in">
                            <c:if test="${!empty tablefieldList}">
                                <c:forEach items="${tablefieldList}" var="field" varStatus="status">
                                    <p class="table-field-p" data-id="${field.fieldName}" data-name="${field.fieldTitle}">${field.fieldTitle}
                                        <a class="add" href="javascript:" onclick="add(this)">[添加]</a>
                                    </p>
                                </c:forEach>
                            </c:if>
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
            form.on('submit(formsubmit)', function(data){
                $('#taskform').submit();
                return false;
            });
        });

        function up(obj) {
            var p = $(obj).parents("p");
            if (p.index() != 0) {
                p.prev().before(p);
            }
            var len = $("#taskField").find(".table-div-in p");
            var val = "";
            len.each(function() {
                val += $(this).attr("data-id") + ";";
            });
            $("#instableField").attr("value", val);
        }

        function down(obj) {
            var len = $("#taskField").find(".table-div-in p");
            var p = $(obj).parents("p");
            if (p.index() != len - 1) {
                p.next().after(p);
            }
            var val = "";
            len.each(function() {
                val += $(this).attr("data-id") + ";";
            });
            $("#instableField").attr("value", val);
        }

        function add(obj){
            $(obj).parent().remove();
            var id = $(obj).parent().attr("data-id");
            var name = $(obj).parent().attr("data-name");

            var del = "<a class='del' href='javascript:' onclick='del(this)'>[删除]</a>";
            var down = "<a class='down' href='javascript:' onclick='down(this)'>[下移]</a>";
            var up = "<a class='up' href='javascript:' onclick='up(this)'>[上移]</a>";
            var span = "<p class='table-field-p' data-id='"+id+"' data-name='"+name+"'>"+name+del+down+up+"</p>";
            $("#taskField").find(".table-div-in").append(span);

        }
        function del(obj){
            $(obj).parent().remove();
            var id = $(obj).parent().attr("data-id");
            var name = $(obj).parent().attr("data-name");

            var add = "<a class='add' href='javascript:' onclick='add(this)'>[添加]</a>";
            var span = "<p class='table-field-p' data-id='"+id+"' data-name='"+name+"'>"+name+add+"</p>";
            $("#tableField").find(".table-div-in").append(span);

            var len = $("#taskField").find(".table-div-in p");
            var val = "";
            len.each(function() {
                val += $(this).attr("data-id") + ";";
            });
            $("#instableField").attr("value", val);
        }
        this.index = "";
        function openselect(){
            index = layer.open({
                type: 2 //此处以iframe举例
                , id: 'myOpenWindow'
                , title: "选择任务表"
                , area: ['600px', '500px']
                , maxmin: true
                , content: "/dictionary/gototableselect.do"
                , btn: ['关闭']
                , yes: function (active) {
                    layer.closeAll();
                }
            });
            console.log(index);
        }
        function updateinput(data){
            var tableName = data.tableName;
            $("#tableName").attr("value", tableName);
            $("#t_tableName").attr("value", tableName);
            var tableTitle = data.tableTitle;
            $("#tableTitle").attr("value", tableTitle);
            $("#tableField").find(".table-div-in").empty();
            var field = data.tableField;
            var val = "";
            if(field!=null && field.length>1){
                var str = field.split(';');
                for(var i = 0,len = str.length-1; i < len; i++){
                    val += str[i] + ";";
                    var name = oa.dataidtoname("field",str[i]);
                    var add = "<a class='add' href='javascript:' onclick='add(this)'>[添加]</a>";
                    var span = "<p class='table-field-p' data-id='"+str[i]+"' data-name='"+name+"'>"+name+add+"</p>";
                    $("#tableField").find(".table-div-in").append(span);
                }
            }
            $("#instableField").attr("value", val);
            layer.close(index);
        }
    </script>
</c:if>
<c:if test="${type=='table'}">
    <div style="padding: 5px;">
        <table class="layui-hide" lay-filter="tableselect" lay-data="{id:'tableselect', height: 'full-40',cellMinWidth: 50,toolbar: '#tablebtn'}">
            <thead>
            <tr>
                    ${thead}
            </tr>
            </thead>
        </table>
    </div>
    <script type="text/html" id="tablebtn">
        <div class="layui-btn-container">
            <a data-method="titlebtn" class="layui-btn layui-btn-xs" onclick="selectvalue()">选择</a>
        </div>
    </script>
    <script>
        var dag = window.parent;
        layui.use(['layer', 'table'], function () {
            var layer = layui.layer;
            var table = layui.table;
            table.init('tableselect', {
                url: '${url}',
                method: 'POST',
                id: 'tableselect',
                page: true,
                limit: 5,
            });
        });
        function selectvalue (){
            var checkStatus = layui.table.checkStatus('tableselect');
            var data = checkStatus.data;
            dag.updateinput(data[0]);
        }
    </script>
</c:if>
</body>
</html>
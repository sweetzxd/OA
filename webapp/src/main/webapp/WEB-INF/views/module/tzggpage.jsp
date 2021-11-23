<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/08/07
  Time: 上午 11:05
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
    <title>通知公告</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/resources/css/flowpage.css" type="text/css"/>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>


</head>

<body >
<table class="layui-hide" lay-filter="tzggpage"
       lay-data="{height: 'full-100',cellMinWidth: 100,even:'false',toolbar:'#toolbar',defaultToolbar: ['filter']}">
    <thead>
    <tr>
        <th lay-data="{field:'tzgg181110001_num',width: '10%',align: 'center'}">序号</th>
        <th lay-data="{field:'tzgg181110001_bt18111000001',width: '30%',align: 'center'}">标题</th>
        <th lay-data="{field:'tzgg181110001_nr18111000001',width: '30%',align: 'center'}">内容</th>
        <th lay-data="{field:'tzgg181110001_recordTime',width: '30%',align: 'center'}">时间</th>
    </tr>
    </thead>
</table>
<input id="type" type="hidden" value="${type}">
<script type="text/html" id="toolbar">
            <c:if test="${type==1}">
                <input type="radio" name="tzggtype" lay-filter="tzgg" value="1" id="tz" title="通知" checked>
                <input type="radio" name="tzggtype" lay-filter="tzgg" value="2" id="gg" title="公告">
            </c:if>
            <c:if test="${type==2}">
                <input type="radio" name="tzggtype" lay-filter="tzgg" value="1" id="tz" title="通知">
                <input type="radio" name="tzggtype" lay-filter="tzgg" value="2" id="gg" title="公告" checked>
            </c:if>
</script>
<div class="layui-form">
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">检索：</label>
        </div>
        <div class="layui-inline">
            <div class="layui-input-inline">
                <select id="option" name="option">
                    <option value="bt18111000001" selected>标题</option>
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
<jsp:include page="/common/js.jsp"></jsp:include>

<script type="text/javascript">
    var tableid = 'tzggpage';
    layui.use(['table','form','laydate','layer'], function () {
        var table = layui.table;
        var form = layui.form   ;
        var laydate = layui.laydate;
        var layer = layui.layer;
        laydate.render({
            elem: '#startDate'
            , value: oa.getdate()
        });
        laydate.render({
            elem: '#endDate'
        });
        form.render();
        table.init('tzggpage', {
            url: '/commonurl/selectAllTzgg.do',
            method: 'POST',
            id: 'tzggpage',
            page: true,
            limit: 10,
            where: {
                type:'${type}'
            }
        });
        var reloadTable = function () {
            table.reload("tzggpage", { //此处是上文提到的 初始化标识id
                where: {
                    option: jQuery("#option  option:selected").val(),
                    inputval: $('#inputval').val(),
                    type:$("input[type='radio']:checked").val()
                }
                ,page: {
                    curr: 1
                }
            });
            if($("#type").val()==1){
                $("#gg").removeAttr('checked');
                $("#tz").attr("checked", true);
            }else if($("#type").val()==2){
                $("#tz").removeAttr('checked');
                $("#gg").attr("checked", true);
            }
        };
        form.on('submit(reload)', function (data) {
            reloadTable();
            return false;
        });

        form.on('radio(tzgg)', function (data) {
            table.reload("tzggpage", { //此处是上文提到的 初始化标识id
                where: {
                    option: jQuery("#option  option:selected").val(),
                    inputval: "",
                    type:data.value
                }
                ,page: {
                    curr: 1
                }
            });
            if(data.value==1){
                $("#gg").removeAttr('checked');
                $("#tz").attr("checked", true);
                $("#type").val(1);
            }else if(data.value==2){
                $("#tz").removeAttr('checked');
                $("#gg").attr("checked", true);
                $("#type").val(2);
            }
        });

    });

</script>
</body>
</html>
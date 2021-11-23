<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/24
  Time: 上午 11:49
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
    <title>节假日管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>


</head>

<c:if test="${type=='list'}">
<body >
<table class="layui-hide" lay-filter="festival"
       lay-data="{height: 'full-100',cellMinWidth: 100,even:'false',toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
    <thead>
    <tr>
        <th lay-data="{templet: '#indexTpl',width:100,align: 'center'}">序号</th>
        <th lay-data="{field:'typeStr',align: 'center',width:150}">类型</th>
        <th lay-data="{field:'festivalName',align: 'center'}">节假日名称</th>
        <th lay-data="{field:'startTime',align: 'center', sort: true}">开始时间</th>
        <th lay-data="{field:'endTime',align: 'center', sort: true}">结束时间</th>
        <th lay-data="{field:'operation',width:200}">操作</th>
    </tr>
    </thead>
</table>
<script type="text/html" id="toolbar">
    <div class="layui-btn-container">
        <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="add">添加</a>
    </div>
</script>
<script type="text/html" id="indexTpl">
    {{d.LAY_TABLE_INDEX+1}}
</script>
<div class="layui-form">
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">检索：</label>
        </div>
        <div class="layui-inline">
            <div class="layui-input-inline">
                <select id="option" name="option" lay-filter="option">
                    <option value="festivalName" selected>节假日名称</option>
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
</c:if>
<c:if test="${type!='list'}">
<body>
<fieldset class="layui-elem-field">
    <legend>节假日内容</legend>
    <form class="layui-form layui-form-pane" id="festivalForm">
        <input type="hidden" name="festivalId" value="${festival.festivalId}">
        <div class="layui-container">
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">节假日名称</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <input type="text" id="festivalName" name="festivalName" required="" autocomplete="off" class="layui-input" value="${festival.festivalName}" <c:if test="${type=='edit' and festival.type!=3}">readonly</c:if>>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="scheduletTitle_Explain"></p>
                    </div>
                </div>
            </div>
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">开始时间</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <input type="text" id="startTime" name="startTime" autocomplete="off" required="" class="layui-input datetime" value="" toLater="endTime">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="startTime_Explain"></p>
                    </div>
                </div>
            </div>
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">结束时间</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <input type="text" id="endTime" name="endTime" autocomplete="off" required="" class="layui-input datetime" value="" laterTo="startTime">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="endTime_Explain"></p>
                    </div>
                </div>
            </div>
        </div>
    </form>
</fieldset>
</c:if>
<script src="/resources/js/system/manager.js" charset="utf-8"></script>

<script type="text/javascript">
    var tableid = 'festival';
    layui.use(['table','form', 'laydate'], function () {
        var table = layui.table;
        var form = layui.form;
        var laydate = layui.laydate;

        table.init('festival', {
            url: '/festival/selectlist.do',
            method: 'POST',
            id: 'festival',
            page: true,
            limit: 10
        });
        //日期
        laydate.render({
            elem: '#startTime'
            , format: 'yyyy-MM-dd'
            , type: 'date'
            , value: '${festival.startTime}'
            , trigger: 'click'
            , calendar: true
            , done: function (value) {
                $("#startTime").val(value);
                $(".layui-form").validate().element($("#startTime"));
            }
        });
        laydate.render({
            elem: '#endTime'
            , format: 'yyyy-MM-dd'
            , type: 'date'
            , value: '${festival.endTime}'
            , trigger: 'click'
            , calendar: true
            , done: function (value) {
                $("#endTime").val(value);
                $(".layui-form").validate().element($("#endTime"));
            }
        });
        form.render();
        var reloadTable = function () {
            table.reload("festival", { //此处是上文提到的 初始化标识id
                where: {
                    option: jQuery("#option  option:selected").val(),
                    inputval: $('#inputval').val(),
                }
            });
        };
        form.on('submit(reload)', function (data) {
            reloadTable();
            return false;
        });
        table.on('toolbar(' + tableid + ')', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            var othis = $(this);
            var method = othis.data('method');

            switch (obj.event) {
                case 'add':
                    //window.location.href = "/festival/gotofestival.do?type=add";
                    var active = opedit(othis, layer);
                    active[method] ? active[method].call(this, othis) : '';
                    break;
            }
        });
    });

    function opedit(othis,layer) {
        var active = {
            titlebtn: function (othis) {
                var that = this;
                var $type = othis.data('type');
                //多窗口模式，层叠置顶
                layer.open({
                    type: 2 //此处以iframe举例
                    , id: 'toolbar' + $type
                    , title: "添加节假日"
                    , area: ['450px', '460px']
                    , shade: 0
                    , maxmin: true
                    , offset: $type
                    , content: "/festival/gotofestival.do?type=add"
                    , btn: ['保存', '取消']
                    , yes: function (active) {
                        if(child()){
                            var datas = fun();
                            $.ajax({
                                type: "POST",
                                url: "/festival/insert.do",
                                data: datas.serializeArray(),
                                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                                cache: false,
                                dataType: "json",
                                success: function (data) {
                                    window.location.href = "/festival/gotofestival.do?type=list";
                                },
                                error: function () {
                                    dag.layer.msg("请求失败");
                                }
                            });
                        }
                    }
                    , btn2: function () {
                        layer.close();
                    }
                    , zIndex: layer.zIndex //重点1
                    , success: function (layero) {
                        layer.setTop(layero); //重点2
                    }
                });
            }
        }
        return active;
    }

    var dag = window.parent;
    dag.fun = function () {
        return $('#festivalForm');
    }

    dag.child = function () {
        var start = $(".layui-form").validate().element($('#startTime'));
        var end = $(".layui-form").validate().element($('#endTime'));
        if (start && end){
            return true;
        }else{
            return false;
        }
    }

    function update(id){
        layer.open({
            type: 2 //此处以iframe举例
            , id: 'toolbarEdit'
            , title: "修改节假日"
            , area: ['450px', '460px']
            , shade: 0
            , maxmin: true
            , content: "/festival/gotofestival.do?type=edit&festivalId="+id
            , btn: ['保存', '取消']
            , yes: function (active) {
                if(child()){
                    var datas = fun();
                    $.ajax({
                        type: "POST",
                        url: "/festival/update.do",
                        data: datas.serializeArray(),
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        cache: false,
                        dataType: "json",
                        success: function (data) {
                            window.location.href = "/festival/gotofestival.do?type=list";
                        },
                        error: function () {
                            dag.layer.msg("请求失败");
                        }
                    });
                }
            }
            , btn2: function () {
                layer.close();
            }
            , zIndex: layer.zIndex //重点1
            , success: function (layero) {
                layer.setTop(layero); //重点2
            }
        });
    }

    function del(id){
        $.ajax({
            type: "POST",
            url: "/festival/delete.do?festivalId="+id,
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            cache: false,
            dataType: "json",
            success: function (data) {
                window.location.href = "/festival/gotofestival.do?type=list";
            },
            error: function () {
                dag.layer.msg("请求失败");
            }
        });
    }

</script>
</body>
</html>


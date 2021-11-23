<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/27
  Time: 下午 5:18
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
    <title>日程管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
    <script src="/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
</head>

<c:if test="${type=='list'}">
<body >
<table class="layui-hide" lay-filter="schedule"
       lay-data="{height: 'full-100',cellMinWidth: 100,even:'false',toolbar:'#toolbar',defaultToolbar: ['filter']}">
    <thead>
    <tr>
        <th lay-data="{type:'checkbox', fixed: 'left',field:'formcmName'}">选择</th>
        <th lay-data="{field:'scheduletTitle',align: 'center'}">日程标题</th>
        <th lay-data="{field:'type',align: 'center',templet:function(d){return typeStr(d.type);}}">类型</th>
        <th lay-data="{field:'content'}">内容</th>
        <th lay-data="{field:'startTime',align: 'center',templet:function(d){return timeStr(d.startTime);}}">开始时间</th>
        <th lay-data="{field:'endTime',align: 'center',templet:function(d){return timeStr(d.endTime);}}">结束时间</th>
    </tr>
    </thead>
</table>
<script type="text/html" id="toolbar">
    <div class="layui-btn-container">
        <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="add">添加</a>
        <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="edit">修改</a>
        <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="delete">删除</a>
        <a  style="margin-right: 10px;margin-bottom: 10px;height: 22px;line-height: 22px;padding: 0 5px;font-size: 12px;">
            开始时间：<input type="text" lay-verify="datetime" id="ks" autocomplete="off" value="">
            结束时间：<input type="text" lay-verify="datetime" id="js" autocomplete="off" value="">
        </a>
        <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="search">查询</a>
    </div>
</script>
</c:if>
<c:if test="${type!='list'}">
<body>
<fieldset class="layui-elem-field">
    <legend>日程内容</legend>
    <form class="layui-form layui-form-pane" id="scheduleForm">
        <input type="hidden" name="scheduleId" value="${schedule.scheduleId}">
        <div class="layui-container">
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">标题</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <input type="text" id="scheduletTitle" name="scheduletTitle" required="" autocomplete="off" class="layui-input" value="${schedule.scheduletTitle}">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="scheduletTitle_Explain"></p>
                    </div>
                </div>
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">类型</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <select id="type" name="type" style="display: none" required="" lay-filter="selone" maxlength="32">
                                <option value="">请选择</option>
                                <option value="1" <c:if test="${schedule.type == 1}">selected</c:if>>工作</option>
                                <option value="2" <c:if test="${schedule.type == 2}">selected</c:if>>业务</option>
                            </select>
                            <div class="layui-unselect layui-form-select layui-form-selected">
                                <div class="layui-select-title">
                                    <input type="text" placeholder="请选择" value="" readonly="" class="layui-input layui-unselect valid" aria-invalid="false">
                                    <i class="layui-edge"></i>
                                </div>
                                <dl class="layui-anim layui-anim-upbit" style="">
                                    <dd lay-value="" class="layui-select-tips layui-this">请选择</dd>
                                    <dd lay-value="1" class="">工作</dd>
                                    <dd lay-value="2" class="">业务</dd>
                                </dl>
                            </div>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="type_Explain"></p>
                    </div>
                </div>
            </div>
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">开始时间</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <input type="text" id="startTime" name="startTime" autocomplete="off" required="" class="layui-input datetime" value="" toLater="endTime">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="startTime_Explain"></p>
                    </div>
                </div>
                <div class="layui-col-xs6">
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
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">内容</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <textarea id="content" name="content" maxlength="1024" required="" class="layui-textarea" value="${schedule.content}">${schedule.content}</textarea>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="content_Explain"></p>
                    </div>
                </div>
            </div>
            <div class="layui-row">
                <div class="layui-col-xs12">
                    <div style="float:left; width:33%;text-align:center;">
                        <a class="layui-btn layui-btn-radius" herf="javascript:void(0)" onclick="save('${type}')">保存</a>
                    </div>
                    <div style="float:left; width:33%;text-align:center;">
                        <a class="layui-btn layui-btn-radius" herf="javascript:void(0)" onclick="fromreset('scheduleForm')">重置</a>
                    </div>
                    <div style="float:right;width:33%;text-align:center;">
                        <a class="layui-btn layui-btn-radius" herf="javascript:void(0)" href="/schedule/gotoScheduleList.do?type=list">取消</a>
                    </div>
                </div>
            </div>
        </div>
    </form>
</fieldset>
</c:if>
<script src="/resources/js/system/manager.js" charset="utf-8"></script>

<script type="text/javascript">
    var tableid = 'schedule';
    layui.use(['table','form','laydate'], function () {
        var table = layui.table;
        var form = layui.form   ;
        var laydate = layui.laydate;
        //日期
        laydate.render({
            elem: '#startTime'
            , format: 'yyyy-MM-dd HH:mm:ss'
            , type: 'datetime'
            , value: '${schedule.startTime.substring(0,19)}'
            , trigger: 'click'
            , calendar: true
            , done: function (value) {
                $("#startTime").val(value);
                $(".layui-form").validate().element($("#startTime"));
            }
        });
        laydate.render({
            elem: '#endTime'
            , format: 'yyyy-MM-dd HH:mm:ss'
            , type: 'datetime'
            , value: '${schedule.endTime.substring(0,19)}'
            , trigger: 'click'
            , calendar: true
            , done: function (value) {console.log(value);
                $("#endTime").val(value);
                $(".layui-form").validate().element($("#endTime"));
            }
        });
        laydate.render({
            elem: '#ks'
            , format: 'yyyy-MM-dd HH:mm:ss'
            , type: 'datetime'
            , value: ''
            , trigger: 'click'
            , calendar: true
        });
        laydate.render({
            elem: '#js'
            , format: 'yyyy-MM-dd HH:mm:ss'
            , type: 'datetime'
            , value: ''
            , trigger: 'click'
            , calendar: true
        });
        form.render();
        table.init('schedule', {
            url: '/schedule/selectlist.do',
            method: 'POST',
            id: 'schedule',
            page: true,
            limit: 10
        });
        table.on('toolbar(' + tableid + ')', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            var othis = $(this);
            var method = othis.data('method');

            switch (obj.event) {
                case 'add':
                    window.location.href = "/schedule/gotoScheduleList.do?type=add";
                    //var active = opedit(othis, 'add', layer, "");
                    //active[method] ? active[method].call(this, othis) : '';
                    break;
                case 'edit':
                    if (data.length == 0) {
                        layer.msg('请选择一行');
                    } else {
                        if (data.length == 1) {
                            window.location.href = "/schedule/gotoScheduleList.do?type=edit&scheduleId="+data[0]["scheduleId"];
                            //var active = opedit(othis, 'edit', layer, data[0]["scheduleId"]);
                            //active[method] ? active[method].call(this, othis) : '';
                        }else{
                            layer.msg('只能选择一行');
                            return;
                        }
                    }
                    break;
                case 'delete':
                    if (data.length === 0) {
                        layer.msg('请选择一行');
                    } else {
                        if (window.confirm("是否确认要删除?")) {
                            var jsons = JSON.stringify(data);
                            var scheduleId = "";
                            for (var i = 0; i < data.length; i++) {
                                scheduleId += data[i]["scheduleId"] + ";";
                            }
                            $.ajax({
                                type: "POST",
                                url: "/schedule/deletes.do?scheduleId="+scheduleId,
                                contentType: "application/json; charset=utf-8",
                                dataType: "html",
                                cache: false,

                                success: function (data) {
                                    window.location.href = "/schedule/gotoScheduleList.do?type=list";
                                },
                                error: function (message) {
                                    layer.msg("日程删除失败！");
                                }
                            });
                        } else {
                            // 取消时做的操作
                        }
                    }
                    break;
                case 'search':
                    if($("#ks").val()!=null&&$("#ks").val()!=""&&$("#js").val()!=null&&$("#js").val()!=""){
                        if($("#ks").val()>$("#js").val()){
                            layer.msg("结束时间不能小于开始时间！");
                            break;
                        }
                    }
                    var ks = $("#ks").val();
                    var js = $('#js').val();
                    table.reload("schedule", { //此处是上文提到的 初始化标识id
                        where: {
                            startTime: ks,
                            endTime: js
                        }
                    });
                    laydate.render({
                        elem: '#ks'
                        , format: 'yyyy-MM-dd HH:mm:ss'
                        , type: 'datetime'
                        , value: ks
                        , trigger: 'click'
                        , calendar: true
                    });
                    laydate.render({
                        elem: '#js'
                        , format: 'yyyy-MM-dd HH:mm:ss'
                        , type: 'datetime'
                        , value: js
                        , trigger: 'click'
                        , calendar: true
                    });
                    break;
            }
        });
    });

    function save(type){
       if(child()){
            var saveurl = "";
            if (type == "add") {
                saveurl = "/schedule/insert.do";
            }else if(type == "edit") {
                saveurl = "/schedule/update.do";
            }
            var datas = fun();
            $.ajax({
                type: "POST",
                url: saveurl,
                data: datas.serializeArray(),
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                cache: false,
                dataType: "json",
                success: function (data) {
                    parent.layer.closeAll();
                    window.location.href = "/schedule/gotoScheduleList.do?type=list";
                },
                error: function () {
                    layer.msg("请求失败");
                }
            });
        }
    }

    //var dag = window.parent;
    function fun() {
        return $('#scheduleForm');
    }

    function child() {
        var start = $(".layui-form").validate().element($('#startTime'));
        var end = $(".layui-form").validate().element($('#endTime'));
        if (start && end){
            return true;
        }else{
            return false;
        }
    }

    function typeStr(type){
        if(type==1){
            return "工作";
        }else if(type==2){
            return "业务";
        }
    }
    function timeStr(time){
        return time.substring(0,19);
    }

</script>
</body>
</html>


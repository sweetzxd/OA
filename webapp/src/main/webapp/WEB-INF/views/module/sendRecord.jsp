<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/1/12
  Time: 下午 5:24
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
    <title>发送跟踪记录</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
    <style>
        .actor a {
            height: 80px;
            line-height: 80px;
            text-decoration: none !important;
            color: #1E9FFF;
        }
        .actor-p a {
            text-decoration: none !important;
            float: right;
            padding-right: 20px;
            text-align: left;
            color: #1E9FFF;
        }
        .layui-input-block {
            margin-left: 180px !important;
        }
        .actor-div-outer {
            border: 1px #e6e6e6 solid !important;
            min-width: 300px;
            height: 100px;
        }
        .actor-div-in {
            width: 100%;
            height: 100%;
            overflow-y: scroll;
            text-align: left;
        }
        .actor {
            min-width: 180px !important;
            min-height: 100px !important;
        }
        .layui-form-label {
            height: auto !important;
        }
        .actor-p {
            padding-left: 20px;
            text-align: left;
        }
    </style>

</head>

<body>
<fieldset class="layui-elem-field">
    <legend>消息内容</legend>
    <form class="layui-form layui-form-pane" id="messageForm">
        <input name="msgType" value="0" hidden>
        <input name="msgText" value="${msgText}" hidden>
        <input name="msgFile" value="" hidden>
        <div class="layui-container">
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">标题</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <input type="text" id="msgTitle" name="msgTitle" required="" autocomplete="off" class="layui-input" value="">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="msgTitle_Explain"></p>
                    </div>
                </div>
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">等级</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <select id="msgLevel" name="msgLevel" style="display: none" required="" lay-filter="selone" maxlength="32">
                                <option value="">请选择</option>
                                <option value="0">正常</option>
                                <option value="1">急</option>
                                <option value="2">紧急</option>
                                <option value="3">特急</option>
                            </select>
                            <div class="layui-unselect layui-form-select layui-form-selected">
                                <div class="layui-select-title">
                                    <input type="text" placeholder="请选择" value="" readonly="" class="layui-input layui-unselect valid" aria-invalid="false">
                                    <i class="layui-edge"></i>
                                </div>
                                <dl class="layui-anim layui-anim-upbit" style="">
                                    <dd lay-value="" class="layui-select-tips layui-this">请选择</dd>
                                    <dd lay-value="0" class="">正常</dd>
                                    <dd lay-value="1" class="">急</dd>
                                    <dd lay-value="2" class="">紧急</dd>
                                    <dd lay-value="3" class="">特急</dd>
                                </dl>
                            </div>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="msgLevel_Explain"></p>
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label actor"><a id="selectactor" href="#" data-method="setTop" data-type="auto">选择接收人</a></label>
                <div class="layui-input-block">
                    <div class="actor-div-outer">
                        <div class="actor-div-in">
                        </div>
                    </div>
                </div>
                <input type="hidden" name="msgRecUser" id="msgRecUser">
            </div>
            <div class="layui-row">
                <div class="layui-col-xs12">
                    <div style="float:left; width:50%;text-align:center;">
                        <a class="layui-btn layui-btn-radius" herf="javascript:void(0)" onclick="save()">发送</a>
                    </div>
                    <div style="float:right;width:50%;text-align:center;">
                        <a class="layui-btn layui-btn-radius" herf="javascript:void(0)" onclick="closelay()">取消</a>
                    </div>
                </div>
            </div>
        </div>
    </form>
</fieldset>
<jsp:include page="/common/js.jsp"></jsp:include>
<script src="/resources/js/system/manager.js" charset="utf-8"></script>

<script type="text/javascript">
    var tableid = 'messagepage';
    layui.use(['table','form'], function () {
        var table = layui.table;
        var form = layui.form;
        form.render();
    });

    $('#selectactor').on('click', function(){
        var othis = $(this), method = othis.data('method');
        active[method].call(this, othis);
    });

    var active = {
        setTop: function () {
            var that = this;
            layer.open({
                type: 2
                , title: '选择接收人'
                , area: ['600px', '400px']
                , shade: 0
                , maxmin: true
                , content: '/workflow/actorpage.do?workflwId=${node.wkflwID}&nodeId=${node.nodeID}'
                , zIndex: layer.zIndex
            });
        }
    }
    var html = "<table class=\"layui-table\" lay-skin=\"line\" lay-size=\"sm\"><colgroup><col width=\"70\"><col width=\"120\"><col width=\"70\"><col width=\"120\"></colgroup><thead><tr><th colspan=\"4\">部门领导审批-部门领导审批</th></tr></thead><tbody><tr id=\"Z2018121700001\" style=\"display:none\"></tr><tr><td><div style=\"width:90%;text-align:right;\"><label class=\"layui-form-text\">意见:</label></div></td><td><label style=\"width:90%;text-align:left;\" class=\"layui-form-text\">同意 </label></td><td><div style=\"width:90%;text-align:right;\"><label class=\"layui-form-text\">意见说明:</label></div></td><td><label style=\"width:90%;text-align:left;\" class=\"layui-form-text\">尽快解决\n" +
        "</label></td></tr><tr><td colspan=\"4\"><div style=\"float:left; width:50%;text-align:center;\"><label class=\"layui-form-text\">发送人: 章旭 </label></div><div style=\"float:right;width:50%;text-align:center;\"><label class=\"layui-form-text\">发送时间:2018-12-17 13:32:44</label></div></td></tr></tbody></table>"

    function save(){
        var datas = $('#messageForm');
        $.ajax({
            type: "POST",
            url: "/message/sendMessage.do",
            data: datas.serializeArray(),
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            cache: false,
            dataType: "json",
            success: function (data) {
                closelay();
            },
            error: function () {
                layer.msg("请求失败");
            }
        });
    }

    function insertActor(type,id,title){
        var name = id;
        $(".actor-div-in").append("<p class='actor-p' data-type='"+type+"' data-id='"+id+"' data-name='"+name+"'>"+title+"<a href='javascript:void(0)' onclick='removeActor(this)'>删除</a></p>");
        layer.closeAll();
        var actors = '';
        $(".actor-div-in").find("p").each(function(){
            actors += $(this).attr("data-type")+"-"+$(this).attr("data-id")+",";
        });
        $("#msgRecUser").val(actors);
    }
    function removeActor(obj){
        $(obj).parent().remove();
        var actors = '';
        $(".actor-div-in").find("p").each(function(){
            actors += $(this).attr("data-type")+"-"+$(this).attr("data-id")+",";
        });
        $("#msgRecUser").val(actors);
    }

    function closelay(){
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index);
    }


</script>
</body>
</html>

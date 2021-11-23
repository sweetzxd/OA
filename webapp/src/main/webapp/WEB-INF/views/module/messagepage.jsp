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
    <title>${title}</title>
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

<c:if test="${type=='list'}">
<body >
    <input type="hidden" id="msgStatus1" value="1">
    <table class="layui-hide" lay-filter="messagepage"
           lay-data="{height: 'full-100',cellMinWidth: 100,even:'false',toolbar:'#toolbar',defaultToolbar: ['filter']}">
        <thead>
        <tr>
            <th lay-data="{type:'checkbox', fixed: 'left',field:'formcmName'}">选择</th>
            <th lay-data="{field:'msgTitle',align: 'center'}">标题</th>
            <th lay-data="{field:'msgText',align: 'center'}">内容</th>
            <th lay-data="{field:'msgSendUserName',align: 'center'}">发出人</th>
            <th lay-data="{field:'msgLevelStr',align: 'center'}">等级</th>
            <th lay-data="{field:'msgSendTimeStr',align: 'center'}">发送时间</th>
        </tr>
        </thead>
    </table>
    <script type="text/html" id="toolbar">
        <div class="layui-btn-container">
            <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="add">发送消息</a>
            <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="delete">删除</a>
            <input type="checkbox" id="msgStatus" lay-skin="switch" lay-text="未读|已读" lay-filter="msgStatus" checked="checked">
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
                        <option value="msgTitle" selected>标题</option>
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
<c:if test="${type=='send'}">
<body onload="fieldEditor()">
<fieldset class="layui-elem-field">
    <legend>消息内容</legend>
    <form class="layui-form layui-form-pane" id="messageForm">
        <input type="hidden" name="msgType" value="0">
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
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">内容</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                    </div>
                </div>
            </div>
            <div class="layui-row layui-col-space40">
                <div class="textarea editor" id="msgText"></div>
                <textarea style="width: 100%;" name="msgText" id="msgText_Value" hidden></textarea>
            </div>
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-upload uploads component">
                        <button type="button" id="uploads_file" class="layui-btn layui-btn-normal title uploadList" data-type="uploads" aria-invalid="false">上传附件</button>
                        <span class="layui-form-mid required" style="display:none;">*</span>
                        <div class="layui-upload-list" id="uploads_file_Upload">
                            <table class="layui-table">
                                <thead>
                                <tr>
                                    <th>文件名</th>
                                    <th>大小</th>
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="uploadFildList">
                                </tbody>
                            </table>
                            <input type="hidden" class="form-upload-ins" id="uploads_file_Value" name="msgFile" value="" maxlength="1024">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="uploads_file_Explain"></p>
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
                        <a class="layui-btn layui-btn-radius" herf="javascript:void(0)" href="/message/seeMessagepage.do">取消</a>
                    </div>
                </div>
            </div>
        </div>
    </form>
</fieldset>
</c:if>
<jsp:include page="/common/js.jsp"></jsp:include>
<script src="/resources/js/system/manager.js" charset="utf-8"></script>

<script type="text/javascript">
    var tableid = 'messagepage';
    layui.use(['table','form','laydate'], function () {
        var table = layui.table;
        var form = layui.form   ;
        var laydate = layui.laydate;
        laydate.render({
            elem: '#startDate'
            , value: oa.getdate()
        });
        laydate.render({
            elem: '#endDate'
        });
        form.render();
        table.init('messagepage', {
            url: '/message/selectAllMessage.do',
            method: 'POST',
            id: 'messagepage',
            page: true,
            limit: 10
        });
        fieldUpload();
        var reloadTable = function () {
            table.reload("messagepage", { //此处是上文提到的 初始化标识id
                where: {
                    option: jQuery("#option  option:selected").val(),
                    inputval: $('#inputval').val(),
                }
            });
            if($("#msgStatus1").val()==2){
                $("#msgStatus").removeAttr('checked');
            }
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
                    window.location.href="/message/messageInsert.do";
                    //var active = opedit(othis, 'add', layer, "");
                    //active[method] ? active[method].call(this, othis) : '';
                    break;
                case 'delete':
                    if (data.length === 0) {
                        layer.msg('请选择一行');
                    } else {
                        var jsons = JSON.stringify(data);

                        var msgId = "";
                        for (var i = 0; i < data.length; i++) {
                            msgId += data[i]["msgId"] + ";";
                        }
                        $.ajax({
                            type: "POST",
                            url: "../message/deletemessage.do?msgId="+msgId,
                            contentType: "application/json; charset=utf-8",
                            data: {msgId: msgId},
                            dataType: "html",
                            cache: false,
                            success: function (data) {
                                window.location.href = "../message/seeMessagepage.do";
                            },
                            error: function (message) {
                                layer.msg("消息删除失败！");
                            }
                        });
                    }
                    break;
            }
        });
        form.on('switch(msgStatus)', function (data) {
            if(data.elem.checked){
                table.reload("messagepage", { //此处是上文提到的 初始化标识id
                    where: {
                        option: jQuery("#option  option:selected").val(),
                        inputval: "",
                        msgStatus:1
                    }
                });
                $("#msgStatus1").val(1);
            }else{
                table.reload("messagepage", { //此处是上文提到的 初始化标识id
                    where: {
                        option: jQuery("#option  option:selected").val(),
                        inputval: "",
                        msgStatus:2
                    }
                });
                $("#msgStatus").removeAttr('checked');
                $("#msgStatus1").val(2);
            }
        });
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

    function save(){
        var datas = fun();
        $.ajax({
            type: "POST",
            url: "/message/sendMessage.do",
            data: datas.serializeArray(),
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            cache: false,
            dataType: "json",
            success: function (data) {
                window.location.href = "/message/seeMessagepage.do";
            },
            error: function () {
                layer.msg("请求失败");
            }
        });
    }
    //var dag = window.parent;
    function fun(){
        return $('#messageForm');
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
</script>
</body>
</html>
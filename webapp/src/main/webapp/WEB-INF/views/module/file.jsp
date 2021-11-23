<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/22
  Time: 下午 4:23
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
    <title>文件管理</title>
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
<table class="layui-hide" lay-filter="file"
       lay-data="{height: 'full-100',cellMinWidth: 100,even:'false',toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
    <thead>
    <tr>
        <th lay-data="{type:'checkbox', fixed: 'left',field:'formcmName'}">选择</th>
        <th lay-data="{field:'fileName',align: 'center'}">文件名称</th>
        <th lay-data="{field:'fileTypeId',align: 'center'}">文件类型</th>
        <th lay-data="{field:'intro'}">简介</th>
        <th lay-data="{field:'uploadTimeStr',align: 'center'}">上传时间</th>
        <th lay-data="{field:'remark'}">备注</th>
        <th lay-data="{field:'fileAdd',templet:function(d){return oa.decipher('uploads',d.fileAdd);}}">附件</th>
    </tr>
    </thead>
</table>
<script type="text/html" id="toolbar">
    <div class="layui-btn-container">
        <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="add">添加</a>
        <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="edit">修改</a>
        <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="delete">删除</a>
    </div>
</script>
<div class="layui-form">
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">检索：</label>
        </div>
        <div class="layui-inline">
            <div class="layui-input-inline">
                <select id="option" name="option" lay-filter="option">
                    <option value="fileName" selected>文件名称</option>
                    <option value="fileTypeId" >文件类型</option>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <div class="layui-input-inline" id="inputvalText">
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
    <legend>文件内容</legend>
    <form class="layui-form layui-form-pane" id="fileForm">
        <input type="hidden" name="fileId" value="${file.fileId}">
        <div class="layui-container">
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">文件名称</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <input type="text" id="fileName" name="fileName" required="" autocomplete="off" class="layui-input" value="${file.fileName}">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="fileName_Explain"></p>
                    </div>
                </div>
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">文件类型</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <select id="fileTypeId" name="fileTypeId" style="display: none" required="" lay-filter="selone" maxlength="32">
                                <option value="">请选择</option>
                                <c:forEach items="${filetypeMap}" var="map">
                                    <c:if test="${type=='edit'}">
                                        <option value="${map.key}" <c:if test="${file.fileTypeId == map.key}">selected</c:if>>${map.value}</option>
                                    </c:if>
                                    <c:if test="${type=='add'}">
                                        <option value="${map.key}">${map.value}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                            <div class="layui-unselect layui-form-select layui-form-selected">
                                <div class="layui-select-title">
                                    <input type="text" placeholder="请选择" value="" readonly="" class="layui-input layui-unselect valid" aria-invalid="false">
                                    <i class="layui-edge"></i>
                                </div>
                                <dl class="layui-anim layui-anim-upbit" style="">
                                    <dd lay-value="" class="layui-select-tips layui-this">请选择</dd>
                                    <c:forEach items="${filetypeMap}" var="map">
                                        <dd lay-value="${map.key}" class="">${map.value}</dd>
                                    </c:forEach>
                                </dl>
                            </div>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="fileTypeId_Explain"></p>
                    </div>
                </div>
            </div>
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">简介</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <textarea id="intro" name="intro" maxlength="1024" required="" class="layui-textarea" value="${file.intro}">${file.intro}</textarea>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="intro_Explain"></p>
                    </div>
                </div>
            </div>
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">备注</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <textarea id="remark" name="remark" maxlength="1024" required="" class="layui-textarea" value="${file.remark}">${file.remark}</textarea>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="remark_Explain"></p>
                    </div>
                </div>
            </div>
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-upload uploads component">
                        <button type="button" id="uploads_fileAdd" class="layui-btn layui-btn-normal title uploadList" data-type="uploads" aria-invalid="false">上传附件</button>
                        <span class="layui-form-mid required" style="display:none;">*</span>
                        <div class="layui-upload-list" id="uploads_fileAdd_Upload">
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
                                    ${filesHtml}
                                </tbody>
                            </table>
                            <input type="hidden" class="form-upload-ins" id="uploads_fileAdd_Value" name="fileAdd" value="${joblog.file}" maxlength="1024">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="uploads_fileAdd_Explain"></p>
                    </div>
                </div>
            </div>
            <div class="layui-row">
                <div class="layui-col-xs12">
                    <div style="float:left; width:33%;text-align:center;">
                        <a class="layui-btn layui-btn-radius" herf="javascript:void(0)" onclick="save('${type}')">保存</a>
                    </div>
                    <div style="float:left; width:33%;text-align:center;">
                        <a class="layui-btn layui-btn-radius" herf="javascript:void(0)" onclick="fromreset('fileForm')">重置</a>
                    </div>
                    <div style="float:right;width:33%;text-align:center;">
                        <a class="layui-btn layui-btn-radius" herf="javascript:void(0)" href="/file/gotofile.do?type=list">取消</a>
                    </div>
                </div>
            </div>
        </div>
    </form>
</fieldset>
</c:if>
<script src="/resources/js/system/manager.js" charset="utf-8"></script>

<script type="text/javascript">
    var tableid = 'file';
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
        table.init('file', {
            url: '/file/selectlist.do',
            method: 'POST',
            id: 'file',
            page: true,
            limit: 10
        });
        uploadsFun("uploads_fileAdd");
        var reloadTable = function () {
            table.reload("file", { //此处是上文提到的 初始化标识id
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
                    window.location.href = "/file/gotofile.do?type=add";
                    //var active = opedit(othis, 'add', layer, "");
                    //active[method] ? active[method].call(this, othis) : '';
                    break;
                case 'edit':
                    if (data.length == 0) {
                        layer.msg('请选择一行');
                    } else {
                        if (data.length == 1) {
                            window.location.href = "/file/gotofile.do?type=edit&fileId="+data[0]["fileId"];
                            //var active = opedit(othis, 'edit', layer, data[0]["fileId"]);
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
                        var r = confirm("您确定要删除这个文件吗！");
                        if(r == true){
                            var jsons = JSON.stringify(data);

                            var fileId = "";
                            for (var i = 0; i < data.length; i++) {
                                fileId += data[i]["fileId"] + ";";
                            }
                            $.ajax({
                                type: "POST",
                                url: "/file/deletes.do?fileId="+fileId,
                                contentType: "application/json; charset=utf-8",
                                dataType: "html",
                                cache: false,
                                success: function (data) {
                                    window.location.href = "/file/gotofile.do?type=list";
                                },
                                error: function (message) {
                                    layer.msg("文件删除失败！");
                                }
                            });
                        }else{

                        }
                    }
                    break;
            }
        });
        form.on('select(option)', function(data){
            if(data.value=="fileName"){
                $("#inputvalText").html("<input class='layui-input' id='inputval' name='inputval' autocomplete='off'>")
            }else if(data.value=="fileTypeId"){
                $.ajax({
                    type: "POST",
                    url: "/file/getFiletypes.do",
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    cache: false,
                    async: false,
                    dataType: "json",
                    success: function (data) {
                        var html = "<select id='inputval' name='inputval'></select>";
                        $("#inputvalText").html(html);
                        for(var key in data){
                            $("#inputval").append("<option value="+key+">"+data[key]+"</option>");
                        }
                        form.render();
                    },
                    error: function () {
                    }
                });
            }
        })
    });

    function save(type){
        var saveurl = "";
        if (type == "add") {
            saveurl = "/file/insert.do";
        }else if(type == "edit") {
            saveurl = "/file/update.do";
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
                window.location.href = "/file/gotofile.do?type=list";
            },
            error: function () {
                layer.msg("请求失败");
            }
        });
    }

    //var dag = window.parent;
    function fun(){
        return $('#fileForm');
    }

</script>
</body>
</html>

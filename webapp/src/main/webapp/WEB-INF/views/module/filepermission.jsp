<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/20
  Time: 下午 5:35
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
    <title>文件权限管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>


</head>

<c:if test="${type=='list'}">
<body >
<table class="layui-hide" lay-filter="filepermissionpage"
       lay-data="{height: 'full-100',cellMinWidth: 100,even:'false',toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
    <thead>
    <tr>
        <th lay-data="{type:'checkbox', fixed: 'left',field:'formcmName'}">选择</th>
        <th lay-data="{field:'fileTypeId',align: 'center'}">文件类型</th>
        <th lay-data="{field:'actorType',align: 'center'}">角色类别</th>
        <th lay-data="{field:'contextValue',align: 'center'}">角色相关值</th>
        <th lay-data="{field:'seeStr',align: 'center'}">查看权限</th>
        <th lay-data="{field:'downloadStr',align: 'center'}">下载权限</th>
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
                <select id="option" name="option">
                    <option value="fileTypeId" selected>文件类型</option>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <div class="layui-input-inline">
                <select id="inputval" name="inputval">
                </select>
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
    <legend>文件权限</legend>
    <form class="layui-form layui-form-pane" id="filepermissionForm">
        <input type="hidden" name="permissionId" value="${filepermission.permissionId}">
        <input type="hidden" name="actorType" value="roleactor">
        <div class="layui-container">
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">文件类型</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <select id="fileTypeId" name="fileTypeId" style="display: none" required="" lay-filter="selone" maxlength="32">
                                <option value="">请选择</option>
                                <c:forEach items="${filetypeMap}" var="map">
                                    <c:if test="${type=='edit'}">
                                        <option value="${map.key}" <c:if test="${filepermission.fileTypeId == map.key}">selected</c:if>>${map.value}</option>
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
                        <label class="layui-form-label title">相关角色</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <select id="contextValue" name="contextValue" style="display: none" required="" lay-filter="selone" maxlength="32">
                                <option value="">请选择</option>
                                <c:forEach items="${roleList}" var="role">
                                    <c:if test="${type=='edit'}">
                                        <option value="${role.roleId}" <c:if test="${filepermission.contextValue==role.roleId}">selected</c:if>>${role.roleTitle}</option>
                                    </c:if>
                                    <c:if test="${type=='add'}">
                                        <option value="${role.roleId}">${role.roleTitle}</option>
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
                                    <c:forEach items="${roleList}" var="role">
                                        <dd lay-value="${role.roleId}" class="">${role.roleTitle}</dd>
                                    </c:forEach>
                                </dl>
                            </div>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="contextValue_Explain"></p>
                    </div>
                </div>
            </div>
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">查看权限</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <select id="see" name="see" style="display: none" required="" lay-filter="selone" maxlength="32">
                                <option value="">请选择</option>
                                <option value="1" <c:if test="${filepermission.see==1}">selected</c:if>>是</option>
                                <option value="2" <c:if test="${filepermission.see==2}">selected</c:if>>否</option>
                            </select>
                            <div class="layui-unselect layui-form-select layui-form-selected">
                                <div class="layui-select-title">
                                    <input type="text" placeholder="请选择" value="" readonly="" class="layui-input layui-unselect valid" aria-invalid="false">
                                    <i class="layui-edge"></i>
                                </div>
                                <dl class="layui-anim layui-anim-upbit" style="">
                                    <dd lay-value="" class="layui-select-tips layui-this">请选择</dd>
                                    <dd lay-value="1" class="">是</dd>
                                    <dd lay-value="2" class="">否</dd>
                                </dl>
                            </div>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="see_Explain"></p>
                    </div>
                </div>
            </div>
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">下载权限</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <select id="download" name="download" style="display: none" required="" lay-filter="selone" maxlength="32">
                                <option value="">请选择</option>
                                <option value="1" <c:if test="${filepermission.download==1}">selected</c:if>>是</option>
                                <option value="2" <c:if test="${filepermission.download==2}">selected</c:if>>否</option>
                            </select>
                            <div class="layui-unselect layui-form-select layui-form-selected">
                                <div class="layui-select-title">
                                    <input type="text" placeholder="请选择" value="" readonly="" class="layui-input layui-unselect valid" aria-invalid="false">
                                    <i class="layui-edge"></i>
                                </div>
                                <dl class="layui-anim layui-anim-upbit" style="">
                                    <dd lay-value="" class="layui-select-tips layui-this">请选择</dd>
                                    <dd lay-value="1" class="">是</dd>
                                    <dd lay-value="2" class="">否</dd>
                                </dl>
                            </div>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="download_Explain"></p>
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
    var tableid = 'filepermissionpage';
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
        table.init('filepermissionpage', {
            url: '/filepermission/selectlist.do',
            method: 'POST',
            id: 'filepermissionpage',
            page: true,
            limit: 10
        });
        fieldUpload();
        var reloadTable = function () {
            table.reload("filepermissionpage", { //此处是上文提到的 初始化标识id
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
                    var active = opedit(othis, 'add', layer, "");
                    active[method] ? active[method].call(this, othis) : '';
                    break;
                case 'edit':
                    if (data.length == 0) {
                        layer.msg('请选择一行');
                    } else {
                        if (data.length == 1) {
                            var active = opedit(othis, 'edit', layer, data[0]["permissionId"]);
                            active[method] ? active[method].call(this, othis) : '';
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
                        var jsons = JSON.stringify(data);

                        var permissionId = "";
                        for (var i = 0; i < data.length; i++) {
                            permissionId += data[i]["permissionId"] + ";";
                        }
                        $.ajax({
                            type: "POST",
                            url: "/filepermission/deletes.do?permissionId="+permissionId,
                            contentType: "application/json; charset=utf-8",
                            dataType: "html",
                            cache: false,
                            success: function (data) {
                                window.location.href = "/filepermission/gotofilepermission.do?type=list";
                            },
                            error: function (message) {
                                layer.msg("消息删除失败！");
                            }
                        });
                    }
                    break;
            }
        });
        form.on('switch(seeStatus)', function (data) {
            var see = 1;
            var permissionId = this.id;
            if(data.elem.checked){
                see = 1;
            }else{
                see = 2;
            }
            $.ajax({
                type: "POST",
                url: "/filepermission/impower.do?permissionId="+permissionId+"&see="+see,
                contentType: "application/json; charset=utf-8",
                dataType: "html",
                cache: false,
                success: function (data) {
                    layer.msg("授权成功！");
                },
                error: function (message) {
                    layer.msg("授权失败！");
                }
            });
        });
        form.on('switch(downloadStatus)', function (data) {
            var download = 1;
            var permissionId = this.id;
            if(data.elem.checked){
                download = 1;
            }else{console.log(this.id);
                download = 2;
            }
            $.ajax({
                type: "POST",
                url: "/filepermission/impower.do?permissionId="+permissionId+"&download="+download,
                contentType: "application/json; charset=utf-8",
                dataType: "html",
                cache: false,
                success: function (data) {
                    layer.msg("授权成功！");
                },
                error: function (message) {
                    layer.msg("授权失败！");
                }
            });
        });
    });

    function opedit(othis, thistype, layer,id) {
        var url = "/filepermission/gotofilepermission.do?type="+thistype+"&permissionId="+id;
        var title = "";
        if(thistype=="add"){
            title = "添加文件权限";
        }else if(thistype=="edit"){
            title = "修改文件权限";
        }
        var active = {
            titlebtn: function (othis) {
                var that = this;
                var $type = othis.data('type');
                //多窗口模式，层叠置顶
                layer.open({
                    type: 2 //此处以iframe举例
                    , id: 'toolbar' + $type
                    , title: title
                    , area: ['435px', '460px']
                    , shade: 0
                    , maxmin: true
                    , offset: $type
                    , content: url
                    , btn: ['保存', '取消']
                    , yes: function (active) {
                        var saveurl = "";
                        if (thistype == "add") {
                            saveurl = "/filepermission/insert.do";
                        }else if(thistype == "edit") {
                            saveurl = "/filepermission/update.do";
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
                                if(data=="1"){
                                    window.location.href = "/filepermission/gotofilepermission.do?type=list";
                                }else if(data=="-1"){
                                    dag.layer.msg("该文件类型对应相关角色已存在");
                                }else if(data=="0"){
                                    dag.layer.msg("系统错误");
                                }
                            },
                            error: function () {
                                dag.layer.msg("请求失败");
                            }
                        });
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
        return $('#filepermissionForm');
    }
    $(function(){
        if('${type}'=='list'){
            $.ajax({
                type: "POST",
                url: "/filepermission/getFiletypes.do",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                cache: false,
                async: false,
                dataType: "json",
                success: function (data) {
                    var html = "";
                    for(var key in data){
                        html += "<option value="+key+">"+data[key]+"</option>"
                        $("#inputval").append("<option value="+key+">"+data[key]+"</option>");
                    }
                },
                error: function () {
                }
            });
        }
    })

</script>
</body>
</html>
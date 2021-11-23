<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/09/17
  Time: 下午 3:55
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
    <title>角色管理页面</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
</head>
<body>
<c:if test="${type=='list'}">
    <table class="layui-hide" lay-filter="roledefines"
           lay-data="{height: 'full-100',cellMinWidth: 100,toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
        <thead>
        <tr>
            <th lay-data="{type:'checkbox', fixed: 'left',field:'rolecheck'}">选择</th>
            <th lay-data="{field:'roleId'}">角色ID</th>
            <th lay-data="{field:'roleTitle'}">角色名称</th>
            <th lay-data="{field:'userName', templet:function(d){return oa.decipher('users',d.userName);}}">角色人员</th>
            <th lay-data="{field:'modifyName', templet:function(d){return oa.decipher('user',d.modifyName);}}">修改人</th>
            <th lay-data="{field:'modifyTime', templet:function(d){return oa.decipher('datetime',d.modifyTime);}}">修改时间</th>
        </tr>
        </thead>
    </table>
    <script type="text/html" id="toolbar">
        <div class="layui-btn-container">
            <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="add">添加</a>
            <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="modi">修改</a>
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
                        <option value="roleId">角色ID</option>
                        <option value="roleTitle">角色名称</option>
                        <option value="userName">角色人员</option>
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
<c:if test="${type=='add' || type=='modi'}">
    <div class="layui-container">
        <div class="layui-row">
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <form class="layui-form" id="roleDefinesForm">
                    <table class='layui-table' lay-skin='line' lay-size='sm'>
                        <colgroup>
                            <col width='90'>
                            <col width='280'>
                        </colgroup>
                        <thead>
                        <tr>
                            <th colspan='2'>${title}</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr hidden>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <span style='color: red;padding:5px;'>*</span>
                                    <label class='layui-form-text'>角色ID:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <input type='text' lay-verify="required"
                                           <c:if test="${type=='modi'}">readonly="readonly"</c:if>
                                           id='roleId' name='roleId' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${role.roleId}</c:if>'
                                           lay-verify="username" class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <span style='color: red;padding:5px;'>*</span>
                                    <label class='layui-form-text'>角色名称:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <input type='text' id='roleTitle' name='roleTitle' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${role.roleTitle}</c:if>'
                                           class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <label class='layui-form-text'>角色人员:</label>
                                </div>
                            </td>
                            <td>
                                <div class="oa-input-parent" id="allselect" title="点击展开人员列表">
                                    <div class="oa-form-input" oa-input-search-type="0">
                                        <div class="oa-input-title " style="height: 34px;">
                                            <input type="hidden" id='userName' name='userName' value="<c:if test="${type=='modi'}">${role.userName}</c:if>"
                                                   autocomplete="off" debounce="0">
                                            <div class="form-input oa-input">
                                                <div class="oa-input-label" style="left: 0px;">
                                                    <c:if test="${!empty userlist }">
                                                        <c:forEach items="${userlist}" var="user" varStatus="status">
                                                            <span value='${user.id}'>
                                                                <label>${user.name}</label><i
                                                                    onClick='event.cancelBubble = true;removeval(this)'
                                                                    class='xm-iconfont icon-close remove'></i>
                                                            </span>
                                                        </c:forEach>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
        <div class="layui-row" style="display: none" id="selectform">
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <iframe id="select" name="select" src="/role/gotouserselect.do" style="overflow: visible;"
                        scrolling="no" frameborder="no" width="100%" height="90%"></iframe>
            </div>
        </div>
    </div>
</c:if>
<jsp:include page="/common/js.jsp"></jsp:include>
<script type="text/javascript">
    var layer = null;
    layui.use(['form', 'layer', 'table', 'laydate'], function () {
        var form = layui.form;
        layer = layui.layer;
        var table = layui.table;
        var laydate = layui.laydate;
        var tableid = 'roledefines';
        laydate.render({
            elem: '#startDate'
            , value: oa.getdate()
        });
        laydate.render({
            elem: '#endDate'
        });
        form.render('select');
        table.init('roledefines', {
            url: '/role/selectAll.do',
            method: 'POST',
            id: 'roledefines',
            page: true,
            limit: 10
        });
        var reloadTable = function () {
            table.reload("roledefines", { //此处是上文提到的 初始化标识id
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
                case 'modi':
                    if (data.length === 0) {
                        layer.msg('请选择一行');
                    } else if (data.length > 1) {
                        layer.msg('只能同时编辑一个');
                    } else {
                        var jsons = JSON.stringify(data);
                        var roleid = oa.trim(data[0]["roleId"]);
                        var active = opedit(othis, 'modi', layer, roleid);
                        active[method] ? active[method].call(this, othis) : '';
                    }
                    break;
                case 'delete':
                    if (data.length === 0) {
                        layer.msg('请选择一行');
                    } else {
                        var jsons = JSON.stringify(data);
                        var roleid = "";
                        for (var i = 0; i < data.length; i++) {
                            roleid += data[i]["roleId"] + ";";
                        }
                        if (roleid != "") {
                            $.ajax({
                                type: "POST",
                                url: "/role/deleterole.do",
                                contentType: "application/json; charset=utf-8",
                                data: {roleid: roleid},
                                dataType: "html",
                                cache: false,
                                success: function (data) {
                                    oa.gotourl("role/roledefines.do");
                                },
                                error: function (message) {
                                    layer.msg("账号信息删除失败！");
                                }
                            });
                        }
                    }
                    break;
            }
        });
        $("#allselect").on("click", function (n) {
            var dis = document.getElementById("selectform").style.display;
            if (dis == "none") {
                document.getElementById("selectform").style.display = "block";
            } else {
                document.getElementById("selectform").style.display = "none";
            }
        });
    });
    function removeval(object){
        var obj = $(object).parent();
        var val = "";
        obj.remove();
        $("#allselect").find("span").each(function () {
            var tvalue = $(this).attr("value");
            if(val.indexOf(tvalue)<0){
                val += $(this).attr("value") + ";"
            }
        });
        $("#userName").attr("value", val);

    }
    function opedit(othis, thistype, layer, roleid) {
        var url = "", title = "";
        if (thistype == "modi") {
            url = '../role/gotoupdate.do?roleid=' + roleid;
            title = '账号信息修改';
        } else {
            url = '../role/gotoinsert.do';
            title = '新增账号信息';
        }
        var active = {
            titlebtn: function (othis) {
                var that = this;
                var $type = othis.data('type');
                layer.open({
                    type: 2
                    , id: 'toolbar' + $type
                    , title: title
                    , area: ['800px', '700px']
                    , shade: 0
                    , maxmin: true
                    , offset: $type
                    , content: url
                    , btn: ['保存', '关闭']
                    , yes: function (active) {
                        var saveurl = "";
                        if (thistype == "add") {
                            saveurl = "/role/insertrole.do";
                        } else if (thistype == "modi") {
                            saveurl = "/role/updaterole.do";
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
                                oa.gotourl("/role/roledefines.do");
                            },
                            error: function () {
                                layer.msg("请求失败");
                            }
                        });
                    }
                    , btn2: function () {
                        layer.close();
                    }
                    , zIndex: layer.zIndex
                    , success: function (layero) {
                        layer.setTop(layero);
                    }
                });
            }
        }
        return active;
    }

    window.parent.fun = function () {
        return $('#roleDefinesForm');
    }

    function updateinput(id, name) {
        var val = $("#userName").attr("value");
        if(val.indexOf(id)<0){
            var span = "<span value='" + id + "'> <label>" + name + "</label><i onClick='event.cancelBubble = true;removeval(this)' class='xm-iconfont icon-close remove'></i></span>";
            $("#allselect").find(".oa-input-label").append(span);
            val = val + id + ";";
            $("#userName").attr("value", val);
        }else{
            layer.msg("所选人员已存在");
        }
    }
</script>
</body>
</html>
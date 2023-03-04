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
</head>
<body>
<c:if test="${type=='list'}">
    <table class="layui-hide" lay-filter="usermanager"
           lay-data="{height: 'full-100',cellMinWidth: 100,toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
        <thead>
        <tr>
            <th lay-data="{type:'checkbox', fixed: 'left',field:'formcmName'}">选择</th>
            <th lay-data="{field:'userName'}">登录名</th>
            <th lay-data="{field:'name'}">姓名</th>
            <th lay-data="{field:'password'}">密码</th>
            <th lay-data="{field:'computerIP'}">登录电脑IP</th>
            <th lay-data="{field:'accountStatus'}">账号状态</th>
            <th lay-data="{field:'userStatus'}">用户状态</th>
            <th lay-data="{field:'startDate'}">账号启用时间</th>
            <th lay-data="{field:'endDate'}">账号停用时间</th>
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
                        <option value="userName">登录名</option>
                        <option value="name">姓名</option>
                        <option value="computerIP">登录电脑IP</option>
                        <option value="accountStatus">账号状态</option>
                        <option value="userStatus">用户状态</option>
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
                <form class="layui-form" id="userManagerForm">
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
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <span style='color: red;padding:5px;'>*</span>
                                    <label class='layui-form-text'>账号:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <input type='text' lay-verify="required"
                                           <c:if test="${type=='modi'}">readonly="readonly"</c:if>
                                           id='userName' name='userName' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${user.userName}</c:if>'
                                           lay-verify="username" class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <span style='color: red;padding:5px;'>*</span>
                                    <label class='layui-form-text'>姓名:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <input type='text' id='name' name='name' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${user.name}</c:if>'
                                           class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <span style='color: red;padding:5px;'>*</span>
                                    <label class='layui-form-text'>密码:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <input type='password' id='password' name='password' autocomplete='off'
                                           value='' class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <label class='layui-form-text'>登录电脑IP:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <input type='text' id='computerIP' name='computerIP' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${user.computerIP}</c:if>'
                                           class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <label class="layui-form-text">账号状态：</label>
                                </div>
                            </td>
                            <td>
                                <div class="layui-inline">
                                    <div class="layui-input-inline">
                                        <select id="accountStatus" name="accountStatus">
                                            <option value="1" <c:if test="${user.accountStatus==1}">selected</c:if>>停用</option>
                                            <option value="2" <c:if test="${user.accountStatus==2}">selected</c:if>>启用</option>
                                        </select>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <label class="layui-form-text">用户状态：</label>
                                </div>
                            </td>
                            <td>
                                <div class="layui-inline">
                                    <div class="layui-input-inline">
                                        <select id="userStatus" name="userStatus">
                                            <option value="1" <c:if test="${user.userStatus==1}">selected</c:if>>临时账号</option>
                                            <option value="2" <c:if test="${user.userStatus==2}">selected</c:if>>可登录</option>
                                            <option value="3" <c:if test="${user.userStatus==3}">selected</c:if>>锁定登录IP</option>
                                        </select>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <label class='layui-form-text'>账号启用时间:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <input type='text' id='startDate' name='startDate' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${user.startDate}</c:if>'
                                           class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <label class='layui-form-text'>账号停用时间:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <input type='text' id='endDate' name='endDate' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${user.endDate}</c:if>'
                                           class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
    </div>
</c:if>
<jsp:include page="/common/js.jsp"></jsp:include>
<script type="text/javascript">
    layui.use(['form', 'layer', 'table', 'laydate'], function () {
        var form = layui.form;
        var layer = layui.layer;
        var table = layui.table;
        var laydate = layui.laydate;
        var tableid = 'usermanager';
        laydate.render({
            elem: '#startDate'
            , value: oa.getdate()
        });
        laydate.render({
            elem: '#endDate'
        });
        form.render();
        table.init('usermanager', {
            url: '/user/selectAll.do',
            method: 'POST',
            id: 'usermanager',
            page: true,
            limit: 10
        });
        var reloadTable = function () {
            table.reload("usermanager", { //此处是上文提到的 初始化标识id
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
                        var username = oa.trim(data[0]["userName"]);
                        var active = opedit(othis, 'modi', layer, username);
                        active[method] ? active[method].call(this, othis) : '';
                    }
                    break;
                case 'delete':
                    if (data.length === 0) {
                        layer.msg('请选择一行');
                    } else {
                        var jsons = JSON.stringify(data);

                        var username = "";
                        for (var i = 0; i < data.length; i++) {
                            username += data[i]["userName"] + ";";
                        }
                        if (recno != "") {
                            $.ajax({
                                type: "POST",
                                url: "/user/deleteuser.do",
                                contentType: "application/json; charset=utf-8",
                                data: {username: username},
                                dataType: "html",
                                cache: false,
                                success: function (data) {
                                    window.location.href = "user/gotousermanager.do";
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
    });

    function opedit(othis, thistype, layer, username) {
        var url = "", title = "";
        if (thistype == "modi") {
            url = '../user/gotoupdate.do?username=' + username;
            title = '账号信息修改';
        } else {
            url = '../user/gotoinsert.do';
            title = '新增账号信息';
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
                    , area: ['600px', '530px']
                    , shade: 0
                    , maxmin: true
                    , offset: $type
                    , content: url
                    , btn: ['保存', '关闭']
                    , yes: function (active) {
                        var saveurl = "";
                        if (thistype == "add") {
                            saveurl = "/user/insertuser.do";
                        } else if (thistype == "modi") {
                            saveurl = "/user/updateuser.do";
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
                                window.location.href = "../user/gotousermanager.do";
                            },
                            error: function () {
                                layer.msg("请求失败");
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
        return $('#userManagerForm');
    }
</script>
</body>
</html>
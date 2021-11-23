<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/09/18
  Time: 下午 3:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<html>
<head>
    <meta charset="utf-8">
    <title>权限管理页面</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
</head>
<body class="layui-layout-body">
<c:if test="${type=='list'}">
    <div class="layui-layout">
        <div class="layui-side">
            <div class="layui-side-scroll">
                <ul id="accesstree"></ul>
            </div>
        </div>
        <div class="layui-body" id="rigthPage">
            <div style="padding: 15px;height:95%">
                <table class="layui-hide" lay-filter="accessrights"
                       lay-data="{height: 'full-100',cellMinWidth: 100,toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
                    <thead>
                    <tr>
                        <th lay-data="{field:'accesscheck', toolbar: '#tool',fixed: 'left'}">选择</th>
                        <th lay-data="{field:'pageTitle'}">菜单</th>
                        <th lay-data="{field:'roleTitle'}">角色</th>
                        <th lay-data="{field:'acc_info',templet: '#switchInfo', unresize: true}">访问</th>
                        <th lay-data="{field:'acc_add',templet: '#switchAdd', unresize: true}">添加</th>
                        <th lay-data="{field:'acc_modi',templet: '#switchModi', unresize: true}">修改</th>
                        <th lay-data="{field:'acc_delete',templet: '#switchDel', unresize: true}">删除</th>
                        <th lay-data="{field:'acc_import',templet: '#switchImport', unresize: true}">导入Excel</th>
                        <th lay-data="{field:'acc_export',templet: '#switchExport', unresize: true}">导出Excel</th>
                        <th lay-data="{field:'acc_send',templet: '#switchSend', unresize: true}">发送</th>
                        <th lay-data="{field:'specialsetup', toolbar: '#special'}">选择</th>
                    </tr>
                    </thead>
                </table>
                <script type="text/html" id="toolbar">
                    <div class="layui-btn-container">
                        <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="add">添加</a>
                    </div>
                </script>
                <script type="text/html" id="tool">
                    <a class="layui-btn layui-btn-xs" lay-event="modi">保存</a>
                    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
                </script>
                <script type="text/html" id="special">
                    <a class="layui-btn layui-btn-xs" lay-event="setup">设置</a>
                </script>
                <script type="text/html" id="switchInfo">
                    <input type="checkbox" id="acc_info" name="acc_info" value="{{d.acc_info}}" lay-skin="switch"
                           lay-text="是|否" lay-filter="acc_info" {{ d.acc_info !="0" ? 'checked' : '' }}>
                </script>
                <script type="text/html" id="switchAdd">
                    {{#  if(d.formType!=0){ }}
                    <input type="checkbox" id="acc_add" name="acc_add" value="{{d.acc_add}}" lay-skin="switch"
                           lay-text="是|否" lay-filter="acc_add" {{ d.acc_add !="0" ? 'checked' : '' }}>
                    {{#  } }}
                </script>
                <script type="text/html" id="switchModi">
                    {{#  if(d.formType!=0){ }}
                    <input type="checkbox" id="acc_modi" name="acc_modi" value="{{d.acc_modi}}" lay-skin="switch"
                           lay-text="是|否" lay-filter="acc_modi" {{ d.acc_modi !="0" ? 'checked' : '' }}>
                    {{#  } }}
                </script>
                <script type="text/html" id="switchDel">
                    {{#  if(d.formType!=0){ }}
                    <input type="checkbox" id="acc_delete" name="acc_delete" value="{{d.acc_delete}}" lay-skin="switch"
                           lay-text="是|否" lay-filter="acc_delete" {{ d.acc_delete !="0" ? 'checked' : '' }}>
                    {{#  } }}
                </script>
                <script type="text/html" id="switchImport">
                    {{#  if(d.formType!=0){ }}
                    <input type="checkbox" id="acc_import" name="acc_import" value="{{d.acc_import}}" lay-skin="switch"
                           lay-text="是|否" lay-filter="acc_import" {{ d.acc_import !="0" ? 'checked' : '' }}>
                    {{#  } }}
                </script>
                <script type="text/html" id="switchExport">
                    {{#  if(d.formType!=0){ }}
                    <input type="checkbox" id="acc_export" name="acc_export" value="{{d.acc_export}}" lay-skin="switch"
                           lay-text="是|否" lay-filter="acc_export" {{ d.acc_export !="0" ? 'checked' : '' }}>
                    {{#  } }}
                </script>
                <script type="text/html" id="switchSend">
                    {{#  if(d.formType!=0){ }}
                    <input type="checkbox" id="acc_send" name="acc_send" value="{{d.acc_send}}" lay-skin="switch"
                           lay-text="是|否" lay-filter="acc_send" {{ d.acc_send !="0" ? 'checked' : '' }}>
                    {{#  } }}
                </script>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${type=='add'}">
    <div class="layui-row" style="display: none" id="selectform">
        <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
            <iframe id="select" name="select" src="/role/gotoroleselect.do" style="overflow: visible;"
                    scrolling="no" frameborder="no" width="100%" height="90%"></iframe>
        </div>
    </div>
</c:if>
<jsp:include page="/common/js.jsp"></jsp:include>
<script type="text/javascript">
    var pageid = null, pagetitle = null, table = null, layer = null, menuNum = 0;
    layui.use(['form', 'layer', 'table', 'laydate', 'tree'], function () {
        var form = layui.form;
        layer = layui.layer;
        table = layui.table;
        var createTree = function () {
            var node = "";
            $.ajax({
                type: "POST",
                url: "/access/accesstree.do",
                contentType: "application/json; charset=utf-8",
                dataType: "html",
                async: false,
                success: function (data) {
                    node = eval(data);
                },
                error: function (message) {
                    layer.msg("菜单树获取失败！");
                }
            });
            return node;
        };
        layui.tree({
            elem: '#accesstree'
            , target: '_blank'
            , nodes: createTree()
            , click: function (item) {
                menuNum = item.menuNum;
                table.init('accessrights', {
                    url: '/access/selectAccess.do',
                    method: 'POST',
                    id: 'accessrights',
                    page: true,
                    where: {
                        pageid: item.id,
                        pagetitle: item.title
                    },
                    done: function (res, curr, count) {
                        pageid = res.pageid;
                        pagetitle = res.pagetitle;
                    }
                });

            }
        });

        table.init('accessrights');

        table.on('toolbar(accessrights)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            var othis = $(this);
            var method = othis.data('method');
            switch (obj.event) {
                case 'add':
                    if (pageid == null) {
                        layer.msg('请先选择需要添加权限的节点或表单');
                    } else {
                        var active = opedit(othis, layer);
                        active[method] ? active[method].call(this, othis) : '';
                    }
                    break;
            }
        });
        table.on('tool(accessrights)', function (obj) {
            var data = obj.data;
            var layEvent = obj.event;
            switch (obj.event) {
                case 'del':
                    layer.confirm("你确定删除数据吗？", {icon: 3, title: '提示'}, function (index) {
                        $.ajax({
                            type: "POST",
                            url: "/access/deleteaccess.do",
                            data: "data=" + data["pageid"],
                            dataType: "json",
                            cache: false,
                            success: function (data) {
                                if (data != "0") {
                                    obj.del();
                                    layer.close(index);
                                }
                            },
                            error: function (message) {
                                layer.msg("数据删除失败！");
                            }
                        });
                    });
                    break;
                case 'modi':
                    var td = obj.tr[0].childNodes;
                    var datas = {}
                    var accdata = []
                    for (var i = 3; i < td.length; i++) {
                        var td_key = oa.trim(td[i].dataset.content);
                        var td_val = oa.trim(td[i].innerText)
                        if (td_val != null && td_val != "") {
                            var json = {};
                            json.key = td_key;
                            json.value = td_val;
                            accdata.push(json);
                        }
                    }
                    datas.pageid = data["pageid"];
                    datas.roleid = data["roleid"];
                    datas.data = accdata;
                    var jsons = JSON.stringify(datas)
                    $.ajax({
                        type: "POST",
                        url: "/access/updateaccess.do",
                        contentType: "application/json; charset=utf-8",
                        data: {datas: jsons},
                        dataType: "json",
                        success: function (data) {

                        },
                        error: function (message) {
                            layer.msg("菜单树获取失败！");
                        }
                    });
                    break;
                case 'setup':
                    layer.open({
                        type: 2
                        , id: 'specialsetup'
                        , title: "特殊权限设置"
                        , area: ['650px', '530px']
                        , shade: 0
                        , maxmin: true
                        , content: "/access/specialsetup.do?pageid=" + data["pageid"] + "&roleid=" + data["roleid"]
                    });
                    break;
                default:
                    break;
            }
        });
    });

    function opedit(othis, layer) {
        var active = {
            titlebtn: function (othis) {
                var $type = othis.data('type');
                layer.open({
                    type: 2
                    , id: 'toolbar' + $type
                    , title: "选择角色"
                    , area: ['800px', '600px']
                    , shade: 0
                    , maxmin: true
                    , offset: $type
                    , content: "/access/gotoroleselect.do"
                });
            }
        }
        return active;
    }

    function updateinput(id, name) {
        var datas = {}
        datas.pageid = pageid;
        datas.roleid = id;
        var jsons = JSON.stringify(datas)
        $.ajax({
            type: "POST",
            url: "/access/insertaccess.do",
            contentType: "application/json; charset=utf-8",
            data: {datas: jsons},
            dataType: "html",
            success: function (data) {
                console.log(data)
                if (data == "2") {
                    layer.msg("插入角色重复");
                } else if (data == "0") {

                } else {
                    var oldData = table.cache["accessrights"];
                    var data1 = {
                        "roleid": id,
                        "roleTitle": name,
                        "pageid": pageid,
                        "pageTitle": pagetitle,
                        "menuNum": menuNum,
                        "acc_info": data,
                        "acc_add": "0",
                        "acc_modi": "0",
                        "acc_delete": "0",
                        "acc_import": "0",
                        "acc_export": "0",
                        "acc_send": "0"
                    };
                    oldData.push(data1);
                    table.reload('accessrights', {
                        data: oldData
                    });
                    layer.closeAll();
                }
            },
            error: function (message) {
                layer.msg("插入角色失败" + message);
            }
        });
    }
</script>
</body>
</html>

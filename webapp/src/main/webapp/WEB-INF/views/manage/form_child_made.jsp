<%--
  Created by IntelliJ IDEA.
  User: zxd
  Date: 2018/08/28
  Time: 下午 4:41
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
    <title>表单定制</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
</head>
<body>
<c:if test="${type=='list'}">
    <div style="padding: 15px;height:95%">
        <table class="layui-hide" lay-filter="formcm"
               lay-data="{height: 'full-100',cellMinWidth: 100,toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
            <thead>
            <tr>
                <th lay-data="{type:'checkbox', fixed: 'left',field:'formcmName'}">选择</th>
                <th lay-data="{field:'formcmName', sort: true}">页面主键</th>
                <th lay-data="{field:'formcmTitle'}">页面标题</th>
                <th lay-data="{field:'formTask', sort: true}">表单任务</th>
                <th lay-data="{field:'listTask'}">列表任务</th>
                <th lay-data="{field:'recordName', sort: true, templet:function(d){return oa.decipher('user',d.recordName);}}">定制人</th>
                <th lay-data="{field:'recordTime', templet:function(d){return oa.decipher('datetime',d.recordTime);}}">定制时间</th>
                <th lay-data="{field:'modifyName', sort: true, templet:function(d){return oa.decipher('user',d.modifyName);}}">修改人</th>
                <th lay-data="{field:'modifyTime', templet:function(d){return oa.decipher('datetime',d.modifyTime);}}">修改时间</th>
                <th lay-data="{fixed: 'right', width:100, align:'center', toolbar: '#listbar'}">管理</th>
            </tr>
            </thead>

        </table>
        <script type="text/html" id="listbar">
            <div class="layui-btn-container">
                <a class="layui-btn layui-btn-xs" lay-event="editpage">编辑页面</a>
            </div>
        </script>
        <script type="text/html" id="toolbar">
            <div class="layui-btn-container">
                <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="add">添加</a>
                <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="modi">修改</a>
                <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="delete">删除</a>
                <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="break">刷新</a>
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
                            <option value="formcmName">页面主键</option>
                            <option value="formcmTitle">页面标题</option>
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
    </div>
</c:if>
<c:if test="${type=='add' || type=='modi'}">
    <div class="layui-container">
        <div class="layui-row">
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <form class="layui-form" id="formcm_form">
                    <div class="layui-form-item" style='display:none'>
                        <label class="layui-form-label">表单ID</label>
                        <div class="layui-input-block">
                            <input type='text' id='formcmName' name='formcmName' autocomplete='off' value='<c:if test="${type=='modi'}">${formcm.formcmName}</c:if>' class='layui-input'>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">表单名称</label>
                        <div class="layui-input-block">
                            <input type='text' id='formcmTitle' name='formcmTitle' autocomplete='off' value='<c:if test="${type=='modi'}">${formcm.formcmTitle}</c:if>' class='layui-input'>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">表单任务</label>
                        <div class="layui-input-block">
                            <input type='text' id='formTask' name='formTask' autocomplete='off' value='<c:if test="${type=='modi'}">${formcm.formTask}</c:if>' class='layui-input'>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">列表任务</label>
                        <div class="layui-input-block">
                            <input type='text' id='listTask' name='listTask' autocomplete='off' value='<c:if test="${type=='modi'}">${formcm.listTask}</c:if>' class='layui-input'>
                        </div>
                    </div>
                    <input type='hidden' id='module' name='module' autocomplete='off' value='${module}' class='layui-input'>
                </form>
            </div>
        </div>
    </div>
</c:if>
<script type="text/javascript">
    layui.use(['form', 'layer', 'table','tree'], function () {
        var form = layui.form;
        var layer = layui.layer;
        var table = layui.table;
        var tableid = 'formcm';
        var error = '${error}';
        if(error!=null && error!="") {
            layer.msg(error);
        }
        form.render('select');
        table.init('formcm', {
            url: '/form_custom/formcmchildselect.do?module=${formid}', //请求地址
            method: 'POST',
            id: 'formcm',
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
                        var recno = trim(data[0]["formcmName"]);
                        var active = opedit(othis, 'modi', layer, recno);
                        active[method] ? active[method].call(this, othis) : '';
                    }
                    break;
                case 'delete':
                    if (data.length === 0) {
                        layer.msg('请选择一行');
                    } else {
                        var jsons = JSON.stringify(data);

                        var recno = "";
                        for(var i=0;i<data.length;i++){
                            recno += data[i]["formcmName"]+",";
                        }
                        console.log(recno)
                        if (recno != "") {
                            $.ajax({
                                type: "POST",
                                url: "/form_custom/formcmdelete.do?recno="+recno,
                                contentType: "application/json; charset=utf-8",
                                dataType: "html",
                                cache: false,
                                success: function (data) {
                                    location.reload();
                                },
                                error: function (message) {
                                    layer.msg("提交数据失败！");
                                }
                            });
                        }
                    }
                    break;
                default:
                    location.reload();
                    break;
            }
        });
        table.on('tool(' + tableid + ')', function (obj) {
            var childId = obj.data.formcmName;
            oa.gotourl("/form_custom/formchildedit.do?formId=${formid}&childId=" + childId);
        });
    });

    function opedit(othis, thistype, layer, recno) {
        var module = '${formid}';
        var active = {
            titlebtn: function (othis) {
                var that = this;
                var $type = othis.data('type');
                //多窗口模式，层叠置顶
                layer.open({
                    type: 2 //此处以iframe举例
                    , id: 'toolbar' + $type
                    , title: "子表定义"
                    , area: ['500px', '350px']
                    , shade: 0
                    , maxmin: true
                    , offset: $type
                    , content: "../form_custom/formcm/childadd.do?recno="+recno+"&module="+module
                    , btn: ['保存', '关闭']
                    , yes: function (active) {
                        var datas = fun();
                        oa.gotourl("/form_custom/gotochildtable.do?formid=${formid}")
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

    function trim(str) {
        if (str.length > 0) {
            return str.replace(/(^\s*)|(\s*$)/g, "");
        } else {
            return "";
        }

    }

    var dag = window.parent;
    dag.fun = function () {
        var dataval = "";
        $.ajax({
            type: "POST",
            url: "/../form_custom/childsave/${type}.do",
            data: $('#formcm_form').serialize(),
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            cache: false,
            dataType: "json",
            success: function (data) {
                dataval = "1";
            },
            error: function () {
                dataval = "0";
            }
        });
        return dataval;
    }

</script>
</body>
</html>

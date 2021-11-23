<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/23
  Time: 上午 11:16
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
    <title>常用文档</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <script type="text/javascript">
        var element = null;
        function starscript() {
            layui.use(['table', 'form', 'layer', 'laydate', 'element', 'tree'], function () {
                var layer = layui.layer;
                var table = layui.table;
                var laydate = layui.laydate;
                var form = layui.form;
                element = layui.element;
                var error = '${error}';
                if (error != null && error != "") {
                    layer.msg(error);
                }
                var createTree = function () {
                    var node = "";
                    $.ajax({
                        type: "POST",
                        url: "/file/filetypeTree.do",
                        contentType: "application/json; charset=utf-8",
                        dataType: "html",
                        async: false,
                        success: function (data) {
                            node = eval(data);
                        },
                        error: function (message) {
                            layer.msg("组织结构获取失败！");
                        }
                    });
                    return node;
                };
                layui.tree({
                    elem: '#filetypeTree'
                    , target: '_blank'
                    , nodes: createTree()
                    , click: function (item) {
                        var id = item.id;
                        table.reload("FileListTable", {
                            where: {
                                option: "fileTypeId",
                                inputval: id,
                            }
                        });
                    }
                });
                table.init('FileListTable', {
                    url: '/file/getUserFileList.do',
                    method: 'POST',
                    id: 'FileListTable',
                    page: true,
                    limit: 10
                });
                form.render();
                element.render();
                lay('.date').each(function () {
                    laydate.render({
                        elem: this
                    });
                });
                form.on('submit(reload)', function (data) {
                    table.reload("FileListTable", { //此处是上文提到的 初始化标识id
                        where: {
                            option: jQuery("#option  option:selected").val(),
                            inputval: $('#inputval').val(),
                        }
                    });
                });
            });
        }

    </script>
<body onload="starscript()">
    <div class="layui-side" style="margin-top: 30px">
        <div class="layui-side-scroll">
            <ul id="filetypeTree"></ul>
        </div>
    </div>
    <div class="layui-body" id="rigthPage">
        <div style="padding: 15px;height:95%">
            <table class="layui-hide" lay-filter="FileListTable"
                   lay-data="{id:'FileListTable',height: 'full-100',cellMinWidth: 100,toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
                <thead>
                <tr>
                    <th lay-data="{field:'fileName',align: 'center',width:200}">文件名称</th>
                    <th lay-data="{field:'fileAdd',templet:function(d){return oa.decipher('uploads',d.fileAdd);}}">附件</th>
                    <th lay-data="{field:'intro',width:300}">简介</th>
                    <th lay-data="{field:'uploadTimeStr',align: 'center',width:200, sort: true}">上传时间</th>
                    <th lay-data="{field:'remark',width:300}">备注</th>
                </tr>
                </thead>

            </table>
            <script type="text/html" id="toolbar">
                <div class="layui-btn-container">
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
                                <option value="fileName">文件名称</option>
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
    </div>
</body>
</html>


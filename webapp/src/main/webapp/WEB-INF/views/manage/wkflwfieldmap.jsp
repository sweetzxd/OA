<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/10/25
  Time: 下午 3:51
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
    <title>设置关联菜单</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/resources/flow/workflow.css" type="text/css"/>
    <jsp:include page="/common/js.jsp"></jsp:include>
</head>
<body>
<c:if test="${type=='info'}">
<div class="layui-container" style="height:95%;">
    <div class="layui-row layui-col-space10">
        <div class="layui-col-xs5">
            <div>
                <table class="layui-hidden" id="treeTable" lay-filter="treeTable"></table>
            </div>
            <script type="text/html" id="titleTpl">
                {{#  if(d.lay_level ==2 && d.pageId != '${id}'){ }}
                <a data-method="treebtn" class="layui-btn layui-btn-primary layui-btn-xs" lay-event="add" style="color: #fff;background-color: #009688">选择</a>
                {{#  } }}
            </script>
        </div>
        <div class="layui-col-xs7 layui-form"  style="margin-top: 10px">
            <div style="border: 1px solid #ccc;height:100%;">
                <div style="height:40px;border-bottom: 1px solid #ccc;text-align: center"><span style='margin-right: 15px;color: red;' id="pageTitle"></span><span style="line-height: 40px">选择节点字段关联</span></div>
                <div id="contextTable"></div>
            </div>
        </div>

    </div>
</div>
</c:if>
<c:if test="${type=='edit'}">
    <div class="layui-col-xs12 layui-form" style="margin-top: 10px;text-align: center">
        <label>节点：</label>
        <div class='layui-input-inline'>
            <select lay-filter="node" id="node">
                ${option}
            </select>
        </div>
    </div>
    <div class="layui-col-xs12 layui-form" style="margin-top: 10px;text-align: center">
        <label>字段：</label>
        <div class='layui-input-inline'>
            <select lay-filter="nodeFiled" id="nodeFiled">

            </select>
        </div>
    </div>
    <input type="hidden" id="nodeFiledValue">
    <input type="hidden" id="nodeFiledText">
</c:if>
<script type="text/javascript">
    var editObj = null, ptable = null, treeGrid = null, tableId = 'treeTable', layer = null;
    layui.config({
        base: '/resources/js/admin/'
    }).extend({
        treeGrid: 'treeGrid'
    }).use(['form','jquery', 'treeGrid', 'layer'], function () {
        var $ = layui.jquery;
        var form = layui.form;
        var table = layui.table;
        form.render();
        treeGrid = layui.treeGrid;//很重要
        layer = layui.layer;
        ptable = treeGrid.render({
            id: tableId
            , elem: '#' + tableId
            , idField: 'pageId'
            , url: '/myurl/treecontexttable.do'
            , cellMinWidth: 200
            , treeId: 'pageId'//树形id字段名称
            , treeUpId: 'parentId'//树形父id字段名称
            , treeShowName: 'pageTitle'//以树形式显示的字段
            , isFilter: false
            , iconOpen: false//是否显示图标【默认显示】
            , isOpenDefault: true//节点默认是展开还是折叠【默认展开】
            , cols: [[
                {field: 'pageTitle',width: '86%', title: '菜单名'}
                ,{ field: 'pageId' ,width: '20%', templet: '#titleTpl'}
            ]]
            , page: true

        });
        treeGrid.on('tool(' + tableId + ')', function (obj) {
            console.log(obj.data)
            var pageId = obj.data.pageId;
            var pageTitle = obj.data.pageTitle;
            var option = "";
            $.ajax({
                type: "POST",
                url: "/workflow/getFieldsByPageId.do?pageId="+pageId,
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                cache: false,
                dataType: "html",
                async:false,
                success: function (data) {
                    option = data;
                },
                error: function () {
                    alert("请求失败");
                }
            });

            $("#contextTable").html(option);
            $("#pageTitle").html(pageTitle+"表单");
        });

        form.on('select(node)', function(data){
            var nodeId = data.value;
            $.ajax({
                type: "POST",
                url: "/workflow/getFieldsByNodeId.do?nodeId="+nodeId,
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                cache: false,
                dataType: "html",
                async:false,
                success: function (data) {
                    $("#nodeFiled").html(data);
                    form.render('select');
                },
                error: function () {
                    alert("请求失败");
                }
            });
        });

        form.on('select(nodeFiled)', function(data){
            $("#nodeFiledValue").val(data.value);
            $("#nodeFiledText").val(data.elem[data.elem.selectedIndex].text);
        });

    });

    function editNodeFieldName(id){
        var wkflwId = '${wkflwId}';
        layui.use( 'layer', function () {
            var layer = layui.layer;
            layer.open({
                type: 2
                , title: '选择节点字段'
                , area: ['400px', '300px']
                , shade: 0
                , maxmin: true
                , content: '/workflow/seeWkflwfieldmap.do?wkflwId='+wkflwId+'&type=edit'
                , btn: ['确定', '取消']
                , yes: function (index, layero) {
                    var content = layero.find("iframe")[0].contentWindow.$("#nodeFiledValue").val();
                    var content1 = layero.find("iframe")[0].contentWindow.$("#nodeFiledText").val();
                    if(content==""){
                        alert("请选择字段");
                        return;
                    }
                    var inpid = id.substring(4,id.length)
                    $("#"+inpid).val(inpid+";"+content);
                    $("#"+id).html("节点："+content1);
                    layer.close(index);
                }
                , btn2: function () {
                    layer.close();
                }
                , zIndex: layer.zIndex
            });
        });
    }

    function delNodeFieldName(id,nodeId){
        if($("#"+nodeId).html()=="节点："){
            layer.msg("该字段没有关联节点字段");
            return;
        }else{
            $("#"+nodeId.substring(4,nodeId.length)).val("");
            $("#"+nodeId).html("节点：");
            if(id!=""){
                $.ajax({
                    type: "POST",
                    url: "/workflow/delWkflwfieldmap.do?formFieldName="+nodeId.substring(4,nodeId.length),
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    cache: false,
                    dataType: "json",
                    success: function (data) {

                    }
                });
            }
            layer.msg("删除成功");
            return;
        }
    }

</script>
</body>
</html>

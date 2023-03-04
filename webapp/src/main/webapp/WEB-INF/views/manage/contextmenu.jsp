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
    <jsp:include page="/common/js.jsp"></jsp:include>
	
</head>
<body>
<div class="layui-container" style="height:95%;">
    <div class="layui-row layui-col-space10">
        <div class="layui-col-xs7 layui-form">
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">选择关联键</label>
                    <div class="layui-input-inline">
                        <select id="comtextkey" name="comtextkey" lay-filter="comtextkey">
                            ${field}
                        </select>
                    </div>
                </div>
            </div>
            <div style="border: 1px solid #ccc;height:60%;">
                <div style="height:40px;border-bottom: 1px solid #ccc;text-align: center"><span>选择关联菜单关联键</span></div>
                <div id="contextTable" lay-filter="contextTable">

                </div>
            </div>
        </div>
        <div class="layui-col-xs5">
            <div style="margin-top: 53px">
                <table class="layui-hidden" id="treeTable" lay-filter="treeTable"></table>
            </div>
            <script type="text/html" id="titleTpl">
                {{#  if(d.lay_level ==2 && d.pageId != '${id}'){ }}
                <a data-method="treebtn" class="layui-btn layui-btn-primary layui-btn-xs" lay-event="add">添加</a>
                {{#  } }}
            </script>
        </div>
    </div>
    <div class="layui-row layui-col-space10">
        <div class="layui-col-xs12">
            <div style="border: 1px solid #ccc;height:28%;" id="contextval">
                ${conmenutext}
            </div>
        </div>
    </div>
    <form class="layui-form" action="" id="contextmenu">
        <input type="hidden" id="menu" name="menu" value="${conmenuval}"/>
    </form>
</div>
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
                { field: 'pageId' ,width: '20%', templet: '#titleTpl'}
                , {field: 'pageTitle',width: '86%', title: '菜单名'}
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
                url: "/myurl/resetcontextmenu.do?pageId="+pageId,
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                cache: false,
                dataType: "html",
                async:false,
                success: function (data) {
                    console.log(data)
                    option = data;
                },
                error: function () {
                    alert("请求失败");
                }
            });
            var title = "<label class='layui-form-label'>"+pageTitle+"</label>";
            var select = "<select name='comtextkey' lay-filter='comtextkey' id='"+pageId+"'> "+option+"</select>";
            var div = "<div class='layui-form-item' style='padding-top:10px;border-bottom: 1px solid #ccc; '>" +
                "<div class='layui-inline'>"+title+"<div class='layui-input-inline'>"+select+" </div>" +
                "<a style='margin-top: 10px;float:left;' href='javascript:void(0);' onclick=\"insertmenu('"+pageTitle+"','"+pageId+"')\">选择</a></div></div>";
            $("#contextTable").append(div);
            form.render('select');
            console.log($(this).parent().parent())
            $(this).parent().parent().parent().html("");
        });
    });
    function insertmenu(title,pageId){
        var $select = $('#'+pageId).val();
        var comtextkey = $('#comtextkey').val();
        console.log(comtextkey)

        var id = comtextkey+":"+pageId+":"+$select;
        var judge = true;
        $("#contextval>div").each(function(i) {
            /*if($(this).attr("data-value") == id){
                layer.msg("该菜单已存在");
                judge = false;
            }*/
            var ids = $(this).attr("data-value").split(":");
            if(ids[1]==pageId){
                layer.msg("该菜单已存在",{offset:'50%'});
                judge = false;
            }
        });
        if(judge){
            var div = "<div style='width: 95%;' data-value='"+id+"'>" +
                "<span>"+$('#comtextkey').find("option:selected").text()+":"+title+":"+$('#'+pageId).find("option:selected").text()+"</span>" +
                "<span style='float: right;' onclick='removemenu(this)'><a href='javascript:void(0);'>删除</a></span></div>";
            $("#contextval").append(div);
            var val = $('#menuskip').val();
            val += comtextkey+":"+pageId+":"+$select+"|";
            $('#menuskip').val(val);
        }
    }
    function removemenu(obj){
        var div = $(obj).parent();
        console.log(div);
        div.remove();
        var val = "";
        $("#contextval>div").each(function(i) {
            val += $(this).attr("data-value")+"|";
        });
        $('#menuskip').val(val);
    }
    var dag = window.parent;
    dag.fun = function () {
        return $('#contextmenu').serialize();
    }
</script>
</body>
</html>

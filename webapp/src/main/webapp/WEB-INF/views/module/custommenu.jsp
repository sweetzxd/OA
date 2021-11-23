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
    <title>设置个人常用功能</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
    <style>
        .table-div-outer {
            border: 1px #e6e6e6 solid !important;
            min-width: 300px;
            height: 600px;

        }

        .table-div-in {
            width: 100%;
            height: 100%;
            overflow-y: scroll;
            text-align: left;
        }

        .table-field-p {
            min-height: 20px;
            margin: 5px;
            padding-left: 20px;
            text-align: left;
            background-color: #e6e6e6;
        }

        .table-field-p a {
            text-decoration: none !important;
            float: right;
            padding-right: 10px;
            text-align: left;
            color: #1E9FFF;
        }
    </style>
</head>
<body>
<div class="layui-container" style="height:95%;">
    <div class="layui-form-item" style="padding-top: 15px;">
        <a href="javascript:location.reload();" class="layui-btn layui-btn-xs">刷新</a>
        <a href="javascript:void(0);" onclick="save()" class="layui-btn layui-btn-xs">保存</a>
    </div>
    <div class="layui-row layui-col-space10">
        <div class="layui-col-xs5 layui-form">
            <div style="height:40px;border: 1px solid #ccc;text-align: center"><span>设置个人常用功能</span></div>
            <div class="table-div-outer" id="taskField">
                <div class="table-div-in" id="changyonglianjie">
                    <c:if test="${!empty listvalue}">
                        <c:forEach items="${listvalue}" var="val" varStatus="status">
                            <p class="table-field-p" data-id="${val.cdzj181210001}"
                               data-name="${val.cdmc181210001}">${val.cdmc181210001}
                                <a class="del" href="javascript:" onclick="del(this)">[删除]</a>
                                <a class="down" href="javascript:" onclick="down(this)">[下移]</a>
                                <a class="up" href="javascript:" onclick="up(this)">[上移]</a>
                            </p>
                        </c:forEach>
                    </c:if>
                </div>
            </div>
        </div>
        <div class="layui-col-xs5">
            <div class="table-div-outer" style="height:650px;overflow-y: hidden;">
                <div class="table-div-in" style="overflow-y: hidden;">
                    <table class="layui-hidden" id="treeTable" lay-filter="treeTable"></table>
                </div>
            </div>
            <script type="text/html" id="titleTpl">
                {{#  if(d.lay_level ==2 && d.pageId != '${id}'){ }}
                <a data-method="treebtn" class="layui-btn layui-btn-primary layui-btn-xs" lay-event="add">添加</a>
                {{#  } }}
            </script>
        </div>
    </div>
    <input type="hidden" id="menuskip" name="menuskip" value="${conmenuval}"/>
</div>
<script type="text/javascript">
    layui.config({
        base: '/resources/js/admin/'
    }).extend({
        treeGrid: 'treeGrid'
    }).use(['treeGrid'], function () {
        var treeGrid = layui.treeGrid;
        treeGrid.render({
            id: 'treeTable'
            , elem: '#treeTable'
            , idField: 'pageId'
            , url: '/myurl/usercustommenu.do'
            , treeId: 'pageId'//树形id字段名称
            , treeUpId: 'parentId'//树形父id字段名称
            , treeShowName: 'pageTitle'//以树形式显示的字段
            , isFilter: false
            , iconOpen: false//是否显示图标【默认显示】
            , isOpenDefault: true//节点默认是展开还是折叠【默认展开】
            , cols: [[
                {field: 'pageId', width: '15%', templet: '#titleTpl'}
                , {field: 'pageTitle', title: '菜单名'}
            ]]
            , height: 'full-30'
            , page: true
            , parseData: function(res){
                $("#menuskip").attr("value", res.menu);
            }
        });
        treeGrid.on('tool(treeTable)', function (obj) {
            add(obj.data);
        });
    });

    function add(val) {
        var shu = $("#changyonglianjie").children('p').length;
        if(shu <= 9){
            var id = val.pageId;
            var name = val.pageTitle;
            var len = $("#taskField").find(".table-div-in p");
            var addtr = true;
            len.each(function () {
                if ($(this).attr("data-id") == id) {
                    addtr = false;
                    return false;
                }
            });

            if (addtr) {
                var del = "<a class='del' href='javascript:' onclick='del(this)'>[删除]</a>";
                var down = "<a class='down' href='javascript:' onclick='down(this)'>[下移]</a>";
                var up = "<a class='up' href='javascript:' onclick='up(this)'>[上移]</a>";
                var span = "<p class='table-field-p' data-id='" + id + "' data-name='" + name + "'>" + name + del + down + up + "</p>";
                $("#taskField").find(".table-div-in").append(span);
                var len = $("#taskField").find(".table-div-in p");
                var val = "";
                len.each(function () {
                    val += $(this).attr("data-id") + ";";
                });
                $("#menuskip").attr("value", val);
            }
        }else{
            window.alert("您只能设置10个常用链接！");
        }
    }

    function del(obj) {
        $(obj).parent().remove();
        var id = $(obj).parent().attr("data-id");
        var name = $(obj).parent().attr("data-name");

        var add = "<a class='add' href='javascript:' onclick='add(this)'>[添加]</a>";
        var span = "<p class='table-field-p' data-id='" + id + "' data-name='" + name + "'>" + name + add + "</p>";
        $("#tableField").find(".table-div-in").append(span);

        var len = $("#taskField").find(".table-div-in p");
        var val = "";
        len.each(function () {
            val += $(this).attr("data-id") + ";";
        });
        $("#menuskip").attr("value", val);
    }

    function up(obj) {
        var p = $(obj).parents("p");
        if (p.index() != 0) {
            p.prev().before(p);
        }
        var len = $("#taskField").find(".table-div-in p");
        var val = "";
        len.each(function () {
            val += $(this).attr("data-id") + ";";
        });
        $("#menuskip").attr("value", val);
    }

    function down(obj) {
        var len = $("#taskField").find(".table-div-in p");
        var p = $(obj).parents("p");
        if (p.index() != len - 1) {
            p.next().after(p);
        }
        var val = "";
        len.each(function () {
            val += $(this).attr("data-id") + ";";
        });
        $("#menuskip").attr("value", val);
    }

    function save() {
        var data = oa.post('/myurl/usercustommenusave.do',{"menuskip": $('#menuskip').val()});
        if(data=="1"){
            parent.layui.admin.events.closeThisTabs();
        }
    }
</script>
</body>
</html>

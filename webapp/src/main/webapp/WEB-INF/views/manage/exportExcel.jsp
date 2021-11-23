<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/12/10
  Time: 下午 3:36
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
    <title>导出Excel</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>

    <style>
        .table-div-outer{
            border:1px #e6e6e6 solid !important;
            min-width: 300px;
            height:500px;
        }
        .table-div-in{
            width:100%;
            height:100%;
            overflow-y:scroll;
            text-align:left;
        }
        .table-field-p {
            min-height: 20px;
            margin: 5px;
            padding-left:20px;
            text-align:left;
            background-color: #e6e6e6;
        }
        .table-field-p a {
            text-decoration: none !important;
            float:right;
            padding-right:10px;
            text-align: left;
            color:#1E9FFF;
        }
    </style>
</head>

<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space10">
        <div class="layui-col-xs4" style="margin-top: 10px;">
            <form class="layui-form layui-form-pane" action="">
                <div class="layui-form-item layui-form-text">
                    <label class="layui-form-label">相关字段</label>
                    <div class="table-div-outer" id="tableField">
                        <div class="table-div-in">
                            <c:if test="${!empty list}">
                                <c:forEach items="${list}" var="m" varStatus="status">
                                    <p class="table-field-p" data-id="${m.fieldName}" data-name="${m.fieldTitle}">${m.fieldTitle}
                                        <a class="add" href="javascript:" onclick="add(this)">[添加]</a>
                                    </p>
                                </c:forEach>
                            </c:if>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <div class="layui-col-xs4" style="margin-top: 10px;">
            <form class="layui-form layui-form-pane" action="">
                <div class="layui-form-item  layui-form-text">
                    <label class="layui-form-label">需导出字段</label>
                    <div class="table-div-outer" id="taskField">
                        <div class="table-div-in">
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <div class="layui-col-xs4" style="margin-top: 10px;">
            <form class="layui-form layui-form-pane" action="">
                <div class="layui-form-item  layui-form-text">
                    <label class="layui-form-label">相关子表</label>
                    <div class="table-div-outer">
                        <div class="table-div-in">
                            <c:if test="${!empty childList}">
                                <c:forEach items="${childList}" var="m">
                                    <input type="checkbox" name="childTable" value="${m.fieldName}" title="${m.fieldTitle}" checked="">
                                </c:forEach>
                            </c:if>
                            <div style=" margin-top: 20px;">
                                <a style="margin-left: 10px;" class="layui-btn layui-btn-normal" onclick="exportInfo();">导出</a>
                            </div>
                        </div>
                    </div>
                </div>
            </form>

        </div>
    </div>
</div>

<script type="text/javascript">
    layui.use(['form'], function () {
        var form = layui.form;
        form.render();
    });

    function exportInfo(){
        var fields = "";
        var childTable = "";
        var len = $("#taskField").find(".table-div-in p");
        len.each(function() {
            fields += ',' +$(this).attr("data-id");
        });
        $("input:checkbox[name='childTable']:checked").each(function() {
            childTable += ',' + $(this).val();
        });

        if(fields==""){
            layer.msg("至少添加一个需导出字段");
            return;
        }else{
            location.href  = "/userpage/exportTable.do?formId="+'${formId}'+"&fields="+fields+"&childTable="+childTable+"&field="+window.parent.$("#field").val()+"&term="+window.parent.$("#term").val()+"&inputval="+window.parent.$("#inputval").val();
            //parent.layer.closeAll();
        }
    }

    function up(obj) {
        var p = $(obj).parents("p");
        if (p.index() != 0) {
            p.prev().before(p);
        }
    }

    function down(obj) {
        var len = $("#taskField").find(".table-div-in p");
        var p = $(obj).parents("p");
        if (p.index() != len - 1) {
            p.next().after(p);
        }
    }

    function add(obj){
        $(obj).parent().remove();
        var id = $(obj).parent().attr("data-id");
        var name = $(obj).parent().attr("data-name");

        var del = "<a class='del' href='javascript:' onclick='del(this)'>[删除]</a>";
        var down = "<a class='down' href='javascript:' onclick='down(this)'>[下移]</a>";
        var up = "<a class='up' href='javascript:' onclick='up(this)'>[上移]</a>";
        var span = "<p class='table-field-p' data-id='"+id+"' data-name='"+name+"'>"+name+del+down+up+"</p>";
        $("#taskField").find(".table-div-in").append(span);

    }

    function del(obj){
        $(obj).parent().remove();
        var id = $(obj).parent().attr("data-id");
        var name = $(obj).parent().attr("data-name");

        var add = "<a class='add' href='javascript:' onclick='add(this)'>[添加]</a>";
        var span = "<p class='table-field-p' data-id='"+id+"' data-name='"+name+"'>"+name+add+"</p>";
        $("#tableField").find(".table-div-in").append(span);
    }

</script>
</body>
</html>


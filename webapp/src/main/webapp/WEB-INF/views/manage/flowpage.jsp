<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/09/21
  Time: 下午 4:51
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
    <title>流程属性</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/resources/css/flowpage.css" type="text/css"/>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript">
        var layer;
        function starscript() {
            layui.use(['form', 'layer','layedit'], function () {
                var form = layui.form;
                layer = layui.layer;
                var layedit = layui.layedit;
                form.render();
                form.on('submit(formsubmit)', function(data){
                    var jsons = JSON.stringify(data.field);
                    console.log(jsons)
                    $.ajax({
                        type: "POST",
                        url: "/workflow/flowsave.do",
                        data:{'data':jsons},
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        dataType: "json",
                        async: false,
                        success: function (data) {
                        },
                        error: function (message) {
                        }
                    });
                    return false;
                });

            });
        }
        function insertFlowLabFld(){
            var table = $("#table").find("option:selected");
            var value = "["+table.val()+"]";
            console.log(value)
            var name1 = $("#flowLabFld").val();
            console.log(name1)
            $("#flowLabFld").val(name1+value);
        }
       function insertFlowLabFldliucheng(){
            var tableliucheng = $("#tableliucheng").find("option:selected");
            var value = "["+tableliucheng.val()+"]";
            console.log(value)
            var name1 = $("#flowLabFldliucheng").val();
            console.log(name1)
            $("#flowLabFldliucheng").val(name1+value);
        }
        function insertSpecialField(){
            var special = $("#special").find("option:selected").val();
            var value = $("#specialField").val();
            $("#specialField").val(value+special);
        }
        function insertNodeField(){
            var special = $("#nodes").find("option:selected").val();
            var value = $("#specialField").val();
            $("#specialField").val(value+special);
        }
        var dag = window.parent;
    </script>
<body onload="starscript()">
<div class="layui-container">
    <div class="layui-row">
        <div class="layui-col-xs12">
            <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
                <legend>流程属性设置</legend>
            </fieldset>
            <form class="layui-form layui-form-pane" id="nodepage">
                <div class="layui-form-item" hidden>
                    <label class="layui-form-label">流程ID</label>
                    <div class="layui-input-block">
                        <input type="text" class="layui-input" name="wkflwID" value="${wkflw.wkflwID}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">流程名字</label>
                    <div class="layui-input-block">
                        <input type="text" class="layui-input" name="wkfName" value="${wkflw.wkfName}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">流程类别</label>
                    <div class="layui-input-block">
                        <select id="wkfType" name="wkfType" lay-filter="wkfType">
                            <option value="市场经营" <c:if test="${wkflw.wkfType=='市场经营'}">selected</c:if>>市场经营</option>
                            <option value="综合办公" <c:if test="${wkflw.wkfType=='综合办公'}">selected</c:if>>综合办公</option>
                            <option value="人力资源" <c:if test="${wkflw.wkfType=='人力资源'}">selected</c:if>>人力资源</option>
                            <option value="资产财务" <c:if test="${wkflw.wkfType=='资产财务'}">selected</c:if>>资产财务</option>
                        </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-row layui-col-space10">
                        <div class="layui-col-xs4">
                            <label class="layui-form-label">任务消息模板</label>
                        </div>
                        <div class="layui-col-xs4">
                            <select id="table" lay-filter="table">
                                <option value="">请选择字段</option>
                                ${select}
                            </select>
                        </div>
                        <div class="layui-col-xs2">
                            <div style="padding-top: 8px;padding-left: 10px;"><a href="#" onclick="insertFlowLabFld()" class="layui-btn layui-btn-xs">插入</a></div>
                        </div>
                        <div class="layui-col-xs12">
                            <textarea class="layui-textarea" name="flowLabFld" lay-verify="flowLabFld" id="flowLabFld">${wkflw.flowLabFld}</textarea>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-row layui-col-space10">
                        <div class="layui-col-xs4">
                            <label class="layui-form-label">流程消息模板</label>
                        </div>
                        <div class="layui-col-xs4">
                            <select id="tableliucheng" lay-filter="table">
                                <option value="">请选择字段</option>
                                ${select}
                            </select>
                        </div>
                        <div class="layui-col-xs2">
                            <div style="padding-top: 8px;padding-left: 10px;"><a href="#" onclick="insertFlowLabFldliucheng()" class="layui-btn layui-btn-xs">插入</a></div>
                        </div>
                        <div class="layui-col-xs12">
                            <textarea class="layui-textarea" name="flowLabFldliucheng" lay-verify="flowLabFldliucheng" id="flowLabFldliucheng">${wkflw.flowLabFldliucheng}</textarea>
                        </div>
                    </div>
                </div>

                <div class="layui-form-item">
                    <div class="layui-row layui-col-space10">
                        <div class="layui-col-xs4">
                            <label class="layui-form-label">添加特殊处理</label>
                        </div>
                        <div class="layui-col-xs4">
                            <select id="special" lay-filter="special">
                                <option value="">请选择</option>
                                <option value="<account[]>">过程插入流程台账</option>
                                <option value="<account>">插入流程台账</option>
                                <option value="<workflow>">流程结束后置</option>
                                <option value="<flownode>">节点发出后置</option>
                            </select>
                        </div>
                        <div class="layui-col-xs2">
                            <div style="padding-top: 8px;padding-left: 10px;"><a href="#" onclick="insertSpecialField()" class="layui-btn layui-btn-xs">插入</a></div>
                        </div>
                    </div>
                    <div class="layui-row layui-col-space10">
                        <div class="layui-col-xs4">
                            <label class="layui-form-label">添加节点</label>
                        </div>
                        <div class="layui-col-xs4">
                            <select id="nodes" lay-filter="table">
                                <option value="">请选择节点</option>
                                ${nodes}
                            </select>
                        </div>
                        <div class="layui-col-xs2">
                            <div style="padding-top: 8px;padding-left: 10px;"><a href="#" onclick="insertNodeField()" class="layui-btn layui-btn-xs">插入</a></div>
                        </div>
                    </div>
                    <div class="layui-row">
                        <div class="layui-col-xs12">
                            <input type="text" class="layui-input" name="specialField" lay-verify="specialField" id="specialField" value="${wkflw.specialField}">
                        </div>
                    </div>
                </div>

                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <button class="layui-btn" lay-submit lay-filter="formsubmit">立即提交</button>
                        <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                    </div>
                </div>
            </form>
            <div style="margin-top: 100px; margin-left: 180px;">
                <button class="layui-btn" onclick="dag.editwkflwfieldmap('${wkflw.wkflwID}')">映射表单字段</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>

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
    <title>线属性</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/resources/css/flowpage.css" type="text/css"/>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript">
        function starscript() {
            layui.use(['form', 'layer'], function () {
                var form = layui.form;
                var layer = layui.layer;
                form.render();
                //监听提交
                form.on('submit(formsubmit)', function(data){
                    var jsons = JSON.stringify(data.field);
                    console.log(jsons)
                    $.ajax({
                        type: "POST",
                        url: "/workflow/linesave.do",
                        data:{'data':jsons},
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        dataType: "json",
                        async: false,
                        success: function (data) {
                            layer.msg("保存成功");
                        },
                        error: function (message) {
                        }
                    });
                    return false;
                });
                form.on('select(logUnitType)', function (data) {
                    var id = data.value;
                });
            });
        }
        function insertLogUnit(){
            var table = $("#table").find("option:selected");
            var judge = $("#judge").find("option:selected").text();
            var params = $("#params").val();
            var logUnitType = $("#logUnitType").find("option:selected").val();

            if(logUnitType=="2"){
                judge = "等于";
                params = "是";
            }else if(logUnitType=="3"){
                $(".actor-div-in").find("p").remove();
            }
            var value="["+table.val()+"|"+judge+"|"+params+"]";
            var title=table.parent().attr("label")+"-"+table.text()+"|"+judge+"|"+params;
            $(".actor-div-in").append("<p class='actor-p' value='"+value+"'>"+title+"<a href='javascript:void(0)' onclick='removeLogUnit(this)'>删除</a></p>");
            var logUnitParams = "";
            $(".actor-div-in").find("p").each(function(){
                logUnitParams += $(this).attr("value")
            });
            $("#logUnitParams").val(logUnitParams);
        }
        function removeLogUnit(obj){
            $(obj).parent().remove();
            var logUnitParams = "";
            $(".actor-div-in").find("p").each(function(){
                logUnitParams += $(this).attr("value")
            });
            $("#logUnitParams").val(logUnitParams);
        }
    </script>
<body onload="starscript()">
<div class="layui-container">
    <div class="layui-row">
        <div class="layui-col-xs12">
            <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
                <legend>线属性设置</legend>
            </fieldset>
            <form class="layui-form layui-form-pane" id="linepage">
                <div class="layui-form-item" hidden>
                    <label class="layui-form-label">ID</label>
                    <div class="layui-input-block">
                        <input type="text" class="layui-input" name="pathId" value="${line.pathId}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">名字</label>
                    <div class="layui-input-block">
                        <input type="text" class="layui-input" name="pathTitle" value="${line.pathTitle}" readonly unselectable="on">
                    </div>
                </div>
                <div class="layui-form-item" pane="">
                    <label class="layui-form-label">是否是并行路径</label>
                    <div class="layui-input-block">
                        <input type="radio" name="isAsynchPath" value="1" title="是" <c:if test="${line.isAsynchPath==1}">checked</c:if>>
                        <input type="radio" name="isAsynchPath" value="0" title="否" <c:if test="${line.isAsynchPath==0}">checked</c:if>>
                    </div>
                </div>
                <div class="layui-form-item" pane="">
                    <label class="layui-form-label">路径是否为退回首节点</label>
                    <div class="layui-input-block">
                        <input type="radio" name="isRebackPath" value="1" title="是" <c:if test="${line.isRebackPath==1}">checked</c:if>>
                        <input type="radio" name="isRebackPath" value="0" title="否" <c:if test="${line.isRebackPath==0}">checked</c:if>>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">多人判断条件方式</label>
                    <div class="layui-input-block">
                        <select name="logTypeOfByMultActor" lay-filter="logTypeOfByMultActor">
                            <option value="1" <c:if test="${line.logTypeOfByMultActor==1}">selected</c:if>>只需一人的填写符合本条件</option>
                            <option value="2" <c:if test="${line.logTypeOfByMultActor==2}">selected</c:if>>需所有人的填写符合本条件</option>
                        </select>
                    </div>
                </div>
                <div class="layui-form-item" pane="">
                    <label class="layui-form-label">通过时其它任务</label>
                    <div class="layui-input-block">
                        <input type="radio" name="path1ToOtherTask" value="0" title="撤消其他人任务" <c:if test="${line.path1ToOtherTask==0}">checked</c:if>>
                        <input type="radio" name="path1ToOtherTask" value="1" title="其他人任务继续" <c:if test="${line.path1ToOtherTask==1}">checked</c:if>>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">判断形式</label>
                    <div class="layui-input-block">
                        <select id="logUnitType" name="logUnitType" lay-filter="logUnitType">
                            <option value="1" <c:if test="${line.logUnitType==1}">selected</c:if>>无条件发送</option>
                            <option value="2" <c:if test="${line.logUnitType==2}">selected</c:if>>指定字段为真</option>
                            <option value="3" <c:if test="${line.logUnitType==3}">selected</c:if>>表达式为真</option>
                            <option value="4" <c:if test="${line.logUnitType==4}">selected</c:if>>多个表达式为真</option>
                            <option value="5" <c:if test="${line.logUnitType==5}">selected</c:if>>任一表达式为真</option>
                        </select>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">添加表达式</label>
                    <div class="layui-row layui-col-space10">
                        <div class="layui-col-xs4">
                            <select id="table" lay-filter="table">
                                <option value="">请选择节点字段</option>
                                ${select}
                            </select>
                        </div>
                        <div class="layui-col-xs3">
                            <select id="judge" lay-filter="judge">
                                <option value="等于">等于</option>
                                <option value="不等于">不等于</option>
                                <option value="大于">大于</option>
                                <option value="大于等于">大于等于</option>
                                <option value="小于">小于</option>
                                <option value="小于等于">小于等于</option>
                                <option value="包含">包含</option>
                            </select>
                        </div>
                        <div class="layui-col-xs3">
                            <input type="text" class="layui-input" id="params">
                        </div>
                        <div class="layui-col-xs2">
                            <div style="padding-top: 8px;padding-left: 10px;"><a href="#" onclick="insertLogUnit()" class="layui-btn layui-btn-xs">插入</a></div>
                        </div>
                        <div class="layui-col-xs12">
                            <div class="actor-div-outer">
                                <div class="actor-div-in">
                                    <c:if test="${!empty logUnitParams}">
                                        <c:forEach items="${logUnitParams}" var="logUnitParam" varStatus="status">
                                            <p class="actor-p" value="${logUnitParam}"><s:dic type="line_param" value="${logUnitParam}"></s:dic><a href="javascript:void(0)" onclick="removeLogUnit(this)">删除</a></p>
                                        </c:forEach>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                    <input type="hidden" name="logUnitParams" id="logUnitParams" value="${line.logUnitParams}">
                </div>
                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <button class="layui-btn" lay-submit lay-filter="formsubmit">立即提交</button>
                        <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>

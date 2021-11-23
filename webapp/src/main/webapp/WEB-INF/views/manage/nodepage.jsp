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
    <title>节点属性</title>
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
            layui.use(['form', 'layer'], function () {
                var form = layui.form;
                layer = layui.layer;
                form.render();
                $('#selectactor').on('click', function(){
                    var othis = $(this), method = othis.data('method');
                     active[method].call(this, othis);
                });
                var active = {
                    setTop: function () {
                        var that = this;
                        layer.open({
                            type: 2
                            , title: '选择执行角色'
                            , area: ['600px', '400px']
                            , shade: 0
                            , maxmin: true
                            , content: '/workflow/actorpage.do?workflwId=${node.wkflwID}&nodeId=${node.nodeID}'
                            , zIndex: layer.zIndex
                        });
                    }
                }
                //监听提交
                form.on('submit(formsubmit)', function(data){
                    var data = data.field;
                    var actors = [];
                    $(".actor-div-in").find("p").each(function(){
                        var v = {}
                        v.id=$(this).attr("data-id");
                        v.type = $(this).attr("data-type");
                        v.context=$(this).attr("data-name");
                        actors.push(v);
                    });
                    data.actor=actors;
                    var jsons = JSON.stringify(data);
                    var nodeId = data.nodeID;
                    $.ajax({
                        type: "POST",
                        url: "/workflow/nodesave.do",
                        data:{'data':jsons},
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        dataType: "json",
                        async: false,
                        success: function (data) {
                            layer.msg("保存成功")
                            //oa.gotourl("/workflow/nodepage.do?id=" + nodeId);
                        },
                        error: function (message) {

                        }
                    });
                    return false;
                });
            });
        }
        function insertActor(type,id,title){
            var name = id;
            $(".actor-div-in").append("<p class='actor-p' data-type='"+type+"' data-id='"+id+"' data-name='"+name+"'>"+title+"<a href='javascript:void(0)' onclick='removeActor(this)'>删除角色</a></p>");
            layer.closeAll();
        }
        function removeActor(obj){
            $(obj).parent().remove();
        }
    </script>
<body onload="starscript()">
<div class="layui-container">
    <div class="layui-row">
        <div class="layui-col-xs12">
            <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
                <legend>节点属性设置</legend>
            </fieldset>
            <form class="layui-form layui-form-pane" id="nodepage">
                <div class="layui-form-item" hidden>
                    <label class="layui-form-label">节点ID</label>
                    <div class="layui-input-block">
                        <input type="text" class="layui-input" name="nodeId" value="${node.nodeID}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">节点名字</label>
                    <div class="layui-input-block">
                        <input type="text" class="layui-input" name="nodeTitle" value="${node.nodeTitle}">
                    </div>
                </div>
                <div class="layui-form-item" pane="">
                    <label class="layui-form-label">允许接受人移交任务</label>
                    <div class="layui-input-block">
                        <input type="radio" name="allowOtherDoit" value="1" title="是" <c:if test="${node.allowOtherDoit==1}">checked</c:if>>
                        <input type="radio" name="allowOtherDoit" value="0" title="否" <c:if test="${node.allowOtherDoit==0}">checked</c:if>>
                    </div>
                </div>
                <div class="layui-form-item" pane="">
                    <label class="layui-form-label">只需一人接受并执行</label>
                    <div class="layui-input-block">
                        <input type="radio" name="isORAction" value="1" title="是" <c:if test="${node.isORAction==1}">checked=""</c:if>>
                        <input type="radio" name="isORAction" value="0" title="否" <c:if test="${node.isORAction==0}">checked=""</c:if>>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label actor"><a id="selectactor" href="#" data-method="setTop" data-type="auto">添加节点执行角色</a></label>
                    <div class="layui-input-block">
                        <div class="actor-div-outer">
                            <div class="actor-div-in">
                                <c:if test="${!empty actors}">
                                    <c:forEach items="${actors}" var="actor" varStatus="status">
                                        <p class="actor-p" data-type="${actor.wkfActorType}" data-id="${actor.nodeActorID}" data-name="${actor.contextValue}"><s:dic type="node_actor" value="${actor.nodeActorID}"></s:dic><a href="javascript:void(0)" onclick="removeActor(this)">删除角色</a></p>
                                    </c:forEach>
                                </c:if>
                            </div>
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
        </div>
    </div>
</div>
</body>
</html>

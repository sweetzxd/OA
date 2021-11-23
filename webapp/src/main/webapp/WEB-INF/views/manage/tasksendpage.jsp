<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/09/30
  Time: 下午 2:01
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
    <title>流程处理页面</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
    <script type="text/javascript">

    </script>
</head>
<body height="95%">
<div class="layui-row layui-col-space4">
    <div class="layui-col-md4">
        <div class="layui-row grid-demo">
            <span class="layui-breadcrumb" lay-separator="|">
              <a href="#">流程历史记录</a>
              <a id="openworkflow" href="#" data-method="setTop" data-type="auto">查看流程图</a>
            </span>
            <iframe id="flowoption" name="flowoption" src="/workflow/logpage.do?procId=${procId}&wkflowId=${wkflwId}"  style="overflow: visible;" top="30px" frameborder="no" width="100%" height="95%"></iframe>
        </div>
    </div>
    <div class="layui-col-md8">
        <div class="layui-row grid-demo grid-demo-bg1" style="margin-top: 6px;">
            <iframe id="formoption" name="formoption" src="/flowpage/gototask.do?procId=${procId}&wkflwId=${wkflwId}&nodeId=${nodeId}&workOrderNO=${workOrderNO}"
                    style="overflow: visible;" frameborder="no" width="100%" height="95%"></iframe>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
    layui.use(['form', 'element'], function () {
        var form = layui.form;
        var element = layui.element;
        element.init();
        $('#openworkflow').on('click', function(){
            layer.open({
                type: 2
                , title: '查看流程图'
                , area: ['800px', '600px']
                , shade: 0
                , maxmin: true
                , content: '/workflow/workflowshow.do?id=${wkflwId}'
                , zIndex: layer.zIndex
            });
        });
    });
</script>
</html>

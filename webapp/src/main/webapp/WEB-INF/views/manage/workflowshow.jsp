<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/09/21
  Time: 上午 10:22
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
    <title>展示流程</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/resources/flow/workflow.css" type="text/css"/>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
</head>

<body onpaste="return false" onload="newflow()">
<div class="layui-row">
    <div class="layui-col-xs12">
        <div id="main">
            <div class="flowchart" id="flowchart">
                <div class="window starnode node" id="starNode" type="0">
                    <div>开始</div>
                </div>
                <div class="window endnode node" id="endNode" type="0">
                    <div>结束</div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<jsp:include page="/common/js.jsp"></jsp:include>
<script type="text/javascript" src="/resources/flow/jsplumb.js"></script>
<script type="text/javascript" src="/resources/flow/workflow.js"></script>
<script type="text/javascript">
    function newflow() {
        var node = ${nodeArray};
        var $div = $("#flowchart");
        if($div!=null) {
            var minus = 100;
            for (var i = 0; i < node.length; i++) {
                var id = node[i].BlockId;
                var text = node[i].BlockContent;
                var type = node[i].BlockType;
                var x = node[i].BlockX;
                var y = node[i].BlockY;
                if (type == '0' && id == "starNode") {
                    minus = y-10;
                    $("#" + id).css("top", y-minus);
                    $("#" + id).css("left", x);
                    add(instance, "starNode", ['Bottom']);
                } else if (type == '0' && id == "endNode") {
                    $("#" + id).css("top", y-minus);
                    $("#" + id).css("left", x);
                    add(instance, "endNode", ['Top']);
                } else if (type == '1') {
                    $div.append("<div class='newnode flownode node' id='" + id + "' type='1'><div class='diamond-text'>" + text + "</div></div>");
                    $("#" + id).css("top", y-minus);
                    $("#" + id).css("left", x);
                    add(instance, id);
                } else if (type == '2') {
                    $div.append("<div class='diamond flownode node' id='" + id + "' type='2'><div class='diamond-div'><div class='diamond-text'>" + text + "</div></div></div>");
                    $("#" + id).css("top", y-minus);
                    $("#" + id).css("left", x);
                    add(instance, id);
                }
            }
            var line = ${lineArray};
            for(var i=0;i<line.length;i++){
                var text = line[i].ConnectionText;
                var id = line[i].ConnectionId;
                var form = line[i].PageSourceId;
                var to = line[i].PageTargetId;
                var formuuid = line[i].StarUuid;
                var touuid = line[i].EndUuid;
                connect(instance, [formuuid,touuid],id,text);

            }
            //instance.draggable(jsPlumb.getSelector(".flowchart .window"), {grid: [20, 20]});
        }
    }
    function add(instance, id, anchors){
        if (!anchors) {
            anchors = ["Top", "Bottom", "Left", "Right"];
        }
        for (var i = 0; i < anchors.length; i++) {
            var anchorsUUID = id + anchors[i];
            instance.addEndpoint(id, point, {anchor: anchors[i], uuid: anchorsUUID});
        }
    }
</script>
</html>


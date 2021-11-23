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
    <title>新建流程</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/resources/flow/workflow.css" type="text/css"/>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
</head>

<body onload="newflow()">
<div class="layui-row">
    <div class="layui-col-xs6" style="height: 100%;border: 1px solid #567567;">
        <div class="layui-btn-group">
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="openflow()">打开流程</button>
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="saveflow('${id}')">保存流程</button>
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="editflow('${id}')">编辑流程</button>
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="newnode1()">新增标准节点</button>
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="newnode2()">新增判定节点</button>
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="editnode()">编辑节点</button>
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="editformnode('${id}')">编辑节点页面</button>
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="delnode()">删除节点</button>
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="editline()">编辑线</button>
            <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="delline()">删除线</button>
        </div>
        <div id="main" style="border-top: 1px solid #567567;">
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
    <div class="layui-col-xs6">
        <iframe id="editpage" name="option" src="/workflow/selectworkflow.do" style="overflow: visible;" scrolling="no"
                frameborder="no"
                width="100%" height="100%"></iframe>
    </div>
</div>
</body>
<jsp:include page="/common/js.jsp"></jsp:include>
<script type="text/javascript" src="/resources/flow/jsplumb.js"></script>
<script type="text/javascript" src="/resources/flow/workflow.js"></script>
<script type="text/javascript">
    function newflow() {
        var node = ${nodeArray};
        if (node != "0") {
            var $div = $("#flowchart");
            if ($div != null) {
                for (var i = 0; i < node.length; i++) {
                    var id = node[i].BlockId;
                    var text = node[i].BlockContent;
                    var type = node[i].BlockType;
                    var x = node[i].BlockX;
                    var y = node[i].BlockY;
                    if (type == '0' && id == "starNode") {
                        $("#" + id).css("top", y);
                        $("#" + id).css("left", x);
                        addEndpoint(instance, "starNode");
                    } else if (type == '0' && id == "endNode") {
                        $("#" + id).css("top", y);
                        $("#" + id).css("left", x);
                        addEndpoint(instance, "endNode");
                    } else if (type == '1') {
                        $div.append("<div class='newnode flownode node' id='" + id + "' type='1' onclick='textmodi(this,1)'><div class='diamond-text'>" + text + "</div></div>");
                        $("#" + id).css("top", y);
                        $("#" + id).css("left", x);
                        addEndpoint(instance, id);
                    } else if (type == '2') {
                        $div.append("<div class='diamond flownode node' id='" + id + "' type='2' onclick='textmodi(this,2)'><div class='diamond-div'><div class='diamond-text'>" + text + "</div></div></div>");
                        $("#" + id).css("top", y);
                        $("#" + id).css("left", x);
                        addEndpoint(instance, id);
                    }
                }
                var line = ${lineArray};
                for (var i = 0; i < line.length; i++) {
                    var text = line[i].ConnectionText;
                    var id = line[i].ConnectionId;
                    var form = line[i].PageSourceId;
                    var to = line[i].PageTargetId;
                    var formuuid = line[i].StarUuid;
                    var touuid = line[i].EndUuid;
                    modiconnect(instance, [formuuid, touuid],id,text);
                }
                instance.draggable(jsPlumb.getSelector(".flowchart .window"), {grid: [20, 20]});
            }
        } else {
            addEndpoint(instance, "starNode");
            addEndpoint(instance, "endNode");
            instance.draggable(jsPlumb.getSelector(".flowchart .window"), {grid: [20, 20]});
            jsPlumb.setSuspendDrawing(true);
        }

    }

    function saveflow(wkflwId) {
        var connects = [];
        $.each(instance.getAllConnections(), function (idx, connection) {
            connects.push({
                ConnectionText: connection.text==null?"":connection.text,
                ConnectionId: connection.id,
                PageSourceId: connection.sourceId,
                PageTargetId: connection.targetId,
                SourceText: oa.trim(connection.source.innerText),
                TargetText: oa.trim(connection.target.innerText),
                StarUuid: connection.endpoints[0].getUuid(),
                EndUuid: connection.endpoints[1].getUuid()
            });
        });
        var blocks = [];
        $("#flowchart .node").each(function (idx, elem) {
            var $elem = $(elem);
            blocks.push({
                BlockId: $elem.attr('id'),
                BlockContent: oa.trim($elem.text()),
                BlockType: $elem.attr('type'),
                BlockX: parseInt($elem.css("left"), 10),
                BlockY: parseInt($elem.css("top"), 10)
            });
        });
        var serliza = {};
        serliza.node = blocks;
        serliza.line = connects;
        var jsons = JSON.stringify(serliza)
        if (blocks.length > 2 && connects.length > 0) {
            var wkfName = "";
            if (wkflwId==null || wkflwId=='0') {
                wkfName = prompt("请输入流程名称：", "新增流程");
            }
            $.ajax({
                type: "POST",
                url: "/workflow/saveflow.do",
                data: {'wkflwId': wkflwId,'wkfName': wkfName, 'flowval': jsons},
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                dataType: "json",
                async: false,
                success: function (data) {
                },
                error: function (message) {
                }
            });
        } else {
            alert("请先绘制流程图");
        }
    }
    function editwkflwfieldmap(wkflwId){
        if (wkflwId != null && wkflwId != "") {
            layui.use( 'layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 2
                    , title: '映射表单字段'
                    , area: ['1000px', '600px']
                    , shade: 0
                    , maxmin: true
                    , content: '/workflow/seeWkflwfieldmap.do?wkflwId='+wkflwId+'&type=info'
                    , btn: ['保存', '关闭']
                    , yes: function (index, layero) {
                        var str = "";
                        var child = layero.find("iframe")[0].contentWindow;
                        child.$("[name=fieldmap]").each(function () {
                            if($(this).val()!=null&&$(this).val()!=""){
                                str += $(this).val()+",";
                            }

                        });
                        console.log("str",str)
                        if(str==""){
                            layer.closeAll();
                            return;
                        }
                        $.ajax({
                            type: "POST",
                            url: "/workflow/saveWkflwfieldmap.do",
                            data:{"fieldstr":str,"wkflwId":wkflwId},
                            contentType: "application/x-www-form-urlencoded; charset=utf-8",
                            cache: false,
                            dataType: "json",
                            success: function (data) {
                                layer.closeAll();
                            },
                            error: function () {
                                alert("请求失败");
                            }
                        });
                    }
                    , btn2: function () {
                        layer.close();
                    }
                    , zIndex: layer.zIndex
                });
            });
        } else {
            alert("请先保存流程");
        }
    }
</script>
</html>


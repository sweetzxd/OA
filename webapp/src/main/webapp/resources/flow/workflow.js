
var connectorPaintStyle = {
    lineWidth: 4,
    strokeStyle: "#61B7CF",
    joinstyle: "round",
    outlineColor: "#216477",
    outlineWidth: 2
};
var connectorHoverStyle = {
    lineWidth: 4,
    strokeStyle: "#216477",
    outlineWidth: 2,
    outlineColor: "#000"
};
var endpointHoverStyle = {
    fillStyle: "#216477",
    strokeStyle: "#216477"
};
var connectorStyle = {
    stub: [30, 30],
    gap: 10,
    cornerRadius: 5,
    alwaysRespectStubs: true
};
var example = {
    paintStyle: {stroke: "blue", strokeWidth: 3},
};
var endpoint = {
    endpoint: "Dot",
    paintStyle: {
        strokeStyle: "#7AB02C",
        stroke: "#567567",
        strokeWidth: 2,
        fillStyle: "#000",
        radius: 4,
        lineWidth: 2
    },
    isSource: true,
    isTarget: true,
    connector: ["Flowchart", connectorStyle],
    hoverPaintStyle: endpointHoverStyle,
    connectorHoverStyle: connectorHoverStyle,
    maxConnections: 5,
    dragOptions: {}
};

var point = {
    endpoint: "Dot",
    paintStyle: {
        strokeStyle: "#7AB02C",
        stroke: "#567567",
        strokeWidth: -5,
        fillStyle: "#000",
        radius: 0.01,
        lineWidth: -5
    },
    isSource: true,
    isTarget: true,
    connector: ["Flowchart", connectorStyle],
    hoverPaintStyle: endpointHoverStyle,
    connectorHoverStyle: connectorHoverStyle,
    maxConnections: 5,
    dragOptions: {}
};
var connection = [
    ["Arrow", {
        width: 7,
        length: 7,
        location: 1,
        foldback: 1
    }],
    ["Label", {
        location: 0.5,
        id: "label",
        text: "labeltext",
        cssClass: "aLabel"
    }]

];
// 初始化，返回流程图的对象
var instance = jsPlumb.getInstance({
    DragOptions: {
        cursor: 'pointer',
        zIndex: 2000
    },
    ConnectionOverlays: connection,
    ReattachConnections: false,
    deleteEndpointsOnDetach: false,
    Container: "flowchart",
});

var num = 3,lineobj = null, nodeobj = null;
function addEndpoint(instance, id, anchors) {
    if (!anchors) {
        anchors = ["Top", "Bottom", "Left", "Right"];
    }
    for (var i = 0; i < anchors.length; i++) {
        var anchorsUUID = id + anchors[i];
        instance.addEndpoint(id, endpoint, {anchor: anchors[i], uuid: anchorsUUID});
    }
    instance.doWhileSuspended(function () {
        instance.draggable(id, {grid: [20, 20]});
    });
    instance.bind("connection", function(info) {
        var text = info.connection.text;
        text = text!=null?text:"线";
        info.connection.getOverlay("label").setLabelText(info.connection.id,text);
    });
}

function modiconnect(instance, uuids,id,text) {
    instance.modiconnect({uuids: uuids, editable: true,id:id,text:text});
}

function connect(instance, uuids,id,text) {
    var obj = instance.connect({uuids: uuids, editable: true});
    obj.getOverlay("label").setLabelText(id,text);
}
function openflow(){
    $("#editpage").attr({"src": "/workflow/selectworkflow.do"});
}

function newnode1() {
    var id = "flowNode" + num;
    var name = "节点" + num;
    $("#starNode").after("<div class='newnode flownode node' id='" + id + "' type='1' onclick='textmodi(this,1)'><div class='diamond-text'>" + name + "</div></div>");
    addEndpoint(instance, id);
    jsPlumb.fire("jsPlumbDemoLoaded", instance);
    num++;
}

function newnode2() {
    var id = "flowNode" + num;
    var name = "节点" + num;
    $("#starNode").after("<div class='diamond flownode node' id='" + id + "' type='2' onclick='textmodi(this,2)'><div class='diamond-div'><div class='diamond-text'>" + name + "</div></div></div>");
    addEndpoint(instance, id);
    jsPlumb.fire("jsPlumbDemoLoaded", instance);
    num++;
}

function textmodi(obj,type) {
    nodeobj = obj;
    var $text = $(obj).find(".diamond-text").text();
    $(obj).dblclick(function () {
        if(type==2){
            $(this).find(".diamond-div").html("");
            $(this).find(".diamond-div").append("<input type='text' class='diamond-text nodeinput' value='" + $text + "' onblur=\"nodetextsave(this,'"+$text+"',1,true)\"/>");
        }else{
            $(this).html("");
            $(this).append("<input type='text' class='diamond-text nodeinput' value='" + $text + "' onblur=\"nodetextsave(this,'"+$text+"',1,true)\"/>");
        }
    });
}
var timer=null;
instance.bind("click", function (connect) {
    clearTimeout(timer);
    timer=setTimeout(function(){
        lineobj = connect;
    },300);
});
instance.bind("dblclick", function (connect) {
    clearTimeout(timer);
    lineobj = connect;
    var $id = connect.id;
    var $text = connect.text;
    var obj = $("#"+$id)
    $(obj).html("");
    $(obj).html("<input type='text' class='diamond-text nodeinput' value='" + $text + "' onblur=\"labletextsave(this)\"/>");
});

function labletextsave(obj){
    $(obj).parent().html($(obj).val());
    lineobj.text=$(obj).val();
}
function nodetextsave(obj,val,type,doo){
    if(doo){
        val = $(".nodeinput").val();
        if(type==2){
            $(obj).parent().html("");
            $(obj).parent().append("<div class='diamond-text'>"+val+"</div>");
        }else{
            $(obj).parent().html("<div class='diamond-text'>"+val+"</div>");
        }
    }else{
        if(type==2){
            $(obj).parent().html("");
            $(obj).parent().append("<div class='diamond-text'>"+val+"</div>");
        }else{
            $(obj).parent().html("<div class='diamond-text'>"+val+"</div>");
        }
    }
}

function starnode(obj){
    nodeobj = obj;
}
function editflow(id) {
    if (id != null) {
        $("#editpage").attr({"src": "/workflow/flowpage.do?id=" + id});
    } else {
        alert("请先打开需要修改的流程");
    }
}
function editnode() {
    if (nodeobj != null) {
        var id = nodeobj.id;
        $("#editpage").attr({"src": "/workflow/nodepage.do?id=" + id});
    } else {
        alert("请先选择需要修改的节点");
    }
}

function editformnode(id){
    if (nodeobj != null) {
        layui.use( 'layer', function () {
            var layer = layui.layer;
            layer.open({
                type: 2
                , title: '编辑节点页面'
                , area: ['1000px', '800px']
                , shade: 0
                , maxmin: true
                , content: '/form_custom/formedit.do?wkflwId=' + id + '&formid=workflow,' + nodeobj.id
                , zIndex: layer.zIndex
            });
        });
        //var text = $(nodeobj).text();
        //$("#editpage").attr({"src": "/workflow/nodepage.do?id=" + nodeobj.id});
    } else {
        alert("请先选择需要创建页面的节点");
    }
}

function delnode() {
    if (nodeobj != null) {
        var text = $(nodeobj).text();
        var id = nodeobj.id;
        if (confirm("是否删除" + text + "的节点?")) {
            $.ajax({
                type: "POST",
                url: "/workflow/deletenode.do",
                data:{'id':id},
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                dataType: "html",
                async: false,
                success: function (data) {
                    if(data=="1"){
                        instance.removeAllEndpoints(id);
                        $(nodeobj).remove();
                        nodeobj = null;
                        num--;
                    }
                },
                error: function (message) {

                }
            });
        } else {
            nodeobj = null;
        }
    } else {
        alert("请先选择需要删除的节点");
    }
}

function editline() {
    if (lineobj != null) {
        var id = lineobj.id;
        console.log(id)
        $("#editpage").attr({"src": "/workflow/linepage.do?id=" + id});
    } else {
        alert("请先选择需要修改的线");
    }
}

function delline() {
    if (lineobj != null) {
        var text = $(lineobj).text();
        var id = lineobj.id;
        if (confirm("是否删除" + text + "的连线?")) {
            $.ajax({
                type: "POST",
                url: "/workflow/deleteline.do",
                data:{'id':id},
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                dataType: "html",
                async: false,
                success: function (data) {
                    if(data=="1"){
                        instance.deleteConnection(lineobj);
                    }
                },
                error: function (message) {

                }
            });
            lineobj = null;
        } else {
            lineobj = null;
        }
    } else {
        alert("请先选择需要删除的线");
    }
}
function manyValues() {
    var val = {}
    var url = window.location.search;
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        var key = new Array(strs.length);
        var value = new Array(strs.length);
        for (i = 0; i < strs.length; i++) {
            key[i] = strs[i].split("=")[0]
            value[i] = unescape(strs[i].split("=")[1]);
            val[key[i]] = value[i];
        }
    }
    return val;
}

function flowformsave(obj, formid) {
    if (!$(obj).hasClass("layui-btn-disabled")) {
        if ($(".layui-form").validate().form()) {
            $(obj).addClass("layui-btn-disabled");
            $("#" + formid).submit();
        }
    }
}

function flowformsend(obj, formid) {
    if (!$(obj).hasClass("layui-btn-disabled")) {
        $(obj).addClass("layui-btn-disabled");
        $("#" + formid + "_info").submit();
    }
}

function flowformreset(obj, formid) {
    if (!$(obj).hasClass("layui-btn-disabled")) {
        $(obj).addClass("layui-btn-disabled");
        var vs = manyValues();
        if (vs.workOrderNO != null) {
            oa.gotourl("/flowpage/gototask.do?procId=" + vs.procId + "&wkflwId=" + vs.wkflwId + "&nodeId=" + vs.nodeId + "&workOrderNO=" + vs.workOrderNO);
        } else {
            oa.gotourl("/flowpage/flowviewpage/" + formid + ".do?wkflwId=" + vs.wkflwId);
        }
    }
}

function flowformmodi(obj, formid) {
    if (!$(obj).hasClass("layui-btn-disabled")) {
        $(obj).addClass("layui-btn-disabled");
        document.getElementById(formid + "_info").action = "/flowpage/moditask.do";
        document.getElementById(formid + "_info").method = "GET";
        document.getElementById(formid + "_info").submit();

    }
}

function flowformcancel(obj, formid, wkflwId) {
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index)
    window.parent.parent.location.reload();
    if (!$(obj).hasClass("layui-btn-disabled")) {
        $(obj).addClass("layui-btn-disabled");
        oa.gotourl("/flowpage/flowviewpage/" + formid + ".do?wkflwId=" + wkflwId);
    }
}

function mywkfprocnum(wkflwId, starnode) {
    $.ajax({
        type: "POST",
        url: "/workflow/mywkfprocnum.do?wkflwId=" + wkflwId + "&starnode=" + starnode,
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        cache: false,
        dataType: "json",
        success: function (data) {
            $(".layui-badge.wc").text(data.WC);
            $(".layui-badge.wc").show();
            $(".layui-badge.jx").text(data.JX);
            $(".layui-badge.jx").show();
        },
        error: function () {
            alert("请求失败");
        }
    });
}

function openflowsub(type, wkflwId, starnode) {
    layer.closeAll();
    switch (type) {
        case 'lct':
            layer.open({
                type: 2
                , title: '查看流程图'
                , area: ['800px', '600px']
                , shade: 0
                , maxmin: true
                , content: '/workflow/workflowshow.do?id=' + wkflwId
                , zIndex: layer.zIndex
            });
            break;
        case 'wc':
            layer.open({
                type: 2
                , title: '查看已完成的流程'
                , area: ['1000px', '400px']
                , shade: 0
                , maxmin: true
                , content: '/workflow/workflowproc.do?wkflwId=' + wkflwId + '&type=wc&starnode=' + starnode
                , zIndex: 1
            });
            break;
        case 'jx':
            layer.open({
                type: 2
                , title: '查看进行中的流程'
                , area: ['1000px', '400px']
                , shade: 0
                , maxmin: true
                , content: '/workflow/workflowproc.do?wkflwId=' + wkflwId + '&type=jx&starnode=' + starnode
                , zIndex: 1
            });
            break;
        default:

            break;
    }
}
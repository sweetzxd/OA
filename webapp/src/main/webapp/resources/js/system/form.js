function formsave(obj, formid) {
    if (!$(obj).hasClass("layui-btn-disabled")) {
        if (formid != null) {
            var formobj = document.getElementById(formid);
            if ($(".layui-form").validate().form()) {
                $(obj).addClass("layui-btn-disabled");
                formobj.submit();
            }
        }
    }
}

function formadd(obj, pageid, formid) {
    if (!$(obj).hasClass("layui-btn-disabled")) {
        $(obj).addClass("layui-btn-disabled");
        oa.gotourl("/userpage/pageform/newformdata.do?type=add&pageid=" + pageid + "&formid=" + formid);
    }
}

function formmodi(obj, pageid, formid, recno) {
    if (!$(obj).hasClass("layui-btn-disabled")) {
        $(obj).addClass("layui-btn-disabled");
        oa.gotourl("/userpage/pageform/" + recno + ".do?type=modi&pageid=" + pageid + "&formid=" + formid);
    }
}

function formdel(obj, pageid, formid, recno) {
    if (!$(obj).hasClass("layui-btn-disabled")) {
        $(obj).addClass("layui-btn-disabled");
        oa.gotourl("/userpage/pagedelete/" + recno + ".do?pageid=" + pageid + "&formid=" + formid);
    }
}

function fromreset(formid) {
    document.getElementById(formid).reset();
}

function fromcancel(pageid, formid) {
    if(pageid=='childtable'){
           oa.gotourl("/userpage/viewpage/" + pageid + ".do?formid=" + formid);
    }else {
        oa.gotourl("/userpage/viewpage/" + pageid + ".do");
    }
}

function formback(pageid) {
    oa.gotourl("/userpage/viewpage/" + pageid + ".do");
}

function formsend(formid, recno){
    layer.open({
        type: 2 //此处以iframe举例
        , title: '发送消息'
        , offset: top + 'px'
        , area: ['750px', '500px']
        , shade: 0
        , maxmin: true
        , content: "/userpage/toSeeSendRecord.do?formid="+formid+"&recno="+recno
        , zIndex: layer.zIndex //重点1
        , success: function (layero) {
            //layer.setTop(layero); //重点2
        }
        , cancel: function (index, layero) {
            layer.close();
        }
    });
}


function selectTool(d,id,type){
    var option = "";
    $.ajax({
        type: "GET",
        url: "/userpage/getfs.do",
        data: {field: id,value: d[id]},
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        dataType: "json",
        async: false,
        success: function (data) {
            if(data.success==1){
                option = data.data;
            }
        },
        error: function (message) {
        }
    });
    if(option != "") {
        var html = '<select name="' + id + '" lay-filter="testSelect" lay-verify="required" data-value="' + d[id] + '" >' + option + '</select>';
        return html;
    }else{
        return d[id];
    }
}

function textTool(d,id,type){
    var html = '<input class="layui-input table-input-edit" value="'+d[id]+'">';
    return html;
}
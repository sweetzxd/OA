var layer =null,element = null;
layui.use(['layer', 'element'], function () {
    var $ = layui.jquery;
    layer = layui.layer;
    element = layui.element;
    $('#animation-left-nav').click(function () {
        var left = $('#leftmenu').attr("style");
        if (left != null && left.indexOf("display: none") >= 0) {
            $(".layui-side").animate({width: 'toggle'});
            $(".layui-body").animate({left: '200px'});
            $(".layui-footer").animate({left: '200px'});
            $('#animation-left-nav').attr("class", "layui-icon layui-icon-shrink-right oa-icon");
        } else {
            $(".layui-side").animate({width: 'toggle'});
            $(".layui-body").animate({left: '0px'});
            $(".layui-footer").animate({left: '0px'});
            $('#animation-left-nav').attr("class", "layui-icon layui-icon-spread-left oa-icon");
        }
    });

    $("body").on("click", "#leftmenu li a.nav-active", function () {
        var dataid = $(this);
        if ($(".layui-tab-title li[lay-id]").length <= 0) {
            active.tabAdd(dataid.attr("data-url"), dataid.attr("data-id"), dataid.attr("data-title"));
            active.tabChange(dataid.attr("data-id"));
        } else {
            var isData = false;
            $.each($(".layui-tab-title li[lay-id]"), function () {
                if ($(this).attr("lay-id") == dataid.attr("data-id")) {
                    isData = true;
                    active.tabChange(dataid.attr("data-id"));
                    if(isData){
                        $(".layui-show").find("iframe").attr("src", dataid.attr("data-url"));
                    }
                }
            });
            if (isData == false) {
                active.tabAdd(dataid.attr("data-url"), dataid.attr("data-id"), dataid.attr("data-title"));
                active.tabChange(dataid.attr("data-id"));
            }
        }
    });

    $("body").on("click", "#tab_open_page li", function () {
        var id = $(this).attr("lay-id");
        /*if (id != null) {
            var src = $(".layui-show").find("iframe").attr("src");
            $(".layui-show").find("iframe").attr("src", src);
        } else {
            $(".layui-show").find("iframe").attr("src", "homepage.do");
        }*/
        active.tabChange(id);
        if (id === 'home') {
            var src = $(".layui-show").find("iframe").attr("src");
            $(".layui-show").find("iframe").attr("src", src);
        }
    });

    $("body").on("click", "#tab_open_page li i.layui-tab-close", function () {
        var id = $(this).attr("data-id");
        active.tabDelete(id);
        var tabtitle = $(".layui-tab-title li");
        var upid = $(tabtitle[tabtitle.length-1]).attr("lay-id");
        active.tabChange(upid);
    });
    $("body").on("click", "#tab_sub_close", function () {
        var tabtitle = $(".layui-tab-title li");
        var ids = new Array();
        $.each(tabtitle, function (i) {
            var id = $(this).attr("lay-id");
            if($(this).attr("lay-id")!="home"){
                active.tabDelete(id);
            }
        })
        active.tabChange("home");
    });
    $("body").on("click", "#tab_sub_refresh", function () {
        var src = $(".layui-show").find("iframe").attr("src");
        $(".layui-show").find("iframe").attr("src", src);
    });

    $("body").on("click", "#modimyemp", function(){
        console.log("modimyemp")
        layer.closeAll();
        var othis = $(this), method = "modimyemp";
        active[method] ? active[method].call(this, othis) : '';
    });

    $("body").on("click", "#pwupdate", function(){
        console.log("pwupdate")
        layer.closeAll();
        var othis = $(this), method = "pwupdate";
        active[method] ? active[method].call(this, othis) : '';
    });

    $("body").on("click", "#addresslist", function(){
        console.log("addresslist")
        layer.closeAll();
        var othis = $(this), method = "addresslist";
        active[method] ? active[method].call(this, othis) : '';
    });
    $("body").on("click", "#uploadphoto", function(){
        console.log("uploadphoto")
        layer.closeAll();
        var othis = $(this), method = "uploadphoto";
        active[method] ? active[method].call(this, othis) : '';
    });
});

//触发事件
var active = {
    tabAdd: function (url, id, name) {
        var title = '<span>' + name + '</span><i class="layui-icon layui-unselect layui-tab-close" data-id="' + id + '">&#x1006;</i>';
        var content = '<iframe data-frameid="' + id + '" scrolling="auto" frameborder="no" src="' + url + '" width="100%" height="94%"></iframe>';
        element.tabAdd('homepage', {
            title: title,
            content: content,
            id: id
        })
    },
    tabRender: function (id) {
        element.render('homepage', id);
    },
    tabChange: function (id) {//切换
        element.tabChange('homepage', id);
    },
    tabDelete: function (id) {
        $('iframe[data-frameid='+id+']').parent().remove();
        element.tabDelete("homepage", id);
    },
    tabDeleteAll: function (ids) {
        $.each(ids, function (i, item) {
            element.tabDelete("homepage", item);
        })
    },
    modimyemp: function(){
        layer.open({
            type: 2 //此处以iframe举例
            ,title: '修改我的信息。'
            ,offset: 'auto'
            ,area: ['460px', '530px']
            ,shade: 0
            ,maxmin: true
            ,content: '/employees/modimyemployees.do'
            ,zIndex: layer.zIndex //重点1
            ,success: function(layero){
                layer.setTop(layero); //重点2
            }
        });
    },
    pwupdate: function(){
        layer.open({
            type: 2 //此处以iframe举例
            ,title: '重置密码'
            ,offset: 'auto'
            ,area: ['470px', '370px']
            ,shade: 0
            ,maxmin: true
            ,content: '/user/gotopwupdate.do'
            ,zIndex: layer.zIndex //重点1
            ,success: function(layero){
                layer.setTop(layero); //重点2
            }
        });
    },
    addresslist: function(){
        layer.open({
            type: 2 //此处以iframe举例
            ,title: '通讯录'
            ,offset: 'auto'
            ,area: ['800px', '600px']
            ,shade: 0
            ,maxmin: true
            ,content: '/employees/addresslist.do'
            ,zIndex: layer.zIndex //重点1
            ,success: function(layero){
                layer.setTop(layero); //重点2
            }
        });
    },
    offset: function(othis){
        var type = othis.data('type')
            ,text = othis.text();
        layer.open({
            type: 1
            ,offset: type
            ,id: 'layer'+type
            ,content: '<div style="padding: 20px 100px;">'+ text +'</div>'
            ,btn: '关闭全部'
            ,btnAlign: 'rt'
            ,time: 2000
            ,shade: 0
            ,yes: function(){
                layer.closeAll();
            }
        });
    },
    uploadphoto:function(){
        layer.open({
            type: 2 //此处以iframe举例
            ,title: '修改头像'
            ,offset: 'auto'
            ,area: ['470px', '370px']
            ,shade: 0
            ,maxmin: true
            ,content: '/employees/uploadphoto.do'
            ,zIndex: layer.zIndex //重点1
            ,success: function(layero){
                layer.setTop(layero); //重点2
            }
        });
    }
};
function newwebsocket(){
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://" + window.location.host + "/websocket.do");
    } else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket("ws://" + window.location.host + "/websocket.do");
    }
    window.onbeforeunload = function () {
        if (websocket != null) {
            websocket.close();
            websocket = null;
        }
    }
    websocket.onmessage = function (msg) {
        var data = msg.data;
        if (isJson(data)) {
            var jsonval = JSON.parse(data);
            var type = jsonval.type;
            var text = jsonval.value;
            if (type === "mymsgnum") {
                $("#openmymsg").find("span").text(jsonval.value);
            } else if (type === "mymsg") {
                $("#winpoptext").text(text);
                tips_pop();
            } else if (type === "online") {
                setOnline(text);
            }
        }
    };
    $("#winpop").bind("mouseenter", function () {
        clearTimeout(timeout);
    });
    $("#winpop").bind("mouseleave", function () {
        timeout = setTimeout(function () {
            $("#winpop").slideUp('slow');
        }, 1500);
    });
}
function closeWebSocket() {
    if (websocket != null) {
        websocket.close();
        websocket = null;
    }
}
function openmymsg(obj) {
    if ($("#home-mymsg").is(":visible")) {
        $("#home-mymsg").hide();
    } else {
        $("#msg_window").text("");
        $.ajax({
            type: "POST",
            url: "/message/getmymsg.do",
            data: {'userId': '${sessionScope.loginer.user}'},
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            dataType: "html",
            async: false,
            success: function (data) {
                $("#msg_window").append(data).trigger("create");
            },
            error: function (message) {
            }
        });
        var myTop = $(obj).offset().top + $(obj).outerHeight();
        var myLeft = $(obj).offset().left;
        $("#home-mymsg").css("left", myLeft - 150);
        $("#home-mymsg").css("top", myTop);
        $("#home-mymsg").fadeIn();
    }
}
function tips_pop() {
    $("#winpop").slideDown('slow');
    if (timeout != null) {
        clearTimeout(timeout);
        cons
    }
    timeout = setTimeout(function () {
        $("#winpop").slideUp('slow');
    }, 2500);
}

function addtab(url,id,name){
    if ($(".layui-tab-title li[lay-id]").length <= 0) {
        active.tabAdd(url, id, name);
    } else {
        var isData = false;
        $.each($(".layui-tab-title li[lay-id]"), function () {
            if ($(this).attr("lay-id") == id) {
                isData = true;
            }
        })
        if (isData == false) {
            active.tabAdd(url, id, name);
        }
    }
    active.tabChange(id);
}

function closetab(id){
    active.tabDelete(id);
    var tabtitle = $(".layui-tab-title li");
    var upid = $(tabtitle[tabtitle.length-1]).attr("lay-id");
    active.tabChange(upid);
}
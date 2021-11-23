oA.open = function (that, id, pathName, type,taskName) {
    var parameter = OA.str;
    switch (pathName) {
        case "employees":
            parameter = "?id=" + id + "&type=" + type;
            break;
        case "formselect":
            parameter = "?id=" + id + "&type=" + type + "&userid="+OA.operation.getQueryString('userid')+"&pageid="+taskName;
            break;
        case "department":
            parameter = "?id=" + id + "&type=" + type;
            break;
    }
    parameter = oA.url.alertPage(pathName) + parameter + "&rd=" + Math.random();
    oA.openPage = layer.open({
        type: 1,
        content: "<div class='scroll-wrapper'><iframe src='" + parameter + "' width='100%' height='" + document.documentElement.clientHeight + "' scrolling='auto' frameborder='0'></iframe></div>",
        anim: 'up',
        style: 'position:fixed; left:0; top:0; width:100%; height:100%; border: none; -webkit-animation-duration: .3s; animation-duration: .3s;',
        success: function(layero){
            $(layero).addClass("scroll-wrapper");//苹果 iframe 滚动条失效解决方式
        }
    });
}



var Manager = {
    url: "/weixin/seeFile.do" + location.search,
    init: function () {
        $.ajax({
            type: "GET",
            url: Manager.url,
            dataType: "json",
            success: function (d) {
                if (d.success) {
                    var file = d.data;
                    $("#fileName").text(file.fileName);
                    $("#uploadTime").text(oa.formatdate('datetime', file.uploadTime));
                    $("#recordName").text(oa.decipher('user', file.recordName));
                    $("#intro").text(file.intro);
                    $("#remark").text(file.remark);
                    var fileurls = file.fileAdd.split("|");
                    for (var i = 0, len = fileurls.length; i < len; i++) {
                        var furl = fileurls[i];
                        if (furl != null & furl != "") {
                            var f = furl.split("/");
                            var html = "<div class=\"enclosure-bar regular-bar\">\n" +
                                "       <div onclick=\"filedownloadaa('" + furl + "')\" ><i class=\"file iconfont mar-r5\">&#xe628;</i>" + f[f.length - 1] + "</div>\n" +
                                "       </div>";
                            $("#fileAdd").append(html);
                        }
                    }
                }
            }
        })
    }
};
Manager.init();

function filedownloadaa(url){
    var urlname = url.split("/");
    var name = urlname[urlname.length-1];
    name = name.substring(name.indexOf("-")+1);
    var val = {url:url,name:name};
    $("#downloadss").html(val);
    filedownload(url,name);
    //window.webkit.messageHandlers.filedownload.postMessage(val);
}
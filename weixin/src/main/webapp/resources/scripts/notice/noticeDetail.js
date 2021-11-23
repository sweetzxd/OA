var Manager={
    url : "/weixin/seeNotice.do"+location.search,
    init : function () {
        $.ajax({
            type: "GET",
            url: Manager.url,
            dataType: "json",
            success: function (d) {
                if(d.success){
                    var notice = d.data;
                    $(".my-mission-title").text(notice.bt18111000001);
                    $(".my-mission-content").text(notice.nr18111000001);
                    $(".mar-r").text(oa.decipher('user',notice.recordName));
                    $(".my-mission-footer").children("span").last().text(oa.formatdate('datetime',notice.recordTime));
                }
            }
        })
    }
}
Manager.init();
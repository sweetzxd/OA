var Manager={
    url : "/weixin/message/seeMessage.do"+location.search,
    init : function () {
        $.ajax({
            type: "GET",
            url: Manager.url,
            dataType: "json",
            success: function (d) {
                if(d.success){
                    var message = d.data[0];
                    $(".my-mission-title").text(oa.decipher('user',message.msgRecUser));
                    $(".my-mission-content").html(message.msgText);
                    $(".mar-r").text(oa.decipher('user',message.msgSendUser));
                    $(".my-mission-footer").children("span").last().text(oa.formatdate('datetime',message.msgSendTime));
                    //$(".my-mission-content").val(message);
                    if(message.msgFile!=null){
                        $(".my-wrap.mar-b").append("<div class='pad-lr'><a href='javascript:;' class='view-attach'>查看附件</a></div>");
                    }
                }
            }
        })
    }
}
Manager.init();
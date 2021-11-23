$(function () {
    var param = location.search;
    var type = param.substring(1,param.length).split("=")[1];
    var itemIndex = 0;
    var tabLoadEndArray = [false, false, false];
    var tabLenghtArray = [28, 15, 47];
    var tabScroolTopArray = [0, 0, 0];
    var pageNum = 1;
    // dropload
    var dropload = $('.marin-wrap').dropload({
        scrollArea: window,
        loadDownFn: function (me) {
            setTimeout(function () {
                if (tabLoadEndArray[itemIndex]) {
                    me.resetload();
                    me.lock();
                    me.noData();
                    me.resetload();
                    return;
                }
                var result = '';
                var noticeList = null;
                $.ajax({
                    url: "/weixin/selectAllTzgg.do",
                    type: 'post',
                    dataType: 'json',
                    data:{page:pageNum,limit:10,type:type},
                    async:false,
                    success: function(d, status){
                        noticeList = d.data;
                        if(pageNum==1){//初始化列表总长度     只初始化一次
                            tabLenghtArray[itemIndex] = d.count;
                        }
                    }
                });

                pageNum = pageNum +1;
                for (var index = 0; index < 10; index++) {
                    if (tabLenghtArray[itemIndex] > 0) {
                        tabLenghtArray[itemIndex]--;
                    } else {
                        tabLoadEndArray[itemIndex] = true;
                        break;
                    }
                    var notice = noticeList[index];
                    result += "<tr class='my-mission-list' onclick=\"seeDetail('"+notice.tzgg181110001_recorderNO+"',"+type+")\">\n" +
                        "           <td class=''>"+notice.tzgg181110001_bt18111000001+"</td>\n" +
                        "           <td class=''><span class='mar-r'>"+oa.decipher('user',notice.tzgg181110001_recordName)+"</span><span class='mar-r'>"+notice.tzgg181110001_recordTime+"</span></td>\n" +
                        "      </tr>";

                }
                $('.my-mission').eq(itemIndex).append(result);
                me.resetload();
            }, 500);
        },
        domDown: {
            domClass: 'dropload-down',
            domRefresh: '<div class="dropload-refresh">上拉加载更多</div>',
            domLoad: '<div class="dropload-load"><span class="loading"></span>加载中...</div>',
            domNoData: '<div class="dropload-noData">已无数据</div>'
        }
    });
});
function seeDetail(id,type){
    if(type=="1"){
        window.location.href="/views/notice/noticeDetail.html?id="+id;
    }else{
        window.location.href="/views/notice/announceDetail.html?id="+id;
    }

}
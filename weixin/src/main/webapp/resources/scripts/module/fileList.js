$(function () {
    var param = location.search;
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
                var fileList = null;
                $.ajax({
                    url: "/weixin/file/getUserFileList.do",
                    type: 'get',
                    dataType: 'json',
                    data:{page:pageNum,limit:10},
                    async:false,
                    success: function(d, status){
                        fileList = d.data;
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
                    var file = fileList[index];
                    result += "<tr class='my-mission-list'>\n" +
                        "      <td class='' onclick=\"seeDetail('"+file.fileId+"')\">"+file.fileName+"</td>\n" +
                        "      </tr>"
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
function seeDetail(id){
    window.location.href="/views/module/FileDetail.html?id="+id;
}
$(function () {
    var param = location.search;
    var itemIndex = 0;
    var tabLoadEndArray = [false, false, false];
    var tabLenghtArray = [28, 15, 47];
    var tabScroolTopArray = [0, 0, 0];
    var pageNum = 1;
    // dropload   手机上拉刷新
    var dropload = $('.marin-wrap').dropload({
        scrollArea: window,
        loadDownFn: function (me) {
            if (tabLoadEndArray[itemIndex]) {
                me.resetload();
                me.lock();
                me.noData();
                me.resetload();
                return;
            }
            var result = '';
            var fileList = null;
            var count = 0;
            $.ajax({
                url: "/weixin/file/selectalldepartment.do",
                type: 'get',
                dataType: 'json',
                data:{page:pageNum,limit:10},
                async:false,
                success: function(d){
                    fileList = d.data;
                    count = d.count;
                    if(pageNum==1){//初始化列表总长度     只初始化一次
                        tabLenghtArray[itemIndex] = d.count;
                    }
                }
            });

            pageNum = pageNum +1;
            for (var index = 0; index < count; index++) {
                if (tabLenghtArray[itemIndex] > 0) {
                    tabLenghtArray[itemIndex]--;
                } else {
                    tabLoadEndArray[itemIndex] = true;
                    break;
                }
                var file = fileList[index];
                result += "<tr class='my-mission-list'>\n" +
                    "           <td class=''>"+file.deptName+"</td><td class=''>"+file.headName+"</td>\n"
                    "      </tr>"
            }
            $('.my-mission').eq(itemIndex).append(result);
            me.resetload();
        },
        domDown: {
            domClass: 'dropload-down',
            domRefresh: '<div class="dropload-refresh">上拉加载更多</div>',
            domLoad: '<div class="dropload-load"><span class="loading"></span>加载中...</div>',
            domNoData: '<div class="dropload-noData">没有更多啦</div>'
        }
    });
});
$(function () {
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
                var taskSenderList = null;
                $.ajax({
        			url: "/weixin/selectusertask.do",
        			type: 'post',
        			dataType: 'json',
        			data:{page:pageNum,limit:10},
        			async:false,
        			success: function(d, status){
                        taskSenderList = d.data;
                        if(pageNum==1){//初始化列表总长度     只初始化一次
                            tabLenghtArray[itemIndex] = d.count;
                        }
        			}
        		});
                console.log(taskSenderList)
                console.log(itemIndex)
                pageNum = pageNum +1;
                for (var index = 0; index < 10; index++) { 
                    if (tabLenghtArray[itemIndex] > 0) {
                        tabLenghtArray[itemIndex]--;
                    } else {
                        tabLoadEndArray[itemIndex] = true;
                        break;
                    }
                    var task = taskSenderList[index];
                    var procID = task.procID;
                    var wkflwID = task.wkflwID;
                    var wkfNode = task.wkfNode;
                    var workOrderNO = task.workOrderNO;
                    var url = "?procId="+procID+"&wkflwID="+wkflwID+"&wkfNode="+wkfNode+"&workOrderNO="+workOrderNO;
                    result += "<tr class=\"my-mission-list\">\n" +
                        "          <td class='renwu-td' onclick=\"seeDetail('"+url+"')\">\n" +
                        "              <div class='mar-b5'>"+task.taskTitle+"</div>\n" +
                        "              <div class='box-bar'><div class='renwu-name cell'>"+oa.decipher('user',task.modifyName)+"</div><div class='box-bar-list'>"+oa.formatdate('datetime',task.modifyTime)+"</div></div>\n" +
                        "          </td>\n" +
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
function seeDetail(url){
    console.log(url);
    window.location.href="/views/task/taskDetail.html"+url;
}
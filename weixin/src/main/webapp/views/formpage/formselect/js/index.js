oA.getPageType = OA.operation.getQueryString('type') === 'single' ? OA.star : OA.end;
oA.success = OA.object("showContent", function (data) {
    oA.arr = OA.array();
    Object.keys(data).splice(0, Object.keys(data).length).forEach(function (key) {
        oA[key] = Object.getOwnPropertyDescriptors(data[key]);
    });
    this.load = function () {
        oA.ele.content.html(oA.transform.content(oA.ele.main, {
            field: this.convert(),
            tbody: this.tbody(),
            eleType: oA.getPageType
        }));
        this.recover(oA._name, oA._Value);
    };
    this.convert = function () {
        for (var key in oA.field) {
            for (var val in oA.thead) {
                if (oA.field[key].value.name === oA.thead[val].value.name) {
                    if (oA.thead[val].value.title) {
                        oA.arr.push(oA.thead[val].value.title);
                    }
                }
            }
        }
        return oA.arr.reverse();
    };
    this.tbody = function () {
        var num = 0;
        var object = OA.object();
        for (var val in data.tbody) {
            object[num] = OA.array();
            object[num].push(data.tbody[val].recorderNO);
            for (var key in data.field) {
                if (Object.keys(data.tbody[val]).includes(data.field[key].name)) {
                    object[num].push(data.tbody[val][data.field[key].name]);
                }
            }
            num++;
        }
        return object;
    };
    this.recover = function (ele, hidden) {
        if (ele.text().trim()) {
            hidden.val().split(';').forEach(function (value) {
                oA.ele.content.find(oA.eleType).filter("[value="+value+"]").attr({checked: oA.ele.checked});
            });
        }
    };
    this.ready = function () {
        ["_name", "_Value"].forEach(function (item) {
            oA[item] = parent.$("#" + OA.operation.getQueryString('id') + item);
        });
        this.load();
    };
    return this;
});

$(function () {
    oA.hasNotMask = OA.star;
    oA.ele = OA.ele(["#txtdate", "#txtdate2", "!header", "#footer", "#content", "main", "radio",
        "checkbox", "checked", "!:text", "#offCanvasBtn", "#searchBtn"]);
    oA.ele.dscriptText = '请选择文本';
    oA.ele.pageNumber = 0;
    oA.eleType = ":" + (oA.getPageType ? oA.ele.radio : oA.ele.checkbox);
    new oA.before.load(0).getAjaxData();
    oA.ele.header.children().slice(1, 2).click(function () {
        window.location.reload();
    });
    oA.ele.footer.find('a').first().click(function (e) {
        var connectiondata = OA.array(), connectionText = OA.array(),
            onChecked = oA.ele.content.find(oA.eleType).filter(":" + oA.ele.checked);
        if (onChecked.size()) {
            onChecked.each(function () {
                connectiondata.push(this.value);
                connectionText.push($(this).parent().parent().next().text());
            });
            oA._name.text(connectionText.join(';'));
            if(OA.operation.getQueryString('type') == 'more'){
                oA._Value.val(connectiondata.join(';')+";");
            }else{
                oA._Value.val(connectiondata.join(';'));
            }
            oA._Value.valid();
            parent.layer.close(parent.oA.openPage);
        } else {
            layer.open({
                content: oA.ele.dscriptText,
                skin: 'msg',
                time: 2
            });
        }
    }).end().last().click(function (e) {
        parent.layer.close(parent.oA.openPage);
    });
    oA.ele.searchBtn.click(function () {
        var syName = ["key", "start", "end"],
            newObject = OA.object(oA.ele.checked),
            inpuText = oA.ele.text,
            agn = OA.array(inpuText.size(), OA.star);
        for (var i = 0; i < inpuText.size(); i++) {
            if (inpuText.eq(i).val()) {
                newObject.checked[syName[i]] = inpuText.eq(i).val() || OA.str;
            }
            agn[i] = inpuText.eq(i).val() ? OA.star : OA.end;
        }
        if((!agn[1]&&!agn[2]&&agn[0])||(!agn[0]&&agn[1]&&agn[2])||(agn[0]&&agn[1]&&agn[2])){
            new oA.before.load(0).getAjaxData(newObject.checked);
            offCanvasWrapper.offCanvas('close');
        } else {
            oA.msg('请输入关键词 或 选择时间');
        }
    });
});

oA.before = OA.object('load', function (pageNumber) {
    this.getAjaxData = function () {
        var postData = {
            userid: OA.operation.getQueryString('userid'),
            pageid: OA.operation.getQueryString('pageid'),
            page: pageNumber
        }
        if (arguments.length) {
            if (arguments[0] instanceof Object){
                Object.assign(postData, arguments[0]);
            }
        }else{
            this.start();
        }
        OA.axios(oA.url.tableform.formtablefield, postData, 'get').done(function (data) {
            if (pageNumber) {
                data.tbody.forEach(function (item) {
                    OA.data.tbody.push(item);
                });
                myScroll.refresh();
            } else {
                OA.data = data;
            }
            new oA.success.showContent(OA.data).ready();
        });

    };
    this.pullUpAction = function () {
        oA.ele.pageNumber++;
        new oA.before.load(oA.ele.pageNumber).getAjaxData(OA.star);
    };
    this.start = function () {
        var currYear = (new Date()).getFullYear();
        var opt = {};
        opt.date = {
            preset: 'date'
        };
        opt.datetime = {
            preset: 'datetime'
        };
        opt.time = {
            preset: 'time'
        };
        opt.default = {
            theme: 'android-ics light', //皮肤样式
            display: 'modal', //显示方式
            mode: 'scroller', //日期选择模式
            dateFormat: 'yyyy-mm-dd',
            lang: 'zh',
            showNow: true,
            nowText: "今天",
            startYear: currYear - 50, //开始年份
            endYear: currYear + 10 //结束年份
        };
        oA.ele.txtdate.mobiscroll($.extend(opt['date'], opt['default']));
        oA.ele.txtdate2.mobiscroll($.extend(opt['date'], opt['default']));
    };
    return this;
});

mui.init({
    swipeBack: false,
});
//侧滑容器父节点
var offCanvasWrapper = mui('#offCanvasWrapper');
//主界面容器
var offCanvasInner = offCanvasWrapper[0].querySelector('.mui-inner-wrap');
//菜单容器
var offCanvasSide = document.getElementById("offCanvasSide");
//移动效果是否为整体移动
var moveTogether = false;
//主界面和侧滑菜单界面均支持区域滚动；
mui('#offCanvasSideScroll').scroll();
mui('#offCanvasContentScroll').scroll();
//实现ios平台的侧滑关闭页面；
if (mui.os.plus && mui.os.ios) {
    offCanvasWrapper[0].addEventListener('shown', function (e) { //菜单显示完成事件
        plus.webview.currentWebview().setStyle({
            'popGesture': 'none'
        });
    });
    offCanvasWrapper[0].addEventListener('hidden', function (e) { //菜单关闭完成事件
        plus.webview.currentWebview().setStyle({
            'popGesture': 'close'
        });
    });
}

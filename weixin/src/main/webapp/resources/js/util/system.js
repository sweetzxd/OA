//接收一个值
function oneValues(){
    var result;
    var url=window.location.search; //获取url中"?"符后的字串
    if(url.indexOf("?")!=-1){
        result = url.substr(url.indexOf("=")+1);
    }
    return result;
}


//接收多值
function manyValues(){
    var val = {}
    var url=window.location.search;
    if(url.indexOf("?")!=-1){
        var str = url.substr(1);
        strs = str.split("&");
        var key=new Array(strs.length);
        var value=new Array(strs.length);
        for(i=0;i<strs.length;i++){
            key[i]=strs[i].split("=")[0]
            value[i]=unescape(strs[i].split("=")[1]);
            val[key[i]] = value[i];
        }
    }
    return val;
}
var oa = {
    getip: function () {
        var curWwwPath = window.document.location.href;
        var pos = curWwwPath.indexOf(window.document.location.pathname);
        var localhostPaht = curWwwPath.substring(0, pos);
        return localhostPaht;
    },
    decipher: function (type, value, data) {
        //console.log(type, value, data);
        var name = null;
        if (type != null && value != null && value.length > 0) {

            var val = value;
            if (data != null && data != "") {
                val = value + "|" + data;
            }
            //console.log(val);
            $.ajax({
                type: "POST",
                url: "/decipher.do",
                dataType: "html",
                data: {type: type, value: val},
                async: false,
                success: function (data) {
                    name = data;
                    console.log(name);
                },
                error: function (data) {
                    console.log(data)
                }
            });
        }
        return name == null ? "" : name;
    },
    formatdate: function (type, value) {
        if (value != null) {
            var fmt = "yyyy-MM-dd";
            if (type == 'datetime') {
                fmt = "yyyy-MM-dd hh:mm:ss";
            }
            var date = new Date(value);
            var o = {
                "M+": date.getMonth() + 1,
                "d+": date.getDate(),
                "h+": date.getHours(),
                "m+": date.getMinutes(),
                "s+": date.getSeconds()
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o) {
                if (new RegExp("(" + k + ")").test(fmt)) {
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                }
            }
            return fmt;
        } else {
            return "";
        }
    },
    dataidtoname: function (type, value) {
        var name = null;
        if (type != null && value != null && value.length > 0) {
            var url = "/" + type + "idtoname.do?value=" + value;
            $.ajax({
                type: "POST",
                url: url,
                dataType: "html",
                async: false,
                success: function (data) {
                    name = data;
                },
                error: function (data) {
                    console.log(data)
                }
            });
        }
        return name == null ? "" : name;
    },
    editor: function (name, d) {
        var json = JSON.stringify(d);
        return "<a href='javascript:void(0)' onclick='openEditor(\"" + name + "\"," + json + ")' class='layui-table-link'>查看详细</a>";
    },
    trim: function (str) {
        if (str != null && str.length > 0) {
            return str.replace(/(^\s*)|(\s*$)/g, "");
        } else {
            return "";
        }
    },
    getdate: function () {
        var date = new Date();
        var seperator1 = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate;
        return currentdate;
    },
    gotourl: function (url) {
        window.location.href = url;
    },
    window: function (title, url, width, height) {
        if (width == null || width == "") {
            width = 800;
        }
        if (height == null || height == "") {
            height = 600;
        }
        if (!width.indexOf("px")) {
            width += "px";
        }
        if (!height.indexOf("px")) {
            height += "px";
        }
        layer.closeAll();
        var id = layer.open({
            type: 2
            , title: title
            , offset: 'auto'
            , area: [width, height]
            , shade: 0
            , maxmin: true
            , content: url
            , zIndex: layer.zIndex
            , success: function (layero) {
                layer.setTop(layero);
            }
        });
        return id
    },
    closeWindow: function (id) {
        if (id != null && id != "") {
            layer.close(id);
        } else {
            layer.closeAll();
        }
    },
    openWindow: function (url, title, saveurl) {
        var index = layer.open({
            type: 2 //此处以iframe举例
            , id: 'myOpenWindow'
            , title: title == null ? "" : title
            , area: ['800px', '600px']
            , maxmin: true
            , content: url
            , btn: ['保存', '关闭']
            , yes: function (active) {
                var datas = fun();
                $.ajax({
                    type: "POST",
                    url: saveurl,
                    data: datas,
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    cache: false,
                    dataType: "html",
                });
                layer.closeAll();
            }
            , btn2: function () {
                layer.close();
            }
        });
        return index;
    },
    open2Window: function (url, title) {
        var index = layer.open({
            type: 2 //此处以iframe举例
            , id: 'myOpenWindow'
            , title: title == null ? "" : title
            , area: ['800px', '600px']
            , maxmin: true
            , content: url
            , btn: []
        });
        return index;
    },
    openmsg:function (text){
        layer.msg(text, {
            area: ["280px","180px"],
            shade: 0,
            time: 3000,
            offset: 'auto',
            anim: 5,
            scrollbar: false,
        });
    },
    get: function (url, data) {
        return oa.ajax("GET", url, data);
    },
    post: function (url, data) {
        return oa.ajax("POST", url, data);
    },
    ajax: function (type,url, data) {
        var value = null;
        $.ajax({
            method: type,
            url: url,
            data: data,
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            dataType: "html",
            async: false,
            success: function (data) {
                value = data;
            },
            error: function (message) {
                layer.msg(JSON.stringify(message));
            }
        });
        return value
    },
    getTypeName: function (fieldType) {
        var name = "";
        if (fieldType == 'text') {
            name = "字符串";
        } else if (fieldType == 'textarea') {
            name = "长文本";
        } else if (fieldType == 'int') {
            name = "整数";
        } else if (fieldType == 'float') {
            name = "小数";
        } else if (fieldType == 'date') {
            name = "日期";
        } else if (fieldType == 'datetime') {
            name = "日期时间";
        } else if (fieldType == 'select') {
            name = "单选";
        } else if (fieldType == 'selects') {
            name = "多选";
        } else if (fieldType == 'user') {
            name = "单选人";
        } else if (fieldType == 'users') {
            name = "多选人";
        } else if (fieldType == 'dept') {
            name = "单部门";
        } else if (fieldType == 'depts') {
            name = "多部门";
        } else if (fieldType == 'form') {
            name = "携带字段(单)";
        } else if (fieldType == 'forms') {
            name = "携带字段(多)";
        } else if (fieldType == 'upload') {
            name = "单附件";
        } else if (fieldType == 'uploads') {
            name = "多附件";
        } else if (fieldType == 'child') {
            name = "子表";
        } else if (fieldType == 'editor') {
            name = "富文本";
        }
        return name;
    },
    htmlgoback:function () {
        var u = navigator.userAgent, app = navigator.appVersion;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1;
        var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
        if (isAndroid) {
            //这个是安卓操作系统
            window.android.finish();
        }
        if (isIOS) {
            //这个是ios操作系统
            navigationPoP('');
        }

    }
};

/*window.alert = function (txt) {
    layer.msg(txt, {
        area: ["280px","180px"],
        shade: 0,
        time: 3000,
        offset: 'auto',
        anim: 5,
        scrollbar: false,
    });
};*/

function addnav(url,id,name){
    window.parent.addtab(url,id,name);
}

function isJson(str) {
    if (typeof str == 'string') {
        try {
            var obj = JSON.parse(str);
            if (typeof obj == 'object' && obj) {
                return true;
            } else {
                return false;
            }
        } catch (e) {
            console.log('error：' + str + '!!!' + e);
            return false;
        }
    }
}

function checkmymsg(msgId, msgType, msgUrl) {
    $.ajax({
        type: "POST",
        url: "../message/updateMessage.do",
        dataType: "html",
        data: {msgId: msgId, msgStatus: 2},
        async: false,
        success: function (data) {
            if (msgType == 0) {
                layer.closeAll();
                layer.open({
                    type: 2 //此处以iframe举例
                    , title: '消息详情'
                    , offset: 'auto'
                    , area: ['900px', '600px']
                    , shade: 0
                    , maxmin: true
                    , content: "../message/seeMessageInfo.do?msgId=" + msgId
                    , zIndex: layer.zIndex //重点1
                    , success: function (layero) {
                        layer.setTop(layero); //重点2
                    }
                });
            } else if (msgType == 1) {
                window.open(msgUrl);
            }
        },
        error: function (data) {
            console.log(data)
        }
    });
}

function seeTzggInfo(recorderNO, type) {
    layer.closeAll();
    layer.open({
        type: 2 //此处以iframe举例
        , title: type + '详情'
        , offset: 'auto'
        , area: ['800px', '600px']
        , shade: 0
        , maxmin: true
        , content: oa.getip() + "/commonurl/seeTzggInfo.do?recorderNO=" + recorderNO
        , zIndex: layer.zIndex //重点1
        , success: function (layero) {
            layer.setTop(layero); //重点2
        }
    });
}

function openEditor(name, json) {
    layer.open({
        type: 1 //Page层类型
        , area: ['500px', '300px']
        , title: '查看富文本'
        , shade: 0 //遮罩透明度
        , content: '<div style="padding:20px">' + json[name] + '</div>'
    });
}

function optExcel(formId) {
    layer.closeAll();
    layer.open({
        type: 2 //此处以iframe举例
        , title: '导入Excel'
        , offset: 'auto'
        , area: ['600px', '450px']
        , shade: 0
        , maxmin: true
        , content: "/userpage/gotoExcel.do?formId=" + formId
        , zIndex: layer.zIndex //重点1
        , success: function (layero) {
            layer.setTop(layero); //重点2
        }
    });
}


function export1(formId) {
    layer.closeAll();
    //弹出即全屏
    var index = layer.open({
        type: 2 //此处以iframe举例
        , title: '导出Excel'
        , area: ['900px', '100%']
        , shade: 0
        , content: "/userpage/exportExcel.do?formId=" + formId
        , zIndex: layer.zIndex //重点1
        , success: function (layero) {
            layer.setTop(layero); //重点2
        }
    });
    layer.full(index);
}


//接收一个值
function oneValues(){
    var result;
    var url=window.location.search; //获取url中"?"符后的字串
    if(url.indexOf("?")!=-1){
        result = url.substr(url.indexOf("=")+1);
    }
    return result;
}


//接收多值
function manyValues(){
    var val = {}
    var url=window.location.search;
    if(url.indexOf("?")!=-1){
        var str = url.substr(1);
        strs = str.split("&");
        var key=new Array(strs.length);
        var value=new Array(strs.length);
        for(i=0;i<strs.length;i++){
            key[i]=strs[i].split("=")[0]
            value[i]=unescape(strs[i].split("=")[1]);
            console.log(key[i],value[i]);
            val[key[i]] = value[i];
        }
    }
    return val;
}

/*function navigationPoP(){
    window.webkit.messageHandlers.navigationPoP.postMessage("");
}*/

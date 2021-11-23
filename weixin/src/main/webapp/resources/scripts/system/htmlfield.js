var File = new Object({
    getFile: function (field) {
        var html = "";
        var data = getData(field)
        oA.transform.content.helper("getidtoname", function (type, value) {
            return oa.decipher(type, value);
        });
        oA.transform.content.helper("getfilehtml", function (id, url) {
            var html = "";
            if (url != null && url != "") {
                var urls = url.split("|");
                for (var i = 0, len = urls.length - 1; i < len; i++) {
                    var u = urls[i];
                    var names = u.split("/");
                    html += filedTableValue(id, u, names[names.length]);
                }
            }
            return html;
        });
        switch (field.type) {
            case 'pkid': //单行文本
                html = oA.transform.content("form-pkid", data);
                break;
            case 'text': //单行文本
                html = oA.transform.content("form-text", data);
                break;
            case 'textarea': //多行文本
                html = oA.transform.content("form-textarea", data);
                break;
            case 'select': //单选
                html = oA.transform.content("form-select", data);
                break;
            case 'selects': //多选
                html = oA.transform.content("form-selects", data);
                break;
            case 'date': //日期选择
                html = oA.transform.content("form-date", data);
                break;
            case 'datetime': //日期时间
                html = oA.transform.content("form-datetime", data);
                break;
            case 'int': //整数
                html = oA.transform.content("form-int", data);
                break;
            case 'decimal': //小数
                html = oA.transform.content("form-decimal", data);
                break;
            case 'user': //单选人
                html = oA.transform.content("form-user", data);
                break;
            case 'users': //多选人
                html = oA.transform.content("form-users", data);
                break;
            case 'dept': //单选部门
                html = oA.transform.content("form-dept", data);
                break;
            case 'depts': //多选部门
                html = oA.transform.content("form-depts", data);
                break;
            case 'form': //携带添加(单)
                html = oA.transform.content("form-form", data);
                break;
            case 'forms': //携带添加(多)
                html = oA.transform.content("form-forms", data);
                break;
            case 'upload': //单文件上传
                html = oA.transform.content("form-upload", data);
                break;
            case 'uploads': //多文件上传
                html = oA.transform.content("form-uploads", data);
                break;
            case 'child': //子表
                html = "";
                break;
            case 'editor': //富文本
                html = oA.transform.content("form-textarea", data);
                break;
            default:
                html = oA.transform.content("form-text", data);
                break;
        }
        return html;
    }
});

/*格式化字段参数*/
function getData(field){
    var id = field.name;
    var name = field.title;
    var value = field.value;
    var length = field.length;
    var required = field.required;
    var placeholder = field.placeholder;
    var special = field.special;
    var decimal = field.decimal;
    var options = field.option;
    var option = [];
    if (options != null && options !== "") {
        var opval = options.split(/[\n,]/g);
        for (var i = 0, len = opval.length; i < len; i++) {
            var kn = opval[i].split(";");
            option[i] = {"id": kn[0], "name": kn[1]}
        }
    }
    if (length == null || length == "") {
        length = "30";
    }
    if (value == '辅助说明') {
        value = "";
    }
    if (placeholder == '辅助说明') {
        placeholder = "";
    }
    var validate = "";
    var table = id.split("_")[0];
    if (special != null && special.indexOf("laterTo") >= 0) {
        special = special.substring(1, special.length - 1);
        var spec = special.split("-");
        var val = table+"_"+spec[1]+"_Value";
        validate = " laterTo="+val+" ";
    } else if (special != null && special.indexOf("toLater") >= 0) {
        special = special.substring(1, special.length - 1);
        var spec = special.split("-");
        var val = table+"_"+spec[1]+"_Value";
        validate = " toLater="+val+" ";
    }
    var data = {
        "id": id,
        "name": name,
        "value": value,
        "length": length,
        "placeholder": placeholder,
        "decimal": decimal,
        "option": option,
        "type": field.type,
        "taskName": field.taskName,
        "todate": getToDate(),
        "required": required == 1 ? "required" : "",
        "validate": validate
    };
    return data;
}
function getToDate() {
    return "";
}

function getFieldHtml(pageid, formid) {
    OA.axios("/weixin/tableform/formfield.do", {
        userid: OA.operation.getQueryString("userid"),
        status: OA.operation.getQueryString("status"),
        pageid: OA.operation.getQueryString("pageid"),
        formid: OA.operation.getQueryString("formid"),
        recno: OA.operation.getQueryString("recno")
    }).done(function (data) {
        var fields = data.field;
        $("#formtitle").text(data.title);
        fields.forEach(function (item) {
            $('#formtable').append(File.getFile(item));
            switch (item.type) {
                case "selects":
                    $("#" + item.name + "_Value").selectpicker('refresh');
                    break;
                case "textarea":
                    autoTextarea(document.getElementById(item.name + "_Value"));
                    break;
                case "date":
                    fielddate(item.name + "_Value");
                    break;
                case "datetime":
                    fielddatetime(item.name + "_Value");
                    break;
                case "upload":
                    //fieldupload(item.name + "_Value");
                    break;
                default:
                    break;
            }
        });
    });
}

function fielddate(id) {
    // 日期控件
    var currYear = (new Date()).getFullYear();
    var def = {
        theme: 'android-ics light', //皮肤样式
        display: 'modal', //显示方式
        mode: 'scroller', //日期选择模式
        dateFormat: 'yyyy-mm-dd',
        lang: 'zh',
        showNow: true,
        nowText: "今天",
        startYear: currYear - 3, //开始年份
        endYear: currYear + 2, //结束年份
        onSelect: function (ViewText, inst) {// 返回值
            $(this).valid();
        }
    };
    $("#" + id).mobiscroll($.extend({preset: 'date'}, def));
}

function fielddatetime(id) {
    // 日期控件
    var currYear = (new Date()).getFullYear();
    var def = {
        theme: 'android-ics light', //皮肤样式
        display: 'modal', //显示方式
        mode: 'scroller', //日期选择模式
        dateFormat: 'yyyy-mm-dd',
        lang: 'zh',
        showNow: true,
        nowText: "今天",
        startYear: currYear - 3, //开始年份
        endYear: currYear + 2, //结束年份
        onSelect: function (ViewText, inst) {// 返回值
            $(this).valid();
        }
    };
    $("#" + id).mobiscroll($.extend({preset: 'datetime'}, def));
}

function fieldupload0(id) {
    var $input = $("#" + id);
    $input.change(function () {
        var u = navigator.userAgent, app = navigator.appVersion;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1;
        var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
        if (isAndroid) {
            //这个是安卓操作系统
            android.openFile(id);
        }
        if (isIOS) {
            //这个是ios操作系统
            window.webkit.messageHandlers.fieldupload.postMessage(id);
        }

    })
}

function fieldupload2(id) {
    var u = navigator.userAgent, app = navigator.appVersion;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1;
    var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
    if (isAndroid) {
        //这个是安卓操作系统
        android.openFile(id);
    }
    if (isIOS) {
        //这个是ios操作系统
        /*window.webkit.messageHandlers.fieldupload.postMessage("");*/
        fieldupload(id);
    }

}

var upLoadAccessoryImageParameter = function(name, url,id) {
    $('#' + id + '_text').append(filedTableValue(id, url, name));
    var file = url + "|" + $('#' + id).val();
    $('#' + id).val(file);
};

function getUploadName(datastr) {
    var id = datastr.id;
    var name = datastr.name;
    var file = datastr.file;
    $('#' + id + '_text').append(filedTableValue(id, file, name));
    var url = file + "|" + $('#' + id).val();
    $('#' + id).val(url);
}

function fileLoad(id, ele) {
    var formData = new FormData();

    var name = $(ele).val();
    var files = $(ele)[0].files[0];
    formData.append("file", files);
    formData.append("name", name);

    id = id.substring(0, id.length - 6);
    $.ajax({
        url: "/uploading/fileuploads.do?type=appload",
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (responseStr) {
            var json = JSON.parse(responseStr);
            $('#' + id + '_text').append(filedTableValue(id, json.file, json.name));
            var url = json.file + "|" + $('#' + id).val();
            $('#' + id).val(url);
        },
        error: function (responseStr) {
            alert("出错啦");
        }
    });
}

function filedTableValue(id, file, name) {
    var tr = "<tr class=\"enclosure-bar my-mission-list on\">\n" +
        " <td class=\"\"><i class=\"file iconfont mar-r5\">&#xe628;</i>" + name + "</td>\n" +
        "<td style=\"background-color: #00FFFF;width: 50px;\" class=\"enclosure-btn\"><a href=\"javascript:;\" class=\"download-btn\" onclick=\"deletefile(this,'" + id + "','" + file + "')\">删除</a></td>" +
        "    </tr>";
    return tr;
}

function deletefile(obj, id, file) {
    obj.parentNode.parentNode.remove();
    var url = $('#' + id).val();
    console.log(url)
    var urls = url.split("|");
    console.log(urls)
    var val = "";
    for (var i = 0, len = urls.length - 1; i < len; i++) {
        var u = urls[i];
        if (u != file) {
            val += u + "|";
        }
    }
    console.log(val)
    $('#' + id).val(val);
}
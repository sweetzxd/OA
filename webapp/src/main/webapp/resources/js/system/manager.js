/**
 * 初始化layui框架
 * 并对相应的对象进行赋值
 * */
layui.config({
    base: '/resources/js/admin/'
}).extend({
    tableSelect: 'tableSelect',
    formSelects: 'formSelects-v4'
}).use(['table', 'form', 'layer', 'laydate', 'layedit', 'element', 'upload', 'tree', 'tableSelect', 'formSelects'], function () {
    this.table = layui.table;
    this.form = layui.form;
    this.layer = layui.layer;
    this.laydate = layui.laydate;
    this.layedit = layui.layedit;
    this.element = layui.element;
    this.upload = layui.upload;
    this.tree = layui.tree;
    this.tableSelect = layui.tableSelect;
    this.formSelects = layui.formSelects;
});

function tableLoading(pageid, tableid, formid, field, value) {
    var table = layui.table, layer = layui.layer, form = layui.form;
    table.init(tableid, {
        url: '/userpage/gettablevalue.do?',
        method: 'POST',
        id: tableid,
        page: true,
        height: 'full-130',
        limit: 10,
        where: {pageid:pageid,formid:formid,glField:field,glValue:value},
        done: function (res, curr, count) {
            count || this.elem.next('.layui-table-view').find('.layui-table-header').css('overflow', 'auto');
            layui.each($('select'), function (index, item) {
                var elem = $(item);
                elem.val(elem.data('value')).parents('div.layui-table-cell').css('overflow', 'visible');
            });
            form.render();
        },
    });
    form.on('select(testSelect)', function (data) {
        var elem = $(data.elem);
        var trElem = elem.parents('tr');
        var tableData = table.cache[tableid];
        // 更新到表格的缓存数据中，才能在获得选中行等等其他的方法中得到更新之后的值
        tableData[trElem.data('index')][elem.attr('name')] = data.value;
        // 其他的操作看需求 TODO
    });
    table.on('toolbar(' + tableid + ')', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        var data = checkStatus.data;
        switch (obj.event) {
            case 'add':
                oa.gotourl("/userpage/pageform/newformdata.do?type=add&pageid=" + pageid + "&formid=" + formid);
                break;
            case 'update':
                /*if (data.length === 0) {
                    layer.msg('请选择一行');
                } else if (data.length > 1) {
                    layer.msg('只能同时编辑一个');
                } else {
                    var recno = data[0][tableid + "_recorderNO"];
                    oa.gotourl("/userpage/pageform/" + recno + ".do?type=modi&pageid=" + pageid + "&formid=" + formid);
                }*/
                oa.gotourl("/userpage/viewpage/" + pageid + ".do?formid=" + formid + "&listsave=true");
                break;
            case 'save':
                var len = data.length;
                if (len === 0) {
                    layer.msg('请勾选需要保存的数据');
                } else {
                    for (var i = 0; i < len; i++) {
                        var data = checkStatus.data[i];
                        $.ajax({
                            type: "POST",
                            url: "/userpage/listsave.do?formid=" + formid,
                            data: data,
                            contentType: "application/x-www-form-urlencoded; charset=utf-8",
                            dataType: "json",
                            async: false,
                        });
                    }
                    parent.layer.msg("保存完毕");
                    oa.gotourl("/userpage/viewpage/" + pageid + ".do?formid=" + formid);
                }
                break;
            case 'delete':
                if (data.length === 0) {
                    layer.msg('请选择一行');
                } else {
                    var recno = "";
                    for (var i = 0; i < data.length; i++) {
                        recno += data[i][tableid + "_recorderNO"] + ";";
                    }
                    if (recno != "") {
                        $.ajax({
                            type: "POST",
                            url: "/userpage/pagealldel.do?tableid=" + tableid + "&recno=" + recno,
                            contentType: "application/json; charset=utf-8",
                            dataType: "html",
                            success: function (data) {
                                formback(pageid);
                            },
                            error: function (message) {
                                console.log(message)
                                layer.msg("提交数据失败！");
                            }
                        });
                    }
                }
                break;
            default:
        }
    });
}

function listModi(tableid) {
    var table = layui.table;
    var formula = listModiFormula(tableid);
    if (formula != "0") {
        var len = formula.length;
        table.on('edit(' + tableid + ')', function (obj) {
            var ce = new CalcEval();
            var $td = $(obj)[0].tr;
            var tr = obj.data;
            var up = {};
            for (var i = 0; i < len; i++) {
                var gs = formula[i];
                var getField = excludeSpecial(gs.value);
                var nu = {};
                var le = getField.length;
                for (var j = 0; j < le; j++) {
                    nu[getField[j]] = tr[tableid + "_" + getField[j]];
                }
                var res = tableid + "_" + gs.result;
                var val = ce.complexEval(listModiFormat(getField, gs.value, nu)) + "";
                var sum = $td.find('td[data-field="' + res + '"]');
                sum.find('div').text(val);
                up[tableid + "_" + gs.result] = val;
            }
            if (up != null) {
                obj.update(up);
            }
        });
    }
}

function tablesearch(field, item, value, tableid) {
    var f = field.find("option:selected").val();
    var i = item.find("option:selected").val();
    var v = value.val();
    console.log(f + "-" + i + "-" + v)
    var table = layui.table;
    table.reload(tableid, {
        where: {
            field: f,
            item: i,
            value: v
        }
        , page: {
            curr: 1
        }
    });
}

function formLoading(pageid, tableid, formid) {
    layui.form.render();
    layui.form.on('select(selone)', function (data) {
        var id = data.elem.id;
        $(".layui-form").validate().element($("select[id='" + id + "']"));
    });
    layui.form.on('select(selall)', function (data) {
        var id = data.elem.id;
        $(".layui-form").validate().element($("select[id='" + id + "']"));
    });
    layui.use('laydate', function (laydate) {
        $('.layui-input.date').each(function () {
            var obj = this;
            laydate.render({
                elem: obj
                , format: 'yyyy-MM-dd'
                , type: 'date'
                , trigger: 'click'
                , calendar: true
                , done: function (value) {
                    $(obj).val(value);
                    $(".layui-form").validate().element($(obj));
                }
            });
        });
        $('.layui-input.datetime').each(function () {
            var obj = this;
            laydate.render({
                elem: obj
                , format: 'yyyy-MM-dd HH:mm:ss'
                , type: 'datetime'
                , trigger: 'click'
                , calendar: true
                , done: function (value) {
                    $(obj).val(value);
                    $(".layui-form").validate().element($(obj));
                }
            });
        });
    });
    fieldUpload();
}

function fieldUpload() {
    //单文件上传
    $('.layui-upload.upload').each(function (index, element) {
        var bt = $(element).children("button")
        uploadFun(bt.attr("id"));
    });
    //多文件上传（列表）
    $('.layui-upload.uploads').each(function (index, element) {
        var bt = $(element).children("button")
        uploadsFun(bt.attr("id"));
    });
}

/**
 * 初始化单附件上传
 * */
var uploadFun = function (elem) {
    layui.use('upload', function (upload) {
        upload.render({
            elem: '#' + elem
            , url: '/uploading/fileuploads.do'
            , data: {'type': 'common'}
            , accept: 'file'
            , before: function (obj) {
                //读取本地文件
                obj.preview(function (index, file, result) {

                });
                layui.layer.load();
            }
            , done: function (res) {
                layui.layer.closeAll('loading');
                var item = this.item;
                var $obj = $('#' + elem).parent();
                var inputId = $obj.find('.form-upload-ins').attr("id");
                if (res.code == 0) {
                    $obj.find('.upload-text').text('上传失败');
                } else {
                    document.getElementById(inputId).value = res.file;
                    $obj.find('.upload-text').text('上传成功');
                }
            }
            , error: function (data) {
                layui.layer.closeAll('loading');
            }
        });
    });
}

/**
 * 初始化多附件上传
 * */
var uploadsFun = function (elem) {
    layui.use('upload', function (upload) {
        var uploadListIns = upload.render({
            elem: '#' + elem
            , url: '/uploading/fileuploads.do'
            , data: {'type': 'common'}
            , accept: 'file'
            , multiple: true
            , choose: function (obj) {
                //将每次选择的文件追加到文件队列
                var files = this.files = obj.pushFile();
                console.log(this)
                //读取本地文件
                obj.preview(function (index, file, result) {
                    var tr = $(['<tr id="upload-' + index + '">'
                        , '<td>' + file.name + '</td>'
                        , '<td>' + (file.size / 1014).toFixed(1) + 'kb</td>'
                        , '<td>等待上传</td>'
                        , '<td>'
                        , '<button class="layui-btn layui-btn-xs upload-reload layui-hide">重传</button>'
                        , '<button class="layui-btn layui-btn-xs layui-btn-danger upload-delete">删除</button>'
                        , '</td>'
                        , '</tr>'].join(''));
                    //单个重传
                    tr.find('.upload-reload').on('click', function () {
                        obj.upload(index, file);
                    });

                    //删除
                    tr.find('.upload-delete').on('click', function () {
                        delete files[index]; //删除对应的文件
                        tr.remove();
                        console.log("sdad")
                        var file = "";
                        var trs = $('#' + elem + "_Upload").find("tbody").find('tr');
                        trs.each(function (obj) {
                            var tds = $(this).children().eq(0);
                            console.log(tds);
                            file += tds.find('a').attr("data-value") + "|";
                        });
                        console.log(file);
                        var id = elem + "_Value";
                        document.getElementById(id).value = file;
                        uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                    });
                    $('#' + elem + "_Upload").find("tbody").append(tr);
                    console.log($('#' + elem + "_Upload").find("tbody"))

                });
            }
            , done: function (res, index, upload) {
                if (res.code == 1) { //上传成功
                    var tr = $('.uploadFildList').find('tr#upload-' + index)
                        , tds = tr.children();
                    var eq1text = tds.eq(0).html();
                    eq1text = "<a data-value='" + res.file + "'>" + eq1text + "</a>";
                    tds.eq(0).html(eq1text);
                    tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                    tds.eq(3).find('.upload-delete').removeClass('layui-hide'); //显示重传
                    var file = "";
                    var tr = $('#' + elem + "_Upload").find("tbody").find('tr');
                    tr.each(function (obj) {
                        var tds = $(this).children().eq(0);
                        file += tds.find('a').attr("data-value") + "|";
                    });
                    var id = elem + "_Value";
                    document.getElementById(id).value = file;
                    return delete this.files[index]; //删除文件队列已经上传成功的文件
                }
                this.error(index, upload);
            }
            , error: function (index, upload) {
                var tr = $('.uploadFildList').find('tr#upload-' + index)
                    , tds = tr.children();
                tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
                tds.eq(3).find('.upload-reload').removeClass('layui-hide'); //显示重传
            }
            , allDone: function (obj) { //当文件全部被提交后，才触发
                var file = "";
                var tr = $('#' + elem + "_Upload").find("tbody").find('tr');
                tr.each(function (obj) {
                    var tds = $(this).children().eq(0);
                    file += tds.find('a').attr("data-value") + "|";
                });
                console.log(elem)
                var id = elem + "_Value";
                console.log(id)
                console.log(document.getElementById(id));
                document.getElementById(id).value = file;
            }
        });
    });
}

//多附件删除
function removefile(trid, field) {
    var tr = $("#" + trid);
    tr.remove();
    var file = "";
    var tr = $('#' + field + "_Upload").find("tbody").find('tr');
    tr.each(function (obj) {
        var tds = $(this).children().eq(0);
        file += tds.find('a').attr("data-value") + "|";
    });
    var id = field + "_Value";
    console.log(file)
    document.getElementById(id).value = file;
}

//携带字段选项删除
function removeval(object) {
    var obj = $(object).parent();
    var intpuId = obj.parent().attr('id');
    obj.remove();
    var span = $("#" + intpuId).find("span");
    console.log(span);
    var val = "";
    span.each(function () {
        if (val.indexOf($(this).attr("value")) < 0) {
            val += $(this).attr("value") + ";"
        }
    });
    $("input[id='" + intpuId + "_Value']").val(val);
    $(".layui-form").validate().element($("input[id='" + intpuId + "_Value']"));
    return false;
}

//打开人员选择页面
function selectuser(obj, val) {
    //var top = parseInt($(obj).offset().top) + 40;
    var id = $(obj).attr("id");
    layer.closeAll();
    layer.open({
        type: 2 //此处以iframe举例
        , title: '选择人员'
        , offset: '200px'
        , area: ['800px', '600px']
        , shade: 0
        , maxmin: true
        , content: "/userpage/gotouserselect.do?type=" + val + "&inputId=" + id
        , zIndex: layer.zIndex //重点1
        , success: function (layero) {
            layer.setTop(layero); //重点2
        }
        , cancel: function (index, layero) {
            $(".layui-form").validate().element($("input[id='" + id + "_Value']"));
        }
    });
}

//打开部门选择页面
function selectdept(obj, val) {
    var id = $(obj).attr("id");
    layer.closeAll();
    layer.open({
        type: 2 //此处以iframe举例
        , title: '选择部门'
        , offset: '200px'
        , area: ['800px', '600px']
        , shade: 0
        , maxmin: true
        , content: "/userpage/gotodeptselect.do?type=" + val + "&inputId=" + id
        , zIndex: layer.zIndex //重点1
        , success: function (layero) {
            layer.setTop(layero); //重点2
        }
        , cancel: function (index, layero) {
            $(".layui-form").validate().element($("input[id='" + id + "_Value']"));
        }
    });
}

//打开表单携带选择页面
function selectform(obj, val, formTask) {
    var id = $(obj).attr("id");
    layer.closeAll();
    layer.open({
        type: 2 //此处以iframe举例
        , title: '携带添加'
        , offset: '200px'
        , area: ['800px', '600px']
        , shade: 0
        , maxmin: true
        , content: "/userpage/gotoformselect.do?type=" + val + "&inputId=" + id + "&formTask=" + formTask
        , zIndex: layer.zIndex //重点1
        , success: function (layero) {
            layer.setTop(layero); //重点2
        }
        , cancel: function (index, layero) {
            $(".layui-form").validate().element($("input[id='" + id + "_Value']"));
        }
    });
}

//弹出选择回写(单)
function updateValInput(intpuId, id, name) {
    var span = "<span value='" + id + "'><label>" + name + "</label><i onClick='event.cancelBubble = true;removeval(this)' class='iconfont icon-shanchu1'></i></span>";
    $("#" + intpuId).html(span);
    $("input[id='" + intpuId + "_Value']").val(id);
    $("p[id='" + intpuId + "_Explain']").html("");
    $(".layui-form").validate().element($("input[id='" + intpuId + "_Value']"));
    layer.closeAll();
}

//弹出选择回写(多)
function updateValsInput(intpuId, data, id, name) {
    var span = $("#" + intpuId).html()
    var values = $("input[id='" + intpuId + "_Value']").val();
    for (var i = 0; i < data.length; i++) {
        var val = data[i][id] + ";";
        if (values.indexOf(id) == -1) {
            span += "<span value='" + data[i][id] + "'><label>" + data[i][name] + "</label><i onClick='event.cancelBubble = true;removeval(this)' class='iconfont icon-shanchu1'></i></span>  ";
            values += data[i][id] + ";";
        }
    }
    $("#" + intpuId).html(span);
    $("input[id='" + intpuId + "_Value']").val(values);
    $("p[id='" + intpuId + "_Explain']").html("");
    layer.closeAll();
}

function fieldEditor() {
    //单文件上传
    $('.textarea.editor').each(function (index, element) {
        wangEditora($(this).attr("id"));
    });

}

//加载富文本编辑器
var wangEditora = function (edit) {
    var E = window.wangEditor
    var editor = new E('#' + edit);
    editor.customConfig.zIndex = 1
    editor.customConfig.menus = [
        'head',  // 标题
        'bold',  // 粗体
        'fontSize',  // 字号
        'fontName',  // 字体
        'italic',  // 斜体
        'underline',  // 下划线
        'strikeThrough',  // 删除线
        'foreColor',  // 文字颜色
        'backColor',  // 背景颜色
        'list',  // 列表
        'justify',  // 对齐方式
        'quote',  // 引用
        'table',  // 表格
        'code',  // 插入代码
        'undo',  // 撤销
        'redo'  // 重复
    ]
    var $text1 = $('#' + edit + "_Value");
    editor.customConfig.onchange = function (html) {
        $text1.val(html);
    }
    editor.create()
    $text1.val(editor.txt.html());
}

function formverify(obj) {
    var value = $(obj).val();
    var id = $(obj).attr("id");
    id = id.substring(0, id.lastIndexOf("_"));
    console.log(id)
    var num = $(obj).attr("maxlength");
    if (value == null || value == "") {
        $("#" + id + "_Explain").text("这是必填字段");
        $("#" + id + "_Explain").css("cssText", "color:red!important");
    } else if (getLength(value) > num) {
        $("#" + id + "_Explain").text("最多可以输入 {" + num + "} 个字符");
        $("#" + id + "_Explain").css("cssText", "color:red!important");
    } else {
        $("#" + id + "_Explain").text("");
    }
}

function getLength(str) {
    return str.replace(/[\u0391-\uFFE5]/g, "aa").length;
};


function openseeoffice(url) {
    var html = "https://view.officeapps.live.com/op/view.aspx?src=" + getRootPath() + url;
    window.open(html);
}

function openseepdf(url) {
    var html = getRootPath() + url;
    window.open(html);
}

function getRootPath() {
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    return (localhostPaht + "/");
}
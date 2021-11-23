var $pageSize = 10;
var $pageNumber = 1;

// 获取查询选项
function taskSearchAction() {
    $("#tasklist").bootstrapTable({
        url: "/dictionary/selecttask.do",    //数据请求路径
        clickToSelect: true,  //点击表格项即可选择
        dataType: "json",   //后端数据传递类型
        pagination: true, // 是否显示分页（*）
        sidePagination: "server",//分页方式 client、server
        pageNumber: $pageNumber,    //如果设置了分页，首页页码
        pageSize: $pageSize,
        pageList: [5, 10, 20],
        method: 'get',      //请求类型
        queryParamsType: "",
        checkbox: "false",
        queryParams: function (params) {
            var temp = {
                pageSize: params.pageSize,
                pageNumber: params.pageNumber,
                option: $("#taskzd").val(),
                inputval: $("#taskcx").val()
            };
            return temp;
        }, // 请求参数，这个关系到后续用到的异步刷新
        rowStyle: function (row, index) {//隔行变色
            var classesArr = ['white', 'red'];
            var strclass = "";
            if (index % 2 === 0) {//偶数行
                strclass = classesArr[0];
            } else {//奇数行
                strclass = classesArr[1];
            }
            return {classes: strclass};
        },
        responseHandler: function (res) {
            return res;
        },
    });
}

function actionFormatter(value, row, index) {
    var taskName = row.taskName;
    var temp = "";
    temp += "<a class=\"updatetask\" href=\"javascript:void(0)\" onclick=\"updatetask('" + taskName + "')\" title=\"Update Item\"><i class=\"glyphicon glyphicon-edit\"></i></a>";
    temp += "<a class=\"removetask\" style=\"padding-left:10px;\" href=\"javascript:void(0)\" onclick=\"removetask('" + taskName + "')\" title=\"Delete Item\"><i class=\"glyphicon glyphicon-remove-circle\"></i></a>";
    return temp;
}

function insertTask() {
    oa.gotourl("dictionary/gotoinserttask.do");
}

function updatetask(taskName) {
    oa.gotourl("dictionary/taskinfo/" + taskName + ".do");
}

function removetask(taskName) {
    $.ajax({
        type: "POST",//方法类型
        url: "/dictionary/deltask/" + taskName + ".do",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        dataType: "html",
        cache: false,
        success: function (data) {
            oa.gotourl("/dictionary/tasklist.do");
        },
        error: function (error) {
        }
    });
}

function selectop(data) {
    $("#tablelist").bootstrapTable('load', data);
}

function taskgoback() {
    oa.gotourl("dictionary/tasklist.do");
}

function taskload(val) {
    if (val == 'ins') {
        insertTask();
    } else {
        updatetask(val);
    }
}

function up(obj) {
    var tr = $(obj).parents("tr");
    if (tr.index() != 0) {
        tr.prev().before(tr);
    }
}

function down(obj) {
    var table = document.getElementById("task");
    var len = table.getElementsByTagName("tr").length;
    var tr = $(obj).parents("tr");
    if (tr.index() != len - 1) {
        tr.next().after(tr);
    }
}

function move(obj, id) {
    var tr = obj.parentNode.parentNode;
    var tbody = tr.parentNode;
    var taskName = tr.cells[0].innerHTML;
    tbody.removeChild(tr);
    var tab = document.getElementById(id);
    var newTR = tab.insertRow(tab.rows.length);
    newTR.setAttribute("id", taskName);
    newTR.setAttribute("style", "text-align: center");
    var newNameTD = newTR.insertCell(0);
    newNameTD.innerHTML = taskName;
    var newDeleteTD = newTR.insertCell(1);
    if (id == "savetask") {
        newDeleteTD.innerHTML = "<input type=\"button\" value=\"删除\" class=\"btn btn-xs btn-link\" onclick=\"move(this,'savetable')\"/> " +
            "<input type=\"button\" value=\"上移\" class=\"btn btn-xs btn-link\" onclick=\"up(this)\" /> " +
            "<input type=\"button\" value=\"下移\" class=\"btn btn-xs btn-link\" onclick=\"down(this)\" />";
    } else if (id == "savetable") {
        newDeleteTD.innerHTML = "<input type=\"button\" value=\"添加\" class=\"btn btn-xs btn-link\" onclick=\"move(this,'savetask')\"/>";
    } else if (id == "savetaskins") {
        newDeleteTD.innerHTML = "<input type=\"button\" value=\"删除\" class=\"btn btn-xs btn-link\" onclick=\"move(this,'savetableins')\"/> " +
            "<input type=\"button\" value=\"上移\" class=\"btn btn-xs btn-link\" onclick=\"up(this)\" /> " +
            "<input type=\"button\" value=\"下移\" class=\"btn btn-xs btn-link\" onclick=\"down(this)\" />";
    } else if (id == "savetableins") {
        newDeleteTD.innerHTML = "<input type=\"button\" value=\"添加\" class=\"btn btn-xs btn-link\" onclick=\"move(this,'savetaskins')\"/>";
    }
}

function savetask(val) {
    var savedata = "";
    var saveurl = "";

    if(val == "ins"){
        saveurl = "/dictionary/inserttask.do";
        var tr = document.getElementById("savetaskins").rows;
        var result = "";
        for (var i = 1; i < tr.length; i++) {
            if (tr[i].id != null && tr[i].id != "") {
                result += tr[i].id + ";";
            }
        }
        $("#instableField").attr("value", result);
        var tabletr = document.getElementById("savetaskins").rows;
        $("#instableName").attr("value", tabletr[0].id);
        savedata = $('#savetaskform').serialize();
    }else{
        saveurl = "/dictionary/uptask.do";
        var tr = document.getElementById("savetask").rows;
        var result = "";
        for (var i = 1; i < tr.length; i++) {
            if (tr[i].id != null && tr[i].id != "") {
                result += tr[i].id + ";";
            }
        }
        $("#tableField").attr("value", result);
        savedata = $('#uptask').serialize();
    }
    $.ajax({
        type: "POST",//方法类型
        data: savedata,
        url: saveurl,
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        dataType: "html",
        cache: false,
        success: function (data) {
            if (data == "1") {
                oa.gotourl("/dictionary/tasklist.do");
            } else if (data == "0") {
                alert("保存失败");
            }
        },
        error: function (error) {
            alert(error);
        }
    });
}

function tableSearchAction() {
    $('#tablelist').bootstrapTable('destroy');
    $("#tablelist").bootstrapTable({
        url: "/dictionary/selecttable.do",    //数据请求路径
        dataType: "json",   //后端数据传递类型
        pagination: true, // 是否显示分页（*）
        sidePagination: "server",//分页方式 client、server
        pageNumber: $pageNumber,    //如果设置了分页，首页页码
        pageSize: $pageSize,
        pageList: [5, 10, 20],
        method: 'get',      //请求类型
        queryParamsType: "",
        checkbox: "false",
        queryParams: function (params) {

            var temp = {//这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                pageSize: params.pageSize,
                pageNumber: params.pageNumber,
                option: $("#tablezd").val(),
                inputval: $("#tablecx").val()
            };
            return temp;
        }, // 请求参数，这个关系到后续用到的异步刷新
        rowStyle: function (row, index) {//隔行变色
            var classesArr = ['white', 'red'];
            var strclass = "";
            if (index % 2 === 0) {//偶数行
                strclass = classesArr[0];
            } else {//奇数行
                strclass = classesArr[1];
            }
            return {classes: strclass};
        },
        responseHandler: function (res) {
            return res;
        },
    });
}

function tableFormatter(value, row, index) {
    var tableName = row.tableName;
    var temp = "<a class=\"updatetable\" href=\"javascript:void(0)\" onclick=\"submittable('" + tableName + "')\" ><i class=\"glyphicon glyphicon-zoom-in\"></i></a>";
    return temp;
}

function submittable(tableName) {
    $.ajax({
        type: "POST",//方法类型
        url: "/dictionary/tasktable/" + tableName + ".do",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        dataType: "html",
        cache: false,
        success: function (data) {
            $('#selecttable').html(data);
        },
        error: function (error) {
            alert(error);
        }
    });
}

function taskVal() {
    $("#savetask").bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            taskName: {
                //message: '请填写字段名',
                validators: {
                    notEmpty: {
                        message: '字段名不能为空'
                    },
                    stringLength: {
                        min: 6,
                        max: 18,
                        message: '字段名长度必须在6到18位之间'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_]+$/,
                        message: '字段名只能包含大写、小写、数字和下划线'
                    },
                    remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}
                        url: "/dictionary/inspect/task.do",//验证地址
                        message: '字段名存在',//提示消息
                        delay: 2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                        type: 'POST',//请求方式
                        data: {
                            value: function () {
                                return $('#savetaskName').val();
                            }
                        }
                    },
                }
            },
            taskTitle: {
                validators: {
                    notEmpty: {
                        message: '字段名不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 18,
                        message: '字段名长度不能超过18位'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_\u0391-\uFFE5]+$/,
                        message: '字段名只能包含汉字、大写、小写、数字和下划线'
                    }
                }
            },
        },
        submitHandler: function (validator, form, submitButton) {
        }
    });
}
var $pageSize = 10;
var $pageNumber = 1;

function fieldSearchAction() {
    $('#fieldlist').bootstrapTable('destroy');
    $("#fieldlist").bootstrapTable({
        url: "/dictionary/selectfield.do",    //数据请求路径
        clickToSelect: true,  //点击表格项即可选择
        dataType: "json",
        pagination: true, // 是否显示分页（*）
        sidePagination: "server",//分页方式 client、server
        pageNumber: $pageNumber,    //首页页码
        pageSize: $pageSize,
        pageList: [5, 10, 20],
        method: 'get',
        queryParamsType: "",
        checkbox: "false",
        queryParams: function (params) {
            var temp = {
                pageSize: params.pageSize,
                pageNumber: params.pageNumber,
                option: $("#fieldzd").val(),
                inputval: $("#fieldcx").val()
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
    var fieldName = row.fieldName;
    var tableName = row.tableName;
    var temp = "";
    if(tableName!='system'){
        temp += "<a class=\"updatefield\" href=\"javascript:void(0)\" onclick=\"updatefield('" + fieldName + "')\" title=\"Update Item\"><i class=\"glyphicon glyphicon-edit\"></i></a>";
        temp += "<a class=\"removefield\" style=\"padding-left:10px;\" href=\"javascript:void(0)\" onclick=\"removefield('" + fieldName + "')\" title=\"Delete Item\"><i class=\"glyphicon glyphicon-remove-circle\"></i></a>";
    }
    return temp;
}

function updatefield(fieldName) {
    $.ajax({
        type: "POST",
        url: "dictionary/fieldinfo/" + fieldName + ".do",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        cache: false,
        dataType: "json",
        // 成功后开启模态框
        success: function (data) {
            $("#myLargeModalLabel").text("修改字段");
            $("#fieldName").val(data.fieldName);
            $("#fieldName").attr("readonly", "readonly");
            $("#fieldTitle").val(data.fieldTitle);
            $("#fieldType").find("option[value = '" + data.fieldType + "']").attr("selected", "selected");
            $("#fieldNum").val(data.fieldNum);
            $('#fieldsave').attr('onclick',"fieldsave('modi')");
            $('#fieldModal').modal('show');
        },
        error: function () {
            alert("请求失败");
        }
    });
}


function removefield(fieldName) {
    $.ajax({
        type: "POST",//方法类型
        url: "/dictionary/delfield/" + fieldName + ".do",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        dataType: "html",
        cache: false,
        success: function (data) {
            $("#rigthPage").load("/dictionary/fieldlist.do");
        },
        error: function (error) {
            alert("请求失败");
        }
    });
}

function selectop(data) {
    $("#fieldlist").bootstrapTable('load', data);
}

function goback() {
    $("#rigthPage").load("dictionary/fieldlist.do");
}

function fieldsave(type) {
    var saveurl = "";
    if (type == "ins") {
        saveurl = "/dictionary/insertfield.do";

    } else if (type == "modi") {
        saveurl = "/dictionary/upfield.do";
    }
    $.ajax({
        type: "POST",//方法类型
        data: $('#fieldform').serialize(),
        url: saveurl,
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        dataType: "html",
        cache: false,
        success: function (data) {
            $("#rigthPage").load("/dictionary/fieldlist.do");
        },
        error: function (error) {
            alert("请求失败");
            $("#rigthPage").load("/dictionary/fieldlist.do");
        }
    });
}

function fieldVal() {
    $("#fieldform").bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            fieldName: {
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
                        url: "/dictionary/field/"+$('#fieldName').val()+".do",//验证地址
                        message: '字段名存在',//提示消息
                        delay :  2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                        type: 'POST'//请求方式
                    },
                }
            },
            fieldTitle: {
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
            fieldNum: {
                validators: {
                    notEmpty: {
                        message: '字段长度不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 5,
                        message: '字段名长度不能超过5位'
                    },
                    regexp: {
                        regexp: /^[0-9]/,
                        message: '字段名只能包含数字'
                    }
                }
            }
        },
        submitHandler: function (validator, form, submitButton) {
            alert("submit");
        }
    });
}
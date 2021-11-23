var $pageSize = 10;
var $pageNumber = 1;

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

function actionFormatter(value, row, index) {
    var tableName = row.tableName;
    var temp = "";
    temp += "<a class=\"updatetable\" href=\"javascript:void(0)\" onclick=\"updatetable('" + tableName + "')\" title=\"Update Item\"><i class=\"glyphicon glyphicon-edit\"></i></a>";
    temp += "<a class=\"removetable\" style=\"padding-left:10px;\" href=\"javascript:void(0)\" onclick=\"removetable('" + tableName + "')\" title=\"Delete Item\"><i class=\"glyphicon glyphicon-remove-circle\"></i></a>";
    return temp;
}

function insertTable() {
    oa.gotourl("dictionary/inserttable.do");
}

function updatetable(tableName) {
    oa.gotourl("dictionary/tableinfo/" + tableName + ".do");
}

function removetable(tableName) {
    $.ajax({
        type: "POST",//方法类型
        url: "/dictionary/deltable/" + tableName + ".do",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        dataType: "html",
        cache: false,
        success: function (data) {
            oa.gotourl("/dictionary/talbelist.do");
        },
        error: function (error) {
        }
    });
}

function selectop(data) {
    $("#tablelist").bootstrapTable('load', data);
}

function tablegoback() {
    oa.gotourl("dictionary/tablelist.do");
}

function tableload(tableName) {
    updatetable(tableName);
}

function searchField() {
    $('#fieldlist').bootstrapTable('destroy');
    $("#fieldlist").bootstrapTable({
        url: "/dictionary/tablenotfield.do",    //数据请求路径
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
                option: $("#fieldzd").val(),
                inputval: $("#fieldcx").val(),
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

function fieldFormatter(value, row, index) {
    var tableName = row.tableName;
    var temp = "";
    temp += "<a class=\"insert\" href=\"javascript:void(0)\" onclick=\"move(this,'table')\" " +
        "title=\"Update Item\"><i class=\"glyphicon glyphicon-arrow-left\"></i></a>";
    return temp;
}

function move(obj, id) {
    var tr = obj.parentNode.parentNode;
    var tbody = tr.parentNode;
    var fieldName = tr.cells[0].innerHTML;
    var fieldTitle = tr.cells[1].innerHTML;
    var fieldType = tr.cells[2].innerHTML;
    var fieldNum = tr.cells[3].innerHTML;
    tbody.removeChild(tr);
    var tab = document.getElementById(id);
    var newTR = tab.insertRow(tab.rows.length);
    newTR.setAttribute("id", fieldName);
    newTR.setAttribute("style", "text-align: center");
    var newNameTD = newTR.insertCell(0);
    newNameTD.innerHTML = fieldName;
    newNameTD = newTR.insertCell(1);
    newNameTD.innerHTML = fieldTitle;
    newNameTD = newTR.insertCell(2);
    newNameTD.innerHTML = fieldType;
    newNameTD = newTR.insertCell(3);
    newNameTD.innerHTML = fieldNum;
}

function tablesave(val) {
    var saveurl = "";
    var savedata = "";
    var tr = document.getElementById("table").rows;
    var result = "";
    for (var i = 1; i < tr.length; i++) {
        if (tr[i].id != null && tr[i].id != "") {
            result += tr[i].id + ";";
        }
    }
    if (val == "ins") {
        $("#instableField").attr("value", result);
        saveurl = "/dictionary/savetable/ins.do";
        savedata = $('#instable').serialize();
    } else {
        var tabtitle = $("#tabtitle").text();
        $("#tableTitle").attr("value", tabtitle);
        $("#tableField").attr("value", result);
        saveurl = "/dictionary/savetable/modi.do";
        savedata = $('#uptable').serialize();
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
                oa.gotourl("/dictionary/tablelist.do");
            } else if (data == "0") {
                oa.gotourl("dictionary/tableinfo/" + val + ".do");
            }
        },
        error: function (error) {
            alert(error);
        }
    });
}
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<html>
<head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/resources/js/system/system.js" type="text/javascript" charset="utf-8"></script>
    <script src="/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
    <script src="/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
    <script src="/resources/js/system/listmodi.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
        function starscript(pageid, tableid, formid) {

        }
    </script>
</head>
<body onload="starscript('${pageid}','${tableid}','${formid}')">
<div class="layui-form layui-side" style="margin-top: 20px">
    <div class="layui-laydate-header">
        <i class="layui-icon laydate-icon laydate-prev-y" title="前一年"></i>
        <div style="margin-left: 5px;text-align: center">
            <span value="" id="year"></span>
        </div>
        <i class="layui-icon laydate-icon laydate-next-y" title="后一年"></i>
    </div>
    <div style="height: 70%">
        <ul class="oa-laydate-list"></ul>
    </div>
    <div style="text-align: center;">
        <a href="javascript:void(0);" onclick="payroll()" class="layui-btn layui-btn-radius layui-btn-normal"
           id="insertpayroll">生成当月工资发放</a>
    </div>
</div>
<div class="layui-body" id="rigthPage">
    <div style="padding: 15px;height:95%">
        <table lay-filter="${tableid}" id="${tableid}" class="layui-table"
               lay-data="{id:'${tableid}', height: 'full-70', cellMinWidth: 100, toolbar: '#toolbar', defaultToolbar: []}">
            <thead>
            <tr>
                <s:page type="list_modititle" value="${formid}"></s:page>
            </tr>
            </thead>
        </table>
        <script type="text/html" id="toolbar">
            <div class="layui-btn-container" style="width: 100%;">
                <a class='layui-btn layui-btn-xs' lay-event='save' id="save" title='保存'>保存</a>
                <a class='layui-btn layui-btn-xs' lay-event='payroll' id="payroll" title='发放'>发放</a>
                <a class='layui-btn layui-btn-xs' href='/userpage/viewpage/${pageid}.do' title="刷新">刷新</a>
            </div>
        </script>
        <div class="table-search" id="tablesearch">
            <div style="float: left">
                <label>检索：</label>
            </div>
            <div style="float: left">
                <select id="field" name="field">
                    <s:page type="list_select" value="${tableid}"></s:page>
                </select>
            </div>
            <div style="float: left">
                <select id="term" name="term">
                    <option value="等于">等于</option>
                    <option value="包含">包含</option>
                    <option value="不等于">不等于</option>
                    <option value="大于">大于</option>
                    <option value="大于等于">大于等于</option>
                    <option value="小于">小于</option>
                    <option value="小于等于">小于等于</option>
                </select>
            </div>
            <div style="float: left">
                <input class="" id="inputval" name="inputval" autocomplete="off">
            </div>
            <a onclick="thistablesearch($('#field'),$('#term'),$('#inputval'),'${tableid}')">搜索</a>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        var date = new Date;
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        $("#year").text(year + "年");
        $("#year").attr("value", year);
        for (var i = 1; i < 13; i++) {
            if (month == i) {
                $('ul.oa-laydate-list').append('<li lay-ym="' + i + '" class="oa-laydate-this">' + i + '月</li>');
            } else {
                $('ul.oa-laydate-list').append('<li lay-ym="' + i + '" class="">' + i + '月</li>');
            }
        }
    });
    var tableint = "";
    layui.use('table', function () {
        var table = layui.table;
        var tableid = '${tableid}';
        tableint = table.init(tableid, {
            url: '/finance/gettablevalue.do?formid=${formid}'
            , method: 'POST'
            , id: tableid
            , page: true
            , limit: 15
            , where: {
                year: $("#year").attr("value"),
                month: $("li.oa-laydate-this").attr("lay-ym"),
            }
            , parseData: function (res) {
                if (res.payroll > 0) {
                    $("#save").hide();
                    $("#payroll").hide();
                    $("#insertpayroll").removeClass('layui-btn-normal').addClass('layui-btn-disabled');
                    $('#insertpayroll').removeAttr('onclick');
                } else {
                    $("#insertpayroll").removeClass('layui-btn-disabled').addClass('layui-btn-normal');
                    $("#insertpayroll").attr("onclick", "payroll();");
                }
            }
        });

        table.on('toolbar(' + tableid + ')', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            switch (obj.event) {
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
                case 'payroll':
                    var year = $("#year").attr("value");
                    var month = $("li.oa-laydate-this").attr("lay-ym")
                    $.ajax({
                        type: "POST",
                        url: "/finance/payroll.do?year=" + year + "&month=" + month,
                        data: data,
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        dataType: "html",
                        async: false,
                        success: function (data) {
                            tableint.reload("${tableid}", {
                                where: {
                                    year: year,
                                    month: month,
                                }
                            });
                        },
                    });
                    break;
            }
        });

        $("ul.oa-laydate-list").on("click", "li", function () {
            $("li.oa-laydate-this").removeClass();
            $(this).addClass("oa-laydate-this");
            tablereload();
        });
        $("i.laydate-prev-y").on("click", function () {
            var date = new Date;
            var thisyear = parseInt(date.getFullYear());
            var year = parseInt($("#year").attr("value")) - 1;
            if (thisyear - 3 < year) {
                $("#year").text(year + "年");
                $("#year").attr("value", year);
                tablereload();
            }
        });
        $("i.laydate-next-y").on("click", function () {
            var date = new Date;
            var thisyear = parseInt(date.getFullYear());
            var year = parseInt($("#year").attr("value")) + 1;
            if (thisyear + 1 >= year) {
                $("#year").text(year + "年");
                $("#year").attr("value", year);
                tablereload();
            }
        });

        function tablereload() {
            table.reload(tableid, {
                where: {
                    year: $("#year").attr("value"),
                    month: $("li.oa-laydate-this").attr("lay-ym"),
                }
            });
        }
    });

    function payroll() {
        var year = $("#year").attr("value");
        var month = $("li.oa-laydate-this").attr("lay-ym");
        $.ajax({
            type: "POST",
            url: "/finance/makewages.do?year=" + year + "&month=" + month,
            contentType: "application/json; charset=utf-8",
            dataType: "html",
            async: false,
            success: function (data) {
                tableint.reload("${tableid}", {
                    where: {
                        year: $("#year").attr("value"),
                        month: $("li.oa-laydate-this").attr("lay-ym"),
                    }
                });
            },
            error: function (message) {
                console.log(message.toString());
            }
        });
    }

    function thistablesearch(field, item, value, tableid) {
        var f = field.find("option:selected").val();
        var i = item.find("option:selected").val();
        var v = value.val();
        var table = layui.table;
        table.reload(tableid, {
            where: {
                field: f,
                item: i,
                value: v,
                year: $("#year").attr("value"),
                month: $("li.oa-laydate-this").attr("lay-ym"),
            }
            , page: {
                curr: 1
            }
        });
    }
</script>
</body>
</html>
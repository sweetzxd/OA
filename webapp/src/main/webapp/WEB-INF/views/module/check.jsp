<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<html>
<head>
    <meta charset="utf-8">
    <title>个人考勤记录</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/resources/css/homepage.css" type="text/css"/>
    <link rel="stylesheet" href="/resources/css/calendar.css" type="text/css"/>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/resources/js/system/manager.js" charset="utf-8"></script>
    <script src="/resources/js/system/calendar.js" charset="utf-8"></script>
    <script src="/resources/js/system/excelExport.js" charset="utf-8"></script>
</head>
<body style="padding: 10px">
<div class="layui-inline" style="padding: 10px;">
    <select style="font-size: 20px;;margin-right: 10px;" id="year"></select><span
        style="font-size: 20px;margin-right: 10px;">年</span>
    <button class="layui-btn layui-btn-sm month" onclick="search('01')" id="month01">一月</button>
    <button class="layui-btn layui-btn-sm month" onclick="search('02')" id="month02">二月</button>
    <button class="layui-btn layui-btn-sm month" onclick="search('03')" id="month03">三月</button>
    <button class="layui-btn layui-btn-sm month" onclick="search('04')" id="month04">四月</button>
    <button class="layui-btn layui-btn-sm month" onclick="search('05')" id="month05">五月</button>
    <button class="layui-btn layui-btn-sm month" onclick="search('06')" id="month06">六月</button>
    <button class="layui-btn layui-btn-sm month" onclick="search('07')" id="month07">七月</button>
    <button class="layui-btn layui-btn-sm month" onclick="search('08')" id="month08">八月</button>
    <button class="layui-btn layui-btn-sm month" onclick="search('09')" id="month09">九月</button>
    <button class="layui-btn layui-btn-sm month" onclick="search('10')" id="month10">十月</button>
    <button class="layui-btn layui-btn-sm month" onclick="search('11')" id="month11">十一月</button>
    <button class="layui-btn layui-btn-sm month" onclick="search('12')" id="month12">十二月</button>
    <button class="layui-btn layui-btn-sm month" style="margin-left: 100px;" onclick="downloadmonth()" id="downloadmonth">导出月记录</button>
</div>

<table class="layui-hide" lay-filter="check" lay-data="{id: 'check',cellMinWidth: 50}">
    <thead>
    <tr>
        <th lay-data="{field:'xm',align: 'center',width:100, templet:function(d){return oa.decipher('user',d.xm);}}">姓名
        </th>
        <c:forEach var="DAY" begin="1" end="31">
            <th lay-data="{field:'DAY_${DAY}',align: 'center',templet:function(d){return formatData(d.DAY_${DAY});}}">${DAY}</th>
        </c:forEach>
    </tr>
    </thead>
</table>
<table class="layui-table" lay-filter="check2" lay-data="{id: 'check2',cellMinWidth: 100}">
    <thead>
    <tr>
        <th lay-data="{field:'day',align: 'center',width:100}">考勤日期</th>
        <th lay-data="{field:'staffname',align: 'center'}">姓名</th>
        <th lay-data="{field:'swsb', width:'180',align: 'center'}">上午上班</th>
        <th lay-data="{field:'swsbh',width:'180',align: 'center',style:'color: orange;'}">上午上班后</th>
        <th lay-data="{field:'swxbq',width:'180',align: 'center',style:'color: orange;'}">上午下班前</th>
        <th lay-data="{field:'swxb', width:'180',align: 'center'}">上午下班</th>
        <th lay-data="{field:'xwsb', width:'180',align: 'center'}">下午上班</th>
        <th lay-data="{field:'xwsbh',width:'180',align: 'center',style:'color: orange;'}">下午上班后</th>
        <th lay-data="{field:'xwxbq',width:'180',align: 'center',style:'color: orange;'}">下午下班前</th>
        <th lay-data="{field:'xwxb', width:'180',align: 'center'}">下午下班</th>
    </tr>

    </thead>
</table>
<script type="text/javascript">
    var aaaaa = "";
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    if (month < 10) {
        month = "0" + month;
    }
    var nextYear = year - 1;
    $(function () {
        var html = "<option>" + nextYear + "</option><option selected>" + year + "</option>";
        $("#year").html(html);
        $("#month" + month).css('background-color', '#1E9FFF !important');
        $("#downloadmonth").attr("onclick","downloadmonth('"+month+"')");
    });
    layui.use(['table'], function () {
        var table = layui.table;
        //table
        table.init('check', {
            url: '/schedule/getCheckListByMonth.do',
            method: 'POST',
            id: 'check',
            page: false

        });
        table.init('check2', {
            url: '/schedule/getCheckListByMonth2.do',
            method: 'POST',
            id: 'check2',
            height: 'full-280',
            page: false
        });
    });

    function formatData(data) {
        var arr =new Array();
        if (data != null && data != "") {
            var str = data.substring(1,data.length-1);
            arr = str.split(',');
        }
        if (arr.length <= 1) {
            return "";
        } else if (arr[1] == "0") {
            return "<div >" + arr[0] + "</div>"
        } else if (arr[1] == "1") {
            return "<div style='color:orange;'>" + arr[0] + "</div>"
        }
        if (arr[1] == "2") {
            return "<div style='color:red;'>" + arr[0] + "</div>"
        }

    }

    function search(month) {
        $(".month").css('background-color', '#009688 !important');
        $("#month" + month).css('background-color', '#1E9FFF !important');
        $("#downloadmonth").attr("onclick","downloadmonth('"+month+"')");
        var year = $("#year").val();
        layui.table.reload("check", {
            where: {
                year: year,
                month: month
            }
        });
        layui.table.reload("check2", {
            where: {
                year: year,
                month: month
            }
        });
    }
    function downloadmonth(month){
        var year = $("#year").val();
        console.log(year,month);
    }
</script>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<html>
<head>
    <meta charset="utf-8">
    <title>工作日历</title>
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
<body>
<div class="layui-inline" style="padding: 10px;">
    <select style="font-size: 20px;;margin-right: 10px;" id="year"></select><span style="font-size: 20px;margin-right: 10px;">年</span>
    <button class="layui-btn month" onclick="search('01')" id="month01">一月</button>
    <button class="layui-btn month" onclick="search('02')" id="month02">二月</button>
    <button class="layui-btn month" onclick="search('03')" id="month03">三月</button>
    <button class="layui-btn month" onclick="search('04')" id="month04">四月</button>
    <button class="layui-btn month" onclick="search('05')" id="month05">五月</button>
    <button class="layui-btn month" onclick="search('06')" id="month06">六月</button>
    <button class="layui-btn month" onclick="search('07')" id="month07">七月</button>
    <button class="layui-btn month" onclick="search('08')" id="month08">八月</button>
    <button class="layui-btn month" onclick="search('09')" id="month09">九月</button>
    <button class="layui-btn month" onclick="search('10')" id="month10">十月</button>
    <button class="layui-btn month" onclick="search('11')" id="month11">十一月</button>
    <button class="layui-btn month" onclick="search('12')" id="month12">十二月</button>
</div>
<table id="tabledata001" class="layui-hide" lay-filter="check" lay-data="{cellMinWidth: 100,even:'false',toolbar:'#toolbar'}">
    <thead>
    <tr>
        <th lay-data="{field:'num',align: 'center',width:80}">考勤日期</th>
        <th lay-data="{field:'xm',align: 'center', templet:function(d){return oa.decipher('user',d.xm);}}">姓名</th>
        <th lay-data="{field:'swsb181120001',align: 'center'}">上午上班</th>
        <th lay-data="{field:'swxb181120001',align: 'center'}">上午下班</th>
        <th lay-data="{field:'xwsb181120001',align: 'center'}">下午上班</th>
        <th lay-data="{field:'xwxb181120001',align: 'center'}">下午下班</th>
        <th lay-data="{field:'isLeave',align: 'center',templet:function(d){return formatData(d.isLeave,'isLeave',d.recorderNOQJ); }}">是否请假</th>
        <th lay-data="{field:'isOut',align: 'center', templet:function(d){return formatData(d.isOut,'isOut',d.recorderNOWCR); }}">是否外出</th>
        <th lay-data="{field:'isCard',align: 'center', templet:function(d){return formatData(d.isCard,'isCard'); }}">是否补卡</th>
    </tr>
    </thead>
</table>
<%--<script type="text/html" id="toolbar">
    <div class="layui-btn-container">
        <a  style="margin-right: 10px;margin-bottom: 10px;height: 22px;line-height: 22px;padding: 0 5px;font-size: 12px;">
            开始时间：<input type="text" lay-verify="datetime" id="ks" autocomplete="off" value="">
            结束时间：<input type="text" lay-verify="datetime" id="js" autocomplete="off" value="">
        </a>
        <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="search">查询</a>
    </div>
</script>--%>

<script type="text/javascript">
    var aaaaa="";
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth()+1;
    if(month<10){
        month = "0"+month;
    }
    var nextYear = year-1;
    $(function(){
        var html = "<option>"+nextYear+"</option><option selected>"+year+"</option>";
        $("#year").html(html);
        $("#month"+month).css('background-color','#1E9FFF !important');
    });
    var layer=null;
    var table=null;
    layui.use(['table','element','laydate','layer'], function(){
        var laydate = layui.laydate
            ,element = layui.element;
        layer = layui.layer;
        table = layui.table;
        //table
        table.init('check', {
            url: '/schedule/getCheckListByMonthold.do',
            method: 'POST',
            id: 'check',
            page: false,
            limit: 31
        });

        //头工具栏事件
        table.on('toolbar(check)', function(obj){
            switch(obj.event){
                case "search":
                    var ks = $("#ks").val();
                    var js = $("#js").val();
                    table.reload("check", {
                        where: {
                            ks: ks,
                            js:js
                        }
                    });
                    //日期
                    laydate.render({
                        elem: '#ks'
                    });
                    laydate.render({
                        elem: '#js'
                    });
                    break;
            };
        });
        //日期
        laydate.render({
            elem: '#ks'
        });
        laydate.render({
            elem: '#js'
        });
    });
    function formatData(data,flag,id){
        if(data=="0"){
            return "否";
        }else if(data=="1"){
            /*return "<a href=javascript:void(0); onclick=\"parent.addtab('/schedule/gotoDetailData.do?flag="+flag+"','detailList','详情')\">是</a>"*/
            return "<a href=javascript:void(0); onclick=\"detailData('"+flag+"','"+id+"')\">是</a>"
        }else{
            return "";
        }
    }
    function detailData(flag,id){
        layer.open({
            type: 2,
            title:'详细信息',
            area: ['1200px', '500px'],
            content: "/schedule/gotoDetailDataold.do?flag="+flag+"&id="+id //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
        });
    }
    function search(month){
        $(".month").css('background-color','#009688 !important');
        $("#month"+month).css('background-color','#1E9FFF !important');
        var year = $("#year").val();
        table.reload("check", {
            where: {
                year: year,
                month:month
            }
        });
    }
</script>
</body>
</html>

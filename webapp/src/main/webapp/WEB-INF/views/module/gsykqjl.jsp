<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/5/15
  Time: 11:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<html>
<head>
    <meta charset="utf-8">
    <title>公司月考勤</title>
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
    <style>
        .biaoming{width: 12%;font-size: 10px;float: left;overflow: hidden;text-overflow:ellipsis; white-space: nowrap;}
    </style>
</head>
<body>
<div class="layui-inline" style="padding: 10px;">
    <select style="font-size: 20px;;margin-right: 10px;" id="year"></select><span style="font-size: 20px;margin-right: 10px;">年</span>
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
<table id="teableData" class="layui-hide" lay-filter="check" lay-data="{cellMinWidth: 100,even:'false',toolbar:'#toolbar'}">
    <thead>
    <tr>
        <th lay-data="{field:'xm',align: 'center',width:80}">姓名\日期</th>
        <c:forEach var="DAY" begin="1" end="31">
            <th lay-data="{field:'DAY_${DAY}',align: 'center',width:'50',templet:function(d){return formatData(d.DAY_${DAY},d.nian,d.yue,d.ri_${DAY},d.name);}}">${DAY}</th>
        </c:forEach>
    </tr>
    </thead>
</table>
<script type="text/html" id="toolbar">
   <div class="biaoming"><span>0</span><span> 未按时打卡</span></div>
   <div class="biaoming"><span style="color: red;">0</span><span> 只打一次卡</span></div>
   <div class="biaoming"><span style="color: orange;">0</span><span> 只早晚打卡</span></div>
   <div class="biaoming"><span>0.5</span><span> 只打半天卡</span></div>
   <div class="biaoming"><span style="color: red;">0.5</span><span> 只中午打卡</span></div>
   <div class="biaoming"><span style="color: orange;">1</span><span> 打了三次卡</span></div>
   <div class="biaoming"><span>1</span><span> 全天打了卡</span></div>
</script>
<script type="text/javascript">
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
        $("#downloadmonth").attr("onclick","downloadmonth('"+month+"')");
    });
    /*选中月份并改变月份的样式*/
    function search(month){
        $(".month").css('background-color','#009688 !important');
        $("#month"+month).css('background-color','#1E9FFF !important');
        $("#downloadmonth").attr("onclick","downloadmonth('"+month+"')");
        var year = $("#year").val();
        table.reload("check", {
            where: {
                year: year,
                month:month
            }
        });
    }
    var layer=null;
    var table=null;
    layui.use(['table','element','laydate','layer'], function(){
        var laydate = layui.laydate
            ,element = layui.element;
        layer = layui.layer;
        table = layui.table;
        //table
        table.init('check', {
            url: '/schedule/getCheckListByMonthAll2.do',
            method: 'POST',
            id: 'check',
            page: false,
            limit: 20
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
    function formatData(data,year,month,day,name){
        var str = data.substring(1,data.length-1);
        var arr = str.split(',');
        if(arr.length <= 1){
            return "";
        }else if(arr[1]=="0"){
            return "<a href=javascript:void(0); onclick=\"detailData('"+year+"','"+month+"','"+day+"','"+name+"')\">"+arr[0]+"</a>"
        }else if(arr[1]=="1"){
            return "<a style='color:orange;' href=javascript:void(0); onclick=\"detailData('"+year+"','"+month+"','"+day+"','"+name+"')\">"+arr[0]+"</a>"
        }if(arr[1]=="2"){
            return "<a style='color:red;' href=javascript:void(0); onclick=\"detailData('"+year+"','"+month+"','"+day+"','"+name+"')\">"+arr[0]+"</a>"
        }
    }
    function detailData(year,month,day,name){
        console.log(year,month,day,name);
        layer.open({
            type: 2,
            offset: '300px',
            title:'详细信息',
            area: ['1000px', '300px'],
            content: "/schedule/gotoDetailData.do?year="+year+"&month="+month+"&day="+day+"&name="+name //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
        });
    }
    function downloadmonth(month){
        $.post('/schedule/getkqexcel.do',{'year':$("#year").val(),'month':month});
    }
</script>
</body>
</html>

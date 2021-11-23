<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/1/4
  Time: 下午 4:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<html>
<head>
    <meta charset="utf-8">
    <title>领导日程</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
</head>
<body>
    <div class="layui-inline" style="padding: 10px;">
        <select style="font-size: 30px;" id="year"></select><span style="font-size: 30px;">年</span>
        <button class="layui-btn" onclick="search('01')" id="month01">一月</button>
        <button class="layui-btn" onclick="search('02')" id="month02">二月</button>
        <button class="layui-btn" onclick="search('03')" id="month03">三月</button>
        <button class="layui-btn" onclick="search('04')" id="month04">四月</button>
        <button class="layui-btn" onclick="search('05')" id="month05">五月</button>
        <button class="layui-btn" onclick="search('06')" id="month06">六月</button>
        <button class="layui-btn" onclick="search('07')" id="month07">七月</button>
        <button class="layui-btn" onclick="search('08')" id="month08">八月</button>
        <button class="layui-btn" onclick="search('09')" id="month09">九月</button>
        <button class="layui-btn" onclick="search('10')" id="month10">十月</button>
        <button class="layui-btn" onclick="search('11')" id="month11">十一月</button>
        <button class="layui-btn" onclick="search('12')" id="month12">十二月</button>
    </div>
    <div style="padding: 10px; background-color: #5bc0de;">
        <a style="font-size: 19px;font-weight: bold;">会议安排</a>
        <div class="layui-row layui-col-space15" id="hyap">
            <c:forEach items="${list1}" var="l">
                <div class="layui-col-md3">
                    <div class="layui-card">
                        <div class="layui-card-header" style="height: auto;line-height: 27px;">${l.rcbt190104001}</div>
                        <div class="layui-card-header" style="height: auto;line-height: 27px;">${l.xgld190104001}${l.department}，${l.kssj190104001}至${l.jssj190104001}</div>
                        <div class="layui-card-body" style="line-height: 27px;cursor: pointer" onclick="contentInfo(this)" data-value="${l.rcnr190104001}">
                            ${l.rcnr}
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <div style="padding: 10px; background-color: #5BB1DE;">
        <a style="font-size: 19px;font-weight: bold;">重大事项</a>
        <div class="layui-row layui-col-space15" id="zdsx">
            <c:forEach items="${list2}" var="l">
                <div class="layui-col-md3">
                    <div class="layui-card">
                        <div class="layui-card-header" style="height: auto;line-height: 27px;">${l.rcbt190104001}</div>
                        <div class="layui-card-header" style="height: auto;line-height: 27px;">${l.xgld190104001}${l.department}，${l.kssj190104001}至${l.jssj190104001}</div>
                        <div class="layui-card-body" style="line-height: 27px;cursor: pointer" onclick="contentInfo(this)" data-value="${l.rcnr190104001}">
                                ${l.rcnr}
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
<script src="/resources/js/system/manager.js" charset="utf-8"></script>
<script type="text/javascript">
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth()+1;
    if(month<10){
        month = "0"+month;
    }
    var nextYear = year+1;
    $(function(){
        var html = "<option>"+year+"</option><option>"+nextYear+"</option>";
        $("#year").html(html);
        $("#month"+month).css('background-color','#1E9FFF !important');
    });
    function contentInfo(obj) {
        var content = $(obj).attr("data-value");
        var left = parseInt($(obj).offset().left);
        var top = parseInt($(obj).offset().top);
        layer.open({
            title: false
            , offset: [(top + 30) + "px", (left + 10) + "px"]
            , shade: 0
            , scrollbar: false
            , btn: []
            , content: '<pre>' + content + '</pre>'
        });
    }
    
    function search(month){
        $(".layui-btn").css('background-color','#009688 !important');
        $("#month"+month).css('background-color','#1E9FFF !important');
        var date = $("#year").val()+"-"+month;
        $.post("/leaderSchedule/getLeaderScheduleByDate.do",{date:date},function(json){
            $("#hyap").html("");
            $("#zdsx").html("");
            if(json.list.length==0){
                layer.msg("本月暂无领导日程");
            }else{
                for(var i=0;i<json.list.length;i++){
                    var html = '<div class="layui-col-md3">'+
                                    '<div class="layui-card">'+
                                        '<div class="layui-card-header" style="height: auto;line-height: 27px;">'+json.list[i].rcbt190104001+'</div>'+
                                        '<div class="layui-card-header" style="height: auto;line-height: 27px;">'+json.list[i].xgld190104001+json.list[i].department+'，'+json.list[i].kssj190104001+'至'+json.list[i].jssj190104001+'</div>'+
                                        '<div class="layui-card-body" style="line-height: 27px;cursor: pointer" onclick="contentInfo(this)" data-value="'+json.list[i].rcnr190104001+'">'+
                                         json.list[i].rcnr+
                                        '</div>'+
                                    '</div>'+
                                '</div>';
                    if(json.list[i].rczl190104001=="会议安排"){
                        $("#hyap").append(html);
                    }else if(json.list[i].rczl190104001=="重大事项"){
                        $("#zdsx").append(html);
                    }
                }
            }
        })
    }

</script>
</body>
</html>
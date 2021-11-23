<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<html>
<head>
    <meta charset="utf-8">
    <title>详情</title>
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
</head>
<body>
<c:if test="${flag == 'isLeave'}">
    <table class="layui-hide" lay-filter="check"
           lay-data="{height: 'full-20',cellMinWidth: 100,even:'false',toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
        <thead>
        <tr>
            <th lay-data="{field:'recorderNO',align: 'center'}">请假记录</th>
            <th lay-data="{field:'qjr1811200001',align: 'center'}">姓名</th>
            <th lay-data="{field:'jhkss18112001',align: 'center'}">计划开始时间</th>
            <th lay-data="{field:'jhjss18112001',align: 'center'}">计划结束时间</th>
            <th lay-data="{field:'jhqjt18112001',align: 'center'}">计划请假天数</th>
            <th lay-data="{field:'sjkss18112001',align: 'center'}">实际开始时间</th>
            <th lay-data="{field:'sjjss18112001',align: 'center'}">实际结束时间</th>
            <th lay-data="{field:'sjqjt18112001',align: 'center'}">实际请假天数</th>
            <th lay-data="{field:'qjlx181120001',align: 'center'}">请假理由</th>
            <th lay-data="{field:'qjyy181120001',align: 'center'}">请假原因</th>
        </tr>
        </thead>
    </table>
</c:if>
<c:if test="${flag == 'isOut'}">
    <table class="layui-hide" lay-filter="check"
           lay-data="{height: 'full-20',cellMinWidth: 100,even:'false',toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
        <thead>
        <tr>
            <th lay-data="{field:'recorderNO',align: 'center'}">外出记录</th>
            <th lay-data="{field:'wcr1811200001',align: 'center'}">外出人姓名</th>
            <th lay-data="{field:'txr1811200001',align: 'center'}">同行人姓名</th>
            <th lay-data="{field:'kssj181120001',align: 'center'}">开始时间</th>
            <th lay-data="{field:'jssj181120001',align: 'center'}">结束时间</th>
            <th lay-data="{field:'wcsm181120001',align: 'center'}">外出说明</th>
            <th lay-data="{field:'fj18112000002',align: 'center'}">附件</th>
            <th lay-data="{field:'bz18112000002',align: 'center'}">备注</th>
        </tr>
        </thead>
    </table>
</c:if>
<c:if test="${flag == 'flag'}">
    <table class="layui-hide" lay-filter="check"
           lay-data="{height: 'full-20',cellMinWidth: 100}">
        <thead>
        <tr>
            <%--<th lay-data="{field:'recorderNO',align: 'center'}">识别编号</th>--%>
            <th lay-data="{field:'xm',align: 'center'}">姓名</th>
                <th lay-data="{field:'swsb', width:'180',align: 'center'}">上午上班</th>
                <th lay-data="{field:'swsbh',width:'180',align: 'center',style:'color: orange;'}">上午上班后</th>
                <th lay-data="{field:'swxbq',width:'180',align: 'center',style:'color: orange;'}">上午下班前</th>
                <th lay-data="{field:'swxb', width:'180',align: 'center'}">上午下班</th>
                <th lay-data="{field:'xwsb', width:'180',align: 'center'}">下午上班</th>
                <th lay-data="{field:'xwsbh',width:'180',align: 'center',style:'color: orange;'}">下午上班后</th>
                <th lay-data="{field:'xwxbq',width:'180',align: 'center',style:'color: orange;'}">下午下班前</th>
                <th lay-data="{field:'xwxb', width:'180',align: 'center'}">下午下班</th>
            <%--<th lay-data="{field:'isLeave',align: 'center',templet:function(d){return formatData(d.isLeave); }}">是否请假</th>
            <th lay-data="{field:'isOut',align: 'center', templet:function(d){return formatData(d.isOut); }}">是否外出</th>
            <th lay-data="{field:'isCard',align: 'center', templet:function(d){return formatData(d.isCard); }}">是否补签</th>--%>
        </tr>
        </thead>
    </table>
</c:if>
<c:if test="${flag == 'recorderNO'}">
    <table class="layui-hide" lay-filter="check2"
           lay-data="{height: 'full-20',cellMinWidth: 100,even:'false',toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
        <thead>
        <tr>
            <th lay-data="{field:'recorderNO',align: 'center'}">签到记录</th>
            <th lay-data="{field:'jlr1811200001',align: 'center',templet:function(d){return oa.decipher('user',d.jlr1811200001);}}">姓名</th>
            <th lay-data="{field:'swsb181120001',align: 'center'}">上午上班</th>
            <th lay-data="{field:'swxb181120001',align: 'center'}">上午下班</th>
            <th lay-data="{field:'xwsb181120001',align: 'center'}">下午上班</th>
            <th lay-data="{field:'xwxb181120001',align: 'center'}">下午下班</th>
            <th lay-data="{field:'isLeave',align: 'center',templet:function(d){return formatData(d.isLeave); }}">是否请假</th>
            <th lay-data="{field:'isOut',align: 'center', templet:function(d){return formatData(d.isOut); }}">是否外出</th>
            <th lay-data="{field:'isCard',align: 'center', templet:function(d){return formatData(d.isCard); }}">是否补签</th>
        </tr>
        </thead>
    </table>
</c:if>


<script type="text/javascript">
    //var month = new date().getMonth()+1;
    //month = month<10?'0'+month:month;
    layui.use(['table','laydate'], function(){
        var table = layui.table;
        var year = "${year}";
        var month = "${month}";
        var day = "${day}";
        var name = "${name}";
        var flag = "${flag}";
        var id = "${id}";
        //table
        table.init('check', {
            url: '/schedule/getDetailList.do',
            method: 'GET',
            id: 'check',
            page: false,
            limit: 10,
            where:{"year":year,"month":month,"day":day,"name":name}
        });
        table.init('check2', {
            url: '/schedule/getDetailListold.do',
            method: 'GET',
            id: 'check2',
            page: false,
            limit: 10,
            where:{"flag":flag,"id":id}
        });
    });
    function formatData(data){
        if(data=="0"){
            return "否";
        }else if(data=="1"){
            return "是";
        }else{
            return "";
        }
    }
</script>
</body>
</html>

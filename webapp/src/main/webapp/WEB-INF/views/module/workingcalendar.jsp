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
    <script type="text/javascript">
        function starscript() {
            YYYYMMDDstart();
            if (document.attachEvent)
                window.attachEvent("onload", YYYYMMDDstart);
            else
                window.addEventListener('load', YYYYMMDDstart, false);
            workingCalendar();
        }
        function YYYYMMDDstart() {
            //先给年下拉框赋内容
            var y = new Date().getFullYear();
            for (var i = (y - 9); i < (y + 1); i++){
                if(i==y){
                    console.log(i)
                    document.reg_testdate.curYear.options.add(new Option(" " + i + " 年", i,true,true));
                }else{
                    document.reg_testdate.curYear.options.add(new Option(" " + i + " 年", i));
                }
            }
            //赋月份的下拉框
            var m = new Date().getMonth();
            for (var i = 1; i < 13; i++){
                if(i==m+1){
                    document.reg_testdate.curMonth.options.add(new Option(" " + i + " 月", i,true,true));
                }else{
                    document.reg_testdate.curMonth.options.add(new Option(" " + i + " 月", i));
                }
            }

        }

        function YYYYDD(){
            $(".tbody").html("");
            workingCalendar();
        }
        function MMDD(){
            $(".tbody").html("");
            workingCalendar();
        }
        function workingCalendar(){
            calendar.init();
            var date = new Date();
            var day = date.getDate();
            detailsView($(".date_"+day));
        }
    </script>
</head>
<body onload="starscript()">
<div class="layui-row layui-col-space15">
    <div class="layui-col-md12">
        <div class="title">
            <form name="reg_testdate">
                <select id="curYear" name="curYear" onChange="YYYYDD(this.value)">
                    <option value="">请选择 年</option>
                </select>
                <select id="curMonth" name="curMonth" onChange="MMDD(this.value)">
                    <option value="">选择 月</option>
                </select>
            </form>
        </div>
    </div>
    <div class="layui-col-md6">
            <div class="test" style="margin-top: 10px;">
                <table class="rl-table">
                    <thead class="rl-thead">
                        <tr class='rl-week'>
                            <td>日</td>
                            <td>一</td>
                            <td>二</td>
                            <td>三</td>
                            <td>四</td>
                            <td>五</td>
                            <td>六</td>
                        </tr>
                    </thead>
                    <tbody class="rl-tbody">

                    </tbody>
                </table>
            </div>
    </div>
    <div class="layui-col-md6" style="margin-top: 15px;right:20px;background-color: #F2F2F2;">
        <div class="layui-row" style="height: 35%;">
            <div class="layui-col-md12">
                <div class="layui-card">
                    <div class="layui-card-header">考勤信息</div>
                    <div class="layui-card-body" style="height: 80%;">
                        <div id="attendance"></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row" style="height: 50%;">
            <div class="layui-col-md12">
                <div class="layui-card">
                    <%--<div class="layui-card-header">工作安排<div class="homepage-more" onclick="parent.addtab('/schedule/gotoScheduleList.do?type=list','scheduleList','日程管理')">更多</div></div>--%>
                    <div class="layui-card-header">工作安排<div class="homepage-more" onclick="oa.open2Window('/schedule/gotoScheduleList.do?type=add','添加日程')">添加</div></div>
                    <div class="layui-card-body" style="height: 95%;">
                        <div id="workplan"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/09/11
  Time: 下午 2:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<html>
<head>
    <title>我的主页</title>
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/resources/css/homepage.css" type="text/css"/>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
    <style>
        .layui-card-header .layui-icon {
            line-height: initial;
            position: initial;
            right: 15px;
            top: 50%;
            margin-top: -7px;
        }
    </style>
</head>
<body>
<div class="layui-row" id="container">
    <div class="layui-col-md9">
        <div class="layui-row homepage-space homepage-background">
            <div class="layui-col-md12">
                <div class="layui-card" style="height: 17.5%;">
                    <div class="layui-card-header">
                        <i class="layui-icon layui-icon-note"></i> 今日工作安排
                        <div class="homepage-more"
                             onclick="newTab('/schedule/gotoScheduleList.do?type=list','scheduleList','日程管理')">更多
                        </div>
                    </div>
                    <div class="layui-card-body">
                        <div class="layui-row layui-col-space10">
                            <c:if test="${!empty scheduleList}">
                                <c:forEach items="${scheduleList}" var="schedule" varStatus="status">
                                    <div class="layui-col-md3" onclick="contentInfo(this)"
                                         data-value="${schedule.content}">
                                        <div class="homepage-plan"
                                             title="${schedule.scheduletTitle}">${schedule.scheduletTitle}</div>
                                    </div>
                                </c:forEach>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row homepage-space" style="height: 37%;">
            <div class="layui-col-md6 homepage-background">
                <div class="layui-card" style="height: 100%;">
                    <div class="layui-card-header">
                        <i class="layui-icon layui-icon-template-1"></i> 我的任务
                        <div class="homepage-more"
                             onclick="newTab('/task/gototasksender.do','tasksender','我的任务')">更多
                        </div>
                    </div>
                    <div class="layui-card-body">
                        <dl>
                            <c:if test="${!empty taskSenderList}">
                                <c:forEach items="${taskSenderList}" var="task" varStatus="status">
                                    <dd onclick="newTab('${task.refLinkUrl}','${task.workOrderNO}','任务处理')">
                                        <div class="homepage-title">${task.taskTitle}</div>
                                        <div class="homepage-name">
                                            <s:dic type="user" value="${task.recordName}"></s:dic>
                                        </div>
                                        <div class="homepage-time">
                                            <s:dic type="date" value="${task.recordTime}"></s:dic>
                                        </div>
                                    </dd>
                                </c:forEach>
                            </c:if>
                        </dl>
                    </div>
                </div>
            </div>
            <div class="layui-col-md6 homepage-background">
                <div class="layui-card" style="height: 100%;">
                    <div class="layui-card-header">
                        <i class="layui-icon layui-icon-notice"></i> 我的消息
                        <div class="homepage-more"
                             onclick="newTab('/message/seeMessagepage.do', 'msgSendUser', '我的消息')">更多
                        </div>
                    </div>
                    <div class="layui-card-body">
                        <dl>
                            <c:if test="${!empty messageList}">
                                <c:forEach items="${messageList}" var="msg" varStatus="status">
                                    <dd onclick="checkmymsg('${msg.msgId}',0,'')">
                                        <div class="homepage-title">${msg.msgTitle}</div>
                                        <div class="homepage-time">
                                            <s:dic type="date" value="${msg.msgSendTime}"></s:dic>
                                        </div>
                                    </dd>
                                </c:forEach>
                            </c:if>
                        </dl>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row homepage-space" style="height: 37.5%;">
            <div class="layui-col-md6 homepage-background">
                <div class="layui-card" style="height: 100%;">
                    <div class="layui-card-header">
                        <i class="layui-icon layui-icon-read"></i> 通知
                        <div class="homepage-more" onclick="newTab('/commonurl/seeTzggpage.do?type=1', 'tzgg', '通知公告')">
                            更多
                        </div>
                    </div>
                    <div class="layui-card-body">
                        <dl>
                            <c:if test="${!empty tzList}">
                                <c:forEach items="${tzList}" var="msg" varStatus="status">
                                    <dd onclick="seeTzggInfo('${msg.recorderNO}','${msg.tzggl18111002}')">
                                        <div class="homepage-title">${msg.bt18111000001}</div>
                                        <div class="homepage-time">
                                            <s:dic type="date" value="${msg.recordTime}"></s:dic>
                                        </div>
                                    </dd>
                                </c:forEach>
                            </c:if>
                        </dl>
                    </div>
                </div>
            </div>
            <div class="layui-col-md6 homepage-background">
                <div class="layui-card" style="height: 100%;">
                    <div class="layui-card-header">
                        <i class="layui-icon layui-icon-release"></i> 公告
                        <div class="homepage-more" onclick="newTab('/commonurl/seeTzggpage.do?type=2', 'tzgg', '通知公告')">
                            更多
                        </div>
                    </div>
                    <div class="layui-card-body">
                        <dl>
                            <c:if test="${!empty ggList}">
                                <c:forEach items="${ggList}" var="msg" varStatus="status">
                                    <dd onclick="seeTzggInfo('${msg.recorderNO}','${msg.tzggl18111002}')">
                                        <div class="homepage-title">${msg.bt18111000001}</div>
                                        <div class="homepage-time">
                                            <s:dic type="date" value="${msg.recordTime}"></s:dic>
                                        </div>
                                    </dd>
                                </c:forEach>
                            </c:if>
                        </dl>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="layui-col-md3">
        <div class="layui-row homepage-card" style="height: 29%;">
            <div class="layui-col-md12">
                <div class="layui-card" style="height: 100%;">
                    <div class="layui-card-header">常用链接</div>
                    <div class="layui-card-body">
                        <div class="layui-row layui-col-space5">
                            <div class="layui-col-md4 layui-col-sm4 layui-col-xs6">
                                <div class="layui-card oa-system-link"
                                     onclick="newTab('/file/gotofileList.do','fileList','常用文档')">
                                    <span>常用文档</span>
                                </div>
                            </div>
                            <div class="layui-col-md4 layui-col-sm4 layui-col-xs6">
                                <div class="layui-card oa-system-link"
                                     onclick="newTab('/schedule/gotoCheckList.do','workingcalendar','考勤记录')">
                                    <span>考勤记录</span>
                                </div>
                                <%--<div class="layui-card oa-system-link"
                                     onclick="newTab('/schedule/gotoworkingcalendar.do','workingcalendar','考勤纪录')">
                                    <span>考勤纪录</span>
                                </div>--%>
                            </div>
                            <div class="layui-col-md4 layui-col-sm4 layui-col-xs6">
                                <div class="layui-card oa-system-link"
                                     onclick="newTab('/schedule/gotoworkingcalendar.do','workingcalendar','日程管理')">
                                    <span>日程管理</span>
                                </div>
                            </div>
                        </div>
                        <div class="layui-row layui-col-space5">
                            <div class="layui-col-md4 layui-col-sm4 layui-col-xs6">
                                <div class="layui-card oa-system-link"
                                     onclick="newTab('/joblog/gotoJoblogList.do?type=list','joblogList','工作日志')">
                                    <span>工作日志</span>
                                </div>
                            </div>
                            <div class="layui-col-md4 layui-col-sm4 layui-col-xs6">
                                <div class="layui-card oa-system-link"
                                     onclick="newTab('/joblog/gotoCheckJoblog.do?type=list','joblogList','审批日志')">
                                    <span>审批日志</span>
                                </div>
                            </div>
                            <div class="layui-col-md4 layui-col-sm4 layui-col-xs6">
                                <div class="layui-card oa-system-link"
                                     onclick="newTab('/joblog/gotocsuserjoblog.do?type=list','joblogList','查看日志')">
                                    <span>查看日志</span>
                                </div>
                            </div>
                            <div class="layui-col-md4 layui-col-sm4 layui-col-xs6">
                                <div class="layui-card oa-system-link"
                                     onclick="newTab('/examination/getuservalue.do','examination','个人评分')">
                                    <span>个人评分</span>
                                </div>
                            </div>
                            <div class="layui-col-md4 layui-col-sm4 layui-col-xs6">
                                <div class="layui-card oa-system-link"
                                     onclick="oa.open2Window('/common/download.html','常用下载')">
                                    <span>常用下载</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row homepage-card" style="height: 45%;">
            <div class="layui-col-md12">
                <div class="layui-card" style="height: 100%;">
                    <div class="layui-card-header">
                        我的链接
                        <div class="homepage-more" onclick="newTab('/myurl/custommenu.do','custommenu','常用链接')">
                            设置
                        </div>
                    </div>
                    <div class="layui-card-body">
                        <div class="layui-row layui-col-space5" id="usermenu">
                            <c:if test="${!empty usermenu}">
                                <c:forEach items="${usermenu}" var="um" varStatus="status">
                                    <div class="layui-col-md4 layui-col-sm4 layui-col-xs6">
                                        <div class="layui-card oa-system-link"
                                             onclick="newTab('${um.url}','${um.id}','${um.name}')">
                                            <div class="menu-imgbox">
                                                <img style="width:50px;height:50px;" src="${um.menuImg}">
                                            </div>
                                            <span>${um.name}</span>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row homepage-card" style="height: 20%;">
            <div class="layui-col-md12">
                <div class="layui-card" style="height: 98%;">
                    <div class="layui-card-header">系统信息</div>
                    <div class="layui-card-body" style="cursor:default;">
                        <div>在线人数：<span id="online">0</span></div>
                        <div>生&emsp;&emsp;日：<s:page type="birthday"></s:page></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
    <script>
        var dag = window.parent;
        var layer = null;
        layui.use(['layer'], function () {
            layer = layui.layer;
        });
        function newTab(url,id,name){
            if(top.layui.index){
                top.layui.index.openTabsPage(url,name)
            }else{
                window.open(url)
            }
        }
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

        dag.setOnline = function (text) {
            $("#online").text(text);
        }
    </script>
</body>
</html>

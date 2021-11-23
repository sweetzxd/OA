<%--
  User: zxd
  Date: 2019/05/16
  Time: 下午 1:51
  Explain: 说明
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<html>
<head>
    <meta charset="utf-8">
    <title>腾叶智慧办公系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">

    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/resources/css/home.css" type="text/css"/>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
    <script type="text/javascript" src="/resources/js/system/home.js"></script>
    <script type="text/javascript">
        var timeout = "";
        var websocket;

        function starscript() {
            $("#home-mymsg").bind("mouseleave", function () {
                $("#home-mymsg").fadeOut();
            });
            $("#openmymsg").bind("mouseleave", function () {
                timeout = setTimeout(function () {
                    $("#home-mymsg").fadeOut();
                }, 1500);
            });
            $("#home-mymsg").bind("mouseenter", function () {
                clearTimeout(timeout);
            });

            newwebsocket();
        }
        function  logoutpc(){
            if (confirm("您确定要退出系统吗？"))
                top.location = "/logout.do";
            return false;
        }


    </script>
</head>
<body class="layui-layout-body" layadmin-themealias="green-header">

<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-header">
            <!-- 头部区域 -->
            <ul class="layui-nav layui-layout-left">
                <li class="layui-nav-item layadmin-flexible" lay-unselect>
                    <a href="javascript:;" layadmin-event="flexible" title="侧边伸缩">
                        <i class="layui-icon layui-icon-shrink-right" id="LAY_app_flexible"></i>
                    </a>
                </li>
                <li class="layui-nav-item" lay-unselect>
                    <a href="javascript:;" layadmin-event="refresh" title="刷新">
                        <i class="layui-icon layui-icon-refresh-3"></i>
                    </a>
                </li>

            </ul>
            <ul class="layui-nav layui-layout-right" lay-filter="layadmin-layout-right">
                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                    <a lay-href="/leaderSchedule/gotoLeaderSchedule.do" lay-text="领导日程" layadmin-event="leaderSchedule" title="查看领导日程">
                        <i class="layui-icon layui-icon-flag oa-icon"></i>
                    </a>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                    <a href="javascript:void(0);" id="addresslist" title="查看通讯录">
                        <i class="layui-icon layui-icon-cellphone oa-icon"></i>
                    </a>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                    <a lay-href="/task/gototasksender.do" lay-text="我的任务" layadmin-event="tasksender" title="查看我的任务">
                        <i class="layui-icon layui-icon-template-1 oa-icon"></i>
                        <c:if test="${taskNum>0}"><span class="layui-badge-dot"></span></c:if>
                    </a>
                </li>
                <%--<li class="layui-nav-item" lay-unselect>
                    <a lay-href="app/message/index.html" layadmin-event="message" lay-text="消息中心">
                        <i class="layui-icon layui-icon-notice"></i>
                        <span class="layui-badge-dot"></span>
                    </a>
                </li>--%>
                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                    <a href="javascript:;" layadmin-event="theme" title="选择配色">
                        <i class="layui-icon layui-icon-theme"></i>
                    </a>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                    <a href="javascript:;" layadmin-event="fullscreen">
                        <i class="layui-icon layui-icon-screen-full"></i>
                    </a>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                    <a href="javascript:;" id="uploadphoto" layadmin-event="message" style="padding-top:10px;padding-bottom: 10px;">
                        <c:choose>
                            <c:when test="${sessionScope.loginer.photo!=null}">
                                <img class="layui-nav-img" style="background-color: white;margin-right: 0px;" src="${sessionScope.loginer.photo}">
                            </c:when>
                            <c:otherwise>
                                <img class="layui-nav-img" src="/upload/photo/other.png">
                            </c:otherwise>
                        </c:choose>
                    </a>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect style="margin-right: 20px;" >
                    <a href="javascript:;" layadmin-event="message">${sessionScope.loginer.name}</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;" id="modimyemp" style="text-align: center;">基本资料</a></dd>
                        <dd><a href="javascript:;" id="pwupdate" style="text-align: center;">安全设置</a></dd>
                        <hr>
                        <dd onclick="logoutpc()" style="text-align: center;"><a style="cursor:pointer;">退出</a></dd>
                    </dl>
                </li>
            </ul>
        </div>

        <!-- 侧边菜单 -->
        <div class="layui-side layui-side-menu">
            <div class="layui-side-scroll">
                <div class="layui-logo" lay-href="homepage.do">
                    <div class="oa-logo" style="cursor: default;"><img src="/images/oalogo.png"/></div>
                </div>

                <ul class="layui-nav layui-nav-tree" lay-shrink="all" id="LAY-system-side-menu" lay-filter="layadmin-system-side-menu">


                </ul>
                <div class="button-box">
                    <%--天气--%>
                    <div class="date-weather-panel">
                        <div class="date-bar" id="showtime"></div>
                        <c:if test="${weather!=null}">
                            <div class="weather-bar">
                                <div class="weather-imgbox"><img src="${weather.get('img')}"></div>
                                <div class="weather-infor box-none">${weather.get('cityname')} ${weather.get('stateDetailed')} ${weather.get('tem1')}~${weather.get('tem2')}</div>
                            </div>
                        </c:if>
                    </div>
                    <%--限行--%>
                    <div class="limit-bar"><label class="box-none">今日限行尾号：</label>
                        <c:if test="${restrict.get('type')==0}">
                            <span id="todaynum">
                                <b class="xh-span">${restrict.get('data').get('st')}</b>
                                <b class="xh-and">和</b>
                                <b class="xh-span">${restrict.get('data').get('en')}</b>
                            </span>
                        </c:if>
                        <c:if test="${restrict.get('type')!=0}">
                            <span id="todaynum">${restrict.get('data').get('st')}</span>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <!-- 页面标签 -->
        <div class="layadmin-pagetabs" id="LAY_app_tabs">
            <div class="layui-icon layadmin-tabs-control layui-icon-prev" layadmin-event="leftPage"></div>
            <div class="layui-icon layadmin-tabs-control layui-icon-next" layadmin-event="rightPage"></div>
            <div class="layui-icon layadmin-tabs-control layui-icon-down">
                <ul class="layui-nav layadmin-tabs-select" lay-filter="layadmin-pagetabs-nav">
                    <li class="layui-nav-item" lay-unselect>
                        <a href="javascript:;"></a>
                        <dl class="layui-nav-child layui-anim-fadein">
                            <dd layadmin-event="closeThisTabs"><a href="javascript:;">关闭当前标签页</a></dd>
                            <dd layadmin-event="closeOtherTabs"><a href="javascript:;">关闭其它标签页</a></dd>
                            <dd layadmin-event="closeAllTabs"><a href="javascript:;">关闭全部标签页</a></dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <div class="layui-tab" lay-unauto lay-allowClose="true" lay-filter="layadmin-layout-tabs">
                <ul class="layui-tab-title" id="LAY_app_tabsheader">
                    <li lay-id="homepage" lay-attr="homepage.do" class="layui-this"><i class="layui-icon layui-icon-home"></i></li>
                </ul>
            </div>
        </div>


        <!-- 主体内容 -->
        <div class="layui-body" id="LAY_app_body">
            <div class="layadmin-tabsbody-item layui-show">
                <iframe src="homepage.do" frameborder="0" class="layadmin-iframe"></iframe>
            </div>
            <div style="height: 50px;width: 100%;line-height: 50px;background-color: #f6f5ec;text-align: center;position: absolute;bottom: 0;">石家庄华腾科技有限责任公司</div>
        </div>
        <!-- 辅助元素，一般用于移动设备下遮罩 -->
        <div class="layadmin-body-shade" layadmin-event="shade"></div>
    </div>
</div>

<script>
    layui.config({
        base: '/resources/layuiadmin/' //静态资源所在路径
    }).extend({
        index: 'lib/index' //主入口模块
    }).use(['index','element'],function () {
        var $ = layui.jquery
            ,element = layui.element;

        $.ajax({
            type:"GET",
            url:"/getusermenu.do",
            dataType:"json",
            success:function(data){

                //先添加所有的主材单
                $.each(data,function(i,obj){
                    var content='<li class="layui-nav-item">';
                    content+='<a href="javascript:;" lay-tips="'+obj.title+'" lay-direction="2" style="height: 55px;">';
                    content+='<img class="layui-icon" src=""><cite>'+obj.title+'</cite>';
                    content+='<span class="layui-nav-more"></span></a>';
                    //这里是添加所有的子菜单
                    content+=loadchild(obj);
                    content+='</li>';
                    $(".layui-nav-tree").append(content);
                });
                element.init();
            },
            error:function(error){
                aler("发生错误："+ error.status);
            }
        });

        //组装子菜单的方法
        function loadchild(obj){
            if(obj==null){
                return;
            }
            var content='<dl class="layui-nav-child">';
            if(obj.menus!=null && obj.menus.length>0){
                $.each(obj.menus,function(i,note){
                    content+="<dd data-name='" + note.id + "'>";
                    if(note.id=="adminManage") {
                        content+= "<a href='"+note.url+"' data-title='"+note.title+"' class='nav-active'>"+note.title+"</a>";
                    }else{
                    content+="<a lay-href='" + note.url + "' lay-text='" + note.title + "'>"+note.title+"</a>";
                    }
                    content+='</dd>';
                });
                content+='</dl>';
            }
            return content;
        }
    });
</script>
</body>
</html>



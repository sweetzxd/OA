<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/29
  Time: 上午 10:54
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
    <title>工作日志管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
    <script src="/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
    <script src="/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
</head>

<c:if test="${type=='list'}">
<body >
<table class="layui-hide" lay-filter="joblog"
       lay-data="{height: 'full-100',cellMinWidth: 100,even:'false',toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
    <thead>
    <tr>
        <th lay-data="{field:'joblogTitle',align: 'center',width:250}">日志标题</th>
        <th lay-data="{field:'leaderStr',align: 'center',width:100}">上报人</th>
        <th lay-data="{field:'csUserStr',align: 'center',width:100}">抄送人</th>
        <th lay-data="{field:'joblogType',align: 'center',width:100}">日志类型</th>
        <th lay-data="{field:'content'}">工作内容</th>
        <th lay-data="{field:'startTime',align: 'center',width:170,templet:function(d){return timeStr(d.startTime);}}">开始时间</th>
        <th lay-data="{field:'endTime',align: 'center',width:170,templet:function(d){return timeStr(d.endTime);}}">结束时间</th>
        <%--<th lay-data="{field:'file',width:200,templet:function(d){return oa.decipher('uploads',d.file);}}">附件</th>
        <th lay-data="{field:'remark',width:200,width:200}">备注</th>--%>
        <th lay-data="{width:210,templet:function(d){return opt(d.joblogId,d.state,d.leader,d.csUser,d.reason);}}">操作</th>
    </tr>
    </thead>
</table>
<script type="text/html" id="toolbar">
    <div class="layui-btn-container">
        <a data-method="titlebtn" class="layui-btn layui-btn-xs"  href="/joblog/gotoJoblogList.do?type=add">添加日志</a>
    </div>
</script>
<div class="layui-form">
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">检索：</label>
        </div>
        <div class="layui-inline">
            <div class="layui-input-inline">
                <select id="option" name="option" lay-filter="option">
                    <option value="state" selected>日志状态</option>
                   <%-- <option value="status">审批情况</option>--%>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <div class="layui-input-inline">
                <select id='inputval' name='inputval'>
                    <option value='1'>填写</option>
                    <option value='2'>上报</option>
                    <option value='3'>修改</option>
                    <option value='4'>完成</option>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <a class="layui-btn layui-btn-xs" lay-submit lay-filter="reload">搜索</a>
        </div>
    </div>
</div>
</c:if>
<c:if test="${type!='list'}">
<body>
<fieldset class="layui-elem-field">
    <legend>工作日志</legend>
    <form class="layui-form layui-form-pane" id="joblogForm">
        <input type="hidden" name="joblogId" value="${joblog.joblogId}">
        <div class="layui-container">
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">日志标题</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <input type="text" id="joblogTitle" name="joblogTitle" required="" autocomplete="off" value="${joblog.joblogTitle}" class="layui-input error" maxlength="200" aria-invalid="true">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="joblogTitle_Explain"></p>
                    </div>
                </div>
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">日志类型</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <select id="joblogType" name="joblogType" lay-filter="selone" required maxlength="32" class="valid" aria-invalid="false">
                                <option value="">请选择</option>
                                <option value="日报" selected>日报</option>
                                <option value="周报">周报</option>
                            </select>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="joblogType_Explain"></p>
                    </div>
                </div>
            </div>


            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">工作内容</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <textarea id="content_Value" name="content" maxlength="1024" required="" class="layui-textarea" value="${joblog.content}">${joblog.content}</textarea>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="content_Explain"></p>
                        <%--<span class="layui-form-mid required" style="display:block; align-content: center">工作内容最多可填写1024个字</span>--%>
                    </div>
                </div>
            </div>
           <%-- <div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">完成情况</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <textarea id="finish_Value" name="finish" maxlength="1024" required="" class="layui-textarea" value="${joblog.finish}">${joblog.finish}</textarea>
                            <font style="color: red">最多可填写1024个字</font>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="finish_Explain"></p>
                    </div>
                </div>
            </div>--%>
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">开始时间</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <input type="text" id="startTime" name="startTime" autocomplete="off" required="" class="layui-input datetime" value="" toLater="endTime">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="startTime_Explain"></p>
                    </div>
                </div>
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">结束时间</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <input type="text" id="endTime" name="endTime" autocomplete="off" required="" class="layui-input datetime" value="" laterTo="startTime">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="endTime_Explain"></p>
                    </div>
                </div>
            </div>
            <div class="layui-row layui-col-space40">
                    <%--<div class="layui-col-xs6">
                        <div class="layui-form-item component" data-method="offset">
                            <label class="layui-form-label title">所属部门</label>
                            <span class="layui-form-mid required" style="display:block;">*</span>
                            <div class="layui-input-block">
                                <div id="deptId" class="layui-input select-input" onclick="selectdept(this,'dept')">
                                    <div class="oa-fa oa-fa-lg oa-fa-input">
                                        <i class="iconfont iconfont-form icon-xuanbumen1 oa-input-select"></i>
                                        <i class="iconfont icon-tianjia1 oa-input-select-2x"></i>
                                    </div>
                                    <c:if test="${!empty joblog.deptId }">
                                        <span value="${joblog.deptId}">
                                            <label> ${joblog.deptStr} </label><i onclick="event.cancelBubble = true;removeval(this)" class="xm-iconfont icon-close remove"></i>
                                        </span>
                                    </c:if>
                                </div>
                                <input type="hidden" id="deptId_Value" name="deptId" required="" autocomplete="off" value="${joblog.deptId}" class="layui-input error" maxlength="200" aria-invalid="true">
                            </div>
                            <p class="layui-form-mid layui-word-aux" id="deptId_Explain"></p>
                        </div>
                    </div>--%>
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">上报人</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <div id="leader" class="layui-input select-input" onclick="selectuser(this,'user')">
                                <div class="oa-fa oa-fa-lg oa-fa-input">
                                    <i class="iconfont iconfont-form icon-user1 oa-input-select"></i>
                                    <i class="iconfont icon-tianjia1 oa-input-select-2x"></i>
                                </div>
                                <c:if test="${!empty joblog.leader }">
                                    <span value="${joblog.leader}">
                                        <label> ${joblog.leaderStr} </label>
                                        <i onclick="event.cancelBubble = true;removeval(this)" class="iconfont icon-shanchu1"></i>
                                    </span>
                                </c:if>
                            </div>
                            <input type="hidden" id="leader_Value" name="leader" required="" autocomplete="off" value="${joblog.leader}" class="layui-input error" maxlength="200" aria-invalid="true">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="leader_Explain"></p>
                    </div>
                </div>
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">抄送人</label>
                        <span class="layui-form-mid required" style="display:none;">*</span>
                        <div class="layui-input-block">
                            <div id="csUser" class="layui-input select-input" onclick="selectuser(this,'users')">
                                <div class="oa-fa oa-fa-lg oa-fa-input">
                                    <i class="iconfont iconfont-form icon-user1 oa-input-select"></i>
                                    <i class="iconfont icon-tianjia1 oa-input-select-2x"></i>
                                </div>
                                <c:if test="${!empty joblog.csUser }">
                                    <c:forTokens items="${joblog.csUser}" delims=";" var="name">
                                        <span value="${name}">
                                            <label> <s:dic type="user" value="${name}"></s:dic> </label>
                                            <i onclick="event.cancelBubble = true;removeval(this)" class="iconfont icon-shanchu1"></i>
                                        </span>
                                    </c:forTokens>
                                </c:if>
                            </div>
                            <input type="hidden" id="csUser_Value" name="csUser" required="" autocomplete="off" value="${joblog.csUser}" class="layui-input error" maxlength="200" aria-invalid="true">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="csUser_Explain"></p>
                    </div>
                </div>

            </div>
           <%-- <div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">备注</label>
                        <div class="layui-input-block">
                            <textarea id="remark_Value" name="remark" maxlength="1024" required="" class="layui-textarea" value="${joblog.remark}">${joblog.remark}</textarea>
                        </div>
                    </div>
                </div>
            </div>--%>
            <%--<div class="layui-row layui-col-space40">
                <div class="layui-col-xs12">
                    <div class="layui-upload uploads component">
                        <button type="button" id="uploads_file" class="layui-btn layui-btn-normal title uploadList" data-type="uploads" aria-invalid="false">上传附件</button>
                        <span class="layui-form-mid required" style="display:none;">*</span>
                        <div class="layui-upload-list" id="uploads_file_Upload">
                            <table class="layui-table">
                                <thead>
                                    <tr>
                                        <th>文件名</th>
                                        <th>大小</th>
                                        <th>状态</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody class="uploadFildList">
                                    ${filesHtml}
                                </tbody>
                            </table>
                            <input type="hidden" class="form-upload-ins" id="uploads_file_Value" name="file" value="${joblog.file}" maxlength="1024">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="uploads_file_Explain"></p>
                    </div>
                </div>
            </div>--%>
            <div class="layui-row">
                <div class="layui-col-xs12">
                    <div style="float:left; width:33%;text-align:center;">
                        <a class="layui-btn layui-btn-radius" herf="javascript:void(0)" onclick="save('${type}')">保存</a>
                    </div>
                    <div style="float:left; width:33%;text-align:center;">
                        <a class="layui-btn layui-btn-radius" herf="javascript:void(0)" onclick="fromreset('joblogForm')">重置</a>
                    </div>
                    <div style="float:right;width:33%;text-align:center;">
                        <a class="layui-btn layui-btn-radius" herf="javascript:void(0)" href="/joblog/gotoJoblogList.do?type=list">取消</a>
                    </div>
                </div>
            </div>
        </div>
    </form>
</fieldset>
</c:if>

<script type="text/javascript">
    var tableid = 'joblog';
    layui.use(['table','form','laydate'], function () {
        var table = layui.table;
        var form = layui.form;
        var laydate = layui.laydate;

        form.render();
        var stime = "${joblog.startTime.substring(0,10)}";
        if(stime==null || stime==""){
            stime = oa.getNowFormatDate();
        }
        laydate.render({
            elem: '#startTime'
            , format: 'yyyy-MM-dd'
            , type: 'date'
            , value: stime
            , trigger: 'click'
            , calendar: true
            , done: function (value) {
                $("#startTime").val(value);
                $(".layui-form").validate().element($("#startTime"));
            }
        });
        var etime = "${joblog.endTime.substring(0,10)}";
        if(etime==null || etime==""){
            etime = oa.getNowFormatDate();
        }
        laydate.render({
            elem: '#endTime'
            , format: 'yyyy-MM-dd'
            , type: 'date'
            , value: etime
            , trigger: 'click'
            , calendar: true
            , done: function (value) {
                $("#endTime").val(value);
                $(".layui-form").validate().element($("#endTime"));
            }
        });
        table.init('joblog', {
            url: '/joblog/selectlist.do',
            method: 'POST',
            id: 'joblog',
            page: true,
            limit: 10
        });
        uploadsFun("uploads_file");
        var reloadTable = function () {
            table.reload("joblog", { //此处是上文提到的 初始化标识id
                where: {
                    option: jQuery("#option  option:selected").val(),
                    inputval: $('#inputval').val()
                }
            });
        };
        form.on('submit(reload)', function (data) {
            reloadTable();
            return false;
        });
    });

    function fun() {
        return $('#joblogForm');
    }
    function child() {
        var joblogTitle = $(".layui-form").validate().element($('#joblogTitle'));
        var content = $(".layui-form").validate().element($('#content_Value'));
        var start = $(".layui-form").validate().element($('#startTime'));
        var end = $(".layui-form").validate().element($('#endTime'));
        var leader = $(".layui-form").validate().element($('#leader_Value'));
        if (joblogTitle && content && start && end && leader){
            return true;
        }else{
            return false;
        }
    }
    function timeStr(time){
        if(time!=null && time.length>=10){
            time = time.substring(0,10);
        }else{
            time="";
        }
        return time;
    }
    function seeState(state){
        if(state==1){
            return "填写"
        }else if(state==2){
            return "上报"
        }else if(state==3){
            return "修改"
        }else if(state==4){
            return "完成"
        }
    }
    function seeStatus(status){
        if(status==0){
            return "未上报"
        }else if(status==1){
            return "待审批"
        }else if(status==2){
            return "通过"
        }else if(status==3){
            return "打回"
        }
    }
    function opt(id,state,leader,csUser,reason){
        var html = "";
        if(state==1){
            html = "<a class=\"layui-btn layui-btn-primary layui-btn-xs\" onclick=update(\""+id+"\",2,\""+leader+"\",\""+csUser+"\")>上报</a>"+
                   "<a class=\"layui-btn layui-btn-normal layui-btn-xs\" href=\"/joblog/gotoJoblogList.do?type=edit&joblogId="+id+"\">编辑</a>"+
                   "<a class=\"layui-btn layui-btn-danger layui-btn-xs\"  onclick=delete1(\""+id+"\")>删除</a>";
        }else if(state==2){
            html = "<a class=\"layui-btn layui-btn-primary layui-btn-xs\" onclick=update(\""+id+"\",1,\""+leader+"\")>撤回</a>";
        }else if(state==3){
            html = "<a class=\"layui-btn layui-btn-primary layui-btn-xs\" onclick=update(\""+id+"\",2,\""+leader+"\",\""+csUser+"\")>上报</a>"+
                   "<a class=\"layui-btn layui-btn-normal layui-btn-xs\" href=\"/joblog/gotoJoblogList.do?type=edit&joblogId="+id+"\">编辑</a>"+
                   "<a class=\"layui-btn layui-btn-danger layui-btn-xs\"  onclick=delete1(\""+id+"\")>删除</a>"+
                   "<a class=\"layui-btn layui-btn-normal layui-btn-xs\"  onclick=seeReason(\""+reason+"\")>打回原因</a>";
        }

        return html;
    }

    function save(type){
        if(child()){
            var saveurl = "";
            if (type == "add") {
                saveurl = "/joblog/insert.do";
            }else if(type == "edit") {
                saveurl = "/joblog/update.do";
            }
            var datas = fun();
            $.ajax({
                type: "POST",
                url: saveurl,
                data: datas.serializeArray(),
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                cache: false,
                dataType: "json",
                success: function (data) {
                    window.location.href = "/joblog/gotoJoblogList.do?type=list";
                },
                error: function () {
                    layer.msg("请求失败");
                }
            });
        }
    }

    function delete1(id){
        $.ajax({
            type: "POST",
            url: "/joblog/delete.do",
            data: {joblogId:id},
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            cache: false,
            dataType: "json",
            success: function (data) {
                window.location.href = "/joblog/gotoJoblogList.do?type=list";
            },
            error: function () {
                layer.msg("请求失败");
            }
        });
    }

    function update(id,state,leader,csUser){
        $.ajax({
            type: "POST",
            url: "/joblog/update.do",
            data: {joblogId:id,state:state,leader:leader,csUser:csUser},
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            cache: false,
            dataType: "json",
            success: function (data) {
                layer.msg("操作成功");
                window.location.href = "/joblog/gotoJoblogList.do?type=list";
            },
            error: function () {
                layer.msg("请求失败");
            }
        });
    }

    function seeReason(reason) {
        layer.open({
            title:"打回原因"
            ,content: '<textarea style="width: 100%;height:120px;" readonly>'+reason+'</textarea>'
            ,btn: ['确定']
            ,yes: function(index, layero){
                layer.closeAll();
            }
        });
    }


</script>
</body>
</html>

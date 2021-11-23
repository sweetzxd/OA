<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/30
  Time: 下午 4:25
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
    <title>查看日志</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <script type="text/javascript">
        var element = null;
        function starscript() {
            layui.use(['table', 'form', 'layer', 'laydate', 'element', 'tree'], function () {
                var layer = layui.layer;
                var table = layui.table;
                var laydate = layui.laydate;
                var form = layui.form;
                element = layui.element;
                var error = '${error}';
                if (error != null && error != "") {
                    layer.msg(error);
                }

                table.init('joblogTable', {
                    url: '/joblog/selectlist.do',
                    method: 'POST',
                    id: 'joblogTable',
                    page: true,
                    limit: 10,
                    where:{
                        csUser:'${user}'
                    }
                });
               form.render();
                element.render();
                lay('.date').each(function () {
                    laydate.render({
                        elem: this
                    });
                });
            });
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
    </script>
<body onload="starscript()">
<c:if test="${type=='list'}">
    <div>
        <div style="padding: 15px;height:95%">
            <table class="layui-hide" lay-filter="joblogTable"
                   lay-data="{id:'joblogTable',height: 'full-100',toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
                <thead>
                <tr>
                    <th lay-data="{field:'userStr', sort: true,align: 'center',width:100}">员工姓名</th>
                    <th lay-data="{field:'joblogTitle',width:250}">标题</th>
                    <th lay-data="{field:'leaderStr',width:100}">审批人</th>
                    <th lay-data="{field:'content'}">工作内容</th>
                    <th lay-data="{field:'startTime',align: 'center',width:170,templet:function(d){return timeStr(d.startTime);}}">开始时间</th>
                    <th lay-data="{field:'endTime',align: 'center',width:170,templet:function(d){return timeStr(d.endTime);}}">结束时间</th>
                    <th lay-data="{field:'state',align: 'center',width:100,templet:function(d){return seeState(d.state);}}">状态</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</c:if>
</body>
</html>

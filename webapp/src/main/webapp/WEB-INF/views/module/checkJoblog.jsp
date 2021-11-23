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
    <title>日志审批管理</title>
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
                var createTree = function () {
                    var node = "";
                    $.ajax({
                        type: "POST",
                        url: "/department/depttree.do",
                        contentType: "application/json; charset=utf-8",
                        dataType: "html",
                        async: false,
                        success: function (data) {
                            node = eval(data);
                        },
                        error: function (message) {
                            layer.msg("组织结构获取失败！");
                        }
                    });
                    return node;
                };
                layui.tree({
                    elem: '#depttree'
                    , target: '_blank'
                    , nodes: createTree()
                    , click: function (item) {
                        var id = item.id;
                        console.log(id)
                        table.reload("joblogTable", {
                            where: {
                                option: "deptId",
                                inputval: id,
                            }
                        });
                    }
                });
                table.init('joblogTable', {
                    url: '/joblog/selectlist.do',
                    method: 'POST',
                    id: 'joblogTable',
                    page: true,
                    limit: 10,
                    where:{
                        leader:'${user}'
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
        function toCheck(id){
            return '<a class="layui-btn layui-btn-normal layui-btn-xs" onclick="check(\''+id+'\',4)">通过</a>'+
                    '<a class="layui-btn layui-btn-danger layui-btn-xs"  onclick=back(\''+id+'\')>打回</a>';
        }
        function check(id,state,reason){
            $.ajax({
                type: "POST",
                url: "/joblog/update.do",
                data: {joblogId:id,state:state,reason:reason,leader:'${user}'},
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                cache: false,
                dataType: "json",
                success: function (data) {
                    layer.msg("操作成功");
                    window.location.href = "/joblog/gotoCheckJoblog.do";
                },
                error: function () {
                    layer.msg("请求失败");
                }
            });
        }
        function back(id) {
            layer.open({
                title:"打回原因"
                ,content: '<textarea style="width: 100%;height:120px;" id="reason" placeholder="请输入打回原因"></textarea>'
                ,btn: ['确定', '取消']
                ,yes: function(index, layero){
                    check(id,3,$("#reason").val());
                }
            });
        }

    </script>
<body onload="starscript()">
<c:if test="${type=='list'}">
    <div class="layui-side" style="margin-top: 30px">
        <div class="layui-side-scroll">
            <ul id="depttree"></ul>
        </div>
    </div>
    <div class="layui-body" id="rigthPage">
        <div style="padding: 15px;height:95%">
            <table class="layui-hide" lay-filter="joblogTable"
                   lay-data="{id:'joblogTable',height: 'full-100',cellMinWidth: 100,toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
                <thead>
                <tr>
                    <th lay-data="{field:'userStr', sort: true,align: 'center',width:100}">员工姓名</th>
                    <th lay-data="{field:'joblogTitle'}">标题</th>
                    <th lay-data="{field:'content'}">工作内容</th>
                    <th lay-data="{field:'startTime',align: 'center',width:170,templet:function(d){return timeStr(d.startTime);}}">开始时间</th>
                    <th lay-data="{field:'endTime',align: 'center',width:170,templet:function(d){return timeStr(d.endTime);}}">结束时间</th>
                    <th lay-data="{field:'state',align: 'center',width:100,templet:function(d){return seeState(d.state);}}">状态</th>
                <%--<th lay-data="{field:'file',templet:function(d){return oa.decipher('uploads',d.file);}}">附件</th>
                    <th lay-data="{field:'remark'}">备注</th>--%>
                    <th lay-data="{align:'center',templet:function(d){return toCheck(d.joblogId);}}">审批管理</th>
                </tr>
                </thead>

            </table>
            <script type="text/html" id="toolbar">
                <div class="layui-btn-container">
                </div>
            </script>
        </div>
    </div>
</c:if>
</body>
</html>

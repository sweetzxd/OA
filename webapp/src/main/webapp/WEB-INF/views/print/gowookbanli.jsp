<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<jsp:include page="/common/css.jsp"></jsp:include>
<jsp:include page="/common/js.jsp"></jsp:include>
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
<script src="//res.layui.com/layui/dist/layui.js" charset="utf-8"></script>
<html style="height: 100%">
<head>
    <meta charset="utf-8">
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md24">
            <div class="layui-card layui-card-body">
                <fieldset class="layui-elem-field">
                    <legend>督办事项</legend>
                    <%--<div style="text-align: center;font-size: 30px;height: 40px;line-height: 40px;">
                        月报日期 ${toudate}
                    </div>--%>
                    <form class="layui-form layui-form-pane" id="gzzb2019052700001" method="get"
                          novalidate="novalidate">
                        <div class="layui-container">
                            <div id="divappend">
                                <table class="layui-table">
                                    <thead><tr><th>事项来源</th><th>项目内容</th><th>开始时间</th><th>技术时间</th><th>阶段任务及完成时限</th></tr></thead>
                                    <tbody>
                                        <c:forEach items="${data}" var="jsone" varStatus="status" >
                                        <tr>
                                            <td>${jsone.get('sxly')}</td>
                                            <td>${jsone.get('xmnr')}</td>
                                            <td> ${jsone.get('starttime')}</td>
                                            <td> ${jsone.get('entime')}</td>
                                            <td> ${jsone.get('jdrw')}</td>
                                        </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>

                            </div>
                        </div>
                    </form>
                </fieldset>
            </div>
        </div>
    </div>
</div>
<script src="//res.layui.com/layui/dist/layui.js" charset="utf-8"></script>
</body>
</html>
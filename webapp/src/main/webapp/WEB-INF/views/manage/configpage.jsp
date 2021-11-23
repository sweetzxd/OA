<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/10/29
  Time: 下午 3:41
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
    <title>系统相关配置页面</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript">
        function starscript(){
            layui.use(['form','element'], function(){
                var element = layui.element;
                var form = layui.form;
                form.render();
                element.init();
            });
        }
    </script>
</head>
<body onload="starscript()">
<span class="layui-breadcrumb" lay-separator="|">
    <a href="/config/configoapage.do?type=dl">登陆错误提示</a>
    <a href="/config/configoapage.do?type=yx">系统邮箱配置</a>
    <a href="/config/configoapage.do?type=qt">其它相关配置</a>
    <a href="/config/configpathpage.do">系统设置</a>
    <a href="/config/configschedule.do">考勤设置</a>
</span>
<c:if test="${type=='oapage-dl'}">
    <form class="layui-form" action="">
        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;width: 50%;" onclick="$('#login').slideToggle('show')" title="点击收缩">
            <legend>登陆错误提示</legend>
        </fieldset>
        <div id="login" style="width: 50%;">
            <div class="layui-form-item">
                <label class="layui-form-label">用户名不存在</label>
                <div class="layui-input-block">
                    <input type="text" name="error_us" lay-verify="error_us" autocomplete="off" class="layui-input" value="${error_us}">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">用户名错误</label>
                <div class="layui-input-block">
                    <input type="text" name="error_user" lay-verify="error_user" autocomplete="off" class="layui-input" value="${error_user}">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">密码错误</label>
                <div class="layui-input-block">
                    <input type="text" name="error_pw" lay-verify="error_pw" autocomplete="off" class="layui-input" value="${error_pw}">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">登陆IP错误</label>
                <div class="layui-input-block">
                    <input type="text" name="error_ip" lay-verify="error_ip" autocomplete="off" class="layui-input" value="${error_ip}">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">账号状态</label>
                <div class="layui-input-block">
                    <input type="text" name="error_as" lay-verify="error_as" autocomplete="off" class="layui-input" value="${error_as}">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">系统错误</label>
                <div class="layui-input-block">
                    <input type="text" name="error_catch" lay-verify="error_catch" autocomplete="off" class="layui-input" value="${error_catch}">
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit="" lay-filter="oapage">保存</button>
            </div>
        </div>
    </form>
    <script type="text/javascript">
        layui.use(['form', 'layedit', 'laydate'], function(){
            var form = layui.form
            //监听提交
            form.on('submit(oapage)', function(data){
                var jsons = JSON.stringify(data.field);
                console.log(jsons)
                $.ajax({
                    type: "POST",
                    url: "/config/saveoapage.do",
                    data:{'data':jsons},
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    dataType: "json",
                    async: false,
                    success: function (data) {
                        layer.msg("保存成功");
                    },
                    error: function (message) {
                    }
                });
                return false;
            });
        });
    </script>
</c:if>
<c:if test="${type=='oapage-yx'}">
    <form class="layui-form" action="">
        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;width: 50%;" onclick="$('#email').slideToggle('show')" title="点击收缩">
            <legend>系统邮箱配置</legend>
        </fieldset>
        <div id="email" style="width: 50%;">
            <div class="layui-form-item">
                <label class="layui-form-label">是否开启邮件提醒</label>
                <div class="layui-input-block">
                    <input type="radio" name="mail_remind" value="true" title="是" <c:if test="${mail_remind=='true'}">checked</c:if>>
                    <input type="radio" name="mail_remind" value="false" title="否" <c:if test="${mail_remind=='false'}">checked</c:if>>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">邮箱用户名</label>
                <div class="layui-input-block">
                    <input type="text" name="mail_username" lay-verify="mail_username" autocomplete="off" class="layui-input" value="${mail_username}">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">邮箱密码</label>
                <div class="layui-input-block">
                    <input type="text" name="mail_password" lay-verify="mail_password" autocomplete="off" class="layui-input" value="${mail_password}">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">邮箱服务器</label>
                <div class="layui-input-block">
                    <input type="text" name="mail_adress" lay-verify="mail_adress" autocomplete="off" class="layui-input" value="${mail_adress}">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">发件人邮箱</label>
                <div class="layui-input-block">
                    <input type="text" name="mail_form" lay-verify="mail_form" autocomplete="off" class="layui-input" value="${mail_form}">
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit="" lay-filter="oapage">保存</button>
            </div>
        </div>
    </form>
    <script type="text/javascript">
        layui.use(['form', 'layedit', 'laydate'], function(){
            var form = layui.form
            //监听提交
            form.on('submit(oapage)', function(data){
                var jsons = JSON.stringify(data.field);
                console.log(jsons)
                $.ajax({
                    type: "POST",
                    url: "/config/saveoapage.do",
                    data:{'data':jsons},
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    dataType: "json",
                    async: false,
                    success: function (data) {
                        layer.msg("保存成功");
                    },
                    error: function (message) {
                    }
                });
                return false;
            });
        });
    </script>
</c:if>
<c:if test="${type=='oapage-qt'}">
    <form class="layui-form" action="">
        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;width: 50%;" onclick="$('#other').slideToggle('show')" title="点击收缩">
            <legend>其它相关配置</legend>
        </fieldset>
        <div id="other" style="width: 50%;">
            <div class="layui-form-item">
                <label class="layui-form-label">是否开启弹窗消息提醒</label>
                <div class="layui-input-block">
                    <input type="radio" name="open_message" value="true" title="是" <c:if test="${open_message=='true'}">checked</c:if>>
                    <input type="radio" name="open_message" value="false" title="否" <c:if test="${open_message=='false'}">checked</c:if>>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">主键带字</label>
                <div class="layui-input-block">
                    <input type="text" name="recno" lay-verify="recno" autocomplete="off" class="layui-input" value="${recno}">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">表单字段分列</label>
                <div class="layui-input-block">
                    <input type="number" name="form_list" lay-verify="form_list" autocomplete="off" class="layui-input" value="${form_list}">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">是否显示创建人、创建时间</label>
                <div class="layui-input-block">
                    <input type="radio" name="record" value="true" title="是" <c:if test="${open_message=='true'}">checked</c:if>>
                    <input type="radio" name="record" value="false" title="否" <c:if test="${open_message=='false'}">checked</c:if>>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">是否显示修改人、修改时间</label>
                <div class="layui-input-block">
                    <input type="radio" name="modify" value="true" title="是" <c:if test="${open_message=='true'}">checked</c:if>>
                    <input type="radio" name="modify" value="false" title="否" <c:if test="${open_message=='false'}">checked</c:if>>
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit="" lay-filter="oapage">保存</button>
            </div>
        </div>
    </form>
    <script type="text/javascript">
        layui.use(['form', 'layedit', 'laydate'], function(){
            var form = layui.form
            //监听提交
            form.on('submit(oapage)', function(data){
                var jsons = JSON.stringify(data.field);
                console.log(jsons)
                $.ajax({
                    type: "POST",
                    url: "/config/saveoapage.do",
                    data:{'data':jsons},
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    dataType: "json",
                    async: false,
                    success: function (data) {
                        layer.msg("保存成功");
                    },
                    error: function (message) {
                    }
                });
                return false;
            });
        });
    </script>
</c:if>
<c:if test="${type=='pathpage'}">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
        <legend>系统配置</legend>
        <form class="layui-form" action="">
            <div class="layui-form-item">
                <label class="layui-form-label">页面生成位置</label>
                <div class="layui-input-block">
                    <input type="text" name="produce_file" lay-verify="produce_file" autocomplete="off" class="layui-input" value="${produce_file}">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">附件上传位置</label>
                <div class="layui-input-block">
                    <input type="text" name="upload_file" lay-verify="upload_file" autocomplete="off" class="layui-input" value="${upload_file}">
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn" lay-submit="" lay-filter="pathpage">保存</button>
                </div>
            </div>
        </form>
    </fieldset>
    <script type="text/javascript">
        layui.use(['form', 'layedit', 'laydate'], function(){
            var form = layui.form
            //监听提交
            form.on('submit(pathpage)', function(data){
                var jsons = JSON.stringify(data.field);
                console.log(jsons)
                $.ajax({
                    type: "POST",
                    url: "/config/savepathpage.do",
                    data:{'data':jsons},
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    dataType: "json",
                    async: false,
                    success: function (data) {
                        layer.msg("保存成功");
                    },
                    error: function (message) {
                    }
                });
                return false;
            });
        });
    </script>
</c:if>
<c:if test="${type=='schedule'}">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
        <legend>系统配置</legend>
        <form class="layui-form" action="">
            <div class="layui-row">
                <div class="layui-col-md12">
                    <div class="layui-inline">
                        <label class="layui-form-label">上午工作-开始</label>
                        <div class="layui-input-inline">
                            <input type="text" class="layui-input" id="amtowork" name="amtowork" autocomplete="off" value="${schedule.amtowork}">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">上午工作-结束</label>
                        <div class="layui-input-inline">
                            <input type="text" class="layui-input" id="amoffwork" name="amoffwork" autocomplete="off" value="${schedule.amoffwork}">
                        </div>
                    </div>
                </div>
            </div>
            <div class="layui-row">
                <div class="layui-col-md12">
                    <div class="layui-inline">
                        <label class="layui-form-label">下午工作-开始</label>
                        <div class="layui-input-inline">
                            <input type="text" class="layui-input" id="pmtowork" name="pmtowork" value="${schedule.pmtowork}">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">下午工作-结束</label>
                        <div class="layui-input-inline">
                            <input type="text" class="layui-input" id="pmoffwork" name="pmoffwork" value="${schedule.pmoffwork}">
                        </div>
                    </div>
                </div>
            </div>
            <div class="layui-row">
                <div class="layui-col-md12">
                    <div class="layui-form-item" pane="">
                        <label class="layui-form-label">工作日设置</label>
                        <div class="layui-input-block">

                            <input type="hidden" name="monday" value="false">
                            <input type="checkbox" name="monday"  title="周一" value="true" <c:if test="${schedule.monday=='true'}">checked</c:if>>

                            <input type="hidden" name="tuesday" value="false">
                            <input type="checkbox" name="tuesday" title="周二" value="true" <c:if test="${schedule.tuesday=='true'}">checked</c:if>>

                            <input type="hidden" name="wednesday" value="false">
                            <input type="checkbox" name="wednesday" title="周三" value="true" <c:if test="${schedule.wednesday=='true'}">checked</c:if>>

                            <input type="hidden" name="thursday" value="false">
                            <input type="checkbox" name="thursday" title="周四" value="true" <c:if test="${schedule.thursday=='true'}">checked</c:if>>

                            <input type="hidden" name="friday" value="false">
                            <input type="checkbox" name="friday" title="周五" value="true" <c:if test="${schedule.friday=='true'}">checked</c:if>>

                            <input type="hidden" name="saturday" value="false">
                            <input type="checkbox" name="saturday" title="周六" value="true" <c:if test="${schedule.saturday=='true'}">checked</c:if>>

                            <input type="hidden" name="sunday" value="false">
                            <input type="checkbox" name="sunday" title="周日" value="true" <c:if test="${schedule.sunday=='true'}">checked</c:if>>

                        </div>
                    </div>
                </div>
            </div>
            <div class="layui-row">
                <div class="layui-col-md12">
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit="" lay-filter="schedule">保存</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </fieldset>
    <script type="text/javascript">
        layui.use(['form', 'layedit', 'laydate'], function(){
            var form = layui.form
                ,layer = layui.layer
                ,layedit = layui.layedit
                ,laydate = layui.laydate;
            laydate.render({
                elem: '#amtowork'
                ,type: 'time'
            });
            laydate.render({
                elem: '#amoffwork'
                ,type: 'time'
            });
            laydate.render({
                elem: '#pmtowork'
                ,type: 'time'
            });
            laydate.render({
                elem: '#pmoffwork'
                ,type: 'time'
            });
            //监听提交
            form.on('submit(schedule)', function(data){
                var jsons = JSON.stringify(data.field);
                console.log(jsons)
                $.ajax({
                    type: "POST",
                    url: "/config/saveschedule.do",
                    data:{'data':jsons},
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    dataType: "json",
                    async: false,
                    success: function (data) {
                        layer.msg("保存成功");
                    },
                    error: function (message) {
                    }
                });
                return false;
            });
        });
    </script>
</c:if>
</body>
</html>

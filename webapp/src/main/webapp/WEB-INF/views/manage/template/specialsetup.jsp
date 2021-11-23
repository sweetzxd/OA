<%--
  User: zxd
  Date: 2019/05/09
  Time: 上午 10:52
  Explain: 说明
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <meta charset="utf-8">
    <title>特殊权限设置</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
</head>
<body>
<div class="layui-container" style="margin-top: 15px;">
    <form class="layui-form layui-form-pane" id="specialsetup">
        <input type="hidden" name="pageid" value="${access.pageId}"/>
        <input type="hidden" name="roleid" value="${access.roleId}"/>
        <div class="layui-row">
            <div class="layui-col-xs6 layui-col-sm6 layui-col-md6">
                <div class="layui-form-item layui-form-text">
                    <label class="layui-form-label">特殊权限设置</label>
                    <div class="layui-input-block" style="margin-left: 0px !important;">
                        <textarea id="special" name="special" class="layui-textarea">${access.accessValue}</textarea>
                    </div>
                </div>
            </div>
            <div class="layui-col-xs1 layui-col-sm1 layui-col-md1">
                <a class="layui-btn layui-btn-xs layui-btn-normal" style="margin-left: 15px;" href="javascript:void(0);"
                   onclick="setSpecial()">添加</a>
            </div>
            <div class="layui-col-xs5 layui-col-sm5 layui-col-md5">
                <div class="layui-form-item layui-form-text">
                    <div class="layui-input-block" style="margin-left: 5px !important;">
                        <select lay-filter="specialselect" id="specialselect">
                            <option value="">选择特殊权限</option>
                            <option value="loginName">查看本人填写数据</option>
                            <option value="loginDept">查看本部门填写数据</option>
                            <option value="userName-负责人字段">查看本人负责的数据</option>
                            <option value="deptName-负责部门字段">查看本部门负责的数据</option>
                            <option value="gsldName-Z2019061100001">公司领导查看全部数据</option>
                            <option value="roleName-角色ID<多角色逗号隔开>">角色配置查看全部数据</option>
                            <option value="condition-语句">根据单条件查看数据</option>
                            <option value="conditions-语句|语句">根据多条件查看数据</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit="" lay-filter="save">提交</button>
            </div>
        </div>
    </form>
</div>

<jsp:include page="/common/js.jsp"></jsp:include>
<script type="text/javascript">
    layui.use(['form', 'layedit', 'laydate'], function () {
        var form = layui.form;
        form.render();
        form.on('submit(save)', function (data) {
            var jsons = data.field;
            $.post("/access/savesprcialsetup.do",{"pageid": jsons.pageid ,"roleid": jsons.roleid ,"special" :jsons.special},function(data){
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index); //再执行关闭
            });
            return false;
        });
    });

    function setSpecial() {
        var sv = $("#specialselect").find("option:selected").val();
        $("#special").text(sv);
    }
</script>
</body>
</html>

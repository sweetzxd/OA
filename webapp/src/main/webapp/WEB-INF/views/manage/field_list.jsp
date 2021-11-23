<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/08/07
  Time: 上午 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <jsp:include page="/common/var.jsp"></jsp:include>
    <jsp:include page="/common/meta.jsp"></jsp:include>
    <title>字段定义</title>
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
    <%--<script type="text/javascript" src="/resources/js/admin/fieldlist.js"></script>--%>
</head>
<body>
<c:if test="${type=='list'}">
    <table class="layui-hide" lay-filter="fieldTable" lay-data="{id:'fieldTable',height: 'full-100',cellMinWidth: 150,toolbar:'#toolbar'}">
        <thead>
        <tr>
            <th lay-data="{field:'fieldName'}">字段名</th>
            <th lay-data="{field:'fieldTitle'}">字段名称</th>
            <th lay-data="{field:'fieldType', templet:function(d){return oa.getTypeName(d.fieldType);}}">字段类型</th>
            <th lay-data="{field:'modTime', templet:function(d){return oa.decipher('datetime',d.modTime);}}">修改时间</th>
            <th lay-data="{fixed:'right', width:110, align:'center', toolbar: '#listbar'}">管理</th>
        </tr>
        </thead>
    </table>
    <script type="text/html" id="listbar">
        <div class="layui-btn-container">
            <a class="layui-btn layui-btn-xs" lay-event="modi">修改</a>
            <%--<a class="layui-btn layui-btn-xs" lay-event="del">删除</a>--%>
        </div>
    </script>
    <script type="text/html" id="toolbar">
        <div class="layui-btn-container">
            <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="add">添加</a>
            <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="load">刷新内存</a>
        </div>
    </script>
    <div class="layui-form">
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">检索：</label>
            </div>
            <div class="layui-inline">
                <div class="layui-input-inline">
                    <select id="option" name="option">
                        <option value="fieldTitle">字段名称</option>
                        <option value="fieldType">字段类型</option>
                    </select>
                </div>
            </div>
            <div class="layui-inline">
                <div class="layui-input-inline">
                    <input class="layui-input" id="inputval" name="inputval" autocomplete="off">
                </div>
            </div>
            <div class="layui-inline">
                <a class="layui-btn layui-btn-xs" lay-submit lay-filter="reload">搜索</a>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${type!='list'}">
<div class="layui-container" style="margin-top: 15px;">
    <form class="layui-form layui-form-pane" id="fieldform">
        <div class="layui-row" <c:if test="${type=='add'}">hidden</c:if>>
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <div class="layui-form-item">
                    <label class="layui-form-label">字段名称</label>
                    <div class="layui-input-block">
                        <input type="text" name="fieldName" lay-verify="required" <c:if test="${type=='modi'}">readonly  unselectable="on" </c:if> autocomplete="off" class="layui-input" value="${field.fieldName}">
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row">
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <div class="layui-form-item">
                    <label class="layui-form-label">字段名称</label>
                    <div class="layui-input-block">
                        <input type="text" name="fieldTitle" lay-verify="required" autocomplete="off" class="layui-input" value="${field.fieldTitle}">
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row">
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <div class="layui-form-item">
                    <label class="layui-form-label">必填</label>
                    <div class="layui-input-block">
                        <input type="radio" name="required" value="0" title="否" <c:if test="${field.required==0 or field==null}">checked</c:if>>
                        <input type="radio" name="required" value="1" title="是" <c:if test="${field.required==1}">checked</c:if>>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row">
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <div class="layui-form-item">
                    <label class="layui-form-label">字段长度</label>
                    <div class="layui-input-block">
                        <input type="number" name="fieldNum" lay-verify="number" autocomplete="off" class="layui-input" value="<c:if test="${type=='add'}">40</c:if>${field.fieldNum}">
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row">
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <div class="layui-form-item">
                    <label class="layui-form-label">小数位数</label>
                    <div class="layui-input-block">
                        <input type="number" name="fieldDigit" lay-verify="number" autocomplete="off" class="layui-input" value="<c:if test="${type=='add'}">2</c:if>${field.fieldDigit}">
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row">
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <div class="layui-form-item">
                    <label class="layui-form-label">默认值</label>
                    <div class="layui-input-block">
                        <input type="text" name="defaultVal" autocomplete="off" class="layui-input" value="${field.defaultVal}">
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row">
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <div class="layui-form-item">
                    <label class="layui-form-label">字段类型</label>
                    <div class="layui-input-block">
                        <select name="fieldType" lay-filter="fieldType">
                            <option value="text" <c:if test="${field.fieldType=='text'}">selected</c:if>>字符串</option>
                            <option value="textarea" <c:if test="${field.fieldType=='textarea'}">selected</c:if>>长文本</option>
                            <option value="int" <c:if test="${field.fieldType=='int'}">selected</c:if>>整数</option>
                            <option value="decimal" <c:if test="${field.fieldType=='decimal'}">selected</c:if>>小数</option>
                            <option value="date" <c:if test="${field.fieldType=='date'}">selected</c:if>>日期</option>
                            <option value="datetime" <c:if test="${field.fieldType=='datetime'}">selected</c:if>>日期时间</option>
                            <option value="select" <c:if test="${field.fieldType=='select'}">selected</c:if>>单选</option>
                            <option value="selects" <c:if test="${field.fieldType=='selects'}">selected</c:if>>多选</option>
                            <option value="user" <c:if test="${field.fieldType=='user'}">selected</c:if>>单选人</option>
                            <option value="users" <c:if test="${field.fieldType=='users'}">selected</c:if>>多选人</option>
                            <option value="dept" <c:if test="${field.fieldType=='dept'}">selected</c:if>>单部门</option>
                            <option value="depts" <c:if test="${field.fieldType=='depts'}">selected</c:if>>多部门</option>
                            <option value="form" <c:if test="${field.fieldType=='form'}">selected</c:if>>携带字段(单)</option>
                            <option value="forms" <c:if test="${field.fieldType=='forms'}">selected</c:if>>携带字段(多)</option>
                            <option value="upload" <c:if test="${field.fieldType=='upload'}">selected</c:if>>单附件</option>
                            <option value="uploads" <c:if test="${field.fieldType=='uploads'}">selected</c:if>>多附件</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row">
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <div class="layui-form-item layui-form-text">
                    <label class="layui-form-label">选项</label>
                    <div class="layui-input-block" style="margin-left: 0px !important;">
                        <textarea id="optionVal" name="optionVal" class="layui-textarea">${field.optionVal}</textarea>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row">
            <div class="layui-col-xs6 layui-col-sm6 layui-col-md6">
                <div class="layui-form-item layui-form-text">
                    <label class="layui-form-label">特殊设置</label>
                    <div class="layui-input-block" style="margin-left: 0px !important;">
                        <textarea id="special" name="special" class="layui-textarea">${field.special}</textarea>
                    </div>
                </div>
            </div>
            <div class="layui-col-xs1 layui-col-sm1 layui-col-md1">
                <a class="layui-btn layui-btn-xs layui-btn-normal" style="margin-left: 15px;" onclick="setSpecial()">添加</a>
            </div>
            <div class="layui-col-xs5 layui-col-sm5 layui-col-md5">
                <div class="layui-form-item layui-form-text">
                    <div class="layui-input-block" style="margin-left: 5px !important;">
                        <select lay-filter="specialselect">
                            <option value="">选择特殊处理项</option>
                            <option value="[loginName]">默认写入登陆人</option>
                            <option value="[currentDept]">默认写入当前部门</option>
                            <option value="[currentDate]">默认写入当前日期</option>
                            <option value="[currentDateTime]">默认写入当前日期时间</option>
                            <option value="[carrydept-部门字段名]">携带填写部门</option>
                            <option value="[child-表单定义主键]">子表</option>
                            <option value="[laterTo-开始时间字段名]">结束时间比较</option>
                            <option value="[toLater-结束时间字段名]">开始时间比较</option>
                            <option value="[thisdate]">小于等于当前时间</option>
                            <option value="[carryform-任务主键]">携带表单添加</option>
                        </select>

                    </div>
                </div>
            </div>
        </div>
    </form>
    <input type="hidden" id="specialselect" autocomplete="off" class="layui-input" value="">
</div>
</c:if>
<script type="text/javascript">
    layui.use(['table', 'form', 'element'], function () {
        var table = layui.table;
        var form = layui.form;
        var element = layui.element;
        table.init('fieldTable', {
            url: "/dictionary/selectfield.do",
            method: 'POST',
            id: 'fieldTable',
            page: true,
            limit: 14
        });
        table.on('toolbar(fieldTable)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            var othis = $(this);
            var method = othis.data('method');
            switch (obj.event) {
                case 'add':
                    oa.openWindow("/dictionary/gotofieldadd.do","新增字段","../dictionary/insertfield.do")
                    break;
                case 'load':
                    $.post("/dictionary/loadfield.do");
                    break;
            }
        });
        table.on('tool(fieldTable)', function (obj) {
            var data = obj.data
            console.log(data)
            switch (obj.event) {
                case 'modi':
                    var fieldName = data.fieldName;
                    var fieldTitle = data.fieldTitle;
                    oa.openWindow("/dictionary/gotofieldmodi.do?fieldName="+fieldName,fieldTitle,"../dictionary/upfield.do")
                    break;
                case 'del':
                    break;
            }
        });
        form.render();
        form.on('select(specialselect)', function(data){
            $("#specialselect").val(data.value);
        });
        form.on('submit(reload)', function (data) {
            table.reload("fieldTable", {
                where: {
                    option: jQuery("#option  option:selected").val(),
                    inputval: $('#inputval').val(),
                }
            });
            return false;
        });
    });
    function setSpecial(){
        var sv = $("#specialselect").val();
        var sp = $("#special").text();
        if(sv!=null && (sp==null || sp.indexOf(sv)==-1)){
            sp = sp==null?"":sp;
            $("#special").text(sp+sv);
        }
    }

    var dag = window.parent;
    dag.fun = function () {
        return $('#fieldform').serialize();
    }
</script>
</body>
</html>
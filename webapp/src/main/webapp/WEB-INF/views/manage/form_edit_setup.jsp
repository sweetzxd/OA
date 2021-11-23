<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/21
  Time: 下午 1:48
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
</head>
<body>
<div class="layui-container" style="margin-top: 15px;">
    <form class="layui-form layui-form-pane" id="fieldform">
        <div class="layui-row" <c:if test="${type=='add'}">hidden</c:if>>
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <div class="layui-form-item">
                    <label class="layui-form-label">字段名称</label>
                    <div class="layui-input-block">
                        <input type="text" name="fieldName" lay-verify="required" autocomplete="off" class="layui-input" value="${field.fieldName}">
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
                        <input type="number" name="fieldNum" lay-verify="number" autocomplete="off" class="layui-input" value="<c:if test="${type=='add'}">50</c:if>${field.fieldNum}">
                    </div>
                </div>
            </div>
        </div>
        <c:if test="${type=='decimal'}">
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
        </c:if>
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
        <div class="layui-row" hidden>
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <div class="layui-form-item">
                    <label class="layui-form-label">字段类型</label>
                    <div class="layui-input-block">
                        <input type="text" name="fieldType" autocomplete="off" class="layui-input" value="${field.fieldType}">
                    </div>
                </div>
            </div>
        </div>
        <c:if test="${type=='select' || type=='selects'}">
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
        </c:if>
        <c:if test="${type!='select' && type!='selects'}">
            <input type="hidden" name="optionVal" autocomplete="off" class="layui-input" value="${field.optionVal}">
        </c:if>
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
                            <option value="[carryform-任务主键]">携带表单添加</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <input type="hidden" id="specialselect" autocomplete="off" class="layui-input" value="">
</div>
<script type="text/javascript">
    layui.use('form', function () {
        var form = layui.form;
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
    dag.fieldFun = function () {
        var formObject = {};
        var formArray =$("#fieldform").serializeArray();
        $.each(formArray,function(i,item){
            if((item.value).indexOf("\n")>0){
                var a = (item.value).split("\n");
                console.log(a.length)
                for(var i=0;i<a.length;i++){
                    console.log(a[i]);
                }
            }
            formObject[item.name] = item.value;
        });
        return formObject;
    }
</script>
</body>
</html>

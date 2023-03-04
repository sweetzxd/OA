<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/12/6
  Time: 下午 4:19
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
    <title>导入Excel</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
	
</head>

<body>
<fieldset class="layui-elem-field">
    <legend>操作</legend>
    <form style="display: none" method="post" action="/userpage/uploadExcel.do"  id="form" enctype="multipart/form-data">
        <input type="file" name="file" id="file" onchange="$('#form').submit()" accept="application/vnd.ms-excel"/>
        <input name="formId" value="${formCustomMade.formcmName}">
    </form>
    <div>
        <%--<a style="margin-left: 80px;" class="layui-btn layui-btn-danger" href="/excel/template/${formCustomMade.formcmTitle}.xls">下载模板文件</a>--%>
        <a style="margin-left: 80px;" class="layui-btn layui-btn-danger" href="/userpage/exportTableTemp.do?formId=${formCustomMade.formcmName}">下载模板文件</a>
        <a class="layui-btn layui-btn-normal" onclick="$('#file').click();">选择文件上传</a>
        <input type="hidden" value="${filePath}" id="hiddenFilePath">
    </div>
    <div style="margin-top: 20px;">
        <c:if test="${!empty filePath}">
            <a style="margin-left: 80px;" class="layui-btn layui-btn-normal" id="check" onclick="check();">校验文件数据</a>
        </c:if>
    </div>
    <div style="margin-top: 20px; margin-left: 80px;">
        <span style="display:none; color:red; font-size: 20px;" id="checkState"></span>
    </div>
    <div style="display:none; margin-top: 20px;" id="tjfs">
        <form class="layui-form" action="">
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label style="margin-left: 20px;" class="layui-form-label title">添加方式</label>
                        <div class="layui-input-block">
                            <select id="type" style="display: none" required="" lay-filter="selone" maxlength="32">
                                <option value="">请选择</option>
                                <option value="1">追加</option>
                                <option value="2">替换</option>
                            </select>
                            <div class="layui-unselect layui-form-select layui-form-selected">
                                <div class="layui-select-title">
                                    <input type="text" placeholder="请选择" value="" readonly="" class="layui-input layui-unselect valid" aria-invalid="false">
                                    <i class="layui-edge"></i>
                                </div>
                                <dl class="layui-anim layui-anim-upbit" style="">
                                    <dd lay-value="" class="layui-select-tips layui-this">请选择</dd>
                                    <dd lay-value="1" class="">追加</dd>
                                    <dd lay-value="2" class="">替换</dd>
                                </dl>
                            </div>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="see_Explain"></p>
                    </div>
                </div>
            </div>
        </form>

    </div>
    <div style="display:none; margin-top: 20px;" id="tj">
        <a style="margin-left: 80px;" class="layui-btn layui-btn-normal" onclick="add();">添加</a>
    </div>
</fieldset>
<script type="text/javascript">
    layui.use(['form'], function () {
        var form = layui.form;
        form.render();
    });

    function check(){
        var result = "";
        $.ajax({
            type: "POST",
            url: "/userpage/checkExcel.do",
            data:{filePath:$("#hiddenFilePath").val()},
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            cache: false,
            async: false,
            success: function (data) {
                result = data;
            },
            error: function () {
                layer.msg("请求失败");
            }
        });
        $("#checkState").html(result);
        $("#checkState").show();
        if($("#checkState").html()=="校验通过"){
            $("#tjfs").show();
            $("#tj").show();
        }
    }
    function add(){
        if($("#type").val()==null||$("#type").val()==""){
            layer.msg("请选择添加类型");
            return;
        }else{
            $.ajax({
                type: "POST",
                url: "/userpage/importTable.do",
                data:{type:$("#type").val(),filePath:$("#hiddenFilePath").val()},
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                cache: false,
                async: false,
                dataType: "json",
                success: function (data) {
                    parent.location.reload();
                    layer.closeAll();
                },
                error: function () {
                }
            });
        }

    }
</script>
</body>
</html>

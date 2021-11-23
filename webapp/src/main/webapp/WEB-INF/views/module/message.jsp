<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/10/16
  Time: 下午 3:19
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
    <title>${title}</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/resources/js/system/manager.js" charset="utf-8"></script>
    <script type="text/javascript">
        function starscript(pageid, tableid, formid) {
            fieldUpload();
            if ('WebSocket' in window) {
                websocket = new WebSocket("ws://" + window.location.host + "/websocket.do");
            } else if ('MozWebSocket' in window) {
                websocket = new MozWebSocket("ws://" + window.location.host + "/websocket.do");
            }
            websocket.onmessage = function (msg) {
                console.log(msg);
                var data = msg.data;
                if (isJson(data)) {
                    var jsonval = JSON.parse(data);
                    console.log(jsonval.value);
                    setMessageInnerHTML(jsonval.value);
                } else {
                    setMessageInnerHTML(msg.data);
                }
            };
        }

        function closeWebSocket() {
            if (websocket != null) {
                websocket.close();
                websocket = null;
            }
        }

        function setMessageInnerHTML(val) {
            document.getElementById('message').innerHTML += val + '<br/>';
        }

        function send() {
            var msg = $("#mymessage").val();
            $.ajax({
                type: "POST",
                url: "/message/external.do",
                data: {'userId': 'admin', 'message': msg},
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                dataType: "html",
                async: false,
                success: function (data) {

                },
                error: function (message) {
                }
            });
        }
    </script>
</head>
<body onload="starscript('${pageid}','${tableid}','${formid}')">
<div class="layui-upload upload">
    <button type="button" id="upload_demo2" class="layui-btn">单文件上传</button>
    <div class="layui-upload-list">
        <input type="hidden" class="form-upload-ins" name="demo2" id="demo1$upload" value="">
        <p class="upload-text"></p>
    </div>
</div>
<div class="layui-upload uploads">
    <button type="button" id="uploads_demo3" class="layui-btn layui-btn-normal uploadList">选择多文件</button>
    <div class="layui-upload-list" id="uploads_demo3_Upload">
        <table class="layui-table">
            <thead>
            <tr>
                <th>文件名</th>
                <th>大小</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody class="uploadFildList"></tbody>
        </table>
        <input type="hidden" class="form-upload-ins" name="uploads_demo3_Value" id="uploads_demo3_Value" value="">
    </div>
</div>
<div class="layui-upload uploads">
    <button type="button" id="uploads_demo4" class="layui-btn layui-btn-normal uploadList">选择多文件</button>
    <div class="layui-upload-list" id="uploads_demo4_Upload">
        <table class="layui-table">
            <thead>
            <tr>
                <th>文件名</th>
                <th>大小</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody class="uploadFildList"></tbody>
        </table>
        <input type="hidden" class="form-upload-ins" name="uploads_demo4_Value" id="uploads_demo4_Value" value="">
    </div>
</div>


<div class="layui-form-item component" data-method="offset">
    <label class="layui-form-label title" id="txzzs18102402_xzzw181024002_Title" data-type="forms">携带添加</label>
    <span class="layui-form-mid required" style="display:none;">*</span>
    <div class="layui-input-block">
        <div tabindex="0" id="carry_add" name="carry_add" class="layui-input select-input" onfocus="selectform(this,'forms','ldhtg18102501')">
            <div class="oa-fa oa-fa-lg oa-fa-input">
                <i class="iconfont icon-biaodan1 icon-biaodan oa-input-select"></i>
                <i class="iconfont icon-tianjia1 oa-input-select-2x"></i>
            </div>

        </div>
        <input type="hidden" id="carry_add_Value" name="carry_add_Value" autocomplete="off" value="" class="layui-input" maxlength="50">
    </div>
</div>

<span value="Z2018102700002" style="border-radius: 5px;background-color: #009688;color: #FFF;padding: 2px 5px 0 5px;float: left;margin-right: 5px;margin-top: 5px;">
    <label style="float: left;margin-top: 3px;">测试删除</label>
    <i onclick="event.cancelBubble = true;removeval(this)" class="iconfont icon-shanchu1 select-remove" style="float:left;margin-left: -5px;color: #FD8D56"></i>
</span>

<div class="layui-form-item component" data-method="offset">
    <label class="layui-form-label title" id="form_field" data-type="form">携带添加(单)</label>
    <span class="layui-form-mid required" style="display:none;">*</span>
    <div class="layui-input-inline">
        <div id="form_field_Name" name="form_field" class="layui-input users_select" onclick="selectform(this,'form','')">
        </div>
        <input type="hidden" maxlength="200" id="form_field_Value" name="form_field_Value" autocomplete="off" class="layui-input" value="">
    </div>
    <p class="layui-form-mid layui-word-aux" id="form_field_help">辅助说明</p>
</div>


<div class="layui-form-item component" data-method="offset">
    <label class="layui-form-label title" id="forms_field" data-type="forms">携带添加(多)</label>
    <span class="layui-form-mid required" style="display:none;">*</span>
    <div class="layui-input-inline">
        <div id="forms_field_Name" name="forms_field" class="layui-input users_select" onclick="selectform(this,'forms','')">
            <input type="hidden" maxlength="200" id="forms_field_Value" name="forms_field_Value" autocomplete="off" class="layui-input" value="">
        </div>
    </div>
    <p class="layui-form-mid layui-word-aux" id="forms_field_Help">辅助说明</p>
</div>

<div class="layui-form-item">
    <label class="layui-form-label">输入信息</label>
    <div class="layui-input-block">
        <input type="text" class="layui-input" name="mymessage" id="mymessage" value="${emp.staffName}">
    </div>
    <button onclick="send()">发送消息</button>
    <button onclick="closeWebSocket()">关闭连接</button>
    <button onclick="clear()">清空</button>
    <div id="message"></div>
</div>

</body>
</html>

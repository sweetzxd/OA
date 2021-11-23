<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/10/13
  Time: 下午 4:15
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
    <title>更新包管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <script src="/resources/js/system/manager.js" charset="utf-8"></script>
    <script type="text/javascript">
        var element = null;
        function starscript() {
            layui.use(['table', 'form', 'layer', 'laydate', 'element', 'tree'], function () {
                var layer = layui.layer;
                var table = layui.table;
                var laydate = layui.laydate;
                var form = layui.form;
                element = layui.element;
                    table.init('UpdatePackage', {
                    url: '/updatepack/select.do',
                    method: 'POST',
                    id: 'UpdatePackage',
                    page: true,
                    limit: 10
                });
                table.on('toolbar(UpdatePackage)', function (obj) {
                    var checkStatus = table.checkStatus(obj.config.id);
                    var data = checkStatus.data;
                    var othis = $(this);
                    var method = othis.data('method');

                    switch (obj.event) {
                        case 'add':
                            oa.gotourl("/updatepack/addupdatepack.do");
                            break;
                        case 'del':
                            if (data.length === 0) {
                                layer.msg('请选择一行');
                            } else {
                                var packIds = "";
                                for (var i = 0; i < data.length; i++) {
                                    packIds += data[i]["packId"] + ";";
                                }
                                console.log(packIds)
                                if (packIds != "") {
                                    $.ajax({
                                        type: "POST",
                                        url: "/updatepack/delete.do?packId="+packIds,
                                        contentType: "application/json; charset=utf-8",
                                        dataType: "html",
                                        cache: false,
                                        success: function (data) {
                                            oa.gotourl("/updatepack/gotoupdatepack.do");
                                        },
                                        error: function (message) {
                                            layer.msg("提交数据失败！");
                                        }
                                    });
                                }
                            }
                            break;
                    }
                });
                table.on('tool(UpdatePackage)', function (obj) {
                    var data = obj.data
                    //console.log(data)
                    switch (obj.event) {
                        case 'modi':
                            packModi(data.packId);
                            break;
                        case 'del':
                            packDelete(data.packId);
                            break;
                        case 'update':
                            packUpdate(data.packId);
                            break;
                    }
                });
                form.render();
                element.render();
                lay('.datetime').each(function () {
                    laydate.render({
                        elem: this
                        ,type: 'datetime'
                    });
                });
                fieldUpload();
                form.on('submit(formsubmit)', function (data) {
                    //console.log(data.toString())
                    $.ajax({
                        type: "POST",
                        url: "/updatepack/save/${type}.do",
                        data: data.field,
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        dataType: "json",
                        async: false,
                        success: function (data) {
                            oa.gotourl("/updatepack/gotoupdatepack.do");
                        },
                        error: function (message) {
                        }
                    });
                    return false;
                });
            });
        }
        function packModi(value) {
            oa.gotourl("/updatepack/modiupdatepack.do?packId=" + value);
        }
        function packUpdate(value){
            $.ajax({
                type: "POST",
                url: "/updatepack/update.do?packId=" + value,
                contentType: "application/json; charset=utf-8",
                dataType: "html",
                async: false,
                success: function (data) {

                },
                error: function (message) {
                }
            });
        }
        function packDelete(value) {
            console.log(value)
            $.ajax({
                type: "POST",
                url: "/updatepack/delete.do?packId="+value,
                contentType: "application/json; charset=utf-8",
                dataType: "html",
                async: false,
                success: function (data) {

                },
                error: function (message) {
                }
            });
        }
    </script>
<body onload="starscript()">
<c:if test="${type=='list'}">
    <table class="layui-hide" lay-filter="UpdatePackage"
           lay-data="{id:'UpdatePackage',height: 'full-100',cellMinWidth: 100,toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
        <thead>
        <tr>
            <th lay-data="{type:'checkbox', fixed: 'left',field:'packId'}">选择</th>
            <th lay-data="{fixed: 'right', width:200, align:'left', toolbar: '#listbar'}">操作</th>
            <th lay-data="{field:'packName', sort: true}">更新包名称</th>
            <th lay-data="{field:'packType', sort: true}">更新包类型</th>
            <th lay-data="{field:'packUpTime'}">上传时间</th>
            <th lay-data="{field:'packUpFile'}">上传路径</th>
            <th lay-data="{field:'packUpName_Value'}">上传人</th>
            <th lay-data="{field:'packVersion'}">更新包版本</th>
        </tr>
        </thead>

    </table>
    <script type="text/html" id="listbar">
        <div class="layui-btn-container">
            {{# if(d.updateType == 0 && d.packType=="APP"){  }}
            <a class="layui-btn layui-btn-xs" lay-event="modi">修改</a>
            <a class="layui-btn layui-btn-xs" lay-event="del">删除</a>
            <a class="layui-btn layui-btn-xs" lay-event="update">更新</a>
            {{#  } else { }}
            <a class="layui-btn layui-btn-xs" lay-event="modi">修改</a>
            <a class="layui-btn layui-btn-xs" lay-event="del">删除</a>
            {{# } }}
        </div>
    </script>
    <script type="text/html" id="toolbar">
        <div class="layui-btn-container">
            <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="add">添加</a>
            <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="del">删除</a>
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
                        <option value="packName">更新包名称</option>
                        <option value="packType">更新包类型</option>
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
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>更新包台账</legend>
        <div class="layui-container">
            <div class="layui-row">
                <div class="layui-col-xs12">
                    <form class="layui-form layui-form-pane" id="gxbtz2018111600001">
                        <div class="layui-container">
                            <div class="layui-form-item" hidden>
                                <label class="layui-form-label">台账主键</label>
                                <div class="layui-input-block">
                                    <input type="text" class="layui-input" name="packId" value="${pack.packId}" readonly unselectable="on">
                                    <input type="number" class="layui-input" name="updateType" value="<c:if test="${type=='add'}">0</c:if>${pack.updateType}">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">更新包名称</label>
                                <div class="layui-input-block">
                                    <input type="text" class="layui-input" name="packName" value="${pack.packName}">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">更新包类型</label>
                                <div class="layui-input-block">
                                    <select name="packType" lay-verify="required" class="layui-select">
                                        <option value="功能" <c:if test="${pack.packType=='功能'}">selected</c:if>>功能</option>
                                        <option value="BUG" <c:if test="${pack.packType=='BUG'}">selected</c:if>>BUG</option>
                                        <option value="其它" <c:if test="${pack.packType=='其它'}">selected</c:if>>其它</option>
                                        <option value="APP" <c:if test="${pack.packType=='APP'}">selected</c:if>>APP</option>
                                    </select>
                                </div>
                            </div>

                            <div class="layui-form-item">
                                <label class="layui-form-label">更新包依赖</label>
                                <div class="layui-input-block">
                                    <input type="text" class="layui-input" name="packRely" value="${pack.packRely}">
                                </div>
                            </div>

                            <div class="layui-form-item">
                                <label class="layui-form-label">上传时间</label>
                                <div class="layui-input-block">
                                    <input type="text" name="packUpTime" id="packUpTime" autocomplete="off" class="layui-input datetime" value="${pack.packUpTime}">
                                </div>
                            </div>
                            <div class="layui-form-item component" data-method="offset">
                                <label class="layui-form-label title" id="packUpName_Title" data-type="user">上传人</label>
                                <span class="layui-form-mid required">*</span>
                                <div class="layui-input-block">
                                    <div id="packUpName"  name="packUpName" class="layui-input select-input" onclick="selectuser(this,'user')">
                                        <div class="oa-fa oa-fa-lg oa-fa-input">
                                            <i class="iconfont iconfont-form icon-user1 oa-input-select"></i>
                                            <i class="iconfont icon-tianjia1 oa-input-select-2x"></i>
                                        </div>
                                        <c:if test="${pack.packUpName_Value !=null}">
                                            <span value='${pack.packUpName_Value}'><label><s:dic type="user" value="${pack.packUpName_Value}"></s:dic></label><i onClick='event.cancelBubble = true;removeval(this)' class='iconfont icon-shanchu1 xm-iconfont icon-close remove'></i></span>
                                        </c:if>
                                    </div>
                                    <input type="hidden" maxlength="200" id="packUpName_Value" name="packUpName_Value" class="layui-input" value="${pack.packUpName_Value}">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">更新包版本</label>
                                <div class="layui-input-block">
                                    <input type="text" name="packVersion" autocomplete="off" placeholder="更新包版本"
                                           class="layui-input" value="${pack.packVersion}">
                                </div>
                            </div>
                            <div class="layui-upload upload">
                                <button type="button" id="upload_packUpFile" class="layui-btn">上传路径</button>
                                <div class="layui-upload-list">
                                    <input type="hidden" class="form-upload-ins" name="packUpFile" id="packUpFile$upload" value="${pack.packUpFile}">
                                    <p class="upload-text"></p>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <div class="layui-input-block">
                                    <button class="layui-btn" lay-submit lay-filter="formsubmit">立即提交</button>
                                    <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </fieldset>
</c:if>
</body>
</html>

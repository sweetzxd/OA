<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/10/13
  Time: 下午 4:16
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
    <title>文件类型管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <script type="text/javascript">
        var ptable = null, treeGrid = null, tableId = 'treeTable', layer = null, tableSelect = null;
        function starscript() {
            layui.config({
                base: '/resources/js/admin/'
            }).extend({
                treeGrid: 'treeGrid',
                tableSelect: 'tableSelect'
            }).use(['form', 'jquery', 'treeGrid', 'layer', 'tableSelect'], function () {
                var $ = layui.jquery;
                var form = layui.form;
                form.render();
                treeGrid = layui.treeGrid;//很重要
                layer = layui.layer;
                tableSelect = layui.tableSelect;
                ptable = treeGrid.render({
                    id: tableId
                    , elem: '#' + tableId
                    , idField: 'fileTypeId'
                    , url: '/filetype/selectlist.do'
                    , cellMinWidth: 200
                    , treeId: 'fileTypeId'//树形id字段名称
                    , treeUpId: 'superiorFileTypeId'//树形父id字段名称
                    , treeShowName: 'fileTypeName'//以树形式显示的字段
                    , height: 'full-140'
                    , isFilter: false
                    , iconOpen: false//是否显示图标【默认显示】
                    , isOpenDefault: true//节点默认是展开还是折叠【默认展开】
                    , cols: [[
                        {type: 'numbers', fixed: 'left'}
                        , {field: 'fileTypeName',title: '类型名称', fixed: 'true'}
                        , {
                            field: 'superiorFileTypeId',align: 'center', title: '上级类型', templet: function (d) {
                                return oa.decipher('filetype', d.superiorFileTypeId);
                            }
                        }
                        , {
                            field: 'roleId', title: '执行角色', templet: function (d) {
                                return oa.decipher('roles', d.roleId);
                            }
                        }
                        , {title: '操作',width:170,align: 'right', fixed: 'right',templet: function (d) {
                                if(d.superiorFileTypeId==""||d.superiorFileTypeId==null){
                                    return '<a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="add">添加</a>'+
                                            '<a data-method="treebtn" class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit">编辑</a>'+
                                            '<a data-method="treebtn" class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>';
                                }else{
                                    return '<a data-method="treebtn" class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit">编辑</a>'+
                                            '<a data-method="treebtn" class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>';
                                }

                            }
                        }
                    ]]
                    , page: true
                    , parseData: function (res) {//数据加载后回调
                        return res;
                    }
                    , onClickRow: function (index, o) {
                    }
                    , onDblClickRow: function (index, o) {
                        var menuNum = o["menuNum"];
                        var id = o["fileTypeId"]
                        if (parseInt(menuNum) == 1) {
                            openorclose(id);
                        }
                    }
                });

                treeGrid.on('tool(' + tableId + ')', function (obj) {
                    treebutton(obj);
                });
                form.on('submit(formsubmit)', function (data) {
                    var saveurl = "", type = '${type}';
                    if (type == 'add') {
                        saveurl = "/filetype/insert.do";
                    } else {
                        saveurl = "/filetype/update.do";
                    }
                    $.ajax({
                        type: "POST",
                        url: saveurl,
                        data: data.field,
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        dataType: "json",
                        async: false,
                        success: function (data) {
                            window.location.href = "/filetype/gotofiletype.do";
                        },
                        error: function (message) {
                        }
                    });
                    return false;
                });
            });
        }

        function treebutton(obj) {
            var type = obj ? obj.event : 'add';
            var pdata = obj ? obj.data : null;
            var pid = pdata ? pdata.superiorFileTypeId : '';
            var id = pdata ? pdata.fileTypeId : '';
            if (type == "add") {
                oa.gotourl('/filetype/gotoadd.do?pid=' + id);
            } else if(type == "del"){
                if(pdata.superiorFileTypeId==""||pdata.superiorFileTypeId==null){
                    $.ajax({
                        type: "POST",
                        url: "/filetype/isExistNext.do",
                        data: {superiorFileTypeId:id},
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        dataType: "json",
                        async: false,
                        success: function (data) {
                            if(data==0){
                                oa.gotourl('/filetype/delete.do?fileTypeId=' + id);
                            }else{
                                alert("该文件类型下存在二级类型，不可删除！");
                                return;
                            }
                        },
                        error: function (message) {
                        }
                    });
                }else{
                    oa.gotourl('/filetype/delete.do?fileTypeId=' + id);
                }
            } else if(type == "edit"){
                oa.gotourl('/filetype/gotoedit.do?fileTypeId=' + id);
            }
        }

        function selectrole(obj) {
            var id = $(obj).attr("id");
            tableSelect.render({
                elem: '#' + id,
                searchKey: 'inputval',
                searchPlaceholder: '',
                table: {
                    url: '/role/selectAll.do?option=roleTitle',
                    cols: [[
                        {type: 'radio'},
                        {field: 'roleId', title: 'ID', width: 100},
                        {field: 'roleTitle', title: '名称', width: 300},
                    ]]
                },
                done: function (elem, data) {
                    var role = data.data;
                    var val = $("#roleId").attr("value");
                    if(val.indexOf(role[0].roleId)<0){
                        var span = "<span value='" + role[0].roleId + "'> <label>" + role[0].roleTitle + "</label><i onClick='event.cancelBubble = true;removeval(this)' class='xm-iconfont icon-close remove'></i></span>";
                        $("#allselect").find(".layui-input").append(span);
                        val = val + role[0].roleId + ";";
                        $("#roleId").attr("value", val);
                    }else{
                        layer.msg("所选角色已存在");
                    }
                }
            });
        }

        function removeval(object){
            var obj = $(object).parent();
            var val = "";
            obj.remove();
            $("#allselect").find("span").each(function () {
                var tvalue = $(this).attr("value");
                if(val.indexOf(tvalue)<0){
                    val += $(this).attr("value") + ";"
                }
            });
            $("#roleId").attr("value", val);

        }

        function openorclose(value) {
            var map = treeGrid.getDataMap(tableId);
            var o = map[value];
            treeGrid.treeNodeOpen(o, !o[treeGrid.config.cols.isOpen]);
        }
        function openAll() {
            var treedata = treeGrid.getDataTreeList(tableId);
            treeGrid.treeOpenAll(tableId, !treedata[0][treeGrid.config.cols.isOpen]);
        }
    </script>
<body onload="starscript()">
<c:if test="${type=='list'}">
    <div>
        <a class="layui-btn layui-btn-primary" onclick="treebutton(null);">新增一级类型</a>
        <a class="layui-btn layui-btn-primary" onclick="openAll();">展开或折叠全部</a>
    </div>
    <div style="height:100%">
        <table class="layui-hidden" id="treeTable" lay-filter="treeTable"></table>
    </div>
</c:if>
<c:if test="${type!='list'}">
<fieldset class="layui-elem-field">
    <legend><c:if test="${type=='add'}">新增</c:if><c:if test="${type=='edit'}">编辑</c:if>文件类型</legend>
    <form class="layui-form layui-form-pane" id="fileForm">
        <input type="hidden" name="fileTypeId" value="${filetype.fileTypeId}">
        <input type="hidden" name="superiorFileTypeId" value="<c:if test="${type=='edit'}">${filetype.superiorFileTypeId}</c:if><c:if test="${type=='add'}">${parent}</c:if>">
        <div class="layui-container">
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">类型名称</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block">
                            <input type="text" id="fileTypeName" name="fileTypeName" required="" autocomplete="off" class="layui-input" value="<c:if test="${type=='edit'}">${filetype.fileTypeName}</c:if><c:if test="${type=='add'}">新建文件类型</c:if>">
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="fileTypeName_Explain"></p>
                    </div>
                </div>
            </div>
            <div class="layui-row layui-col-space40">
                <div class="layui-col-xs6">
                    <div class="layui-form-item component" data-method="offset">
                        <label class="layui-form-label title">执行角色</label>
                        <span class="layui-form-mid required" style="display:block;">*</span>
                        <div class="layui-input-block" id="allselect">
                            <input type="hidden" id='roleId' name='roleId' value="<c:if test="${type=='edit'}">${filetype.roleId}</c:if>"
                                   autocomplete="off" debounce="0">
                            <div class="layui-input select-dept" id="roleName" onclick="selectrole(this)">
                                <c:if test="${!empty roleList }">
                                    <c:forEach items="${roleList}" var="role" varStatus="status">
                                        <span value='${role.roleId}'>
                                            <label>${role.roleTitle}</label>
                                            <i onClick='event.cancelBubble = true;removeval(this)'class='xm-iconfont icon-close remove'></i>
                                        </span>

                                    </c:forEach>
                                </c:if>
                            </div>
                        </div>
                        <p class="layui-form-mid layui-word-aux" id="roleId_Explain"></p>
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn" lay-submit lay-filter="formsubmit">立即提交</button>
                </div>
            </div>
        </div>
    </form>
</fieldset>
</c:if>
</body>
</html>


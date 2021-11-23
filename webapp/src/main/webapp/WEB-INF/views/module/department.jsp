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
    <title>部门信息管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/resources/css/flowpage.css" type="text/css"/>
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
                    , idField: 'deptId'
                    , url: '/department/selectlist.do'
                    , cellMinWidth: 200
                    , treeId: 'deptId'//树形id字段名称
                    , treeUpId: 'superiorDeptId'//树形父id字段名称
                    , treeShowName: 'deptName'//以树形式显示的字段
                    , height: 'full-40'
                    , isFilter: false
                    , iconOpen: false//是否显示图标【默认显示】
                    , isOpenDefault: false//节点默认是展开还是折叠【默认展开】
                    , limit: 0
                    , cols: [[
                        {type: 'numbers', fixed: 'left'}
                        , {field: 'deptName', title: '部门名称', fixed: 'true'}
                        , {field: 'deptId', width: '20%', title: '部门编号'}
                        , {
                            field: 'superiorDeptId', width: '20%', title: '上级部门', templet: function (d) {
                                return oa.decipher('dept', d.superiorDeptId);
                            }
                        }
                        , {
                            field: 'deptHead', width: '20%', title: '部门负责人', templet: function (d) {
                                return oa.decipher('user', d.deptHead);
                            }
                        }
                        , {width: '15%', align: 'right', templet: '#titleTpl', fixed: 'right'}
                    ]]

                    , page: true
                    , parseData: function (res) {//数据加载后回调
                        return res;
                    }
                    , onClickRow: function (index, o) {
                    }
                    , onDblClickRow: function (index, o) {
                        var menuNum = o["menuNum"];
                        var id = o["deptId"]
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
                        saveurl = "/department/insert.do";
                    } else {
                        saveurl = "/department/update.do";
                    }
                    $.ajax({
                        type: "POST",
                        url: saveurl,
                        data: data.field,
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        dataType: "json",
                        async: false,
                        success: function (data) {
                            window.location.href = "/department/gotodepartment.do";
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
            var pid = pdata ? pdata.superiorDeptId : 'organize';
            var id = pdata ? pdata.deptId : 'organize';
            if (type == "add") {
                oa.gotourl('/department/deptadd.do?pid=' + id);
            } else if(type == "del"){
                oa.gotourl('/department/delete.do?deptId=' + id);
            } else if(type == "edit"){
                oa.gotourl('/department/deptedit.do?deptId=' + id);
            } else {
                oa.gotourl('/department/deptmodi/' + id + '.do?pid=' + pid);
            }
        }

        function selectuser(obj) {
            var id = $(obj).attr("id");
            tableSelect.render({
                elem: '#' + id,
                searchKey: 'inputval',
                searchPlaceholder: '',
                table: {
                    url: '/user/selectAll.do',
                    cols: [[
                        {type: 'radio'},
                        {field: 'userName', title: 'ID', width: 100},
                        {field: 'name', title: '姓名', width: 300},
                    ]]
                },
                done: function (elem, data) {
                    var user = data.data;
                    document.getElementById(id).value = user[0].name;
                    var inputName = id.substring(0, id.indexOf("Name"));
                    document.getElementById(inputName).value = user[0].userName;
                }
            });
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
        <a class="layui-btn layui-btn-primary" onclick="treebutton(null);">新增主节点</a>
        <a class="layui-btn layui-btn-primary" onclick="openAll();">展开或折叠全部</a>
    </div>
    <div style="height:95%">
        <table class="layui-hidden" id="treeTable" lay-filter="treeTable"></table>
    </div>
    <script type="text/html" id="titleTpl">
        <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="add">添加</a>
        <a data-method="treebtn" class="layui-btn layui-btn-normal layui-btn-xs" lay-event="modi">编辑</a>
        <a data-method="treebtn" class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit">维护成员</a>
        <a data-method="treebtn" class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
    </script>
</c:if>
<c:if test="${type!='list' && type!='edit'}">
    <div class="layui-container">
        <div class="layui-row">
            <div class="layui-col-xs12">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
                    <legend>部门信息</legend>
                </fieldset>
                <form class="layui-form layui-form-pane" id="dept_edit">
                    <div class="layui-form-item" hidden>
                        <label class="layui-form-label">部门主键:</label>
                        <div class="layui-input-block">
                            <input type='text' name='deptId' autocomplete='off' readonly unselectable="on"
                                   value='${dept.deptId}' class='layui-input'>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">部门名称:</label>
                        <div class="layui-input-block">
                            <input type='text' id='deptName' name='deptName' autocomplete='off'
                                   value='<c:if test="${type=='modi' || type=='edit'}">${dept.deptName}</c:if><c:if test="${type=='add'}">新建部门</c:if>'
                                   class='layui-input'>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">上级部门:</label>
                        <div class="layui-input-block">
                            <input type='hidden' name='superiorDeptId' autocomplete='off' value='${parent.deptId}'
                                   readonly unselectable="on" class='layui-input'>
                            <input type='text' name='superiorDeptIdName' autocomplete='off' value='${parent.deptName}'
                                   readonly unselectable="on" class='layui-input'>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">上级负责人:</label>
                        <div class="layui-input-block">
                            <input type='hidden' name='superiorDeptHead' autocomplete='off' value='${parent.deptHead}'
                                   readonly unselectable="on" class='layui-input'>
                            <input type='text' name='superiorDeptHeadName' autocomplete='off'
                                   value='<s:dic type="user" value="${parent.deptHead}"></s:dic>' readonly
                                   unselectable="on" class='layui-input'>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">部门负责人:</label>
                        <div class="layui-input-block">
                            <input type='hidden' id='deptHead' name='deptHead' autocomplete='off'
                                   value='${dept.deptHead}' class='layui-input'>
                            <input type="text" id="deptHeadName" name="deptHeadName" autocomplete="off"
                                   class="layui-input" readonly unselectable="on"
                                   value='<s:dic type="user" value="${dept.deptHead}"></s:dic>'
                                   onfocus="selectuser(this)">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">部门负责人（副）:</label>
                        <div class="layui-input-block">
                            <input type='hidden' id='deputyDeptHead' name='deputyDeptHead' autocomplete='off'
                                   value='${dept.deputyDeptHead}' class='layui-input'>
                            <input type='text' id="deputyDeptHeadName" name='deputyDeptHeadName' autocomplete='off'
                                   class='layui-input' readonly unselectable="on"
                                   value='<s:dic type="users" value="${dept.deputyDeptHead}"></s:dic>'
                                   onfocus="selectusers(this)">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="formsubmit">立即提交</button>
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                            <a href="javascript:history.go(-1);" class="layui-btn layui-btn-primary">返回</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${type=='edit'}">
<div class="layui-container">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>维护部门内人员  [${dept.deptName}]</legend>
    </fieldset>
    <div class="layui-row" style="margin-bottom: 20px;">
        <a href='/department/deptedit.do?deptId=${deptId}' class="layui-btn layui-btn-primary">刷新</a>
        <a href='javascript:void(0)' onclick="empedit('${deptId}')" style="padding-left: 30px" class="layui-btn layui-btn-primary">保存</a>
        <a href="javascript:history.go(-1);" class="layui-btn layui-btn-primary">返回</a>
    </div>
    <div class="layui-row">
        <div class="layui-col-xs3">
            <div class="grid-demo grid-demo-bg1">
                <select id="leftop" multiple="multiple" style="width:200px; height:240px;">
                    <c:if test="${!empty userlist}">
                        <c:forEach items="${userlist}" var="user" varStatus="status">
                            <option value="${user.staffId}">${user.staffName}</option>
                        </c:forEach>
                    </c:if>
                </select>
            </div>
        </div>
        <div class="layui-col-xs1">
            <div class="grid-demo" style="margin-top: 80px;">
                <button id="rightbtn" class="layui-btn layui-btn-normal layui-btn-sm" title="加入部门"><i class="layui-icon"></i></button>
                <br/><br/>
                <button  id="leftbtn" class="layui-btn layui-btn-normal layui-btn-sm" title="从部门删除"><i class="layui-icon"></i></button>
            </div>
        </div>
        <div class="layui-col-xs4">
            <div class="grid-demo">
                <select id="rightop" multiple="multiple" size="12" style="width:200px; height:240px;">
                    <c:if test="${!empty notuserlist}">
                        <c:forEach items="${notuserlist}" var="notuser" varStatus="status">
                            <option value="${notuser.staffId}">${notuser.staffName}</option>
                        </c:forEach>
                    </c:if>
                </select>
            </div>
        </div>
    </div>
    <script language="javascript" type="text/javascript">
        $(document).ready(function (){
            //为添加按钮增加事件
            $("#leftbtn").click(function (){
                //获取选择的值
                $("#leftop option:selected").each(function (i){
                    //在右边添加所选值，并且添加之后在左边删除所选值
                    $("#rightop").append("<option value='"+this.value+"'>"+this.text+"</option>");
                }).remove();
            });
            //为删除按钮增加事件
            $("#rightbtn").click(function (){
                //获取所选择的值
                $("#rightop option:selected").each(function (i){
                    //在左边添加所选值，并且添加之后在右边删除所选值
                    $("#leftop").append("<option value='"+this.value+"'>"+this.text+"</option>");
                }).remove();
            });
        });
        function empedit(deptId){
            var staffIds = "";
            $("#leftop option").each(function (i){
                staffIds += $(this).val()+";";
            });
            if(staffIds!="" && staffIds.length>2){
                $.ajax({
                    type: "POST",
                    url: "/department/empedit.do",
                    data: {'deptId':deptId,'staffId':staffIds},
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    dataType: "json",
                    async: false,
                    success: function (data) {
                        oa.gotourl("/department/gotodepartment.do");
                    },
                    error: function (message) {
                    }
                });
            }
            return false;
        }
    </script>
</div>
</c:if>
</body>
</html>


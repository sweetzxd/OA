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
    <title>员工信息管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <script type="text/javascript">
        var element = null;

        function starscript() {
            layui.use(['table', 'form', 'layer', 'laydate', 'element', 'tree'], function () {
                var layer = layui.layer;
                var table = layui.table;
                var laydate = layui.laydate;
                var form = layui.form;
                element = layui.element;
                var error = '${error}';
                if (error != null && error != "") {
                    layer.msg(error);
                }
                var createTree = function () {
                    var node = "";
                    $.ajax({
                        type: "POST",
                        url: "/department/depttree.do",
                        contentType: "application/json; charset=utf-8",
                        dataType: "html",
                        async: false,
                        success: function (data) {
                            node = eval(data);
                        },
                        error: function (message) {
                            layer.msg("组织结构获取失败！");
                        }
                    });
                    return node;
                };
                layui.tree({
                    elem: '#depttree'
                    , target: '_blank'
                    , nodes: createTree()
                    , click: function (item) {
                        var id = item.id;
                        console.log(id)
                        table.reload("EmployeesTable", {
                            where: {
                                option: "department",
                                inputval: id,
                            }
                        });
                    }
                });
                table.init('EmployeesTable', {
                    url: '/employees/select.do',
                    method: 'POST',
                    id: 'EmployeesTable',
                    page: true,
                    limit: 14
                });
                table.on('toolbar(EmployeesTable)', function (obj) {
                    var checkStatus = table.checkStatus(obj.config.id);
                    var data = checkStatus.data;
                    var othis = $(this);
                    var method = othis.data('method');

                    switch (obj.event) {
                        case 'add':
                            oa.gotourl("/employees/addemployees.do");
                            break;
                        case 'del':
                            if (data.length === 0) {
                                layer.msg('请选择一行');
                            } else {
                                var staffIds = "";
                                for (var i = 0; i < data.length; i++) {
                                    staffIds += data[i]["staffId"] + ";";
                                }
                                console.log(staffIds)
                                if (staffIds != "") {
                                    $.ajax({
                                        type: "POST",
                                        url: "/employees/delete.do?staffId=" + staffIds,
                                        contentType: "application/json; charset=utf-8",
                                        dataType: "html",
                                        cache: false,
                                        success: function (data) {
                                            oa.gotourl("/employees/gotoemployees.do");
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
                table.on('tool(EmployeesTable)', function (obj) {
                    var data = obj.data
                    console.log(data)
                    switch (obj.event) {
                        case 'modi':
                            empModi(data.staffId);
                            break;
                        case 'del':
                            empDelete(data.staffId);
                            break;
                    }
                });
                form.render();
                element.render();
                lay('.date').each(function () {
                    laydate.render({
                        elem: this
                    });
                });

                form.on('submit(formsubmit)', function (data) {
                    var saveurl = "", type = '${type}';
                    if (type == 'add') {
                        saveurl = "/employees/insert.do";
                    } else {
                        saveurl = "/employees/update.do";
                    }
                    $.ajax({
                        type: "POST",
                        url: saveurl,
                        data: data.field,
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        dataType: "json",
                        async: false,
                        success: function (data) {
                            oa.gotourl("/employees/gotoemployees.do");
                        },
                        error: function (message) {
                        }
                    });
                    return false;
                });
            });

            function empModi(value) {
                oa.gotourl("/employees/modiemployees.do?staffId=" + value);
            }

            function empDelete(value) {
                $.ajax({
                    type: "POST",
                    url: "/employees/delete.do?staffId=" + value,
                    contentType: "application/json; charset=utf-8",
                    dataType: "html",
                    async: false,
                    success: function (data) {
                        layer.msg("删除成功");
                        setTimeout(function () {
                            history.go(0);
                        }, 1000);
                    },
                    error: function (message) {
                    }
                });
            }
        }

    </script>
<body onload="starscript()">
<c:if test="${type=='list'}">
    <div class="layui-side" style="margin-top: 30px">
        <div class="layui-side-scroll">
            <ul id="depttree"></ul>
        </div>
    </div>
    <div class="layui-body" id="rigthPage">
        <div style="padding: 15px;height:95%">
            <table class="layui-hide" lay-filter="EmployeesTable"
                   lay-data="{id:'EmployeesTable',height: 'full-100',cellMinWidth: 100,toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
                <thead>
                <tr>
                    <th lay-data="{type:'checkbox', fixed: 'left',field:'staffId'}">选择</th>
                    <th lay-data="{field:'staffName', sort: true}">员工姓名</th>
                    <th lay-data="{field:'phone', sort: true}">联系电话</th>
                    <th lay-data="{field:'mailbox'}">邮箱地址</th>
                    <th lay-data="{field:'department',templet:function(d){return oa.decipher('dept',d.department);}}">
                        部门
                    </th>
                    <th lay-data="{field:'hiredate'}">入职日期</th>
                    <th lay-data="{field:'contractsExpire'}">合同到期</th>
                        <%--<th lay-data="{field:'recordName', sort: true, templet:function(d){return oa.decipher('user',d.recordName);}}">
                            定制人
                        </th>
                        <th lay-data="{field:'recordTime', templet:function(d){return oa.decipher('datetime',d.recordTime);}}">
                            定制时间
                        </th>--%>
                    <th lay-data="{field:'modifyName', sort: true, templet:function(d){return oa.decipher('user',d.modifyName);}}">
                        修改人
                    </th>
                    <th lay-data="{field:'modifyTime', templet:function(d){return oa.decipher('datetime',d.modifyTime);}}">
                        修改时间
                    </th>
                    <th lay-data="{fixed: 'right', width:150, align:'center', toolbar: '#listbar'}">管理</th>
                </tr>
                </thead>

            </table>
            <script type="text/html" id="listbar">
                <div class="layui-btn-container">
                    <a class="layui-btn layui-btn-xs" lay-event="modi">修改</a>
                    <a class="layui-btn layui-btn-xs" lay-event="del">删除</a>
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
                                <option value="staffName">员工姓名</option>
                                <option value="staffId">员工编号</option>
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
        </div>
    </div>
</c:if>
<c:if test="${type!='list'}">

    <%--<div class="layui-row">
        <div class="layui-col-xs12">
            <span class="layui-breadcrumb" lay-separator="|">
                ${contextmenu}
            </span>
        </div>
    </div>--%>
    <div style="overflow: scroll;width:100%;height: 100%;">
        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
            <legend>员工信息</legend>
        </fieldset>
        <form class="layui-form layui-form-pane" id="empform">
            <div class="layui-container">
                <div class="layui-row">
                    <div class="layui-col-xs12">
                        <div class="layui-form-item" hidden>
                            <label class="layui-form-label">员工编号</label>
                            <div class="layui-input-block">
                                <input type="text" class="layui-input" name="staffId" value="${emp.staffId}" readonly
                                       unselectable="on">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">员工姓名</label>
                            <div class="layui-input-block">
                                <input type="text" class="layui-input" name="staffName" value="${emp.staffName}"
                                       required>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">员工性别</label>
                            <div class="layui-input-block">
                                <input type="radio" name="sex" value="男" title="男"
                                       <c:if test="${emp.sex=='男'}">checked</c:if>>
                                <input type="radio" name="sex" value="女" title="女"
                                       <c:if test="${emp.sex=='女'}">checked</c:if>>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">出生日期</label>
                            <div class="layui-input-block">
                                <input type="text" name="dateOfBirth" id="dateOfBirth" autocomplete="off"
                                       class="layui-input date" value="${emp.dateOfBirth}" required>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">员工年龄</label>
                            <div class="layui-input-block">
                                <input type="text" name="age" autocomplete="off" placeholder="员工年龄" class="layui-input"
                                       value="${emp.age}">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">民族</label>
                            <div class="layui-input-block">
                                <input type="text" name="nations" autocomplete="off" placeholder="民族"
                                       class="layui-input"
                                       value="${emp.nations}">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">婚否</label>
                            <div class="layui-input-block">
                                <input type="radio" name="married" value="已婚" title="已婚"
                                       <c:if test="${emp.married=='已婚'}">checked</c:if>>
                                <input type="radio" name="married" value="未婚" title="未婚"
                                       <c:if test="${emp.married=='未婚'}">checked</c:if>>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">籍贯</label>
                            <div class="layui-input-block">
                                <input type="text" name="nativePlace" autocomplete="off" placeholder="籍贯"
                                       class="layui-input" value="${emp.nativePlace}">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">住址</label>
                            <div class="layui-input-block">
                                <input type="text" name="address" autocomplete="off" placeholder="住址"
                                       class="layui-input"
                                       value="${emp.address}">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">联系电话</label>
                                <div class="layui-input-inline">
                                    <input type="tel" name="phone" autocomplete="off" class="layui-input"
                                           value="${emp.phone}">
                                </div>
                            </div>
                            <div class="layui-inline">
                                <label class="layui-form-label">邮箱</label>
                                <div class="layui-input-inline">
                                    <input type="text" name="mailbox" autocomplete="off" class="layui-input"
                                           value="${emp.mailbox}">
                                </div>
                            </div>
                        </div>
                        <div class="layui-form-item layui-form-text">
                            <label class="layui-form-label">备注</label>
                            <div class="layui-input-block">
                                <textarea name="remark" placeholder="请输入内容"
                                          class="layui-textarea">${emp.remark}</textarea>
                            </div>
                        </div>

                        <div class="layui-form-item component" data-method="offset">
                            <label class="layui-form-label title" id="department_Title" data-type="user">所属部门</label>
                            <div class="layui-input-block">
                                <div tabindex="0" id="department" name="department" class="layui-input select-input"
                                     onclick="selectempdept(this,'dept')">
                                    <div class="oa-fa oa-fa-lg oa-fa-input">
                                        <i class="iconfont iconfont-form icon-xuanbumen1 oa-input-select"></i>
                                        <i class="iconfont icon-tianjia1 oa-input-select-2x"></i>
                                    </div>
                                </div>
                                <input type="hidden" id="department_Value" name="department" autocomplete="off"
                                       value="${emp.department}" class="layui-input" maxlength="50">
                                <input type="hidden" id="departmentName_Value" name="departmentName" autocomplete="off"
                                       class="layui-input"
                                       value="<s:dic type="dept" value="${emp.department}"></s:dic>">
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label class="layui-form-label">工作岗位</label>
                            <div class="layui-input-block">
                                <input type="text" name="post" autocomplete="off" class="layui-input"
                                       value="${emp.post}">
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label class="layui-form-label">入职日期</label>
                            <div class="layui-input-block">
                                <input type="text" name="hiredate" id="hiredate" autocomplete="off"
                                       class="layui-input date"
                                       value="${emp.hiredate}">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">合同到期</label>
                            <div class="layui-input-block">
                                <input type="text" name="contractsExpire" id="contractsExpire" autocomplete="off"
                                       class="layui-input date" value="${emp.contractsExpire}">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">离职日期</label>
                            <div class="layui-input-block">
                                <input type="text" name="leaveDate" id="leaveDate" autocomplete="off"
                                       class="layui-input date" value="${emp.leaveDate}">
                            </div>
                        </div>
                        <input type="hidden" name="userName" id="userName" value="${emp.userName}">
                        <div class="layui-form-item">
                            <div class="layui-input-block">
                                <button class="layui-btn" lay-submit lay-filter="formsubmit">立即提交</button>
                                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                                <a href="javascript:history.go(-1);" class="layui-btn layui-btn-primary">返回</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <script>
        //打开部门选择页面
        function selectempdept(obj, val) {
            var id = $(obj).attr("id");
            layer.closeAll();
            layer.open({
                type: 2 //此处以iframe举例
                , title: '选择部门'
                , area: ['650px', '400px']
                , shade: 0
                , maxmin: true
                , content: "/userpage/gotodeptselect.do?type=" + val + "&inputId=" + id
                , zIndex: layer.zIndex //重点1
                , success: function (layero) {
                    layer.setTop(layero); //重点2
                }

            });
        }

        //弹出选择回写(单)
        function updateValInput(intpuId, id, name) {
            var span = "<span value='" + id + "'><label>" + name + "</label><i onClick='event.cancelBubble = true;removeval(this)' class='iconfont icon-shanchu1'></i></span>";
            $("#" + intpuId).html(span);
            $("input[id='" + intpuId + "_Value']").val(id);
            $("input[id='departmentName_Value']").val(name);
            layer.closeAll();
        }
    </script>
</c:if>
</body>
</html>

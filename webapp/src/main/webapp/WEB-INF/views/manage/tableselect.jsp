<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/09/13
  Time: 下午 2:37
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
    <jsp:include page="/common/js.jsp"></jsp:include>
</head>
<body>
<c:if test="${type!='form' and type!='forms'}">
    <%--<div class="layui-btn-group" style="margin-left: 10px;padding-top: 5px;">
        <button class="layui-btn layui-btn-xs " data-type="select">确认选择</button>
    </div>--%>
    <c:if test="${type!='workflow' and type!='formcm'}">
    <div class="layui-side" style="margin-top: 30px">
        <div class="layui-side-scroll">
            <ul id="formtree"></ul>
        </div>
    </div>
    </c:if>
    <div class="<c:if test="${type!='workflow' and type!='formcm'}">layui-body</c:if>" id="rigthPage">
        <div style="height:90%;margin-right: 10px;">
            <table class="layui-hide" lay-filter="tableselect"
                   lay-data="{id:'tableselect',height: '480',cellMinWidth: 100}">
                <thead>
                <tr>
                        ${thead}
                </tr>
                </thead>
            </table>
        </div>
        <div class="layui-btn-group" style="margin-bottom: 6px;margin-right: 10px; height: 40px;">
            <button class="layui-btn layui-btn-xs " data-type="select" style="height: 40px;width: 60px;font-size: 16px;">确认</button>
        </div>
    </div>
    <script>
        var dag = window.parent;
        layui.use(['layer', 'table', 'tree'], function () {
            var layer = layui.layer;
            var table = layui.table;
            var type = "${type}";
            table.init('tableselect', {
                url: '${url}',
                method: 'POST',
                id: 'tableselect',
                page: true,
                limit: 10,
            });
            if(type!="workflow" && type!="formcm" ) {
                var createTree = function () {
                    var node = "";
                    $.ajax({
                        type: "POST",
                        url: "/department/depttree.do?spread=false",
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
                    elem: '#formtree'
                    , target: '_blank'
                    , nodes: createTree()
                    , click: function (item) {
                        var id = item.id;
                        table.reload('tableselect', {
                            where: {
                                option: "deptId",
                                inputval: id,
                            }
                            ,page: {
                                curr: 1
                            }
                        });
                    }
                });
            }
            $('.layui-btn-group .layui-btn').on('click', function () {
                var checkStatus = table.checkStatus('tableselect');
                var data = checkStatus.data;
                var inputId = "${inputId}";
                if (data.length === 0) {
                    layer.msg('请选择需要引入的条目');
                } else {
                    if (inputId != null && inputId != "") {
                        if (type == "user" || type == "dept") {
                            dag.updateValInput(inputId, data[0]["${id}"], data[0]["${name}"]);
                        } else if (type == "users" || type == "depts") {
                            dag.updateValsInput(inputId, data, '${id}', '${name}');
                        }
                    } else {
                        dag.updateinput(data[0]["${id}"], data[0]["${name}"]);
                    }
                }
            });
            if (type == 'user' || type == 'dept') {
                table.on('rowDouble(tableselect)', function (obj) {
                    console.log(obj.data.userName)
                    var data = obj.data;
                    var inputId = "${inputId}";
                    if (data.length === 0) {
                        layer.msg('请选择需要引入的条目');
                    } else {
                        dag.updateValInput(inputId, data["${id}"], data["${name}"]);
                    }
                });
            }
        });

        //表格分页复选框
        layui.define(['jquery', 'table'], function (exports) {
            var $ = layui.jquery
                , table = layui.table;

            //记录选中表格记录编号
            var checkedList = {};

            var tableCheckBoxUtil = {
               /* /!*初始化分页设置*!/*/
                init: function (settings) {
                    var param = {
                        //表格id
                        gridId: 'tableselect'
                        //表格lay-filter值
                        , filterId: 'tableselect'
                        //表格主键字段名
                        , fieldName: 'userName'
                    };
                    $.extend(param, settings);

                    //设置当前保存数据参数
                    if (checkedList[param.gridId] == null) {
                        checkedList[param.gridId] = [];
                    }

                    //监听选中行
                    table.on('checkbox(' + param.filterId + ')', function (obj) {
                        var type = obj.type;
                        var checked = obj.checked;
                        console.log(table);

                        //当前页数据
                        var currentPageData = table.cache[param.gridId];
                        //当前选中数据
                        var checkRowData = [];
                        //当前取消选中的数据
                        var cacelCheckedRowData = [];

                        //debugger;
                        //选中
                        if (checked) {
                            checkRowData = table.checkStatus(param.gridId).data;
                        }
                        //取消选中
                        else {
                            if (type == 'all') {
                                cacelCheckedRowData = currentPageData;
                            }
                            else {
                                cacelCheckedRowData.push(obj.data);
                            }
                            //console.log(cacelCheckedRowData);
                        }
                        //debugger;
                        //清除数据
                        $.each(cacelCheckedRowData, function (index, item) {
                            var itemValue = item[param.fieldName];

                            checkedList[param.gridId] = checkedList[param.gridId].filter(function (fItem, fIndex) {
                                return fItem != itemValue;
                            })
                        });

                        //添加选中数据
                        $.each(checkRowData, function (index, item) {
                            var itemValue = item[param.fieldName];
                            if (checkedList[param.gridId].indexOf(itemValue) < 0) {
                                checkedList[param.gridId].push(itemValue);
                            }
                        });

                        console.log(checkedList);
                    });
                }
                //设置页面默认选中（在表格加载完成之后调用）
                , checkedDefault: function (settings) {
                    var param = {
                        //表格id
                        gridId: 'tableselect'
                        //表格主键字段名
                        , fieldName: 'userName'
                    };

                    $.extend(param, settings);

                    //当前页数据
                    var currentPageData = table.cache[param.gridId];
                    if (checkedList[param.gridId] != null && checkedList[param.gridId].length > 0) {
                        $.each(currentPageData, function (index, item) {
                            var itemValue = item[param.fieldName];

                            if (checkedList[param.gridId].indexOf(itemValue) >= 0) {
                                //设置选中状态
                                item.LAY_CHECKED = true;

                                var rowIndex = item['LAY_TABLE_INDEX'];
                                updateRowCheckStatus(param.gridId, 'tr[data-index=' + rowIndex + '] input[type="checkbox"]');
                            }
                        });
                    }
                    //判断当前页是否全选
                    var currentPageCheckedAll = table.checkStatus(param.gridId).isAll;
                    if (currentPageCheckedAll) {
                        updateRowCheckStatus(param.gridId, 'thead tr input[type="checkbox"]');
                    }
                    //console.log(table.cache[param.gridId]);
                }
                //获取当前获取的所有集合值
                , getValue: function (settings) {
                    var param = {
                        //表格id
                        gridId: 'tableselect'
                    };
                    $.extend(param, settings);

                    return checkedList[param.gridId];
                }
                //设置选中的id（一般在编辑时候调用初始化选中值）
                , setIds: function (settings) {
                    var param = {
                        gridId: 'tableselect'
                        //数据集合
                        , ids: []
                    };
                    $.extend(param, settings);

                    checkedList[param.gridId] = [];
                    $.each(param.ids, function (index, item) {
                        checkedList[param.gridId].push(parseInt(item));
                    });
                }
            };

            var updateRowCheckStatus = function (gridId, ele) {
                var layTableView = $('.layui-table-view');
                //一个页面多个表格，这里防止更新表格错误
                $.each(layTableView, function (index, item) {
                    if ($(item).attr('lay-id') == gridId) {
                        $(item).find(ele).prop('checked', true);
                        $(item).find(ele).next().addClass('layui-form-checked');
                    }
                });
            }
            //输出
            exports('tableCheckBoxUtil', tableCheckBoxUtil);
        });
    </script>
</c:if>
<c:if test="${type=='form' or type=='forms'}">
    <div class="layui-btn-group">
        <button class="layui-btn layui-btn-xs" data-type="select">选择</button>
    </div>
    <table class="layui-hide" lay-filter="tableselect" lay-data="{id:'tableselect',height: 'full-40',cellMinWidth: 100}">
        <thead>
        <tr>
            ${thead}
        </tr>
        </thead>
    </table>
    <script>
        var dag = window.parent;
        layui.use(['layer', 'table', 'tree'], function () {
            var layer = layui.layer;
            var table = layui.table;
            var type = "${type}";
            table.init('tableselect', {
                url: '${url}',
                method: 'POST',
                id: 'tableselect',
                page: true,
                limit: 10,
            });

            $('.layui-btn-group .layui-btn').on('click', function () {
                var checkStatus = table.checkStatus('tableselect');
                var data = checkStatus.data;
                var inputId = "${inputId}";
                if (data.length === 0) {
                    layer.msg('请选择需要引入的条目');
                } else {
                    if (type == "form") {
                        console.log(data[0]["recorderNO"])
                        dag.updateValInput(inputId, data[0]["${id}"], data[0]["${name}"]);
                    } else if (type == "forms") {
                        console.log(data)
                        dag.updateValsInput(inputId, data, '${id}', '${name}');
                    }
                }
            });
            if (type == 'form') {
                table.on('rowDouble(tableselect)', function (obj) {
                    console.log(obj.data.userName)
                    var data = obj.data;
                    var inputId = "${inputId}";
                    if (data.length === 0) {
                        layer.msg('请选择需要引入的条目');
                    } else {
                        dag.updateValInput(inputId, data["${id}"], data["${name}"]);
                    }
                });
            }
        });
    </script>
</c:if>
</body>
</html>

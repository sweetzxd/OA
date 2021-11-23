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
    <title>流程运行数据查看</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <script type="text/javascript">
        var tabless;
        function starscript() {
            var nodes = ${workflow};
            layui.use(['table', 'form', 'layer', 'laydate', 'element', 'tree'], function () {
                var layer = layui.layer;
                var form = layui.form;
                var table = layui.table;
                var error = '${error}';
                var wkflwId = '${wkflwId}';
                form.render('select');
                if (error != null && error != "") {
                    layer.msg(error);
                }
                if (wkflwId == null || wkflwId == '') {
                    var createTree = function () {
                        return nodes;
                    };
                    layui.tree({
                        elem: '#workflowtree'
                        , target: '_blank'
                        , nodes: createTree()
                        , click: function (item) {
                            console.log(item);
                            var id = item.id;
                            if (id != null && id != '') {
                                table.reload("proctable", {
                                    where: {
                                        option: "wkflwId",
                                        inputval: id,
                                    }
                                });
                                console.log("不是空");
                            } else {
                                table.reload("proctable", {});
                            }
                        }
                    });console.log("是空");
                }
                tabless = table.init('proctable', {
                    url: '/workflow/selectproclist.do',
                    method: 'POST',
                    id: 'proctable',
                    page: true,
                    limit: 10,
                    where: {
                        option: "wkflwId",
                        inputval: wkflwId,
                        type: '${type}',
                        starnode: '${starnode}',
                    }
                });
                table.on('tool(proctable)', function (obj) {
                    var $obj = window;
                    if (wkflwId != null && wkflwId != '') {
                        $obj = parent;
                    }
                    $obj.layer.open({
                        type: 2
                        , title: '查看流程运行数据'
                        , ffset: 'auto'
                        , area: ['800px', '600px']
                        , shade: 0
                        , maxmin: true
                        , content: '/workflow/logpage.do?procId=' + obj.data.procId + "&wkflowId=" + obj.data.wkflwID
                        , zIndex: layer.zIndex //重点1
                        , success: function (layero) {
                            console.log(layero)
                            layer.setTop(layero); //重点2
                        }
                    });

                });

            });
        }
        function selectss() {
            tabless.reload("proctable", {
                where: {
                    option: "originator",
                    inputval: $("#inputval").val(),
                }
            });
        }
    </script>
<body onload="starscript()">
<c:if test="${wkflwId==null or wkflwId==''}">
<div class="layui-side" style="margin-top: 30px">
    <div class="layui-side-scroll">
        <ul id="workflowtree"></ul>
    </div>
</div>

<div class="layui-body" id="rigthPage">
    </c:if>
    <c:if test="${wkflwId!=null and wkflwId!=''}">
    <div class="layui-form">
        </c:if>
        <div style="padding: 15px;height:95%">
            <table class="layui-hide" lay-filter="proctable"
                   lay-data="{id:'proctable',height: 'full-100',cellMinWidth: 100}">
                <thead>
                <tr>
                    <th lay-data="{field:'procId', fixed: 'left'}">流程运行号</th>
                    <th lay-data="{field:'wkflwID', sort: true,templet:function(d){return oa.decipher('flowname',d.wkflwID);}}">
                        流程定义号
                    </th>
                    <th lay-data="{field:'originator',templet:function(d){return oa.decipher('user',d.originator);}}">
                        发起人
                    </th>
                    <th lay-data="{field:'curNodeID',templet:function(d){return oa.decipher('node',d.curNodeID);}}">
                        当前节点
                    </th>
                    <th lay-data="{field:'nodespeople',templet:function(d){return oa.decipher('users',d.nodespeople);}}">
                        节点执行人
                    </th>
                    <th lay-data="{field:'flowpeoples',templet:function(d){return oa.decipher('users',d.flowpeoples);}}">
                        流程参与人
                    </th>
                    <th lay-data="{field:'wkfStatus',sort: true, templet:'#wkfStatus'}">流程状态</th>
                    <th lay-data="{fixed: 'right', width:150, align:'center', toolbar: '#listbar'}">管理</th>
                </tr>
                </thead>

            </table>
            <script type="text/html" id="listbar">
                <div class="layui-btn-container">
                    <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="check">查看流程记录</a>
                </div>
            </script>
            <script type="text/html" id="wkfStatus">
                {{#  if(d.wkfStatus == 1){ }}
                进行中
                {{#  } else if(d.wkfStatus = 2) { }}
                完成
                {{#  } }}
            </script>
            <div class="layui-form">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">检索：</label>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-input-inline">
                            <select id="option" name="option">
                                <option value="originator">发起人</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-input-inline">
                            <input class="layui-input" id="inputval" name="inputval" autocomplete="off">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <a class="layui-btn layui-btn-xs" lay-submit lay-filter="reload" onclick="selectss()">搜索</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

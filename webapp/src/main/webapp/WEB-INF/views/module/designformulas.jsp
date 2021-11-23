<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/12/17
  Time: 下午 3:53
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
    <title>计算公式管理页面</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
</head>
<c:if test="${type=='list'}">
<body class="layui-layout-body">
    <div class="layui-layout">
        <div class="layui-side">
            <div class="layui-side-scroll">
                <ul id="accesstree"></ul>
            </div>
        </div>
        <div class="layui-body" id="rigthPage">
            <div style="padding: 15px;height:95%">
                <table class="layui-hide" lay-filter="designformulas"
                       lay-data="{height: 'full-100',cellMinWidth: 100,toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
                    <thead>
                    <tr>
                        <th lay-data="{align: 'center', toolbar: '#tool',fixed: 'left'}">操作</th>
                        <th lay-data="{field:'formulasName',align: 'center'}">公式名称</th>
                        <th lay-data="{field:'formulasType',align: 'center'}">所属分类</th>
                        <th lay-data="{field:'formulasValue'}">计算公式</th>
                        <th lay-data="{field:'formulasResult',align: 'center'}">公式计算结果</th>
                    </tr>
                    </thead>
                </table>
                <script type="text/html" id="toolbar">
                    <div class="layui-btn-container">
                        <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="add">添加</a>
                    </div>
                </script>
                <script type="text/html" id="tool">
                    <a class="layui-btn layui-btn-xs" lay-event="modi">修改</a>
                    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
                </script>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${type!='list'}">
<body>
<div class="layui-container" style="padding: 15px;">
    <form class="layui-form layui-form-pane">
        <input type="hidden" name="recorderNO" value="${recorderNO}">
        <input type="hidden" name="tableName" value="${tableName}">
        <div class="layui-form-item">
            <label class="layui-form-label">公式名称</label>
            <div class="layui-input-block">
                <input type="text" class="layui-input valid" aria-invalid="false" id="formulasName" name="formulasName" value="${gsMap['formulasName']}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">所属分类</label>
            <div class="layui-input-block">
                <input type="text" class="layui-input valid" aria-invalid="false" id="formulasType" name="formulasType" value="${gsMap['formulasType']}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">公式所需字段</label>
            <div class="layui-input-block">
                <div class="layui-col-xs6">
                    <select id="table" lay-filter="table">
                        <option value="">请选择字段</option>
                        <c:if test="${!empty fieldMap}">
                            <c:forEach items="${fieldMap}" var="m">
                                <option value="${m.key}">${m.value}</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </div>
                <div class="layui-col-xs2">
                    <div style="padding-top: 8px;padding-left: 10px;"><a href="#" onclick="insertFlowLabFld()" class="layui-btn layui-btn-xs">插入</a></div>
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-col-xs12">
                <label class="layui-form-label">计算公式</label>
                <textarea class="layui-textarea" name="formulasValue" lay-verify="flowLabFld" value="${gsMap['formulasValue']}" id="flowLabFld">${gsMap['formulasValue']}</textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">公式计算结果</label>
            <div class="layui-input-block">
                <div class="layui-col-xs6">
                    <select lay-filter="table" name="formulasResult" id="formulasResult">
                        <option value="">请选择字段</option>
                        <c:if test="${!empty fieldMap}">
                            <c:forEach items="${fieldMap}" var="m">
                                <option value="${m.key}" <c:if test="${m.key==gsMap['formulasResult']}">selected</c:if>>${m.value}</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit="" lay-filter="formsubmit">保存</button>
            </div>
        </div>
    </form>
</div>
</c:if>
<jsp:include page="/common/js.jsp"></jsp:include>
<script type="text/javascript">
    var pageid = null, pagetitle = null, table = null, layer = null,menuNum=0;
    layui.use(['form', 'layer', 'table', 'laydate', 'tree'], function () {
        var form = layui.form;
        layer = layui.layer;
        table = layui.table;
        var createTree = function () {
            var node = "";
            $.ajax({
                type: "POST",
                url: "/designformulas/getAccessTree.do",
                contentType: "application/json; charset=utf-8",
                dataType: "html",
                async: false,
                success: function (data) {
                    node = eval(data);
                },
                error: function (message) {
                    layer.msg("菜单树获取失败！");
                }
            });
            return node;
        };
        if(${type=="list"}){
            table.init('designformulas', {
                url: '/designformulas/selectDesignformulas.do',
                method: 'POST',
                id: 'designformulas',
                page: true
            });
            layui.tree({
                elem: '#accesstree'
                , target: '_blank'
                , nodes: createTree()
                , click: function (item) {
                    layer.closeAll();
                    menuNum = item.menuNum;
                    var itmeId = item.id;
                    table.init('designformulas', {
                        url: '/designformulas/selectDesignformulas.do',
                        method: 'POST',
                        id: 'designformulas',
                        page: true,
                        where: {
                            pageid: itmeId,
                            pagetitle: item.name
                        },
                        done: function (res, curr, count) {
                            if(menuNum==2){
                                pageid = res.pageid;
                                pagetitle = res.pagetitle;
                            }else{
                                pageid = null;
                            }
                        }
                    });

                }
            });
        }

        table.on('toolbar(designformulas)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            var othis = $(this);
            var method = othis.data('method');
            switch (obj.event) {
                case 'add':
                    if (pageid == null) {
                        layer.msg('请先选择需要添加计算公式的二级菜单');
                    } else {
                        var active = opedit(othis, layer);
                        active[method] ? active[method].call(this, othis) : '';
                    }
                    break;
            }
        });
        table.on('tool(designformulas)', function (obj) {
            var data = obj.data;
            var layEvent = obj.event;
            if (layEvent === 'del') {
                layer.confirm("你确定删除数据吗？", {icon: 3, title: '提示'}, function (index) {

                    $.ajax({
                        type: "POST",
                        url: "/designformulas/delDesignformulas.do",
                        data: "recorderNO="+data["recorderNO"],
                        dataType: "json",
                        cache:false,
                        success: function (data) {
                            if(data!="0"){
                                obj.del();
                                layer.close(index);
                            }
                        },
                        error: function (message) {
                            layer.msg("数据删除失败！");
                        }
                    });
                });
            } else if (layEvent === 'modi') {
                layer.closeAll();
                layer.open({
                    type: 2
                    , title: data["formulasName"]
                    , area: ['650px', '530px']
                    , shade: 0
                    , maxmin: true
                    , offset: 'auto'
                    , content: "/designformulas/gotoDesignformulas.do?type=add&pageid="+pageid+"&recorderNO="+data["recorderNO"]
                });
                return;
            }
        });

        form.render();
        form.on('submit(formsubmit)', function(data){
            /*if($("#flowLabFld").val()!=null&&$("#flowLabFld").val().replace(/^\s+|\s+$/g,"")!=""){
                var str  = $("#flowLabFld").val();
                var multObj = str.match(/\([^()]+\)/g);//匹配括号
                console.log(multObj);
                //不断计算最底层括号的数据
                while (null != multObj) {
                    var content = multObj[0] + "";
                    var result = this.simpleEval(content.substr(1, content.length - 2));
                    str = str.replace(multObj[0], result);
                    multObj = str.match(/\([^()]+\)/g);//匹配括号
                    console.log(multObj)
                }
                return false;
            }*/
            if($("#formulasName").val()==null||$("#formulasName").val().replace(/^\s+|\s+$/g,"")==""){
                layer.msg("请填写公式名称");
                return false;
            }else if($("#formulasType").val()==null||$("#formulasType").val().replace(/^\s+|\s+$/g,"")==""){
                layer.msg("请填写所属分类");
                return false;
            }else if($("#flowLabFld").val()==null||$("#flowLabFld").val().replace(/^\s+|\s+$/g,"")==""){
                layer.msg("请填写计算公式");
                return false;
            }else if($("#formulasResult").find("option:selected").val()==""){
                layer.msg("请选择公式计算结果字段");
                return false;
            }
            if($("#flowLabFld").val()!=null&&$("#flowLabFld").val().replace(/^\s+|\s+$/g,"")!=""){
                var left  = $("#flowLabFld").val().split("(").length;
                var right = $("#flowLabFld").val().split(")").length;
                if(left != right){
                    layer.msg("计算公式“( )”没有完全对应");
                    return false;
                }
            }
            var jsons = JSON.stringify(data.field);
            $.ajax({
                type: "POST",
                url: "/designformulas/saveDesignformulas.do",
                data:{'data':jsons},
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                dataType: "json",
                async: false,
                success: function (data) {
                    parent.table.reload("designformulas",{
                        where: {
                            pageid: parent.pageid,
                        }
                    });
                    parent.layer.closeAll();
                },
                error: function (message) {
                    layer.msg("系统错误!");
                }
            });
        });
    });

    function opedit(othis, layer) {
        var active = {
            titlebtn: function (othis) {
                layer.closeAll();
                layer.open({
                    type: 2
                    , title: pagetitle+"--计算公式"
                    , area: ['650px', '530px']
                    , shade: 0
                    , maxmin: true
                    , offset: 'auto'
                    , content: "/designformulas/gotoDesignformulas.do?type=add&pageid="+pageid
                });
            }
        }
        return active;
    }


    function insertFlowLabFld(){
        var table = $("#table").find("option:selected");
        var value = table.val();
        var name1 = $("#flowLabFld").val();
        $("#flowLabFld").val(name1+value);
    }

</script>
</body>
</html>


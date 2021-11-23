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
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <script src="/resources/js/system/manager.js" charset="utf-8"></script>
    <script src="/resources/js/system/form.js" charset="utf-8"></script>
    <script type="text/javascript">
        function starscript(pageid, tableid, formid) {
            var type = '${type}';
            if(type == 'list'){
                tableLoading(pageid, tableid, formid);
            }
            formLoading(pageid, tableid, formid);
        }

        function tableLoading(pageid, tableid, formid) {
            var table = layui.table, layer = layui.layer;
            table.init(tableid, {
                url: '/userpage/gettablevalue.do?formid=' + formid+"&linkId=${linkId}",
                method: 'POST',
                id: tableid,
                page: true,
                limit: 10
            });
            table.on('toolbar(' + tableid + ')', function (obj) {
                var checkStatus = table.checkStatus(obj.config.id);
                var data = checkStatus.data;
                switch (obj.event) {
                    case 'add':
                        oa.gotourl("/userpage/pageform/newformdata.do?type=add&pageid=" + pageid + "&formid=" + formid+"&linkId=${linkId}");
                        break;
                    case 'update':
                        if (data.length === 0) {
                            layer.msg('请选择一行');
                        } else if (data.length > 1) {
                            layer.msg('只能同时编辑一个');
                        } else {
                            var recno = data[0][tableid + "_recorderNO"];
                            oa.gotourl("/userpage/pageform/" + recno + ".do?type=modi&pageid=" + pageid + "&formid=" + formid+"&linkId=${linkId}");
                        }
                        break;
                    case 'delete':
                        if (data.length === 0) {
                            layer.msg('请选择一行');
                        } else {
                            var recno = "";
                            for (var i = 0; i < data.length; i++) {
                                recno += data[i][tableid + "_recorderNO"] + ";";
                            }
                            if (recno != "") {
                                $.ajax({
                                    type: "POST",
                                    url: "/userpage/pagealldel.do?tableid=" + tableid + "&recno=" + recno,
                                    contentType: "application/json; charset=utf-8",
                                    dataType: "html",
                                    success: function (data) {
                                        formback(pageid);
                                    },
                                    error: function (message) {
                                        console.log(message)
                                        layer.msg("提交数据失败！");
                                    }
                                });
                            }
                        }
                        break;

                    default:

                }
            });
        }

        function tablesearch(field, item, value, tableid) {
            var f = field.find("option:selected").val();
            var i = item.find("option:selected").val();
            var v = value.val();
            console.log(f + "-" + i + "-" + v)
            var table = layui.table;
            table.reload(tableid, {
                where: {
                    field: f,
                    item: i,
                    value: v
                }
                , page: {
                    curr: 1
                }
            });
        }
    </script>
</head>
<body onload="starscript('${pageid}','${tableid}','${formid}')">
<c:if test="${type=='list'}">
    <div class="table-search" id="tablesearch">
        <div style="float: left">
            <label>检索：</label>
        </div>
        <div style="float: left">
            <select id="field" name="field">
                <s:page type="list_select" value="${tableid}"></s:page>
            </select>
        </div>
        <div style="float: left">
            <select id="term" name="term">
                <option value="等于">等于</option>
                <option value="包含">包含</option>
                <option value="不等于">不等于</option>
                <option value="大于">大于</option>
                <option value="大于等于">大于等于</option>
                <option value="小于">小于</option>
                <option value="小于等于">小于等于</option>
            </select>
        </div>
        <div style="float: left">
            <input class="" id="inputval" name="inputval" autocomplete="off">
        </div>
        <a onclick="tablesearch($('#field'),$('#term'),$('#inputval'),'${tableid}')">搜索</a>
    </div>
    <table lay-filter="${tableid}" id="${tableid}" class="layui-table"
           lay-data="{id:'${tableid}', height: 'full-60', cellMinWidth: 80, page: true, toolbar: '#toolbar', defaultToolbar: []}">
        <thead>
        <tr>
                ${listTitle}
        </tr>
        </thead>
    </table>
    <script type="text/html" id="tableNum">
        <a class="layui-btn layui-btn-sm" style="text-decoration:none;" href="/userpage/pageform/{{d.${tableid}_recorderNO}}.do?type=modi&pageid=${pageid}&formid=${formid}&linkId=${linkId}">{{d.${tableid}_num}}</a>
    </script>
    <script type="text/html" id="toolbar">
        <div class="layui-btn-container">
            <a class='layui-btn layui-btn-primary layui-btn-xs' lay-event='add' title='添加'>添加</a>
            <a class='layui-btn layui-btn-xs' lay-event='update' title='修改'>修改</a>
            <a class='layui-btn layui-btn-danger layui-btn-xs' lay-event='delete' title='删除'>删除</a>
            <a class='layui-btn layui-btn-xs' href='/userpage/viewpage/childtable.do?formid=${formid}&linkId=${linkId}' title="刷新">刷新</a>
        </div>
    </script>
</c:if>
<c:if test="${type=='modi' || type=='add'}">
    <c:if test="${contextmenu!=null and contextmenu!=''}">
        <div class="layui-row">
            <div class="layui-col-xs12">
                <span class="layui-breadcrumb" lay-separator="|">
                        ${contextmenu}
                </span>
            </div>
        </div>
    </c:if>
    <fieldset class="layui-elem-field">
        <legend>${title}</legend>
        <form class="layui-form layui-form-pane" id="${formid}" action="/userpage/formsave/${pageid}.do?type=${type}&formid=${formid}&linkId=${linkId}" method='POST'>
            <div class="layui-container">
                <c:if test="${!empty form}">
                    <c:forEach items="${form}" var="field" varStatus="status">
                        ${field}
                    </c:forEach>
                </c:if>
            </div>
            <<input type="hidden" name="${tableId}_linkRecorderNO" value="${linkId}">
        </form>
    </fieldset>
</c:if>
</body>
</html>
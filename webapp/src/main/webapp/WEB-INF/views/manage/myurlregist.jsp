<%--
  Created by IntelliJ IDEA.
  User: zxd
  Date: 2018/08/28
  Time: 下午 4:41
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
    <title>菜单树定义</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
</head>
<body>
<c:if test="${type=='list'}">
    <div>
        <a class="layui-btn layui-btn-primary" onclick="window.location.href='/myurl/myurlregist.do';">刷新</a>
        <a class="layui-btn layui-btn-primary" onclick="add(null);">新增主节点</a>
        <a class="layui-btn layui-btn-primary" onclick="openAll();">展开或折叠全部</a>
        <a class="layui-btn layui-btn-primary" onclick="resetmenu()">重生菜单树</a>
        <a class="layui-btn layui-btn-primary" onclick="resetmymenu('${sessionScope.loginer.id}')">重生个人菜单树</a>
    </div>
    <div style="height:92%">
        <table class="layui-hidden" id="treeTable" lay-filter="treeTable"></table>
    </div>
    <script type="text/html" id="titleSort">
        {{#  if(d.lay_level ==2){ }}
        <a data-method="treebtn" class="layui-btn layui-btn-primary layui-btn-xs" lay-event="up">上</a>
        <a data-method="treebtn" class="layui-btn layui-btn-primary layui-btn-xs" lay-event="down">下</a>
        {{#  } }}
    </script>
    <script type="text/html" id="titleTpl">
        {{#  if(d.lay_level ==1){ }}
        <a data-method="treebtn" class="layui-btn layui-btn-primary layui-btn-xs" lay-event="add">添加</a>
        {{#  } }}
        {{#  if(d.lay_level ==2 && d.formType!=2){ }}
        <a data-method="treebtn" class="layui-btn layui-btn-normal layui-btn-xs" lay-event="contmenu">添加关联菜单</a>
        {{#  } }}
        <a data-method="treebtn" class="layui-btn layui-btn-normal layui-btn-xs" lay-event="modi">修改</a>
        <a data-method="treebtn" class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
    </script>
</c:if>
<c:if test="${type=='add' || type=='modi'}">
    <div class="layui-container">
        <div class="layui-row">
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <form class="layui-form" id="myurl_form">
                    <table class='layui-table' lay-skin='line' lay-size='sm'>
                        <colgroup>
                            <col width='70'>
                            <col width='200'>
                        </colgroup>
                        <thead>
                        <tr>
                            <th colspan='2'>菜单维护<i class="layui-icon layui-icon-right"
                                                   style="font-size: 10px; color: #1E9FFF;"></i>
                                <c:if test="${type=='add'}">${myurl.pageTitle}</c:if>
                                <c:if test="${type=='modi'}">${parent.pageTitle}</c:if>
                                <i class="layui-icon layui-icon-right" style="font-size: 10px; color: #1E9FFF;"></i>
                                <c:if test="${type=='add'}"><c:if test="${myurl.pageId!='topmenu'}">新增菜单</c:if><c:if
                                        test="${myurl.pageId=='topmenu'}">新增节点</c:if></c:if>
                                <c:if test="${type=='modi'}">${myurl.pageTitle}</c:if>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td <c:if test="${type=='add'}">style='display:none'</c:if>>
                                <div style='width:90%;text-align:right;'>
                                    <label class='layui-form-text'>菜单主键:</label>
                                </div>
                            </td>
                            <td <c:if test="${type=='add'}">style='display:none'</c:if>>
                                <div class='layui-input-label'>
                                    <input type='text'
                                           <c:if test="${type=='modi'}">readonly="readonly"</c:if>
                                           id='pageId' name='pageId' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${myurl.pageId}</c:if>'
                                           class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <label class='layui-form-text'>父菜单主键:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <input type='text' id='parentId' name='parentId' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${myurl.parentId}</c:if><c:if test="${type=='add'}">${myurl.pageId}</c:if>'
                                           readonly unselectable="on" class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <label class='layui-form-text'>菜单名:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <input type='text' id='pageTitle' name='pageTitle' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${myurl.pageTitle}</c:if><c:if test="${type=='add'}">新建节点</c:if>'
                                           class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <label class='layui-form-text'>选择类型:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <select id="formType" name="formType" lay-filter="formType">
                                        <option value="1" <c:if test="${myurl.formType==1}">selected</c:if>>表单</option>
                                        <option value="2" <c:if test="${myurl.formType==2}">selected</c:if>>流程</option>
                                        <option value="3" <c:if test="${myurl.formType==3}">selected</c:if>>外部表单</option>
                                    </select>
                                </div>
                            </td>
                        </tr>
                        <c:if test="${myurl.pageId!='topmenu' && parent.pageId!='topmenu'}">
                            <tr>
                                <td>
                                    <div style='width:90%;text-align:right;'>
                                        <label class='layui-form-text'>关联主键:</label>
                                    </div>
                                </td>
                                <td>
                                    <div class='layui-input-label'>
                                        <input type='text' id='formId' name='formId' autocomplete='off'
                                               value='<c:if test="${type=='modi'}">${myurl.formId}</c:if>'
                                               readonly unselectable="on"
                                               class='layui-input'>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
        <c:if test="${myurl.pageId!='topmenu' && parent.pageId!='topmenu'}">
            <div class="layui-row">
                <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                    <div id="selectform">
                        <iframe id="formcn" name="formcn" src="/myurl/gotoformselect.do" style="overflow: visible;"
                                scrolling="no"
                                frameborder="no" width="100%" height="100%"></iframe>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</c:if>
<script type="text/javascript">
    var editObj = null, ptable = null, treeGrid = null, tableId = 'treeTable', layer = null;
    layui.config({
        base: '/resources/js/admin/'
    }).extend({
        treeGrid: 'treeGrid'
    }).use(['form','jquery', 'treeGrid', 'layer'], function () {
        var $ = layui.jquery;
        var form = layui.form;
        form.render();
        treeGrid = layui.treeGrid;//很重要
        layer = layui.layer;
        ptable = treeGrid.render({
            id: tableId
            , elem: '#' + tableId
            , idField: 'pageId'
            , url: '/myurl/treetable.do'
            , cellMinWidth: 200
            , treeId: 'pageId'//树形id字段名称
            , treeUpId: 'parentId'//树形父id字段名称
            , treeShowName: 'pageTitle'//以树形式显示的字段
            , isFilter: false
            , iconOpen: false//是否显示图标【默认显示】
            , isOpenDefault: false//节点默认是展开还是折叠【默认展开】
            , cols: [[
                {field: 'security',width: '5%', title: '排序', align: 'center'}
                , {width: '10%', align: 'center', templet: '#titleSort'}
                , {field: 'pageTitle', title: '菜单名'}
                , {field: 'pageId', width: '20%', title: '菜单主键'}
                , {field: 'parentId', width: '20%', title: '父菜单主键'}
                , {width: '20%', align: 'right', templet: '#titleTpl', fixed: 'right'}
            ]]
            , page: true
            , parseData: function (res) {//数据加载后回调
                return res;
            }
            /*, onClickRow: function (index, o) {
                var menuNum = o["menuNum"];
                var id = o["pageId"]
                if (parseInt(menuNum) == 1) {
                    openorclose(id);
                }
            }*/
        });

        treeGrid.on('tool(' + tableId + ')', function (obj) {
            if (obj.event === 'del') {//删除行
                del(obj);
            } else if (obj.event === "add") {//添加行
                add(obj);
            } else if (obj.event === "modi") {//添加行
                modi(obj);
            } else if (obj.event === "contmenu") {
                contmenu(obj);
            } else if(obj.event === "up"){
                menuup(obj,this);
            } else if(obj.event === "down"){
                menudowm(obj,this);
            }
        });

        form.on('select(formType)', function (data) {
            var value = data.value;
            document.formcn.location.href = "/myurl/gotoformselect.do?value="+value;
        });
    });

    function del(obj) {
        layer.confirm("你确定删除数据吗？如果存在下级节点则一并删除，此操作不能撤销！", {icon: 3, title: '提示'},
            function (index) {//确定回调
                var id = obj.data.pageId;
                if (id != null) {
                    obj.del();
                    $.ajax({
                        type: "POST",
                        url: "/myurl/delete.do?pageid=" + id,
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        cache: false,
                        dataType: "json",
                        success: function (data) {
                            window.location.href = "../myurl/myurlregist.do";
                        },
                        error: function () {
                            alert("请求失败");
                        }
                    });
                }
                layer.close(index);
            }, function (index) {//取消回调
                layer.close(index);
            }
        );
    }

    function add(pObj) {
        var pdata = pObj ? pObj.data : null;
        var pid = pdata ? pdata.pageId : 'topmenu';
        var othis = $(this);
        var active = opedit(othis, 'add', '', pid);
        active ? active.call(this, othis) : '';
        // treeGrid.addRow(tableId,pdata?pdata[treeGrid.config.indexName]+1:0,param);
    }

    function modi(pObj) {
        var pdata = pObj ? pObj.data : null;
        var pid = pdata ? pdata.parentId : 'topmenu';
        var id = pdata ? pdata.pageId : 'topmenu';
        var othis = $(this);
        var active = opedit(othis, 'modi', id, pid);
        active ? active.call(this, othis) : '';
    }
    function contmenu(pObj) {
        var pdata = pObj ? pObj.data : null;
        var pid = pdata ? pdata.parentId : 'topmenu';
        var id = pdata ? pdata.pageId : 'topmenu';
        var othis = $(this);
        var active = opedit(othis, 'contmenu', id, pid);
        active ? active.call(this, othis) : '';
    }

    function openorclose(value) {
        var map = treeGrid.getDataMap(tableId);
        var o = map[value];
        treeGrid.treeNodeOpen(o, !o[treeGrid.config.cols.isOpen]);
    }

    function openAll() {
        var treedata = treeGrid.getDataTreeList(tableId);
        treeGrid.treeOpenAll(tableId, !treedata[0][treeGrid.config.cols.isOpen])
    }

    function menuup(obj,o){
        var q = $(o).parents('tr');
        var tdArr = $(q).children();
        var security = tdArr.eq(0).text();
        var num = parseInt(security.substring(1,security.length));
        if(num-1>=1){
            $(q).insertBefore($(q).prev());
            var n = parseInt(security);
            tdArr.eq(0).text(n-1);
            $(q).next().children().eq(0).text(n);
            setSecurity(tdArr.eq(3).text(),n-1,$(q).next().children().eq(3).text(),n);
        }
        return false;
    }

    function menudowm(obj,o){
        var q = $(o).parents('tr');
        var data = obj.data;
        var pid = data.parentId;
        var treedata = treeGrid.getDataTreeList(tableId);
        var vod = "";
        for(var i=0,t=treedata.length;i<t;i++){
            if(treedata[i]["pageId"] == pid){
                vod = treedata[i];
                break;
            }
        }
        var tdArr = $(q).children();
        var security = tdArr.eq(0).text();
        var num = parseInt(security.substring(1,security.length));
        if(num+1 <= vod.children.length){
            $(q).insertAfter($(q).next());
            var n = parseInt(security);
            tdArr.eq(0).text(n+1);
            $(q).prev().children().eq(0).text(n);
            setSecurity(tdArr.eq(3).text(),n+1,$(q).prev().children().eq(3).text(),n);
        }
        return false;
    }

    function setSecurity(id,num,sid,snum){
        console.log({"id":id,"num":num,"sid":sid,"snum":snum})
        $.post("/myurl/setsecurity.do",{"id":id,"num":num,"sid":sid,"snum":snum},function(){ });
    }

    function opedit(othis, thistype, id, pid) {
        var url = "", title = "";
        if (thistype == "modi") {
            url = '../myurl/myurlmodi/' + id + '.do?pid=' + pid;
            title = '菜单修改';
        } else if (thistype == "contmenu") {
            url = '../myurl/contextmenu.do?id=' + id;
            title = '菜单修改';
        } else {
            url = '../myurl/myurladd.do?pid=' + pid;
            title = '新增菜单';
        }
        var active = function (othis) {
            var that = this;
            var $type = othis.data('type');
            //多窗口模式，层叠置顶
            layer.open({
                type: 2 //此处以iframe举例
                , id: 'toolbar' + $type
                , title: title
                , area: ['800px', '600px']
                , shade: 0
                , maxmin: true
                , offset: $type
                , content: url
                , btn: ['保存', '关闭']
                , yes: function (active) {
                    var saveurl = "";
                    if (thistype == "add") {
                        saveurl = "../myurl/insert.do";
                    } else if (thistype == "modi") {
                        saveurl = "../myurl/update.do";
                    } else if (thistype == "contmenu") {
                        saveurl = "../myurl/cmupdate.do?pageId="+id;
                    }
                    var datas = fun();
                    $.ajax({
                        type: "POST",
                        url: saveurl,
                        data: datas,
                        contentType: "application/x-www-form-urlencoded; charset=utf-8",
                        cache: false,
                        dataType: "json",
                        success: function (data) {
                            layer.closeAll();
                        },
                        error: function () {
                            alert("请求失败");
                        }
                    });
                }
                , btn2: function () {
                    layer.close();
                }
                , zIndex: layer.zIndex //重点1
                , success: function (layero) {
                    layer.setTop(layero); //重点2
                }
            });
        }
        return active;
    }
    function resetmenu(){
        $.ajax({
            type: "POST",
            url: "/myurl/resetmenu.do",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            cache: false,
            dataType: "json",
            success: function (data) {
                if(data==1){
                    layer.msg("菜单树重生成功");
                }else{
                    layer.msg("菜单树重生失败");
                }
                //window.location.href = "../myurl/myurlregist.do";
            },
            error: function () {
                layer.msg("请求失败");
            }
        });
    }

    function resetmymenu(value){
        $.ajax({
            type: "POST",
            url: "/myurl/resetmymenu.do?userName="+value,
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            cache: false,
            dataType: "json",
            success: function (data) {
                layer.msg(data);
                //window.location.href = "../myurl/myurlregist.do";
            },
            error: function () {
                layer.msg("请求失败");
            }
        });
    }

    var dag = window.parent;
    dag.fun = function () {
        return $('#myurl_form').serialize();
    }

    function selectform() {
        $('#selectform').attr("display", "black");
    }

    function updateinput(id, name) {
        $('#formId').val(id)
        $("#pageTitle").val(name)
    }

</script>
</body>
</html>

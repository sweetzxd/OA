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
    <title>表单定制</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script type="text/javascript" src="/resources/js/system/system.js"></script>
</head>
<body>
<c:if test="${type=='list'}">
    <div class="layui-side" style="margin-top: 30px">
        <div class="layui-side-scroll">
            <ul id="accesstree"></ul>
        </div>
    </div>
    <div class="layui-body" id="rigthPage">
        <div style="padding: 15px;height:95%">
            <table class="layui-hide" lay-filter="formcm"
                   lay-data="{height: 'full-100',cellMinWidth: 100,toolbar:'#toolbar',defaultToolbar: ['filter', 'exports']}">
                <thead>
                <tr>
                    <th lay-data="{type:'checkbox', fixed: 'left',field:'formcmName'}">选择</th>
                    <th lay-data="{field:'formcmName', sort: true}">页面主键</th>
                    <th lay-data="{field:'formcmTitle'}">页面标题</th>
                    <th lay-data="{field:'formTask', sort: true}">表单任务</th>
                    <th lay-data="{field:'listTask'}">列表任务</th>
                    <th lay-data="{field:'recordName', sort: true, templet:function(d){return oa.decipher('user',d.recordName);}}">定制人</th>
                    <th lay-data="{field:'recordTime', templet:function(d){return oa.formatdate('datetime',d.recordTime);}}">定制时间</th>
                    <th lay-data="{field:'modifyName', sort: true, templet:function(d){return oa.decipher('user',d.modifyName);}}">修改人</th>
                    <th lay-data="{field:'modifyTime', templet:function(d){return oa.formatdate('datetime',d.modifyTime);}}">修改时间</th>
                    <th lay-data="{fixed: 'right', width:220, align:'center', toolbar: '#listbar'}">管理</th>
                </tr>
                </thead>

            </table>
            <script type="text/html" id="listbar">
                {{# if (d.formType!=3){ }}
                <div class="layui-btn-container">
                    <a class="layui-btn layui-btn-xs" lay-event="editpage">编辑页面</a>
                    <a class="layui-btn layui-btn-xs" lay-event="publishpage">发布页面</a>
                    <a class="layui-btn layui-btn-xs" lay-event="viewpage">定制子表</a>
                </div>
                {{# } }}
            </script>
            <script type="text/html" id="toolbar">
                <div class="layui-btn-container">
                    <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="add">添加</a>
                    <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="modi">修改</a>
                    <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="delete">删除</a>
                    <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="break">刷新</a>
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
                                <option value="formcmName">页面主键</option>
                                <option value="formcmTitle">页面标题</option>
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
<c:if test="${type=='add' || type=='modi'}">
    <div class="layui-container">
        <div class="layui-row">
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <form class="layui-form" id="formcm_form">
                    <table class='layui-table' lay-skin='line' lay-size='sm'>
                        <colgroup>
                            <col width='70'>
                            <col width='200'>
                        </colgroup>
                        <thead>
                        <tr>
                            <th colspan='2'>新建表单</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td <c:if test="${type=='add'}">style='display:none'</c:if>>
                                <div style='width:90%;text-align:right;'>
                                    <span style='color: red;padding:5px;'>*</span>
                                    <label class='layui-form-text'>表单ID:</label>
                                </div>
                            </td>
                            <td <c:if test="${type=='add'}">style='display:none'</c:if>>
                                <div class='layui-input-label'>
                                    <input type='text' lay-verify="required"
                                           <c:if test="${type=='modi'}">readonly="readonly"</c:if>
                                           id='formcmName' name='formcmName' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${formcm.formcmName}</c:if>'
                                           class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <span style='color: red;padding:5px;'>*</span>
                                    <label class='layui-form-text'>表单名称:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <input type='text' id='formcmTitle' name='formcmTitle' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${formcm.formcmTitle}</c:if>'
                                           class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <span style='color: red;padding:5px;'>*</span>
                                    <label class='layui-form-text'>表单类型:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <select id="formType" name="formType" lay-filter="formType">
                                        <option value="1" <c:if test="${formcm.formType==1}">selected</c:if>>表单</option>
                                        <option value="3" <c:if test="${formcm.formType==3}">selected</c:if>>外部</option>
                                    </select>
                                </div>
                            </td>
                        </tr>
                        <tr <c:if test="${formcm.formType==3}">hidden</c:if>>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <label class='layui-form-text'>表单任务:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <input type='text' id='formTask' name='formTask' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${formcm.formTask}</c:if>'
                                           class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        <tr <c:if test="${formcm.formType==3}">hidden</c:if>>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <label class='layui-form-text'>列表任务:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <input type='text' id='listTask' name='listTask' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${formcm.listTask}</c:if>'
                                           class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        <tr <c:if test="${type=='add' || formcm.formType==1}">hidden</c:if>>
                            <td>
                                <div style='width:90%;text-align:right;'>
                                    <label class='layui-form-text'>页面路径:</label>
                                </div>
                            </td>
                            <td>
                                <div class='layui-input-label'>
                                    <input type='text' id='editPage' name='editPage' autocomplete='off'
                                           value='<c:if test="${type=='modi'}">${formcm.editPage}</c:if>'
                                           class='layui-input'>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
    </div>
</c:if>
<script type="text/javascript">
    var tableOptions = {
        url: '/form_custom/formcmselect.do', //请求地址
        method: 'POST', //方式
        id: 'formcm', //生成 Layui table 的标识 id，必须提供，用于后文刷新操作，笔者该处出过问题
        page: true, //是否分页
        limit: 10
    };
    layui.use(['form', 'layer', 'table','tree'], function () {
        var form = layui.form;
        var layer = layui.layer;
        var table = layui.table;
        var tableid = 'formcm';
        var error = '${error}';
        if(error!=null && error!="") {
            layer.msg(error);
        }
        var createTree = function () {
            var node = "";
            $.ajax({
                type: "POST",
                url: "/access/accesstree.do",
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
        layui.tree({
            elem: '#accesstree'
            , target: '_blank'
            , nodes: createTree()
            , click: function (item) {
                menuNum = item.menuNum;
                table.reload("formcm", {
                    where: {
                        option: "pageid",
                        inputval : item.id,
                    }
                });
            }
        });
        form.render('select');
        table.init('formcm', tableOptions);
        var reloadTable = function () {
            table.reload("formcm", { //此处是上文提到的 初始化标识id
                where: {
                    option: jQuery("#option  option:selected").val(),
                    inputval : $('#inputval').val(),
                }
            });
        };

        table.on('toolbar(' + tableid + ')', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            var othis = $(this);
            var method = othis.data('method');

            switch (obj.event) {
                case 'add':
                    var active = opedit(othis, 'add', layer, "");
                    active[method] ? active[method].call(this, othis) : '';
                    break;
                case 'modi':
                    if (data.length === 0) {
                        layer.msg('请选择一行');
                    } else if (data.length > 1) {
                        layer.msg('只能同时编辑一个');
                    } else {
                        var jsons = JSON.stringify(data);
                        var recno = trim(data[0]["formcmName"]);
                        var active = opedit(othis, 'modi', layer, recno);
                        active[method] ? active[method].call(this, othis) : '';
                    }
                    break;
                case 'delete':
                    if (data.length === 0) {
                        layer.msg('请选择一行');
                    } else {
                        var jsons = JSON.stringify(data);

                        var recno = "";
                        for(var i=0;i<data.length;i++){
                            recno += data[i]["formcmName"]+",";
                        }
                        console.log(recno)
                        if (recno != "") {
                            $.ajax({
                                type: "POST",
                                url: "/form_custom/formcmdelete.do?recno="+recno,
                                contentType: "application/json; charset=utf-8",
                                dataType: "html",
                                cache: false,
                                success: function (data) {
                                    oa.gotourl("/form_custom/form_custom_made.do");
                                },
                                error: function (message) {
                                    layer.msg("提交数据失败！");
                                }
                            });
                        }
                    }
                    break;
                default:
                    location.reload();
                    break;
            }
        });
        table.on('tool(' + tableid + ')', function (obj) {
            var data = obj.data
            console.log(data)
            switch (obj.event) {
                case 'editpage':
                    editpage(data.formcmName);
                    break;
                case 'publishpage':
                    publishpage(data.formcmName);
                    break;
                case 'viewpage':
                    viewpage(data.formcmName);
                    break;
            }
        });
        form.on('submit(reload)', function(data){
            reloadTable();
            return false;
        });

        form.on('select(formType)', function (data) {
            var value = data.value;
            if(value==3){
                $("#editPage").parent().parent().parent().show();
                $("#formTask").parent().parent().parent().hide();
                $("#formTask").val("");
                $("#listTask").parent().parent().parent().hide();
                $("#listTask").val("");
            }else{
                $("#editPage").parent().parent().parent().hide();
                $("#editPage").val("");
                $("#formTask").parent().parent().parent().show();
                $("#listTask").parent().parent().parent().show();
            }
        });
    });

    function editpage(value) {
        oa.gotourl("/form_custom/formedit.do?formid=" + value);
    }

    function viewpage(value) {
        oa.gotourl("/form_custom/gotochildtable.do?formid=" + value);
    }

    function opedit(othis, thistype, layer, recno) {
        var url = "", title = "";
        if (thistype == "modi") {
            console.log("recno:" + recno)
            url = '../form_custom/formcm/modi.do?recno=' + recno;
            title = '表单定义修改';
        } else {
            url = '../form_custom/formcm/add.do';
            title = '新增表单定义';
        }
        var active = {
            titlebtn: function (othis) {
                var that = this;
                var $type = othis.data('type');
                //多窗口模式，层叠置顶
                layer.open({
                    type: 2 //此处以iframe举例
                    , id: 'toolbar' + $type
                    , title: title
                    , area: ['500px', '350px']
                    , shade: 0
                    , maxmin: true
                    , offset: $type
                    , content: url
                    , btn: ['保存', '关闭']
                    , yes: function (active) {
                        var saveurl = "";
                        if (thistype == "add") {
                            saveurl = "../form_custom/formcminsert.do";
                        } else if (thistype == "modi") {
                            saveurl = "../form_custom/formcmupdate.do";
                        }
                        var datas = fun();
                        console.log(datas)
                        $.ajax({
                            type: "POST",
                            url: saveurl,
                            data: datas,
                            contentType: "application/x-www-form-urlencoded; charset=utf-8",
                            cache: false,
                            dataType: "json",
                            success: function (data) {
                                window.location.href = "../form_custom/form_custom_made.do";
                            },
                            error: function () {
                                layer.msg("请求失败");
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
        }
        return active;
    }

    function trim(str) {
        if (str.length > 0) {
            return str.replace(/(^\s*)|(\s*$)/g, "");
        } else {
            return "";
        }

    }

    var dag = window.parent;
    dag.fun = function () {
        return $('#formcm_form').serialize();
    }

    function publishpage(formid) {
        $.ajax({
            type: "POST",//方法类型
            url: "/form_custom/rebirthPage/" + formid + ".do",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            dataType: "html",
            cache: false,
            success: function (data) {
                alert("重生成功");
            },
            error: function (error) {
                alert("请求失败");
            }
        });
    }
    function getDecipher(type,value){
        return oa.idtoname(type,value);
    }
</script>
</body>
</html>

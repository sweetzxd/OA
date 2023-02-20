<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<%--jspBegin--%>
<%--jspEnd--%>
<html>
<head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/zjyoa-pc/resources/css/tablebox.css" type="text/css"/>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
    <script src="/zjyoa-pc/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
    <script src="/zjyoa-pc/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
    <%--scriptBegin--%>
    <%--scriptEnd--%>
    <script type="text/javascript">
        function starscript(pageid, tableid, formid, field, value) {
            var type = '${type}',listsave = '${listsave}';
            if(type == 'list'){tableLoading(pageid, tableid, formid, field, value);}
            if(listsave == 'true'){listModi(tableid);}
            formLoading(pageid, tableid, formid);
            <%--functionBegin--%>
            layui.table.on('tool(${tableid})',function (obj) {
                var data=obj.data
                if(obj.event==='printallnxjtz'){
                    console.log(data)
                    empModi(data.recorderNO);
                }
            })
            function empModi(value) {
                console.log(value)
                oa.open2Window("/zjyoa-pc/printall/nianxiujiaupdateprint.do?&recorderNO=" + value);
            }
            <%--functionEnd--%>
        }
        <%--jsBegin--%>
        <%--jsEnd--%>
    </script>
</head>
<body onload="starscript('${pageid}','${tableid}','flowtable2019080200006','${field}','${value}')">
<div class="layui-fluid">
        <div class="layui-tab layui-tab-card div-box" lay-filter="docDemoTabBrief">
            <ul class="layui-tab-title">
                <li class="layui-this" lay-id="agents">进行中</li>
                <li lay-id="track">完成</li>
            </ul>
        <div class="layui-tab-content" >
            <div class="layui-tab-item div-box">
                <table lay-filter="proctable" id="nxjdzdy2019111200001" class="layui-table" lay-data="{id:'grsqd19080201',cellMinWidth: 80, page: true, toolbar: '#toolbar', defaultToolbar: ['filter', 'exports','批量打印审批单']}" style="display: none;">
                    <thead>
                    <tr>
                        <th lay-data="{type:'checkbox', fixed: 'left',align:'center',width:60,field:'recorderNO'}"></th>
                        <th lay-data="{field:'gzdbt19041901_num',fixed: 'left',align:'center', unresize:true,width:85, templet: '#tableNum'}">序号</th>
                        <th lay-data="{field:'sqr1908020004', unresize:true,align:'center', sort: true ,templet:function(d){return oa.decipher('user',d.sqr1908020004);}}">申请人</th>
                        <th lay-data="{field:'sqbm190802004', unresize:true,align:'center', sort: true ,templet:function(d){return oa.decipher('dept',d.sqbm190802004);}}">申请部门</th>
                        <th lay-data="{field:'zw19102600002', unresize:true,align:'center', sort: true ,templet:function(d){return oa.decipher('select','zw19102600002',d.zw19102600002);}}">职位</th>
                        <th lay-data="{field:'yjhks19080201', unresize:true, align:'center',sort: true ,templet:function(d){return oa.decipher('datetime',d.yjhks19080201);}}">原计划开始时间</th>
                        <th lay-data="{field:'yjhjs19080201', unresize:true,align:'center', sort: true ,templet:function(d){return oa.formatdate('datetime',d.yjhjs19080201);}}">原计划结束时间</th>
                        <th lay-data="{field:'yjhts19080201', unresize:true, align:'center',sort: true ,templet:function(d){return oa.decipher('decimal',d.yjhts19080201);}}">原计划天数</th>
                        <th lay-data="{field:'printstatus', width:90,sort: true,align:'center', templet:'#printstatus'}">打印状态</th>
                        <th lay-data="{field:'wkfStatus', width:90,sort: true,align:'center', templet:'#wkfStatus'}">流程状态</th>
                    </tr>
                    </thead>
                </table>
               <script type="text/html" id="toolbar">
                   <div class="layui-btn-container">
                       <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="allprint">批量打印</a>
                       <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="break">刷新</a>
                      <%-- <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="nxjing">进行中</a>
                       <a data-method="titlebtn" class="layui-btn layui-btn-primary layui-btn-xs" lay-event="nxjend">已完成</a>--%>
                   </div>
               </script>
                <script type="text/html" id="wkfStatus">
                    {{#  if(d.wkfStatus == 1){ }}
                    进行中
                    {{#  } else if(d.wkfStatus = 2) { }}
                    完成
                    {{#  } }}
                </script>
                <script type="text/html" id="tableNum">
                    <a class="layui-btn layui-btn-sm" style="text-decoration:none;" >{{d.gzdbt19041901_num}}</a>
                </script>
               <script type="text/html" id="printstatus">
                   {{#  if(d.printstatus == 0){ }}
                   未打印
                   {{#  } else if(d.printstatus == 1) { }}
                   已打印
                   {{#  } }}
               </script>
            </div>
           <div class="layui-tab-item div-box">
                <table id="demo2" lay-filter="test2" class="layui-table" lay-data="{id: 'demo2'}"></table>
               <script type="text/html" id="toolbar1">
                   <div class="layui-btn-container">
                       <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="allprint">批量打印</a>
                       <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="break">刷新</a>
                       <%-- <a data-method="titlebtn" class="layui-btn layui-btn-xs" lay-event="nxjing">进行中</a>
                        <a data-method="titlebtn" class="layui-btn layui-btn-primary layui-btn-xs" lay-event="nxjend">已完成</a>--%>
                   </div>
               </script>
            </div>
        </div>

    </div>
</div>
<script type="text/javascript">
    layui.use(['table', 'form', 'layer', 'laydate', 'element', 'tree'], function () {
        var layer = layui.layer;
        var form = layui.form;
        var table = layui.table;
        var tableid = 'proctable';
        var error = '${error}';
        var wkflwId = '${wkflwId}';
        var tab = table.render();
        var element = layui.element;

        element.tabChange('docDemoTabBrief', "agents");
        element.on('tab(docDemoTabBrief)', function(){
            var type = this.getAttribute('lay-id');
            switch (type) {
                case "agents":
                    tab = table.reload("proctable", {});;
                    break;

                case "track":
                    tab = table.render(gettable1("demo2",type));
                    break;
            }
        });

        table.init('proctable', {
            url: '/zjyoa-pc/nxjtzall/nxjselelist.do?type=1',
            method: 'POST',
            id: 'proctable',
            page: true,
            limit: 10,
        });
        table.on('toolbar(' + tableid + ')', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            var othis = $(this);
            var method = othis.data('method');
            switch (obj.event) {
                case 'allprint':
                    if (data.length === 0) {
                        layer.msg('请选择一行');
                    } else {
                        var jsons = JSON.stringify(data);
                        console.log(jsons);
                        console.log("999988")
                        var recno = "";
                       for(var i=0;i<data.length-1;i++){
                            recno += data[i]["recorderNO"]+",";
                        }
                        console.log(recno)
                        if (recno != "") {
                            oa.open2Window("/zjyoa-pc/printall/nianxiujiaupdateprint.do?&type=1&recorderNO=" + recno);
                        }
                    }
                    break;
                case 'nxjing':
                    tab = table.render(gettable1("demo2",type));
                    break;
                case 'nxjend':

                    break;
                default:
                    location.reload();
                    break;
            }
        });

        table.on('toolbar(test2)', function(obj){
            console.log("55555")
            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            var othis = $(this);
            var method = othis.data('method');
            switch (obj.event) {
                case 'allprint':
                    if (data.length === 0) {
                        layer.msg('请选择一行');
                    } else {
                        var jsons = JSON.stringify(data);
                        console.log(jsons);
                        console.log("999988")
                        var recno = "";
                        for(var i=0;i<data.length;i++){
                            recno += data[i]["recorderNO"]+",";
                        }
                        console.log(recno)
                        if (recno != "") {
                            oa.open2Window("/zjyoa-pc/printall/nianxiujiaupdateprint.do?&type=1&recorderNO=" + recno);
                        }
                    }
                    break;


                case 'nxjing':
                    tab = table.render(gettable1("demo2",type));
                    break;
                case 'nxjend':

                    break;
                default:
                    location.reload();
                    break;
            }

        });

        table.on('toolbar1(' + tableid + ')', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            var othis = $(this);
            var method = othis.data('method');
            switch (obj.event) {
                case 'allprint':
                    if (data.length === 0) {
                        layer.msg('请选择一行');
                    } else {
                        var jsons = JSON.stringify(data);
                        console.log(jsons);
                        console.log("999988")
                        var recno = "";
                        for(var i=0;i<data.length;i++){
                            recno += data[i]["recorderNO"]+",";
                        }
                        console.log(recno)
                        if (recno != "") {
                            oa.open2Window("/zjyoa-pc/printall/nianxiujiaupdateprint.do?&type=1&recorderNO=" + recno);
                        }
                    }
                    break;
                case 'nxjing':
                    tab = table.render(gettable1("demo2",type));
                    break;
                case 'nxjend':

                    break;
                default:
                    location.reload();
                    break;
            }
        });
    });

    function gettable1(id,type){
        var ty='${type}';
        var inputval='${wkflwId}';
        return {
            elem: '#'+id
            ,height: 'full-140'
            ,url: '/zjyoa-pc/nxjtzall/nxjselelist.do?type=2'
            ,page: true //开启分页
            ,defaultToolbar: [] //这里在右边显示
            ,toolbar: '#toolbar1'
            ,cols: [[ //表头
                {type:'checkbox', fixed: 'left',align:'center',width:60,field:'recorderNO'}
                ,{field: 'gzdbt19041901_num', title: '序号',fixed: 'left',align:'center', unresize:true,width:85, templet: '#tableNum'}
                ,{field: 'sqr1908020004', title: '申请人',unresize:true, align:'center',sort: true ,templet:function(d){return oa.decipher('user',d.sqr1908020004);}}
                ,{field: 'sqbm190802004', title: '申请部门',unresize:true, align:'center',sort: true ,templet:function(d){return oa.decipher('dept',d.sqbm190802004);}}
                ,{field: 'zw19102600002', title: '职位',sort: true,align:'center',templet:function(d){return oa.decipher('select','zw19102600002',d.zw19102600002);}}
                ,{field: 'yjhks19080201', title: '原计划开始时间',align:'center', sort: true ,templet:function(d){return oa.decipher('datetime',d.yjhks19080201);}}
                ,{field: 'yjhjs19080201', title: '原计划结束时间',align:'center',sort: true,templet:function(d){return oa.formatdate('datetime',d.yjhjs19080201);}}
                ,{field: 'yjhts1908020', title: '原计划天数', align:'center',templet:function(d){return oa.decipher('decimal',d.yjhts19080201);}}
                ,{field: 'wkfStatus', title: '流程状态',width:90,align:'center',sort: true, templet:'#wkfStatus'}
                ,{field: 'printstatus', title: '打印状态',width:90, align:'center',templet:'#printstatus'}
            ]]
        };
    }
</script>


</body>
</html>
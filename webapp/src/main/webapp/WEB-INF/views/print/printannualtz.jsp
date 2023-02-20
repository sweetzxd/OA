<%--
  User: zxd
  Date: 2020/02/18
  Time: 下午 2:08
  Explain: 说明
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.springframework.util.Base64Utils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<%@ page import="com.oa.core.bean.Loginer" %>
<%@ page import="com.oa.core.service.util.TableService" %>
<%@ page import="com.oa.core.util.SpringContextUtil" %>
<%@ page import="com.oa.core.helper.DateHelper" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<%
    String decodeUrl = request.getParameter("url");
    if (decodeUrl != null) {
        String url = new String(Base64Utils.decode(decodeUrl.getBytes()));
        request.setAttribute("url", url);
    }
    Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
    TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
    int cnum2 = tableService.selectSqlCount("select count(*) from userInfo where userName not in('admin','lvshi','dongzhanwei','longdongmei','tianxu','daizhiqiang','luoqiang','zhangyuanheng')  ");
    request.setAttribute("qb", cnum2);
%>

<html>
<head>
    <title>年假申请打印</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" type="text/css" href="/zjyoa-pc/resources/css/print.css"/>
    <link rel="stylesheet" href="/zjyoa-pc/resources/layuiadmin/layui/css/layui.css" media="all">
    <style>
        .icon-box{
            float:left;
            margin-left: 30px;
            height: 25px;
            line-height: 25px;
        }
        .layui-size{
            padding: 5px 0 5px 0;
            font-size: 15px;
        }
    </style>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
    <script src="/zjyoa-pc/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
    <script src="/zjyoa-pc/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
    <%--导出word引入的js--%>
    <script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.js"></script>
    <script type="text/javascript" src="/zjyoa-pc/resources/js/printword/FileSaver.js"></script>
    <script type="text/javascript" src="/zjyoa-pc/resources/js/printword/jquery.wordexport.js"></script>
    <script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>
    <script type="text/javascript">
        function preview(oper) {
            console.log("444");
            if (oper < 10) {
                bdhtml = window.document.body.innerHTML;
                sprnstr = "<!--startprint" + oper + "-->";
                eprnstr = "<!--endprint" + oper + "-->";
                prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 18);
                console.log(prnhtml);
                prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));
                console.log(prnhtml);
                window.document.body.innerHTML = prnhtml;
                console.log(prnhtml);
                console.log("3333333--------------");
                window.print();
                window.document.body.innerHTML = bdhtml;
            } else {
                window.print();
            }
            window.location.reload();
        }

        var idTmr;
        //兼容的浏览器
        function  getExplorer() {
            var explorer = window.navigator.userAgent ;
            //ie
            if (explorer.indexOf("MSIE") >= 0) {
                return 'ie';
            }
            //firefox
            else if (explorer.indexOf("Firefox") >= 0) {
                return 'Firefox';
            }
            //Chrome
            else if(explorer.indexOf("Chrome") >= 0){
                return 'Chrome';
            }
            //Opera
            else if(explorer.indexOf("Opera") >= 0){
                return 'Opera';
            }
            //Safari
            else if(explorer.indexOf("Safari") >= 0){
                return 'Safari';
            }
        }
        //导出excel
        function exportexcelseal(tableid) {
            if(getExplorer()=='ie')
            {
                var curTbl = document.getElementById(tableid);
                var oXL = new ActiveXObject("Excel.Application");
                var oWB = oXL.Workbooks.Add();
                var xlsheet = oWB.Worksheets(1);
                var sel = document.body.createTextRange();
                sel.moveToElementText(curTbl);
                sel.select();
                sel.execCommand("Copy");
                xlsheet.Paste();
                oXL.Visible = true;

                try {
                    var fname = oXL.Application.GetSaveAsFilename("Excel.xls", "Excel Spreadsheets (*.xls), *.xls");
                } catch (e) {
                    print("Nested catch caught " + e);
                } finally {
                    oWB.SaveAs(fname);
                    oWB.Close(savechanges = false);
                    oXL.Quit();
                    oXL = null;
                    idTmr = window.setInterval("Cleanup();", 1);
                }
            }
            else
            {
                tableToExcel(tableid)
            }
        }
        function Cleanup() {
            window.clearInterval(idTmr);
            CollectGarbage();
        }
        var tableToExcel = (function() {
            var uri = 'data:application/vnd.ms-excel;base64,',
                template = '<html><head><meta charset="UTF-8"></head><body><table>{table}</table></body></html>',
                base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) },
                format = function(s, c) {
                    return s.replace(/{(\w+)}/g,
                        function(m, p) { return c[p]; }) }
            return function(table, name) {
                if (!table.nodeType) table = document.getElementById(table)
                var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
                window.location.href = uri + base64(format(template, ctx))
            }
        })()
        //导出word
        function exportwordseal() {
            $("#sealdiv").wordExport('年假申请打印')
        }
        var createTree = function () {
            var node = "";
            var type= 1;
            $.ajax({
                type: "POST",
                url: "/zjyoa-pc/annual/deptworknxj.do?type=2",
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
        var element = null;
        function starscript() {
        }

    </script>
</head>
<body onload="starscript()">
<div class="layui-side" style="margin-top: 30px">
    <div class="layui-side-scroll">
        <ul id="depttree">
            <li data-spread="true">
                <a href="javascript:getall();"><i class="layui-icon layui-tree-leaf"></i><cite>质检院</cite></a>
            </li>
        </ul>
    </div>
</div>
<div style="padding-left: 10%;padding-top: 5%">
   <input style="width: 100px;height: 30px;background-color: #0b96e5;" type=button name='button_export' value='打印' onclick=preview(1)>
    <input style="width: 100px;height: 30px;background-color: #0b96e5;" type=button name='button_export' value='导出excel'  onclick="exportexcelseal('sealdiv')">
</div>
<div id="sealdiv">
<!--startprint1-->
    <div style="height:95%">
        <div  class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                         <div align="center" style="font-size: 20px;padding-top: 25px">河北省产品质量监督检验研究院工作人员带薪年休假计划表</div>
                         <span id="dept" style="padding-left: 12%;font-size: 15px;padding-top: 30px;display: inline-block;">部门：</span><span id="year" style="padding-left: 60%;display: inline-block;font-size: 15px"></span>
                      <%--  <div style="padding-left: 12%;font-size: 15px;display: inline-block;">--%>
                         <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                            <div  style='font-size: 20px;padding-left: 12%;'>
                                <table border="1" cellspacing="0" style="width: 100%;font-size:15px;text-align: center" >
                                    <thead>
                                    <tr style='height: 50px'>
                                        <td width="70px"></td>
                                        <td width="50px">序号</td>
                                        <td width="70px">姓名</td>
                                        <td width="100px">参加工作时间</td>
                                        <td width="100px">工作年限</td>
                                        <td width="100px">应休天数</td>
                                        <td>计划休假时间</td>
                                        <td width="100px">B角人员</td>
                                    </tr>
                                    </thead>
                                    <tbody id="tbodybox">

                                    </tbody>
                                </table>
                                <div id="user-row"></div>
                            </div>
                         </div>
                   <div>
                       <span id="deptzg" style="padding-left: 12%;font-size: 15px;display: inline-block;"></span>
                       <span id="deptbm" style="padding-left:5%;font-size: 15px;display: inline-block;"></span>
                       <span id="deptkq" style="padding-left:5%;font-size: 15px;display: inline-block;"></span>
                   </div></div>
    </div>
<!--endprint1-->
</div>
<script>
    var createTree = function () {
        var node = "";
        var type= 1;
        $.ajax({
            type: "POST",
            url: "/zjyoa-pc/annual/deptworknxj.do?type=2",
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

    var deptTree = createTree();

    layui.use(['table', 'element', 'tree'], function () {
        var layer = layui.layer;
        var element = layui.element;
        var error = '${error}';
        if (error != null && error != "") {
            layer.msg(error);
        }
        settable(deptTree[0].id,false,0,deptTree);
        for(var i in deptTree){
            var dept = deptTree[i];
            var a="<span style='color: red'>dept</span>";
            console.log(i,dept.id,dept.name);
        }
        layui.tree({
            elem: '#depttree'
            , target: '_blank'
            , nodes: deptTree
            , click: function (item) {
                settable(item.id,true,0);
                /* $.post("/zjyoa-pc/annual/selecontentnxj.do?inputval="+id);
                 window.location.reload();*/
            }
        });
    });
    function getall(){
        settable(deptTree[0].id,false,0,deptTree);
    }  function settable(id,type,num,deptTree){
        if(num==0){
            $('#tbodybox').html("");
        }
        $.ajax({
            type: "post",
            url: "/zjyoa-pc/annual/selecontentnxj.do",
            data: {"option": 'department', "inputval": id},
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            dataType: "json",
            success: function (dat) {
                var  ddaa=dat.data;
                var year=dat.year;
                var dept=dat.dept;
                if(type){
                    $('#tbodybox').html("");
                    $('#year').html(year);
                    $('#dept').html("部门："+dept);
                    $('#deptzg').html("");
                    $('#deptbm').html("");
                    $('#deptkq').html("");
                    /*if(ddaa.length==0){
                        $('#tbodybox').append("<tr style='height: 50px'><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>");
                    }else {
                        $('#deptzg').html("主管院领导：" + dat.deptzg);
                        $('#deptbm').html("部门领导：" + dat.deptbm);
                        $('#deptkq').html("填表人：" + dat.deptkq);
                    }*/
                }
                for(var i=0,len=ddaa.length;i<len;i++){
                    if(i==0){
                        $('#tbodybox').append("<tr style='height: 50px'><td rowspan='"+len+"' width='70px'>"+dept+"</td><td width='50px'>"+(i+1)+"</td><td width='70px'>"+ddaa[i].sqr+"</td><td width='100px'>"+ddaa[i].rzrq+"</td><td width='100px'>"+ddaa[i].gznx+"</td width='100px'><td>"+ddaa[i].yxts+"</td><td>"+ddaa[i].njrq+"</td><td width='100px'> "+ddaa[i].Bjry+"</td></tr>");
                    }else{
                        $('#tbodybox').append("<tr style='height: 50px'><td width='50px'>"+(i+1)+"</td><td width='70px'>"+ddaa[i].sqr+"</td><td width='100px'>"+ddaa[i].rzrq+"</td><td width='100px'>"+ddaa[i].gznx+"</td><td width='100px'>"+ddaa[i].yxts+"</td><td>"+ddaa[i].njrq+"</td><td width='100px'> "+ddaa[i].Bjry+"</td></tr>");
                    }
                }
                $("#user-row").text("总人数：${qb}人")
                if(!type){
                    settable(deptTree[num+1].id,type,num+1,deptTree);
                }
            },
            error: function () {
            }
        })
    }

</script>
</body>
</html>
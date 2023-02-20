<%--
  User: zxd
  Date: 2020/02/18
  Time: 下午 2:08
  Explain: 说明
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.springframework.util.Base64Utils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
    <title>年休假确认</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" type="text/css" href="/zjyoa-pc/resources/css/print.css"/>
    <link rel="stylesheet" href="/zjyoa-pc/resources/layuiadmin/layui/css/layui.css" media="all">
    <style>
        .icon-box {
            float: left;
            margin-left: 30px;
            height: 25px;
            line-height: 25px;
        }

        .layui-size {
            padding: 5px 0 5px 0;
            font-size: 15px;
        }
        .date-box{
            text-align: left;

        }
        .date-box a{
            color: #0aa8e4;
            cursor:pointer;
            margin: 0 20px;
        }
        .date-box a:hover{
            color: #e7740d;
         }

    </style>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" type="text/javascript"
            charset="utf-8"></script>
    <script src="/zjyoa-pc/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
    <script src="/zjyoa-pc/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
    <%--导出word引入的js--%>
    <script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.js"></script>
    <script type="text/javascript" src="/zjyoa-pc/resources/js/printword/FileSaver.js"></script>
    <script type="text/javascript" src="/zjyoa-pc/resources/js/printword/jquery.wordexport.js"></script>
    <script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>
    <script type="text/javascript">
        function preview(oper) {
            if (oper < 10) {
                bdhtml = window.document.body.innerHTML;
                sprnstr = "<!--startprint" + oper + "-->";
                eprnstr = "<!--endprint" + oper + "-->";
                prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 18);
                prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));
                window.document.body.innerHTML = prnhtml;
                window.print();
                window.document.body.innerHTML = bdhtml;
            } else {
                window.print();
            }
            window.location.reload();
        }

        var idTmr;

        //兼容的浏览器
        function getExplorer() {
            var explorer = window.navigator.userAgent;
            //ie
            if (explorer.indexOf("MSIE") >= 0) {
                return 'ie';
            }
            //firefox
            else if (explorer.indexOf("Firefox") >= 0) {
                return 'Firefox';
            }
            //Chrome
            else if (explorer.indexOf("Chrome") >= 0) {
                return 'Chrome';
            }
            //Opera
            else if (explorer.indexOf("Opera") >= 0) {
                return 'Opera';
            }
            //Safari
            else if (explorer.indexOf("Safari") >= 0) {
                return 'Safari';
            }
        }

        //导出excel
        function exportexcelseal(tableid) {
            if (getExplorer() == 'ie') {
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
            } else {
                tableToExcel(tableid)
            }
        }

        function Cleanup() {
            window.clearInterval(idTmr);
            CollectGarbage();
        }

        var tableToExcel = (function () {
            var uri = 'data:application/vnd.ms-excel;base64,',
                template = '<html><head><meta charset="UTF-8"></head><body><table>{table}</table></body></html>',
                base64 = function (s) {
                    return window.btoa(unescape(encodeURIComponent(s)))
                },
                format = function (s, c) {
                    return s.replace(/{(\w+)}/g,
                        function (m, p) {
                            return c[p];
                        })
                }
            return function (table, name) {
                if (!table.nodeType) table = document.getElementById(table)
                var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
                window.location.href = uri + base64(format(template, ctx))
            }
        })()

        //导出word
        function exportwordseal() {
            $("#sealdiv").wordExport('年假申请打印')
        }

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
    <input style="width: 100px;height: 30px;background-color: #0b96e5;" type=button name='button_export' value='打印'
           onclick=preview(1)>
    <input style="width: 100px;height: 30px;background-color: #0b96e5;" type=button name='button_export' value='导出excel'
           onclick="exportexcelseal('sealdiv')">
</div>
<div id="sealdiv">
    <!--startprint1-->
    <div style="height:95%">
        <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
            <div align="center" style="font-size: 20px;padding-top: 25px">河北省产品质量监督检验研究院工作人员带薪年休假确认表</div>
            <span id="dept"
                  style="padding-left: 12%;font-size: 15px;padding-top: 30px;display: inline-block;">部门：</span><span
                id="year" style="padding-left: 60%;display: inline-block;font-size: 15px"></span>
            <%--  <div style="padding-left: 12%;font-size: 15px;display: inline-block;">--%>
            <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
                <div style='font-size: 20px;padding-left: 12%;'>
                    <table border="1" cellspacing="0" style="width: 100%;font-size:15px;text-align: center">
                        <thead>
                        <tr style='height: 50px'>
                            <td width="10%"></td>
                            <td width="5%">序号</td>
                            <td width="10%">姓名</td>
                            <td width="60%">最终休假时间</td>
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
            </div>
        </div>
    </div>
    <!--endprint1-->
</div>
<script>
    var countman = 0;
    var createTree = function () {
        var node = "";
        var type = 1;
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
        settable(deptTree[0].id, false, 0, deptTree);
        for (var i in deptTree) {
            var dept = deptTree[i];
            var a = "<span style='color: red'>dept</span>";
        }
        layui.tree({
            elem: '#depttree'
            , target: '_blank'
            , nodes: deptTree
            , click: function (item) {
                countman = 0;
                settable(item.id, true, 0);
            }
        });
    });

    function getall() {
        settable(deptTree[0].id, false, 0, deptTree);
    }

    var re = [];
    var re1 = [];

    function settable(id, type, num, deptTree) {
        if (num == 0) {
            $('#tbodybox').html("");
        }
        $.ajax({
            type: "post",
            url: "/zjyoa-pc/Kqconfirm/confirm.do?inputval=" + id,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (dat) {
                var ddaa = dat.data;
                var year = dat.year;
                var dept = dat.dept;
                var returnmap = dat.returnmap;
                var returnmap1 = dat.returnmap1;
                var returnmap2 = dat.returnmap2;
                var returnmap3 = dat.returnmap3;
                re = returnmap;
                re1 = returnmap1;
                if (type) {
                    $('#tbodybox').html("");
                    $('#year').html(year);
                    if (dept == '') {
                        $('#dept').html("部门：质检院");
                    } else {
                        $('#dept').html("部门：" + dept);
                    }
                    $('#deptzg').html("");
                    $('#deptbm').html("");
                    $('#deptkq').html("");
                }

                for (var i = 0, len = ddaa.length; i < len; i++) {
                    var username = ddaa[i].username;
                    if (i == 0) {
                        var thh = "";
                        for (var j = 0; j < returnmap[ddaa[i].username].length; j++) {
                            var date = returnmap[username][j];
                            var date2 = returnmap2[username][date];
                            if(date2==null || date2.length<1){
                                date2 = "无";
                            }
                            thh += "<p>" + returnmap[ddaa[i].username][j] + "<a onclick=\"ckeckedth('" + ddaa[i].username + "','" + date + "',this)\">确认</a><lable>已确认："+date2+"</lable><p>";
                        }
                        countman++;
                        $('#tbodybox').append("<tr style='height: 50px'><td rowspan='" + len + "' width='70px'>" + dept + "</td><td width='50px'>" + ddaa[i].num + "</td><td width='70px'>" + ddaa[i].sqr + "</td><td width='70px' class='date-box'>" + thh + "</td></tr>");
                    } else {
                        var thh = "";
                        for (var j = 0; j < returnmap[ddaa[i].username].length; j++) {
                            var date = returnmap[username][j];
                            var date2 = returnmap2[username][date];
                            if(date2==null || date2.length<1){
                                date2 = "无";
                            }
                            thh += "<p>" + returnmap[ddaa[i].username][j] + "<a onclick=\"ckeckedth('" + ddaa[i].username + "','" + date + "',this)\">确认</a><lable>已确认："+date2+"</lable><p>";
                        }
                        countman++;
                        $('#tbodybox').append("<tr style='height: 50px'><td width='50px'>" + ddaa[i].num + "</td><td width='70px'>" + ddaa[i].sqr + "</td><td width='70px' class='date-box'>" + thh + "</td></tr>");
                    }
                }
                $("#user-row").text("总人数："+countman+"人")
                if (!type) {
                    if(deptTree[num + 1]){
                        settable(deptTree[num + 1].id, type, num + 1, deptTree);
                    }
                }
            },
            error: function () {
            }
        })
    }

    function ckeckedth(name, date, obj) {
        var arr = new Array();
        arr = date.split(" - ");
        var jihua = getAllWeak(arr[0], arr[1]);
        $.ajax({
            type: "post",
            url: "/zjyoa-pc/Kqconfirm/getquerenMap.do?name=" + name,
            success: function (data) {
                if (data.code == 1) {
                    var qd = data.qdmaps;
                    var desc = data.qdmapsdesc;
                    if (jihua != null) {
                        var num = 0;
                        for (var jh of jihua) {
                            var ht = "";
                            if (qd.indexOf(jh) >= 0) {
                                ht = "<div><input class='date_off' name='date' type='checkbox' value='" + jh + "'  disabled=true>" + jh + "<input value='" + desc[num] + "' readonly><a href='javascript:void(0)' onclick=\"deletedate(this,'"+date+"','"+jh+"','"+name+"')\">删除</a></div><br>";
                                num++;
                            } else {
                                ht = "<div class='checkdate'><input class='date_no' name='date' type='checkbox' value='" + jh + "'>" + jh + "<input name='desc' ></div><br>";
                            }
                            $('#ta').append(ht);
                        }
                    }
                }
            }
        });

        layer.open({
            title: "确认日期"
            , area: ['500px', '350px']
            , maxmin: true
            , offset: '100px'
            , content: '<button type="button" onclick="checkall()">全选</button>  <button type="button" onclick="fanx()">反选</button></br><div id="ta"></div>'
            , btn: ['确定']
            , yes: function (index, layero) {
                $('.checkdate').each(function (i, obj) {
                    var dates = $(obj).find('input[type="checkbox"]:checked');

                    if (dates != null && dates.length > 0) {

                        var eachday = dates.val();
                        if (eachday != null) {
                            var resdesc = $(obj).find('input[name="desc"]').val();

                            $.ajax({
                                type: "post",
                                url: "/zjyoa-pc/Kqconfirm/insertckeck.do?name=" + name + "&date=" + date + "&eachday=" + eachday + "&resdesc=" + resdesc,
                                success: function (dat) {
                                    var isqs = dat.isqs;
                                    var resdesc = dat.resdesc;
                                    if (isqs == 1) {
                                        $(obj).attr("onclick", "123");
                                        $(obj).html("已确认  " + resdesc);
                                    } else {
                                        console.log("shibai");
                                    }
                                },
                                error: function () {
                                }
                            });
                        }
                    }
                })
                layer.closeAll();
            }
        });
    }
    function deletedate(obj,date,eachday,name){
        var ht = "<input class='date_no' name='date' type='checkbox' value='" + eachday + "'>" + eachday + "<input name='desc'>";
        $.ajax({
            type: "GET",
            url: "/zjyoa-pc/Kqconfirm/deleteckeck.do?name=" + name + "&date=" + date + "&eachday=" + eachday,
            success: function (dat) {
                console.log(dat);
                if(dat.code==1) {
                    $(obj).parent().html(ht).addClass("checkdate");
                }
            },
            error: function () {
            }
        });
    }
    function checkall(){
        var checkboxs=document.getElementsByClassName("date_no");
        for (var i=0;i<checkboxs.length;i++) {
            var e=checkboxs[i];
            e.checked=true;
        }
    }
    function fanx(){
        var checkboxs=document.getElementsByClassName("date_no");
        for (var i=0;i<checkboxs.length;i++) {
            var e=checkboxs[i];
            e.checked=!e.checked;
        }
    }
    /**
     * 获取日期段所有的日期字符串
     * var weak = getAllWeak(begintime,endtime)+"," 加“，”  //调用方法将动态的开始时间，结束时间放
     * 入参数中
     * weak.split(",")[i]  //将获取的字符串截取
     * @param start_time
     * @param end_time
     * @returns  返回所有日期的字符串
     */
    function getAllWeak(start_time, end_time) {
        var begin = new Date(start_time), end = new Date(end_time);
        var begin_time = begin.getTime(), end_time = end.getTime(), time_diff = end_time - begin_time;
        var all_d = [];
        for (var i = 0; i <= time_diff; i += 86400000) {
            var ds = new Date(begin_time + i);
            var zero = "";
            var zero1 = "";
            if (ds.getMonth() + 1 < 10) {
                zero = "0";
            }
            if (ds.getDate() < 10) {
                zero1 = "0";
            }
            all_d.push(ds.getFullYear() + "-" + zero + (ds.getMonth() + 1) + "-" + zero1 + ds.getDate());
        }
        return all_d;
    }
</script>
</body>
</html>
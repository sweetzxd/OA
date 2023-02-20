<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/5/21
  Time: 14:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.springframework.util.Base64Utils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" type="text/css" href="/zjyoa-pc/resources/css/print.css"/>
<jsp:include page="/common/js.jsp"></jsp:include>
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
<%
    String decodeUrl = request.getParameter("url");
    if (decodeUrl != null) {
        String url = new String(Base64Utils.decode(decodeUrl.getBytes()));
        request.setAttribute("url", url);
    }
%>
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>

<script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.js"></script>
<script type="text/javascript" src="/zjyoa-pc/resources/js/printword/FileSaver.js"></script>
<script type="text/javascript" src="/zjyoa-pc/resources/js/printword/jquery.wordexport.js"></script>


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
    function exportexcelopinioin(tableid) {
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
        }
        else {
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

    function exportwordopinion() {
        $("#opiondiv").wordExport('省质检研究院异地租车申请单')
    }

</script>
<html>
<head>
    <title>薪资审批台账打印单</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick="preview(1)">
</div>
        <!--startprint1-->
        <div id="opiondiv" style="width: 100%;height: 100%">
            <div id="Adiv" style="padding-bottom: 8%">
                <table style="width: 100%;height: 30%;font-size:18px;" border="1" cellspacing="0">
                        <tr>
                            <td colspan="4" style="text-align: left"><br/><h3>${ym}月份人员工资审批表</h3></td>
                        </tr>
                        <tr >
                            <td style="width: 10%;text-align: center">A公司</td>
                            <td style="width: 20%;text-align: left">合计支付：${data.get(6).get(0).get("summoneyA")}</td>
                            <td style="width: 65%;text-align: left">人数：${data.get(6).get(0).get("numA")}</td>

                        </tr>
                </table>
                <table style="width: 100%;font-size:13px;" border="1" cellspacing="0">
                  <tr style="height: 10%">
                      <td rowspan="3" style="text-align: center">序号</td>
                      <td rowspan="3" style="text-align: center">部门</td>
                      <td rowspan="3" style="text-align: center">姓名</td>
                      <td colspan="3" style="text-align: center">绩效工资</td>
                      <td colspan="4" rowspan="2" style="text-align: center">津补贴</td>
                      <td rowspan="3" style="text-align: center">补发工资</td>
                      <td rowspan="3" style="text-align: center">扣发工资</td>
                      <td rowspan="3" style="text-align: center">应发<br/>工资</td>
                      <td rowspan="3" style="text-align: center">代扣个人<br/>保险</td>
                      <td rowspan="1"colspan="5" style="text-align: center">个人所得税专项附加扣除</td>
                      <td rowspan="3" style="text-align: center">本月实交<br/>个税</td>
                      <td rowspan="3" style="text-align: center">实发工资</td>
                      <td rowspan="3" style="text-align: center">社保单位<br/>承担部分<br/>(五险)</td>
                      <td rowspan="3" style="text-align: center">服务费</td>
                      <td rowspan="3" style="text-align: center">费用合计</td>
                  </tr>
                    <tr style="height: 10%">
                        <td rowspan="2" style="text-align: center">基础绩效</td>
                        <td colspan="2" style="text-align: center">奖励绩效</td>
                        <td rowspan="2" style="text-align: center">子女教育</td>
                        <td rowspan="2" style="text-align: center">住房租金</td>
                        <td rowspan="2" style="text-align: center">住房贷款</td>
                        <td rowspan="2" style="text-align: center">赡养老人</td>
                        <td rowspan="2" style="text-align: center">继续教育</td>
                    </tr>
                    <tr style="height: 10%">
                        <td style="text-align: center">岗位津贴</td>
                        <td style="text-align: center">业绩奖励</td>
                        <td style="text-align: center">通讯补</td>
                        <td style="text-align: center">值班费</td>
                        <td style="text-align: center">外勤补助</td>
                        <td style="text-align: center">兼岗</td>
                    </tr>
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        <tr >
                            <td style="text-align: center">${status.index+1}</td>
                            <td style="text-align: center">${da.bm191212000001}</td>
                            <td style="text-align: center">${da.xm191212000001}</td>
                            <td style="text-align: center">${da.jcjx1912120001}</td>
                            <td style="text-align: center">${da.gwjt1912120001}</td>
                            <td style="text-align: center">${da.yjjl1912120001}</td>
                            <td style="text-align: center">${da.txb19121200001}</td>
                            <td style="text-align: center">${da.zbf19121200001}</td>
                            <td style="text-align: center">${da.wqbz1912120001}</td>
                            <td style="text-align: center">${da.jg191212000001}</td>
                            <td style="text-align: center">${da.bfgz1912120001}</td>
                            <td style="text-align: center">${da.kfgz1912120001}</td>
                            <td style="text-align: center">${da.yfhj1912120001}</td>
                            <td style="text-align: center">${da.dkbx1912120001}</td>
                            <td style="text-align: center">${da.znjy1912120001}</td>
                            <td style="text-align: center">${da.zfjj1912120001}</td>
                            <td style="text-align: center">${da.zfdk1912120001}</td>
                            <td style="text-align: center">${da.sylr1912120001}</td>
                            <td style="text-align: center">${da.jxjy1912120001}</td>
                            <td style="text-align: center">${da.bysjg191212001}</td>
                            <td style="text-align: center">${da.sfgz1912120001}</td>
                            <td style="text-align: center">${da.sbdwc191212001}</td>
                            <td style="text-align: center">${da.fwf19121200001}</td>
                            <td style="text-align: center">${da.fyhj1912120001}</td>
                        </tr>
                    </c:forEach>

                </table>
                <table style="width: 100%;font-size:13px;padding-top: 5%" border="1" cellspacing="0">
                    <tr style="height: 10%">
                        <td rowspan="2" style="text-align: center">序号</td>
                        <td rowspan="2" style="text-align: center">部门</td>
                        <td rowspan="2" style="text-align: center">姓名</td>
                        <td rowspan="2" style="text-align: center">身份证号</td>
                        <td rowspan="2" style="text-align: center">岗位工资</td>
                        <td rowspan="2" style="text-align: center">薪级工资</td>
                        <td rowspan="2" style="text-align: center">基础绩效</td>
                        <td rowspan="2" style="text-align: center">妇女卫<br/>生费</td>
                        <td rowspan="2" style="text-align: center">通讯<br/>补贴</td>
                        <td rowspan="2" style="text-align: center">值班费</td>
                        <td rowspan="2" style="text-align: center">个人交原单<br/>位缴纳保险<br/>费</td>
                        <td rowspan="2" style="text-align: center">技术委员<br/>基础薪酬</td>
                        <td rowspan="2" style="text-align: center">兼岗</td>
                        <td rowspan="2" style="text-align: center">奖励绩效</td>
                        <td rowspan="2" style="text-align: center">补发<br/>工资</td>
                        <td rowspan="2" style="text-align: center">扣发<br/>工资</td>
                        <td rowspan="2" style="text-align: center">应发<br/>合计</td>
                        <td rowspan="1"colspan="5" style="text-align: center">个人所得税专项附加扣除</td>
                        <td rowspan="2" style="text-align: center">本月实交<br/>个税</td>
                        <td rowspan="2" style="text-align: center">实发工资</td>
                        <td rowspan="2" style="text-align: center">社保单位<br/>承担部分<br/>(五险)</td>
                        <td rowspan="2" style="text-align: center">服务费</td>
                        <td rowspan="2" style="text-align: center">费用合计</td>
                    </tr>
                    <tr style="height: 10%">
                        <td rowspan="1" style="text-align: center">子女教育</td>
                        <td rowspan="1" style="text-align: center">住房租金</td>
                        <td rowspan="1" style="text-align: center">住房贷款</td>
                        <td rowspan="1" style="text-align: center">赡养老人</td>
                        <td rowspan="1" style="text-align: center">继续教育</td>
                    </tr>
                    <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                        <tr >
                            <td style="text-align: center">${status.index+1}</td>
                            <td style="text-align: center">${da.bm191212000002}</td>
                            <td style="text-align: center">${da.xm191212000002}</td>
                            <td style="text-align: center">111111</td>
                            <td style="text-align: center">${da.gwgz1912120001}</td>
                            <td style="text-align: center">${da.xjgz1912120001}</td>
                            <td style="text-align: center">${da.jcjx1912120002}</td>
                            <td style="text-align: center">${da.fnwsf191212001}</td>
                            <td style="text-align: center">${da.txbt1912120001}</td>
                            <td style="text-align: center">${da.zbf19121200002}</td>
                            <td style="text-align: center">${da.grjyd191212001}</td>
                            <td style="text-align: center">${da.jswyj191212001}</td>
                            <td style="text-align: center">${da.jg191212000002}</td>
                            <td style="text-align: center">${da.jljx1912120001}</td>
                            <td style="text-align: center">${da.bfgz1912120002}</td>
                            <td style="text-align: center">${da.kfgz1912120002}</td>
                            <td style="text-align: center">${da.yfhj1912120002}</td>
                            <td style="text-align: center">${da.znjy1912120002}</td>
                            <td style="text-align: center">${da.zfzj1912120001}</td>
                            <td style="text-align: center">${da.zfdk1912120002}</td>
                            <td style="text-align: center">${da.sylr1912120002}</td>
                            <td style="text-align: center">${da.jxjy1912120002}</td>
                            <td style="text-align: center">${da.bysjg191212002}</td>
                            <td style="text-align: center">${da.sfgz1912120002}</td>
                            <td style="text-align: center">${da.sbdwc191212002}</td>
                            <td style="text-align: center">${da.fwf19121200002}</td>
                            <td style="text-align: center">${da.fyhj1912120002}</td>
                        </tr>
                    </c:forEach>
                </table>
                <div style="solid:1px ;padding-top: 2%;"><span style="padding-left: 8%">批准：${ldm}</span><span style="padding-left: 8%">审核：${zhl}</span><span style="padding-left: 8%">制表：${lhn}</span></div>

            </div>
            <div id="Bdiv" style="padding-bottom: 8%">
                <table style="width: 100%;height: 30%;font-size:18px;" border="1" cellspacing="0">
                    <tr>
                        <td colspan="4" style="text-align: left"><br/><h3>${ym}月份人员工资审批表</h3></td>
                    </tr>
                    <tr >
                        <td style="width: 10%;text-align: center">B公司</td>
                        <td style="width: 20%;text-align: left">合计支付：${data.get(6).get(1).get("summoneyB")}</td>
                        <td style="width: 65%;text-align: left">人数：${data.get(6).get(1).get("numB")}</td>

                    </tr>
                </table>
                <table style="width: 100%;font-size:13px;" border="1" cellspacing="0">
                    <tr style="height: 10%">
                        <td rowspan="3" style="text-align: center">序号</td>
                        <td rowspan="3" style="text-align: center">部门</td>
                        <td rowspan="3" style="text-align: center">姓名</td>
                        <td colspan="3" style="text-align: center">绩效工资</td>
                        <td colspan="4" rowspan="2" style="text-align: center">津补贴</td>
                        <td rowspan="3" style="text-align: center">补发工资</td>
                        <td rowspan="3" style="text-align: center">扣发工资</td>
                        <td rowspan="3" style="text-align: center">应发<br/>工资</td>
                        <td rowspan="3" style="text-align: center">代扣个人<br/>保险</td>
                        <td rowspan="1"colspan="5" style="text-align: center">个人所得税专项附加扣除</td>
                        <td rowspan="3" style="text-align: center">本月实交<br/>个税</td>
                        <td rowspan="3" style="text-align: center">实发工资</td>
                        <td rowspan="3" style="text-align: center">社保单位<br/>承担部分<br/>(五险)</td>
                        <td rowspan="3" style="text-align: center">服务费</td>
                        <td rowspan="3" style="text-align: center">费用合计</td>
                    </tr>
                    <tr style="height: 10%">
                        <td rowspan="2" style="text-align: center">基础绩效</td>
                        <td colspan="2" style="text-align: center">奖励绩效</td>
                        <td rowspan="2" style="text-align: center">子女教育</td>
                        <td rowspan="2" style="text-align: center">住房租金</td>
                        <td rowspan="2" style="text-align: center">住房贷款</td>
                        <td rowspan="2" style="text-align: center">赡养老人</td>
                        <td rowspan="2" style="text-align: center">继续教育</td>
                    </tr>
                    <tr style="height: 10%">
                        <td style="text-align: center">岗位津贴</td>
                        <td style="text-align: center">业绩奖励</td>
                        <td style="text-align: center">通讯补</td>
                        <td style="text-align: center">值班费</td>
                        <td style="text-align: center">外勤补助</td>
                        <td style="text-align: center">兼岗</td>
                    </tr>
                    <c:forEach items="${data.get(2)}" var="da" varStatus="status">
                        <tr>
                            <td style="text-align: center">${status.index+1}</td>
                            <td style="text-align: center">${da.bm191212000001}</td>
                            <td style="text-align: center">${da.xm191212000001}</td>
                            <td style="text-align: center">${da.jcjx1912120001}</td>
                            <td style="text-align: center">${da.gwjt1912120001}</td>
                            <td style="text-align: center">${da.yjjl1912120001}</td>
                            <td style="text-align: center">${da.txb19121200001}</td>
                            <td style="text-align: center">${da.zbf19121200001}</td>
                            <td style="text-align: center">${da.wqbz1912120001}</td>
                            <td style="text-align: center">${da.jg191212000001}</td>
                            <td style="text-align: center">${da.bfgz1912120001}</td>
                            <td style="text-align: center">${da.kfgz1912120001}</td>
                            <td style="text-align: center">${da.yfhj1912120001}</td>
                            <td style="text-align: center">${da.dkbx1912120001}</td>
                            <td style="text-align: center">${da.znjy1912120001}</td>
                            <td style="text-align: center">${da.zfjj1912120001}</td>
                            <td style="text-align: center">${da.zfdk1912120001}</td>
                            <td style="text-align: center">${da.sylr1912120001}</td>
                            <td style="text-align: center">${da.jxjy1912120001}</td>
                            <td style="text-align: center">${da.bysjg191212001}</td>
                            <td style="text-align: center">${da.sfgz1912120001}</td>
                            <td style="text-align: center">${da.sbdwc191212001}</td>
                            <td style="text-align: center">${da.fwf19121200001}</td>
                            <td style="text-align: center">${da.fyhj1912120001}</td>
                        </tr>
                    </c:forEach>
                </table>
                <table style="width: 100%;font-size:13px;padding-top: 5%" border="1" cellspacing="0">
                    <tr style="height: 10%">
                        <td rowspan="2" style="text-align: center">序号</td>
                        <td rowspan="2" style="text-align: center">部门</td>
                        <td rowspan="2" style="text-align: center">姓名</td>
                        <td rowspan="2" style="text-align: center">身份证号</td>
                        <td rowspan="2" style="text-align: center">岗位工资</td>
                        <td rowspan="2" style="text-align: center">薪级工资</td>
                        <td rowspan="2" style="text-align: center">基础绩效</td>
                        <td rowspan="2" style="text-align: center">妇女卫<br/>生费</td>
                        <td rowspan="2" style="text-align: center">通讯<br/>补贴</td>
                        <td rowspan="2" style="text-align: center">值班费</td>
                        <td rowspan="2" style="text-align: center">个人交原单<br/>位缴纳保险<br/>费</td>
                        <td rowspan="2" style="text-align: center">技术委员<br/>基础薪酬</td>
                        <td rowspan="2" style="text-align: center">兼岗</td>
                        <td rowspan="2" style="text-align: center">奖励绩效</td>
                        <td rowspan="2" style="text-align: center">补发<br/>工资</td>
                        <td rowspan="2" style="text-align: center">扣发<br/>工资</td>
                        <td rowspan="2" style="text-align: center">应发<br/>合计</td>
                        <td rowspan="1"colspan="5" style="text-align: center">个人所得税专项附加扣除</td>
                        <td rowspan="2" style="text-align: center">本月实交<br/>个税</td>
                        <td rowspan="2" style="text-align: center">实发工资</td>
                        <td rowspan="2" style="text-align: center">社保单位<br/>承担部分<br/>(五险)</td>
                        <td rowspan="2" style="text-align: center">服务费</td>
                        <td rowspan="2" style="text-align: center">费用合计</td>
                    </tr>
                    <tr style="height: 10%">
                        <td rowspan="1" style="text-align: center">子女教育</td>
                        <td rowspan="1" style="text-align: center">住房租金</td>
                        <td rowspan="1" style="text-align: center">住房贷款</td>
                        <td rowspan="1" style="text-align: center">赡养老人</td>
                        <td rowspan="1" style="text-align: center">继续教育</td>
                    </tr>
                    <c:forEach items="${data.get(3)}" var="da" varStatus="status">
                        <tr>
                            <td style="text-align: center">${status.index+1}</td>
                            <td style="text-align: center">${da.bm191212000002}</td>
                            <td style="text-align: center">${da.xm191212000002}</td>
                            <td style="text-align: center">111111</td>
                            <td style="text-align: center">${da.gwgz1912120001}</td>
                            <td style="text-align: center">${da.xjgz1912120001}</td>
                            <td style="text-align: center">${da.jcjx1912120002}</td>
                            <td style="text-align: center">${da.fnwsf191212001}</td>
                            <td style="text-align: center">${da.txbt1912120001}</td>
                            <td style="text-align: center">${da.zbf19121200002}</td>
                            <td style="text-align: center">${da.grjyd191212001}</td>
                            <td style="text-align: center">${da.jswyj191212001}</td>
                            <td style="text-align: center">${da.jg191212000002}</td>
                            <td style="text-align: center">${da.jljx1912120001}</td>
                            <td style="text-align: center">${da.bfgz1912120002}</td>
                            <td style="text-align: center">${da.kfgz1912120002}</td>
                            <td style="text-align: center">${da.yfhj1912120002}</td>
                            <td style="text-align: center">${da.znjy1912120002}</td>
                            <td style="text-align: center">${da.zfzj1912120001}</td>
                            <td style="text-align: center">${da.zfdk1912120002}</td>
                            <td style="text-align: center">${da.sylr1912120002}</td>
                            <td style="text-align: center">${da.jxjy1912120002}</td>
                            <td style="text-align: center">${da.bysjg191212002}</td>
                            <td style="text-align: center">${da.sfgz1912120002}</td>
                            <td style="text-align: center">${da.sbdwc191212002}</td>
                            <td style="text-align: center">${da.fwf19121200002}</td>
                            <td style="text-align: center">${da.fyhj1912120002}</td>
                        </tr>
                    </c:forEach>

                </table>
                <div style="solid:1px ;padding-top: 2%;"><span style="padding-left: 8%">批准：${ldm}</span><span style="padding-left: 8%">审核：${zhl}</span><span style="padding-left: 8%">制表：${lhn}</span></div>

            </div>
            <div id="lwxydiv" style="padding-bottom: 8%">
                <table style="width: 100%;height: 30%;font-size:18px;" border="1" cellspacing="0">
                    <tr>
                        <td colspan="4" style="text-align: left"><br/><h3>${ym}月份人员工资审批表</h3></td>
                    </tr>
                    <tr >
                        <td style="width: 10%;text-align: center">劳务协议</td>
                        <td style="width: 20%;text-align: left">合计支付：${data.get(6).get(2).get("summoney")}</td>
                        <td style="width: 65%;text-align: left;"colspan="2">人数：${data.get(6).get(2).get("num")}</td>

                    </tr>
                </table>
                <table style="width: 100%; font-size:13px;" border="1" cellspacing="0">
                    <tr style="height: 10%">
                        <td rowspan="3" style="text-align: center">序号</td>
                        <td rowspan="3" style="text-align: center">部门</td>
                        <td rowspan="3" style="text-align: center">姓名</td>
                        <td colspan="3" style="text-align: center">绩效工资</td>
                        <td colspan="4" rowspan="2" style="text-align: center">津补贴</td>
                        <td rowspan="3" style="text-align: center">补发工资</td>
                        <td rowspan="3" style="text-align: center">扣发工资</td>
                        <td rowspan="3" style="text-align: center">应发<br/>工资</td>
                        <td rowspan="3" style="text-align: center">代扣个人<br/>保险</td>
                        <td rowspan="1"colspan="5" style="text-align: center">个人所得税专项附加扣除</td>
                        <td rowspan="3" style="text-align: center">本月实交<br/>个税</td>
                        <td rowspan="3" style="text-align: center">实发工资</td>
                        <td rowspan="3" style="text-align: center">社保单位<br/>承担部分<br/>(五险)</td>
                        <td rowspan="3" style="text-align: center">服务费</td>
                        <td rowspan="3" style="text-align: center">费用合计</td>
                    </tr>
                    <tr style="height: 10%">
                        <td rowspan="2" style="text-align: center">基础绩效</td>
                        <td colspan="2" style="text-align: center">奖励绩效</td>
                        <td rowspan="2" style="text-align: center">子女教育</td>
                        <td rowspan="2" style="text-align: center">住房租金</td>
                        <td rowspan="2" style="text-align: center">住房贷款</td>
                        <td rowspan="2" style="text-align: center">赡养老人</td>
                        <td rowspan="2" style="text-align: center">继续教育</td>
                    </tr>
                    <tr style="height: 10%">
                        <td style="text-align: center">岗位津贴</td>
                        <td style="text-align: center">业绩奖励</td>
                        <td style="text-align: center">通讯补</td>
                        <td style="text-align: center">值班费</td>
                        <td style="text-align: center">外勤补助</td>
                        <td style="text-align: center">兼岗</td>
                    </tr>
                    <c:forEach items="${data.get(4)}" var="da" varStatus="status">
                        <tr>
                            <td style="text-align: center">${status.index+1}</td>
                            <td style="text-align: center">${da.bm191212000001}</td>
                            <td style="text-align: center">${da.xm191212000001}</td>
                            <td style="text-align: center">${da.jcjx1912120001}</td>
                            <td style="text-align: center">${da.gwjt1912120001}</td>
                            <td style="text-align: center">${da.yjjl1912120001}</td>
                            <td style="text-align: center">${da.txb19121200001}</td>
                            <td style="text-align: center">${da.zbf19121200001}</td>
                            <td style="text-align: center">${da.wqbz1912120001}</td>
                            <td style="text-align: center">${da.jg191212000001}</td>
                            <td style="text-align: center">${da.bfgz1912120001}</td>
                            <td style="text-align: center">${da.kfgz1912120001}</td>
                            <td style="text-align: center">${da.yfhj1912120001}</td>
                            <td style="text-align: center">${da.dkbx1912120001}</td>
                            <td style="text-align: center">${da.znjy1912120001}</td>
                            <td style="text-align: center">${da.zfjj1912120001}</td>
                            <td style="text-align: center">${da.zfdk1912120001}</td>
                            <td style="text-align: center">${da.sylr1912120001}</td>
                            <td style="text-align: center">${da.jxjy1912120001}</td>
                            <td style="text-align: center">${da.bysjg191212001}</td>
                            <td style="text-align: center">${da.sfgz1912120001}</td>
                            <td style="text-align: center">${da.sbdwc191212001}</td>
                            <td style="text-align: center">${da.fwf19121200001}</td>
                            <td style="text-align: center">${da.fyhj1912120001}</td>
                        </tr>
                    </c:forEach>


                </table>
                <table style="width: 100%; font-size:13px;padding-top: 5%" border="1" cellspacing="0">
                    <tr style="height: 10%">
                        <td rowspan="2" style="text-align: center">序号</td>
                        <td rowspan="2" style="text-align: center">部门</td>
                        <td rowspan="2" style="text-align: center">姓名</td>
                        <td rowspan="2" style="text-align: center">身份证号</td>
                        <td rowspan="2" style="text-align: center">岗位工资</td>
                        <td rowspan="2" style="text-align: center">薪级工资</td>
                        <td rowspan="2" style="text-align: center">基础绩效</td>
                        <td rowspan="2" style="text-align: center">妇女卫<br/>生费</td>
                        <td rowspan="2" style="text-align: center">通讯<br/>补贴</td>
                        <td rowspan="2" style="text-align: center">值班费</td>
                        <td rowspan="2" style="text-align: center">个人交原单<br/>位缴纳保险<br/>费</td>
                        <td rowspan="2" style="text-align: center">技术委员<br/>基础薪酬</td>
                        <td rowspan="2" style="text-align: center">兼岗</td>
                        <td rowspan="2" style="text-align: center">奖励绩效</td>
                        <td rowspan="2" style="text-align: center">补发<br/>工资</td>
                        <td rowspan="2" style="text-align: center">扣发<br/>工资</td>
                        <td rowspan="2" style="text-align: center">应发<br/>合计</td>
                        <td rowspan="1"colspan="5" style="text-align: center">个人所得税专项附加扣除</td>
                        <td rowspan="2" style="text-align: center">本月实交<br/>个税</td>
                        <td rowspan="2" style="text-align: center">实发工资</td>
                        <td rowspan="2" style="text-align: center">社保单位<br/>承担部分<br/>(五险)</td>
                        <td rowspan="2" style="text-align: center">服务费</td>
                        <td rowspan="2" style="text-align: center">费用合计</td>
                    </tr>
                    <tr style="height: 10%">
                        <td rowspan="1" style="text-align: center">子女教育</td>
                        <td rowspan="1" style="text-align: center">住房租金</td>
                        <td rowspan="1" style="text-align: center">住房贷款</td>
                        <td rowspan="1" style="text-align: center">赡养老人</td>
                        <td rowspan="1" style="text-align: center">继续教育</td>
                    </tr>
                    <c:forEach items="${data.get(5)}" var="da" varStatus="status">
                        <tr>
                            <td style="text-align: center">${status.index+1}</td>
                            <td style="text-align: center">${da.bm191212000002}</td>
                            <td style="text-align: center">${da.xm191212000002}</td>
                            <td style="text-align: center">111111</td>
                            <td style="text-align: center">${da.gwgz1912120001}</td>
                            <td style="text-align: center">${da.xjgz1912120001}</td>
                            <td style="text-align: center">${da.jcjx1912120002}</td>
                            <td style="text-align: center">${da.fnwsf191212001}</td>
                            <td style="text-align: center">${da.txbt1912120001}</td>
                            <td style="text-align: center">${da.zbf19121200002}</td>
                            <td style="text-align: center">${da.grjyd191212001}</td>
                            <td style="text-align: center">${da.jswyj191212001}</td>
                            <td style="text-align: center">${da.jg191212000002}</td>
                            <td style="text-align: center">${da.jljx1912120001}</td>
                            <td style="text-align: center">${da.bfgz1912120002}</td>
                            <td style="text-align: center">${da.kfgz1912120002}</td>
                            <td style="text-align: center">${da.yfhj1912120002}</td>
                            <td style="text-align: center">${da.znjy1912120002}</td>
                            <td style="text-align: center">${da.zfzj1912120001}</td>
                            <td style="text-align: center">${da.zfdk1912120002}</td>
                            <td style="text-align: center">${da.sylr1912120002}</td>
                            <td style="text-align: center">${da.jxjy1912120002}</td>
                            <td style="text-align: center">${da.bysjg191212002}</td>
                            <td style="text-align: center">${da.sfgz1912120002}</td>
                            <td style="text-align: center">${da.sbdwc191212002}</td>
                            <td style="text-align: center">${da.fwf19121200002}</td>
                            <td style="text-align: center">${da.fyhj1912120002}</td>
                        </tr>
                    </c:forEach>
                </table>
                <div style="solid:1px ;padding-top: 2%;"><span style="padding-left: 8%">批准：${ldm}</span><span style="padding-left: 8%">审核：${zhl}</span><span style="padding-left: 8%">制表：${lhn}</span></div>

            </div>
        </div>
        <!--endprint1-->

</body>
</html>

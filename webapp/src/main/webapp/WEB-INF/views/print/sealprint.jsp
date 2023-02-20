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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
        $("#sealdiv").wordExport('用印审批单')
    }


</script>
<html>
<head>
    <title>印章打印页</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick=preview(1)>
    <input type=button name='button_export' value='导出excel' onclick="exportexcelseal('sealdiv')">
    <input type=button name='button_export' value='导出word' onclick="exportwordseal();">
</div>
<div id="sealdiv" style="width: 756px;height: 1086px;">
<!--startprint1-->
<table style="width: 756px;height: 200px">
    <tr>
        <td style="width: 756px;font-size:30px;height:2%" align='center' valign='middle' colspan="4">河北省产品质量监督检验研究院用印审批单
        </td>
    </tr>
</table>
<table border="1" cellspacing="0" style="width: 756px;height: 806px">
    <tr style="height: 10%">
        <td style="width: 25%;text-align: center">用印部门/人</td>
        <td style="width: 25%;text-align: center" colspan="2">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get("peo")} ${da.get("dept")}
                </c:forEach>
            </c:if>
        </td>
        <td style="width: 25%; text-align: center">经办人</td>
        <td style="width: 25%; text-align: center" colspan="2">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get("peo")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 8%">
        <td style="width: 20%;text-align: center">文件标题或事由</td>
        <td style="width: 80%;text-align: center" colspan="5">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get("descsm")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 12%">
        <td style="width: 10%;text-align: center">印别</td>
        <td style="width: 15%;text-align: center">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get("sort")}
                </c:forEach>
            </c:if>
        </td>
        <td style="width: 15%; text-align: center">时间</td>
        <td style="width: 15%; text-align: center">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get("yztime")}
                </c:forEach>
            </c:if>
        </td>
        <td style="width: 15%; text-align: center">份数</td>
        <td style="width: 15%; text-align: center">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get("num")}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 10%">
        <td style="width: 20%;text-align: center">用印部门意见</td>
        <td style="width: 80%;text-align: center" colspan="5">
            <c:if test="${data.get(1).size()>0 && data.get(1) != null}">
                <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                    <div class="msg-box">
                        <label class="title">${da.get("yz01")}</label>
                        <label class="name">${da.get("yzname")}</label>
                        <label class="time">${da.get("yztime")}</label>
                    </div>
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height:15%">
        <td style="width: 20%;text-align: center">分管院领导批示</td>
        <td style="width: 80%;text-align: center" colspan="5">
            <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                <c:forEach items="${data.get(2)}" var="da" varStatus="status">
                    <div class="msg-box">
                        <label class="title">${da.get("fgydesc")}</label>
                        <label class="name">${da.get("fgyname")}</label>
                        <label class="time">${da.get("fgytime")}</label>
                    </div>
                </c:forEach>
            </c:if>
            <%--<c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                <c:forEach items="${data.get(3)}" var="da" varStatus="status">
                    <div class="msg-box">
                        <label class="title">${da.get("bgsdesc")}</label>
                        <label class="name">${da.get("bgsname")}</label>
                        <label class="time">${da.get("bgstime")}</label>
                    </div>
                </c:forEach>
            </c:if>--%>
        </td>
    </tr>
    <tr style="height:15%">
        <td style="width: 20%;text-align: center">院长批示</td>
        <td style="width: 80%;text-align: center" colspan="5">
            <c:if test="${data.get(4).size()>0 && data.get(4) != null}">
                <c:forEach items="${data.get(4)}" var="da" varStatus="status">
                    <div class="msg-box">
                        <label class="title">${da.get("yzdesc")}</label>
                        <label class="name">${da.get("yzname")}</label>
                        <label class="time">${da.get("yzdid")}</label>
                    </div>
                </c:forEach>
            </c:if>
        </td>
    </tr>
	   <tr style="height:15%">
        <td style="width: 20%;text-align: center">书记批示</td>
        <td style="width: 80%;text-align: center" colspan="5">
            <c:if test="${data.get(5).size()>0 && data.get(5) != null}">
                <c:forEach items="${data.get(5)}" var="da" varStatus="status">
                    <div class="msg-box">
                        <label class="title">${da.get("sjdesc")}</label>
                        <label class="name">${da.get("sjname")}</label>
                        <label class="time">${da.get("sjdid")}</label>
                    </div>
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr style="height: 30%">
        <td style="width: 20%;text-align: center">备注</td>
        <td style="width: 80%;text-align: left" colspan="5">
            <p>1.对上级机关行文、对外交流合作、涉及三重一大等重要事项用印须由书记签字批准；重要合同用印需经法律顾问把关签字及院党委会研究决定后,办公室盖章。</p>
            <p>2、涉及国抽、省抽的文件材料及标书类文件用印由部门负责人、分管院领导、院长逐级审批后，办公室盖章。</p>
            <p>3、院级资质、法人证书使用，个人职称评审、申请、证明、介绍信等事项用印由部门负责人签字，分管院领导审批后,办公室盖章。</p>
        </td>
    </tr>
</table>
<!--endprint1-->
</div>
</body>
</html>

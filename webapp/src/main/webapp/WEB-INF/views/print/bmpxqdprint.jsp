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

<html>
<head>
    <title>培训签到表</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" type="text/css" href="/zjyoa-pc/resources/css/print.css"/>
    <style>
        a{

            text-decoration: none;
        }
        a:link {
            color: #297DF0;
        }
        a:visited {
            color: #297DF0;
        }
        a:hover {
            color: #E95800;
        }
    </style>
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
    <script type="text/javascript" src="/zjyoa-pc/resources/js/printword/FileSaver.js"></script>
    <script type="text/javascript" src="/zjyoa-pc/resources/js/printword/jquery.wordexport.js"></script>
    <script type="text/javascript">

        function preview(oper) {
            if (oper < 10) {
                $(".sign-button").hide();
                bdhtml = window.document.body.innerHTML;
                sprnstr = "<!--startprint" + oper + "-->";
                eprnstr = "<!--endprint" + oper + "-->";
                prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 18);
                prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));
                window.document.body.innerHTML = prnhtml;
                window.print();
                window.document.body.innerHTML = bdhtml;
                $(".sign-button").show();
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
            $("#sealdiv").wordExport('培训签到表')
        }

        function tzrqdd(){
            var pxrq = $("#pxrq").val();
            var pxdd = $("#pxdd").val();
            $("#tdpxrq").text(pxrq);
            $("#tdpxdd").text(pxdd);
        }

    </script>
</head>
<body>

<div>
    调整培训日期：<input type="date" id="pxrq" value=""/>
    调整培训地点：<input type="text" id="pxdd" value=""/>
    <a href="javascript:void(0)" style="padding-right: 5%" onclick="tzrqdd()">点击更改</a>
    <input type=button name='button_export' value='打印'  onclick=preview(1)>
</div>
<script langguage="JavaScript">
    $(document).ready(function () {
        var time = new Date();
        var day = ("0" + time.getDate()).slice(-2);
        var month = ("0" + (time.getMonth() + 1)).slice(-2);
        var today = time.getFullYear() + "-" + (month) + "-" + (day);
        $('#pxrq').val(today);
    })
</script>
<div id="sealdiv" style="width: 756px;height: 1086px;">
<!--startprint1-->
<table style="width: 756px;height: 100px">
    <tr>
        <td style="width: 756px;font-size:15px;height:30px" align='left' valign='middle' colspan="4">ZJY-GS-602Be-2019/0
        </td>
    </tr>
    <tr>
        <td style="width: 756px;font-size:30px;height:70px" align='center' valign='middle' colspan="4">培训签到表
        </td>
    </tr>
</table>
<table border="1" cellspacing="0" style="width: 100%;height: 50%" >
<tr>
    <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
    <c:set var="recno" value="${data.get(0).get(0).get('recno')}"></c:set>
    </c:if>
    <td style="width: 100px;text-align: center" colspan="2">培训内容</td>
    <td style="width: 100px;text-align: center" colspan="2">
        <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
            <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                <s:dic type="dept" value="${da.get('xmnr')}"></s:dic>
            </c:forEach>
        </c:if>
    </td>
    <td style="width: 100px;text-align: center" colspan="2">培训计划编号</td>
    <td style="width: 100px;text-align: center" colspan="2">
        <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
            <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                ${da.get('pxjhbh')}
            </c:forEach>
        </c:if>
    </td>
</tr>
    <tr>
        <td style="width: 100px;text-align: center" colspan="2">部门</td>
        <td style="width: 100px;text-align: center" colspan="2">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get('bm')}
                </c:forEach>
            </c:if>
        </td>
        <td style="width: 100px;text-align: center" colspan="2">培训日期</td>
        <td style="width: 100px;text-align: center" colspan="2" id="tdpxrq">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    ${da.get('pxksr')}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr>
        <td style="width: 100px;text-align: center" colspan="2">授课教师</td>
        <td style="width: 100px;text-align: center" colspan="2">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    <s:dic type="dept" value="${da.get('skls')}"></s:dic>
                </c:forEach>
            </c:if>
        </td>
        <td style="width: 100px;text-align: center" colspan="2">培训地点</td>
        <td style="width: 100px;text-align: center" colspan="2" id="tdpxdd">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    地点：${da.get('dd')}
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr>
        <td style="width: 100px;text-align: center" colspan="2">培训对象</td>
        <td style="width: 100px;text-align: center" colspan="6">
            <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                    <s:dic type="dept" value="${da.get('pxdx')}"></s:dic>
                </c:forEach>
            </c:if>
        </td>
    </tr>
        <tr>
            <td style="width: 100px;text-align: center">序号</td>
            <td style="width: 100px;text-align: center">人员编号</td>
            <td style="width: 100px;text-align: center">姓名</td>
            <td style="width: 200px;text-align: center">签名</td>
            <td style="width: 100px;text-align: center">序号</td>
            <td style="width: 100px;text-align: center">人员编号</td>
            <td style="width: 100px;text-align: center">姓名</td>
            <td style="width: 200px;text-align: center">签名</td>
        </tr>

                <c:if test="${data.get(1).size()>0 && data.get(0) != null}">
                             <tr>
                    <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                                <td style="width: 100px;text-align: center">${status.index+1}</td>
                                <td style="width: 100px;text-align: center">A${status.index+1}</td>
                                <td style="width: 100px;text-align: center">${da.get('anothername')}</td>
                                <td style="width: 100px;text-align: center">
                                    <c:if test="${type==1}">
                                    <c:if test="${!fn:contains(data.get(2), da.get('name'))}">
                                        <p class="sign-button"><a href="javascript:void(0)" onclick="signIn(this,'${recno}','${da.get('name')}')">签到</a></p>
                                    </c:if>
                                    </c:if>
                                    <c:if test="${fn:contains(data.get(2), da.get('name'))}">
                                        <p><img style='width: 80px;height: 40px;' src='/zjyoa-pc/upload/signature/${da.get('name')}.png'></p>
                                    </c:if>
                                </td>
                        <c:if test="${(status.index+1)%2==0}">
                            </tr><tr>
                        </c:if>
                        <c:if test="${status.last&&(status.index+1)%2!=0}">
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </c:if>
                    </c:forEach>
                            </tr>
            </c:if>
    </table>
<!--endprint1-->
</div>
<script>
    function signIn(obj,recno,user){
        var pobj = $(obj).parent();
        $.ajax({
            url: "/zjyoa-pc/printall/bmpxqdsignin.do",
            type: 'post',
            dataType: 'json',
            data:{"recno":recno,"user":user},
            async:false,
            success: function(d, status){
                if(d.success==1){
                    obj.remove();
                    pobj.append("<img style='width: 80px;height: 40px;' src='/zjyoa-pc/upload/signature/"+user+".png'>");
                }
            }
        });
    }
</script>
</body>
</html>

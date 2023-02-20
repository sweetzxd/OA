<%--
  User: zxd
  Date: 2020/02/18
  Time: 下午 2:08
  Explain: 说明
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.springframework.util.Base64Utils" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.oa.core.util.SpringContextUtil" %>
<%@ page import="com.oa.core.service.util.TableService" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<%
    String decodeUrl = request.getParameter("url");
    if (decodeUrl != null) {
        String url = new String(Base64Utils.decode(decodeUrl.getBytes()));
        request.setAttribute("url", url);
    }
%>

<html>
<head>
    <title>培训实施记录表</title>
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
            $("#sealdiv").wordExport('培训实施记录表')
        }


    </script>
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick=preview(1)>
    <input type=button name='button_export' value='导出excel' onclick="exportexcelseal('sealdiv')">
    <input type=button name='button_export' value='导出word' onclick="exportwordseal();">
</div>
<div id="sealdiv" style="width: 756px;height: 1086px;">
    <!--startprint1-->
    <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
        <c:set var="recno" value="${data.get(0).get(0).get('recno')}"></c:set>
    </c:if>
    <table style="width: 756px;height: 100px">
        <tr>
            <td style="width: 756px;font-size:15px;height:30px" align='left' valign='middle' colspan="4">ZJY-GS-602Bd-2019/0
            </td>
        </tr>
        <tr>
            <td style="width: 756px;font-size:30px;height:70px" align='center' valign='middle' colspan="4">培训实施记录表
            </td>
        </tr>
    </table>

    <table border="1" cellspacing="0" style="width: 100%;height: 50%" >
        <tr style="height:50px;">
            <td style="width: 94px;text-align: center" colspan="1">培训内容</td>
            <td style="width: 282px;text-align: center" colspan="3">
                <c:if test="${data.get('data') != null}">
                    ${data.get('data').get("pxnr200218001")}
                </c:if>
            </td>
            <td style="width: 188px;text-align: center" colspan="2">培训计划编号</td>
            <td style="width: 188px;text-align: center" colspan="2">
                <c:if test="${data.get('data') != null}">
                    ${data.get('data').get("pxjhb20021802")}
                </c:if>
            </td>
        </tr>
        <tr style="height:50px;">
            <td style="width: 94px;text-align: center" colspan="1">部门</td>
            <td style="width: 188px;text-align: center" colspan="2">
                <c:if test="${data.get('data')!=null}">
                    ${data.get('data').get('bm')}
                </c:if>
            </td>
            <td style="width: 94px;text-align: center" colspan="1">培训日期</td>
            <td style="width: 188px;text-align: center" colspan="2">
                <c:if test="${data.get('data') != null}">
                    ${data.get('data').get("pxrq200218002")}
                </c:if>
            </td>
            <td style="width: 94px;text-align: center" colspan="1">地点</td>
            <td style="width: 94px;text-align: center" colspan="1">
                <c:if test="${data.get('data') != null}">
                    ${data.get('data').get("pxdd200218002")}
                </c:if>
            </td>
        </tr>
        <tr style="height:50px;">
            <td style="width: 94px;text-align: center" colspan="1">培训类别</td>
            <td style="width: 188px;text-align: center" colspan="2">
                <c:if test="${data.get('data') != null}">
                    ${data.get('data').get("pxlb200218002")}
                </c:if>
            </td>
            <td style="width: 94px;text-align: center" colspan="1">培训形式</td>
            <td style="width: 188px;text-align: center" colspan="2">
                <c:if test="${data.get('data') != null}">
                    ${data.get('data').get("pxxs200218003")}
                </c:if>
            </td>
            <td style="width: 94px;text-align: center" colspan="1">学时</td>
            <td style="width: 94px;text-align: center" colspan="1">
                <c:if test="${data.get('data') != null}">
                    ${data.get('data').get("xs20021800001")}
                </c:if>
            </td>
        </tr>
        <tr style="height:50px;">
            <td style="width: 94px;text-align: center" colspan="1">授课教师</td>
            <td style="width: 188px;text-align: center" colspan="2">
                <c:if test="${data.get('data') != null}">
                    ${data.get('data').get("skjs200218002")}
                </c:if>
            </td>
            <td style="width: 94px;text-align: center" colspan="1">工作单位</td>
            <td style="width: 376px;text-align: center" colspan="4">
                <c:if test="${data.get('data') != null}">
                    ${data.get('data').get("gzdw200218002")}
                </c:if>
            </td>
        </tr>
        <tr style="height:50px;">
            <td style="width: 94px;text-align: center" colspan="1">培训对象</td>
            <td style="width: 188px;text-align: center" colspan="2">
                <c:if test="${data.get('data') != null}">
                    ${data.get('data').get("pxdx200218002")}
                </c:if>
            </td>
            <td style="width: 94px;text-align: center" colspan="1">培训人员</td>
            <td style="width: 376px;text-align: center" colspan="4">
                <c:if test="${data.get('data') != null}">
                    ${data.get('data').get("pxry")}
                </c:if>
            </td>
        </tr>
        <tr style="height:180px;">
            <td style="position:relative;" colspan="8">
                <span style="position:absolute;left:10px;top:5px;">培训纪要：</span>
                <span style="position:absolute;left:50px;top:30px;padding-right:50px;">
                    <c:if test="${data.get('data') != null}">
                        ${data.get('data').get("pxjy200218002")}
                    </c:if>
                </span>
            </td>
        </tr>
        <tr style="height:50px;">
            <td style="width: 94px;text-align: center" colspan="1">应到人数</td>
            <td style="width: 94px;text-align: center" colspan="1">
                <c:if test="${data.get('data') != null}">
                    ${data.get('data').get("ydrs200218002")}
                </c:if>
            </td>
            <td style="width: 94px;text-align: center" colspan="1">实到人数</td>
            <td style="width: 94px;text-align: center" colspan="1">
                <c:if test="${data.get('data') != null}">
                    ${data.get('data').get("sdrs200218002")}
                </c:if>
            </td>
            <td style="width: 188px;text-align: center" colspan="2">缺席补习方式</td>
            <td style="width: 188px;text-align: center" colspan="2">
                <c:if test="${data.get('data') != null}">
                    ${data.get('data').get("qxbxf20021802")}
                </c:if>
            </td>
        </tr>
        <tr style="height:50px;">
            <td style="text-align: center" colspan="8">
                <span style="float:left;margin-left: 10px;">考核方式</span>
                <c:set var="string2" value="${fn:split(data.get('data').get('khfs200218002'), ',')}" />
                <c:set var="iscontain1" value="false" />
                <c:set var="iscontain2" value="false" />
                <c:set var="iscontain3" value="false" />
                <c:set var="iscontain4" value="false" />
                <c:set var="iscontain5" value="false" />
                <c:forEach var="item" items="${string2}">  
                    <c:if test="${item eq '笔试' and !iscontain1}">    
                        <c:set var="iscontain1" value="true" /> 
                    </c:if>
                    <c:if test="${item eq '操作' and !iscontain2}">    
                        <c:set var="iscontain2" value="true" /> 
                    </c:if>
                    <c:if test="${item eq '审核' and !iscontain3}">    
                        <c:set var="iscontain3" value="true" /> 
                    </c:if>
                    <c:if test="${item eq '评定' and !iscontain4}">    
                        <c:set var="iscontain4" value="true" /> 
                    </c:if>
                    <c:if test="${item eq '其它' and !iscontain5}">    
                        <c:set var="iscontain5" value="true" /> 
                    </c:if>
                </c:forEach>
                <span class="icon-box">
                    <c:choose>
                        <c:when test="${iscontain1}">
                            <i class="layui-icon layui-size">&#xe643;</i>
                        </c:when>
                        <c:otherwise>
                            <i class="layui-icon layui-size">&#xe63f;</i>
                        </c:otherwise>
                    </c:choose>
                </span>
                <span style="float:left;margin-left: 10px;">笔试</span>
                <span class="icon-box">
                    <c:choose>
                        <c:when test="${iscontain2}">
                            <i class="layui-icon layui-size">&#xe643;</i>
                        </c:when>
                        <c:otherwise>
                            <i class="layui-icon layui-size">&#xe63f;</i>
                        </c:otherwise>
                    </c:choose>
                </span>
                <span style="float:left;margin-left: 10px;">操作</span>
                <span class="icon-box">
                    <c:choose>
                        <c:when test="${iscontain3}">
                            <i class="layui-icon layui-size">&#xe643;</i>
                        </c:when>
                        <c:otherwise>
                            <i class="layui-icon layui-size">&#xe63f;</i>
                        </c:otherwise>
                    </c:choose>
                </span>
                <span style="float:left;margin-left: 10px;">审核</span>
                <span class="icon-box">
                    <c:choose>
                        <c:when  test="${iscontain4}" >
                            <i class="layui-icon layui-size">&#xe643;</i>
                        </c:when>
                        <c:otherwise>
                            <i class="layui-icon layui-size">&#xe63f;</i>
                        </c:otherwise>
                    </c:choose>
                </span>
                <span style="float:left;margin-left: 10px;">评定</span>
                <span class="icon-box">
                    <c:choose>
                        <c:when test="${iscontain5}">
                            <i class="layui-icon layui-size">&#xe643;</i>
                        </c:when>
                        <c:otherwise>
                            <i class="layui-icon layui-size">&#xe63f;</i>
                        </c:otherwise>
                    </c:choose>
                </span>
                <span style="float:left;margin-left: 10px;">其它</span>
                <span style="float:left;margin-left: 10px;"></span>
            </td>
        </tr>
        <tr style="height:180px;">
            <td style="position:relative;" colspan="8">
                <span style="position:absolute;left:10px;top:5px;">考核结论：</span>
                <span style="position:absolute;left:50px;top:30px;padding-right:50px;">
                    <c:if test="${data.get('khjl') != null}">
                        ${data.get('khjl').get("khjl200218001")}
                    </c:if>
                </span>
            </td>
        </tr>
        <tr style="height:260px;">
            <td style="position:relative;" colspan="8">
                <span style="position:absolute;left:10px;top:5px;">有效性评价：</span>
                <span style="position:absolute;left:50px;top:30px;padding-right:50px;">
                    <c:if test="${data.get('yxxpj') != null}">
                        ${data.get('yxxpj').get("yxxpj20021801")}
                    </c:if>
                </span>
                <span style="position:absolute;right:50px;bottom:30px;padding-right:50px;">
                    <c:if test="${data.get('yxxpj') != null}">
                        评价人：${data.get('yxxpj').get("pjr")}&nbsp;&nbsp;&nbsp;&nbsp;日期：<fmt:formatDate type="date" value="${data.get('yxxpj').get('modifyTime')}" />
                    </c:if>
                </span>
            </td>
        </tr>
    </table>
    <!--endprint1-->
</div>
<script>

</script>
</body>
</html>
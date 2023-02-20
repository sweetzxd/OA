<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<%--jspBegin--%>
<%--jspEnd--%>
<html>
<head>
    <title>办公用品申请详情</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <link rel="stylesheet" href="/zjyoa-pc/resources/css/tablebox.css" type="text/css"/>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>
    <script src="/zjyoa-pc/resources/js/system/manager.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>
    <script src="/zjyoa-pc/resources/js/system/flow.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
    <%--scriptBegin--%>
    <%--scriptEnd--%>
    <script type="text/javascript">
        let runIndex, newHtmlString;
        function starscript(pageid, tableid, formid) {
            <%--functionBegin--%>
            <%--functionEnd--%>
        }
        <%--jsBegin--%>
        <%--jsEnd--%>
    </script>
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
    </script>
</head>
<body onload="starscript('${pageid}','Officesuppdetails001','${formid}')">

<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-xs12 layui-col-sm12 layui-col-md12">
            <div class="layui-card layui-card-body">
                <div class="layui-container">
                    <c:if test="${type=='2'}">
                        <p><div style="font-size: 20px;padding-bottom: 30px"><b>${bmname} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ${offerdate}</b></div></p>
                        <h3>物品明细</h3>
                        <div id="sealdiv" style="width: 100%;height: 50%;padding-top: 20px;padding-bottom: 30px;">
                            <table lay-filter="parse-table" style="line-height: 200%; width: 100%"; border="1px">
                                <thead>
                                <tr>
                                    <th style="text-align: center;background-color:#0aa8e4;">序号</th>
                                    <th style="text-align: center;background-color:#0aa8e4;">物品类型</th>
                                    <th style="text-align: center;background-color:#0aa8e4;">物品名称</th>
                                    <th style="text-align: center;background-color:#0aa8e4;">单位</th>
                                    <th style="text-align: center;background-color:#0aa8e4;">数量</th>
                                    <th style="text-align: center;background-color:#0aa8e4;">备注</th>
                                </tr>
                                <c:if test="${!empty data}">
                                    <c:forEach items="${data}" var="field" varStatus="status">
                                        <tr style="text-align: center;">
                                            <td>${status.index+1}</td>
                                            <td>${field.wplxX220221002}</td>
                                            <td>${field.wpmcX220221001}</td>
                                            <td>${field.dwX22022100002}</td>
                                            <td>${field.slX22022100001}</td>
                                            <td>${field.bzX22022100003}</td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                                </thead>
                            </table>
                        </div>
                        <p><b>申请人:${sqr}</b></p>
                        <p><b>部门负责人: ${bmbz}</b></p>
                        <p><b>经办人: ${agentpeople}</b></p>
                        <p><b>申请时间: ${spdata}</b></p>
                        <p><b>审批完成时间: ${spenddata}</b></p>
                    </c:if>
                    <c:if test="${type=='1'}">
                        <input type=button name='button_export' value='&nbsp;打印&nbsp;'  onclick=preview(1)>
 <!--startprint1-->
                        <div id="sealdiv">
                            <div align="center" style="font-size: 20px;padding-top: 25px">办公用品申请表</div>
                            <span  style="padding-left: 5px;padding-bottom: 10px;">部门:${bmname}</span>
                            <span  style="padding-left: 65%;padding-bottom: 10px;">申请日期:${applydate}</span>

                            <table lay-filter="parse-table" style="line-height: 200%; width: 100%"; border="1px">
                                <thead>
                                <tr>
                                    <th style="text-align: center;">序号</th>
                                    <th style="text-align: center;">物品类型</th>
                                    <th style="text-align: center;">物品名称</th>
                                    <th style="text-align: center;">单位</th>
                                    <th style="text-align: center;">数量</th>
                                    <th style="text-align: center;">备注</th>
                                </tr>
                                <c:if test="${!empty data}">
                                    <c:forEach items="${data}" var="field" varStatus="status">
                                        <tr style="text-align: center;">
                                            <td>${status.index+1}</td>
                                            <td>${field.wplxX220221002}</td>
                                            <td>${field.wpmcX220221001}</td>
                                            <td>${field.dwX22022100002}</td>
                                            <td>${field.slX22022100001}</td>
                                            <td>${field.bzX22022100003}</td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                                </thead>
                            </table>
                            <span style="padding-left:5px;padding-top: 5px;">部门负责人:${bmbz}</span><span  style="padding-left: 70%;padding-top: 5px;">经办人:${agentpeople}</span>
                            <div align="left" style="padding-top: 5px">
                                <p>备注:1、各部门每月20日至月底前，根据每月需求填写此表,报办公室秦怀东或宋建培处。常用物品每月申领一次，特殊物品需提前告知办公室采购。</p>
                                <p style="padding-left: 32px"> 2、各部门每次申请需提交一份电子版，一份部门负责人、经办人签字纸质版，以便核算部门消耗。没有部门负责人签字，办公室不予办理。</p>
                            </div>
                        </div>
<!--endprint1-->
                    </c:if>
            </div>
        </div>
    </div>
</div>
<%--divBegin--%>
<%--divEnd--%>
</body>
</html>

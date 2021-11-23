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
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <script src="/resources/js/system/manager.js" charset="utf-8"></script>
    <script src="/resources/js/system/flow.js" type="text/javascript" charset="utf-8"></script>
    <%--scriptBegin--%>
    <%--scriptEnd--%>
    <script type="text/javascript">
        function starscript(pageid, tableid, formid) {
            formLoading(pageid, tableid, formid);
            mywkfprocnum('${wkflwId}','${starnode}');
            <%--functionBegin--%>
            <%--functionEnd--%>
        }
        <%--jsBegin--%>
        <%--jsEnd--%>
    </script>
</head>
<body onload="starscript('${pageid}','${tableid}','${formid}')">
<c:if test="${type=='modi' || type=='add'}">
    <fieldset class="layui-elem-field">
        <legend>${title}</legend>
        <div class="layui-container">
            <div class="layui-row">
                <div class="layui-col-xs10 layui-col-sm10 layui-col-md10">
                    <c:if test="${starnode=='true'}">
                        <div style="width: 100px; height: 30px;float: right;text-align: center;" class="layui-nav-item layui-hide-xs"><a href="javascript:void(0);" onclick="openflowsub('lct','${wkflwId}','${starnode}')">查看流程</a></div>
                        <div style="width: 100px; height: 30px;float: right;text-align: center;" class="layui-nav-item layui-hide-xs"><a href="javascript:void(0);" onclick="openflowsub('wc','${wkflwId}','${starnode}')">已完成  <span class="layui-badge wc" style="display: none"></span></a></div>
                        <div style="width: 100px; height: 30px;float: right;text-align: center;" class="layui-nav-item layui-hide-xs"><a href="javascript:void(0);" onclick="openflowsub('jx','${wkflwId}','${starnode}')">进行中  <span class="layui-badge jx" style="display: none"></span></a></div>
                    </c:if>
                    <c:if test="${starnode=='false'}">
                        <div style="width: 100px; height: 30px;float: right;text-align: center;" class="layui-nav-item layui-hide-xs"><a href="javascript:void(0);" onclick="openflowsub('wc','${wkflwId}','${starnode}')">已完成  <span class="layui-badge wc" style="display: none"></span></a></div>
                        <div style="width: 100px; height: 30px;float: right;text-align: center;" class="layui-nav-item layui-hide-xs"><a href="javascript:void(0);" onclick="openflowsub('jx','${wkflwId}','${starnode}')">进行中  <span class="layui-badge jx" style="display: none"></span></a></div>
                    </c:if>
                </div>
            </div>
            <div class="layui-row">
                <div class="layui-col-xs10 layui-col-sm10 layui-col-md10">
                    <form class="layui-form layui-form-pane" id="${formid}" action="/flowpage/flowaddsave/${formid}.do" method='POST'>
                        <div class="layui-container">
                            <c:if test="${!empty form}">
                                <c:forEach items="${form}" var="field" varStatus="status">
                                    ${field}
                                </c:forEach>
                                <div class='layui-row'>
                                    <div class='layui-col-xs12'>
                                        <div style='float:left; width:33%;text-align:center;'><a class='layui-btn layui-btn-radius' herf='javascript:void(0)' onclick="flowformsave(this,'${formid}')">保存</a></div>
                                        <div style='float:left; width:34%;text-align:center;'><a class='layui-btn layui-btn-radius' herf='javascript:void(0)' onclick="flowformreset(this,'${formid}')">重置</a></div>
                                        <div style='float:right;width:33%;text-align:center;'><a class='layui-btn layui-btn-radius' herf='javascript:void(0)' onclick="flowformcancel(this,'${formid}','${wkflwId}')">取消</a></div>
                                        <%--modiaBegin--%>
                                        <%--modiaEnd--%>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                        <input type='hidden' id='${tableName}_workflowProcID' name='${tableName}_workflowProcID' autocomplete='off' value='<c:if test="${type=='modi'}">${table.workflowProcID}</c:if><c:if test="${type=='add'}">${procId}</c:if>' class='layui-input'>
                        <input type='hidden' id='${tableName}_workflowNodeID' name='${tableName}_workflowNodeID' autocomplete='off' value='<c:if test="${type=='modi'}">${table.workflowNodeID}</c:if><c:if test="${type=='add'}">${nodeId}</c:if>' class='layui-input'>
                        <input type='hidden' id='workOrderNO' name='workOrderNO' autocomplete='off' value='${workOrderNO}' class='layui-input'>
                        <input type='hidden' id='wkflwId' name='wkflwId' autocomplete='off' value='${wkflwId}' class='layui-input'>
                        <input type='hidden' id='formid' name='formid' autocomplete='off' value='${formid}' class='layui-input'>
                        <%--modiFormBegin--%>
                        <%--modiFormEnd--%>
                    </form>
                </div>
            </div>
        </div>
    </fieldset>
</c:if>
<c:if test="${type=='info'}">
    <fieldset class="layui-elem-field">
        <legend>${title}</legend>
        <div class="layui-row">
            <div class="layui-col-xs10 layui-col-sm10 layui-col-md10">
                <form class="layui-form" id="${formid}_info" action="/task/flowtask.do" method='POST'>
                    <c:if test="${!empty form}">
                        <c:forEach items="${form}" var="field" varStatus="status">
                            ${field}
                        </c:forEach>
                    </c:if>
                    <div class='layui-row'>
                        <div class='layui-col-xs12'>
                            <div style='float:left; width:33%;text-align:center;'><a class='layui-btn layui-btn-radius' herf='javascript:void(0)' onclick="flowformsend(this,'${formid}')">发送</a></div>
                            <div style='float:left; width:34%;text-align:center;'><a class='layui-btn layui-btn-radius' herf='javascript:void(0)' onclick="flowformmodi(this,'${formid}')">修改</a></div>
                            <div style='float:right;width:33%;text-align:center;'><a class='layui-btn layui-btn-radius' herf='javascript:void(0)' onclick="flowformcancel(this,'${formid}','${wkflwId}')">取消</a></div>
                            <%--aBegin--%>
                            <%--aEnd--%>
                        </div>
                    </div>
                    <input type='hidden' name='recorderNO' autocomplete='off' value='${table.recorderNO}' class='layui-input'>
                    <input type='hidden' name='workflowProcID' autocomplete='off' value='${table.workflowProcID}' class='layui-input'>
                    <input type='hidden' name='workflowNodeID' autocomplete='off' value='${table.workflowNodeID}' class='layui-input'>
                    <input type='hidden' name='workOrderNO' autocomplete='off' value='${workOrderNO}' class='layui-input'>
                    <input type='hidden' name='wkflwId' autocomplete='off' value='${wkflwId}' class='layui-input'>
                    <input type='hidden' name='formid' autocomplete='off' value='${formid}' class='layui-input'>
                    <%--formBegin--%>
                    <%--formEnd--%>
                </form>
            </div>
        </div>
    </fieldset>
</c:if>
<%--divBegin--%>
<%--divEnd--%>
</body>
</html>

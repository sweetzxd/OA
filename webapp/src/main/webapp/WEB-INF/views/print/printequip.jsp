<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.springframework.util.Base64Utils" %>
<%@ page import="com.oa.core.service.util.TableService" %>
<%@ page import="com.oa.core.util.SpringContextUtil" %>
<%@ page import="java.util.Map" %>
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
<%--导出word引入的js--%>
<script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.js"></script>
<script type="text/javascript" src="/zjyoa-pc/resources/js/printword/FileSaver.js"></script>
<script type="text/javascript" src="/zjyoa-pc/resources/js/printword/jquery.wordexport.js"></script>
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" charset="utf-8"></script>
<script type="text/javascript">
    // 禁用右键默认行为
    document.oncontextmenu = function (evt) {
        evt.preventDefault();
    };

    // 属性返回false
    document.oncontextmenu = function (evt) {
        evt.returnValue = false;
    }

    // 或者直接返回整个事件
    document.oncontextmenu = function () {
        return false;
    }

    document.oncopy = function (evt) {
        evt.returnValue = false;
    }

    // 整个事件直接返回false
    document.oncopy = function () {
        return false;
    }

    document.onselectstart = function (evt) {
        evt.returnValue = false;
    }

    // 整个事件直接返回false
    document.onselectstart = function () {
        return false;
    }

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
<html>
<head>
    <title>设备维修审批表</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<body>
<div>
    <input type=button style="font-size: 18px" name='button_export' value=' 打印 ' onclick=preview(1)>
</div>
<div id="sealdiv" style="width:100%;height: 830px">
    <!--startprint1-->
    <table style="width: 850px;height: 20px;margin:auto">
        <tr>
            <td style="width: 756px;font-size:13px;height:5px;" align='left' valign='middle' colspan="4">
                ZJY-GS-604Cf/1
            </td>
        </tr>
        <tr>
            <td style="width: 756px;font-size:16px;height:15px;" align='center' valign='middle' colspan="4">设备维修/技术改造审批表</td>
        </tr>
    </table>
    <table border="1" cellspacing="0" style="width: 850px;height: 800px;margin:auto;table-layout:fixed;font-size: 13px">
        <tr style="height: 50px;">
            <td style="width: 200px;text-align: center">设备名称</td>
            <td style="width: 200px;text-align: center;word-wrap:break-word;word-break:break-all;">
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("sbmc")}
                    </c:forEach>
                </c:if>
            </td>
            <td style="width: 200px; text-align: center">院编号</td>
            <td style="width: 250px; text-align: center;word-wrap:break-word;word-break:break-all;" >
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("ybh")}
                    </c:forEach>
                </c:if></td>
        </tr>
        <tr style="height: 50px;">
            <td style="width: 200px;text-align: center">购入日期</td>
            <td style="width: 200px;text-align: center">
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("grrq")}
                    </c:forEach>
                </c:if>
            </td>
            <td style="width: 200px; text-align: center">存放地点</td>
            <td style="width: 250px; text-align: center;word-wrap:break-word;word-break:break-all;">
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("cfdd")}
                    </c:forEach>
                </c:if></td>
        </tr>
        <tr style="height: 50px;">
            <td style="width: 100px;text-align: center">故障时间</td>
            <td style="width: 100px;text-align: center">
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("gzsj")}
                    </c:forEach>
                </c:if>
            </td>
            <td style="width: 100px; text-align: center">原值</td>
            <td style="width: 100px; text-align: center">
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("ynum")}
                    </c:forEach>
                </c:if></td>
        </tr>
        <tr style="height: 70px;" aria-rowspan="2">
            <td style="width: 100px;text-align: center">初步维修方案</td>
            <td style="width: 600px;text-align: center;border-right: 0" colspan="2">
                <div style="text-align: left;padding-left: 5px;padding-bottom: 10px;">一、故障想象及原因分析:
                    <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                        <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                            ${da.get("gzxxjyyfx")}
                        </c:forEach>
                    </c:if>
                </div>
                <div style="text-align: left;padding-left: 5px;padding-bottom: 5px;">二、方案:</div>
                <div style="text-align: left;padding-left: 5px;padding-bottom: 5px;padding-right: 10px;">维修单位:
                    <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                        <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                            ${da.get("wxdw")}
                        </c:forEach>
                    </c:if>
                </div>
                <div style="text-align: left;padding-left: 5px;padding-bottom: 5px;padding-right: 10px;">联系人及电话:
                    <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                        <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                            ${da.get("lxr")}
                        </c:forEach>
                    </c:if>
                </div>
                <div style="text-align: left;padding-left: 5px;padding-bottom: 5px;padding-right: 10px;">预计维修时间:
                    <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                        <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                            ${da.get("yjwxsj")}
                        </c:forEach>
                    </c:if>
                </div>
                <div style="text-align: left;padding-left: 5px;padding-bottom: 5px;padding-right: 10px;">预计维修费用:
                    <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                        <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                            ${da.get("yjwxfy")}
                        </c:forEach>
                    </c:if>
                </div>
                <div style="text-align: left;padding-left: 5px;padding-bottom: 5px;">
                    维修内容:(含更换配件明细、报价及维修保时限等;涉及技术改造的附相关方案):
                </div>
                <div style="text-align: left;padding-left: 5px;padding-bottom: 5px;">
                    <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                        <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                            ${da.get("wxnr")}
                        </c:forEach>
                    </c:if></div>
                <div style="text-align: right">设备保管员/设备管理员:</div>
            </td>
            <td style="width: 160px;text-align: center;padding-top: 28%;border-left: 0">
                <c:if test="${data.get(0).size()>0 && data.get(0) != null}">
                    <c:forEach items="${data.get(0)}" var="da" varStatus="status">
                        ${da.get("sqr")}
                        ${da.get("recordtime")}
                    </c:forEach>
                </c:if>
            </td>
        </tr>
        <tr style="height: 50px" aria-rowspan="2">
            <td style="width: 100px;text-align: center">使用部门意见</td>
            <td style="width: 400px;text-align: center;border-right: 0" colspan="2">
                <c:if test="${data.get(1).size()>0 && data.get(1) != null}">
                    <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                        ${da.get("bmyj")}
                    </c:forEach>
                </c:if>
                <div style="text-align: right">部门负责人:</div>
            </td>
            <td style="width: 220px;text-align: center;padding-top: 5%;border-left: 0">
                <c:if test="${data.get(1).size()>0 && data.get(1) != null}">
                    <c:forEach items="${data.get(1)}" var="da" varStatus="status">
                        ${da.get("NAME")}
                        ${da.get("time")}
                    </c:forEach>
                </c:if>
            </td>
        </tr>
        <tr style="height: 50px" aria-rowspan="2">
            <td style="width: 100px;text-align: center">设备管理部门意见</td>
            <td style="width: 400px;text-align: center;border-right: 0" colspan="2">
                <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                    <c:forEach items="${data.get(2)}" var="da" varStatus="status">
                        ${da.get("zlbmyj")}

                    </c:forEach>
                </c:if>
                <div style="text-align: right">部门负责人:</div>
            </td>
            <td style="width: 220px;text-align: center;padding-top: 5%;border-left: 0">
                <c:if test="${data.get(2).size()>0 && data.get(2) != null}">
                    <c:forEach items="${data.get(2)}" var="da" varStatus="status">
                        ${da.get("NAME")}
                        ${da.get("time")}
                    </c:forEach>
                </c:if>
            </td>
        </tr>
      <%--  <tr style="height: 40px" aria-rowspan="2">
            <td style="width: 100px;text-align: center">院设备管理员意见</td>
            <td style="width: 470px;text-align: center;border-right: 0" colspan="2">
                <c:if test="${data.get(7).size()>0 && data.get(7) != null}">
                    <c:forEach items="${data.get(7)}" var="da" varStatus="status">
                        ${da.get("ysbglyyj")}
                    </c:forEach>
                </c:if>
                <div style="text-align: right">院设备管理员:</div>
            </td>
            <td style="width: 150px;text-align: center;padding-top: 5%;border-left: 0">
                <c:if test="${data.get(7).size()>0 && data.get(7) != null}">
                    <c:forEach items="${data.get(7)}" var="da" varStatus="status">
                        ${da.get("NAME")}
                        ${da.get("time")}
                    </c:forEach>
                </c:if>
            </td>
        </tr>--%>
        <tr style="height: 50px" aria-rowspan="2">
            <td style="width: 100px;text-align: center">设备维修主管领导意见</td>
            <td style="width: 470px;text-align: center;border-right: 0" colspan="2">
                <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                    <c:forEach items="${data.get(3)}" var="da" varStatus="status">
                        ${da.get("zlzglyj")}
                    </c:forEach>
                </c:if>
                <div style="text-align: right">主管领导:</div>
            </td>
            <td style="width: 150px;text-align: center;padding-top: 5%;border-left: 0">
                <c:if test="${data.get(3).size()>0 && data.get(3) != null}">
                    <c:forEach items="${data.get(3)}" var="da" varStatus="status">
                        ${da.get("NAME")}
                        ${da.get("time")}
                    </c:forEach>
                </c:if>
            </td>
        </tr>
       <%-- <tr style="height: 90px" aria-rowspan="2">
            <td style="width: 100px;text-align: center">设备维修院领导意见</td>
            <td style="width: 470px;text-align: center;border-right: 0" colspan="2">
                <c:if test="${data.get(4).size()>0 && data.get(4) != null}">
                    <c:forEach items="${data.get(4)}" var="da" varStatus="status">
                        ${da.get("sjyj")}
                    </c:forEach>
                </c:if>
                <div style="text-align: right;margin-top: 10px">院领导:</div>
            </td>
            <td style="width: 150px;text-align: center;padding-top: 5%;border-left: 0">
                <c:if test="${data.get(4).size()>0 && data.get(4) != null}">
                    <c:forEach items="${data.get(4)}" var="da" varStatus="status">
                        ${da.get("NAME")}
                        ${da.get("time")}
                    </c:forEach>
                </c:if>
            </td>
        </tr>--%>
        <tr style="height: 10px" aria-rowspan="2">
            <td style="width: 100px;text-align: center">方案实施情况</td>
            <td style="width: 470px;border-right: 0" colspan="2">
                <c:if test="${data.get(5).size()>0 && data.get(5) != null}">
                    <c:forEach items="${data.get(5)}" var="da" varStatus="status">
                        <c:set var="wxssqk" value="${da.get('wxssqk')}"/>
                    </c:forEach>
                </c:if>
                <input type="checkbox" style="margin: 10px 10px;" name="onewxssqk" value='按照维修方案完成'
                       <c:if test="${wxssqk=='按照维修方案完成'}">checked</c:if>> 按照维修方案完成 <br>
                <input type="checkbox" style="margin: 10px 10px;" name="onewxssqk" value="调整维修方案"
                       <c:if test="${wxssqk=='调整维修方案'}">checked</c:if> > 调整维修方案:${tznr} <br>
                <div style="text-align: right">院设备保管员:</div>
            </td>
            <td style="width: 150px;text-align: center;padding-top: 5%;border-left: 0">
                <c:if test="${data.get(5).size()>0 && data.get(5) != null}">
                    <c:forEach items="${data.get(5)}" var="da" varStatus="status">
                        ${da.get("NAME")}
                        ${da.get("time")}
                    </c:forEach>
                </c:if>
            </td>
        </tr>
        <tr style="height: 90px" aria-rowspan="2">
            <td style="width: 100px;text-align: center">维修结果确认</td>
            <td style="width: 500px;border-right: 0" colspan="2">
                <c:if test="${data.get(6).size()>0 && data.get(6) != null}">
                    <c:forEach items="${data.get(6)}" var="da" varStatus="status">
                        <c:set var="onevalue" value="${da.get('onevalue')}"/>
                        <c:set var="twovalue" value="${da.get('twovalue')}"/>
                        <c:set var="threevalue" value="${da.get('threevalue')}"/>
                        <c:set var="bzvalue" value="${da.get('bzvalue')}"/>
                        <c:set var="fourvalue" value="${da.get('fourvalue')}"/>
                    </c:forEach>
                </c:if>
                <input type="checkbox" style="margin: 10px 10px;" name="onevalue" value='维修后,经检定/校准确认'
                       <c:if test="${onevalue=='维修后,经检定/校准确认'}">checked</c:if>> 维修后,经检定/校准确认
                <input type="checkbox" style="margin: 10px 10px;" name="twovalue" value='正常使用'
                       <c:if test="${twovalue=='正常使用' && onevalue=='维修后,经检定/校准确认'}">checked</c:if>>正常使用
                <input type="checkbox" style="margin: 10px 5px;" name="twovalue" value='降级使用'
                       <c:if test="${twovalue=='降级使用' && onevalue=='维修后,经检定/校准确认'}">checked</c:if>>降级使用
                <br>
                <input type="checkbox" style="margin: 10px 10px;" name="onevalue" value="维修后,经核查"
                       <c:if test="${onevalue=='维修后,经核查'}">checked</c:if> > 维修后,经核查
                <input type="checkbox" style="margin: 10px 10px;margin-left: 80px" name="twovalue" value='正常使用'
                       <c:if test="${twovalue=='正常使用' && onevalue=='维修后,经核查'}">checked</c:if>>正常使用
                <input type="checkbox" style="margin: 10px 5px;" name="twovalue" value='降级使用'
                       <c:if test="${twovalue=='降级使用' && onevalue=='维修后,经核查'}">checked</c:if>>降级使用
                <br>
                <input type="checkbox" style="margin: 10px 10px;" name="onevalue" value='维修后,对未修好部分的建议'
                       <c:if test="${onevalue=='维修后,对未修好部分的建议'}">checked</c:if>> 维修后,对未修好部分的建议 : ${threevalue}
                <br>
                <input type="checkbox" style="margin: 10px 10px;" name="onevalue" value="技术改造后,经检定/校准确认"
                       <c:if test="${onevalue=='技术改造后,经检定/校准确认'}">checked</c:if> > 技术改造后,经检定/校准确认:
                <input type="checkbox" style="margin: 10px 10px;margin-left: 80px" name="twovalue" value='正常使用'
                       <c:if test="${twovalue=='正常使用' && onevalue=='技术改造后,经检定/校准确认'}">checked</c:if>>正常使用
                <input type="checkbox" style="margin: 10px 5px;" name="twovalue" value='降级使用'
                       <c:if test="${twovalue=='降级使用' && onevalue=='技术改造后,经检定/校准确认'}">checked</c:if>>降级使用
                <br>
                <input type="checkbox" style="margin: 10px 10px;" name="onevalue" value="技术改造后,经核查"
                       <c:if test="${onevalue=='技术改造后,经核查'}">checked</c:if> > 技术改造后,经核查:
                <input type="checkbox" style="margin: 10px 10px;margin-left: 80px" name="twovalue" value='正常使用'
                       <c:if test="${twovalue=='正常使用' && onevalue=='技术改造后,经核查'}">checked</c:if>>正常使用
                <input type="checkbox" style="margin: 10px 5px;" name="twovalue" value='降级使用'
                       <c:if test="${twovalue=='降级使用' && onevalue=='技术改造后,经核查'}">checked</c:if>>降级使用
                <input type="checkbox" style="margin: 10px 10px;" name="fourvalue" value="维修后审批表归档"
                       <%--<c:if test="${fourvalue=='维修审批表归档'}">checked</c:if>--%> > 维修后审批表归档 <br>
                <div style="text-align: right">设备保管员:</div>
            </td>
            <td style="width: 130px;text-align: center;padding-top: 15%;border-left: 0">
                <c:if test="${data.get(6).size()>0 && data.get(6) != null}">
                    <c:forEach items="${data.get(6)}" var="da" varStatus="status">
                        ${da.get("NAME")}
                        ${da.get("time")}
                    </c:forEach>
                </c:if>
            </td>
        </tr>
        <tr style="height: 90px" aria-rowspan="2">
            <td style="width: 100px;text-align: center">备注:</td>
            <td style="width: 500px;border-right: 0" colspan="3">
               ${bzvalue}
            </td>
        </tr>

    </table>
    <span>注：该单据手写无效</span>
    <!--endprint1-->
</div>
</body>
</html>

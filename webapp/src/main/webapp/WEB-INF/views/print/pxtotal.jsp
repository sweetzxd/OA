<%@ page import="com.oa.core.util.SpringContextUtil" %>
<%@ page import="com.oa.core.service.util.TableService" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oa.core.bean.Loginer" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<%--jspBegin--%>
<%
    Loginer loginer = (Loginer) request.getSession().getAttribute("loginer");
    switch (loginer.getId()) {
        case "mashaoying":
            request.setAttribute("qx", true);
            break;
        case "admin":
            request.setAttribute("qx", true);
            break;
        default:
            request.setAttribute("qx", false);
            break;
    }
    TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
    List<String> bms = tableService.selectSql("SELECT deptidstoname(useridtodeptid(recordName)) as bm from pxssj20021801 where curStatus=2 group by recordName");
    request.setAttribute("bms", bms);
%>
<%--jspEnd--%>
<html>
<head>
<meta charset="utf-8">
<title>部门培训汇总</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<jsp:include page="/common/css.jsp"></jsp:include>
<link rel="stylesheet" href="/zjyoa-pc/resources/css/tablebox.css" type="text/css"/>
<jsp:include page="/common/js.jsp"></jsp:include>
<script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/manager.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
<script src="/zjyoa-pc/resources/js/system/form.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
<%--scriptBegin--%>
<%--scriptEnd--%>
<script type="text/javascript">
        function starscript() {
            var date=new Date();
            var theyear=date.getFullYear();
            var themonth=date.getMonth();
            console.log(theyear);
            var years=document.getElementById('year');
            years.options[1].value = theyear;
            years.options[1].text = theyear;
            years.options[0].value = theyear-1;
            years.options[0].text = theyear-1;
            layui.table.init('pxtotal', {
                url: '/zjyoa-pc/printall/pxtotalselect.do',
                method: 'POST',
                id: 'pxtotal',
                page: false,
                where: {
                    type:"1"
                }

            });
            <%--functionBegin--%>
            <%--functionEnd--%>
        }
        <%--jsBegin--%>
      function  seljs(){
            var tablebm =$("#bmselect option:selected").html();
            var ye=$("#year option:selected").val();
            var jd=$("#jd option:selected").html();
            layui.table.reload('pxtotal', {
              page: false,
              where: {
                  type:"2",
                  tablebm: tablebm,
                  jd: jd,
                  ye: ye
              }
            });
      }

       function exporttotal (){
           var tablebm =$("#bmselect option:selected").html();
           var ye=$("#year option:selected").val();
           var jd=$("#jd option:selected").html();
           var type="";
           if(tablebm!=''&&tablebm!='全部'){
               type=2;
           }else {
               type=1;
           }
           window.location.href = "/zjyoa-pc/printall/exportpxtab.do?type="+type+"&tablebm="+tablebm+"&ye="+ye+"&jd="+jd;
       }
       //打印走的方法
        function preview(oper) {
            document.getElementById("strdiv").style.display = "none";
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

        <%--jsEnd--%>
    </script>
</head>
<body onload="starscript()">
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md24">
            <div class="layui-card layui-card-body">
                    <div style="text-align: left;font-size: 18px;padding-bottom: 1%">
                        <span style="width: 100%;font-size: 23px;padding-left: 10%;padding-right: 1%">年份:</span>
                        <span style="width: 100% ">
                          <select style="font-size: 23px;text-align: left;margin-left: 0px;" id="year" name="year">
                               <option></option>
                               <option selected=""></option>
                           </select>
                       </span>
                        <span style="width: 100%;font-size: 23px;padding-left: 10%;">季度:</span>
                        <span style="width: 100% ">
                           <select style=" font-size: 20px;text-align: left;margin-left: 0px;" id="jd" name="jd">
                               <option selected value="1">第一季度</option>
                               <option value="2">第二季度</option>
                               <option value="3">第三季度</option>
                               <option value="4">第四季度</option>
                           </select>
                       </span>
                        <span style="width: 100%;font-size: 23px;padding-left: 10%;">部门:</span>
                        <c:if test="${!empty bms and qx}">
                            <select id="bmselect">
                                <option value="1">全部</option>
                                <c:forEach items="${bms}" var="bm" varStatus="status">
                                    <option value="${status.index+2}">${bm}</option>
                                </c:forEach>
                            </select>
                        </c:if>
                        <span style="width: 100%;font-size: 23px;padding-left: 10%;">
                            <a id="butpeo" style="background-color: #0b96e5; height: 38px; color: white; line-height: 38px; padding:10px 18px;font-size: 16px;" onclick='seljs()'>检索</a>
                        </span>
                    </div>
                <!--startprint1-->

                    <table lay-filter="pxtotal" id="pxtotal" class="layui-table"
                           lay-data="{id:'pxtotal',cellMinWidth: 80, toolbar: '#toolbar', defaultToolbar: []}">
                        <thead>
                        <tr>
                            <th lay-data="{field:'numb',align:'center',width:90}">
                                序号
                            </th>
                            <th lay-data="{field:'dept',width:160,align:'center', sort: true }">
                                部门
                            </th>
                            <th lay-data="{field:'pxnr',width:320,align:'left'}">培训内容</th>
                            <th lay-data="{field:'ydrs200218002',width:110,align:'center', sort: true}">
                               培训计划人数
                            </th>
                            <th lay-data="{field:'sjrspeople',width:140,align:'center'}">培训实际人数</th>
                            <th lay-data="{field:'xs20021800001',width:140,align:'center'}">培训时长</th>
                            <th lay-data="{field:'onepeoxs',width:140,align:'center', sort: true }">
                                人均学时
                            </th>
                            <th lay-data="{field:'cq',width:140,align:'center'}">出勤率</th>
                            <th lay-data="{field:'pxqdbfj1',width:120,align:'center'}">培训签到表附件</th>
                            <th lay-data="{field:'pxclfj2',width:120,align:'center'}">培训材料附件</th>
                            <th lay-data="{field:'jywgfj3',width:120,align:'center'}">讲义文稿附件</th>
                        </tr>
                        </thead>
                    </table>
                <!--endprint1-->
                    <script type="text/html" id="toolbar">
                        <div class="layui-btn-container" id="strdiv">
                               <a class='layui-btn layui-btn-xs' href='/zjyoa-pc/userpage/viewpage/${pageid}.do' title="刷新">刷新</a>
                               <a  class="layui-btn layui-btn-xs" onclick="exporttotal()">导出Excel</a>
                            <%--<a class='layui-btn layui-btn-xs' onclick="preview(1)">打印</a>--%>
                                <%--aBegin--%>
                                <%--aEnd--%>
                        </div>
                    </script>
                <%--divBegin--%>
                <%--divEnd--%>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<%--
  User: zxd
  Date: 2020/02/22
  Time: 上午 10:51
  Explain: 说明
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
    <title>培训人员评分</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" type="text/css" href="/zjyoa-pc/resources/css/print.css"/>
    <link rel="stylesheet" href="/zjyoa-pc/resources/layuiadmin/layui/css/layui.css" media="all">
    <style>
        table tr td{
            width: 94px;
            text-align: center
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
</head>
<body>
<div id="sealdiv" style="margin:0 auto;width: 756px;height: 1086px;">
    <table style="width: 756px;height: 100px">
        <tr>
            <td style="width: 756px;font-size:15px;height:30px;text-align:left;" align='left' valign='middle' colspan="4">
            </td>
        </tr>
        <tr>
            <td style="width: 756px;font-size:30px;height:70px" align='center' valign='middle' colspan="4">培训人员评分
            </td>
        </tr>
    </table>
    <table border="1" cellspacing="0" style="width: 100%;max-height: 900px;" id="pxygpf" >
        <tr style="height:50px;">
            <td>姓名</td>
            <td>部门</td>
            <td>签到情况</td>
            <td>评分</td>
        </tr>
        <c:if test="${pfs!=null and pfs.size()>0}">
        <c:forEach items="${pfs}" var="pf" varStatus="status">
            <tr style="height:50px;" id="${pf.recno}">
                <td>${pf.qdr}</td>
                <td>${pf.qdrbm}</td>
                <td>
                    <c:if test="${isstart!=0}"><c:if test="${pf.type==0}"><p class="sign-button"><a href="javascript:void(0)" onclick="signIn(this,'${recno}','${pf.user}')">签到</a></p></c:if></c:if>
                    <c:if test="${isstart==0}"><p class="sign-button"><a href="javascript:void(0)" onclick="signInstart(this,'${pxconton}','${pf.user}','${isstart}')">签到</a></p></c:if>
                    <c:if test="${pf.type==1}">已签到</c:if>
                </td>
                <td >
                    <c:if test="${pf.type==1}">
                    <input type="text" value="${pf.cj}" placeholder="请填写评分" style="width: 100px;"/>
                    </c:if>
                    <c:if test="${pf.type==0}">
                        <div id="${pf.user}"></div>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </c:if>
        <tr style="height:50px;">
            <td colspan="4">
                <a class="layui-btn layui-btn-xs" href="javascript:void(0);" onclick="submitpf()">提交评分</a>
            </td>
        </tr>
    </table>
</div>
</body>
<script>
    function submitpf(){
        var tb = document.getElementById('pxygpf');
        var rows = tb.rows;
        console.log(rows)
        var data=[];
        for(var i = 1; i<rows.length-1; i++ ){
            var trid = rows[i].id;
            if(trid!=null && trid!=''){
                var va = {};
                va.id=trid;
                va.val=rows[i].cells[3].children[0].value;
                data.push(va);
            }
        }
        console.log(data);
        $.ajax({
            type: "POST",
            url: "/zjyoa-pc/printall/submitpf.do",
            data: JSON.stringify(data),
            contentType: "application/json",
            dataType: "json",
            cache: false,
            success: function (data) {
                console.log(data);
                console.log("89898989898");
                alert(data.msg);
            },
            error: function (message) {
                layer.msg("请求服务器失败。");
            }
        });
    }
//原有的签到按钮走的方法
    function signIn(obj,recno,user){
        console.log(recno,user)
        var pobj = $(obj).parent();
        var udiv = $("#"+user).parent();
        $.ajax({
            url: "/zjyoa-pc/printall/bmpxqdsignin.do",
            type: 'post',
            dataType: 'json',
            data:{"recno":recno,"user":user},
            async:false,
            success: function(d, status){
                if(d.success==1){
                    obj.remove();
                    pobj.append("已签到");
                    pobj.parent().parent().attr("id",d.data.recorderNO);
                    $("#"+user).remove();
                    udiv.append("<input type='text' value='' placeholder='请填写评分' style='width: 100px;'/>");
                }
            }
        });
    }
//首页签到按钮走的方法

    function signInstart(obj,pxconton,user,isstart){
        var pobj = $(obj).parent();
        var udiv = $("#"+user).parent();
        $.ajax({
            url: "/zjyoa-pc/printall/bmpxqdsignin.do",
            type: 'post',
            dataType: 'json',
            data:{"recno":pxconton,"user":user,"isstart":isstart},
            async:false,
            success: function(d, status){
                if(d.success==1){
                    obj.remove();
                    pobj.append("已签到");
                    pobj.parent().parent().attr("id",d.data.recorderNO);
                    $("#"+user).remove();
                    udiv.append("<input type='text' value='' placeholder='请填写评分' style='width: 100px;'/>");
                }
            }
        });
    }


</script>
</html>

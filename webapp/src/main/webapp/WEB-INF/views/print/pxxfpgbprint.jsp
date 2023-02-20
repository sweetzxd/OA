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
<%--jspEnd--%>
<html>
<head>
<meta charset="utf-8">
<title>培训效果评估表</title>
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
    <style>
        input[type=radio]{
            margin: 2px 10px;
        }

    </style>
<%--scriptEnd--%>
<script type="text/javascript">
        <%--jsBegin--%>
       //打印走的方法
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
        //给复选框赋值
        function checkselect() {
            var data =JSON.parse('${data}');

            $("#bm2020071601").html(data.bm2020071601);
            $("#name2020071601").html(data.name2020071601);
            $("#pxnr2020071601").html(data.pxnr2020071601);
            $("#pxteacher2020071601").html(data.pxteacher2020071601);
            $("#pxtime2020071601").html(data.pxtime2020071601);
            $("#pxxs2020071601").html(data.pxxs2020071601);
            $("#jyxq2020071602").html(data.jyxq2020071602);
            $("#pxgw2020071602").html(data.pxgw2020071602);
            //单选框的赋值
            var arr=["teachercd2020071601","teachjksp2020071601","teachjkfs2020071601","islxsj2020071601",
                "pxnr2020071602","pxfs2020071602","pxsc2020071602","pxsh2020071602"];
            for(var k=0;k<arr.length;k++){
                var pp=arr[k];
                $("input[name="+pp+"]").each(function(i,obj){
                    var ai=$("input[name="+pp+"]:eq('"+i+"')").val();
                    if(ai==data[pp]){
                        $("input[name="+pp+"]:eq('"+i+"')").attr("checked",true);
                    }
                });
            }
            //复选框的赋值
            var onesh=data.onepxsh2020071602;
            var splitonesh = onesh.split(',');

            for(var a=0;a<splitonesh.length;a++){
                console.log(splitonesh[a]);
                  $("input[name=onepxsh2020071602]").each(function(i,obj){
                      var af=$("input[name=onepxsh2020071602]:eq('"+i+"')").val();
                      if(af.indexOf("，")>=0||af==splitonesh[a]){
                          $("input[name=onepxsh2020071602]:eq('"+i+"')").attr("checked",true);
                      }
                  });
            }
        }

        <%--jsEnd--%>
    </script>
</head>
<html>
<body onload="checkselect()">
<div style="padding: 20px 20px">
    <input type=button name='button_export' value='打印' onclick=preview(1)>
</div>
<c:if test="${type=='2'}">
<div id="strdiv" style="width: 756px;height: 1100px;margin: 0 auto;background-color: white" >
    <!--startprint1-->
     <div style="width: 756px;margin: 0 auto;">
            <table style="width: 756px;height: 90px">
                <tr>
                    <td style="width: 756px;font-size:30px;height:90px" align='center' valign='middle' colspan="4">培训效果评估表
                    </td>
                </tr>
            </table>
            <table border="1" cellspacing="0" style="width: 756px;height: 450px; overflow-y:scroll">
                <tr style="height: 40px">
                    <td style="width: 25%;text-align: center">所在部门</td>
                    <td style="width: 25%;text-align: center" id="bm2020071601">
                    </td>
                    <td style="width: 25%; text-align: center">姓名</td>
                    <td style="width: 25%; text-align: center" id="name2020071601">
                    </td>
                </tr>
                <tr style="height: 40px">
                    <td style="width: 25%;text-align: center">培训课程名称</td>
                    <td style="width: 25%;text-align: center" id="pxnr2020071601">
                    </td>
                    <td style="width: 25%; text-align: center">培训教师</td>
                    <td style="width: 25%; text-align: center" id="pxteacher2020071601">
                    </td>
                </tr>

                <tr style="height: 40px">
                    <td style="width: 25%;text-align: center">培训时间</td>
                    <td style="width: 25%;text-align: center" id="pxtime2020071601">
                    </td>
                    <td style="width: 25%; text-align: center">培训形式</td>
                    <td style="width: 25%; text-align: center" id="pxxs2020071601">
                    </td>
                </tr>
                <tr style="height: 12%">
                    <td style="width: 25%;text-align: center" rowspan="4">对培训教师的评价</td>
                    <td style="width: 25%;text-align: left ;padding-left: 10px;">教师敬业程度</td>
                    <td style="width: 25%; text-align: left;padding: 9px 9px;" colspan="2">
                        <input type="radio" name="teachercd2020071601" value="优秀">优秀
                        <input type="radio" name="teachercd2020071601" value="良好" >良好
                        <input type="radio" name="teachercd2020071601" value="尚可" >尚可
                        <input type="radio" name="teachercd2020071601" value="较差" >较差
                    </td>
                </tr>
                <tr style="height: 8%">
                    <td style="width: 25%;text-align: left ;padding-left: 10px;">讲授水平</td>
                    <td style="width: 25%; text-align: left;padding: 9px 9px;" colspan="2">
                        <input type="radio" name="teachjksp2020071601" value="优秀" >优秀
                        <input type="radio" name="teachjksp2020071601" value="良好">良好
                        <input type="radio" name="teachjksp2020071601" value="尚可">尚可
                        <input type="radio" name="teachjksp2020071601" value="较差">较差
                    </td>
                </tr>
                <tr style="height: 8%">
                    <td style="width: 25%;text-align: left;padding-left: 10px;">讲授方式</td>
                    <td style="width: 25%; text-align: left;padding: 9px 9px;" colspan="2">
                        <input type="radio" name="teachjkfs2020071601" value="十分生动">十分生动
                        <input type="radio" name="teachjkfs2020071601" value="生动">生动
                        <input type="radio" name="teachjkfs2020071601" value="一般">一般
                        <input type="radio" name="teachjkfs2020071601" value="不生动">不生动
                    </td>
                </tr>
                <tr style="height: 8%">
                    <td style="width: 25%;text-align: left;padding-left: 10px;">是否联系实际</td>
                    <td style="width: 25%; text-align: left;padding: 9px 9px;" colspan="2">
                        <input type="radio" name="islxsj2020071601" value="联系密切">联系密切
                        <input type="radio" name="islxsj2020071601" value="有些联系">有些联系
                        <input type="radio" name="islxsj2020071601" value="无联系">无联系
                    </td>
                </tr>


                <tr style="height: 12%">
                    <td style="width: 25%;text-align: center" rowspan="5">对培训组织者的评价</td>
                    <td style="width: 25%;text-align: left;padding-left: 10px;">培训内容</td>
                    <td style="width: 25%; text-align: left;padding: 9px 9px;" colspan="2">
                        <input type="radio" name="pxnr2020071602" value="优秀">优秀
                        <input type="radio" name="pxnr2020071602" value="良好">良好
                        <input type="radio" name="pxnr2020071602" value="尚可">尚可
                        <input type="radio" name="pxnr2020071602" value="较差">较差
                    </td>
                </tr>

                <tr style="height: 12%">
                    <td style="width: 25%;text-align: left;padding-left: 10px;">培训方式</td>
                    <td style="width: 25%; text-align: left;padding: 9px 9px;" colspan="2">
                        <input type="radio" name="pxfs2020071602" value="优秀">优秀
                        <input type="radio" name="pxfs2020071602" value="良好">良好
                        <input type="radio" name="pxfs2020071602" value="尚可">尚可
                        <input type="radio" name="pxfs2020071602" value="较差">较差
                    </td>
                </tr>

                <tr style="height: 12%">
                    <td style="width: 25%;text-align: left;padding-left: 10px;">培训时长</td>
                    <td style="width: 25%;text-align: left;padding: 9px 9px;" colspan="2">
                        <input type="radio" name="pxsc2020071602" value="太长">太长
                        <input type="radio" name="pxsc2020071602" value="适合">适合
                        <input type="radio" name="pxsc2020071602" value="不足">不足
                    </td>
                </tr>

                <tr style="height: 12%">
                    <td style="width: 25%;text-align: left;padding-left: 10px;">培训收获</td>
                    <td style="width: 25%; text-align: left;padding: 9px 9px;" colspan="2">
                        <input type="radio" name="pxsh2020071602" value="较大">较大
                        <input type="radio" name="pxsh2020071602" value="一般">一般
                        <input type="radio" name="pxsh2020071602" value="较少">较少
                        <input type="radio" name="pxsh2020071602" value="无">无
                    </td>
                </tr>
                <tr style="height: 12%">
                    <td style="width: 25%;text-align: left;padding: 10px 10px;" colspan="3">参加本次培训有哪些收获?(可多选)<br>
                        <input type="checkbox" style="margin: 10px 10px;" name="onepxsh2020071602" value="获得适用的新知识" > 获得适用的新知识 <br>
                        <input type="checkbox" style="margin: 10px 10px;"  name="onepxsh2020071602" value="可以用在工作上的一些有效的研究技巧及技术" > 可以用在工作上的一些有效的研究技巧及技术 <br>
                        <input type="checkbox" style="margin: 10px 10px;"  name="onepxsh2020071602" value="将帮助我改变我的工作态度"> 将帮助我改变我的工作态度 <br>
                        <input type="checkbox" style="margin: 10px 10px;"  name="onepxsh2020071602" value="帮助我印证了某些观念"> 帮助我印证了某些观念 <br>
                        <input type="checkbox" style="margin: 10px 10px;"  name="onepxsh2020071602" value="给我一个很好的机会，客观的观察我自己以及我的工作"> 给我一个很好的机会,客观的观察我自己以及我的工作 <br>
                    </td>
                </tr>

                <tr style="height: 12%">
                  <td style="width: 25%;height: 150px;padding-bottom: 65px" colspan="4">
                      对今后培训的建议和需求:<br>
                      <div id="jyxq2020071602" style="padding-left: 10px"></div>
                    </td>
                </tr>
                <tr style="height: 12%">
                    <td style="width: 25%;height: 150px;padding-bottom: 65px" colspan="4">
                       培训感悟:<br>
                        <div id="pxgw2020071602" style="padding-left: 10px"></div>
                    </td>
                </tr>

            </table>
        </div>
    <!--endprint1-->
</div>
</c:if>
</body>
</html>
</html>
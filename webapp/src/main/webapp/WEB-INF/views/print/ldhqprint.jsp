<%--
  Created by IntelliJ IDEA.
  User: xingyu
  Date: 2019/11/27
  Time: 11:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>征求意见打印页</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" type="text/css" href="/zjyoa-pc/resources/css/print.css"/>
    <style>
        .msg-box{
            float: left;
            width: 50%;
            height: 80px;
            line-height: 100px;
        }
        .name-box{
            float: left;
            width: 20%;
            height: 75px;
            margin-top: 25px;
        }
        .date-box{
            float: left;
            height: 80px;
            margin-left: 20px;
            padding: 10px;
            line-height: 100px;
        }
    </style>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/zjyoa-pc/resources/js/system/system.js?t=<%=System.currentTimeMillis()%>" type="text/javascript" charset="utf-8"></script>
    <script src="/zjyoa-pc/resources/js/system/manager.js" type="text/javascript" charset="utf-8"></script>
    <script src="/zjyoa-pc/resources/js/system/form.js" type="text/javascript" charset="utf-8"></script>
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
        function exportexcelopinioin(tableid) {
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
        function exportwordopinion() {
            $("#opiondiv").wordExport('建议表')
        }

        //当前日期
        function newDate() {
            var time = new Date();
            var year = time.getFullYear();
            var month = time.getMonth();
            var date = time.getDate();
            var minutes = time.getMinutes();
            //month<10?month='0'+month:month;
            month = month + 1;
            var now_date = year + '年' + month + '月' + date+"日";
            document.getElementById('newdate').innerHTML = now_date;
        }

    </script>
</head>
<body>
<div>
    <input type=button name='button_export' value='打印' onclick="preview(1)">
    <input type=button name='button_export' value='导出excel' onclick="exportexcelopinioin('opiondiv')">
    <input type=button name='button_export' value='导出word' onclick="exportwordopinion()">
</div>
<!--startprint1-->
<div id="opiondiv" style="width: 756px;height: 1086px;">
    <table style="width: 756px;height: 1px">
        <tr>
            <td style="width: 756px;" align='center' valign='middle' colspan="4">
                    <div style="font-size:25px;padding: 10px;">河北省质检研究院会议纪要会签单</div>
                    <div style="font-size:15px;text-align: right" id="newdate"></div>
            </td>
        </tr>
    </table>
    <table border="1" cellspacing="0" style="width: 756px;height: 886px">
        <tr style="height: 50px">
            <td style="width: 150px;text-align: center">
                <label>起草部门</label>
            </td>
            <td style="width: 100px;text-align: center" colspan="2">
                <label>${data.get('dept')}</label>
            </td>
            <td style="width: 156px;text-align: center">
                <label>起草人</label>
            </td>
            <td style="width: 250px;text-align: center">
                <label>${data.get('user')}</label>
            </td>
        </tr>
        <tr style="height:50px">
            <td style="width: 100px;text-align: center" colspan="2">文件标题</td>
            <td style="width: 656px;text-align: center" colspan="3">
                <label>${data.get('title')}</label>
            </td>
        </tr>
        <tr style="height:90px">
            <td style="width: 100px;text-align: center" colspan="2">签发人</td>
            <td style="width: 656px;text-align: center" colspan="3">
                <c:if test="${data.get('dongzhanwei')==''}">
                    <label>${data.get('dongzhanwei').get("msg")}</label>
                    <label>${data.get('dongzhanwei').get("name")}</label>
                    <label>${data.get('dongzhanwei').get("date")}</label>
                </c:if>
            </td>
        </tr>
        <tr style="height:90px">
            <td style="width: 100px;text-align: center" rowspan="5">
                <label>领</label><br/>
                <label>导</label><br/>
                <label>传</label><br/>
                <label>阅</label><br/>
                <label>意</label><br/>
                <label>见</label>
            </td>
            <td style="width: 100px;text-align: center">
                <label>龙冬梅</label>
            </td>
            <td style="width: 656px;text-align: center" colspan="3">
                <c:if test="${data.get('longdongmei')==''}">
                    <label>${data.get('longdongmei').get("msg")}</label>
                    <label>${data.get('longdongmei').get("name")}</label>
                    <label>${data.get('longdongmei').get("date")}</label>
                </c:if>
            </td>
        </tr>
        <tr style="height:90px">
            <td style="width: 100px;text-align: center">
                <label>田  旭</label>
            </td>
            <td style="width: 656px;text-align: center" colspan="3">
                <c:if test="${data.get('tianxu')==''}">
                    <label>${data.get('tianxu').get("msg")}</label>
                    <label>${data.get('tianxu').get("name")}</label>
                    <label>${data.get('tianxu').get("date")}</label>
                </c:if>
            </td>
        </tr>
        <tr style="height:90px">
            <td style="width: 100px;text-align: center">
                <label>张远恒</label>
            </td>
            <td style="width: 656px;text-align: center" colspan="3">
                <c:if test="${data.get('zhangyuanheng')==''}">
                    <label>${data.get('zhangyuanheng').get("msg")}</label>
                    <label>${data.get('zhangyuanheng').get("name")}</label>
                    <label>${data.get('zhangyuanheng').get("date")}</label>
                </c:if>
            </td>
        </tr>
        <tr style="height:90px">
            <td style="width: 100px;text-align: center">
                <label>罗  强</label>
            </td>
            <td style="width: 656px;text-align: center" colspan="3">
                <c:if test="${data.get('luoqiang')==''}">
                    <label>${data.get('luoqiang').get("msg")}</label>
                    <label>${data.get('luoqiang').get("name")}</label>
                    <label>${data.get('luoqiang').get("date")}</label>
                </c:if>
            </td>
        </tr>
        <tr style="height:90px">
            <td style="width: 100px;text-align: center">
                <label>戴志强</label>
            </td>
            <td style="width: 656px;text-align: center" colspan="3">
                <c:if test="${data.get('daizhiqiang')==''}">
                    <label>${data.get('daizhiqiang').get("msg")}</label>
                    <label>${data.get('daizhiqiang').get("name")}</label>
                    <label>${data.get('daizhiqiang').get("date")}</label>
                </c:if>
            </td>
        </tr>
        <tr style="height:90px">
            <td style="width: 100px;text-align: center">
                <label>院长助理</label>
            </td>
            <td style="width: 100px;text-align: center">
                <label>候都兴</label>
            </td>
            <td style="width: 656px;text-align: center" colspan="3">
                <c:if test="${data.get('houduxing')==''}">
                    <label>${data.get('houduxing').get("msg")}</label>
                    <label>${data.get('houduxing').get("name")}</label>
                    <label>${data.get('houduxing').get("date")}</label>
                </c:if>
            </td>
        </tr>
        <tr style="height:90px">
            <td style="width: 100px;text-align: center">
                <label>总工程师</label>
            </td>
            <td style="width: 100px;text-align: center">
                <label>李  欣</label>
            </td>
            <td style="width: 656px;text-align: center" colspan="3">
                <c:if test="${data.get('lixin')==''}">
                    <label>${data.get('lixin').get("msg")}</label>
                    <label>${data.get('lixin').get("name")}</label>
                    <label>${data.get('lixin').get("date")}</label>
                </c:if>
            </td>
        </tr>
        <tr style="height:90px">
            <td style="width: 100px;text-align: center">
                <label>党群工作部</label>
            </td>
            <td style="width: 100px;text-align: center">
                <label>鲍  培</label>
            </td>
            <td style="width: 656px;text-align: center" colspan="3">
                <c:if test="${data.get('baopei')==''}">
                    <label>${data.get('baopei').get("msg")}</label>
                    <label>${data.get('baopei').get("name")}</label>
                    <label>${data.get('baopei').get("date")}</label>
                </c:if>
            </td>
        </tr>
        <tr style="height:90px">
            <td style="width: 100px;text-align: center">
                <label>战略规划部</label>
            </td>
            <td style="width: 100px;text-align: center">
                <label>周  敬</label>
            </td>
            <td style="width: 656px;text-align: center" colspan="3">
                <c:if test="${data.get('zhoujing')==''}">
                    <label>${data.get('zhoujing').get("msg")}</label>
                    <label>${data.get('zhoujing').get("name")}</label>
                    <label>${data.get('zhoujing').get("date")}</label>
                </c:if>
            </td>
        </tr>
    </table>
    <!--endprint1-->
</div>
<script>
    $(document).ready(function(){
        newDate();
    });
</script>
</body>
</html>
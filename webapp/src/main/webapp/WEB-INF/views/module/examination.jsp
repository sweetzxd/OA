<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/29
  Time: 上午 11:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.oa.com/core" prefix="s" %>
<jsp:include page="/common/var.jsp"></jsp:include>

<html>
<head>
    <meta charset="utf-8">
    <title>绩效考核评分</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <jsp:include page="/common/css.jsp"></jsp:include>
    <jsp:include page="/common/js.jsp"></jsp:include>
    <script src="/resources/js/system/system.js" charset="utf-8"></script>
    <script src="/resources/js/system/manager.js" charset="utf-8"></script>
    <style>
        table, table tr th, table tr td {
            border: 1px solid #dddddd;
        }

        table {
            width: 100%;
            min-height: 25px;
            line-height: 25px;
            text-align: center;
            border-collapse: collapse;
        }

        pre {
            border-radius: 0px;
            background-color: white;
            border: 0px solid white;
        }

        .oa-laydate-list {
            position: absolute;
            left: 0;
            top: 45px;
            width: 100%;
            height: 80%;
            padding: 10px;
            background-color: #fff
        }

        .oa-laydate-list > li {
            position: relative;
            width: 100%;
            height: 36px;
            line-height: 36px;
            margin: 3px 0;
            text-align: center;
            cursor: pointer
        }

        .oa-laydate-list > li:hover {
            background-color: #A5CAE6;
        }

        .oa-laydate-this {
            background-color: #B1E6D8;
        }
    </style>
    <script type="text/javascript">
        function starscript() {

        }
    </script>
<body onload="starscript()">
<c:if test="${type!='user'}">
    <div class="layui-form layui-side" style="margin-top: 20px">
        <div class="layui-laydate-header">
            <i class="layui-icon laydate-icon laydate-prev-y" title="前一年"></i>
            <div style="margin-left: 5px;text-align: center">
                <span value="" id="year"></span>
            </div>
            <i class="layui-icon laydate-icon laydate-next-y" title="后一年"></i>
        </div>
        <ul class="oa-laydate-list"></ul>
    </div>
    <div class="layui-body" id="rigthPage">
        <div style="padding: 15px;height:95%">
            <table lay-filter="examination" id="examination" class="layui-table"
                   lay-data="{id:'examination', height: 'full-60', cellMinWidth: 80, page: true}">
                <thead>
                <tr>
                    <th lay-data="{field:'staffName'}">员工</th>
                    <th lay-data="{field:'deptName'}">部门</th>
                    <th lay-data="{field:'grpf181129001', unresize: true}">个人评分</th>
                    <th lay-data="{field:'totalscores', unresize: true}">总分</th>
                    <th lay-data="{field:'examine',templet: '#examine', unresize: true}">考核</th>
                </tr>
                </thead>
            </table>
            <script type="text/html" id="examine">
                {{# if(d.examine=='2'){ }}
                <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="modi" title="点击修改">已评分</a>
                <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="put" title="点击提交，提交后不可修改">提交</a>
                {{# }else if(d.examine=='1'){ }}
                <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="add" title="点击进行评分">评分</a>
                {{# }else{ }}
                <span>已提交</span>
                {{# } }}
            </script>
        </div>
    </div>
    <script type="text/javascript">
        $(document).ready(function () {
            var date = new Date;
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            $("#year").text(year + "年");
            $("#year").attr("value", year);
            for (var i = 1; i < 13; i++) {
                if (month == i) {
                    $('ul.oa-laydate-list').append('<li lay-ym="' + i + '" class="oa-laydate-this">' + i + '月</li>');
                } else {
                    $('ul.oa-laydate-list').append('<li lay-ym="' + i + '" class="">' + i + '月</li>');
                }
            }
        });
        layui.use('table', function () {
            var table = layui.table;
            table.init('examination', {
                url: '/examination/gotoexamination.do'
                , method: 'POST'
                , id: 'examination'
                , page: true
                , limit: 15
                , where: {
                    year: $("#year").attr("value"),
                    month: $("li.oa-laydate-this").attr("lay-ym"),
                }
            });
            $("ul.oa-laydate-list").on("click", "li", function () {
                $("li.oa-laydate-this").removeClass();
                $(this).addClass("oa-laydate-this");
                tablereload();
            });
            $("i.laydate-prev-y").on("click", function () {
                var date = new Date;
                var thisyear = parseInt(date.getFullYear());
                var year = parseInt($("#year").attr("value")) - 1;
                if (thisyear - 3 < year) {
                    $("#year").text(year + "年");
                    $("#year").attr("value", year);
                    tablereload();
                }
            });
            $("i.laydate-next-y").on("click", function () {
                var date = new Date;
                var thisyear = parseInt(date.getFullYear());
                var year = parseInt($("#year").attr("value")) + 1;
                if (thisyear + 1 >= year) {
                    $("#year").text(year + "年");
                    $("#year").attr("value", year);
                    tablereload();
                }
            });

            function tablereload() {
                table.reload("examination", {
                    where: {
                        year: $("#year").attr("value"),
                        month: $("li.oa-laydate-this").attr("lay-ym"),
                    }
                });
            }

            table.on('tool(examination)', function (obj) {
                var data = obj.data;
                switch (obj.event) {
                    case 'add':
                        examineEdit(data.userName, data.deptId, data.date);
                        break;
                    case 'modi':
                        examineEdit(data.userName, data.deptId, data.date);
                        break;
                    case 'put':
                        examinePut(data.userName, data.deptId, data.date);
                        break;
                }
            });
        });

        function examineEdit(userId, deptId, date) {
            oa.gotourl("/examination/gettablevalue.do?userName=" + userId + "&deptId=" + deptId + "&date=" + date + "&leader=1");
        }

        function examinePut(userId, deptId, date) {
            if (confirm("提交后将不可在进行修改，您确定进行提交吗？")) {
                $.ajax({
                    type: "POST",
                    url: "/examination/examineput.do",
                    //contentType: "application/json; charset=utf-8",
                    data: {userName: userId, deptId: deptId, date: date},
                    dataType: "html",
                    error: function (message) {
                        console.log(message.toString())
                    }
                });
                location.reload();
            }
        }
    </script>
</c:if>
<c:if test="${type=='user'}">
    <div style="position:relative;">
        <div style="position:absolute;float:left;height: 38px;line-height:38px;z-index: 1;">
            <a href="javascript:void(0);" onclick="save()" class="layui-btn layui-btn-xs layui-btn-normal"
               style="margin-left: 10px">保存</a>
            <a href="javascript:void(0);" onclick="preview(1);" class="layui-btn layui-btn-xs layui-btn-normal"
               style="margin-left: 10px">打印</a>
            <a href="javascript:void(0);" onclick="location.reload();" class="layui-btn layui-btn-xs layui-btn-normal"
               style="margin-left: 10px">刷新</a>
        </div>
        <!--startprint1-->
        <div style="margin-left: 100px">
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">考核人员</label>
                    <div class="layui-input-inline">
                        <input type="text" readonly unselectable="on" name="name" lay-verify="name" autocomplete="off"
                               value="${name}" class="layui-input">
                        <input type="hidden" name="userName" lay-verify="userName" autocomplete="off"
                               value="${userName}" class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">所属部门</label>
                    <div class="layui-input-inline">
                        <input type="text" readonly unselectable="on" name="deptName" lay-verify="deptName"
                               autocomplete="off" value="${deptName}" class="layui-input">
                        <input type="hidden" name="deptId" lay-verify="deptId" autocomplete="off" value="${deptId}"
                               class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">考核日期</label>
                    <div class="layui-input-inline">
                        <input type="text" readonly unselectable="on" name="date" lay-verify="date" autocomplete="off"
                               value="${date}" class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">总分</label>
                    <div class="layui-input-inline">
                        <input type="text" readonly unselectable="on" name="totalscores" id="totalscores"
                               lay-verify="totalscores" autocomplete="off" value="" class="layui-input">
                    </div>
                </div>
            </div>
        </div>
        <c:set value="0" var="totalscores"/>
        <table width="100%" style="border:1px solid #ddd;">
            <thead>
            <tr>
                <td width="10%">考核种类</td>
                <td width="20%">考核内容</td>
                <td width="20%">参考依据</td>
                <td width="5%">权重分</td>
                <td width="5%">个人评分</td>
                <td width="5%">评分</td>
                <td width="20%">评分参考</td>
                <td width="5%">附件</td>
                <td width="10%">备注</td>
            </tr>
            </thead>
            <tbody>
            <c:if test="${!empty tbodyval}">
                <c:forEach items="${tbodyval}" var="val" varStatus="status">
                    <tr id="${val.recorderNO}">
                        <td hidden>
                            <input type="text" name="khx1811290001" lay-verify="khx1811290001" autocomplete="off"
                                   class="layui-input" value="${val.khx1811290001}">
                        </td>
                        <td width="10%" style="text-align: center;font-size: 14px;">
                            <pre>${val.khzl181129002}</pre>
                        </td>
                        <td width="20%" style="text-align: left;font-size: 14px;">
                            <pre>${val.khnr181129002}</pre>
                        </td>
                        <td width="20%" style="text-align: left;font-size: 14px;">
                            <pre>${val.ckyj181129002}</pre>
                        </td>
                        <td width="5%" style="font-size: 14px;">
                            <pre>${val.khqz181129002}</pre>
                        </td>
                        <td width="5%" style="font-size: 14px;">
                            <input type="number" name="grpf181129001" lay-verify="grpf181129001" autocomplete="off"
                                   <c:if test="${leader == '1'}">readonly unselectable="on"</c:if>
                                   class="layui-input" value="${val.grpf181129001}"
                                   onchange="setTotalscores('grpf181129001')">
                        </td>
                        <td width="5%" style="font-size: 14px;">
                            <input type="number" name="pf18112900001" lay-verify="pf18112900001" autocomplete="off"
                                   <c:if test="${leader == '0'}">readonly unselectable="on"</c:if>
                                   class="layui-input" value="${val.pf18112900001}"
                                   onchange="setTotalscores('pf18112900001')">

                        </td>
                        <td width="20%" style="font-size: 14px;">
                            <textarea name="pfck181129001" lay-verify="pfck181129001"
                                      class="layui-textarea">${val.pfck181129001}</textarea>
                        </td>
                        <td width="10%" style="font-size: 14px;">
                            <input type="text" name="bz18112900002" lay-verify="bz18112900002" autocomplete="off"
                                   class="layui-input" value="${val.bz18112900002}">
                        </td>
                        <td width="5%" style="font-size: 14px;">
                            <textarea name="fj18112900001" lay-verify="fj18112900001" class="layui-textarea"
                                      style="height: 100% !important;">${val.fj18112900001}</textarea>
                        </td>
                    </tr>
                    <c:set value="${totalscores + val.pf18112900001}" var="totalscores"/>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
        <!--endprint1-->
    </div>
    <script>
        $(function () {
            setTotalscores("into");
        });

        function setTotalscores(val) {
            var num = ${totalscores};
            if (val !== "into") {
                num = 0;
                $("input[name='" + val + "']").each(function (j, item) {
                    num += parseInt(item.value);
                });
            }
            $("#totalscores").val(num);
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

        function save() {
            var data = [];
            $("table tbody").find("tr").each(function () {
                var tdArr = $(this).children();
                var json = {};
                json.recorderNO = $(this).attr("id");//主键
                json.khry181129001 = "${userName}";//考核人员
                json.ssbm181129001 = "${deptId}";//所属部门
                json.khrq181129001 = "${date}";//考核日期
                json.khx1811290001 = tdArr.eq(0).find("input").val();//绩效关联键
                json.grpf181129001 = parseInt(tdArr.eq(5).find("input").val());//评分
                json.pf18112900001 = parseInt(tdArr.eq(6).find("input").val());//评分
                json.pfck181129001 = tdArr.eq(7).find("textarea").val();//评分参考
                json.bz18112900002 = tdArr.eq(8).find("input").val();//附件
                json.fj18112900001 = tdArr.eq(9).find("textarea").val();//备注
                json.leader = "${leader}";
                data.push(json)
            });
            var jsons = JSON.stringify(data);
            $.ajax({
                type: "POST",
                url: "/examination/save.do",
                data: {datas: jsons},
                dataType: "html",
                success: function (data) {
                    layer.msg("保存成功。");
                    window.history.go(-1);
                },
                error: function (message) {
                    console.log(message.toString())
                }
            });
        }
    </script>
</c:if>
</body>
</html>

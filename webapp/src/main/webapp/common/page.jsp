<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/08/07
  Time: 上午 11:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/common/var.jsp"></jsp:include>
<jsp:include page="/common/meta.jsp"></jsp:include>
<title></title>
<jsp:include page="/common/css.jsp"></jsp:include>
<jsp:include page="/common/js.jsp"></jsp:include>
<style type="text/css">
    .btn {
        /*color: rgb(50, 151, 233);*/
        /*background-color: #F6F6F6;*/
        cursor: pointer;
        float: none;
        margin-left: auto;
        padding: 5px 10px;
    }
</style>
<script type="text/javascript">
    function doSubmitForm(value) {
        if (isNaN(value)) {
            alert("请输入0-9的数字!");
            document.getElementById('page').value = "";
            return false;
        } else if (value === null || value === "" || value <= 0) {
            document.getElementById("currentPage").value = 1;
        } else if (parseInt(value) >parseInt('${requestScope.page.totalPage}')) {
            document.getElementById("currentPage").value = '${requestScope.page.totalPage}';
        } else {
            document.getElementById("currentPage").value = value;
        }
        document.forms[0].submit();
    }
    function chageSize() {
        $('#currentPage').val('1');
        document.forms[0].submit();
    }
</script>
<table style="font-size: 12px;" align="right">
    <tr>
        <c:if test="${requestScope.page.totalRecord > 0}">
            <td style="border: 0;">
                共<span class='totalRecord'>${requestScope.page.totalRecord}条记录&nbsp;&nbsp;每页
                <select name="pageSize" id="pageSize" onchange="chageSize()" title="">
                    <option value="5"  <c:if test="${requestScope.page.pageSize == 5}">selected</c:if>>5</option>
                    <option value="10" <c:if test="${requestScope.page.pageSize == 10}">selected</c:if>>10</option>
                    <option value="15" <c:if test="${requestScope.page.pageSize == 15}">selected</c:if>>15</option>
                    <option value="20" <c:if test="${requestScope.page.pageSize == 20}">selected</c:if>>20</option>
                    <option value="30" <c:if test="${requestScope.page.pageSize == 30}">selected</c:if>>30</option>
                </select>
                条&nbsp;&nbsp;第
                ${requestScope.page.currentPage}
                页/共
                ${requestScope.page.totalPage}
                页
                <input type="button" class="btn addNewButton btn-info" value="首页"
                       onclick="<c:if test="${requestScope.page.totalPage > 1}">
                               doSubmitForm(getElementById('firstPage').value);</c:if>"/>
                <input type="hidden" value="${1}" id="firstPage"/>


                <c:if test="${requestScope.page.currentPage > 1 &&
                    requestScope.page.currentPage <= requestScope.page.totalPage}">
                    <input type="button" class="btn addNewButton btn-info" value="上一页"
                           onclick="doSubmitForm(getElementById('upPage').value);"/>
                    <input type="hidden" value="${requestScope.page.upPage}"
                           id="upPage"/>
                </c:if>

                <c:if test="${requestScope.page.currentPage >= 1 &&
                    requestScope.page.currentPage < requestScope.page.totalPage}">
                    <input type="button" class="btn addNewButton btn-info" value="下一页"
                           onclick="doSubmitForm(getElementById('downPage').value);"/>
                    <input type="hidden" value="${requestScope.page.downPage}" id="downPage"/>
                </c:if>

                <input type="button" class="btn addNewButton btn-info" value="末页"
                       onclick="<c:if test="${requestScope.page.totalPage > 1 }">
                               doSubmitForm(getElementById('endPage').value);</c:if>"/>
                <input type="hidden" value="${requestScope.page.totalPage}" id="endPage"/>
                转到
                <input type="text" style="width:30px;" id="page" size="2" title="">
                页
                <input type="button" value="GO" class="btn addNewButton btn-info"
                       onclick="<c:if test="${requestScope.page.totalPage > 1}">
                               doSubmitForm(getElementById('page').value);</c:if>">
                <input type="hidden" name="currentPage" id="currentPage"
                       value="${requestScope.page.currentPage}"/>
            </td>
        </c:if>
        <c:if test="${requestScope.page.totalRecord == 0}">
            <td colspan="99" style="border: 0;">
                <div align="left">
                    <span style="color: red">没有符合条件的记录，请输入条件后重试!</span>
                </div>
            </td>
        </c:if>
    </tr>

</table>

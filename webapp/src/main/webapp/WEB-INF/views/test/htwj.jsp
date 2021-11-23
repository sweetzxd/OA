<%--
  User: zxd
  Date: 2019/05/16
  Time: 上午 11:44
  Explain: 说明
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <title>WebOffice 在线编辑WORD ,EXCEL等文档处理演示</title>
    <style rel="stylesheet" type="text/css">
        body{font-size:15px;}
        table{font-size:15px;}
    </style>
</head>
<script language="javascript" type ="text/javascript" >
    var pfile='20161108144356.doc';
    var varpath = decodeURI(window.location.pathname);
    var strRoot = varpath.substring(1,varpath.lastIndexOf('/')+1);//取得当前页路径
    if (strRoot =='')strRoot = varpath.substring(1,varpath.lastIndexOf('\\')+1);
    var strOpenUrl = strRoot + "file/" + pfile ; //取得打开路径和文件名
    var strSaveUrl = strRoot + "file/" + pfile ; //取得保存路径和文件名
    var strSmartUrl = "WebOffice://|Officectrl|" + window.location.href;//开启智能窗模式
    function OpenDoc(flag)
    {
        var strPath='edit.html';
        ShowPage(strRoot,strPath);
    }
    function ShowPage(root,path)
    {
        var pre = "WebOffice://|Officectrl|";//智能窗打开的固定参数
        var v=getBrowser();
        if(v==1){//当浏览器返回值为1时定义为使用智能窗方式打开网址
            strUrl = pre + root      ;
            window.open(strUrl,'_self');
        }
        else
        { //当浏览器返回值1以外的数据时采用传统方式打开网址
            strUrl = root + path;
            window.open(strUrl,'_blank');
        }
    }
    function getBrowser(){
        var Sys = {};
        var ua = navigator.userAgent.toLowerCase();
        var s;
        var ver;
        (s = ua.match(/edge\/([\d.]+)/)) ? Sys.edge = s[1] :
            (s = ua.match(/rv:([\d.]+)\) like gecko/)) ? Sys.ie = s[1] :
                (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
                    (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
                        (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
                            (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
                                (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
        if (Sys.edge) return 1;
        if (Sys.ie) return 0;
        if (Sys.firefox) return 1;
        if (Sys.chrome){ ver = Sys.chrome;ver.toLowerCase();var arr = ver.split('.');if(parseInt(arr[0])>43){return 1;}else{return 0;}}
        if (Sys.opera) return 1;
        if (Sys.safari) return 1;
        return 1;
    }
    function openfile()
    {

        document.getElementById('WebOffice').Open(strOpenUrl,true,"Word.Document","","");
    }
    function WebSave()
    {
        document.getElementById('WebOffice').ActiveDocument.SaveAs(strSaveUrl);
        alert('表单数据和office文档保存成功!');
        location.reload();
    }
</script>

<body>
<div style="text-align:center;"><h2><font color="#0099ff">WebOffice 在线编辑WORD ,EXCEL等文档处理演示</font></h2><br><br>
    <br><b><font style="font-size:12pt;color:green">自动识动Office文档类型</font><br><br>
        <table width=90% bgcolor=black cellpadding=1 cellspacing=1 align=center>
            <tr bgcolor=#cccccc>
                <th align=center nowrap><b>文件编号</b></td>
                <th><b>文件名</b></td>
                <th><b>类型</b></td>
                <th><b>文件大小</b></td>
                <th align=center><b>操作</b></td>
            </tr>
            <tr>
                <td bgcolor=white align=center>230</td>
                <td bgcolor=white>测试文档20161108144356</td>
                <td bgcolor=white><a href="javascript:OpenDoc(1);"><img src="images/doc.gif" border=0></a>&nbsp;&nbsp;</td>
                <td bgcolor=white>11685</td><td bgcolor=white align=center> <a href="javascript:OpenDoc(1);">编辑</a>&nbsp;&nbsp;
            </tr>
        </table>

    </b>
</div>
</body>
</html>


function getTableHtml() {
    oA.ele = OA.ele(["#template", "#tablelist"]);
    oA.ele.template.load("/views/template/tableInfo.html", function (content) {
        $(this).html(content);
        $.getJSON("/weixin/tableform/tablefield.do?pageid=" + OA.operation.getQueryString('pageid'), function (data) {
            var thead = oA.transform.content("table-thead", data);
            var tbody = oA.transform.content("table-tbody", data);
            if (tbody === null || tbody === "") {
                tbody = "<tr class=\"my-page-tr\"><td colspan='4'>没有数据</td></tr>";
            }
            oA.ele.tablelist.children().first().html(thead).next().html(tbody);
        });
    });
}



function tableHtml(){
    var urlval = manyValues();
    $("#template").load("/views/template/tableInfo.html", function (content) {
        $(this).html(content);
    });
    oA.transform.content.helper("getidtoname", function (type,value,data) {
        console.log(type,value,data);
        return oa.decipher(type,value,data);
    });
    oA.transform.content.helper("getfilehtml", function (id,url) {
        var html = "";
        if(url!=null && url!="") {
            var urls = url.split("|");
            for (var i = 0, len = urls.length - 1; i < len; i++) {
                var u = urls[i];
                var names = u.split("/");
                html+=filedTableValue("111", u, names[names.length]);
            }
        }
        return html;
    });
    $.getJSON("/weixin/tableform/tablefield.do?userid="+urlval.userid+"&pageid="+urlval.pageid+"&formid="+urlval.formid, function (data) {
        var thead = oA.transform.content("table-thead", data);
        if(data.thead==null || data.thead==""){
            thead = "";
        }
        var tbody = oA.transform.content("table-tbody", data);
        if(tbody==null || tbody==""){
            tbody = "<tr class=\"my-page-tr\"><td colspan='4'>没有数据</td></tr>";
        }
        $('.header-bar-title').html(data.title);
        $('#tablelist thead').html(thead);
        $('#tablelist tbody').html(tbody);
        var thm = $("#tablelist").height();
        $('#wrapper').height(thm);
        $('#scroller').height(thm+60);
    });
}


function getNextTableHtml(page){

    var urlval = manyValues();
    oA.transform.content.helper("getidtoname", function (type,value,data) {
        console.log(type,value,data);
        return oa.decipher(type,value,data);
    });
    $.getJSON("/weixin/tableform/tablefield.do?userid="+urlval.userid+"&pageid="+urlval.pageid+"&formid="+urlval.formid+"&page="+page, function (data) {
        console.log(data)
        if(data.tbody==null || data.tbody.length==0){
            $('#table-down').html("没有更多数据！");
        }else{
            var tbody = oA.transform.content("table-tbody", data);
            var html = $('#tablelist tbody').html();
            $('#tablelist tbody').html(html+tbody);
        }
        var thm = $("#tablelist").height();
        console.log(2,thm,$('#scroller')[0].offsetHeight)
        $('#scroller').height(thm+60);
    });
}


function getLoadTableHtml(){
    var urlval = manyValues();
    oA.transform.content.helper("getidtoname", function (type,value,data) {
        console.log(type,value,data);
        return oa.decipher(type,value,data);
    });
    $.getJSON("/weixin/tableform/tablefield.do?userid="+urlval.userid+"&pageid="+urlval.pageid+"&formid="+urlval.formid, function (data) {
        console.log(data);
        var tbody = oA.transform.content("table-tbody", data);
        if(tbody==null || tbody==""){
            tbody = "<tr class=\"my-page-tr\"><td colspan='4'>没有数据</td></tr>";
        }
        $('#tablelist tbody').html(tbody);
        var thm = $("#tablelist").height();
        console.log(3,thm)
        $('#scroller').height(thm+60);
    });
}
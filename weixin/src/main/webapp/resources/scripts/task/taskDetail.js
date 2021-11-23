var Manager = {
    url : "/weixin/workflow/logpage.do"+location.search,
    url2 : "/weixin/workflow/gototask.do"+location.search,
    init : function() {
        $.ajax({
            type:"GET",
            url:Manager.url,
            dataType:"json",
            success:function(d){
                if(d.success){
                    var result ="";
                    for(var i=0; i<d.wkflwlog.length; i++){
                        var $table = $(d.wkflwlog[i].inParams);
                        var title = $table.find("tr:first").text().split("-")[0];
                        var tr_last = $table.find("tr:last").text();
                        var examUser = $table.find("tr:last").find("div:first").text().split(":")[1];
                        var examTime = $table.find("tr:last").find("div:last").text();
                        examTime = examTime.substring(examTime.indexOf(":")+1,examTime.length);
                        var collapsed = "";
                        var show="";
                        if(i<d.wkflwlog.length-1){
                            collapsed = " collapsed";
                        }else{
                            show = " show";
                        }
                        result+="<div class=\"my-mission mar-b\">";
                        result += " <div class='task-header box-bar"+collapsed+"' onclick=\"collapseSixios(this,'collapseSix"+i+"')\">\n" +
                            "                <div class='task-tit box-bar-list corb font-bold font-16'>"+title+"</div>\n" +
                            "                <div class='task-tit cell mar-r'>"+examUser+"</div>\n" +
                            "                <div class='task-tit cell mar-r15'>"+examTime+"</div>\n" +
                            "                <div class='task-header-icon cell'></div>\n" +
                            "          </div> "+
                            "           <div id='collapseSix"+i+"' class='collapse"+show+"'>\n" +
                            "                <div class=\"task-body\">\n" +
                            "                    <div class='task-body-title'>"+title+"</div>"+
                            "                    <div class=\"task-body-content\">";
                        var trArr = $table.find("tr");
                        for(var j=1;j<trArr.length-1; j++){   //循环tr   不循环第一行和最后一行
                            var tdArr = $(trArr[j]).find("td");
                            result += "         <div class=\"circumstance-bar box-bar\">\n" +
                                "                     <div class='circumstance-title cell'>"+$(tdArr[0]).text()+"</div>\n" +
                                "                     <div class='box-bar-list corh'>"+$(tdArr[1]).text()+"</div>\n" +
                                "               </div>\n" +
                                "               <div class='circumstance-bar box-bar'>\n" +
                                "                     <div class='circumstance-title cell'>"+$(tdArr[2]).text()+"</div>\n" +
                                "                     <div class='box-bar-list corh'>"+$(tdArr[3]).text()+"</div>\n" +
                                "               </div>";
                        }
                        result += "</div>"+
                            "<div class='task-body-footer'><span class='mar-r corb'>"+examUser+"</span><span class='corb'>"+examTime+"</span>"+
                            "</div>";
                        result += "</div></div></div>";
                    }
                    $("#accordion").append(result);
                }else{

                }
            },error:function (d) {

            }
        });
    },
    gototask:function () {
        $.ajax({
            type: "GET",
            url: Manager.url2,
            dataType: "json",
            success: function (d) {
                if(d.success==1){
                    window.location.href=d.url;
                }

            },error:function (d) {

            }
        })
    }
};
Manager.init();

function collapseSixios(obj,id){
    $(obj).toggleClass("collapsed");
    $("#"+id).toggleClass("show");
    console.log($(obj).parent().siblings())
    console.log($("#"+id).parent().siblings())
    $(obj).parent().siblings().children("div:first-child").attr("class","task-header box-bar collapsed");
    $(obj).parent().siblings().children("div:last-child").attr("class","collapse");
}
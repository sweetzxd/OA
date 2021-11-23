$(document).ready(function(){
    $.ajax({
        type: "POST",
        url: "/weixin/usermenu.do",
        dataType: "html",
        async: false,
        success: function (data) {
            console.log(data)
            menuGeneration(JSON.parse(data));
        },
        error: function (message) {

        }
    });

});
function menuGeneration(menu){
    for(var m in menu){
        $(".menuskip-box").append(getMenuHtml(menu[m]));
    }
}

function getMenuHtml(data){
    var html = "";
    var id = data["id"];
    var title = data["title"];
    var num = sum(1,5);
    html +="<a href=\"javascript:;\" class=\"function-list\" title=\""+title+"\">";
    html +="<img src=\"/resources/images/menuimg"+num+".png\">";
    html +="<label>"+title+"</label>";
    html +="</a>";
    return html;
}

function sum (m,n){
    return Math.floor(Math.random()*(m - n) + n);
}
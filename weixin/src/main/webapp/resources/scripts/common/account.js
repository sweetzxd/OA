function logout(){
    $.get("/weixin/logout.do");
    window.location.href="/login.html";
}

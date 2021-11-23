//发送手机验证码
var countdown = 60;
function settime(obj) {
    if (countdown == 0) {
        obj.removeAttribute("disabled");
        $(obj).text("获取验证码")
        $(obj).removeClass("on");
        countdown = 60;
        return;
    } else {
        obj.setAttribute("disabled", true);
        $(obj).text("重新发送(" + countdown + ")");
        $(obj).addClass("on");
        countdown--;
    }
    setTimeout(function () {
        settime(obj)
    }, 1000)
}

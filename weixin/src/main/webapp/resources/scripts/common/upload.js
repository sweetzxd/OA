function tclick(id) {//触发文件选择
    $('#'+id).click();
}

function uploadf(_this,id) {//上传
    var fileList = _this.files;
    var len = fileList.length;
    var formData = new FormData();
    for (var i = 0; i < len; i++) {
        formData.append("files", fileList[i]);
    }
    $.ajax({
        url: "/uploading/files?pid="+projId,// url地址
        type: 'post',
        data: formData,
        contentType: false,
        processData: false
    }).done(function (data) {
        var urls = "";
        for (var i = 0, j = data.obj.length; i < j; i++) {
            var uplUrl = data.obj[i].message;//获取上传文件相对路径
            urls+= uplUrl+"|";
            var uname = uplUrl.split("/");
            var uplName = uname[uname.length-1];//获取文件名
            var id = uplName.split(".")[0];//获取文件名即id
            var type = uplName.split(".")[1];//获取文件类型
        }
        $('#'+id).val(urls);
    }).fail(function (data) {
        console.info(data);
    });
}
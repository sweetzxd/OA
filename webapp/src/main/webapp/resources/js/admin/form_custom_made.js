
function formcmsave(value) {
    var saveurl = "";
    var savedata = "";
    if (value == "ins") {
        saveurl = "platform/admin/formcminsert.do";
        ;
    } else if (value == "modi") {
        saveurl = "platform/admin/formcmupdate.do";
    }
    $.ajax({
        type: "POST",
        url: saveurl,
        data: $('#formcm_form').serialize(),
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        cache: false,
        dataType: "json",
        // 成功后开启模态框
        success: function (data) {
            $("#rigthPage").load("/platform/admin/formcmselect.do");
        },
        error: function () {
            alert("请求失败");
        }
    });
}

function updateformcm(formcmName) {
    $.ajax({
        type: "POST",
        url: "platform/admin/formcmupdate/" + formcmName + ".do",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        cache: false,
        dataType: "json",
        // 成功后开启模态框
        success: function (data) {
            $("#LargeModalLabel").text("修改字段");
            $("#formcmName").val(data.formcmName);
            $("#formcmName").attr("readonly", "readonly");
            $("#formcmTitle").val(data.formcmTitle);
            $("#formTask").val(data.formTask);
            $("#listTask").val(data.listTask);
            $('#formcmsave').attr('onclick', "formcmsave('modi')");
            $('#formcm_modal').modal('show');
        },
        error: function () {
            alert("请求失败");
        }
    });
}

function removeformcm(formcmName) {
    $.ajax({
        type: "POST",//方法类型
        url: "platform/admin/formcmdelete/" + formcmName + ".do",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        dataType: "html",
        cache: false,
        success: function (data) {
            $("#rigthPage").load("platform/admin/formcmselect.do");
        },
        error: function (error) {
            alert("请求失败");
        }
    });
}
function formcm_search(){
    var option = $("#formcm_zd").val();
    var inputval = $("#formcm_cx").val();
    $("#rigthPage").load("/platform/admin/formcmselect.do?option="+option+"&inputval="+inputval);
}

function editpage(value){
    $("#rigthPage").load("/platform/admin/formedit.do?formid="+value);
}

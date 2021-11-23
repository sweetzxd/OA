$().ready(function () {
    $("#formtable").validate({
        errorPlacement: function (error, element) {
            error.appendTo(element.parent().find("p"));
        }
        , onfocusout: function (element) {
            $(element).valid();
        }
    });
});

$.validator.addMethod("laterTo", function (value, element, param) {
    var later = $("#" + param).val();
    if (later != null && later != "" && value != null && value != "") {
        var startDate = new Date(later.replace(/-/g, "/"));
        var endDate = new Date(value.replace(/-/g, "/"));
        console.log(startDate,endDate);
        return startDate <= endDate;
    } else {
        return true;
    }
}, "结束时间不能小于开始时间");

    $.validator.addMethod("toLater", function (value, element, param) {
    var later = $("#" + param).val();
    if (later != null && later != "" && value != null && value != "") {
        var startDate = new Date(later.replace(/-/g, "/"));
        var endDate = new Date(value.replace(/-/g, "/"));
        return startDate >= endDate;
    } else {
        return true;
    }
}, "开始时间不能大于结束时间");

/*$.validator.addMethod("idcardno", function (value, element) {
    return this.optional(element) || isIdCardNo(value);
}, "请正确输入身份证号码");*/

/*$.validator.addMethod("alnum", function (value, element) {
    return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
}, "只能包括英文字母和数字");*/

/*$.validator.addMethod("zipcode", function (value, element) {
    var tel = /^[0-9]{6}$/;
    return this.optional(element) || (tel.test(value));
}, "请正确填写邮政编码");*/

/*$.validator.addMethod("chcharacter", function (value, element) {
    var tel = /^[u4e00-u9fa5]+$/;
    return this.optional(element) || (tel.test(value));
}, "请输入汉字");*/

/*$.validator.addMethod("stringMinLength", function (value, element, param) {
    var length = value.length;
    for (var i = 0; i < value.length; i++) {
        if (value.charCodeAt(i) > 127) {
            length++;
        }
    }
    return this.optional(element) || (length >= param);
}, $.validator.format("长度不能小于 {0}!"));*/

/*$.validator.addMethod("string", function (value, element) {
    return this.optional(element) || /^[u0391-uFFE5w]+$/.test(value);
}, "不允许包含特殊符号!");*/

/*$.validator.addMethod("mobile", function (value, element) {
    var length = value.length;
    return this.optional(element) || (length == 11 && /^(((13[0-9]{1})|(15[0-9]{1}))+d{8})$/.test(value));
}, "手机号码格式错误!");*/



/*$.validator.addMethod("decimal", function (value, element, param) {
    var re = true;
    if (value != null && value.indexOf(".") >= 0) {
        var deci = value.toString().split(".")[1].length;
        if (deci > param) {
            re = false;
        }
    }
    return this.optional(element) || re;
}, $.validator.format("小数位数不正确,长度不能超过 {0} 位!"));*/

/*$.validator.addMethod("carrydept", function (value, element, param) {
    var id = param + "_Value";
    var deptval = $("#"+id).val();
    if(deptval==null || deptval==""){
        var dept = oa.decipher("todeptid+name",value,null);
        if(dept!=null && dept!=""){
            updateValInput(param,dept.split("-")[0],dept.split("-")[1]);
        }
    }
    return true;
}, "");*/

/*
$.validator.addMethod("formula", function (value, element, param) {
    var field = $(element).attr("id").split("_")[1];
    var tableid = $(element).attr("id").split("_")[0];
    var ids = param.split(",");
    ids=$(".form-formula").find("input");
    for(var i=0;i<ids.length;i++) {
        //计算公式
        var gs = $(ids[i]).val();
        if (gs.indexOf(field)>=0) {

            //结果字段
            var gsz = $(ids[i]).attr("data");
            var ce = new CalcEval();
            //公式所用字段数组
            var getField = excludeSpecial(gs);
            var nu = {};
            var le = getField.length;
            for (var j = 0; j < le; j++) {
                //赋值到集合中
                var num = $("#" + tableid + "_" + getField[j]+"_Value").val();
                if(num==null){
                    num = 0;
                }

                nu[getField[j]] = num;
            }
            //进行公式计算
            var val = ce.complexEval(listModiFormat(getField, gs, nu)) + "";
            //结果字段
            var res = tableid + "_" + gsz+"_Value";
            $("#" + res).val(val);

            //更新 layui 的data ？？
            /!*up[res] = val;
            if (up != null) {
                obj.update(up);
            }*!/
            //结束更新data
        }
    }
    return true;
}, "");*/

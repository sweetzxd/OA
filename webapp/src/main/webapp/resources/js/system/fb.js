$(document).ready(function () {
    $("form").delegate(".component", "mousedown", function (md) {
        $(".popover").remove();

        md.preventDefault();
        var tops = [];
        var mouseX = md.pageX;
        var mouseY = md.pageY;
        var $temp;
        var timeout;
        var $this = $(this);
        var delays = {
            main: 120,
            form: 120
        }
        var type;

        if ($this.parent().parent().parent().parent().attr("id") === "components") {
            type = "main";
        } else {
            type = "form";
        }


        var delayed = setTimeout(function () {
            if (type === "main") {
                $temp = $("<form class='form-horizontal span6' id='temp'></form>").append($this.clone());
            } else {
                if ($this.attr("id") !== "legend") {
                    $temp = $("<form class='form-horizontal span6' id='temp'></form>").append($this);
                }
            }
            $("body").append($temp);
            $temp.css({
                "position": "absolute",
                "top": mouseY - ($temp.height()) + "px",
                "left": mouseX - ($temp.width()) + "px",
                "opacity": "0.9"
            }).show()

            var half_box_height = ($temp.height() / 2);
            var half_box_width = ($temp.width() / 2);
            var $target = $("#target");
            var tar_pos = $target.position();
            var $target_component = $("#target .component");

            $(document).delegate("body", "mousemove", function (mm) {

                var mm_mouseX = mm.pageX;
                var mm_mouseY = mm.pageY;

                $temp.css({
                    "top": mm_mouseY - half_box_height + "px",
                    "left": mm_mouseX - half_box_width + "px"
                });

                if (mm_mouseX > tar_pos.left &&
                    mm_mouseX < tar_pos.left + $target.width() + $temp.width() / 2 &&
                    mm_mouseY > tar_pos.top &&
                    mm_mouseY < tar_pos.top + $target.height() + $temp.height() / 2
                ) {
                    $("#target").css("background-color", "#fafdff");
                    $target_component.css({"border-top": "1px solid white", "border-bottom": "none"});
                    tops = $.grep($target_component, function (e) {
                        return ($(e).position().top - mm_mouseY + half_box_height > 0 && $(e).attr("id") !== "legend");
                    });
                    if (tops.length > 0) {
                        $(tops[0]).css("border-top", "1px solid #22aaff");
                    } else {
                        if ($target_component.length > 0) {
                            $($target_component[$target_component.length - 1]).css("border-bottom", "1px solid #22aaff");
                        }
                    }
                } else {
                    $("#target").css("background-color", "#fff");
                    $target_component.css({"border-top": "1px solid white", "border-bottom": "none"});
                    $target.css("background-color", "#fff");
                }
            });

            $("body").delegate("#temp", "mouseup", function (mu) {
                mu.preventDefault();

                var mu_mouseX = mu.pageX;
                var mu_mouseY = mu.pageY;
                var tar_pos = $target.position();
                $("#target .component").css({"border-top": "1px solid white", "border-bottom": "none"});

                // acting only if mouse is in right place
                if (mu_mouseX + half_box_width > tar_pos.left &&
                    mu_mouseX - half_box_width < tar_pos.left + $target.width() &&
                    mu_mouseY + half_box_height > tar_pos.top &&
                    mu_mouseY - half_box_height < tar_pos.top + $target.height()
                ) {
                    $temp.attr("style", null);
                    // where to add
                    if (tops.length > 0) {
                        $($temp.html()).insertBefore(tops[0]);
                    } else {
                        $("#target fieldset").append($temp.append("\n\n\ \ \ \ ").html());

                    }
                } else {
                    // no add
                    $("#target .component").css({"border-top": "1px solid white", "border-bottom": "none"});
                    tops = [];
                }

                //clean up & add popover
                $target.css("background-color", "#fff");
                $(document).undelegate("body", "mousemove");
                $("body").undelegate("#temp", "mouseup");
                $("#target .component").popover({trigger: "manual"});
                $temp.remove();
                genSource();
            });
        }, delays[type]);

        $(document).mouseup(function () {
            clearInterval(delayed);
            return false;
        });
        $(this).mouseout(function () {
            clearInterval(delayed);
            return false;
        });
        if (type == "form") {
            var id = $this.find(".title").attr("title");
            $this.find(".title").on("click", function () {
                var thistype = $this.find(".title").attr("data-type");
                //var method = "offset";
                //var active = json(thistype);
                //active[method] ? active[method].call(this, $this) : '';
                fieldsetup(thistype, $this);
            });
        }

    });

    var genSource = function () {
        var $temptxt = $("<div>").html($("#build").html());
        $($temptxt).find(".component").attr({
            "title": null,
            "data-original-title": null,
            "data-type": null,
            "data-content": null,
            "rel": null,
            "trigger": null,
            "style": null
        });
        $($temptxt).find(".valtype").attr("data-valtype", null).removeClass("valtype");
        $($temptxt).find(".component").removeClass("component");
        $($temptxt).find("form").attr({"id": null, "style": null});
        //$("#source").val($temptxt.html().replace(/\n\ \ \ \ \ \ \ \ \ \ \ \ /g, "\n"));
    }

    //activate legend popover
    $("#target .component").popover({trigger: "manual"});
    //popover on click event

    /*$(".formtitle").on("click", function (e) {
        //console.log("$this:" + $(this).parent().html())
        var method = "offset";
        var active = json("title");
        //active[method] ? active[method].call(this, $(this).parent()) : '';
        fieldsetup("title",this);
    });*/

});

function fieldsetup(type, obj) {
    var json = encodeURIComponent(JSON.stringify(getFieldValue(obj)));
    layer.open({
        type: 2
        , offset: type
        , id: 'layer' + type
        , area: ['800px', '600px']
        , content: "/form_custom/gotoformsetup.do?type=" + type + "&data=" + json
        , btn: ['保存', '取消'] //只是为了演示
        , btnAlign: 'c' //按钮居中
        , shade: 0 //不显示遮罩
        , yes: function () {
            var datas = fieldFun();
            setFieldValue(obj, datas, type);
            obj.find('.value').val(JSON.stringify(datas));
            layui.use('form', function () {
                var form = layui.form;
                form.render();
            });
            layer.closeAll();
        }
        , btn2: function () {
            layer.closeAll();
        }
    });
}
function getFieldValue(obj){
    var fieldType = obj.find('.title').attr("data-type");
    var fieldName = isFieldId(obj.find('.title').attr("id"));
    var fieldTitle = obj.find('.title').text();
    var json = {};

    var fieldNum = 40;
    var fieldDigit = 0;
    if (fieldType == 'select' || fieldType == 'selects') {//字段选项
        fieldNum = obj.find('select').attr("maxlength");
    } else if (fieldType == 'upload' || fieldType == 'uploads') {//字段选项
        fieldName = isFieldId(obj.find('button').attr("id"));
        fieldTitle = obj.find('button').text();
        fieldNum = obj.find('input').attr("maxlength");
    } else if (fieldType == 'textarea') {
        fieldNum = obj.find('textarea').attr("maxlength");
    } else if (fieldType == 'decimal') {
        fieldDigit = obj.find('input').attr("decimal");
    } else {
        fieldNum = obj.find('input').attr("maxlength");
    }
    json["fieldName"] = fieldName;
    json["fieldTitle"] = fieldTitle;
    json["special"] = isNull(obj.find('input').attr("placeholder"));
    json["defaultVal"] = obj.find('p').text();
    json["optionVal"] = getSelValue(obj);
    json["fieldNum"] = parseInt(fieldNum);
    json["fieldDigit"] = parseInt(fieldDigit);
    json["fieldType"] = (fieldType==null || fieldType=="")?"text":fieldType;
    if (obj.find('span').css("display") == 'block') {
        json["required"] = 1;
    } else {
        json["required"] = 0;
    }
    return json;
}
function setFieldValue(obj, json, type) {
    obj.find('.title').attr("id", json["fieldName"]);//字段主键
    obj.find('.title').text(json["fieldTitle"]);//字段名称
    obj.find('input').attr("placeholder", json["special"]);//特殊定义
    obj.find('input').attr("maxlength", json["fieldNum"]);//字段长度
    if (json["required"] == 1) {//是否必填
        obj.find('span').css('display', 'block');
    } else {
        obj.find('span').css('display', 'none');
    }
    if (type == 'select' || type == 'selects') {//字段选项
        obj.find('select').empty();
        var sel = json["optionVal"].split("\n");
        for (var i = 0; i < sel.length; i++) {
            if(sel[i].indexOf(";")>=0){
                var vt = sel[i].split(";")
                obj.find('select').append("<option value='" + vt[0] + "'>" + vt[1] + "</option>");
            }else{
                obj.find('select').append("<option>" + sel[i] + "</option>");
            }
        }
    } else if (type == 'decimal') {//小数位数
        obj.find('input').attr("decimal", json["fieldDigit"]);
    }
    obj.find('p').text(json["defaultVal"])//默认值
}

function getSelValue(obj) {
    obj = obj.find("select").children("option");
    var selvalue = "";
    for (var i = 0, len = obj.length; i < len; i++) {
        var value = obj[i].value;
        var text = obj[i].innerText;
        text = text.replace(/[\n\r]/g, "");
        if (value != null && value != "") {
            selvalue += value + ";" + text + "\n";
        } else if (text.indexOf("请选择") < 0) {
            selvalue += text + "\n";
        }
    }
    selvalue = selvalue.substring(0, selvalue.length - 1);
    return selvalue;
}

function getTexttoId(text) {
    var fieldId = "";
    return fieldId;
}

function isNull(val) {
    if (val == null) {
        return ""
    } else {
        return val;
    }
}

function isFieldId(val) {
    if (val != null && val.indexOf("_Title") >= 0) {
        return val.substring(val.indexOf("_") + 1, val.indexOf("_Title"));
    } else {
        if (val != null && val.indexOf("_field") >= 0) {
            return val;
        }else{
            return val.substring(val.indexOf("_") + 1,val.length);
        }
    }
}

//搜索过滤器
var myFilter = function (value, text, id) {
    var result;
    if (escape(value).indexOf("%u") != -1) { //汉字
        result = text.indexOf(value) > -1;
    } else {
        var len = value.length
        result = ConvertPinyin(text).substring(0, len) === value || makePy(text)[0].toLowerCase().substring(0, len) === value || text.toLowerCase().indexOf(value) > -1 || (id === undefined ? false : id.indexOf(value) > -1);
    }
    if (result == true) {
        return false;
    } else {
        return true;
    }
};

//检测值是否不属于 select 项
var notOption = function (value, callback, origin) {
    var num = 0;
    layui.each(dds, function () {
        var othis = $(this)
            , text = othis.text()
            , id = othis.attr("layui-value")
            // ,not = text.indexOf(value) === -1
            , not = myFilter(value, text, id);
        //value为input框输入的值，
        //text是当前dd的值，即当前被遍历到的选项，
        //id为当前dd的layui-value属性
        if (value === '' || (origin === 'blur') ? value !== text : not) num++;
        origin === 'keyup' && othis[not ? 'addClass' : 'removeClass'](HIDE);
    });
    var none = num === dds.length;
    return callback(none), none;
};
function json(value) {
    console.log("进入json："+value);
    var active = "";
    if (value == 'text') {
        active = {
            offset: function (othis) {
                var type = othis.data('type');
                var _id = isFieldId(othis.find('.title').attr("id"));
                var _text = othis.find('.title').text();
                var _placeholder = isNull(othis.find('input').attr("placeholder"));
                var _helptext = othis.find('p').text();
                var _length = othis.find('input').attr("maxlength");
                var id = "<input class='input-large' type='text' name='id' id='id' value='" + _id + "'><br/>";
                var text = "<input class='input-large' type='text' name='label' id='label' value='" + _text + "'><br/>";
                var placeholder = "<input type='text' name='placeholder' id='placeholder' value='" + _placeholder + "'><br/>";
                var helptext = "<input type='text' name='helptext' id='helptext' value='" + _helptext + "'><br/>";
                var length = "<input type='int' name='length' id='length' value='" + _length + "'><br/>";
                var check;
                if (othis.find('span').css("display") == 'block') {
                    check = "<input type='checkbox' name='checkbox' id='checkbox' checked>";
                } else {
                    check = "<input type='checkbox' name='checkbox' id='checkbox'>";
                }
                layer.open({
                    type: 1
                    , offset: type //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
                    , id: 'layer' + type //防止重复弹出
                    , area: ['270px', '300px']
                    , content: "<form class='actform'> " +
                        "<div class='controls'> " +
                        "<label class='control-label'>字段主键</label> " + id +
                        "<label class='control-label'>字段名称</label>  " + text +
                        "<label class='control-label'>默认内容</label>  " + placeholder +
                        "<label class='control-label'>字段说明</label> " + helptext +
                        "<label class='control-label'>字段长度</label> " + length +
                        "<label class='control-label'>是否必填</label> " + check +
                        "</div> " +
                        "</form>"
                    , btn: ['保存', '取消'] //只是为了演示
                    , btnAlign: 'c' //按钮居中
                    , shade: 0 //不显示遮罩
                    , yes: function () {
                        othis.find('.title').attr("id", $('.actform #id').val());
                        othis.find('.title').text($('.actform #label').val());
                        othis.find('input').attr("placeholder", $('.actform #placeholder').val());
                        othis.find('input').attr("maxlength", $('.actform #length').val());
                        othis.find('p').text($('.actform #helptext').val());
                        console.log($('.actform #checkbox').is(':checked'))
                        if ($('.actform #checkbox').is(':checked')) {
                            othis.find('span').css('display', 'block');
                        } else {
                            othis.find('span').css('display', 'none');
                        }
                        layer.closeAll();
                    }
                    , btn2: function () {
                        layer.closeAll();
                    }
                });
            }
        };
    } else if (value == 'textarea') {
        active = {
            offset: function (othis) {
                var type = othis.data('type');
                var id = "<input class='input-large' type='text' name='id' id='id' value='" + isFieldId(othis.find('.title').attr("id")) + "'><br/>";
                var text = "<input class='input-large' type='text' name='label' id='label' value='" + othis.find('.title').text() + "'><br/>";
                var placeholder = "<input type='text' name='placeholder' id='placeholder' value='" + isNull(othis.find('textarea').attr("placeholder")) + "'><br/>";
                var helptext = "<input type='text' name='helptext' id='helptext' value='" + othis.find('p').text() + "'><br/>";
                var length = "<input type='int' name='length' id='length' value='" + othis.find('textarea').attr("maxlength") + "'><br/>";
                var check;
                if (othis.find('span').css("display") == 'block') {
                    check = "<input type='checkbox' name='checkbox' id='checkbox' checked>";
                } else {
                    check = "<input type='checkbox' name='checkbox' id='checkbox'>";
                }
                layer.open({
                    type: 1
                    , offset: type //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
                    , id: 'layer' + type //防止重复弹出
                    , area: ['270px', '300px']
                    , content: "<form class='actform'> " +
                        "<div class='controls'> " +
                        "<label class='control-label'>字段主键</label> " + id +
                        "<label class='control-label'>字段名称</label>  " + text +
                        "<label class='control-label'>默认内容</label>  " + placeholder +
                        "<label class='control-label'>字段说明</label> " + helptext +
                        "<label class='control-label'>字段长度</label> " + length +
                        "<label class='control-label'>是否必填</label> " + check +
                        "</div> " +
                        "</form>"
                    , btn: ['保存', '取消'] //只是为了演示
                    , btnAlign: 'c' //按钮居中
                    , shade: 0 //不显示遮罩
                    , yes: function () {
                        othis.find('.title').attr("id", $('.actform #id').val());
                        othis.find('.title').text($('.actform #label').val());
                        othis.find('textarea').attr("placeholder", $('.actform #placeholder').val());
                        othis.find('textarea').attr("maxlength", $('.actform #length').val());
                        othis.find('p').text($('.actform #helptext').val());
                        console.log($('.actform #checkbox').is(':checked'))
                        if ($('.actform #checkbox').is(':checked')) {
                            othis.find('span').css('display', 'block');
                        } else {
                            othis.find('span').css('display', 'none');
                        }
                        layer.closeAll();
                    }
                    , btn2: function () {
                        layer.closeAll();
                    }
                });
            }
        };
    } else if (value == 'select') {
        console.log("value==select")
        active = {
            offset: function (othis) {
                var type = othis.data('type');
                var id = "<input class='input-large' type='text' id='id' value='" + isFieldId(othis.find('.title').attr("id")) + "'><br/>";
                var text = "<input class='input-large' type='text' id='label' value='" + othis.find('.title').text() + "'><br/>";
                var select = "<textarea type='text' id='select' rows='5'>"+getSelValue(othis)+"</textarea><br/>";
                var helptext = "<input type='text' id='helptext' value='" + othis.find('p').text() + "'><br/>";
                var length = "<input type='int' id='length' value='" + othis.find('select').attr("maxlength") + "'><br/>";
                var check;
                if (othis.find('span').css("display") == 'block') {
                    check = "<input type='checkbox' name='checkbox' id='checkbox' checked>";
                } else {
                    check = "<input type='checkbox' name='checkbox' id='checkbox'>";
                }
                layer.open({
                    type: 1
                    , offset: type
                    , id: 'layer' + type //防止重复弹出
                    , area: ['270px', '300px']
                    , content: "<form class='actform'> " +
                        "<div class='controls'> " +
                        "<label class='control-label'>字段主键</label> " + id +
                        "<label class='control-label'>字段名称</label>  " + text +
                        "<label class='control-label'>字段选项</label>  " + select +
                        "<label class='control-label'>字段说明</label> " + helptext +
                        "<label class='control-label'>字段长度</label> " + length +
                        "<label class='control-label'>是否必填</label> " + check +
                        "</div> " +
                        "</form>"
                    , btn: ['保存', '取消'] //只是为了演示
                    , btnAlign: 'c' //按钮居中
                    , shade: 0 //不显示遮罩
                    , yes: function () {
                        othis.find('.title').attr("id", $('.actform #id').val());
                        othis.find('.title').text($('.actform #label').val());
                        othis.find('select').empty();
                        var sel = $('.actform #select').val().split("\n");
                        //othis.find('select').prepend("<option>value;text</option>");
                        for (var i=0;i<sel.length;i++){
                            var vt = sel[i].split(";")
                            othis.find('select').append("<option value='"+vt[0]+"'>"+vt[1]+"</option>");
                        }
                        othis.find('select').attr("maxlength", $('.actform #length').val());
                        othis.find('p').text($('.actform #helptext').val());
                        if ($('.actform #checkbox').is(':checked')) {
                            othis.find('span').css('display', 'block');
                        } else {
                            othis.find('span').css('display', 'none');
                        }
                        layer.closeAll();
                    }
                    , btn2: function () {
                        layer.closeAll();
                    }
                });
            }
        };
    } else if (value == 'selects') {
        console.log("value==selects")
        active = {
            offset: function (othis) {
                var type = othis.data('type');
                var id = "<input class='input-large' type='text' id='id' value='" + isFieldId(othis.find('.title').attr("id")) + "'><br/>";
                var text = "<input class='input-large' type='text' id='label' value='" + othis.find('.title').text() + "'><br/>";
                var select = "<textarea type='text' id='select' rows='5'>"+getSelValue(othis)+"</textarea><br/>";
                var helptext = "<input type='text' id='helptext' value='" + othis.find('p').text() + "'><br/>";
                var length = "<input type='int' id='length' value='" + othis.find('select').attr("maxlength") + "'><br/>";
                var check;
                if (othis.find('span').css("display") == 'block') {
                    check = "<input type='checkbox' name='checkbox' id='checkbox' checked>";
                } else {
                    check = "<input type='checkbox' name='checkbox' id='checkbox'>";
                }
                layer.open({
                    type: 1
                    , offset: type
                    , id: 'layer' + type //防止重复弹出
                    , area: ['270px', '300px']
                    , content: "<form class='actform'> " +
                        "<div class='controls'> " +
                        "<label class='control-label'>字段主键</label> " + id +
                        "<label class='control-label'>字段名称</label>  " + text +
                        "<label class='control-label'>字段选项</label>  " + select +
                        "<label class='control-label'>字段说明</label> " + helptext +
                        "<label class='control-label'>字段长度</label> " + length +
                        "<label class='control-label'>是否必填</label> " + check +
                        "</div> " +
                        "</form>"
                    , btn: ['保存', '取消'] //只是为了演示
                    , btnAlign: 'c' //按钮居中
                    , shade: 0 //不显示遮罩
                    , yes: function () {
                        othis.find('.title').attr("id", $('.actform #id').val());
                        othis.find('.title').text($('.actform #label').val());
                        othis.find('select').empty();
                        var sel = $('.actform #select').val().split("\n");
                        //othis.find('select').prepend("<option>value;text</option>");
                        for (var i=0;i<sel.length;i++){
                            var vt = sel[i].split(";")
                            othis.find('select').append("<option value='"+vt[0]+"'>"+vt[1]+"</option>");
                        }
                        othis.find('select').attr("maxlength", $('.actform #length').val());
                        othis.find('p').text($('.actform #helptext').val());
                        if ($('.actform #checkbox').is(':checked')) {
                            othis.find('span').css('display', 'block');
                        } else {
                            othis.find('span').css('display', 'none');
                        }
                        layer.closeAll();
                    }
                    , btn2: function () {
                        layer.closeAll();
                    }
                });
            }
        };
    } else if (value == 'update') {
        active = {
            offset: function (othis) {
                var type = othis.data('type');
                var id = "<input class='input-large' type='text' id='id' value='" + isFieldId(othis.find('button').attr("id")) + "'><br/>";
                layer.open({
                    type: 1
                    , offset: type
                    , id: 'layer' + type //防止重复弹出
                    , area: ['270px', '300px']
                    , content: "<form class='actform'> " +
                        "<div class='controls'> " +
                        "<label class='control-label'>字段主键</label> " + id +
                        "</div> " +
                        "</form>"
                    , btn: ['保存', '取消'] //只是为了演示
                    , btnAlign: 'c' //按钮居中
                    , shade: 0 //不显示遮罩
                    , yes: function () {
                        othis.find('button').attr("id", $('.actform #id').val());
                        layer.closeAll();
                    }
                    , btn2: function () {
                        layer.closeAll();
                    }
                });
            }
        };
    } else if (value == 'int') {
        active = {
            offset: function (othis) {
                var type = othis.data('type');
                var id = "<input class='input-large' type='text' name='id' id='id' value='" + isFieldId(othis.find('.title').attr("id")) + "'><br/>";
                var text = "<input class='input-large' type='text' name='label' id='label' value='" + othis.find('.title').text() + "'><br/>";
                var placeholder = "<input type='text' name='placeholder' id='placeholder' value='" + isNull(othis.find('input').attr("placeholder")) + "'><br/>";
                var helptext = "<input type='text' name='helptext' id='helptext' value='" + othis.find('p').text() + "'><br/>";
                var length = "<input type='int' name='length' id='length' value='" + othis.find('input').attr("maxlength") + "'><br/>";
                var check;
                if (othis.find('span').css("display") == 'block') {
                    check = "<input type='checkbox' name='checkbox' id='checkbox' checked>";
                } else {
                    check = "<input type='checkbox' name='checkbox' id='checkbox'>";
                }
                layer.open({
                    type: 1
                    , offset: type //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
                    , id: 'layer' + type //防止重复弹出
                    , area: ['270px', '300px']
                    , content: "<form class='actform'> " +
                        "<div class='controls'> " +
                        "<label class='control-label'>字段主键</label> " + id +
                        "<label class='control-label'>字段名称</label>  " + text +
                        "<label class='control-label'>默认内容</label>  " + placeholder +
                        "<label class='control-label'>字段说明</label> " + helptext +
                        "<label class='control-label'>字段长度</label> " + length +
                        "<label class='control-label'>是否必填</label> " + check +
                        "</div> " +
                        "</form>"
                    , btn: ['保存', '取消'] //只是为了演示
                    , btnAlign: 'c' //按钮居中
                    , shade: 0 //不显示遮罩
                    , yes: function () {
                        othis.find('.title').attr("id", $('.actform #id').val());
                        othis.find('.title').text($('.actform #label').val());
                        othis.find('input').attr("placeholder", $('.actform #placeholder').val());
                        othis.find('input').attr("maxlength", $('.actform #length').val());
                        othis.find('p').text($('.actform #helptext').val());
                        console.log($('.actform #checkbox').is(':checked'))
                        if ($('.actform #checkbox').is(':checked')) {
                            othis.find('span').css('display', 'block');
                        } else {
                            othis.find('span').css('display', 'none');
                        }
                        layer.closeAll();
                    }
                    , btn2: function () {
                        layer.closeAll();
                    }
                });
            }
        };
    } else if (value == 'decimal') {
        active = {
            offset: function (othis) {
                var type = othis.data('type');
                var id = "<input class='input-large' type='text' name='id' id='id' value='" + isFieldId(othis.find('.title').attr("id")) + "'><br/>";
                var text = "<input class='input-large' type='text' name='label' id='label' value='" + othis.find('.title').text() + "'><br/>";
                var placeholder = "<input type='text' name='placeholder' id='placeholder' value='" + isNull(othis.find('input').attr("placeholder")) + "'><br/>";
                var helptext = "<input type='text' name='helptext' id='helptext' value='" + othis.find('p').text() + "'><br/>";
                if(othis.find('input').attr("step")!=0){
                    var str = othis.find('input').attr("step").split('.');
                }
                var length = "<input type='int' name='length' id='length' value='" + str[1].length + "'><br/>";
                var check;
                if (othis.find('span').css("display") == 'block') {
                    check = "<input type='checkbox' name='checkbox' id='checkbox' checked>";
                } else {
                    check = "<input type='checkbox' name='checkbox' id='checkbox'>";
                }
                layer.open({
                    type: 1
                    , offset: type //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
                    , id: 'layer' + type //防止重复弹出
                    , area: ['270px', '300px']
                    , content: "<form class='actform'> " +
                        "<div class='controls'> " +
                        "<label class='control-label'>字段主键</label> " + id +
                        "<label class='control-label'>字段名称</label>  " + text +
                        "<label class='control-label'>默认内容</label>  " + placeholder +
                        "<label class='control-label'>字段说明</label> " + helptext +
                        "<label class='control-label'>小数位数</label> " + length +
                        "<label class='control-label'>是否必填</label> " + check +
                        "</div> " +
                        "</form>"
                    , btn: ['保存', '取消'] //只是为了演示
                    , btnAlign: 'c' //按钮居中
                    , shade: 0 //不显示遮罩
                    , yes: function () {
                        othis.find('.title').attr("id", $('.actform #id').val());
                        othis.find('.title').text($('.actform #label').val());
                        othis.find('input').attr("placeholder", $('.actform #placeholder').val());
                        othis.find('input').attr("maxlength", $('.actform #length').val());
                        othis.find('p').text($('.actform #helptext').val());
                        console.log($('.actform #checkbox').is(':checked'))
                        if ($('.actform #checkbox').is(':checked')) {
                            othis.find('span').css('display', 'block');
                        } else {
                            othis.find('span').css('display', 'none');
                        }
                        layer.closeAll();
                    }
                    , btn2: function () {
                        layer.closeAll();
                    }
                });
            }
        };
    }else if (value == 'date') {
        active = {
            offset: function (othis) {
                var type = othis.data('type');
                var id = "<input class='input-large' type='text' name='id' id='id' value='" + isFieldId(othis.find('.title').attr("id")) + "'><br/>";
                var text = "<input class='input-large' type='text' name='label' id='label' value='" + othis.find('.title').text() + "'><br/>";
                var helptext = "<input type='text' name='helptext' id='helptext' value='" + othis.find('p').text() + "'><br/>";
                var check;
                if (othis.find('span').css("display") == 'block') {
                    check = "<input type='checkbox' name='checkbox' id='checkbox' checked>";
                } else {
                    check = "<input type='checkbox' name='checkbox' id='checkbox'>";
                }
                layer.open({
                    type: 1
                    , offset: type //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
                    , id: 'layer' + type //防止重复弹出
                    , area: ['270px', '300px']
                    , content: "<form class='actform'> " +
                        "<div class='controls'> " +
                        "<label class='control-label'>字段主键</label> " + id +
                        "<label class='control-label'>字段名称</label>  " + text +
                        "<label class='control-label'>字段说明</label> " + helptext +
                        "<label class='control-label'>是否必填</label> " + check +
                        "</div> " +
                        "</form>"
                    , btn: ['保存', '取消'] //只是为了演示
                    , btnAlign: 'c' //按钮居中
                    , shade: 0 //不显示遮罩
                    , yes: function () {
                        othis.find('.title').attr("id", $('.actform #id').val());
                        othis.find('.title').text($('.actform #label').val());
                        othis.find('input').attr("maxlength", $('.actform #length').val());
                        othis.find('p').text($('.actform #helptext').val());
                        console.log($('.actform #checkbox').is(':checked'))
                        if ($('.actform #checkbox').is(':checked')) {
                            othis.find('span').css('display', 'block');
                        } else {
                            othis.find('span').css('display', 'none');
                        }
                        layer.closeAll();
                    }
                    , btn2: function () {
                        layer.closeAll();
                    }
                });
            }
        };
    }else if (value == 'datetime') {
        active = {
            offset: function (othis) {
                var type = othis.data('type');
                var id = "<input class='input-large' type='text' name='id' id='id' value='" + isFieldId(othis.find('.title').attr("id")) + "'><br/>";
                var text = "<input class='input-large' type='text' name='label' id='label' value='" + othis.find('.title').text() + "'><br/>";
                var helptext = "<input type='text' name='helptext' id='helptext' value='" + othis.find('p').text() + "'><br/>";
                var check;
                if (othis.find('span').css("display") == 'block') {
                    check = "<input type='checkbox' name='checkbox' id='checkbox' checked>";
                } else {
                    check = "<input type='checkbox' name='checkbox' id='checkbox'>";
                }
                layer.open({
                    type: 1
                    , offset: type //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
                    , id: 'layer' + type //防止重复弹出
                    , area: ['270px', '300px']
                    , content: "<form class='actform'> " +
                        "<div class='controls'> " +
                        "<label class='control-label'>字段主键</label> " + id +
                        "<label class='control-label'>字段名称</label>  " + text +
                        "<label class='control-label'>字段说明</label> " + helptext +
                        "<label class='control-label'>是否必填</label> " + check +
                        "</div> " +
                        "</form>"
                    , btn: ['保存', '取消'] //只是为了演示
                    , btnAlign: 'c' //按钮居中
                    , shade: 0 //不显示遮罩
                    , yes: function () {
                        othis.find('.title').attr("id", $('.actform #id').val());
                        othis.find('.title').text($('.actform #label').val());
                        othis.find('input').attr("maxlength", $('.actform #length').val());
                        othis.find('p').text($('.actform #helptext').val());
                        console.log($('.actform #checkbox').is(':checked'))
                        if ($('.actform #checkbox').is(':checked')) {
                            othis.find('span').css('display', 'block');
                        } else {
                            othis.find('span').css('display', 'none');
                        }
                        layer.closeAll();
                    }
                    , btn2: function () {
                        layer.closeAll();
                    }
                });
            }
        };
    }else {
        active = {
            offset: function (othis) {
                var type = othis.data('type');
                var _id = isFieldId(othis.find('.title').attr("id"));
                var _text = othis.find('.title').text();
                var _helptext = othis.find('p').text();
                var _length = othis.find('input').attr("maxlength");
                var id = "<input class='input-large' type='text' name='id' id='id' value='" + _id + "'><br/>";
                var text = "<input class='input-large' type='text' name='label' id='label' value='" + _text + "'><br/>";
                var helptext = "<input type='text' name='helptext' id='helptext' value='" + _helptext + "'><br/>";
                var check;
                if (othis.find('span').css("display") == 'block') {
                    check = "<input type='checkbox' name='checkbox' id='checkbox' checked>";
                } else {
                    check = "<input type='checkbox' name='checkbox' id='checkbox'>";
                }
                layer.open({
                    type: 1
                    , offset: type //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
                    , id: 'layer' + type //防止重复弹出
                    , area: ['270px', '300px']
                    , content: "<form class='actform'> " +
                        "<div class='controls'> " +
                        "<label class='control-label'>字段主键</label> " + id +
                        "<label class='control-label'>字段名称</label>  " + text +
                        "<label class='control-label'>字段说明</label> " + helptext +
                        "<label class='control-label'>是否必填</label> " + check +
                        "</div> " +
                        "</form>"
                    , btn: ['保存', '取消'] //只是为了演示
                    , btnAlign: 'c' //按钮居中
                    , shade: 0 //不显示遮罩
                    , yes: function () {
                        othis.find('.title').attr("id", $('.actform #id').val());
                        othis.find('.title').text($('.actform #label').val());
                        othis.find('p').text($('.actform #helptext').val());
                        console.log($('.actform #checkbox').is(':checked'))
                        if ($('.actform #checkbox').is(':checked')) {
                            othis.find('span').css('display', 'block');
                        } else {
                            othis.find('span').css('display', 'none');
                        }
                        layer.closeAll();
                    }
                    , btn2: function () {
                        layer.closeAll();
                    }
                });
            }
        };
    }
    if(value == 'title'){
        active = {
            offset: function (othis) {
                var type = othis.data('type');
                var id = "<input class='input-large' type='text' id='id' value='" + isFieldId(othis.find('legend').attr("id")) + "'><br/>";
                var text = "<input class='input-large' type='text' id='text' value='" + othis.find('legend').text() + "'><br/>";
                layer.open({
                    type: 1
                    , offset: type
                    , id: 'layer' + type //防止重复弹出
                    , area: ['270px', '160px']
                    , content: "<form class='actform'> " +
                        "<div class='controls'> " +
                        "<label class='control-label'>表名</label> " + id +
                        "<label class='control-label'>标题</label> " + text +
                        "</div> " +
                        "</form>"
                    , btn: ['保存', '取消'] //只是为了演示
                    , btnAlign: 'c' //按钮居中
                    , shade: 0 //不显示遮罩
                    , yes: function () {
                        othis.find('legend').attr("id", $('.actform #id').val());
                        othis.find('legend').text($('.actform #text').val());
                        layer.closeAll();
                    }
                    , btn2: function () {
                        layer.closeAll();
                    }
                });
            }
        };
    }
    return active;
}


function analysis(obj){
    var fieldName = "",fieldTitle = "";
    var fieldType = obj.find('.title').attr("data-type");
    var fieldNum = "",fieldDef = "",optionVal="";
    if(fieldType=='text'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = obj.find('input').attr("maxlength");
        fieldDef = isNull(obj.find('input').attr("placeholder"));
    }else if(fieldType=='textarea'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = obj.find('textarea').attr("maxlength");
        fieldDef = isNull(obj.find('textarea').attr("placeholder"));
    }else if(fieldType=='select'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = obj.find('select').attr("maxlength");
        optionVal = getSelValue(obj);
    }else if(fieldType=='selects'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = obj.find('select').attr("maxlength");
        optionVal = getSelValue(obj);
    }else if(fieldType=='int'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = obj.find('input').attr("maxlength");
        fieldDef = isNull(obj.find('input').attr("placeholder"));
    }else if(fieldType=='decimal'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = obj.find('input').attr("maxlength");
        if(obj.find('input').attr("step")!=0){
            var str = obj.find('input').attr("step").split('.');
            fieldDef =str[1].length+"";
        }else{
            fieldDef ="0";
        }
    }else if(fieldType=='date'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = obj.find('input').attr("maxlength");
        fieldDef = isNull(obj.find('input').attr("placeholder"));
    }else if(fieldType=='datetime'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = obj.find('input').attr("maxlength");
        fieldDef = isNull(obj.find('input').attr("placeholder"));
    }else if(fieldType=='user'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = obj.find('input').attr("maxlength");
        fieldDef = isNull(obj.find('input').attr("placeholder"));
    }else if(fieldType=='users'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = obj.find('input').attr("maxlength");
        fieldDef = isNull(obj.find('input').attr("placeholder"));
    }else if(fieldType=='dept'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = obj.find('input').attr("maxlength");
        fieldDef = isNull(obj.find('input').attr("placeholder"));
    }else if(fieldType=='depts'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = obj.find('input').attr("maxlength");
        fieldDef = isNull(obj.find('input').attr("placeholder"));
    }else if(fieldType=='form'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = obj.find('input').attr("maxlength");
        fieldDef = isNull(obj.find('input').attr("placeholder"));
    }else if(fieldType=='forms'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = obj.find('input').attr("maxlength");
        fieldDef = isNull(obj.find('input').attr("placeholder"));
    }else if(fieldType=='upload'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = 1024;
        fieldDef = "";
    }else if(fieldType=='uploads'){
        fieldName = obj.find('.title').attr("id");
        fieldTitle = obj.find('.title').text();
        fieldNum = 1024;
        fieldDef = "";
    }else{
        fieldNum = 1024;
        fieldDef = "";
    }

    var j = {};
    j.fieldName = isFieldId(fieldName);
    j.fieldTitle = fieldTitle;
    j.fieldType = fieldType;
    j.fieldNum = fieldNum;
    j.fieldDef = fieldDef;
    j.optionVal = optionVal;
    var check = obj.find('span').css("display");
    console.log(check)
    if(check == 'block'){
        j.required=1;
    }else{
        j.required=0;
    }
    return j;
}
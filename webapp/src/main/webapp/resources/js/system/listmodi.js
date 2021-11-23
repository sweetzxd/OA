var CalcEval = function () {
};
//复杂的计算方法（包含括号）
CalcEval.prototype.complexEval = function (str) {
    if (str == null) {
        return "";
    }
    if (typeof str != "string") {//转化成字符串
        str = str + ""
    }
    var multObj = str.match(/\([^()]+\)/g);//匹配括号
    //不断计算最底层括号的数据
    while (null != multObj) {
        var content = multObj[0] + "";
        var result = this.simpleEval(content.substr(1, content.length - 2));
        str = str.replace(multObj[0], result);
        multObj = str.match(/\([^()]+\)/g);//匹配括号
    }
    return this.simpleEval(str);
};
//简单的计算方法，只有加减乘除
CalcEval.prototype.simpleEval = function (str) {
    if (str == null) {
        return "";
    }
    if (typeof str != "string") {//转化成字符串
        str = str + ""
    }

    var valueArray = new Array();//值的数组
    var markArray = new Array();//符号的数组
    var tempValue = "";
    var ch = str.split("");
    var isOper = false;
    for (var i = 0; i < ch.length; i++) {
        if (ch[i] == "+" || ch[i] == "-" || ch[i] == "*" || ch[i] == "/") {//符号
            var dv = tempValue * 1;
            if (isOper) {
                var value = valueArray.pop();
                var mark = markArray.pop();
                dv = this.simpleTwoEval(mark, value, dv);
            }
            valueArray.push(dv);
            markArray.push(ch[i]);
            tempValue = "";
            isOper = false;
            if (ch[i] == "*" || ch[i] == "/")
                isOper = true;
        } else {
            tempValue += ch[i] + "";
            if (i == ch.length - 1) {//最后一位
                var dv = tempValue * 1;
                if (isOper) {
                    var dv1 = valueArray.pop();
                    var mark = markArray.pop();
                    var result = this.simpleTwoEval(mark, dv1, tempValue);
                    dv = result;
                }
                valueArray.push(dv);
            }
        }
    }

    valueArray = this.reverseArray(valueArray);
    markArray = this.reverseArray(markArray);
    while (valueArray.length > 1) {
        var v1 = valueArray.pop();
        var v2 = valueArray.pop();
        var mark = markArray.pop();
        valueArray.push(this.simpleTwoEval(mark, v1, v2));
    }
    return valueArray[0];

};
//两个数的加减乘除
CalcEval.prototype.simpleTwoEval = function (mark, value1, value2) {
    if (mark == "+") {
        return value1 + value2;
    } else if (mark == "-") {
        return value1 - value2;
    } else if (mark == "*") {
        return value1 * value2;
    } else if (mark == "/") {
        return value1 / value2;
    }
    return 0;
};
//反转数组
CalcEval.prototype.reverseArray = function (oldArray) {
    var newArray = new Array();
    var size = oldArray.length;
    for (var i = 0; i < size; i++) {
        newArray.push(oldArray.pop());
    }
    return newArray;

};

//获取公式中所有的字段
function excludeSpecial(formula) {
    var str = formula.replace(/\+/g, "-");
    str = str.replace(/\*/g, "-");
    str = str.replace(/\//g, "-");
    str = str.replace(/\(/g, "");
    str = str.replace(/\)/g, "");
    var re = str.split("-");
    var obj = {};
    re = re.reduce(function (item, next) {
        obj[next] ? '' : obj[next] = true && item.push(next);
        return item;
    }, []);

    return re;
}

//格式化公式，将字段替换为数值
function listModiFormat(re,formula, val){
    for (var i = 0, len = re.length; i < len; i++) {
        var ae = re[i];
        var reg = new RegExp(ae, "g");
        formula = formula.replace(reg, val[ae]);
    }
    return formula;
}

//测试
function test(){
    var nu = {"a1":4,"b2":3,"c3":6,"d4":5};
    var gs = "a1+b2-((a1+c3)/d4)*c3";
    var s = format(gs,nu);
    var ce = new CalcEval();
    var val = ce.complexEval(s);
    console.log(val)
}


function formula1(type){
    var f = "";
    $.ajax({
        type: "POST",
        url: "/finance/getfinance.do?type="+type,
        //contentType: "application/json; charset=utf-8",
        dataType: "html",
        async: false,
        success: function (data) {
            f = data;
        },
        error: function (message) {
        }
    });
    return f;
}

function listModiFormula(tableName){
    var f = "";
    $.ajax({
        type: "POST",
        url: "/finance/getfinance.do?tableName="+tableName,
        dataType: "html",
        async: false,
        success: function (data) {
            f = data;
        },
        error: function (message) {
        }
    });
    if(f!="0"){
        return JSON.parse(f);
    }else{
        return "0";
    }
}
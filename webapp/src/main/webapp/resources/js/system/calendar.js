;(function (undefined) {
    "use strict"
    var _global;
    var create = function () {
        var node = "";
        var curYear = $("#curYear option:selected").val();
        var curMonth = $("#curMonth option:selected").val();
        $.ajax({
            type: "POST",
            url: "/schedule/getworkingcalendar.do?year=" + curYear + "&month=" + curMonth,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            async: false,
            success: function (data) {
                console.log(data)
                node = eval(data);
            },
            error: function (message) {
                layer.msg("日程信息获取失败！");
            }
        });
        return node;
    };
    var calendar = {
        init: function initCalendar() {
            $(".rl-tbody").html("");
            var curYear = $("#curYear option:selected").val();
            var curMonth = $("#curMonth option:selected").val();
            var table = "";
            var trNum = 0, tdNum = 0, nextDay = 0;
            var upDay = getUpMonth(curYear, curMonth) - getWeekOneDay(curYear, curMonth);
            var cr = new create();
            for (var i = 0; i < 42; i++) {
                if (trNum == 0) {
                    table += "<tr>";
                }
                if (upDay < getUpMonth(curYear, curMonth)) {
                    upDay++;
                    table += "<td>" + getCalenderDay("empty", upDay, null) + "</td>";
                } else if (tdNum < getCountDays(curYear, curMonth)) {
                    tdNum++;
                    var crval = cr[tdNum];
                    if (tdNum < 10) {
                        crval = cr["0" + tdNum];
                    }
                    var type = "work";
                    var value = null;
                    if (crval != null && crval != "") {
                        type = crval.type;
                        value = crval.value;
                    }
                    table += "<td>" + getCalenderDay(type, tdNum, value) + "</td>";
                } else {
                    nextDay++;
                    table += "<td>" + getCalenderDay("empty", nextDay, null) + "</td>";
                }
                trNum++;
                if (trNum == 7) {
                    table += "</tr>";
                    trNum = 0;
                }
            }
            $(".rl-tbody").append(table);

        },
    };
    _global = (function () {
        return this || (0, eval)('this');
    }());
    if (typeof module !== "undefined" && module.exports) {
        module.exports = calendar;
    } else if (typeof define === "function" && define.amd) {
        define(function () {
            return calendar;
        });
    } else {
        !('calendar' in _global) && (_global.calendar = calendar);
    }
}());

function getDay(day) {
    var curYear = $("#curYear option:selected").val();
    var curMonth = $("#curMonth option:selected").val();
    var newDate = new Date();
    var date1 = new Date(curYear, curMonth - 1, day);
    var date2 = new Date(newDate.getFullYear(), newDate.getMonth(), newDate.getDate())
    return (date1.getTime() === date2.getTime());
}

function getCountDays(curYear, curMonth) {
    var date = new Date(curYear, curMonth, 0);
    return date.getDate();
}

function getWeekOneDay(curYear, curMonth) {
    var date = new Date(curYear, curMonth - 1, 1);
    return date.getDay();//0-6
}

function getUpMonth(curYear, curMonth) {
    var date = new Date(curYear, curMonth - 1, 0);
    return date.getDate();
}

function getCalenderDay(type, day, value) {
    var getValue = "";
    var cday = "<span class='td-day'>" + day + "</span>";
    var content = cday;
    if (value != null) {
        if (value.exp != null && value.exp != "") {
            content = cday + "<span class='td-val-exp'>" + value.exp + "</span>";
        }
        if (value.am != null && value.am != "") {
            content += "<span class='td-val-am'>签到：" + value.am + "</span>";
        }
        if (value.pm != null && value.pm != "") {
            content += "<span class='td-val-pm'>签退：" + value.pm + "</span>";
        }
    }
    switch (type) {
        case 'work'://工作
            getValue = "<div class='calendar-work date_" + day + "' id='work_" + day + "' onclick='detailsView(this);'>" + content + "</div>";
            break;
        case 'rest'://休息
            getValue = "<div class='calendar-rest date_" + day + "' id='rest_" + day + "' onclick='detailsView(this);'>" + content + "</div>";
            break;
        case 'leave'://请假
            getValue = "<div class='calendar-leave date_" + day + "' id='leave_" + day + "' onclick='detailsView(this);'>" + content + "</div>";
            break;
        case 'late'://迟到
            getValue = "<div class='calendar-late date_" + day + "' id='late_" + day + "' onclick='detailsView(this);'>" + content + "</div>";
            break;
        case 'early'://早退
            getValue = "<div class='calendar-early date_" + day + "' id='early_" + day + "' onclick='detailsView(this);'>" + content + "</div>";
            break;
        case 'out'://外出
            getValue = "<div class='calendar-out date_" + day + "' id='out_" + day + "' onclick='detailsView(this);'>" + content + "</div>";
            break;
        case 'travel'://出差
            getValue = "<div class='calendar-travel date_" + day + "' id='travel_" + day + "' onclick='detailsView(this);'>" + content + "</div>";
            break;
        case 'empty'://空
            getValue = "<div class='calendar-empty date_" + day + "' id='empty_" + day + "' onclick='detailsView(this);'>" + content + "</div>";
            break;
        case 'today'://今天
            getValue = "<div class='calendar-today date_" + day + "' id='today_" + day + "' onclick='detailsView(this);'>" + content + "</div>";
            break;
        case 'schedule'://日程
            getValue = "<div class='calendar-today date_" + day + "' id='today_" + day + "' onclick='detailsView(this);'>" + content + "</div>";
            break;
    }
    return getValue;

}

function detailsView(obj) {
    console.log($(obj).attr("id"))
    var exp = $(obj).find(".td-val-exp").text();
    var am = $(obj).find(".td-val-am").text();
    var pm = $(obj).find(".td-val-pm").text();
    $("#attendance").html("");
    $("#workplan").html("");
    var html = "<span>状态：" + exp + "</span></br><span>" + am + "</span></br><span>" + pm + "</span>";
    $("#attendance").append(html);
    var day = $(obj).attr("id").split("_");
    var year = $("#curYear option:selected").val();
    var month = $("#curMonth option:selected").val();
    var date = year + "-" + month + "-" + day[1];
    var html1 = "";
    $.ajax({
        type: "POST",
        url: "/schedule/getUserSchedule.do?date=" + date,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        async: false,
        success: function (data) {
            console.log(data)
            for (var i = 0; i < data.length; i++) {
                html1 += "<span style='cursor: pointer;' onclick=seeInfo(\'" + data[i].content + "\')>" + data[i].scheduletTitle + "</span></br>";
            }
        },
        error: function (message) {
            layer.msg("日程安排获取失败！");
        }
    });
    $("#workplan").append(html1);


}

function seeInfo(content) {
    layer.open({
        title: '日程内容'
        , content: content
    });
}
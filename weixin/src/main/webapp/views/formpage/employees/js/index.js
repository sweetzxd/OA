$(function () {
    oA.hasNotMask = OA.star;
    oA.ele = OA.ele(["#content", "#onSelected", "#footer", "#menuLeftDiv", "#searchBtn",
        "!:text", "addClass", "removeClass", "on", "padleft"]);
    oA.ele.dscriptText = '请选择联系人';
    oA.ele.onSelected.text(oA.ele.dscriptText);
    oA.connectiondata = OA.array();
    oA.inputid = "";
    $.when(OA.axios(oA.url.department.depttree),
        OA.axios(oA.url.employees.selectall)).done(function (A, B) {
        oA.showContent(A, B);
        oA.recover.data(oA._name, oA._Value);
    });
    oA.ele.footer.find('a').first().click(function (e) {
        var list = oA.ele.content.find('li.on');
        if (list.size()) {
            list.each(function () {
                oA.connectiondata.push($(this).data("uid"));
            });
            oA._name.text(oA.ele.onSelected.text().substr(4));
            if(OA.operation.getQueryString('type') == 'more'){
                oA._Value.val(oA.connectiondata.join(';')+";");
            }else{
                oA._Value.val(oA.connectiondata.join(';'));
            }
            oA._Value.valid();
            parent.layer.close(parent.oA.openPage);
        } else {
            layer.open({
                content: oA.ele.dscriptText,
                skin: 'msg',
                time: 2
            });
        }
    }).end().last().click(function (e) {
        parent.layer.close(parent.oA.openPage);
    });

    oA.ele.searchBtn.click(function (e) {
        oA.ul = oA.ele.content.find('\u0075\u006c');
        if (oA.ele.text.val()) {
            oA.ul.hide().children().hide().end().prev().hide().parent().hide();
            oA.codeKey(oA.ele.text.val(), OA.verify.isRealName(oA.ele.text.val()) ? OA.star : OA.end);
        }
    });

    oA.ele.text.keyup(function (e) {
        if (e.which === 13) {
            oA.ele.searchBtn.click();
            return;
        }
        oA.ul = oA.ele.content.find('\u0075\u006c');
        oA.arr = OA.array();
        oA.ele.onSelected.empty();
        oA.ul.children().removeClass(oA.ele.on).end().prev().find('.all-select').removeClass(oA.ele.on).text('全选');
        if (!this.value) {
            oA.ul.show().children().show().end().prev().show().parent().show();
            oA.ele.menuLeftDiv.css({left: 0});
            oA.ele.content.children().addClass(oA.ele.padleft);
            oA.ele.onSelected.text(oA.ele.dscriptText);
            delete oA.ele.flag;
        } else {
            oA.ele.searchBtn.click();
        }
    });

    $(window).scroll(function () {
        if (!oA.ele.flag) {
            oA.scrollTop = $(this).scrollTop();
            oA.ele.content.children().each(function (index) {
                if (this.offsetTop - 130 <= oA.scrollTop) {
                    oA.ele.menuLeftDiv.find('li').eq(index).addClass('active').siblings().removeClass('active')
                        .end().end().end().stop().animate({
                        scrollTop: index > 3 ? 53 * (index - 3) : index
                    }, 100);
                    if (oA.scrollTop + document.documentElement.clientHeight < $(document).height()) {
                        $(OA.body).stop().animate({
                            scrollTop: oA.ele.content.children().eq(index).offset().top - 100
                        }, 150);
                    }
                }
            });
        }
    });
});
oA.recover = OA.object('data', function (ele, hidden) {
    if (ele.text().trim()) {
        hidden.val().split(';').forEach(function (uid) {
            var onLi = oA.ele.content.find("li[data-uid=" + uid + "]").addClass(oA.ele.on);
            oA.arr.push(onLi.children().last().text());
        });
        /*oA.ele.content.children().each(function () {
            if ($(this).find('li').size() === $(this).find('li.on').size()) {
                $(this).find('span').first().addClass(oA.ele.on).text('取消');
            }
        });*/
        oA.ele.onSelected.text('已选择：' + ele.text().trim());
    }
});
oA.getUrl = OA.operation.getQueryString('type') === 'single' ? OA.star : OA.end;
["_name", "_Value"].forEach(function (item) {
    oA[item] = parent.$("#" + OA.operation.getQueryString('id') + item);
});
oA.showContent = function (dataMenu, dataList) {
    if (dataMenu[1] === 'success' && dataList[1] === 'success') {
        oA.ele.menuLeftDiv.html(oA.transform.content('leftMenu', {dataMenu: dataMenu[0]})).find('li').click(function () {
            oA.ele.flag = OA.star;
            $(this).addClass('active').siblings().removeClass('active');
            $(OA.body).stop().animate({
                scrollTop: oA.ele.content.children().eq($(this).index()).offset().top - 100
            }, 300, function () {
                delete oA.ele.flag;
            });
        });
        oA.arr = OA.array();
        dataMenu[0].forEach(function (item) {
            oA.arr.push({
                name: item.name,
                id: item.id,
                type: oA.getUrl,
                children: []
            });
            if (item.children.length) {
                item.children.forEach(function (data) {
                    oA.arr.push({
                        name: data.name,
                        id: data.id,
                        type: oA.getUrl,
                        children: []
                    });
                });
            }
        });
        dataList[0].data.forEach(function (data) {
            oA.arr.forEach(function (department) {
                if (data.department === department.id) {
                    department.children.push(data);
                }
            });
        });
        //console.log(oA.arr)
        oA.ele.content.html(oA.transform.content('rct', {data: oA.arr})).find('li.hasperson').click(function () {
            if (oA.getUrl) {
                oA.ele.content.find('li').removeClass(oA.ele.on);
                $(this).addClass(oA.ele.on);
                oA.ele.onSelected.text('已选择：' + $(this).children().last().text());
                return;
            }
            oA.flag = OA.star;
            $(this).toggleClass(oA.ele.on);
            if (oA.arr.length) {
                oA.DuplicateRemoval($(this));
                if (oA.flag) {
                    oA.arr.push($(this).children().last().text());
                }
            } else {
                oA.arr.push($(this).children().last().text());
            }
            oA.setArray(oA.arr);
            var flag = $(this).parent().children('.on').size() === $(this).parent().children(':visible').size();
            $(this).parent().prev().children().last().children()[flag ? oA.ele.addClass : oA.ele.removeClass](oA.ele.on).text(flag ? '取消' : '全选');
        });
        oA.arr.splice(0, oA.arr.length);
        oA.ele.content.find('.all-select').click(function (e) {
            var $that = $(this);
            $(this).toggleClass(oA.ele.on).closest('h5').next().children()[$(this).hasClass(oA.ele.on) ? oA.ele.addClass : oA.ele.removeClass](oA.ele.on).each(function () {
                if ($that.hasClass(oA.ele.on) && $(this).is(':visible')) {
                    oA.arr.push($(this).children().last().text());
                    oA.arr = Array.from(OA.set(oA.arr));
                } else {
                    oA.DuplicateRemoval($(this));
                }
            }).end().end().end().text($(this).hasClass(oA.ele.on) ? '取消' : '全选');
            oA.setArray(oA.arr);
        });
    } else {
        oA.msg('数据接口加载失败!')
    }
    oA.setArray = function (arr) {
        oA.ele.onSelected.text(arr.length ? '已选择：' + Array.from(OA.set(arr)).join(' ') : oA.ele.dscriptText).parent().scrollLeft(arr.length * 30);
    }
    oA.DuplicateRemoval = function (object) {
        oA.arr.forEach(function (text, index) {
            if (object.children().last().text() === text) {
                oA.arr.splice(index, 1);
                oA.flag = OA.end;
            }
        });
    }
    oA.codeKey = function (text, juget) {
        oA.ul.children('.hasperson').each(function () {
            var express = makePy($(this).text()).toString().toLowerCase().indexOf(text.toLowerCase()) >= 0;
            if (juget) {
                express = $(this).text().indexOf(text) >= 0;
            }
            if (express) {
                $(this).show().parent().show().prev().show().parent().show();
                oA.ele.menuLeftDiv.css({left: -100});
                oA.ele.content.children().removeClass(oA.ele.padleft);
                oA.ele.flag = OA.star;
            }
        });
    }
}
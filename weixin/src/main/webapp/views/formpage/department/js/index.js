$(function () {
    oA.hasNotMask = OA.star;
    oA.ele = OA.ele(["#onSelected", "#footer", "#searchBtn", "!:text", "#content", "radio", "checkbox", "on", "li",
        "checked", "attr", "removeAttr", "addClass", "removeClass", "not", "filter", "ul"]);
    oA.ele.dscriptText = '请选择部门';
    oA.ele.onSelected.text(oA.ele.dscriptText);
    OA.axios(oA.url.department.depttree).done(function (data) {
        new oA.success.showContent(data).ready();
        oA.recover.data(oA._name, oA._Value);
    });
    oA.ele.footer.find('a').first().click(function (e) {
        var connectiondata = OA.array(),
            onChecked = oA.ele.content.find(oA.eleType).filter(":" + oA.ele.checked);
        if (onChecked.size()) {
            onChecked.each(function () {
                connectiondata.push(this.value);
            });
            oA._name.text(oA.ele.onSelected.text().substr(4));
            if(OA.operation.getQueryString('type') == 'more'){
                oA._Value.val(connectiondata.join(';')+";");
            }else{
                oA._Value.val(connectiondata.join(';'));
            }
            oA._Value.valid();
            parent.layer.close(parent.oA.openPage);
        } else {
            oA.msg(oA.ele.dscriptText);
        }
    }).end().last().click(function (e) {
        parent.layer.close(parent.oA.openPage);
    });

    oA.ele.searchBtn.click(function (e) {
        oA.ul = oA.ele.content.find('\u0075\u006c');
        if (oA.ele.text.val()) {
            oA.ul.hide().children().hide().end().prev().hide().parent().hide();
            oA.this.codeKey(oA.ele.text.val(), OA.verify.isRealName(oA.ele.text.val()) ? OA.star : OA.end);
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
        oA.ul.find('.all-select').removeClass(oA.ele.on).text('全选').end().find(oA.eleType).removeAttr(oA.ele.checked);
        if (!this.value) {
            oA.ul.show().children().show().end().prev().show().parent().show();
            oA.ele.onSelected.text(oA.ele.dscriptText);
        } else {
            oA.ele.searchBtn.click();
        }
    });
});

oA.success = OA.object("showContent", function (dataMenu) {
    oA.this = this;
    this.load = function () {
        oA.arr = OA.array();
        dataMenu.forEach(function (item) {
            item.type = oA.getPageType;
        });
        oA.ele.content.html(oA.transform.content('rct', {data: dataMenu})).find(oA.eleType).change(function (e) {
            if (this.checked) {
                if (oA.getPageType) {
                    oA.arr.splice(0, oA.arr.length);
                }
                oA.arr.push(this.title);
            } else {
                oA.arr.splice(oA.arr.indexOf(this.title), 1);
            }
            var SubLi = $(this).closest(oA.ele.li).siblings().addBack(),
                status = (SubLi.size() === SubLi.find(oA.eleType).filter(":" + oA.ele.checked).size());
            SubLi.first().find('span.rt')[status ? oA.ele.addClass : oA.ele.removeClass](oA.ele.on).text(!status ? '全选' : '取消');
            oA.this.onSelectResult(oA.arr);
        }).end().find('.all-select').click(function (e) {
            e.preventDefault();
            $(this).toggleClass(oA.ele.on).text($(this).hasClass(oA.ele.on) ? '取消' : '全选').closest(oA.ele.li).nextAll().addBack().filter(":visible").find(oA.eleType)[$(this).hasClass(oA.ele.on) ? oA.ele.not : oA.ele.filter](":" + oA.ele.checked).click();
            oA.this.onSelectResult(oA.arr);
        });
        return this;
    };
    this.onSelectResult = function (data) {
        oA.ele.onSelected.text(data.length ? '已选择：' + Array.from(OA.set(data)).join('；') : oA.ele.dscriptText).parent().scrollLeft(data.length * 200);
    };
    this.codeKey = function (text, juget) {
        oA.ul.children().each(function () {
            var title = $(this).find(oA.eleType).get(0).title,
                express = makePy(title).toString().toLowerCase().indexOf(text.toLowerCase()) >= 0;
            if (juget) {
                express = title.indexOf(text) >= 0;
            }
            if (express) {
                $(this).show().parent().show().prev().show().parent().show();
            }
        });
    };
    this.ready = function () {
        oA.getPageType = OA.operation.getQueryString('type') === 'single' ? OA.star : OA.end;
        ["_name", "_Value"].forEach(function (item) {
            oA[item] = parent.$("#" + OA.operation.getQueryString('id') + item);
        });
        oA.eleType = ":" + (oA.getPageType ? oA.ele.radio : oA.ele.checkbox);
        this.load();
    };
    return this;
});
oA.recover = OA.object('data', function (ele, hidden) {
    if (ele.text().trim()) {
        hidden.val().split(';').forEach(function (value) {
            var onCheckBox = oA.ele.content.find(oA.eleType).filter("[value="+value+"]").attr({checked: oA.ele.checked});
            oA.arr.push(onCheckBox.next().next().text());
        });
        oA.ele.content.find('\u0075\u006c').each(function () {
            if ($(this).children().size() === $(this).find(oA.eleType).filter(":"+oA.ele.checked).size()) {
                $(this).find('span.rt').addClass(oA.ele.on).text('取消');
            }
        });
        oA.ele.onSelected.text('已选择：' + ele.text().trim());
    }
});

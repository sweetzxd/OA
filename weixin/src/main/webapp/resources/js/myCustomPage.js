;(function (factory) {
    if (typeof exports === 'object' && typeof module !== 'undefined') {
        module.exports = factory(require('jquery'));
    } else if (typeof define === "function" && define.amd) {
        return define(["jquery"], factory);
    } else {
        factory(window, function (filed) {
            return {
                '\u0061\u0073\u0079\u006e\u0063': function () {
                    return jQuery[filed];
                },
                send: new Object({
                    timeout: 6000,
                    success: function () {
                        layer.close(oA.loading);
                    },
                    complete: function (XMLHttpRequest, status) {
                        if (status === '\u0074\u0069\u006d\u0065\u006f\u0075\u0074') {
                            oA.queryTruckAjax.abort();
                        } else if (status === "\u0065\u0072\u0072\u006f\u0072") {
                            layer.closeAll();
                            layer.open({
                                content: '\u627e\u4e0d\u5230\u670d\u52a1\u5668\uff01',
                                skin: 'msg',
                                time: 3
                            });
                        }
                    },
                    error: function (e) {
                        try {
                            layer.open({
                                content: e.responseJSON.Message,
                                btn: '我知道了'
                            });
                        } catch (event) {
                            console.error("回调错误");
                        }
                    }
                }), hasNotMask: false,
                msg: function (msg) {
                    layer.open({
                        content: msg
                        , skin: 'msg'
                        , time: 2
                    });
                },
                transform: {
                    content: typeof haipor === 'function' ? haipor : null
                },
                set: function (name, val) {
                    localStorage.setItem(name, val);
                },
                get: function (name) {
                    return localStorage.getItem(name);
                },
                clear: function () {
                    localStorage.clear();
                },
                del: function (name) {
                    localStorage.removeItem(name);
                }
            };
        }, new Object({}));
    }
}(function (pop, source, config) {
    "use strict";
    pop['\u006f\u0041'] = source('\u0061\u006a\u0061\u0078');
    $.fn.instance = new Object({
        axios: function (url, data, method) {
            oA.argument = arguments;
            if (oA.argument.length) {
                if (!oA.hasNotMask) {
                    oA.loading = layer.open({type: 2});
                }
                ['\u0075\u0072\u006c', '\u0064\u0061\u0074\u0061', '\u0074\u0079\u0070\u0065'].forEach(function (key, index) {
                    oA.send[key] = (!!oA.argument[index]) ? oA.argument[index] : index === 1 ? new Object({}) : '\u0070\u006f\u0073\u0074';
                });
                oA.queryTruckAjax = oA['\u0061\u0073\u0079\u006e\u0063']()(oA.send);
                return oA.queryTruckAjax;
            } else {
                console.error('none params!')
            }
        },
        ele: function (array) {
            var newObject = this.object();
            if (Array.isArray(array)) {
                $.each(array, function (i, v) {
                    if (v.substring(0, 1) === '\u0023' || v.substring(0, 1) === '\u002e' || v.substring(0, 1) === '\u0021') {
                        newObject[v.substring(1, 2) === '\u003a' ? (v.substring(2).indexOf('\u003a') > 0 ? v.substring(2, v.substring(2).indexOf('\u003a') + 2) : v.substring(2)) : v.substring(1)] = $(v.indexOf('\u0021') === 0 ? v.substring(1) : v);
                    } else {
                        newObject[v.substring(0, 1) === '\u003a' ? v.substring(1) : v] = v.substring(0, 1) === '\u003a' ? $(v) : v;
                    }
                })
            } else {
                if (array.substring(0, 1) === '\u0023' || array.substring(0, 1) === '\u002e' || array.substring(0, 1) === '\u0021') {
                    newObject[array.substring(1, 2) === '\u003a' ? (array.substring(2).indexOf('\u003a') > 0 ? array.substring(2, array.substring(2).indexOf('\u003a') + 2) : array.substring(2)) : array.substring(1)] = $(array.indexOf('\u0021') === 0 ? array.substring(1) : array);
                } else {
                    newObject[array.substring(0, 1) === '\u003a' ? array.substring(1) : array] = array.substring(0, 1) === '\u003a' ? $(array) : array;
                }
            }
            return newObject;
        },
        array: function (length, value) {
            var newArray = new Array(0);
            if (arguments.length) {
                for (var arr = 0; arr < length; arr++) {
                    newArray.push(arguments.length === 2 ? value : arguments.length === 3 ? [value] : new Array(0));
                }
            }
            return newArray;
        },
        object: function (string, value) {
            var tmpjct = new Object({});
            if (arguments.length) {
                if (string.indexOf('.') < 0) {
                    tmpjct[arguments[0]] = arguments.length > 1 ? value : {};
                } else {
                    var strArray = string.split(','),
                        response = new Object({}),
                        result = strArray.map(function (name, index) {
                            tmpjct[name] = index === strArray.length - 1 ? value : new Object({});
                            tmpjct = tmpjct[name];
                            if (!index) {
                                return tmpjct;
                            }
                        });
                    result.splice(1);
                    response[strArray[0]] = result[0];
                    return response;
                }
            }
            return tmpjct;
        },
        //set: (arr) => new Set(arr),
        set: function(arr){
            return new Set(arr);
        },
        verify: {
            isMobil: function (tel) {
                var telReg = !!tel.match(/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/);
                return (!telReg) ? OA.end : OA.star;
            },
            isPhone: function (str) {
                var re = /^0\d{2,3}-?\d{7,8}$/;
                return (!re.test(str)) ? OA.end : OA.star;
            },
            isRealName: function (name) {
                var namerg = /[\u4e00-\u9fa5]/;
                return (!namerg.test(name)) ? OA.end : OA.star;
            },
            floatNumber: function (c) {
                var r = /^[1-9]?[0-9]*\.[0-9]*$/;
                return r.test(c);
            },
            IsFloatLength: function (num, len) {
                if (num.split('.')[1].length > len) {
                    return OA.end;
                }
                return OA.star;
            },
            CheckMail: function (mail) {
                var checkStr = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
                return (!checkStr.test(mail)) ? false : true;
            },
            isCodeValid: function (idCard) {
                var regIdCard = /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
                if (regIdCard.test(idCard)) {
                    if (idCard.length == 18) {
                        var idCardWi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
                        var idCardY = new Array(1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2);
                        var idCardWiSum = 0;
                        for (var i = 0; i < 17; i++) {
                            idCardWiSum += idCard.substring(i, i + 1) * idCardWi[i];
                        }
                        var idCardMod = idCardWiSum % 11;
                        var idCardLast = idCard.substring(17);
                        if (idCardMod === 2) {
                            if (idCardLast === "X" || idCardLast === "x") {
                                return OA.star;
                            } else {
                                return OA.end;
                            }
                        } else {
                            if (idCardLast === idCardY[idCardMod]) {
                                return OA.star;
                            } else {
                                return OA.end;
                            }
                        }
                    }
                } else {
                    return false;
                }
            },
            PostalCode: function (pcode) {
                var pcodename = /^[0-9][0-9]{5}$/;
                return (!pcodename.test(pcode)) ? OA.end : OA.star;
            }
        },
        operation: {
                getQueryString: function (name) {
                var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
                var r = window.location.search.substr(1).match(reg);
                if(r!=null)return  unescape(r[2]); return null;
            }
        }
    });
    $.fn.extend($.fn.instance);
    config.set = {
        star: true,
        end: false,
        str: "",
        html: "html",
        body: "html,body",
    }
    if (typeof Object.assign != 'function') {
        Object.assign = function(target) {
            'use strict';
            if (target == null) {
                throw new TypeError('Cannot convert undefined or null to object');
            }

            target = Object(target);
            for (var index = 1; index < arguments.length; index++) {
                var source = arguments[index];
                if (source != null) {
                    for (var key in source) {
                        if (Object.prototype.hasOwnProperty.call(source, key)) {
                            target[key] = source[key];
                        }
                    }
                }
            }
            return target;
        };
    }
    Object.assign($.fn.instance, config.set);
    pop['\u004f\u0041'] = $.fn.instance;
    delete config.set;
}))
;

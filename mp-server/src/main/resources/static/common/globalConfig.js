// 全局的js配置

// dataTable 异常信息修正
$.ajaxSetup({
    global: true,
    type: "POST",
    error: function (xhr, status, error) {
        switch (xhr.status) {
            case 401:
                Message.error("您的账号已经异地登录,请返回登录页重试");
                break;
            case 403:
                Message.error("该资源您无权限访问,请联系管理员取得权限再试")
                break;
            default :
                Message.error("阿偶,服务器发生了一个异常,抱歉奔溃了")
        }
    }
});
$.fn.dataTable.ext.errMode = function (settings, tn, msg) {
    console.log(msg);
    switch (settings.jqXHR.status) {
        case 401:
            Message.error("您的账号已经异地登录,请返回登录页重试");
            break;
        case 403:
            Message.error("该资源您无权限访问,请联系管理员取得权限再试")
            break;
        default :
            Message.error("阿偶,服务器发生了一个异常,抱歉奔溃了")
    }
};


//修复ie array对象缺少from方法的问题
if (!Array.from) {
    Array.from = function (el) {
        return Array.apply(this, el);
    }
}

//给字符串加入startWith和endWith方法
String.prototype.startWith = function (str) {
    let reg = new RegExp("^" + str);
    return reg.test(this);
};


String.prototype.endWith = function (s) {
    if (s == null || s === "" || this.length === 0 || s.length > this.length)
        return false;
    return this.substring(this.length - s.length) === s;
};


if (!Array.prototype.distinct) {
    Object.defineProperty(Array.prototype, 'distinct', {
        value: function () {
            return this.reduce(function (newArr, oArr) {
                if (newArr.indexOf(oArr) === -1) newArr.push(oArr);
                return newArr;
            }, []);
        }
    });
}
//绑定find方法
if (!Array.prototype.find) {
    Object.defineProperty(Array.prototype, 'find', {
        value: function (predicate) {
            if (this == null) {
                throw new TypeError('Array.prototype.find called on null or undefined');
            }
            if (typeof predicate !== 'function') {
                throw new TypeError('predicate must be a function');
            }
            let list = Object(this);
            let length = list.length >>> 0;
            let thisArg = arguments[1];
            let value;

            for (let i = 0; i < length; i++) {
                value = list[i];
                if (predicate.call(thisArg, value, i, list)) {
                    return value;
                }
            }
            return undefined;
        }
    });
}

/**
 * 空安全工具类
 *
 * @author maple
 * @date 2019-5-20
 */
// 构造
function Optional(data, search) {
    this.data = data;
    this.search = search;
}

(function ($Optional) {
    $Optional.prototype = {
        get: function (search) {
            if (!this.data) return undefined;
            search = search || this.search;
            if (!search) return this.data;
            return StrTool.objAnalysis(this.data, search);
        },
        // 获取 获取不到则返回默认值
        orElse: function (defaultVal, search) {
            return this.get(search) || defaultVal;
        },
        // 获取 或者自定义方式返回
        orElseGet: function (fun, search) {
            return this.get(search) || fun(search)
        },
        // 如果非空执行
        ifPresent: function (fun, search) {
            let rtObj = this.get(search);
            if (!this._isEmpty(rtObj)) {
                fun.apply(rtObj, search);
            }
            let _self = this;
            return {
                else: function (eFun) {
                    _self.ifEmpty(eFun, search);
                }
            }
        },
        // 如果为空执行
        ifEmpty: function (fun, search) {
            if (this._isEmpty(this.get(search))) {
                fun(search);
            }
            let _self = this;
            return {
                else: function (eFun) {
                    _self.ifPresent(eFun, search);
                }
            }
        },
        // 如果为空或空串执行
        ifBlank: function (fun, search) {
            if (this._isBlank(this.get(search))) {
                fun(search);
            }
            let _self = this;
            return {
                else: function (eFun) {
                    _self.ifNotBlank(eFun, search);
                }
            }
        },
        // 如果不为空或空串执行
        ifNotBlank: function (fun, search) {
            let rtObj = this.get(search);
            if (!this._isBlank(rtObj)) {
                fun(rtObj, search);
            }
            let _self = this;
            return {
                else: function (eFun) {
                    _self.ifBlank(eFun, search);
                }
            }
        },
        // 将对象转化为另一个对象
        map: function (fun, search) {
            let rtObj = this.get(search);
            if (!rtObj) {
                return Optional.empty();
            }
            // 捕获内部的空调用异常
            try {
                rtObj = fun(rtObj, search);
            } catch (e) {
                return Optional.empty();
            }
            return Optional.of(rtObj);
        },
        _isEmpty: function (obj) {
            return obj === undefined || obj === null;
        },
        _isBlank: function (obj) {
            return !obj || !obj.trim();
        }

    };
    Optional.of = function (value, search) {
        return new Optional(value, search);
    };
    Optional.empty = function () {
        return new Optional();
    }
})(Optional);
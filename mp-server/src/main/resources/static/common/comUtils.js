window.ComUtils = {
    randomKey: function () {
        return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    },
    //生成随机id
    getUUid: function () {
        return (this.randomKey() + this.randomKey() + "-" + this.randomKey() + "-" + this.randomKey() + "-" + this.randomKey() + "-" + this.randomKey() + this.randomKey() + this.randomKey());
    },
    //判断对象是否是Object或者Array中的一个
    isObjectOrArray: function (obj) {
        return this.isObject(obj) || this.isArray(obj)
    },
    //判断对象是否既不是Object也不是Array
    isNotObjectAndArray: function (obj) {
        return !this.isObjectOrArray(obj)
    },
    //拷贝对象
    cloneData: function (oVal, newVal) {
        if (!newVal) {
            //判断旧值是数组还是对象
            if (oVal === null) {
                return null;
            }
            if (oVal instanceof Array) {
                newVal = [];
            } else {
                newVal = {};
            }
        }
        for (let dataIndex in oVal) {
            //如果内部值仍为对象则地递归调用
            if (typeof oVal[dataIndex] === 'object') {
                newVal[dataIndex] = this.cloneData(oVal[dataIndex]);
            } else {
                newVal[dataIndex] = oVal[dataIndex];
            }
        }
        return newVal;
    }
};

/**
 * 增加判断基本类型的方法
 */
(function (ComUtils) {
    let objTypeArr = ['String', 'Array', 'Object', 'Number', 'Boolean', 'Symbol', 'Null', 'Undefined', 'Function'];
    for (let index in objTypeArr) {
        (function () {
            let type = objTypeArr[index];
            ComUtils['is' + type] = function (obj) {
                return Object.prototype.toString.call(obj) === '[object ' + type + ']';
            };
            ComUtils['isNot' + type] = function (obj) {
                return Object.prototype.toString.call(obj) !== '[object ' + type + ']';
            };
        })();
    }
})(ComUtils);
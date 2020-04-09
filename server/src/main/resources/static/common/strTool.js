let StrTool = {
    /**
     * 模板字符串解析 将数据替换入字符串 只替换{{}}包裹中的内容
     *
     * @param {Object} data 数据源
     * @param {String} str 待解析字符串
     */
    tempStrAnalysis: function (data, str) {
        let _self = this;
        return str.replace(/{{([.\w()\[\]]+)}}/g, function (match, key) {
            return _self.objAnalysis(data, key);
        });
    },
    /**
     * 从数据源中取出字符串代表的内容
     * 如a.name 取出data.a.name a[0] 取出data.a[0]
     * a() 调用data.a()
     *
     * @param {Object} data 数据源
     * @param {String} str 字符串
     */
    objAnalysis: function (data, str) {
        if (!str) {
            return null;
        }
        // 将所有[]替换为. 去除所有空格
        str = str
            .replace(/\[(\w+)]/g, function (matched, key) {
                return "." + key;
            })
            .replace(/\s+/g, "");
        let rtObj = data;
        let strArr = str.split(".");
        for (let i = 0; i < strArr.length; i++) {
            // 如果括号结尾则为函数调用
            if (strArr[i].endWith("()")) {
                let funName = strArr[i].substring(0, strArr[i].length - 2);
                if (rtObj[funName] === undefined) {
                    return undefined;
                }
                rtObj = rtObj[funName]();
                if (rtObj === undefined) {
                    return undefined;
                }
                continue;
            }
            // 否则正常调用
            rtObj = rtObj[strArr[i]];
            if (rtObj === undefined) {
                return undefined;
            }
        }
        return rtObj;
    }
};

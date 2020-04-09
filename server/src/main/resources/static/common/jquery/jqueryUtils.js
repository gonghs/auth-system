window.JqueryUtils = {
    // 获取元素真实高度
    getElementRealHeight: function (element) {
        if (!(element instanceof jQuery)) {
            return;
        }
        if (element.is('iframe')) {
            // 捕获异常 防止在页面未加载完毕时调用报错
            try {
                return element.contents().height();
            } catch (e) {
                return 0;
            }
        } else {
            return element.height();
        }
    },
    /**
     * 创建一个包含I标签的A标签
     *
     * @param label 文字描述
     * @param icon 图标样式名 为空则不创建I标签
     * @param action 回调函数 若为function则绑定click事件(调用时第一个参数为a标签自身 第二个参数为额外参数)  若为string则绑定至href 额外的
     * @param extraParams 额外的参数
     */
    createAIncludeI: function (label, icon, action, extraParams) {
        let aTag = $('<a></a>');
        if (icon && ComUtils.isString(icon)) {
            aTag.append('<i class="' + icon + '">' + label + '</i>');
        } else {
            aTag.append(label);
        }
        if (ComUtils.isString(action)) {
            aTag.attr('href', action);
        } else if (ComUtils.isFunction(action)) {
            aTag.click(function () {
                // 将当前对象赋予方法参数
                action(this, extraParams);
            })
        }
        return aTag;
    },
    getFormData: function (formTag) {
        let returnObj = {};
        let valArr = formTag.serializeArray();
        $.each(valArr, function () {
            returnObj[this.name] = this.value;
        });
        return returnObj;
    }
};
/**
 * 尺寸变化的监听
 *
 * @author maple
 * @date 2019-9-15
 */
(function ($) {
    $.fn.sizeChanged = function (handleFunction) {
        let element = this;
        let lastWidth = element.width();
        let lastHeight = element.height();

        setInterval(function () {
            if (lastWidth === element.width() && lastHeight === element.height())
                return;
            if (typeof (handleFunction) == 'function') {
                handleFunction({width: lastWidth, height: lastHeight},
                    {width: element.width(), height: element.height()});
                lastWidth = element.width();
                lastHeight = element.height();
            }
        }, 100);
        return element;
    };
}(jQuery));
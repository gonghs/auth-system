/**
 * 高度监听器
 *
 * @author maple
 * @date 2019-9-18
 */
let sidebar = $(".admin-sidebar");
let content = $(".admin-content");
let iframeContent = $(".content");
$(function () {
    // 监听左侧高度变化 高度变化时将二者的高度都设为较大值
    sidebar.sizeChanged(function () {
        content.height(Math.max(JqueryUtils.getElementRealHeight(iframeContent), sidebar.height()));
        iframeContent.height(Math.max(JqueryUtils.getElementRealHeight(iframeContent), sidebar.height()));
    });
    //监听右侧高度变化
    content.sizeChanged(function () {
        sidebar.height(Math.max(JqueryUtils.getElementRealHeight(iframeContent), sidebar.height()));
    });


    $('#doc-prompt-toggle').on('click', function () {
        $('#my-prompt').modal({
            relatedElement: this,
            onConfirm: function (data) {
                alert('你输入的是：' + data)
            },
            onCancel: function () {
                alert('不想说!');
            }
        });
    });
});

iframeContent.on("load", function() {
    // 确保页面只出现一个滚动条
    let maxHeight = Math.max(sidebar.height(), JqueryUtils.getElementRealHeight(iframeContent));
    setHeight(maxHeight);
});
function setHeight(height) {
    sidebar.height(height);
    content.height(height);
    iframeContent.height(height);
}
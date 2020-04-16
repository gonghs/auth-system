/**
 * 菜单渲染js
 *
 * @author maple
 * @date 2019-9-18
 */
let joinLiByMenu = function (menu) {
    return $('<li page-link="' + menu.url + '"><a href="#"><span class="' + menu.icon + '"></span> ' + menu.menuName + '</a></li>')
};
$(function () {
    // 获取外层ul
    let menuListUl = $('.admin-sidebar-list');

    Api.get("/menu/getList").then(function (menuList) {
        console.log(menuList);
        menuList.forEach(function (menu) {
            // 如果是叶子节点或不存在子菜单则直接加入ul
            if (menu.isLeaf && (!menu.children || menu.children.length === 0)) {
                menuListUl.append(joinLiByMenu(menu));
                return;
            }
            // modelId用于做收缩控制
            let modelId = ComUtils.getUUid();
            // 创建父层li
            let li = $('<li class="admin-parent"><a class="am-cf" data-am-collapse="{target:' +
                ' \'#' + modelId + '\'}"><span class="' + menu.icon + '"></span> ' + menu.menuName + ' <span' +
                ' class="am-icon-angle-right am-fr' +
                ' am-margin-right"></span></a></li>');
            let ul = $('<ul class="am-list am-collapse admin-sidebar-sub am-in" id="' + modelId + '"></ul>').appendTo(li);
            let children = menu.children;
            children.forEach(function (child) {
                ul.append(joinLiByMenu(child));
            });
            menuListUl.append(li);
        });

        // 绑定菜单事件
        $(".admin-sidebar-list li").click(function () {
            iframeContent.attr("src", $(this).attr("page-link"));
        });
    });

});

let selectTheme = {
    name: 'theme',
    className: 'theme-white'
};

function getThemeToggle() {
    // 判断用户是否已有自己选择的模板风格
    if (storageLoad('theme')) {
        selectTheme = storageLoad('theme');
        $('body').attr('class', selectTheme.className);
        if (selectTheme.className === 'theme-white') {
            $('#eyesI').attr('class', 'am-icon-toggle-off');
        } else {
            $('#eyesI').attr('class', 'am-icon-toggle-on');
        }
    } else {
        storageSave(selectTheme);
        $('body').attr('class', 'theme-white')
    }
}
/**
 * 更换主题
 */
function changeTheme() {
    //按钮切换
    let $eyesI = $('#eyesI');
    $eyesI.toggleClass('am-icon-toggle-off');
    $eyesI.toggleClass('am-icon-toggle-on');
    //主题切换
    let body = $('body');
    let iframeBody = $('iframe').contents().find('body');
    body.toggleClass('theme-white');
    body.toggleClass('theme-black');
    iframeBody.toggleClass('theme-white');
    iframeBody.toggleClass('theme-black');
    // 缓存
    selectTheme.Color = body.attr('class');
    storageSave(selectTheme);
}

function storageSave(objectData) {
    sessionStorage.setItem(objectData.Name, JSON.stringify(objectData));
}

function storageLoad(objectName) {
    if (sessionStorage.getItem(objectName)) {
        return JSON.parse(sessionStorage.getItem(objectName))
    } else {
        return false
    }
}
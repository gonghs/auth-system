/**
 * 通用axios封装
 * @author maple
 * @date 2019-9-17
 */
let Api = axios.create({
    baseURL: "http://" + window.location.host + baseUrl,
    timeout: 500000
});

Api.interceptors.request.use(function (cfg) {
    if (cfg.method === 'post') {
        cfg.headers['Content-Type'] = 'application/json';
        cfg.headers['X-Requested-With'] = 'XMLHttpRequest';
    } else {
        cfg.params = Object.assign({}, cfg.params);
        cfg.headers['X-Requested-With'] = 'XMLHttpRequest';
        cfg.headers['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
    }
    return cfg
}, function (error) {
    Message.error('啊偶..请求失败了.. 错误信息: ' + error);
    return Promise.reject(error)
});

// 响应贴合后台封装进行处理
Api.interceptors.response.use(function (resp) {
    if (resp.data.success) {
        // 去除result对象包装 将消息放入响应对象
        let respData = resp.data.data;
        respData.message = resp.data.message;
        return respData;
    } else {
        // 如果该异常为未登录则路由跳转至登陆页面
        if (resp.data.code === '999') {
            location.replace("/login")
        }
        Message.error(resp.data.message);
        return Promise.reject(resp.data);
    }
}, function (error) {
    if (!error || !error.response) {
        Message.error('啊偶..请求失败了.. 错误信息: ' + error);
        return Promise.reject(error);
    }
    switch (error.response.status) {
        case 401:
            Message.error('您的账号已经异地登录,请返回登录页重试');
            break;
        case 403:
            Message.error('无权限访问');
            break;
        default:
            Message.error('啊偶..请求失败了.. 错误信息: ' + error);
    }
    return Promise.reject(error);

});
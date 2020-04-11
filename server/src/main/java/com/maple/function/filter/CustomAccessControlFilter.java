package com.maple.function.filter;

import com.maple.common.constant.SecurityConst;
import com.maple.common.exception.AuthException;
import com.maple.dto.admin.UserDTO;
import com.maple.utils.RequestUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * 自定义访问控制过滤器
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 19:56
 */
@Slf4j
@Setter
public class CustomAccessControlFilter extends AccessControlFilter {
    /**
     * 重复登陆时跳转的url
     */
    private String kickOutUrl;
    /**
     * 重复登陆时踢出后一个用户还是前一个
     */
    private boolean kickOutAfter = false;
    /**
     * 同一个帐号最大会话数
     */
    private int maxSession = 1;
    /**
     * 会话管理器
     */
    private SessionManager sessionManager;
    /**
     * 缓存
     */
    private Cache<String, Deque<Serializable>> cache;

    /**
     * 设置Cache的key的前缀
     */
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro-activeSessionCache");
    }


    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        // 没有登录授权 且没有记住我
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            // 如果没有登录，直接进行之后的流程
            return true;
        }
        // 获得用户请求的URI
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();
        log.info("当前请求的uri: " + path);

        if (Objects.equals(path, SecurityConst.LOGIN_URL)) {
            return true;
        }
        Session session = subject.getSession();
        log.info("session时间设:：" + session.getTimeout());

        try {
            // 当前用户
            UserDTO user = (UserDTO) subject.getPrincipal();
            String account = user.getAccount();
            log.info("当前用户account:" + account);
            Serializable sessionId = session.getId();
            log.info("当前用户sessionId:" + sessionId);
            // 读取缓存用户 没有就存入
            Deque<Serializable> deque = cache.get(account);
            log.info("当前deque:" + deque);
            if (deque == null) {
                // 初始化队列
                deque = new ArrayDeque<>();
            }
            // 如果队列里没有此sessionId，且用户没有被踢出；放入队列
            if (!deque.contains(sessionId) && session.getAttribute(SecurityConst.KICK_OUT_ATTRIBUTE_KEY) == null) {
                // 将sessionId存入队列
                deque.push(sessionId);
                // 将用户的sessionId队列缓存
                cache.put(account, deque);
            }
            // 如果队列里的sessionId数超出最大会话数，开始踢人
            while (deque.size() > maxSession) {
                log.info("deque队列长度:" + deque.size());
                Serializable kickOutSessionId = kickOutAfter ? deque.removeFirst() : deque.removeLast();
                // 踢出后再更新下缓存队列
                cache.put(account, deque);
                try {
                    // 获取被踢出的sessionId的session对象
                    Session kickOutSession = sessionManager
                            .getSession(new DefaultSessionKey(kickOutSessionId));
                    if (kickOutSession != null) {
                        // 设置会话的kickOut属性表示踢出了
                        request.setAttribute(SecurityConst.KICK_OUT_ATTRIBUTE_KEY, true);
                        kickOutSession.setAttribute(SecurityConst.KICK_OUT_ATTRIBUTE_KEY, true);
                    }
                } catch (Exception e) {
                    log.error("用户踢出异常: ", e);
                }
            }

            // 如果被踢出了，(前者或后者)直接退出，重定向到踢出后的地址
            if (Objects.equals(session.getAttribute(SecurityConst.KICK_OUT_ATTRIBUTE_KEY), true)) {
                // 会话被踢出了
                try {
                    // 退出登录
                    subject.logout();
                } catch (Exception e) {
                    log.error("退出登陆异常: ", e);
                }
                saveRequest(request);
                log.debug("踢出后用户重定向的路径kickOutUrl:" + kickOutUrl);
                return response(request, response);
            }
            return true;
        } catch (Exception e) {
            log.error("控制用户在线数量异常！", e);
            return response(request, response);
        }
    }

    private boolean response(ServletRequest request,
                             ServletResponse response) throws IOException, AuthException {
        // 判断是否已经踢出
        // 如果是Ajax 访问，抛出异常由全局异常捕获器捕获返回
        // 如果是普通请求，直接跳转到登录页
        if (RequestUtils.isAjax(request)) {
            log.info("当前用户已经在其他地方登录，并且是Ajax请求！");
            WebUtils.toHttp(response).sendError(401);
        } else {
            // 重定向
            WebUtils.issueRedirect(request, response, kickOutUrl);
        }
        return false;
    }

}

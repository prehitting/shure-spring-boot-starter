package cn.shure.framework.security.base;

import org.springframework.core.Ordered;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class ShureRequestFilter extends OncePerRequestFilter implements Ordered {

    /**
     * 是否放行,返回false会抛出AccessDenied异常
     *
     * @param request
     * @param response
     * @return
     */
    protected abstract boolean handle(HttpServletRequest request, HttpServletResponse response);

    @Override
    protected final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        if (!handle(requestWrapper, response)) {
            throw new AccessDeniedException("Access Denied");
        }
        filterChain.doFilter(request, response);
    }

    /**
     * order高者优先执行
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}

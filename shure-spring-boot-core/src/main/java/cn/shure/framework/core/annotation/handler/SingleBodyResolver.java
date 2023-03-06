package cn.shure.framework.core.annotation.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import cn.shure.framework.core.annotation.SingleParamBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * @Description SingleBodyResolver
 * @Author 下课菌
 * @Date 2023/3/3
 */
@SuppressWarnings("all")
@Slf4j
public class SingleBodyResolver implements HandlerMethodArgumentResolver {

    private static final String POST = "post";


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SingleParamBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String contentType = Objects.requireNonNull(servletRequest).getContentType();
        if (Objects.isNull(contentType) || !contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            log.error("参数解析异常!request:{},contentType:{}", servletRequest.getRequestURI());
            throw new RuntimeException("参数解析异常！须为json格式！");
        }
        if (!HttpMethod.POST.name().equalsIgnoreCase(servletRequest.getMethod())) {
            throw new RuntimeException("非post请求！");
        }
        return bindRequestParams(parameter, servletRequest);
    }

    private Object bindRequestParams(MethodParameter parameter, HttpServletRequest servletRequest) {
        SingleParamBody singleParamBody = parameter.getParameterAnnotation(SingleParamBody.class);

        Class<?> parameterType = parameter.getParameterType();
        String requestBody = getRequestBody(servletRequest);
        HashMap params = JSONUtil.toBean(requestBody, HashMap.class);
        params = MapUtil.isEmpty(params) ? new HashMap<>(0) : params;
        String name = StringUtils.isBlank(singleParamBody.value()) ? parameter.getParameterName() : (StringUtils.isBlank(singleParamBody.value()) ? singleParamBody.name() : singleParamBody.value());
        Object value = params.get(name);

        if (parameterType.equals(String.class)) {
            if (StringUtils.isBlank((String) value)) {
                log.error("参数解析异常,String类型参数不能为空");
                throw new RuntimeException("参数解析异常,String类型参数不能为空");
            }
        }
        if (singleParamBody.required()) {
            if (value == null) {
                log.error("参数解析异常,require=true,值不能为空");
                throw new RuntimeException("参数解析异常,require=true,值不能为空");
            }
        } else {
            if (singleParamBody.defaultValue().equals(ValueConstants.DEFAULT_NONE)) {
                log.error("参数解析异常,require=false,必须指定默认值");
                throw new RuntimeException("参数解析异常,require=false,必须指定默认值");
            }
            if (value == null) {
                value = singleParamBody.defaultValue();
            }
        }
        return Convert.convert(parameterType, value);
    }

    /**
     * 获取请求body
     *
     * @param servletRequest request
     * @return 请求body
     */
    private String getRequestBody(HttpServletRequest servletRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(servletRequest);
        try {
            BufferedReader reader = requestWrapper.getReader();
            char[] buf = new char[1024];
            int length;
            while ((length = reader.read(buf)) != -1) {
                stringBuilder.append(buf, 0, length);
            }
        } catch (IOException e) {
            log.error("读取流异常", e);
            throw new RuntimeException("读取流异常");
        }
        return stringBuilder.toString();
    }
}

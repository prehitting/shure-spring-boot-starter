package cn.shure.framework.security.base;


import cn.hutool.core.collection.CollectionUtil;
import cn.shure.framework.core.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description BaseSecuredApiMapping
 * @Author 下课菌
 * @Date 2023/3/5
 */
public class BaseSecuredApiMapping<A extends Annotation> extends LinkedHashMap<HttpMethod, ArrayList<String>> {

    @Resource
    @Qualifier("requestMappingHandlerMapping")
    private RequestMappingHandlerMapping requestMappingHandlerMapping;


    protected BaseSecuredApiMapping() {
        for (HttpMethod method : HttpMethod.values()) {
            put(method, new ArrayList<>());
        }
    }

    @PostConstruct
    protected final void initHandlerMapping() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Class<A> annotationClass = (Class<A>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        handlerMethods.forEach(((info, handlerMethod) -> {
            Method reqMethod = handlerMethod.getMethod();
            if (!reqMethod.isAnnotationPresent(annotationClass)) {
                return;
            }
            A annotation = reqMethod.getAnnotation(annotationClass);
            if (!decide(annotation, info, reqMethod)) {
                return;
            }
            Set<String> patternValues = info.getPatternValues().stream().map(pattern -> custom(annotation, pattern)).collect(Collectors.toSet());
            Set<RequestMethod> supportMethods = info.getMethodsCondition().getMethods();
            supportMethods.forEach(method -> {
                if (CollectionUtil.isNotEmpty(patternValues)) {
                    get(HttpUtil.parseToHttpMethod(method)).addAll(patternValues);
                }
            });
        }));
    }

    protected String custom(A annotation, String pattern) {
        return pattern;
    }

    protected boolean decide(A annotation, RequestMappingInfo info, Method reqMethod) {
        return true;
    }


}

package cn.shure.framework.security.annotation.handler;


import cn.shure.framework.core.utils.HttpUtil;
import cn.shure.framework.security.annotation.Anonymous;
import cn.shure.framework.security.base.BaseSecuredApiMapping;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;

/**
 * @Description AnonymousApiMapping
 * @Author 下课菌
 * @Date 2023/3/5
 */
@Component
public class AnonymousApiMapping extends BaseSecuredApiMapping<Anonymous> {

    public AnonymousApiMapping() {
        super();
    }

    @Override
    protected String custom(Anonymous annotation, String pattern) {
        if (StringUtils.hasText(annotation.value())) {
            return annotation.value();
        }
        return HttpUtil.parseRestfulToAnt(pattern);
    }

    @Override
    protected boolean decide(Anonymous annotation, RequestMappingInfo info, Method reqMethod) {
        return super.decide(annotation, info, reqMethod);
    }
}

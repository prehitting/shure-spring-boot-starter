package cn.shure.framework.security.annotation.handler;


import cn.shure.framework.core.utils.HttpUtil;
import cn.shure.framework.security.annotation.Exposed;
import cn.shure.framework.security.base.BaseSecuredApiMapping;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;

/**
 * @Description ExposedAnnotationHandler
 * @Author 下课菌
 * @Date 2023/3/5
 */
@Component
public class ExposedApiMapping extends BaseSecuredApiMapping<Exposed> {

    public ExposedApiMapping() {
        super();
    }

    @Override
    protected String custom(Exposed annotation, String pattern) {
        if (StringUtils.hasText(annotation.value())) {
            return annotation.value();
        }
        return HttpUtil.parseRestfulToAnt(pattern);
    }

    @Override
    protected boolean decide(Exposed annotation, RequestMappingInfo info, Method reqMethod) {
        return super.decide(annotation, info, reqMethod);
    }
}

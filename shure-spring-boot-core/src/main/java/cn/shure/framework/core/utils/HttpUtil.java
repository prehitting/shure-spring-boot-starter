package cn.shure.framework.core.utils;

import cn.hutool.json.JSONUtil;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @Description HttpUtil
 * @Author 下课菌
 * @Date 2023/3/5
 */
public class HttpUtil {

    public static void writeToResponse(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter writer = response.getWriter();
        writer.write(JSONUtil.toJsonStr(obj));
    }

    public static HttpMethod parseToHttpMethod(RequestMethod requestMethod) {
        return HttpMethod.resolve(requestMethod.name());
    }

    public static String parseRestfulToAnt(String rawPath) {
        return rawPath.replaceAll("\\{.*?}", "*");
    }
}

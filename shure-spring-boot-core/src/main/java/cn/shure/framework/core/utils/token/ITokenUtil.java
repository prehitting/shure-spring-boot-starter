package cn.shure.framework.core.utils.token;

import java.util.Map;

/**
 * @Description ITokenUtil
 * @Author 下课菌
 * @Date 2023/2/28
 */
public interface ITokenUtil {

    /**
     * 自定义载荷创建Token
     *
     * @param payloads 载荷
     * @return token
     */
    String createToken(final Map<String, Object> payloads);

    /**
     * 创建token
     *
     * @param payloads        载荷
     * @param expireInSeconds 过期时间（秒）
     * @return token
     */
    String createToken(final Map<String, Object> payloads, long expireInSeconds);

    /**
     * 获取载荷
     *
     * @param token token
     * @return 载荷map
     */
    Map<String, Object> getPayloads(String token);

    /**
     * 验证token有效性
     *
     * @param token token
     * @return 是否有效
     */
    boolean verify(String token);


    boolean isExpired(String token);

}

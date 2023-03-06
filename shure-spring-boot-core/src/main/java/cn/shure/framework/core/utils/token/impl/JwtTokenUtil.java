package cn.shure.framework.core.utils.token.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import cn.shure.framework.core.utils.token.ITokenUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Description JwtTokenUtil
 * @Author 下课菌
 * @Date 2023/2/28
 */
@Slf4j
@Setter
@Component
@ConditionalOnProperty(prefix = "shure.web.token.jwt", name = "key")
@ConfigurationProperties(prefix = "shure.web.token.jwt")
public class JwtTokenUtil implements ITokenUtil {

    private Long expire = 3600L;

    private String issuer = "cn.shure";

    private String key;

    private JWTSigner signer;

    @PostConstruct
    private void checkArgs() {
        if (Objects.isNull(key)) {
            throw new IllegalArgumentException("JWT密钥缺失！");
        }
        signer = JWTSignerUtil.hs512(key.getBytes(StandardCharsets.UTF_8));
    }

    private JWT createBaseJwt(Map<String, Object> payloads) {
        return JWT.create()
                .setSigner(signer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setIssuer(issuer)
                .setCharset(StandardCharsets.UTF_8)
                .setJWTId(UUID.fastUUID().toString(true))
                .addPayloads(payloads);
    }

    @Override
    public String createToken(Map<String, Object> payloads) {
        return createBaseJwt(payloads)
                .setExpiresAt(new Date(System.currentTimeMillis() + expire * 1000))
                .sign();
    }

    @Override
    public String createToken(Map<String, Object> payloads, long expireInSeconds) {
        return createBaseJwt(payloads)
                .setExpiresAt(new Date(System.currentTimeMillis() + expireInSeconds * 1000))
                .sign();
    }

    @Override
    public Map<String, Object> getPayloads(String token) {
        JSONObject payloads;
        try {
            JWT jwt = JWTUtil.parseToken(token);
            payloads = jwt.getPayloads();
        } catch (Exception e) {
            return null;
        }
        return new HashMap<>(payloads);
    }

    @Override
    public boolean verify(String token) {
        try {
            return JWTUtil.verify(token, signer) && JWTUtil.parseToken(token).getPayload(JWTPayload.ISSUER).equals(issuer);
        } catch (JWTException e) {
            return false;
        }
    }

    @Override
    public boolean isExpired(String token) {
        if (!verify(token)) {
            return false;
        }
        JWT jwt = JWTUtil.parseToken(token);
        return new Date((Long) jwt.getPayload(JWTPayload.ISSUED_AT)).before(new Date(System.currentTimeMillis()));
    }
}

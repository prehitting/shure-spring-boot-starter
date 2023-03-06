package cn.shure.framework.core.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description ShureResult
 * @Author 下课菌
 * @Date 2023/3/5
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class ShureResult<T> implements Serializable {

    private Integer code;

    private String message;

    private T data;

    private static final int SUCCESS = 2000;

    private static final int CREATED = 2001;

    private static final int ERROR = 4000;


    public static <T> ShureResult<T> ok() {
        return new ShureResult<>(SUCCESS, "SUCCESS", null);
    }

    public static <T> ShureResult<T> ok(T data) {
        return new ShureResult<>(CREATED, "SUCCESS", null);
    }

    public static <T> ShureResult<T> ok(T data, String message) {
        return new ShureResult<>(CREATED, message, null);
    }

    public static <T> ShureResult<T> fail(Integer code, String message) {
        return new ShureResult<>(code, message, null);
    }

    public static <T> ShureResult<T> fail(String message) {
        return new ShureResult<>(ERROR, message, null);
    }

    public static <T> ShureResult<T> fail(Integer code) {
        return new ShureResult<>(code, "", null);
    }

}

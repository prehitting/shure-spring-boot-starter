package cn.shure.framework.core.constant.regex;

/**
 * @Description RegexConstant
 * @Author 下课菌
 * @Date 2023/3/1
 */
public final class RegexConstant {

    public static final String PHONE = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    public static final String EMAIL = "/^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$/";

    public static final String ID_CARD = " /^[1-9]\\d{5}(19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$/";

    /**
     * 数字+字母+特殊符号任选2种，8-15位
     */
    public static final String PASSWORD_LEVEL_1 = "^(?![a-zA-Z]+$)(?!\\d+$)(?![^\\da-zA-Z\\s]+$).{8,15}$";

}

package com.hand.handlibray.util;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by feng on 2016/7/31.
 */
public class TextUtil {
    //匹配手机号
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^1[3|4|5|6|7|8|9]\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    //匹配由数字、26个英文字母或者下划线组成的字符串,至少是6位密码
    public static boolean isPSD(String passWord) {
        Pattern p = Pattern.compile("^\\w+");
        Matcher m = p.matcher(passWord);
        return m.matches() && passWord.length() >= 6;
    }

    //匹配验证码
    public static boolean isIdenCode(String idenCode) {
        Pattern p = Pattern.compile("^[0-9]\\d*");
        Matcher m = p.matcher(idenCode);
        return m.matches() && idenCode.length() == 6;
    }

    /**
     * 校验身份证
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])", idCard);
    }
    //隐藏电话中间4位
    public static void hideMidlleTelNumber(TextView tv_tel) {
        String mobile = tv_tel.getText().toString();
        if (mobile != null) {
            String maskNumber = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
            tv_tel.setText(maskNumber);
        } else {
            tv_tel.setText("");
        }

    }

    /**
     * 保留一位小数
     *
     * @param num
     * @return
     */
    public static String getSNum(double num) {
        return String.format("%.1f", num);
    }

    /**
     * 保留两位小数
     *
     * @param num
     * @return
     */
    public static String getNumber(double num) {
        return String.format("%.2f", num);
    }


}

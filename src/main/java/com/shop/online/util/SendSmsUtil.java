package com.shop.online.util;

import com.alibaba.fastjson.JSONObject;
import com.shop.online.constant.SendServerUserInfo;
import com.zhenzi.sms.ZhenziSmsClient;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/26 0026
 */
public class SendSmsUtil {
    public static final String APP_URI = "https://sms_developer.zhenzikj.com";
    public static final String APP_ID = "100948";
    public static final String APP_SECRET = "62dfe78d-26ae-4659-b734-5511ce941ae3";

    public static boolean sendSms(String code, String number) throws Exception {
        ZhenziSmsClient client = new ZhenziSmsClient(SendServerUserInfo.APP_URI, SendServerUserInfo.APP_ID, SendServerUserInfo.APP_SECRET);
        String result = client.send(number, "您的验证码为:" + code + "，该码有效期为3分钟，该码只能使用一次！");
        JSONObject json = JSONObject.parseObject(result);
        //发送短信失败
        if (json.getIntValue("code") != 0) {
            return false;
        }
        return true;
    }
}

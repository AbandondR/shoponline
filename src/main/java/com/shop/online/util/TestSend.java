package com.shop.online.util;

import com.alibaba.fastjson.JSONObject;
import com.shop.online.constant.SendServerUserInfo;
import com.zhenzi.sms.ZhenziSmsClient;

import java.util.Random;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/9 0009
 */
public class TestSend {
    public static void main(String[] args) {
        try {
            JSONObject json = null;
            //生成6位验证码
            String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
            //发送短信
            ZhenziSmsClient client = new ZhenziSmsClient(SendServerUserInfo.APP_URI, SendServerUserInfo.APP_ID, SendServerUserInfo.APP_SECRET);
            String balance = client.balance();
            json = JSONObject.parseObject(balance);
            if (json.getIntValue("code") != 0) {
                System.out.println(json.get("code") + ", "+json.get("data"));
                return;
            }
            if(json.getIntValue("data") <= 0)
            {
                return;
            }
            System.out.println(json.get("code") + ", "+json.get("data"));
            String result = client.send("13281094790", "您的验证码为:" + verifyCode + "，该码有效期为3分钟，该码只能使用一次！","dfee_dfdw_xdfd_dfdfd");
            json = JSONObject.parseObject(result);
            //发送短信失败
            if (json.getIntValue("code") != 0) {
                System.out.println(json.get("code") + ", "+json.get("data"));
            }
            else {
                String resultInfo = client.findSmsByMessageId("dfee_dfdw_xdfd_dfdfd");
                System.out.println(resultInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

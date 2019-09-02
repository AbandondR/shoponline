package com.shop.online.constant;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016092500595499";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC6WoSRlY7gxVl/p71tiHqDKnAdAUF6THvUozoNZHHo05/aJJoXM/l9OVKeTQaEL9+tZMUyy9aFE5Q2Tscz0d8BUSEjsJjlgoj+fkdDxG8Di/xqHWz1/6lzczo1gnmq2a7oWJ4ykaQFoTtPVKQeAg+hPoBUkQUXT5xdSX4z5/3flN90z3ExQOp7sIWHHDkInho/IbVblwK+Bi5D5rkH+YP2yc9RGpM2uOAV5HDhuuMeQOdeyBBO/gGWBOfgJ7HerDqkLvqCPbHpPVPtj4ItpnnbFHtMXlVzr/OIq8mnUxQiwmrUZOcNuy+LDYZ7MuZNJibz5tdkqIdu6vjcn6YUbIVJAgMBAAECggEAUXf/GtJiazt7vAYuFzOiM7Oa3eNNp9TGEhjl6TLvZZUW/jwgLWKogKlNyCzuW12+kMRMPb/FYdpEYlWgWrQ2pRG9xiePQE8H6xJzv1miGs/ZIt8IYqdJglJusTQAUsPl5mfPiVYIefmZvz3GnePSx3BWlCHuMtm4B0R46uJSJ6rjgKTRALhHMtmC1vXxvD4O2TjGTkOKBtbUDVeBxF88LpYkigAZqLO1CrwwF+S8YmiP3QgXaJWnKkr5oJ2N9OZRmCyFMUApX0+nHI/hT46C+fJoUxUJf7f8+25PnsCbregEMsguKOkYP6FHHy53lN4nemGX8vo0KaobtQK3/YSSXQKBgQDpF77OE9zkFz4e8IiyqKVHxQjUXZVyGyoudTxqdXUO5A8DnI8RzZyTjEayW+5k+FA+bwiLjD2EndPzpr/aMgUQ8WrA8nL/5G3CPCxmM/h03gpMO3LS9AjkJ0fzDhJ+q8rsMVALRINFyJcyWNcvrNq/kWP5V3suVBqQDp8UMKHyjwKBgQDMquMSPG3GHvYJH2bQ+PX64Qh4I2alNdotba/9GmA2J1cPkzqnmd2/G80zPciF6Vn+WdXMgPG5DwKgSpqmwCfnkKtInpE6ZCg/EWdqyb6Uluq61W+Ho3XNr4r29ghQyOnXRCkNa3XAa1cTab6b/xt/5eIxdKdB7aB10nUA3KEWpwKBgAoY7DwTIjODSj6e3t0Wbyp/v5UrhKjPwnEGxRo7glmlyXy3kH8N8CV4IyvgzZ/0Hc3Gk5Ev/w46pA7e/1qdZhjIzH4VQqQRKiwK3UWEJNQzNjD9NkkrJyNzDCvcRTLVkFHRBQ2n5yPjv6D/tjqre0JnMvR/NKfaHth0ubFvNeTDAoGAeEn/h1pI0makCBySm0OXnsnf5mw4CeWqw4RyKPn1jh51BsxrbtH6CDZY1kL7tei+YcV+6ODoMPHngzoVxuetDGZ0pQu9I2p2VQrvk03bWw2QDBshn8CsniUMMbXMkV5kQYbn8eE0pdDwrpI52wmGqhb8aEC9hPwakOUOq2EAtIUCgYEAqIFmrfetkcVo4RBibfHFuKoZZV8ovkxRFpQPnVVsXCDx1CumEHhNXHz85DV6YcuSih85IXGrSWoW0e+Q7wbvva81bcfpjJEzLcSrBVVv+0RsZqfn2dLPmgN1DFtAi41ux9BhuoFRJZapNQIM1RuNADSp+o2TmW1PiW0lcmoIUpU=";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAso4/G2RmG0Tk8hyz1CxjABNP6vHq4Djk+TZA6AEiZy9F77VFf9ZSX9PGHmMdbqaHV9oYDuq42IXFyTrgWINE0gBiGHi4L/t6eS3BU1LlQRFF7gzehnh7ETsYR8Ep3/tFi0vd4MYJ3lT38jay4FIOJ461z/VIMSPXXdmGfrA8A/syesBiOjWHG0utuXnr+BdxGR106a4JT49SlwAXmQrB74BosO33EcwoDY9WXxjhVAz9ankw4ybgRWG2miOYacrKpKviQyazceVFbLcRBGCB0cspvWMv4NrZhgIXpAfsmKZ6bZmxmLYfueKjWs6KtBA6iCe7/FbG5tR+S2lxTMDCswIDAQAB";

    public static String public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAulqEkZWO4MVZf6e9bYh6gypwHQFBekx71KM6DWRx6NOf2iSaFzP5fTlSnk0GhC/frWTFMsvWhROUNk7HM9HfAVEhI7CY5YKI/n5HQ8RvA4v8ah1s9f+pc3M6NYJ5qtmu6FieMpGkBaE7T1SkHgIPoT6AVJEFF0+cXUl+M+f935TfdM9xMUDqe7CFhxw5CJ4aPyG1W5cCvgYuQ+a5B/mD9snPURqTNrjgFeRw4brjHkDnXsgQTv4BlgTn4Cex3qw6pC76gj2x6T1T7Y+CLaZ52xR7TF5Vc6/ziKvJp1MUIsJq1GTnDbsviw2GezLmTSYm8+bXZKiHbur43J+mFGyFSQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://24n3c66025.zicp.vip/orders/notify_url";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://24n3c66025.zicp.vip/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "/log/";

	//支付超时时间
	public static String pay_timeout = "1h";

	//UID
	public static String seller_id = "2088102177251732";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


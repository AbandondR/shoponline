package com.shop.online.util;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件发送工具类
 */
public class MailUitls {
    /**
     * 发送邮件的方法
     *
     * @param to   :收件人
     * @param code :激活码
     */
    public static void sendMail(String to, String code, String userId) {
        /**
         * 1.获得一个Session对象.
         * 2.创建一个代表邮件的对象Message.
         * 3.发送邮件Transport
         */
        // 1.获得连接对象
        Properties props = new Properties();
        //设置发送邮件的主机
        props.setProperty("mail.smtp.host", "smtp.163.com");
        props.setProperty("mail.smtp.auth", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //使用那个发邮件
                return new PasswordAuthentication("yjh2test@163.com", "yjh2test");
            }
        });

        //session.setDebug(true);
        // 2.创建邮件对象:
        Message message = new MimeMessage(session);

        try {
            // 设置发件人:
            message.setFrom(new InternetAddress("yjh2test@163.com"));
            // 设置收件人:
            message.addRecipient(RecipientType.TO, new InternetAddress(to));
            // 抄送 CC   密送BCC
            // 设置标题
            message.setSubject("来自购物天堂官方激活邮件");
            message.setSentDate(new Date());
            // 设置邮件正文:
            message.setContent
                    ("<h1>购物天堂官方激活邮件!点下面链接完成激活操作!</h1><h3" +
							"><a href='http://24n3c66025.zicp.vip/user/active?code=" + code + "&id="+userId+"'>立即激活" +
							"</a></h3>", "text/html;charset=UTF-8");
            // 3.发送邮件:
            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //sendMail("2918694831@qq.com", "11111111111111");
    }
}
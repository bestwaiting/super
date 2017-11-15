package com.bestwaiting.mail;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by bestwaiting on 17/5/27.
 */
public class MailJavaUtils {
    private static final String sendUserName = "zr_lele@126.com";// 发送邮件需要连接的服务器的用户名

    private static final String sendPassword = "pwd";// 发送邮件需要连接的服务器的密码

    private static final String sendProtocol = "smtp";// 发送邮件使用的端口

    private static final String sendHostAddress = "smtp.126.com";// 发送邮件使用的服务器的地址

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");// 服务器需要认证
        properties.setProperty("mail.transport.protocol", sendProtocol);// 声明发送邮件使用的端口
        properties.setProperty("mail.host", sendHostAddress);// 发送邮件的服务器地址

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendUserName, sendPassword);
            }
        });
        session.setDebug(true);//在后台打印发送邮件的实时信息

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("zr_lele@126.com"));
        message.setSubject("Demo2JavaCode发送邮件测试，采用Authenticator");// 设置主题
        message.setRecipients(Message.RecipientType.TO, InternetAddress
                .parse("492134880@qq.com,zr_lele@126.com"));// 发送
        message.setRecipients(Message.RecipientType.CC, InternetAddress
                .parse("msn_zr_lele@hotmail.com"));// 抄送
        message.setContent(
                        "如果您看到，证明测试成功了！",
                        "text/html;charset=gbk");

        Transport.send(message);//发送邮件
    }

    public static void test() throws Exception {

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");// 服务器需要认证
        properties.setProperty("mail.transport.protocol", sendProtocol);// 声明发送邮件使用的端口
        properties.setProperty("mail.host", sendHostAddress);// 发送邮件的服务器地址

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendUserName, sendPassword);
            }
        });

        session.setDebug(true);//在后台打印发送邮件的实时信息

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("zr_lele@126.com"));
        message.setSubject("Demo2JavaCode发送邮件测试，采用Authenticator");// 设置主题
        message.setRecipients(Message.RecipientType.TO, InternetAddress
                .parse("492134880@qq.com,zr_lele@126.com"));// 发送
        message.setRecipients(Message.RecipientType.CC, InternetAddress
                .parse("msn_zr_lele@hotmail.com"));// 抄送
        message.setContent(
                        "如果您看到，证明测试成功了！",
                        "text/html;charset=gbk");

        MimeMultipart msgMultipart = new MimeMultipart("mixed");// 标明邮件的组合关系，混合的关系
        message.setContent(msgMultipart);// 设置邮件体


        MimeBodyPart attch1 = new MimeBodyPart();// 附件1
        MimeBodyPart attch2 = new MimeBodyPart();// 附件2
        MimeBodyPart content = new MimeBodyPart();// 邮件的正文，混合体（图片+文字）

        // 将附件和正文设置到这个邮件体中
        msgMultipart.addBodyPart(attch1);
        msgMultipart.addBodyPart(attch2);
        msgMultipart.addBodyPart(content);

        // 设置第一个附件
        DataSource ds1 = new FileDataSource("F:/ACCP5.0/文件/ssh配置.txt");// 指定附件的数据源
        DataHandler dh1 = new DataHandler(ds1);// 附件的信息
        attch1.setDataHandler(dh1);// 指定附件
        attch1.setFileName("ssh.txt");

        // 设置第二个附件
        DataSource ds2 = new FileDataSource("resource/48.jpg");// 指定附件的数据源
        DataHandler dh2 = new DataHandler(ds2);// 附件的信息
        attch2.setDataHandler(dh2);// 指定附件
        attch2.setFileName("48.jpg");

        //设置邮件的正文
        MimeMultipart bodyMultipart = new MimeMultipart("related");//依赖关系
        content.setContent(bodyMultipart);//指定正文
        MimeBodyPart htmlPart = new MimeBodyPart();
        MimeBodyPart gifPart = new MimeBodyPart();
        bodyMultipart.addBodyPart(htmlPart);
        bodyMultipart.addBodyPart(gifPart);


        DataSource gifds = new FileDataSource("resource/48.jpg");//正文的图片
        DataHandler gifdh = new DataHandler(gifds);
        gifPart.setHeader("Content-Location", "http://mimg.126.net/logo/126logo.gif");
        gifPart.setDataHandler(gifdh);//设置正文的图片

        htmlPart.setContent("我只是来打酱油的，这是我的形象照！", "text/html;charset=gbk");//设置正文文字

        message.saveChanges();//保存邮件

        //将邮件保存成文件
        OutputStream ops = new FileOutputStream("C:/Users/Administrator/Desktop/test.eml");
        message.writeTo(ops);
        ops.close();

        Transport.send(message);
    }
}

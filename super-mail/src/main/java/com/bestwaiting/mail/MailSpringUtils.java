package com.bestwaiting.mail;

import com.google.common.base.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by bestwaiting on 17/5/27.
 */
public class MailSpringUtils {
    public static void main(String[] args) throws Exception {
        EmailDTO dto = new EmailDTO();
        dto.setSubject("hello");
        dto.setEmailContent("test");
        String[] temp = new String[]{"416560142@qq.com"};
        dto.setReceivers(temp);
        sendEmailMessageOfHtmlText(dto, new Date());
    }

    public static void sendEmailMessageOfHtmlText(EmailDTO emailDto, Date date) throws MessagingException {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setDefaultEncoding(Charsets.UTF_8.toString());
        javaMailSender.setProtocol("smtp");
        javaMailSender.setHost("smtp.126.com");
        javaMailSender.setUsername("zr_lele@126.com");
        javaMailSender.setPassword("***");

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", Boolean.TRUE);
        javaMailProperties.put("mail.smtp.starttls.enable", Boolean.TRUE);
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.smtp.timeout", "10000");
        javaMailSender.setJavaMailProperties(javaMailProperties);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(javaMailSender.getUsername());

        helper.setValidateAddresses(true);
        if (StringUtils.isNotBlank(emailDto.getEmailContent())) {
            helper.setText(emailDto.getEmailContent(), true);
        }
        helper.setSubject(emailDto.getSubject());

        helper.setTo(emailDto.getReceivers());
        if (emailDto.getBcc() != null && emailDto.getBcc().length != 0) {
            helper.setBcc(emailDto.getBcc());
        }

        if (emailDto.getCc() != null && emailDto.getCc().length != 0) {
            helper.setCc(emailDto.getCc());
        }

        if (null == date) {
            date = new Date();
        }
        helper.setSentDate(date);

        javaMailSender.send(message);
    }
}

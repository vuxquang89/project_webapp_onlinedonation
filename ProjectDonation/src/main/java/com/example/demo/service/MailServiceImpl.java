package com.example.demo.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService{

	private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\""; //noi dung mail ho tro dinh danh html
	
	@Value("${config.mail.host}")
	private String host;
	@Value("${config.mail.port}")
	private String port;
	@Value("${config.mail.username}")
	private String email;
	@Value("${config.mail.password}")
	private String password;
	
	@Autowired
	ThymeleafServiceImpl thymeleafService;
	
	/*init set up info host mail*/
	@Override
	public Session init() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", port);
        
        Session session = Session.getInstance(properties, new Authenticator() {
        	@Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
        	}
		});
        
        return session;
	}
	
	/*send mail create account to email register*/
	@Override
	public void sendMail(String name, String username, String pass, String received_mail) {
		
        
        Message message = new MimeMessage(init());
        try {
			message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(received_mail)});
			message.setFrom(new InternetAddress(email));
	        message.setSubject("Tài khoản Trao Yêu Thương");
	        message.setContent(thymeleafService.getContent(name, username, pass), CONTENT_TYPE_TEXT_HTML);
	        Transport.send(message);
		} catch (MessagingException e) {
			
			e.printStackTrace();
		}
        
	}
}

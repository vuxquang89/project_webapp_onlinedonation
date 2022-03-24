package com.example.demo.service;

import javax.mail.Session;


public interface MailService {

	/*init set up info host mail*/
	Session init();
	
	/*send mail create account to email register*/
	void sendMail(String name, String username, String pass, String received_mail);
}

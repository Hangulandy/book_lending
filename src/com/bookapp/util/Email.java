package com.bookapp.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.bookapp.business.Member;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class Email {
	
	public static void sendEmail(Member member, String messageContent) throws MessagingException, UnsupportedEncodingException {

	Properties props = new Properties();
    props.put("mail.transport.protocol", "smtps");
    props.put("mail.smtps.host", "smtp.gmail.com");
    props.put("mail.smtps.port", 465);
    props.put("mail.smtps.auth", "true");
    props.put("mail.smtps.quitwait", "false");

    Session session = Session.getDefaultInstance(props);
    session.setDebug(true);

    Message message = new MimeMessage(session);

    //TODO: 
    message.setSubject("SUBJECT GOES HERE");

    //TODO:
    message.setText("something");
    message.setContent(messageContent, "text/html");

    
    //TODO: 
    Address toAddress = new InternetAddress(member.getEmail(), member.getUserName());
    message.setRecipient(Message.RecipientType.TO, toAddress);

    //FIXME:
//    Transport transport = session.getTransport();
//    transport.connect("EMAIL","PASSWORD");
//    transport.sendMessage(message, message.getAllRecipients());
//    transport.close();
    
    }
}

package com.cnpm.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cnpm.dto.mail.MailDto;

@Service
public class MailService {
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendEmail(MailDto request) {

        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(request.getEmail(), request.getEmail());
        msg.setSubject("Potencial Contacto");
        String emailBody = "Nombre: " + request.getName() + "\nEmail: " +
                request.getEmail() + "\n\n\nMensaje: " + request.getDescription();
        msg.setText(emailBody);

        javaMailSender.send(msg);

        System.out.println("Mail Session has been created successfully..");

    }
}

package com.gustavo.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.gustavo.cursomc.domain.Pedido;

public interface EmailService {
	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);
}
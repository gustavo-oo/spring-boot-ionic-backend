package com.gustavo.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.gustavo.cursomc.domain.Cliente;
import com.gustavo.cursomc.dto.ClienteDTO;
import com.gustavo.cursomc.repositories.ClienteRepository;
import com.gustavo.cursomc.resources.exceptions.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@Autowired
	private HttpServletRequest request;
	
	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
		
		List<FieldMessage> list = new ArrayList<>();
		
		Cliente cliente = clienteRepository.findByEmail(objDto.getEmail());
		
		if(cliente != null && cliente.getId() != uriId) {
			list.add(new FieldMessage("email", "Email já está em uso"));
		}

		//Transforma cada erro do tipo FieldMessage e converte para o erro do SpringBoot
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}

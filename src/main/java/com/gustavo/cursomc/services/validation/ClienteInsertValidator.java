package com.gustavo.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.gustavo.cursomc.domain.enums.TipoCliente;
import com.gustavo.cursomc.dto.ClienteNewDTO;
import com.gustavo.cursomc.resources.exceptions.FieldMessage;
import com.gustavo.cursomc.services.validation.utils.DocumentUtil;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();

		if(TipoCliente.toEnum(objDto.getTipo()) == TipoCliente.PESSOAFISICA) {
			if(!DocumentUtil.isValidCpf(objDto.getCpfOuCnpj())) {
				list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
			}
		}
		
		if(TipoCliente.toEnum(objDto.getTipo()) == TipoCliente.PESSOAJURIDICA) {
			if(!DocumentUtil.isValidCnpj(objDto.getCpfOuCnpj())) {
				list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
			}
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

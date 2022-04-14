package com.gustavo.cursomc.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.cursomc.domain.Cidade;
import com.gustavo.cursomc.domain.Cliente;
import com.gustavo.cursomc.domain.Endereco;
import com.gustavo.cursomc.domain.enums.TipoCliente;
import com.gustavo.cursomc.dto.ClienteDTO;
import com.gustavo.cursomc.dto.ClienteNewDTO;
import com.gustavo.cursomc.repositories.ClienteRepository;
import com.gustavo.cursomc.repositories.EnderecoRepository;
import com.gustavo.cursomc.services.exceptions.DataIntegrityException;
import com.gustavo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	EnderecoRepository enderecoRepository;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = clienteRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		
		obj.setId(null);
		Cliente savedClient = clienteRepository.save(obj);
		
		enderecoRepository.saveAll(savedClient.getEnderecos());
		
		return savedClient;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		
		updateData(newObj, obj);
		return clienteRepository.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		
		try {
			clienteRepository.deleteById(id);
		}
		catch(DataIntegrityViolationException exception){
			throw new DataIntegrityException("Não é possível excluir um cliente porque há pedidos relacionados");
		}
		
	}
	
	public List<Cliente> findAll(){
		return clienteRepository.findAll();
	}
	
	//Renderiza os dados do banco por páginas
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		//throw new UnsupportedOperationException();
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDTO) {
		//throw new UnsupportedOperationException();
		Cliente cliente = new Cliente(
				null, 
				objDTO.getNome(), 
				objDTO.getEmail(), 
				objDTO.getCpfOuCnpj(), 
				TipoCliente.toEnum(objDTO.getTipo()),
				passwordEncoder.encode(objDTO.getSenha()));
		
		Cidade cidade = new Cidade(objDTO.getCidadeId(), null, null);
		
		Endereco endereco = new Endereco(
				null, 
				objDTO.getLogradouro(), 
				objDTO.getNumero(), 
				objDTO.getComplemento(), 
				objDTO.getBairro(), 
				objDTO.getCep(), 
				cliente, 
				cidade);
		
		cliente.setEnderecos(Arrays.asList(endereco));
		
		cliente.setTelefones(new HashSet<String>(objDTO.getTelefones()));
		
		return cliente;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}

package com.gustavo.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gustavo.cursomc.domain.ItemPedido;
import com.gustavo.cursomc.domain.PagamentoComBoleto;
import com.gustavo.cursomc.domain.Pedido;
import com.gustavo.cursomc.domain.Produto;
import com.gustavo.cursomc.domain.enums.EstadoPagamento;
import com.gustavo.cursomc.repositories.ItemPedidoRepository;
import com.gustavo.cursomc.repositories.PagamentoRepository;
import com.gustavo.cursomc.repositories.PedidoRepository;
import com.gustavo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepositoy;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = pedidoRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = pedidoRepository.save(obj);
		
		pagamentoRepositoy.save(obj.getPagamento());
		
		Produto produto;
		
		for(ItemPedido item: obj.getItens()) {
			produto = produtoService.find(item.getProduto().getId());
					
			item.setDesconto(0.0);
			item.setPreco(produto.getPreco());
			item.setPedido(obj);
			item.setProduto(produto);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
}

package br.com.helpconnect.gerenciadorpedidos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.helpconnect.gerenciadorpedidos.model.Produto;
import br.com.helpconnect.gerenciadorpedidos.repository.ProdutoRepository;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	public Produto calculaPromocao(Produto produto) {
		
		double memoria = produto.getPreco() * produto.getPromocao() / 100;
		
		produto.setPreco(memoria);
		
		return produto;
	}
	
}

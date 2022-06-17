package br.com.helpconnect.gerenciadorpedidos.service;

import org.springframework.stereotype.Service;

import br.com.helpconnect.gerenciadorpedidos.model.Produto;

@Service
public class ProdutoService {
	
	public Produto calculaPromocao(Produto produto) {
		
		double memoria = produto.getPreco() * produto.getPromocao() / 100;
		
		produto.setPreco(memoria);
		
		return produto;
	}
	
}

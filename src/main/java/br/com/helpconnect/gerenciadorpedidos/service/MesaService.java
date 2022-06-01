package br.com.helpconnect.gerenciadorpedidos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.helpconnect.gerenciadorpedidos.model.Mesa;
import br.com.helpconnect.gerenciadorpedidos.model.Produto;
import br.com.helpconnect.gerenciadorpedidos.repository.MesaRepository;
import br.com.helpconnect.gerenciadorpedidos.repository.ProdutoRepository;

@Service
public class MesaService {

	@Autowired
	private MesaRepository mesaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	public Produto buscaUltimoProdutoDaMesa(long id) {
		
		// ARMAZENA OS DADOS DA MESA POR ID
		Optional<Mesa> retornoMesa = mesaRepository.findById(id);
		
		// RETORNA O ULTIMO PRODUTO CADASTRADO NO CARRINHO DA MESA
		return retornoMesa.get().getProdutos().get(retornoMesa.get().getProdutos().size() - 1);
	}
	
	public boolean gerenciaAdicionaAoCarrinhoMesa(long idMesa, long idProduto) {
		
		// RETORNA O POSITIVO OU NEGATIVO DA REQUISICAO
		boolean retorno = false;
		
		// CARREGA OS DADOS A SEREM TRATADOS
		Optional<Mesa> mesaExistete = mesaRepository.findById(idMesa);
		Optional<Produto> produtoExistente = produtoRepository.findById(idProduto);
		
		// CASO OS PRODUTOS EXISTAM, ELES ESTRAM NA CONDICAO
		if(mesaExistete.isPresent() && produtoExistente.isPresent()) {
			mesaExistete.get().getProdutos().add(produtoExistente.get());
		
			retorno = true;
		}
		
		mesaRepository.save(mesaExistete.get());
		
		return retorno;
	}
	
	public boolean gerenciaRemoveDoCarrinhoMesa(long idMesa, long idProduto) {
		
		// RETORNA O POSITIVO OU NEGATIVO DA REQUISICAO
		boolean retorno = false;
		
		// CARREGA OS DADOS A SEREM TRATADOS
		Optional<Mesa> mesaExistete = mesaRepository.findById(idMesa);
		Optional<Produto> produtoExistente = produtoRepository.findById(idProduto);
		
		// CASO OS PRODUTOS EXISTAM, ELES ESTRAM NA CONDICAO
		if(mesaExistete.isPresent() && produtoExistente.isPresent()) {
			mesaExistete.get().getProdutos().remove(produtoExistente.get());
		
			retorno = true;
			
		}
		
		mesaRepository.save(mesaExistete.get());
		
		return retorno;
	}
	
	public List<Produto> verificaDuplicidadeProdutos(Mesa mesa) {
		
		List<Produto> produtos = new ArrayList<>();
		
		int contadorProduto = 0;
		
		// NAVEGA NO ARRAY DE PRODUTO DO USUARIO, E CASO ELE SEJA REPEDIDO, E INCREMENTADO O VALOR NA QTDPRODUTO E ADICIONADO AO ARRAY
		// CASO ELE NAO EXISTA AINDA NO ARRAY
		for(int i = 0; i < mesa.getProdutos().size(); i++) {
			for(int j = 0; j < mesa.getProdutos().size(); j++) {
				
				// VERIFICA SE EXISTE REPETICAO DO PRODUTO, PARA REALIZAR O INCREMENTO DO VALOR AO 'QTDPRODUTO'
				if(mesa.getProdutos().contains(mesa.getProdutos().get(j))) {
					mesa.getProdutos().get(i).setQtdProduto(contadorProduto++); // INCREMENTA O VALOR NO CONTADOR
					
				}
			
			}
			
			// SE ELE NAO EXISTER NA LISTA AINDA, ELE E ADICIONADO
			if(!mesa.getProdutos().contains(mesa.getProdutos().get(i))) {
				produtos.add(mesa.getProdutos().get(i));
			}
			
			contadorProduto = 0; // ZERA O CONTADOR PARA O PROXIMO PRODUTO
			
		}
		
		for (Produto produto : mesa.getProdutos()) {
			if(mesa.getProdutos().contains(produto.getId())) {
				
				// SE ELE NAO EXISTER NA LISTA AINDA, ELE E ADICIONADO
				if(!produtos.contains(produto.getId())) {
					produtos.add(produto);
				}
				
			}
		}
		
		return produtos;
	}
	
	public boolean limpaCarrinhoMesa(long id) {
		
		boolean retorno = false;
		
		// CARREGA O OBJ A SER TRATADO
		Optional<Mesa> mesaExistete = mesaRepository.findById(id);
		
		// LIMPA OS ITENS DO CARRINHO DA MESA
		for(int i = 0; i < mesaExistete.get().getProdutos().size(); i++) {
			
			// REMOVE O ITEM DO ARRAY DO CARRINHO
			retorno = gerenciaRemoveDoCarrinhoMesa(mesaExistete.get().getId(), mesaExistete.get().getProdutos().get(i).getId());
			
			// CASO TENHA SIDO BEM SUCEDIDO E SALVO O DADO NO BANCO
			if(retorno) {
				mesaRepository.save(mesaExistete.get());
				
			}
			
		}
		
		// CASO TENHA SIDO LIMPO POR COMPLETO O ARRAY DA MESA, ELE RETORNA UM TRUE CASO CONTRARIO FALSE
		if(mesaRepository.findById(id).get().getProdutos().size() == 0) {
			retorno = true;
		
		}else {
			retorno = false;
		
		}
		
		return retorno;
	}
	
}

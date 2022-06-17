package br.com.helpconnect.gerenciadorpedidos.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.helpconnect.gerenciadorpedidos.model.Mesa;
import br.com.helpconnect.gerenciadorpedidos.model.MesaLogin;
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
		
		// CASO NAO SE TENHA DADOS NO ARRAY DO CARRINHO DA MESA RETORNA UM OBJ PRODUTO VAZIO
		if(retornoMesa.get().getProdutos().size() == 0) {
			Produto produtoVazio = new Produto();
			
			return produtoVazio;
		}
		
		int contador = 0;
		
		for(int i = 0 ; i < retornoMesa.get().getProdutos().size(); i++) {
			if(retornoMesa.get().getProdutos().get(retornoMesa.get().getProdutos().size() - 1).getId() == retornoMesa.get().getProdutos().get(i).getId()) {
				
				contador = contador + 1;
				
			}
			
		}
		
		retornoMesa.get().getProdutos().get(retornoMesa.get().getProdutos().size() - 1).setQtdProduto(contador); // AJUSTA A QTD DE PRODUTOS REPETIDOS NO ARRAY DA MESA
		
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
				if(mesa.getProdutos().get(i).getId() == mesa.getProdutos().get(j).getId()) {
					
					contadorProduto = contadorProduto + 1;
					
					mesa.getProdutos().get(i).setQtdProduto(contadorProduto); // INCREMENTA O VALOR NO CONTADOR
					mesa.getProdutos().get(i).setNome(mesa.getProdutos().get(j).getNome()); // INCREMENTA O VALOR NO CONTADOR
					mesa.getProdutos().get(i).setImg(mesa.getProdutos().get(j).getImg()); // INCREMENTA O VALOR NO CONTADOR
				
				}
				
			}
			
			if(!produtos.contains(mesa.getProdutos().get(i))) {
				System.out.println("Mesa: "+ mesa.getNome());
				System.out.println("Nome: "+ mesa.getProdutos().get(i).getNome() +" | "+ mesa.getProdutos().get(i).getQtdProduto());
				
				produtos.add(mesa.getProdutos().get(i));
				
			}
			
			contadorProduto = 0; // ZERA O CONTADOR PARA O PROXIMO PRODUTO
			
		}
		
		/*for (Produto produto : mesa.getProdutos()) {
			if(mesa.getProdutos().contains(produto.getId())) {
				
				// SE ELE NAO EXISTER NA LISTA AINDA, ELE E ADICIONADO
				if(!produtos.contains(produto.getId())) {
					produtos.add(produto);
				}
				
			}
		}*/
		
		return produtos;
	}
	
	public List<Produto> verificaDuplicidadeProdutosTodasMesas(Mesa mesa) {
		
		List<Produto> produtos = new ArrayList<>();
		
		int contadorProduto = 0;
		
		// NAVEGA NO ARRAY DE PRODUTO DO USUARIO, E CASO ELE SEJA REPEDIDO, E INCREMENTADO O VALOR NA QTDPRODUTO E ADICIONADO AO ARRAY
		// CASO ELE NAO EXISTA AINDA NO ARRAY
		for (Produto produto : mesa.getProdutos()) {

			for (Produto produtoRepeticao : mesa.getProdutos()) {

				// VERIFICA SE EXISTE REPETICAO DO PRODUTO, PARA REALIZAR O INCREMENTO DO VALOR AO 'QTDPRODUTO'
				if(produto.getId() == produtoRepeticao.getId()) {
						
					contadorProduto = contadorProduto + 1;
					
					produto.setQtdProduto(contadorProduto); // INCREMENTA O VALOR NO CONTADOR

				}

			}

			if(!produtos.contains(produto)) {
				
				if(mesa.getTipo() != "adm") {
					produtos.add(produto);
				}
				
			}
			
			contadorProduto = 0; // ZERA O CONTADOR PARA O PROXIMO PRODUTO
			
		}

		contadorProduto = 0; // ZERA O CONTADOR PARA O PROXIMO PRODUTO
		
		return produtos;
	}
	
	public double calculaTotalCarrinho(Mesa mesa) {
		
		double total = 0.0;
		
		for(int i = 0; i < mesa.getProdutos().size(); i++) {
			total = total + mesa.getProdutos().get(i).getPreco();
			
		}
		
		NumberFormat formatter = new DecimalFormat("#0.00");
		return Double.parseDouble(formatter.format(total).replace(",", "."));
	}
	
	public boolean limpaCarrinhoMesa(long id) {
		
		boolean retorno = false;
		
		// CARREGA O OBJ A SER TRATADO
		Optional<Mesa> mesaExistete = mesaRepository.findById(id);
		List<Produto> listaProduto = new ArrayList<>();
		
		// LIMPA OS ITENS DO CARRINHO DA MESA
		mesaExistete.get().setProdutos(listaProduto);
		
		// SALVA AS MODIFICACOES NO BANCO
		mesaRepository.save(mesaExistete.get());
		
		// CASO TENHA SIDO LIMPO POR COMPLETO O ARRAY DA MESA, ELE RETORNA UM TRUE CASO CONTRARIO FALSE
		if(mesaRepository.findById(id).get().getProdutos().size() == 0) {
			retorno = true;
		
		}else {
			retorno = false;
		
		}
		
		return retorno;
	}

	public Optional<MesaLogin> logar(Optional<MesaLogin> mesaLogin) {

		Optional<Mesa> mesa = mesaRepository.findByNome(mesaLogin.get().getNome());

		if (mesa.isPresent()) {
			if(mesa.get().getSenha().equals(mesaLogin.get().getSenha())) {
				mesaLogin.get().setId(mesa.get().getId());
				mesaLogin.get().setImg(mesa.get().getImg());
				mesaLogin.get().setTipo(mesa.get().getTipo());
				mesaLogin.get().setSenha(mesa.get().getSenha()); 
				mesaLogin.get().setNome(mesa.get().getNome());	

				return mesaLogin;
				
			}else {

				return null;
			}

		}

		return null;
	}
	
}

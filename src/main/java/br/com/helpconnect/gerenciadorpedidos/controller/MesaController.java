package br.com.helpconnect.gerenciadorpedidos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.helpconnect.gerenciadorpedidos.model.Mesa;
import br.com.helpconnect.gerenciadorpedidos.model.Produto;
import br.com.helpconnect.gerenciadorpedidos.repository.MesaRepository;
import br.com.helpconnect.gerenciadorpedidos.service.MesaService;

@RestController
@RequestMapping("/mesas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MesaController {
	
	@Autowired
	private MesaRepository repository;
	
	@Autowired
	private MesaService mesaService;
	
	@GetMapping
	public ResponseEntity<List<Mesa>> findAllMesas() {
		
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Mesa> findByIdMesa(@PathVariable("id") long id) {
		
		// BUSCA A MESA POR ID
		Optional<Mesa> mesaExistente = repository.findById(id);
		Mesa mesa = new Mesa();
		
		// POPULA O OBJ COM OS DADOS RETORNANDO
		mesa.setId(mesaExistente.get().getId());
		mesa.setImg(mesaExistente.get().getImg());
		mesa.setNome(mesaExistente.get().getNome());
		mesa.setProdutos(mesaService.verificaDuplicidadeProdutos(mesaExistente.get())); // AJUSTA A LISTA, VERIFICANDO DUPLICIDANDE DE DADOS
		mesa.setSenha(mesaExistente.get().getSenha());
		
		return ResponseEntity.ok(mesa);
	}
	
	
	@GetMapping("/{nome}")
	public ResponseEntity<Mesa> findByNomeMesa(@PathVariable("nome") String nome) {
		
		return repository.findByNome(nome)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/buscaUltimoProdutoMesa/{id}")
	public ResponseEntity<Produto> findByUltimoProdutoMesa(@PathVariable("id") long id) {
		
		return ResponseEntity.ok(mesaService.buscaUltimoProdutoDaMesa(id));
	}
	
	@GetMapping("/mesa_produto/adiciona/idMesa/{idMesa}/idProduto/{idProduto}")
	public boolean adicionaProdutoMesa(@PathVariable("idMesa") long idMesa, @PathVariable("idProduto") long idProduto) {
		
		return mesaService.gerenciaAdicionaAoCarrinhoMesa(idMesa, idProduto);
	}
	
	@GetMapping("/mesa_produto/remove/idMesa/{idMesa}/idProduto/{idProduto}")
	public boolean removeProdutoMesa(@PathVariable("idMesa") long idMesa, @PathVariable("idProduto") long idProduto) {
		
		return mesaService.gerenciaRemoveDoCarrinhoMesa(idMesa, idProduto);
	}
	
}

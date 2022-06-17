package br.com.helpconnect.gerenciadorpedidos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.helpconnect.gerenciadorpedidos.model.Mesa;
import br.com.helpconnect.gerenciadorpedidos.model.MesaLogin;
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
		
		List<Mesa> listaMesas = repository.findAll();
		
		for(int i = 0; i < listaMesas.size(); i++) {
			
			// BUSCA A MESA POR ID
			Optional<Mesa> mesaExistente = repository.findById(listaMesas.get(i).getId());

			// POPULA O OBJ COM OS DADOS RETORNANDO
			listaMesas.get(i).setTotal(mesaService.calculaTotalCarrinho(mesaExistente.get()));

			// POPULA O OBJ COM OS DADOS RETORNANDO
			listaMesas.get(i).setProdutos(mesaService.verificaDuplicidadeProdutosTodasMesas(mesaExistente.get())); // AJUSTA A LISTA, VERIFICANDO DUPLICIDANDE DE DADOS

		}

		List<Mesa> listaMesasMemoria = listaMesas;

		for(int i = 0; i < listaMesasMemoria.size(); i++) {
			if(listaMesasMemoria.get(i).getTipo().equals("adm")) {
				listaMesas.remove(i);
			
			}else if(listaMesas.get(i).getProdutos().size() == 0) {
				listaMesas.remove(i);

			}

		}
		
		return ResponseEntity.ok(listaMesas);
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
		mesa.setTipo(mesaExistente.get().getTipo());
		mesa.setTotal(mesaService.calculaTotalCarrinho(mesaExistente.get()));
		
		return ResponseEntity.ok(mesa);
	}
	
	
	@GetMapping("nome/{nome}")
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
	
	@GetMapping("/limpaCarrinhoMesa/{id}")
	public boolean limpaCarrinhoMesa(@PathVariable("id") long id) {
		
		return mesaService.limpaCarrinhoMesa(id);
	}
	
	@PostMapping
	public ResponseEntity<Mesa> cadastraMesa(@RequestBody Mesa mesa) {
		
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(mesa));
	}

	@PostMapping("/login")
	public ResponseEntity<MesaLogin> login(@RequestBody Optional<MesaLogin> usuarioLogin) {
		return mesaService.logar(usuarioLogin)
			.map(resp -> ResponseEntity.ok(resp))
			.orElse(ResponseEntity.notFound().build());
	}
	
	@PutMapping
	public ResponseEntity<Mesa> atualizaMesa(@RequestBody Mesa mesa) {
		
		return ResponseEntity.status(HttpStatus.OK).body(repository.save(mesa));
	}
	
	@DeleteMapping("/{id}")
	public void deleteaMesa(@PathVariable long id) {
		repository.deleteById(id);
		
	}
	
}

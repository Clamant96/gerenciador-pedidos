package br.com.helpconnect.gerenciadorpedidos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.helpconnect.gerenciadorpedidos.model.Mesa;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
	
	public Optional<Mesa> findByNome(String nome);
	
}

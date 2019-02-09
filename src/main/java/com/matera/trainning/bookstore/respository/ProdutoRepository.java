package com.matera.trainning.bookstore.respository;

import java.util.Collection;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.matera.trainning.bookstore.domain.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	public Optional<Produto> findByCodigo(String codigo);

	@Transactional
	public void deleteByCodigo(String codigo);

	@Query("SELECT p FROM Produto p WHERE LOWER(p.descricao) LIKE CONCAT('%', LOWER(?1), '%')")
	public Collection<Produto> findByDescricao(String descricao);
	
}

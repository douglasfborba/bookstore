package com.matera.trainning.bookstore.respository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.matera.trainning.bookstore.model.Produto;

public interface ProdutoRepository extends PagingAndSortingRepository<Produto, Long> {

	public Optional<Produto> findByCodigo(String codigo);
	
	@Query("SELECT p FROM Produto p WHERE LOWER(p.descricao) LIKE CONCAT('%', LOWER(?1), '%')")
	public Page<Produto> findAllByDescricao(String descricao, Pageable pageable);

	@Query(value = "SELECT p.* FROM dis_produto p " +
				   "JOIN dis_avaliacao a ON a.produto_id = p.id " +
				   "WHERE a.rating > ?1",
		  countQuery = "SELECT COUNT(p.id) FROM dis_produto p " +
				   "JOIN dis_avaliacao a ON a.produto_id = p.id " +
				   "WHERE a.rating > ?1",
		  nativeQuery = true)
	public Page<Produto> findAllByRatingGreaterThanParam(Double rating, Pageable pageable);
		
}

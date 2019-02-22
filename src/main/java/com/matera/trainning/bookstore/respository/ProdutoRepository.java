package com.matera.trainning.bookstore.respository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.matera.trainning.bookstore.model.Produto;

public interface ProdutoRepository extends PagingAndSortingRepository<Produto, Long> {

	public Optional<Produto> findByCodigo(String codigo);
	
	@Query(value = "SELECT p.*, r.rate AS rating FROM dis_produto p " + 
				   "JOIN (SELECT produto_id, comentario_id, avg(rating) AS rate FROM dis_avaliacao GROUP BY produto_id, comentario_id) r " + 
				   "ON r.produto_id = p.id WHERE r.rate > :rating",
		   countQuery = "SELECT COUNT(p.id) AS cnt FROM dis_produto p " +  
		   				"JOIN (SELECT produto_id, comentario_id, avg(rating) AS rate FROM dis_avaliacao GROUP BY produto_id, comentario_id) r " + 
		   				"ON r.produto_id = p.id WHERE r.rate > :rating",
		   nativeQuery = true)
	public Page<Object> findAllByRatingGreaterThanParam(Double rating, Pageable pageable);
	
	@Query("SELECT p FROM Produto p WHERE LOWER(p.descricao) LIKE CONCAT('%', LOWER(?1), '%')")
	public Page<Produto> findAllByDescricao(String descricao, Pageable pageable);	
		
}

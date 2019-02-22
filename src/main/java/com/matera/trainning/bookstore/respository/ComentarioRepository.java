package com.matera.trainning.bookstore.respository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.Produto;

public interface ComentarioRepository extends PagingAndSortingRepository<Comentario, Long> {

	public Optional<Comentario> findByCodigo(String codigo);
		
	public Page<Comentario> findAllByUsuario(String usuario, Pageable pageable);

	public Page<Comentario> findAllByProduto(Produto produto, Pageable pageable);

	@Query( "SELECT c FROM Comentario c JOIN c.avaliacoes a WHERE a.rating > ?1")
	public Page<Comentario> findAllByRatingGreaterThanParam(Double rating, Pageable pageable);

}

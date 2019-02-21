package com.matera.trainning.bookstore.respository;

import java.util.Optional;
import java.util.OptionalDouble;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.matera.trainning.bookstore.model.Avaliacao;
import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.Produto;

public interface AvaliacaoRepository extends PagingAndSortingRepository<Avaliacao, Long> {

	@Query(value = "SELECT AVG(a.rating) FROM dis_avaliacao a WHERE a.produto_id = :#{#produto.id}", nativeQuery = true)
	public OptionalDouble findAvgRatingByProduto(@Param("produto") Produto produto);

	public Optional<Avaliacao> findByCodigo(String codAvaliacao);

	public Page<Avaliacao> findAllByUsuario(String usuario, Pageable pageable);

	public Page<Avaliacao> findAllByProduto(Produto produto, Pageable pageable);

	public Page<Avaliacao> findAllByComentario(Comentario comentario, Pageable pageable);

}

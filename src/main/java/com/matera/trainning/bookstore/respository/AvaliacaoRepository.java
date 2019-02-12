package com.matera.trainning.bookstore.respository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.matera.trainning.bookstore.domain.Avaliacao;
import com.matera.trainning.bookstore.domain.Comentario;
import com.matera.trainning.bookstore.domain.Produto;

public interface AvaliacaoRepository extends PagingAndSortingRepository<Avaliacao, Long> {
	
	public Page<Avaliacao> findAllByProduto(Produto produto, Pageable pageable);
	
	public Page<Avaliacao> findAllByComentario(Comentario comentario, Pageable pageable);

}

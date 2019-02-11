package com.matera.trainning.bookstore.respository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.matera.trainning.bookstore.domain.impl.Comentario;

public interface ComentarioRepository extends PagingAndSortingRepository<Comentario, Long> {

	public Optional<Comentario> findByCodigo(String codigo);

	public void deleteByCodigo(String codigo);	
	
	public Page<Comentario> findAllByProdutoCodigo(String codigoProduto, Pageable pageable);
	
}

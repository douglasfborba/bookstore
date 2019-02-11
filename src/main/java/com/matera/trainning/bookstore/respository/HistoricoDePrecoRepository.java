package com.matera.trainning.bookstore.respository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.matera.trainning.bookstore.domain.impl.HistoricoDePreco;

public interface HistoricoDePrecoRepository extends PagingAndSortingRepository<HistoricoDePreco, Long> {

	public Page<HistoricoDePreco> findAllByProdutoCodigo(String codigoProduto, Pageable pageable);

}

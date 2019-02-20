package com.matera.trainning.bookstore.respository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.matera.trainning.bookstore.model.HistoricoDePreco;
import com.matera.trainning.bookstore.model.Produto;

public interface HistoricoDePrecoRepository extends PagingAndSortingRepository<HistoricoDePreco, Long> {

	@Query(value = "SELECT h.* FROM dis_historico h JOIN (SELECT m.produto_id, MIN(m.preco) as pr from dis_historico m GROUP BY m.produto_id) g ON g.produto_id = h.produto_id where h.produto_id = ?1 and h.preco = g.pr", nativeQuery = true)
	public Optional<HistoricoDePreco> findMinPrecoByProduto(Long id);
	
	@Query(value = "SELECT h.* FROM dis_historico h JOIN (SELECT m.produto_id, MAX(m.preco) as pr from dis_historico m GROUP BY m.produto_id) g ON g.produto_id = h.produto_id where h.produto_id = ?1 and h.preco = g.pr", nativeQuery = true)
	public Optional<HistoricoDePreco> findMaxPrecoByProduto(Long id);
	
	public Page<HistoricoDePreco> findAllByProduto(Produto produto, Pageable pageable);
	
}

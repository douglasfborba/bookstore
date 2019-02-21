package com.matera.trainning.bookstore.respository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.matera.trainning.bookstore.model.HistoricoDePreco;
import com.matera.trainning.bookstore.model.Produto;

public interface HistoricoDePrecoRepository extends PagingAndSortingRepository<HistoricoDePreco, Long> {

	@Query(value = "SELECT h.* FROM dis_historico h "
			+ "JOIN (SELECT produto_id, MIN(preco) AS preco FROM dis_historico GROUP BY produto_id) j ON j.produto_id = h.produto_id "
			+ "WHERE h.preco = j.preco AND h.produto_id = :#{#produto.id}", 
		  nativeQuery = true)
	public Optional<HistoricoDePreco> findMinPrecoByProduto(@Param("produto") Produto produto);
	
	@Query(value = "SELECT h.* FROM dis_historico h " + 
			"JOIN (SELECT produto_id, MIN(preco) AS preco FROM dis_historico GROUP BY produto_id) j ON j.produto_id = h.produto_id " +
			"WHERE h.preco = j.preco and h.produto_id = :#{#produto.id}", 
		  nativeQuery = true)
	public Optional<HistoricoDePreco> findMaxPrecoByProduto(@Param("produto") Produto produto);
	
	@Query(value = "SELECT h.* FROM dis_historico h " + 
			"JOIN (SELECT produto_id, MIN(preco) AS preco FROM dis_historico GROUP BY produto_id) j ON j.produto_id = h.produto_id " +
			"WHERE h.preco = j.preco and h.produto_id = :#{#produto.id} AND h.data_hora_alteracao >= :#{#dtInicial} AND h.data_hora_alteracao <= :#{#dtFinal}", 
		  nativeQuery = true)
	public Optional<HistoricoDePreco> findMinByProdutoBetweenDates(@Param("produto") Produto produto, @Param("dtInicial") LocalDate dtInicial, @Param("dtFinal") LocalDate dtFinal);
	
	@Query(value = "SELECT h.* FROM dis_historico h " + 
			"JOIN (SELECT produto_id, MIN(preco) AS preco FROM dis_historico GROUP BY produto_id) j ON j.produto_id = h.produto_id " +
			"WHERE h.preco = j.preco and h.produto_id = :#{#produto.id} AND h.data_hora_alteracao >= :#{#dtInicial} AND h.data_hora_alteracao <= :#{#dtFinal}", 
		  nativeQuery = true)
	public Optional<HistoricoDePreco> findMaxByProdutoBetweenDates(@Param("produto") Produto produto, @Param("dtInicial") LocalDate dtInicial, @Param("dtFinal") LocalDate dtFinal);
	
	public Page<HistoricoDePreco> findAllByProduto(Produto produto, Pageable pageable);
	
}

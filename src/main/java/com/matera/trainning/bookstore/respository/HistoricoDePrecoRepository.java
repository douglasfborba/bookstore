package com.matera.trainning.bookstore.respository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.matera.trainning.bookstore.domain.HistoricoDePreco;

public interface HistoricoDePrecoRepository extends JpaRepository<HistoricoDePreco, Long> {
	
	public Optional<HistoricoDePreco> findByProdutoCodigoAndDataHoraAlteracao(String codigoProduto, LocalDateTime dataHoraAlteracao);

	@Query("SELECT h FROM HistoricoDePreco h WHERE h.produto.codigo = ?1 AND h.dataHoraAlteracao >= ?2 AND h.dataHoraAlteracao <= ?3")
	public List<HistoricoDePreco> findAllByProdutoCodigoWithDataHoraAlteracaoBetween(String codigoProduto, LocalDateTime inicio, LocalDateTime fim);
	
	public List<HistoricoDePreco> findAllByProdutoCodigo(String codigoProduto);
	
}

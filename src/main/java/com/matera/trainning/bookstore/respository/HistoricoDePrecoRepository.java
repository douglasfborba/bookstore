package com.matera.trainning.bookstore.respository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.matera.trainning.bookstore.domain.HistoricoDePreco;
import com.matera.trainning.bookstore.domain.HistoricoDePrecoPK;

public interface HistoricoDePrecoRepository extends JpaRepository<HistoricoDePreco, HistoricoDePrecoPK> {
	
	public Optional<HistoricoDePreco> findByPkProdutoCodigoAndPkDataHoraAlteracao(String codigoProduto, LocalDateTime dataHoraAlteracao);

	@Query("SELECT h FROM HistoricoDePreco h WHERE h.pk.produto.codigo = ?1 AND h.pk.dataHoraAlteracao >= ?2 AND h.pk.dataHoraAlteracao <= ?3")
	public List<HistoricoDePreco> findAllByPkProdutoCodigoWithDataHoraAlteracaoBetween(String codigoProduto, LocalDateTime inicio, LocalDateTime fim);
	
	public List<HistoricoDePreco> findAllByPkProdutoCodigo(String codigoProduto);
	
}

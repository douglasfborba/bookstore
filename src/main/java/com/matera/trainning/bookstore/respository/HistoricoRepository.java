package com.matera.trainning.bookstore.respository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matera.trainning.bookstore.domain.Historico;
import com.matera.trainning.bookstore.domain.HistoricoPK;

public interface HistoricoRepository extends JpaRepository<Historico, HistoricoPK> {
	
	public Optional<Historico> findByPkProdutoCodigoAndPkDataHoraAlteracao(String codigoProduto, LocalDateTime dataHoraAlteracao);
		
}

package com.matera.trainning.bookstore.service;

import org.springframework.stereotype.Service;

@Service
public class HistoricoDePrecoService {
//
//	@Autowired
//	private HistoricoDePrecoRepository repository;
//
//	public HistoricoDePreco insert(HistoricoDePreco historico) {
//		String codigo = historico.getProduto().getCodigo();
//		LocalDateTime dataHoraAlteracao = historico.getDataHoraAlteracao();
//
//		Optional<HistoricoDePreco> opcional = repository.findByProdutoCodigoAndDataHoraAlteracao(codigo, dataHoraAlteracao);
//
//		if (!opcional.isPresent())
//			return repository.save(historico);
//
//		throw new ResourceAlreadyExistsException("Histórico já existente");
//	}
//
//	public HistoricoDePreco findByProdutoCodigoAndDataHoraAlteracao(String codigo, LocalDateTime dataHoraAlteracao) throws RegistroNotFoundException {
//		Optional<HistoricoDePreco> opcional = repository.findByProdutoCodigoAndDataHoraAlteracao(codigo, dataHoraAlteracao);
//
//		if (!opcional.isPresent())
//			throw new RegistroNotFoundException("Histórico inexistente");
//
//		return opcional.get();
//	}
//	
//	public List<HistoricoDePreco> findAllByProdutoCodigo(String codigoProduto) {
//		return repository.findAllByProdutoCodigo(codigoProduto);
//	}	
//	
//	public List<HistoricoDePreco> findAllByProdutoCodigoWithDataHoraAlteracaoBetween(String codigoProduto, LocalDateTime inicio, LocalDateTime fim) {
//		return repository.findAllByProdutoCodigoWithDataHoraAlteracaoBetween(codigoProduto, inicio, fim);
//	}

}

package com.matera.trainning.bookstore.service;

import static java.util.Base64.getEncoder;
import static java.util.Comparator.comparing;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.controller.mapper.AvaliacaoMapper;
import com.matera.trainning.bookstore.controller.mapper.ComentarioMapper;
import com.matera.trainning.bookstore.controller.mapper.HistoricoDePrecoMapper;
import com.matera.trainning.bookstore.controller.mapper.ProdutoMapper;
import com.matera.trainning.bookstore.model.Avaliacao;
import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.HistoricoDePreco;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.ProdutoRepository;
import com.matera.trainning.bookstore.service.exception.RecursoAlreadyExistsException;
import com.matera.trainning.bookstore.service.exception.RecursoNotFoundException;

@Service
@Transactional(propagation = SUPPORTS, readOnly = true)
public class ProdutoService {

	@Autowired
	private ProdutoMapper mapper;
	
	@Autowired
	private ProdutoRepository repository;
	
	@Autowired
	private ComentarioService comentarioService;
	
	@Autowired
	private HistoricoDePrecoService historicoService;
	
	@Autowired
	private AvaliacaoService avaliacaoService;
			
	@Transactional(propagation = REQUIRED, readOnly = false)
	public ProdutoDTO inserirProduto(ProdutoDTO dtoProduto)  {	
		repository.findByCodigo(dtoProduto.getCodigo())
				.ifPresent(produto -> {
					String mensagem = "Produto " + produto.getCodigo() + " já existente";
					throw new RecursoAlreadyExistsException(mensagem, produto.getCodigo(), "/v1/produtos"); 
				});
				
		Produto produto = mapper.toEntity(dtoProduto);
		if (produto.getDataCadastro() == null)
			produto.setDataCadastro(LocalDate.now());
				
		Produto produtoSalvo = repository.save(produto);
		ProdutoDTO dto = historizarPreco(produtoSalvo);
		
		return dto;
	}
		
	@Transactional(propagation = REQUIRED, readOnly = false)
	public void atualizarProduto(String codProduto, ProdutoDTO dtoProduto) {		
		Produto produtoSalvo = repository.findByCodigo(codProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codProduto + " inexistente"));
		
		Produto produto = mapper.toEntity(dtoProduto);			
		produto.setId(produtoSalvo.getId());
		produto.setDescricao(dtoProduto.getDescricao());
		produto.setDataCadastro(produtoSalvo.getDataCadastro());
		produto.setComentarios(produtoSalvo.getComentarios());
		produto.setPrecos(produtoSalvo.getPrecos());
		
		if (produtoSalvo.getPreco().compareTo(dtoProduto.getPreco()) != 0)
			produto.addHistoricoDePreco(getHistoricoDePreco(produto));
		
		repository.save(produto);
	}
	
	@Transactional(propagation = REQUIRED, readOnly = false)
	public void removerProduto(String codigoProduto) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codigoProduto + " inexistente"));

		repository.delete(produto);
	}

	public ProdutoDTO buscarProdutoDadoCodigo(String codigoProduto) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codigoProduto + " inexistente"));

		return mapper.toDto(produto);
	}

	public Page<ProdutoDTO> listarProdutosDadoDescricao(String descricao, Pageable pageable) {
		return repository.findAllByDescricao(descricao, pageable)
				.map(produto -> mapper.toDto(produto));
	}

	public Page<ProdutoDTO> listarProdutos(Pageable pageable) {
		return repository.findAll(pageable)
				.map(produto -> mapper.toDto(produto));
	}
	
	public Page<ProdutoDTO> listarProdutosComRatingMaiorQueParam(Double rating, Pageable pageable) {
		Page<ProdutoDTO> produtos = repository.findAllByRatingGreaterThanParam(rating, pageable)
									.map(registro -> {
										Object[] linha = (Object[]) registro;
										ProdutoDTO dtoProduto = new ProdutoDTO();
					
										dtoProduto.setCodigo((String) linha[1]);
										dtoProduto.setDescricao((String) linha[2]);
										dtoProduto.setPreco((BigDecimal) linha[3]);	
										
										Date dataCadastro = (Date) linha[4];
										dtoProduto.setDataCadastro(dataCadastro.toLocalDate());
										
										BigDecimal avaliacao = (BigDecimal) linha[5];
										dtoProduto.setRating(avaliacao.doubleValue());
					
										return dtoProduto;
									});
		return produtos;
	}
	
	public Page<ComentarioDTO> listarComentariosDadoCodigoProduto(String codigoProduto, Pageable pageable) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codigoProduto + " inexistente"));

		return comentarioService.listarComentariosDadoProduto(produto, pageable);
	}	
		
	@Transactional(propagation = REQUIRED, readOnly = false)
	public ComentarioDTO comentarProduto(String codigoProduto, ComentarioDTO dtoComentario) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codigoProduto + " inexistente"));
		
		ComentarioMapper mapper = ComentarioMapper.INSTANCE;		
		Comentario comentario = mapper.toEntity(dtoComentario);
		
		LocalDateTime dataHoraAtual = LocalDateTime.now();

		comentario.setCodigo(geraCodigoEmBase64(comentario.getUsuario(), dataHoraAtual));
		comentario.setDataHoraCriacao(dataHoraAtual);
		comentario.setProduto(produto);
		
		produto.addComentario(comentario);
		repository.save(produto);
		
		return mapper.toDto(comentario);
	}
	
	@Transactional(propagation = REQUIRED, readOnly = false)
	public void atualizarComentario(String codigoProduto, String codigoComentario, ComentarioDTO dtoComentario) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codigoProduto + " inexistente"));
		
		Comentario comentario = produto.getComentarios().stream()
				.filter(dto -> dto.getCodigo().equals(codigoComentario))
				.findFirst()
					.orElseThrow(() -> new RecursoNotFoundException("Comentário " + codigoComentario + " inexistente"));
		
		comentario.setUsuario(comentario.getUsuario());
		comentario.setDescricao(dtoComentario.getDescricao());
				
		repository.save(produto);
	}
	
	@Transactional(propagation = REQUIRED, readOnly = false)
	public void removerComentario(String codigoProduto, String codigoComentario) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codigoProduto + " inexistente"));
		
		Comentario comentario = produto.getComentarios().stream()
			.filter(dto -> dto.getCodigo().equals(codigoComentario))
			.findFirst()
				.orElseThrow(() -> new RecursoNotFoundException("Comentário " + codigoComentario + " inexistente"));
		
		produto.removeComentario(comentario);
		
		repository.save(produto);
	}
	
	public Page<HistoricoDePrecoDTO> listarHistoricoDePrecosDadoProduto(String codigoProduto, Pageable pageable) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codigoProduto + " inexistente"));
		
		return historicoService.listarItensHistPrecosDadoProduto(produto, pageable);
	}
	
	public Page<HistoricoDePrecoDTO> listarHistoricoDePrecosNoPeriodoDadoProduto(String codigoProduto, LocalDate dataInicial, LocalDate dataFinal, Pageable pageable) {		
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codigoProduto + " inexistente"));
		
		HistoricoDePrecoMapper mapper = HistoricoDePrecoMapper.INSTANCE;

		List<HistoricoDePrecoDTO> precoes = produto.getPrecos().stream()
				.map(itemHistPreco -> mapper.toDto(itemHistPreco))
				.filter(histPrecoItem -> 
					{ 
						LocalDate dataAlteracao = histPrecoItem.getDataHoraAlteracao().toLocalDate();
						
						boolean isAfterOrEqualDataInicial = dataAlteracao.isAfter(dataInicial) || dataAlteracao.isEqual(dataInicial);
						boolean isBeforeOrEqualDataFinal = dataAlteracao.isBefore(dataFinal) || dataAlteracao.isEqual(dataFinal);
						
						return isAfterOrEqualDataInicial && isBeforeOrEqualDataFinal;		
					})
				.collect(Collectors.toList());
	
		return new PageImpl<HistoricoDePrecoDTO>(precoes, pageable, precoes.size());
	}
	
	public HistoricoDePrecoDTO buscarPrecoMaximoDadoCodigoProduto(String codProduto) {
		Produto produto = repository.findByCodigo(codProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codProduto + " inexistente"));
		
		return historicoService.buscarPrecoMaximoDadoCodigoProduto(produto);
	}
	
	public HistoricoDePrecoDTO buscarPrecoMinimoDadoCodigoProduto(String codProduto) {
		Produto produto = repository.findByCodigo(codProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codProduto + " inexistente"));
		
		return historicoService.buscarPrecoMinimoDadoCodigoProduto(produto);
	}
	
	public HistoricoDePrecoDTO buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV1(String codProduto, LocalDate dtInicial, LocalDate dtFinal) {
		Produto produto = repository.findByCodigo(codProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codProduto + " inexistente"));
		
		HistoricoDePrecoMapper mapper = HistoricoDePrecoMapper.INSTANCE;
		
		return produto.getPrecos().stream()
				.map(itemHistPreco -> mapper.toDto(itemHistPreco))
				.filter(histPrecoItem -> 
					{ 
						LocalDate dataAlteracao = histPrecoItem.getDataHoraAlteracao().toLocalDate();
						
						boolean isAfterOrEqualDataInicial = dataAlteracao.isAfter(dtInicial) || dataAlteracao.isEqual(dtInicial);
						boolean isBeforeOrEqualDataFinal = dataAlteracao.isBefore(dtFinal) || dataAlteracao.isEqual(dtFinal);
						
						return isAfterOrEqualDataInicial && isBeforeOrEqualDataFinal;		
					})
				.min(comparing(HistoricoDePrecoDTO::getPreco))
				.orElseThrow(() -> new RecursoNotFoundException("Preço mínimo inexistente no período informado"));	
	}
	
	public HistoricoDePrecoDTO buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV1(String codProduto, LocalDate dtInicial, LocalDate dtFinal) {
		Produto produto = repository.findByCodigo(codProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codProduto + " inexistente"));
		
		HistoricoDePrecoMapper mapper = HistoricoDePrecoMapper.INSTANCE;

		return produto.getPrecos().stream()
				.map(itemHistPreco -> mapper.toDto(itemHistPreco))
				.filter(histPrecoItem -> 
					{ 
						LocalDate dataAlteracao = histPrecoItem.getDataHoraAlteracao().toLocalDate();
						
						boolean isAfterOrEqualDataInicial = dataAlteracao.isAfter(dtInicial) || dataAlteracao.isEqual(dtInicial);
						boolean isBeforeOrEqualDataFinal = dataAlteracao.isBefore(dtFinal) || dataAlteracao.isEqual(dtFinal);
						
						return isAfterOrEqualDataInicial && isBeforeOrEqualDataFinal;		
					})
				.max(comparing(HistoricoDePrecoDTO::getPreco))
				.orElseThrow(() -> new RecursoNotFoundException("Preço máximo inexistente no período informado"));	
	}
	
	public HistoricoDePrecoDTO buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV2(String codProduto, LocalDate dtInicial, LocalDate dtFinal) {
		Produto produto = repository.findByCodigo(codProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codProduto + " inexistente"));
		
		return historicoService.buscarPrecoMinimoDadoProdutoNoPeriodo(produto, dtInicial, dtFinal);
	}
	
	public HistoricoDePrecoDTO buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV2(String codProduto, LocalDate dtInicial, LocalDate dtFinal) {
		Produto produto = repository.findByCodigo(codProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codProduto + " inexistente"));
		
		return historicoService.buscarPrecoMaximoDadoProdutoNoPeriodo(produto, dtInicial, dtFinal);	
	}	
	
	public Page<AvaliacaoDTO> listarAvaliacoesDadoCodigoDoProduto(String codigoProduto, Pageable pageable) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codigoProduto + " inexistente"));
		
		return avaliacaoService.listarAvaliacoesDadoProduto(produto, pageable);
	}
	
	@Transactional(propagation = REQUIRED, readOnly = false)
	public AvaliacaoDTO avaliarProduto(String codProduto, AvaliacaoDTO dtoEntrada) {
		Produto produto = repository.findByCodigo(codProduto)
				.orElseThrow(() -> new RecursoNotFoundException("Produto " + codProduto + " inexistente"));
		
		produto.getAvaliacoes().stream()
			.filter(avaliacao -> avaliacao.getUsuario().equalsIgnoreCase(dtoEntrada.getUsuario()))
			.findFirst()
				.ifPresent(avaliacao -> {
					String mensagem = "Avaliação já existente para o usuário " + dtoEntrada.getUsuario();
					throw new RecursoAlreadyExistsException(mensagem, codProduto, "/v1/avaliacoes"); 
				});
	
		AvaliacaoMapper mapper = AvaliacaoMapper.INSTANCE;
		Avaliacao avaliacao = mapper.toEntity(dtoEntrada);	

		avaliacao.setCodigo(geraCodigoEmBase64(avaliacao.getUsuario(), LocalDateTime.now()));
		avaliacao.setComentario(null);
		avaliacao.setUsuario(avaliacao.getUsuario());
		avaliacao.setProduto(produto);
		avaliacao.setRating(dtoEntrada.getRating());

		produto.addAvaliacao(avaliacao);		
		repository.save(produto);
		
		return mapper.toDto(avaliacao);
	}
	
	private ProdutoDTO historizarPreco(Produto produto) {
		produto.addHistoricoDePreco(getHistoricoDePreco(produto));
		
		ProdutoDTO dtoProduto =  mapper.toDto(produto);
		atualizarProduto(produto.getCodigo(), dtoProduto);
		
		return dtoProduto;
	}
	
	private HistoricoDePreco getHistoricoDePreco(Produto produto) {
		HistoricoDePreco historico = new HistoricoDePreco();
		
		historico.setProduto(produto);
		historico.setDataHoraAlteracao(LocalDateTime.now());
		historico.setPreco(produto.getPreco());		

		return historico;
	}
	
	private String geraCodigoEmBase64(String usuario, LocalDateTime dataHoraAtual) {
		String identificador = usuario + dataHoraAtual;
		return new String(getEncoder().encode(identificador.getBytes()));
	}

}

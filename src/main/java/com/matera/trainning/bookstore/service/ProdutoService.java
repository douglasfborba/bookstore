package com.matera.trainning.bookstore.service;

import static java.util.Base64.getEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.domain.Avaliacao;
import com.matera.trainning.bookstore.domain.Comentario;
import com.matera.trainning.bookstore.domain.HistoricoDePreco;
import com.matera.trainning.bookstore.domain.Produto;
import com.matera.trainning.bookstore.exception.RecursoNotFoundException;
import com.matera.trainning.bookstore.exception.ResourceAlreadyExistsException;
import com.matera.trainning.bookstore.respository.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProdutoRepository repository;
	
	@Autowired
	private ComentarioService comentarioService;
	
	@Autowired
	private HistoricoDePrecoService historicoService;
	
	@Autowired
	private AvaliacaoService avaliacaoService;
			
	@PostConstruct
	public void configuraMapper() {
		modelMapper.addConverter(ProdutoDTO.getConverter());
		modelMapper.addConverter(AvaliacaoDTO.getConverter());
	}
	
	public ProdutoDTO inserirProduto(ProdutoDTO dtoProduto)  {	
		repository.findByCodigo(dtoProduto.getCodigo())
				.ifPresent(produto -> {
					throw new ResourceAlreadyExistsException(produto.getCodigo()); 
				});
				
		Produto produto = modelMapper.map(dtoProduto, Produto.class);
		if (produto.getDataCadastro() == null)
			produto.setDataCadastro(LocalDate.now());
				
		Produto produtoSalvo = repository.save(produto);
		ProdutoDTO dto = historizarPreco(produtoSalvo);
		
		return dto;
	}
		
	public void atualizarProduto(String codigoProduto, ProdutoDTO dtoProduto) {		
		Produto produtoSalvo = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
		
		Produto produto = modelMapper.map(dtoProduto, Produto.class);			
		produto.setId(produtoSalvo.getId());
		produto.setDataCadastro(produtoSalvo.getDataCadastro());
		produto.setComentarios(produtoSalvo.getComentarios());
		produto.setPrecos(produtoSalvo.getPrecos());
		
		if (produtoSalvo.getPreco().compareTo(dtoProduto.getPreco()) != 0)
			produto.addHistoricoDePreco(getHistoricoDePreco(produto));
		
		repository.save(produto);
	}
	
	@Transactional
	public void removerProduto(String codigoProduto) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));

		repository.delete(produto);
	}

	public ProdutoDTO buscarProdutoDadoCodigo(String codigoProduto) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));

		return modelMapper.map(produto, ProdutoDTO.class);
	}

	public Page<ProdutoDTO> buscarProdutoDadoDescricao(String descricao, Pageable pageable) {
		return repository.findByDescricao(descricao, pageable)
				.map(produto -> modelMapper.map(produto, ProdutoDTO.class));
	}

	public Page<ProdutoDTO> listarProdutos(Pageable pageable) {
		return repository.findAll(pageable)
				.map(produto -> modelMapper.map(produto, ProdutoDTO.class));
	}
	
	public Page<ComentarioDTO> listarComentariosDadoCodigoProduto(String codigoProduto, Pageable pageable) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));

		return comentarioService.findAllByProduto(produto, pageable);
	}	
	
	public ComentarioDTO buscarComentarioDadoCodigoDoProdutoAndCodigoComentario(String codigoProduto, String codigoComentario) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
				
		return produto.getComentarios().stream()
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class))		
				.filter(dto -> dto.getCodigo().equals(codigoComentario))
				.findFirst()
					.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
	}
	
	public ComentarioDTO inserirComentario(String codigoProduto, ComentarioDTO dtoComentario) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
		
		Comentario comentario = modelMapper.map(dtoComentario, Comentario.class);	
		LocalDateTime dataHoraAtual = LocalDateTime.now();

		comentario.setCodigo(geraCodigoEmBase64(comentario, dataHoraAtual));
		comentario.setDataHoraCriacao(dataHoraAtual);
		comentario.setProduto(produto);
		
		produto.addComentario(comentario);
		repository.save(produto);
		
		return modelMapper.map(comentario, ComentarioDTO.class);
	}
	
	public void atualizarComentario(String codigoProduto, String codigoComentario, ComentarioDTO dtoComentario) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
		
		Comentario comentario = produto.getComentarios().stream()
				.filter(dto -> dto.getCodigo().equals(codigoComentario))
				.findFirst()
					.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
		
		comentario.setUsuario(comentario.getUsuario());
		comentario.setDescricao(dtoComentario.getDescricao());
				
		repository.save(produto);
	}
	
	public void removerComentario(String codigoProduto, String codigoComentario) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
		
		Comentario comentario = produto.getComentarios().stream()
			.filter(dto -> dto.getCodigo().equals(codigoComentario))
			.findFirst()
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
		
		produto.removeComentario(comentario);
		
		repository.save(produto);
	}
	
	public Page<HistoricoDePrecoDTO> listarHistoricoDePrecos(String codigoProduto, Pageable pageable) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
		
		return historicoService.findAllByProduto(produto, pageable);
	}
	
	public Page<HistoricoDePrecoDTO> buscarHistoricoDePrecosNoPeriodo(String codigoProduto, LocalDate dataInicial, LocalDate dataFinal, Pageable pageable) {		
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
		
		List<HistoricoDePrecoDTO> precoes = produto.getPrecos().stream()
				.map(preco -> modelMapper.map(preco, HistoricoDePrecoDTO.class))
				.filter(preco -> 
					{ 
						LocalDate dataAlteracao = preco.getDataHoraAlteracao().toLocalDate();
						
						boolean isAfterOrEqualDataInicial = dataAlteracao.isAfter(dataInicial) || dataAlteracao.isEqual(dataInicial);
						boolean isBeforeOrEqualDataFinal = dataAlteracao.isBefore(dataFinal) || dataAlteracao.isEqual(dataFinal);
						
						return isAfterOrEqualDataInicial && isBeforeOrEqualDataFinal;		
					})
				.collect(Collectors.toList());
	
		return new PageImpl<HistoricoDePrecoDTO>(precoes, pageable, precoes.size());
	}
//	
//	public HistoricoDePrecoDTO buscarPrecoMaximoDadoCodigoProduto(String codProduto) {
//		Produto produto = repository.findByCodigo(codProduto)
//				.orElseThrow(() -> new RecursoNotFoundException(codProduto));
//		
//		return historicoService.findAllByProduto(produto, n);
//	}	
//	
	public Page<AvaliacaoDTO> listarAvaliacoesDadoCodigoDoProduto(String codigoProduto, Pageable pageable) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
		
		return avaliacaoService.findAllByProduto(produto, pageable);
	}
	
	public Page<AvaliacaoDTO> listarAvaliacoesDadoProdutoAndComentario(String codigoProduto, String codigoComentario, Pageable pageable) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
				
		Comentario comentario = produto.getComentarios().stream()
				.filter(dto -> dto.getCodigo().equals(codigoComentario))
				.findFirst()
					.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
		
		return avaliacaoService.findAllByComentario(comentario, pageable);
	}
	
	public AvaliacaoDTO avaliarProduto(String codigoProduto, AvaliacaoDTO dtoAvaliacao) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
		
		Avaliacao avaliacao = modelMapper.map(dtoAvaliacao, Avaliacao.class);	

		avaliacao.setCodigo(geraCodigoEmBase64(avaliacao, LocalDateTime.now()));
		avaliacao.setComentario(null);
		avaliacao.setUsuario(avaliacao.getUsuario());
		avaliacao.setProduto(produto);
		avaliacao.setRating(dtoAvaliacao.getRating());

		produto.addAvaliacao(avaliacao);		
		repository.save(produto);
		
		return modelMapper.map(avaliacao, AvaliacaoDTO.class);
	}
	
	public AvaliacaoDTO avaliarComentario(String codigoProduto, String codigoComentario, AvaliacaoDTO dtoAvaliacao) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
		
		Comentario comentario = produto.getComentarios().stream()
			.filter(dto -> dto.getCodigo().equals(codigoComentario))
			.findFirst()
				.orElseThrow(() -> new RecursoNotFoundException(codigoProduto));
		
		Avaliacao avaliacao = modelMapper.map(dtoAvaliacao, Avaliacao.class);	

		avaliacao.setCodigo(geraCodigoEmBase64(avaliacao, LocalDateTime.now()));
		avaliacao.setComentario(comentario);
		avaliacao.setUsuario(avaliacao.getUsuario());
		avaliacao.setProduto(null);
		avaliacao.setRating(dtoAvaliacao.getRating());

		produto.addAvaliacao(avaliacao);		
		repository.save(produto);
		
		return modelMapper.map(avaliacao, AvaliacaoDTO.class);
	}	
		
	private ProdutoDTO historizarPreco(Produto produtoSalvo) {
		produtoSalvo.addHistoricoDePreco(getHistoricoDePreco(produtoSalvo));
		
		ProdutoDTO dto =  modelMapper.map(produtoSalvo, ProdutoDTO.class);
		atualizarProduto(produtoSalvo.getCodigo(), dto);
		
		return dto;
	}
	
	private HistoricoDePreco getHistoricoDePreco(Produto produto) {
		HistoricoDePreco historico = new HistoricoDePreco();
		
		historico.setProduto(produto);
		historico.setDataHoraAlteracao(LocalDateTime.now());
		historico.setPreco(produto.getPreco());		

		return historico;
	}
	
	private String geraCodigoEmBase64(Comentario comentario, LocalDateTime dataHoraAtual) {
		String identificador = comentario.getUsuario() + dataHoraAtual;
		return new String(getEncoder().encode(identificador.getBytes()));
	}
	
	private String geraCodigoEmBase64(Avaliacao avaliacao, LocalDateTime dataHoraAtual) {
		String identificador = avaliacao.getUsuario() + dataHoraAtual;
		return new String(getEncoder().encode(identificador.getBytes()));
	}

}

package com.matera.trainning.bookstore.service;

import static java.util.Base64.getEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
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
	
	public ProdutoDTO inserir(ProdutoDTO dtoProduto)  {	
		repository.findByCodigo(dtoProduto.getCodigo())
				.ifPresent(produto -> {
					throw new ResourceAlreadyExistsException(); 
				});
				
		Produto produto = modelMapper.map(dtoProduto, Produto.class);
		if (produto.getDataCadastro() == null)
			produto.setDataCadastro(LocalDate.now());
				
		produto.addHistoricoDePreco(getHistoricoDePreco(produto));
		Produto produtoSalvo = repository.save(produto);
			
		return modelMapper.map(produtoSalvo, ProdutoDTO.class);	
	}
		
	private HistoricoDePreco getHistoricoDePreco(Produto produto) {
		HistoricoDePreco historico = new HistoricoDePreco();
		
		historico.setProduto(produto);
		historico.setDataHoraAlteracao(LocalDateTime.now());
		historico.setPreco(produto.getPreco());		

		return historico;
	}

	public void atualizar(String codigoProduto, ProdutoDTO dtoProduto) {		
		Produto produtoSalvo = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException());
		
		Produto produto = modelMapper.map(dtoProduto, Produto.class);			
		produto.setId(produtoSalvo.getId());
		produto.setDataCadastro(produtoSalvo.getDataCadastro());
		produto.setComentarios(produtoSalvo.getComentarios());
		produto.setPrecos(produtoSalvo.getPrecos());
		
		if (produtoSalvo.getPreco().compareTo(dtoProduto.getPreco()) != 0)
			produto.addHistoricoDePreco(getHistoricoDePreco(produto));
		
		repository.save(produto);
	}
	
	public void remover(String codigoProduto) {
		repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException());

		repository.deleteByCodigo(codigoProduto);
	}

	public ProdutoDTO buscarDadoCodigoDoProduto(String codigoProduto) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException());

		return modelMapper.map(produto, ProdutoDTO.class);
	}

	public Collection<ProdutoDTO> buscarProdutosDadoDescricao(String descricao) {
		return repository.findByDescricao(descricao).stream()
				.map(produto -> modelMapper.map(produto, ProdutoDTO.class))
				.collect(Collectors.toList());
	}

	public Collection<ProdutoDTO> listarTodosOsProdutos() {
		return repository.findAll().stream()
				.map(produto -> modelMapper.map(produto, ProdutoDTO.class))
				.collect(Collectors.toList());
	}
	
	public Collection<ComentarioDTO> listarComentariosDadoCodigoDoProduto(String codigoProduto) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException());

		return produto.getComentarios().stream()
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class))
				.collect(Collectors.toList());
	}
	
	public ComentarioDTO buscarComentarioDadoCodigoDoProdutoAndCodigoComentario(String codigoProduto, String codigoComentario) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException());
				
		return produto.getComentarios().stream()
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class))		
				.filter(dto -> dto.getCodigo().equals(codigoComentario))
				.findFirst()
					.orElseThrow(() -> new RecursoNotFoundException());
	}
	
	public ComentarioDTO inserirNovoComentario(String codigoProduto, ComentarioDTO dtoComentario) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException());
		
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
				.orElseThrow(() -> new RecursoNotFoundException());
		
		Comentario comentario = produto.getComentarios().stream()
				.filter(dto -> dto.getCodigo().equals(codigoComentario))
				.findFirst()
					.orElseThrow(() -> new RecursoNotFoundException());
		
		comentario.setUsuario(comentario.getUsuario());
		comentario.setDescricao(dtoComentario.getDescricao());
				
		repository.save(produto);
	}
	
	public void removerComentario(String codigoProduto, String codigoComentario) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException());
		
		Comentario comentario = produto.getComentarios().stream()
			.filter(dto -> dto.getCodigo().equals(codigoComentario))
			.findFirst()
				.orElseThrow(() -> new RecursoNotFoundException());
		
		produto.removeComentario(comentario);
		
		repository.save(produto);
	}
	
	public Collection<HistoricoDePrecoDTO> listarPrecosDadoCodigoDoProduto(String codigoProduto) {
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException());
		
		return produto.getPrecos().stream()
				.map(preco -> modelMapper.map(preco, HistoricoDePrecoDTO.class))
				.collect(Collectors.toList());		
	}
	
	private String geraCodigoEmBase64(Comentario comentario, LocalDateTime dataHoraAtual) {
		String identificador = comentario.getUsuario() + dataHoraAtual;
		return new String(getEncoder().encode(identificador.getBytes()));
	}

	public Collection<HistoricoDePrecoDTO> listasPrecosEntreDataInicialAndDataFinal(String codigoProduto, LocalDate dataInicial, LocalDate dataFinal) {		
		Produto produto = repository.findByCodigo(codigoProduto)
				.orElseThrow(() -> new RecursoNotFoundException());
		
		return produto.getPrecos().stream()
				.map(preco -> modelMapper.map(preco, HistoricoDePrecoDTO.class))
				.filter(preco -> 
					{ 
						LocalDate dataAlteracao = preco.getDataHoraAlteracao().toLocalDate();
						
						boolean isAfterOrEqualDataInicial = dataAlteracao.isAfter(dataInicial) || dataAlteracao.isEqual(dataInicial);
						boolean isBeforeOrEqualDataFinal = dataAlteracao.isBefore(dataFinal) || dataAlteracao.isEqual(dataFinal);
						
						return isAfterOrEqualDataInicial && isBeforeOrEqualDataFinal;		
					})
				.collect(Collectors.toList());
	}

}

package com.matera.trainning.bookstore.service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.ComentarioRepository;
import com.matera.trainning.bookstore.service.exceptions.RegistroAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@Service
public class ComentarioService {

	@Autowired
	private ComentarioRepository repository;
	
	@Autowired
	private ProdutoService produtoService;

	public Comentario insert(String codigoProduto, Comentario comentario) throws RegistroAlreadyExistsException, RegistroNotFoundException {	
		Produto produto = produtoService.findByCodigo(codigoProduto);
		
		comentario.setProduto(produto);
		comentario.setDataHoraCriacao(LocalDateTime.now());

		String codigoBase64 = geraCodigoEmBase64(comentario, comentario.getDataHoraCriacao());
		comentario.setCodigo(codigoBase64);
		
		return repository.save(comentario);
	}

	public void update(String codigoProduto, String codigoComentario, Comentario comentario) throws RegistroNotFoundException {
		Optional<Comentario> opcional = repository.findByProdutoCodigoAndCodigo(codigoProduto, codigoComentario);
		
		if (!opcional.isPresent())
			throw new RegistroNotFoundException("Comentário inexistente para o produto informado");

		Comentario comentarioSalvo = opcional.get();
		comentarioSalvo.setDescricao(comentario.getDescricao());
		comentarioSalvo.setUsuario(comentario.getUsuario());

		repository.save(comentarioSalvo);
	}

	public void delete(String codigoProduto, String codigoComentario) throws RegistroNotFoundException {
		Optional<Comentario> opcional = repository.findByProdutoCodigoAndCodigo(codigoProduto, codigoComentario);

		if (!opcional.isPresent())
			throw new RegistroNotFoundException("Comentário inexistente para o produto informado");

		repository.deleteByCodigo(codigoComentario);
	}

	public Comentario findByCodigo(String codigo) throws RegistroNotFoundException {
		Optional<Comentario> opcional = repository.findByCodigo(codigo);

		if (!opcional.isPresent())
			throw new RegistroNotFoundException("Comentário inexistente");

		return opcional.get();
	}

	public Comentario findByProdutoCodigoAndCodigo(String codigoProduto, String codigoComentario) throws RegistroNotFoundException {
		Optional<Comentario> opcional = repository.findByProdutoCodigoAndCodigo(codigoProduto, codigoComentario);
		
		if (!opcional.isPresent())
			throw new RegistroNotFoundException("Comentário inexistente para o produto informado");
		
		return opcional.get();
	}

	public List<Comentario> findByProdutoCodigoAndDescricao(String codigoProduto, String descricao) {
		return repository.findByProdutoCodigoAndDescricao(codigoProduto, descricao);
	}

	public List<Comentario> findByProdutoCodigo(String codigoProduto) {
		return repository.findByProdutoCodigo(codigoProduto);
	}

	public List<Comentario> findAll() {
		return repository.findAll();
	}

	private String geraCodigoEmBase64(Comentario comentario, LocalDateTime dataHoraAtual) {
		String identificador = comentario.getUsuario() + dataHoraAtual;
		return new String(Base64.getEncoder().encode(identificador.getBytes()));
	}

}

package com.matera.trainning.bookstore.service;

import static java.util.Base64.getEncoder;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.model.Avaliacao;
import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.ComentarioRepository;
import com.matera.trainning.bookstore.service.exception.RecursoAlreadyExistsException;
import com.matera.trainning.bookstore.service.exception.RecursoNotFoundException;

@Service
@Transactional(propagation = SUPPORTS, readOnly = true)
public class ComentarioService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ComentarioRepository repository;
	
	@Autowired
	private AvaliacaoService avaliacaoService;
	
	@Transactional(propagation = REQUIRED, readOnly = false)
	public void atualizarComentario(String codComentario, ComentarioDTO dtoEntrada) {
		Comentario comentario = repository.findByCodigo(codComentario)
				.orElseThrow(() -> new RecursoNotFoundException("Comentário " + codComentario + " inexistente"));

		comentario.setUsuario(dtoEntrada.getUsuario());
		comentario.setDescricao(dtoEntrada.getDescricao());

		repository.save(comentario);
	}

	@Transactional(propagation = REQUIRED, readOnly = false)
	public void removerComentario(String codComentario) {
		Comentario comentario = repository.findByCodigo(codComentario)
				.orElseThrow(() -> new RecursoNotFoundException("Comentário " + codComentario + " inexistente"));

		repository.delete(comentario);
	}

	public ComentarioDTO buscarComentarioDadoCodigo(String codComentario) {
		Comentario comentario = repository.findByCodigo(codComentario)
				.orElseThrow(() -> new RecursoNotFoundException("Comentário " + codComentario + " inexistente"));
		
		return modelMapper.map(comentario, ComentarioDTO.class);
	}

	public Page<ComentarioDTO> listarComentariosDadoProduto(Produto produto, Pageable pageable) {
		return repository.findAllByProduto(produto, pageable)
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class));
	}

	public Page<ComentarioDTO> listarComentarios(Pageable pageable) {
		return repository.findAll(pageable).map(comentario -> modelMapper.map(comentario, ComentarioDTO.class));
	}

	public Page<ComentarioDTO> listarComentariosDadoUsuario(String usuComentario, Pageable pageable) {
		Page<ComentarioDTO> comentarios =  repository.findAllByUsuario(usuComentario, pageable)
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class));
		
		if (comentarios.isEmpty())
			throw new RecursoNotFoundException("Usuário " + usuComentario + " inexistente"); 
		
		return comentarios;
	}
		
	public Page<AvaliacaoDTO> listarAvaliacoesDadoCodigoComentario(String codComentario, Pageable pageable) {				
		Comentario comentario = repository.findByCodigo(codComentario)
				.orElseThrow(() -> new RecursoNotFoundException("Comentário " + codComentario + " inexistente"));

		return avaliacaoService.listarAvaliacoesDadoComentario(comentario, pageable);
	}
	
	@Transactional(propagation = REQUIRED, readOnly = false)
	public AvaliacaoDTO avaliarComentario(String codComentario, AvaliacaoDTO dtoEntrada) {		
		Comentario comentario = repository.findByCodigo(codComentario)
				.orElseThrow(() -> new RecursoNotFoundException("Comentário " + codComentario + " inexistente"));
		
		comentario.getAvaliacoes().stream()
			.filter(avaliacao -> avaliacao.getUsuario().equalsIgnoreCase(dtoEntrada.getUsuario()))
			.findFirst()
				.ifPresent(avaliacao -> {
					String mensagem = "Avaliação já existente para o usuário " + dtoEntrada.getUsuario();
					throw new RecursoAlreadyExistsException(mensagem, avaliacao.getCodigo(), "/v1/avaliacoes");
				});
		
		Avaliacao avaliacao = modelMapper.map(dtoEntrada, Avaliacao.class);	

		avaliacao.setCodigo(geraCodigoEmBase64(avaliacao.getUsuario(), LocalDateTime.now()));
		avaliacao.setComentario(comentario);
		avaliacao.setUsuario(avaliacao.getUsuario());
		avaliacao.setProduto(null);
		avaliacao.setRating(dtoEntrada.getRating());

		comentario.addAvaliacao(avaliacao);		
		repository.save(comentario);
		
		return modelMapper.map(avaliacao, AvaliacaoDTO.class);
	}
	
	private String geraCodigoEmBase64(String usuario, LocalDateTime dataHoraAtual) {
		String identificador = usuario + dataHoraAtual;
		return new String(getEncoder().encode(identificador.getBytes()));
	}

}

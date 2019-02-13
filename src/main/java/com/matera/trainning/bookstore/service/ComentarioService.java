package com.matera.trainning.bookstore.service;

import static java.util.Base64.getEncoder;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.domain.Avaliacao;
import com.matera.trainning.bookstore.domain.Comentario;
import com.matera.trainning.bookstore.domain.Produto;
import com.matera.trainning.bookstore.exception.RecursoAlreadyExistsException;
import com.matera.trainning.bookstore.exception.RecursoNotFoundException;
import com.matera.trainning.bookstore.respository.ComentarioRepository;

@Service
public class ComentarioService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ComentarioRepository repository;
	
	@Autowired
	private AvaliacaoService avalicaoService;
	
	@PostConstruct
	public void configuraMapper() {
		modelMapper.addConverter(AvaliacaoDTO.getConverter());
		modelMapper.addConverter(ComentarioDTO.getConverter());
	}
	
	public void atualizarComentario(String codComentario, ComentarioDTO dtoEntrada) {
		Comentario comentario = repository.findByCodigo(codComentario)
				.orElseThrow(() -> new RecursoNotFoundException(codComentario));

		comentario.setUsuario(dtoEntrada.getUsuario());
		comentario.setDescricao(dtoEntrada.getDescricao());

		repository.save(comentario);
	}

	public void removerComentario(String codComentario) {
		Comentario comentario = repository.findByCodigo(codComentario)
				.orElseThrow(() -> new RecursoNotFoundException(codComentario));

		repository.delete(comentario);
	}

	public ComentarioDTO buscarDadoCodigo(String codComentario) {
		Comentario comentario = repository.findByCodigo(codComentario)
				.orElseThrow(() -> new RecursoNotFoundException(codComentario));
		
		return modelMapper.map(comentario, ComentarioDTO.class);
	}

	public Page<ComentarioDTO> findAllByProduto(Produto produto, Pageable pageable) {
		return repository.findAllByProduto(produto, pageable)
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class));
	}

	public Page<ComentarioDTO> listarTodos(Pageable pageable) {
		return repository.findAll(pageable).map(comentario -> modelMapper.map(comentario, ComentarioDTO.class));
	}

	public Page<ComentarioDTO> buscarComentarioDadoUsuario(String usuComentario, Pageable pageable) {
		return repository.findAllByUsuario(usuComentario, pageable)
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class));
	}
		
	public Page<AvaliacaoDTO> listarAvaliacoesDadoComentario(String codComentario, Pageable pageable) {				
		Comentario comentario = repository.findByCodigo(codComentario)
				.orElseThrow(() -> new RecursoNotFoundException(codComentario));

		return avalicaoService.findAllByComentario(comentario, pageable);
	}
	
	public AvaliacaoDTO avaliarComentario(String codComentario, AvaliacaoDTO dtoEntrada) {		
		Comentario comentario = repository.findByCodigo(codComentario)
				.orElseThrow(() -> new RecursoNotFoundException(codComentario));
		
		comentario.getAvaliacoes().stream()
			.filter(avaliacao -> avaliacao.getUsuario().equalsIgnoreCase(dtoEntrada.getUsuario()))
			.findFirst()
				.ifPresent(avaliacao -> {
					throw new RecursoAlreadyExistsException("Comentário já avalido pelo usuário " + avaliacao.getUsuario());
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

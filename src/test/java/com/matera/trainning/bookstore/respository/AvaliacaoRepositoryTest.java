package com.matera.trainning.bookstore.respository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.matera.trainning.bookstore.model.Avaliacao;
import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.Produto;

@DataJpaTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class AvaliacaoRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private AvaliacaoRepository repository;

	private Produto produto;

	private Comentario comentario;

	private Avaliacao avaliacao;

	@Before
	public void setUp() {
		produto = new Produto();
		produto.setCodigo("LIVRO23040");
		produto.setDescricao("Livro The Hobbit");
		produto.setPreco(new BigDecimal(57.63));
		produto.setDataCadastro(LocalDate.now());

		comentario = new Comentario();
		comentario.setCodigo("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1");
		comentario.setProduto(produto);
		comentario.setUsuario("usuario.hater");
		comentario.setDescricao("Odiei, este é o pior livro do mundo!");
		comentario.setDataHoraCriacao(LocalDateTime.now());
		
		avaliacao = new Avaliacao();
		avaliacao.setCodigo("1YXJpby5oYXRlco124qWErTEyMDE5MTckQtz");
		avaliacao.setRating(3.5);
		avaliacao.setUsuario("usuario.teste");
	}
	
	@Test
	public void buscaAvaliacaoPeloCodigo() {
		avaliacao.setProduto(produto);
		produto.addAvaliacao(avaliacao);
		
		entityManager.persist(produto);

		Optional<Avaliacao> opcional = repository.findByCodigo("1YXJpby5oYXRlco124qWErTEyMDE5MTckQtz");

		assertThat(opcional.isPresent()).isTrue();
		assertThat(opcional.get()).isNotNull().isEqualTo(avaliacao);
	}

	@Test
	public void buscaComentarioPeloCodigoInvalido() {
		avaliacao.setProduto(produto);
		produto.addAvaliacao(avaliacao);
		
		entityManager.persist(produto);

		Optional<Avaliacao> opcional = repository.findByCodigo("1YXJpby5oYXRlco124qWErTEyMDE5MTckQte");

		assertThat(opcional.isPresent()).isFalse();
	}

	@Test
	public void listaAvaliacoesPeloUsuario() {
		avaliacao.setProduto(produto);
		produto.addAvaliacao(avaliacao);
		
		entityManager.persist(produto);

		List<Avaliacao> avaliacoes = repository.findAllByUsuario("usuario.teste", PageRequest.of(0, 1)).getContent();

		assertThat(avaliacoes).isNotEmpty().hasSize(1).contains(avaliacao);
	}

	@Test
	public void listaAvaliacoesPeloUsuarioInexistente() {
		avaliacao.setProduto(produto);
		produto.addAvaliacao(avaliacao);
		
		entityManager.persist(produto);

		List<Avaliacao> avaliacoes = repository.findAllByUsuario("usuario.hater", PageRequest.of(0, 1)).getContent();

		assertThat(avaliacoes).hasSize(0);
	}

	@Test
	public void buscaAvaliacoesPeloComentario() throws Exception {
		produto.addComentario(comentario);
		avaliacao.setComentario(comentario);
		comentario.addAvaliacao(avaliacao);
		
		entityManager.persist(produto);

		List<Avaliacao> avaliacoes = repository.findAllByComentario(comentario, PageRequest.of(0, 1)).getContent();

		assertThat(avaliacoes).isNotEmpty().hasSize(1).contains(avaliacao);
	}

	@Test
	public void buscaAvaliacoesPeloComentarioInexistente() throws Exception {
		produto.addComentario(comentario);
		avaliacao.setProduto(produto);
		produto.addAvaliacao(avaliacao);
		
		entityManager.persist(produto);

		List<Avaliacao> avaliacoes = repository.findAllByComentario(comentario, PageRequest.of(0, 1)).getContent();

		assertThat(avaliacoes).hasSize(0);
	}

}

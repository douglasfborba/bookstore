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

import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.Produto;

@DataJpaTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class ComentarioRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ComentarioRepository repository;

	private Produto produto;

	private Comentario comentario;

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
	}

	@Test
	public void buscaComentarioPeloCodigo() {
		produto.addComentario(comentario);
		entityManager.persist(produto);

		Optional<Comentario> opcional = repository.findByCodigo("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1");

		assertThat(opcional.isPresent()).isTrue();
		assertThat(opcional.get()).isNotNull().isEqualTo(comentario);
	}

	@Test
	public void buscaComentarioPeloCodigoInvalido() {
		produto.addComentario(comentario);
		entityManager.persist(produto);

		Optional<Comentario> opcional = repository.findByCodigo("xXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI2");

		assertThat(opcional.isPresent()).isFalse();
	}

	@Test
	public void listaComentariosPeloUsuario() {
		produto.addComentario(comentario);
		entityManager.persist(produto);

		List<Comentario> comentarios = repository.findAllByUsuario("usuario.hater", PageRequest.of(0, 1)).getContent();

		assertThat(comentarios).isNotEmpty().hasSize(1).contains(comentario);
	}

	@Test
	public void listaComentariosPeloUsuarioInexistente() {
		produto.addComentario(comentario);
		entityManager.persist(produto);

		List<Comentario> comentarios = repository.findAllByUsuario("usuario.teste", PageRequest.of(0, 1)).getContent();

		assertThat(comentarios).hasSize(0);
	}

	@Test
	public void buscaComentarioPeloProduto() throws Exception {
		produto.addComentario(comentario);
		entityManager.persist(produto);

		List<Comentario> comentarios = repository.findAllByProduto(produto, PageRequest.of(0, 1)).getContent();

		assertThat(comentarios).isNotEmpty().hasSize(1).contains(comentario);
	}

	@Test
	public void buscaComentarioPeloProdutoInexistente() throws Exception {
		comentario.setProduto(null);
		entityManager.persist(produto);

		List<Comentario> comentarios = repository.findAllByProduto(produto, PageRequest.of(0, 1)).getContent();

		assertThat(comentarios).hasSize(0);
	}

}

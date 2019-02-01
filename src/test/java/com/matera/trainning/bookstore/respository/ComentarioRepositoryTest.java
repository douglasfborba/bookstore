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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.matera.trainning.bookstore.domain.Comentario;
import com.matera.trainning.bookstore.domain.Produto;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ComentarioRepositoryTest {

	private static final String COD_USU_HATER = "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1";

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ComentarioRepository repository;
	
	private Comentario hater;
	private Comentario lover;

	@Before
	public void setUp() {
		Produto livroTheHobbit = new Produto("LIVRO23040", "Livro The Hobbit", new BigDecimal(57.63), LocalDate.now());

		hater = new Comentario("Odiei, este é o pior livro do mundo!", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1",
				"usuario.hater", LocalDateTime.now(), livroTheHobbit);

		lover = new Comentario("Amei, melhor livro do mundo :D", "dXN1YXJpby5sb3ZlcjMwMDEyMDE5MTcyNTAw",
				"usuario.lover", LocalDateTime.now(), livroTheHobbit);
	}

	@Test
	public void listaComentariosEmBancoPopulado() {
		entityManager.persist(hater);
		entityManager.persist(lover);

		List<Comentario> comentarios = repository.findAll();
		assertThat(comentarios).hasSize(2).contains(hater, lover);
	}

	@Test
	public void listaComentariosEmBancoVazio() {
		List<Comentario> comentarios = repository.findAll();
		assertThat(comentarios).isEmpty();
	}

	@Test
	public void buscaComentarioPeloCodigo() {
		entityManager.persist(hater);

		Optional<Comentario> opcional = repository.findByCodigo(COD_USU_HATER);
		assertThat(opcional.get()).isNotNull().isEqualTo(hater);
	}

	@Test
	public void buscaComentarioInexistentePeloCodigo() {
		Optional<Comentario> opcional = repository.findByCodigo(COD_USU_HATER);
		assertThat(opcional.isPresent()).isFalse();
	}
	
	@Test
	public void buscaComentarioPeloCodigoDoProduto() throws Exception {
		entityManager.persist(hater);

		Optional<Comentario> opcional = repository.findByProdutoCodigoAndCodigo("LIVRO23040", COD_USU_HATER);
		assertThat(opcional.get()).isNotNull().isEqualTo(hater);
	}

	@Test
	public void buscaComentarioInexistentePeloProdutoCodigoAndCodigo() throws Exception {
		Optional<Comentario> opcional = repository.findByProdutoCodigoAndCodigo("LIVRO23040", COD_USU_HATER);
		assertThat(opcional.isPresent()).isFalse();
	}

	@Test
	public void buscaComentarioPeloProdutoCodigoAndDescricao() {
		entityManager.persist(hater);
		entityManager.persist(lover);

		List<Comentario> comentarios = repository.findByProdutoCodigoAndDescricao("LIVRO23040", "LiVrO");
		assertThat(comentarios).hasSize(2).contains(hater);
	}

	@Test
	public void buscaComentarioInexistentePeloProdutoCodigoAndDescricao() {
		List<Comentario> comentarios = repository.findByProdutoCodigoAndDescricao("LIVRO23040", "LiVrO");
		assertThat(comentarios).isEmpty();
	}

	@Test
	public void persisteComentario() {
		repository.save(hater);

		List<Comentario> comentarios = repository.findAll();
		assertThat(comentarios).hasSize(1).contains(hater);
	}

	@Test
	public void atualizaComentario() {
		entityManager.persist(hater);

		Optional<Comentario> opcional = repository.findByCodigo(hater.getCodigo());
		Comentario comentario = opcional.get();

		assertThat(comentario).hasFieldOrPropertyWithValue("descricao", "Odiei, este é o pior livro do mundo!");

		comentario.setDescricao("Pior livro que já li.");

		opcional = repository.findByCodigo(hater.getCodigo());
		comentario = opcional.get();

		assertThat(comentario).hasFieldOrPropertyWithValue("descricao", "Pior livro que já li.");
	}

	@Test
	public void removeComentarioPeloCodigo() {
		entityManager.persist(hater);

		List<Comentario> comentarios = repository.findAll();
		assertThat(comentarios).hasSize(1).contains(hater);

		repository.deleteByCodigo(hater.getCodigo());

		comentarios = repository.findAll();
		assertThat(comentarios).isEmpty();
	}

}

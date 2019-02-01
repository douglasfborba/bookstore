package com.matera.trainning.bookstore.respository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
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

import com.matera.trainning.bookstore.domain.Produto;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ProdutoRepositoryTest {

	private static final String COD_LIVRO_HOBBIT = "LIVRO23040";

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ProdutoRepository repository;

	private Produto livroTheHobbit;
	private Produto livroIt;

	@Before
	public void setUp() {
		livroTheHobbit = new Produto(COD_LIVRO_HOBBIT, "Livro The Hobbit", new BigDecimal(57.63), LocalDate.now());
		livroIt = new Produto("LIVRO34536", "Livro IT - A coisa", new BigDecimal(74.90), LocalDate.now());
	}

	@Test
	public void listaProdutosEmBancoPopulado() {
		entityManager.persist(livroTheHobbit);
		entityManager.persist(livroIt);

		List<Produto> produtos = repository.findAll();
		assertThat(produtos).hasSize(2).contains(livroTheHobbit, livroIt);
	}

	@Test
	public void listaProdutosEmBancoVazio() {
		List<Produto> produtos = repository.findAll();
		assertThat(produtos).isEmpty();
	}

	@Test
	public void buscaProdutoPeloCodigo() {
		entityManager.persist(livroTheHobbit);

		Optional<Produto> opcional = repository.findByCodigo(COD_LIVRO_HOBBIT);
		assertThat(opcional.get()).isNotNull().isEqualTo(livroTheHobbit);
	}

	@Test
	public void buscaProdutoInexistentePeloCodigo() {
		Optional<Produto> opcional = repository.findByCodigo(COD_LIVRO_HOBBIT);
		assertThat(opcional.isPresent()).isFalse();
	}

	@Test
	public void buscaProdutoPelaDescricao() {
		entityManager.persist(livroTheHobbit);
		entityManager.persist(livroIt);

		List<Produto> produtos = repository.findByDescricao("LiVrO");
		assertThat(produtos).hasSize(2).contains(livroTheHobbit, livroIt);
	}

	@Test
	public void buscaProdutoInexistentePelaDescricao() {
		List<Produto> produtos = repository.findByDescricao("LiVrO");
		assertThat(produtos).isEmpty();
	}

	@Test
	public void persisteProduto() {
		repository.save(livroTheHobbit);

		List<Produto> produtos = repository.findAll();
		assertThat(produtos).hasSize(1).contains(livroTheHobbit);
	}

	@Test
	public void atualizaProduto() {
		entityManager.persist(livroTheHobbit);

		Optional<Produto> opcional = repository.findByCodigo(livroTheHobbit.getCodigo());
		Produto produto = opcional.get();

		assertThat(produto).hasFieldOrPropertyWithValue("descricao", "Livro The Hobbit");

		produto.setDescricao("Livro The Hobbit 2");

		opcional = repository.findByCodigo(livroTheHobbit.getCodigo());
		produto = opcional.get();

		assertThat(produto).hasFieldOrPropertyWithValue("descricao", "Livro The Hobbit 2");
	}

	@Test
	public void removeProdutoPeloCodigo() {
		entityManager.persist(livroTheHobbit);

		List<Produto> produtos = repository.findAll();
		assertThat(produtos).hasSize(1).contains(livroTheHobbit);

		repository.deleteByCodigo(livroTheHobbit.getCodigo());

		produtos = repository.findAll();
		assertThat(produtos).isEmpty();
	}

}

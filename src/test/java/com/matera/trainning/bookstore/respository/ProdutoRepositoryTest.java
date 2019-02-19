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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.matera.trainning.bookstore.model.Produto;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ProdutoRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ProdutoRepository repository;

	private Produto produto;

	@Before
	public void setUp() {
		produto = new Produto();
		produto.setCodigo("LIVRO23040");
		produto.setDescricao("Livro The Hobbit");
		produto.setPreco(new BigDecimal(57.63));
		produto.setDataCadastro(LocalDate.now());
	}
	
	@Test
	public void buscaProdutoPeloCodigo() {
		entityManager.persist(produto);

		Optional<Produto> opcional = repository.findByCodigo("LIVRO23040");

		assertThat(opcional.isPresent()).isTrue();
		assertThat(opcional.get()).isNotNull().isEqualTo(produto);
	}

	@Test
	public void buscaProdutoPeloCodigoInvalido() {
		entityManager.persist(produto);

		Optional<Produto> opcional = repository.findByCodigo("LIVRO23041");

		assertThat(opcional.isPresent()).isFalse();
	}
	
	@Test
	public void listaProdutosPelaDescricao() {
		entityManager.persist(produto);

		List<Produto> produtos = repository.findAllByDescricao("HoBbiT", PageRequest.of(0, 1)).getContent();

		assertThat(produtos).isNotEmpty().hasSize(1).contains(produto);
	}

	@Test
	public void listaProdutosPelaDescricaoInexistente() {
		entityManager.persist(produto);

		List<Produto> produtos = repository.findAllByDescricao("Senhor", PageRequest.of(0, 1)).getContent();

		assertThat(produtos).hasSize(0);
	}

}

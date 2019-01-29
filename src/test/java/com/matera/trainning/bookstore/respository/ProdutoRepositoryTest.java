package com.matera.trainning.bookstore.respository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.matera.trainning.bookstore.model.Produto;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ProdutoRepositoryTest {

	@Autowired
	private ProdutoRepository repository;

	private Produto livroTheHobbit;
	private Produto livroIt;

	@Before
	public void setUp() {
		livroTheHobbit = new Produto("LIVRO23040", "Livro The Hobbit", new BigDecimal(57.63), LocalDate.of(2019, 01, 29));
		livroIt = new Produto("LIVRO34536", "Livro IT - A coisa", new BigDecimal(74.90), LocalDate.of(2019, 01, 29));
	}

	@Test
	public void naoDeveListarProdutosEmBancoVazio() {
		List<Produto> produtos = repository.findAll();
		assertThat(produtos).isEmpty();
	}

	@Test
	public void deveListarTodosOsProdutosNoBanco() {
		repository.save(livroTheHobbit);
		repository.save(livroIt);

		List<Produto> produtos = repository.findAll();
		assertThat(produtos).hasSize(2).contains(livroTheHobbit, livroIt);
	}

	@Test
	public void deveSalvarProduto() {
		List<Produto> produtos = repository.findAll();
		assertThat(produtos).isEmpty();

		repository.save(livroTheHobbit);

		produtos = repository.findAll();
		assertThat(produtos).hasSize(1).contains(livroTheHobbit);
	}

	@Test
	public void naoDeveBuscarProdutoPeloCodigoEmBancoVazio() {
		Produto produto = repository.findByCodigo(livroTheHobbit.getCodigo());
		assertThat(produto).isNull();
	}

	@Test
	public void deveBuscarProdutoPeloCodigo() {
		repository.save(livroTheHobbit);

		Produto produto = repository.findByCodigo(livroTheHobbit.getCodigo());
		assertThat(produto).isNotNull().isEqualTo(livroTheHobbit);
	}

	@Test
	public void deveAtualizarProdutoSalvo() {
		repository.save(livroTheHobbit);

		Produto produto = repository.findByCodigo(livroTheHobbit.getCodigo());
		assertThat(produto).hasFieldOrPropertyWithValue("descricao", "Livro The Hobbit");

		produto.setDescricao("Livro The Hobbit 2");

		produto = repository.findByCodigo(livroTheHobbit.getCodigo());
		assertThat(produto).hasFieldOrPropertyWithValue("descricao", "Livro The Hobbit 2");
	}

	@Test
	public void deveRemoverProdutoPeloCodigo() {
		repository.save(livroTheHobbit);

		List<Produto> produtos = repository.findAll();
		assertThat(produtos).hasSize(1).contains(livroTheHobbit);

		repository.deleteByCodigo(livroTheHobbit.getCodigo());

		produtos = repository.findAll();
		assertThat(produtos).isEmpty();
	}

	@Test
	public void deveBuscarProdutosPelaDescricao() {
		repository.save(livroTheHobbit);
		repository.save(livroIt);
		
		List<Produto> produtos = repository.findByDescricao("Hobbit");
		assertThat(produtos).hasSize(1).contains(livroTheHobbit);
		
		produtos = repository.findByDescricao("LiVrO");
		assertThat(produtos).hasSize(2).contains(livroTheHobbit, livroIt);
	}

}

package com.matera.trainning.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.ProdutoRepository;
import com.matera.trainning.bookstore.service.exceptions.RegistroAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@RunWith(SpringRunner.class)
public class ProdutoServiceTest {

	private static final String COD_LIVRO_HOBBIT = "LIVRO23040";

	@Mock
	private ProdutoRepository repository;

	@InjectMocks
	private ProdutoService service;

	private Produto livroTheHobbit;
	private Produto livroIt;

	@Before
	public void setUp() {
		livroTheHobbit = new Produto("LIVRO23040", "Livro The Hobbit", new BigDecimal(57.63), LocalDate.now());
		livroIt = new Produto("LIVRO34536", "Livro IT - A coisa", new BigDecimal(74.90), LocalDate.now());
	}

	@Test
	public void listaProdutosEmBancoPopulado() {
		when(repository.findAll()).thenReturn(list(livroTheHobbit, livroIt));

		List<Produto> produtos = service.findAll();
		assertThat(produtos).hasSize(2).contains(livroTheHobbit, livroIt);
	}

	@Test
	public void listaProdutosEmBancoVazio() throws Exception {
		when(repository.findAll()).thenReturn(list());

		List<Produto> produtos = service.findAll();
		assertThat(produtos).isEmpty();
	}

	@Test
	public void buscaProdutoPeloCodigo() throws Exception {
		when(repository.findByCodigo(COD_LIVRO_HOBBIT)).thenReturn(Optional.of(livroTheHobbit));

		Produto produto = service.findByCodigo(COD_LIVRO_HOBBIT);
		assertThat(produto).isNotNull().isEqualTo(livroTheHobbit);
	}

	@Test
	public void buscaProdutoInexistentePeloCodigo() throws Exception {
		when(repository.findByCodigo(COD_LIVRO_HOBBIT)).thenReturn(Optional.empty());
		try {
			service.findByCodigo(COD_LIVRO_HOBBIT);
			fail();
		} catch (RegistroNotFoundException ex) {
			assertEquals("Produto inexistente", ex.getMessage());
		}
	}

	@Test
	public void buscaProdutoPelaDescricao() throws Exception {
		when(repository.findByDescricao(Mockito.anyString())).thenReturn(list(livroTheHobbit, livroIt));

		List<Produto> produtos = service.findByDescricao("LiVrO");
		assertThat(produtos).hasSize(2).contains(livroTheHobbit, livroIt);
	}

	@Test
	public void buscaProdutoInexistentePelaDescricao() throws Exception {
		when(repository.findByDescricao(Mockito.anyString())).thenReturn(list());

		List<Produto> produtos = service.findByDescricao("LiVrO");
		assertThat(produtos).isEmpty();
	}

	@Test
	public void persisteProdutoComDataCadastroPreenchida() throws Exception {
		when(repository.findByCodigo(COD_LIVRO_HOBBIT)).thenReturn(Optional.empty());
		when(repository.save(Mockito.any(Produto.class))).thenReturn(livroTheHobbit);

		Produto produto = service.insert(livroTheHobbit);
		assertThat(produto).isNotNull().hasFieldOrPropertyWithValue("dataCadastro", livroTheHobbit.getDataCadastro());
	}

	@Test
	public void persisteProdutoComDataCadastroNula() throws Exception {
		livroTheHobbit.setDataCadastro(null);

		when(repository.findByCodigo(COD_LIVRO_HOBBIT)).thenReturn(Optional.empty());
		when(repository.save(Mockito.any(Produto.class))).thenReturn(livroTheHobbit);

		Produto produto = service.insert(livroTheHobbit);
		assertThat(produto).isNotNull().hasFieldOrPropertyWithValue("dataCadastro", LocalDate.now());
	}

	@Test
	public void persisteProdutoDuplicado() throws Exception {
		when(repository.findByCodigo(COD_LIVRO_HOBBIT)).thenReturn(Optional.of(livroTheHobbit));
		try {
			service.insert(livroTheHobbit);
			fail();
		} catch (RegistroAlreadyExistsException ex) {
			assertEquals("Produto já existente", ex.getMessage());
		}
	}

	@Test
	public void atualizaProdutoInexistente() throws Exception {
		when(repository.findByCodigo(COD_LIVRO_HOBBIT)).thenReturn(Optional.empty());
		try {
			service.update(COD_LIVRO_HOBBIT, livroTheHobbit);
			fail();
		} catch (RegistroNotFoundException ex) {
			assertEquals("Produto inexistente", ex.getMessage());
		}
	}

	@Test
	public void excluiProdutoInexistente() throws Exception {
		when(repository.findByCodigo(COD_LIVRO_HOBBIT)).thenReturn(Optional.empty());
		try {
			service.delete(COD_LIVRO_HOBBIT);
			fail();
		} catch (RegistroNotFoundException ex) {
			assertEquals("Produto inexistente", ex.getMessage());
		}
	}

}

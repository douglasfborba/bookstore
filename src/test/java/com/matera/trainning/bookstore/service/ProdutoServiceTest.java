package com.matera.trainning.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.ProdutoRepository;
import com.matera.trainning.bookstore.service.exceptions.ProdutoAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.ProdutoNotFoundException;

@RunWith(SpringRunner.class)
public class ProdutoServiceTest {
	
	@Mock
	private ProdutoRepository repository;
	
	@InjectMocks
	private ProdutoService service;
			
	private Produto livroTheHobbit;
	private Produto livroIt;

	@Before
	public void setUp() {	
		livroTheHobbit = new Produto("LIVRO23040", "Livro The Hobbit", new BigDecimal(57.63), LocalDate.of(2019, 01, 29));
		livroIt = new Produto("LIVRO34536", "Livro IT - A coisa", new BigDecimal(74.90), LocalDate.of(2019, 01, 29));		
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
		when(repository.findByCodigo("LIVRO23040")).thenReturn(livroTheHobbit);

		Produto produto = service.findByCodigo("LIVRO23040");
		assertThat(produto).isNotNull().isEqualTo(livroTheHobbit);
	}
	
	@Test
	public void buscaProdutoInexistentePeloCodigo() throws Exception {
		when(repository.findByCodigo("LIVRO23040")).thenReturn(null);
		try {
			service.findByCodigo("LIVRO23040");
			fail();
		} catch (ProdutoNotFoundException ex) {
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
		when(repository.findByCodigo("LIVRO23040")).thenReturn(null);
		when(repository.save(Mockito.any(Produto.class))).thenReturn(livroTheHobbit);
		
		Produto produto = service.insert(livroTheHobbit);
		assertThat(produto).isNotNull().hasFieldOrPropertyWithValue("dataCadastro", livroTheHobbit.getDataCadastro());
	}
	
	@Test
	public void persisteProdutoComDataCadastroNula() throws Exception {		
		livroTheHobbit.setDataCadastro(null);
		
		when(repository.findByCodigo("LIVRO23040")).thenReturn(null);
		when(repository.save(Mockito.any(Produto.class))).thenReturn(livroTheHobbit);
		
		Produto produto = service.insert(livroTheHobbit);
		assertThat(produto).isNotNull().hasFieldOrPropertyWithValue("dataCadastro", LocalDate.now());
	}
	
	@Test
	public void persisteProdutoDuplicado() throws Exception {				
		when(repository.findByCodigo("LIVRO23040")).thenReturn(livroTheHobbit);
		try {
			service.insert(livroTheHobbit);
			fail();
		} catch (ProdutoAlreadyExistsException ex) {
			assertEquals("Produto já existente", ex.getMessage());
		}	
	}
	
	@Test
	public void atualizaProdutoInexistente() throws Exception {
		when(repository.findByCodigo("LIVRO23040")).thenReturn(null);
		try {
			service.update("LIVRO23040", livroTheHobbit);
			fail();
		} catch (ProdutoNotFoundException ex) {
			assertEquals("Produto inexistente", ex.getMessage());
		}		
	}
	
	@Test
	public void excluiProdutoInexistente() throws Exception {
		when(repository.findByCodigo("LIVRO23040")).thenReturn(null);
		try {
			service.delete("LIVRO23040");
			fail();
		} catch (ProdutoNotFoundException ex) {
			assertEquals("Produto inexistente", ex.getMessage());
		}		
	}

}

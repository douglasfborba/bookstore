package com.matera.trainning.bookstore.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.ProdutoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProdutoServiceTest {

	@Autowired
	private MockMvc mock;
	
	@Autowired
	private ProdutoService service;

	@MockBean
	private ProdutoRepository repository;
	
	private Produto livroTheHobbit;
	private Produto livroIt;

	@Before
	public void setUp() {
		livroTheHobbit = new Produto("LIVRO230401", "The Hobbit", new BigDecimal(57.63), LocalDate.now());
		livroIt = new Produto("LIVRO345361", "IT - A coisa", new BigDecimal(74.90), LocalDate.now());
	}

	@Test
	public void tentaInserirProdutoNulo() {
		Produto produto = null;
		service.insert(livroTheHobbit);
	}
	
//	@Test
//	public void deleteByCodigo() {
//		assertThat(repository.findAll()).isEmpty();
//
//		repository.save(livroTheHobbit);
//		assertThat(repository.findAll()).contains(livroTheHobbit).hasSize(1);
//
//		repository.save(livroIt);
//		assertThat(repository.findAll()).contains(livroTheHobbit, livroIt).hasSize(2);
//	}
//
//	@Test
//	public void testFindByCodigo() {
//		assertThat(repository.findByCodigo(livroTheHobbit.getCodigo())).isNull();
//
//		repository.save(livroTheHobbit);
//		assertThat(repository.findByCodigo(livroTheHobbit.getCodigo())).isNotNull().isEqualTo(livroTheHobbit);
//
//		repository.save(livroIt);
//		assertThat(repository.findByCodigo(livroIt.getCodigo())).isNotNull().isEqualTo(livroIt);
//	}
//
//	@Test
//	public void testFindByDescricao() {
//		assertThat(repository.findByDescricao("livro")).isEmpty();
//
//		repository.save(livroTheHobbit);
//		assertThat(repository.findByDescricao("livro")).contains(livroTheHobbit).hasSize(1);
//
//		repository.save(livroIt);
//		assertThat(repository.findByDescricao("livro")).contains(livroTheHobbit, livroIt).hasSize(2);
//
//		assertThat(repository.findByDescricao("revista")).isEmpty();
//	}
}

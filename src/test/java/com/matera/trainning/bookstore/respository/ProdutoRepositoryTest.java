package com.matera.trainning.bookstore.respository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matera.trainning.bookstore.model.Produto;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProdutoRepositoryTest {

	@Autowired
	private ProdutoRepository repository;

	private Produto livroTheHobbit;
	private Produto livroIt;

	@Before
	public void setUp() {
		livroTheHobbit = new Produto("LIVRO230401", "Livro The Hobbit", new BigDecimal(57.63), LocalDate.now());
		livroIt = new Produto("LIVRO345361", "Livro IT - A coisa", new BigDecimal(74.90), LocalDate.now());
	}

	@Test
	public void deleteByCodigo() {
		assertThat(repository.findAll()).isEmpty();

		repository.save(livroTheHobbit);
		assertThat(repository.findAll()).contains(livroTheHobbit).hasSize(1);

		repository.save(livroIt);
		assertThat(repository.findAll()).contains(livroTheHobbit, livroIt).hasSize(2);
	}

	@Test
	public void testFindByCodigo() {
		assertThat(repository.findByCodigo(livroTheHobbit.getCodigo())).isNull();

		repository.save(livroTheHobbit);
		assertThat(repository.findByCodigo(livroTheHobbit.getCodigo())).isNotNull().isEqualTo(livroTheHobbit);

		repository.save(livroIt);
		assertThat(repository.findByCodigo(livroIt.getCodigo())).isNotNull().isEqualTo(livroIt);
	}

	@Test
	public void testFindByDescricao() {
		assertThat(repository.findByDescricao("livro")).isEmpty();

		repository.save(livroTheHobbit);
		assertThat(repository.findByDescricao("livro")).contains(livroTheHobbit).hasSize(1);

		repository.save(livroIt);
		assertThat(repository.findByDescricao("livro")).contains(livroTheHobbit, livroIt).hasSize(2);

		assertThat(repository.findByDescricao("revista")).isEmpty();
	}

}

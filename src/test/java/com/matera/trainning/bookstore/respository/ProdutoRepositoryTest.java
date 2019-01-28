package com.matera.trainning.bookstore.respository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.matera.trainning.bookstore.model.Produto;

@DataJpaTest
@RunWith(SpringRunner.class)
public class ProdutoRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ProdutoRepository repository;

	@Test
	public void testFindAll() {
		assertThat(repository.findAll()).isEmpty();

		Produto livroTheHobbit = new Produto("LIVRO230401", "The Hobbit", new BigDecimal(57.63), LocalDate.now());
		persisteProduto(livroTheHobbit);

		assertThat(repository.findAll()).contains(livroTheHobbit).hasSize(1);

		Produto livroIt = new Produto("LIVRO345361", "IT - A coisa", new BigDecimal(74.90), LocalDate.now());
		persisteProduto(livroIt);

		assertThat(repository.findAll()).contains(livroTheHobbit, livroIt).hasSize(2);
	}

	private void persisteProduto(Produto livroTheHobbit) {
		entityManager.persist(livroTheHobbit);
		entityManager.flush();
	}

}

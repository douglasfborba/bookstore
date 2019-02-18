package com.matera.trainning.bookstore.respository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.matera.trainning.bookstore.model.HistoricoDePreco;
import com.matera.trainning.bookstore.model.Produto;

@DataJpaTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class HistoricoDePrecoRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private HistoricoDePrecoRepository repository;

	private Produto produto;

	private HistoricoDePreco itemHistPreco;

	@Before
	public void setUp() {
		produto = new Produto();
		produto.setCodigo("LIVRO23040");
		produto.setDescricao("Livro The Hobbit");
		produto.setPreco(new BigDecimal(57.63));
		produto.setDataCadastro(LocalDate.now());

		itemHistPreco = new HistoricoDePreco();
		itemHistPreco.setProduto(produto);
		itemHistPreco.setPreco(new BigDecimal(57.63));
		itemHistPreco.setDataHoraAlteracao(LocalDateTime.now());
	}
	
	@Test
	public void buscaHistoricoDePrecosPeloProduto() throws Exception {
		produto.addHistoricoDePreco(itemHistPreco);
		entityManager.persist(produto);

		List<HistoricoDePreco> itemsHistPreco = repository.findAllByProduto(produto, PageRequest.of(0, 1)).getContent();

		assertThat(itemsHistPreco).isNotEmpty().hasSize(1).contains(itemHistPreco);
	}

	@Test
	public void buscaHistoricoDePrecosPeloProdutoInexistente() throws Exception {
		itemHistPreco.setProduto(null);
		entityManager.persist(produto);

		List<HistoricoDePreco> itemsHistPreco = repository.findAllByProduto(produto, PageRequest.of(0, 1)).getContent();

		assertThat(itemsHistPreco).hasSize(0);
	}

}

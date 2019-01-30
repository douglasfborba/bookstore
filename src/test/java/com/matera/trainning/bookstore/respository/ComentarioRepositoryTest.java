package com.matera.trainning.bookstore.respository;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ComentarioRepositoryTest {

//	private static final String COD_USU_HATER = "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1";
//	private static final String CONT_DESC_BUSCA = "LiVrO";
//
//	@Autowired
//	private TestEntityManager entityManager;
//
//	@Autowired
//	private ProdutoRepository repository;
//
//	private Comentario hater;
//	private Comentario lover;
//
//	@Before
//	public void setUp() {
//		Produto livroTheHobbit = new Produto("LIVRO23040", "Livro The Hobbit", new BigDecimal(57.63),
//				LocalDate.of(2019, 01, 29));
//
//		hater = new Comentario("Odiei, este é o pior livro do mundo!", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1",
//				"usuario.hater", LocalDateTime.now(), livroTheHobbit);
//
//		lover = new Comentario("Amei, melhor livro do mundo :D", "dXN1YXJpby5sb3ZlcjMwMDEyMDE5MTcyNTAw",
//				"usuario.lover", LocalDateTime.now(), livroTheHobbit);
//	}
//
//	@Test
//	public void listaProdutosEmBancoPopulado() {
//		entityManager.persist(livroTheHobbit);
//		entityManager.persist(livroIt);
//
//		List<Produto> produtos = repository.findAll();
//		assertThat(produtos).hasSize(2).contains(livroTheHobbit, livroIt);
//	}
//
//	@Test
//	public void listaProdutosEmBancoVazio() {
//		List<Produto> produtos = repository.findAll();
//		assertThat(produtos).isEmpty();
//	}
//
//	@Test
//	public void buscaProdutoPeloCodigo() {
//		entityManager.persist(livroTheHobbit);
//
//		Optional<Produto> opcional = repository.findByCodigo(COD_LIVRO_HOBBIT);
//		assertThat(opcional.get()).isNotNull().isEqualTo(livroTheHobbit);
//	}
//
//	@Test
//	public void buscaProdutoInexistentePeloCodigo() {
//		Optional<Produto> opcional = repository.findByCodigo(COD_LIVRO_HOBBIT);
//		assertThat(opcional.isPresent()).isFalse();
//	}
//
//	@Test
//	public void buscaProdutoPelaDescricao() {
//		entityManager.persist(livroTheHobbit);
//		entityManager.persist(livroIt);
//
//		List<Produto> produtos = repository.findByDescricao("LiVrO");
//		assertThat(produtos).hasSize(2).contains(livroTheHobbit, livroIt);
//	}
//
//	@Test
//	public void buscaProdutoInexistentePelaDescricao() {
//		List<Produto> produtos = repository.findByDescricao("LiVrO");
//		assertThat(produtos).isEmpty();
//	}
//
//	@Test
//	public void persisteProduto() {
//		repository.save(livroTheHobbit);
//
//		List<Produto> produtos = repository.findAll();
//		assertThat(produtos).hasSize(1).contains(livroTheHobbit);
//	}
//
//	@Test
//	public void atualizaProduto() {
//		entityManager.persist(livroTheHobbit);
//
//		Optional<Produto> opcional = repository.findByCodigo(livroTheHobbit.getCodigo());
//		Produto produto = opcional.get();
//
//		assertThat(produto).hasFieldOrPropertyWithValue("descricao", "Livro The Hobbit");
//
//		produto.setDescricao("Livro The Hobbit 2");
//
//		opcional = repository.findByCodigo(livroTheHobbit.getCodigo());
//		produto = opcional.get();
//
//		assertThat(produto).hasFieldOrPropertyWithValue("descricao", "Livro The Hobbit 2");
//	}
//
//	@Test
//	public void removeProdutoPeloCodigo() {
//		entityManager.persist(livroTheHobbit);
//
//		List<Produto> produtos = repository.findAll();
//		assertThat(produtos).hasSize(1).contains(livroTheHobbit);
//
//		repository.deleteByCodigo(livroTheHobbit.getCodigo());
//
//		produtos = repository.findAll();
//		assertThat(produtos).isEmpty();
//	}

}

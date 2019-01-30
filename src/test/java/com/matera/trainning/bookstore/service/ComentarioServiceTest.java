package com.matera.trainning.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.ComentarioRepository;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@RunWith(SpringRunner.class)
public class ComentarioServiceTest {

	private static final String COD_USU_HATER = "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1";
	private static final String CONT_DESC_BUSCA = "LiVrO";

	@Mock
	private ComentarioRepository repository;

	@InjectMocks
	private ComentarioService service;

	private Comentario hater;
	private Comentario lover;

	@Before
	public void setUp() {
		Produto livroTheHobbit = new Produto("LIVRO23040", "Livro The Hobbit", new BigDecimal(57.63),
				LocalDate.of(2019, 01, 29));

		hater = new Comentario("Odiei, este é o pior livro do mundo!", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1",
				"usuario.hater", LocalDateTime.now(), livroTheHobbit);

		lover = new Comentario("Amei, melhor livro do mundo :D", "dXN1YXJpby5sb3ZlcjMwMDEyMDE5MTcyNTAw",
				"usuario.lover", LocalDateTime.now(), livroTheHobbit);
	}

	@Test
	public void listaComentariosEmBancoPopulado() {
		when(repository.findAll()).thenReturn(list(hater, lover));

		List<Comentario> comentarios = service.findAll();
		assertThat(comentarios).hasSize(2).contains(hater, lover);
	}

	@Test
	public void listaComentariosEmBancoVazio() throws Exception {
		when(repository.findAll()).thenReturn(list());

		List<Comentario> comentarios = service.findAll();
		assertThat(comentarios).isEmpty();
	}

	@Test
	public void buscaComentarioPeloCodigo() throws Exception {
		when(repository.findByCodigo(COD_USU_HATER)).thenReturn(Optional.of(hater));

		Comentario comentario = service.findByCodigo(COD_USU_HATER);
		assertThat(comentario).isNotNull().isEqualTo(hater);
	}

	@Test
	public void buscaComentarioInexistentePeloCodigo() throws Exception {
		when(repository.findByCodigo(COD_USU_HATER)).thenReturn(Optional.empty());
		try {
			service.findByCodigo(COD_USU_HATER);
			fail();
		} catch (RegistroNotFoundException ex) {
			assertEquals("Comentário inexistente", ex.getMessage());
		}
	}
	
	@Test
	public void buscaComentarioPeloCodigoDoProduto() throws Exception {
		when(repository.findByProdutoCodigo("LIVRO23040")).thenReturn(list(hater));

		List<Comentario> comentarios = service.findByProdutoCodigo("LIVRO23040");
		assertThat(comentarios).hasSize(1).contains(hater);
	}

	@Test
	public void buscaComentarioInexistentePeloCodigoDoProduto() throws Exception {
		when(repository.findByDescricao(Mockito.anyString())).thenReturn(list());

		List<Comentario> comentarios = service.findByDescricao("LIVRO23040");
		assertThat(comentarios).isEmpty();
	}

	@Test
	public void buscaComentarioPelaDescricao() throws Exception {
		when(repository.findByDescricao(Mockito.anyString())).thenReturn(list(hater, lover));

		List<Comentario> comentarios = service.findByDescricao(CONT_DESC_BUSCA);
		assertThat(comentarios).hasSize(2).contains(hater, lover);
	}

	@Test
	public void buscaComentarioInexistentePelaDescricao() throws Exception {
		when(repository.findByDescricao(Mockito.anyString())).thenReturn(list());

		List<Comentario> comentarios = service.findByDescricao(CONT_DESC_BUSCA);
		assertThat(comentarios).isEmpty();
	}

	@Test
	public void persisteComentarioComDataCadastroPreenchida() throws Exception {
		when(repository.findByCodigo(COD_USU_HATER)).thenReturn(Optional.empty());
		when(repository.save(Mockito.any(Comentario.class))).thenReturn(hater);

		Comentario comentario = service.insert(hater);
		assertThat(comentario).isNotNull().hasFieldOrPropertyWithValue("dataHoraCriacao", hater.getDataHoraCriacao());
	}

	@Test
	public void persisteComentarioComDataCadastroNula() throws Exception {
		hater.setDataHoraCriacao(null);

		when(repository.findByCodigo(COD_USU_HATER)).thenReturn(Optional.empty());
		when(repository.save(Mockito.any(Comentario.class))).thenReturn(hater);

		Comentario comentario = service.insert(hater);
		assertThat(comentario).isNotNull().hasFieldOrPropertyWithValue("dataHoraCriacao", comentario.getDataHoraCriacao());
	}

	@Test
	public void atualizaComentarioInexistente() throws Exception {
		when(repository.findByCodigo(COD_USU_HATER)).thenReturn(Optional.empty());
		try {
			service.update(COD_USU_HATER, hater);
			fail();
		} catch (RegistroNotFoundException ex) {
			assertEquals("Comentário inexistente", ex.getMessage());
		}
	}

	@Test
	public void excluiComentarioInexistente() throws Exception {
		when(repository.findByCodigo(COD_USU_HATER)).thenReturn(Optional.empty());
		try {
			service.delete(COD_USU_HATER);
			fail();
		} catch (RegistroNotFoundException ex) {
			assertEquals("Comentário inexistente", ex.getMessage());
		}
	}

}

package com.matera.trainning.bookstore.respository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.matera.trainning.bookstore.model.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

	public Optional<Comentario> findByCodigo(String codigo);

	@Transactional
	public void deleteByCodigo(String codigo);

	@Query("SELECT c FROM Comentario c JOIN Produto p ON p.codigo = ?1 WHERE LOWER(c.descricao) LIKE CONCAT('%', LOWER(?2), '%')")
	public List<Comentario> findByProdutoCodigoAndDescricao(String codigo, String descricao);
	
	public List<Comentario> findByProdutoCodigo(String produtoCodigo);
	
	public Optional<Comentario> findByProdutoCodigoAndCodigo(String produtoCodigo, String comentarioCodigo);

}

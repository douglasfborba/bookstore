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

	@Query("SELECT c FROM Comentario c WHERE LOWER(c.descricao) LIKE CONCAT('%', LOWER(?1), '%')")
	public List<Comentario> findByDescricao(String descricao);

	public List<Comentario> findByProdutoCodigo(String codigo);

}

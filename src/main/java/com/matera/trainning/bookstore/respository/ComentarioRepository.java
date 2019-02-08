package com.matera.trainning.bookstore.respository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matera.trainning.bookstore.domain.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

	public Optional<Comentario> findByCodigo(String codigo);

	@Transactional
	public void deleteByCodigo(String codigo);	

}

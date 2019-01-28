package com.matera.trainning.bookstore.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matera.trainning.bookstore.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}

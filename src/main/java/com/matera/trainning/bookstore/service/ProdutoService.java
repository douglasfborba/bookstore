package com.matera.trainning.bookstore.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.ProdutoRepository;
import com.matera.trainning.bookstore.service.exceptions.ProdutoAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.ProdutoNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repository;

	public List<Produto> findAll() {
		return repository.findAll();
	}

	public Produto insert(Produto produto) throws ProdutoAlreadyExistsException {
		Produto produtoSalvo = repository.findByCodigo(produto.getCodigo());

		if (produtoSalvo == null) {
			if (produto.getDataCadastro() == null)
				produto.setDataCadastro(LocalDate.now());
			
			return repository.save(produto);
		}
		
		throw new ProdutoAlreadyExistsException("Produto já existente");
	}

	public void updateByCodigo(String codigo, Produto produto) throws ProdutoNotFoundException {
		Produto produtoSalvo = repository.findByCodigo(codigo);

		if (produtoSalvo == null)
			throw new ProdutoNotFoundException("Produto inexistente");

		produtoSalvo.setCodigo(produto.getCodigo());
		produtoSalvo.setDescricao(produto.getDescricao());
		produtoSalvo.setPreco(produto.getPreco());
		produtoSalvo.setDataCadastro(produto.getDataCadastro());

		repository.save(produtoSalvo);
	}

	public void deleteByCodigo(String codigo) throws ProdutoNotFoundException {
		Produto produto = repository.findByCodigo(codigo);

		if (produto == null)
			throw new ProdutoNotFoundException("Produto inexistente");

		repository.deleteByCodigo(codigo);
	}

	public Produto findByCodigo(String codigo) throws ProdutoNotFoundException {
		Produto produto = repository.findByCodigo(codigo);

		if (produto == null)
			throw new ProdutoNotFoundException("Produto inexistente");

		return produto;
	}

	public List<Produto> findByDescricao(String descricao) {
		return repository.findByDescricao(descricao);
	}

}

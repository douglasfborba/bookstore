package com.matera.trainning.bookstore.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.domain.Produto;
import com.matera.trainning.bookstore.respository.ProdutoRepository;
import com.matera.trainning.bookstore.service.exceptions.RegistroAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repository;

	public Produto insert(Produto produto) throws RegistroAlreadyExistsException {
		Optional<Produto> opcional = repository.findByCodigo(produto.getCodigo());

		if (!opcional.isPresent()) {
			if (produto.getDataCadastro() == null)
				produto.setDataCadastro(LocalDate.now());

			return repository.save(produto);
		}

		throw new RegistroAlreadyExistsException("Produto já existente");
	}

	public void update(String codigo, Produto produto) throws RegistroNotFoundException {
		Optional<Produto> opcional = repository.findByCodigo(codigo);

		if (!opcional.isPresent())
			throw new RegistroNotFoundException("Produto inexistente");

		Produto produtoSalvo = opcional.get();

		produtoSalvo.setCodigo(produto.getCodigo());
		produtoSalvo.setDescricao(produto.getDescricao());
		produtoSalvo.setPreco(produto.getPreco());

		repository.save(produtoSalvo);
	}

	public void delete(String codigo) throws RegistroNotFoundException {
		Optional<Produto> opcional = repository.findByCodigo(codigo);

		if (!opcional.isPresent())
			throw new RegistroNotFoundException("Produto inexistente");

		repository.deleteByCodigo(codigo);
	}

	public Produto findByCodigo(String codigo) throws RegistroNotFoundException {
		Optional<Produto> opcional = repository.findByCodigo(codigo);

		if (!opcional.isPresent())
			throw new RegistroNotFoundException("Produto inexistente");

		return opcional.get();
	}

	public List<Produto> findByDescricao(String descricao) {
		return repository.findByDescricao(descricao);
	}

	public List<Produto> findAll() {
		return repository.findAll();
	}

}

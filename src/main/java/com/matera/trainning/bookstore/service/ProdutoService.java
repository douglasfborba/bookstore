package com.matera.trainning.bookstore.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.domain.HistoricoDePreco;
import com.matera.trainning.bookstore.domain.Produto;
import com.matera.trainning.bookstore.respository.ProdutoRepository;
import com.matera.trainning.bookstore.service.exceptions.RegistroAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repository;
	
	@Autowired
	private HistoricoDePrecoService historicoService;

	public Produto insert(Produto produto) throws RegistroAlreadyExistsException {
		Optional<Produto> opcional = repository.findByCodigo(produto.getCodigo());

		if (!opcional.isPresent()) {
			if (produto.getDataCadastro() == null)
				produto.setDataCadastro(LocalDate.now());
			
			Produto produtoSalvo = repository.save(produto);
			historizaPreco(produtoSalvo);
			
			return produtoSalvo;			
		}

		throw new RegistroAlreadyExistsException("Produto já existente");
	}
		
	public void update(String codigo, Produto produto) throws RegistroNotFoundException, RegistroAlreadyExistsException {
		Optional<Produto> opcional = repository.findByCodigo(codigo);

		if (!opcional.isPresent())
			throw new RegistroNotFoundException("Produto inexistente");

		Produto produtoSalvo = opcional.get();
	
		produto.setId(produtoSalvo.getId());
		produto.setDataCadastro(produtoSalvo.getDataCadastro());
				
		if (produtoSalvo.getPreco().compareTo(produto.getPreco()) != 0)
			historizaPreco(produto);
		
		repository.save(produto);
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
	
	private void historizaPreco(Produto produto) throws RegistroAlreadyExistsException {
		HistoricoDePreco historico = new HistoricoDePreco();
		
		historico.setProduto(produto);
		historico.setDataHoraAlteracao(LocalDateTime.now());
		historico.setPreco(produto.getPreco());
		
		historicoService.insert(historico);
	}

}

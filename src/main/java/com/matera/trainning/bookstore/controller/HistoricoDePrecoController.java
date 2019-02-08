package com.matera.trainning.bookstore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtos/{codigoProduto}/precos")
public class HistoricoDePrecoController {
//
//	@Autowired
//	private HistoricoDePrecoFacade facade;
//
//	@GetMapping(value = "/search", params = { "dataHoraAlteracao" })
//	public ResponseEntity<HistoricoDePrecoDTO> findByProdutoCodigoAndDataHoraAlteracao(@PathVariable String codigoProduto, @RequestParam("dataHoraAlteracao") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHoraAlteracao) {
//		try {
//			HistoricoDePrecoDTO dto = facade.findByProdutoCodigoAndDataHoraAlteracao(codigoProduto, dataHoraAlteracao);
//			return ResponseEntity.ok(dto);
//		} catch (RegistroNotFoundException ex) {
//			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
//		}
//	}
//	
//	@GetMapping
//	public List<HistoricoDePrecoDTO> findAllByProdutoCodigo(@PathVariable String codigoProduto) {
//		return facade.findAllByProdutoCodigo(codigoProduto);
//	}
//	
//	@GetMapping(value = "/search", params = {"dataHoraInicio", "dataHoraFim"})
//	public List<HistoricoDePrecoDTO> findAllByProdutoCodigoWithDataHoraAlteracaoBetween(@PathVariable String codigoProduto, @RequestParam("dataHoraInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio, @RequestParam("dataHoraFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
//		return facade.findAllByProdutoCodigoWithDataHoraAlteracaoBetween(codigoProduto, inicio, fim);
//	}	

}

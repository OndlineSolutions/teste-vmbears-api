package com.testeTecnico.desafio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testeTecnico.desafio.model.Compra;
import com.testeTecnico.desafio.repository.CompraRepository;

@Service
public class CompraService {
	
	@Autowired
	private CompraRepository compraRepository;
	
	public Compra salvarCompra(Compra compra) throws Exception {
		try {
			return compraRepository.save(compra);			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void deletarDados() throws Exception {
		try {
			compraRepository.deleteAll();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}

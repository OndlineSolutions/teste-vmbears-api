package com.testeTecnico.desafio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testeTecnico.desafio.model.PrecoMedio;
import com.testeTecnico.desafio.repository.PrecoMedioRepository;

@Service
public class PrecoMedioService {
	
	@Autowired
	private PrecoMedioRepository precoMedioRepository;
	
	public PrecoMedio salvarPrecoMedio(PrecoMedio precoMedio) throws Exception {
		try {
			return precoMedioRepository.save(precoMedio);			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void deletarDados() throws Exception {
		try {
			precoMedioRepository.deleteAll();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}

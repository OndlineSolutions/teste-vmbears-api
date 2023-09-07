package com.testeTecnico.desafio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testeTecnico.desafio.model.Geracao;
import com.testeTecnico.desafio.repository.GeracaoRepository;

@Service
public class GeracaoService {
	
	@Autowired
	private GeracaoRepository geracaoRepository;
	
	public Geracao salvarGeracao(Geracao geracao) throws Exception {
		try {
			return geracaoRepository.save(geracao);			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void deletarDados() throws Exception {
		try {
			geracaoRepository.deleteAll();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}

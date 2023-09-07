package com.testeTecnico.desafio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testeTecnico.desafio.model.Regiao;
import com.testeTecnico.desafio.repository.RegiaoRepository;

@Service
public class RegiaoService {
	
	@Autowired
	private RegiaoRepository regiaoRepository;
	
	public Regiao salvarRegiao(Regiao regiao) throws Exception {
		try {
			return regiaoRepository.save(regiao);			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void deletarDados() throws Exception {
		try {
			regiaoRepository.deleteAll();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}

package com.testeTecnico.desafio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testeTecnico.desafio.model.Agente;
import com.testeTecnico.desafio.repository.AgenteRepository;

@Service
public class AgenteService {
	
	@Autowired
	private AgenteRepository agenteRepository;
	
	@Autowired
	private RegiaoService regiaoService;
	
	@Autowired
	private GeracaoService geracaoService;
	
	@Autowired
	private CompraService compraService;
	
	@Autowired
	private PrecoMedioService precoMedioService;
	
	public List<Agente> buscarAgentes() throws Exception {
		List<Agente> agentes = new ArrayList<>();
		try {
			return agenteRepository.findAll();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void deletarDados() throws Exception {
		try {
			precoMedioService.deletarDados();
			compraService.deletarDados();
			geracaoService.deletarDados();
			compraService.deletarDados();
			regiaoService.deletarDados();
			agenteRepository.deleteAll();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Agente salvarAgente(Agente agente) throws Exception {
		try {
			return agenteRepository.save(agente);
//			return null;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Agente findByCodigo(Long codigo) throws Exception {
		try {
			return agenteRepository.findByCodigo(codigo);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}

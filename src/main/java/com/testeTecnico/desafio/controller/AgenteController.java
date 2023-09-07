package com.testeTecnico.desafio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.testeTecnico.desafio.model.Agente;
import com.testeTecnico.desafio.service.AgenteService;

@RestController
@RequestMapping("/agentes")
public class AgenteController {

	@Autowired
	private AgenteService agenteService;
	
	@GetMapping
	public ResponseEntity<List<Agente>> validateXml() throws Exception{		
		return new ResponseEntity<>(agenteService.buscarAgentes(), HttpStatus.OK);
	}
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarDados() throws Exception {
		agenteService.deletarDados();
	}
}

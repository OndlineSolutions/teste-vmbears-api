package com.testeTecnico.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testeTecnico.desafio.model.Agente;

public interface AgenteRepository extends JpaRepository <Agente, Long> {

	public Agente findByCodigo(Long codigo);
}

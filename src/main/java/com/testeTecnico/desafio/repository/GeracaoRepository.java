package com.testeTecnico.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testeTecnico.desafio.model.Geracao;

public interface GeracaoRepository extends JpaRepository <Geracao, Long> {

}

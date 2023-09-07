package com.testeTecnico.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testeTecnico.desafio.model.PrecoMedio;

public interface PrecoMedioRepository extends JpaRepository <PrecoMedio, Long> {

}

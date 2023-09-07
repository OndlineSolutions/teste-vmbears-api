package com.testeTecnico.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testeTecnico.desafio.model.Compra;

public interface CompraRepository extends JpaRepository <Compra, Long> {

}

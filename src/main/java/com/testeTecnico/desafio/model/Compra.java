package com.testeTecnico.desafio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="compra")
public class Compra {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;
	
	private String valor;

	@ManyToOne
	@JsonIgnore
    @JoinColumn(name="regiao_id", nullable=true)
    private Regiao regiao;
	
    public Compra(String valor, Regiao regiao) {
    	this.valor = valor;
    	this.regiao = new Regiao(regiao);
    }
}

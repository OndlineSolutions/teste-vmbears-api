package com.testeTecnico.desafio.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "agente")
public class Agente {

	@Id
	@Column(name = "codigo")
	private Long codigo;

	@Column(name = "data")
	private LocalDateTime data;

	@OneToMany(mappedBy = "agente")
	private Set<Regiao> regioes;
	
	public Agente(Long codigo, LocalDateTime data) {
		this.codigo = codigo;
		this.data = data;
	}
	
	public Agente(Agente agente) {
		this.codigo = agente.getCodigo();
		this.data = agente.getData();
	}
}

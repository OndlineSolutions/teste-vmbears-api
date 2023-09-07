package com.testeTecnico.desafio.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="regiao")
public class Regiao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;
	
	private String sigla;
	
	@OneToMany(mappedBy = "regiao")
	private Set<Geracao> geracao;
	
	@OneToMany(mappedBy = "regiao")
	private Set<Compra> compra;
	
	@OneToMany(mappedBy = "regiao")
	private Set<PrecoMedio> precoMedio;
	
    @ManyToOne
	@JsonIgnore
    @JoinColumn(name="agente_codigo", nullable=true)
    private Agente agente;
    
    public Regiao(String sigla, Agente agente) {
    	this.sigla = sigla;
    	this.agente = new Agente(agente);
    }
    
    public Regiao(Regiao regiao) {
    	this.sigla = regiao.sigla;
    }
}

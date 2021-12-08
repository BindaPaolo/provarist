package it.unimib.bdf.assignment3.models;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Persona {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Basic(optional = false, fetch = FetchType.LAZY)
	private String nome;

	@Basic(optional = false, fetch = FetchType.LAZY)
	private String cognome;

	protected Persona(String nome, String cognome) {
		this.nome = nome;
		this.cognome = cognome;
	}
}

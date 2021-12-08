package it.unimib.bdf.assignment3.models;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Staff extends Persona {

	@Basic(optional = false)
	private String cf;

	@Basic(optional = false)
	private String nome;

	@Basic(optional = false)
	private String cognome;

	@Basic(optional = false)
	private String ruolo;

	@Basic(optional = false)
	@Temporal(TemporalType.DATE)
	private Date dataNascita;
	/*
	 * TO SET: temporalValues.setUtilDate( new
	 * SimpleDateFormat("yyyy-MM-dd").parse("2017-11-15"));
	 */

	public Staff(String cf, String nome, String cognome, String ruolo, Date dataNascita) {
		super(nome, cognome);

		this.cf = cf;
		this.nome = nome;
		this.cognome = cognome;
		this.ruolo = ruolo;
		this.dataNascita = dataNascita;
	}
}

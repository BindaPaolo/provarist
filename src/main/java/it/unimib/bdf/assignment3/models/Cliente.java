package it.unimib.bdf.assignment3.models;

import java.util.ArrayList;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Cliente extends Persona {

	@Basic(optional = false)
	private String numTel;

	@Basic
	private String email;

	private Cliente consigliatoDa;

	@Column
	private ArrayList<Alimento> allergia;

	public Cliente(String nome, String cognome, String numTel, String email) {
		super(nome, cognome);

		this.numTel = numTel;
		this.email = email;

		allergia = new ArrayList<>();
	}
}

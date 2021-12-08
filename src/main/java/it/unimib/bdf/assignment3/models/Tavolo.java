package it.unimib.bdf.assignment3.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tavolo {

	@Id
	private int id;

	private Integer numPosti;

	public Tavolo(Integer numPosti) {
		this.numPosti = numPosti;
	}

}

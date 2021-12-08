package it.unimib.bdf.assignment3.models;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Prenotazione {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false)
	private int numCommensali;

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataTimeStamp;
	/*
	 * TO SET: temporalValues.setUtilTimestamp( new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
	 * .parse("2017-11-15 15:30:14.332"));
	 */

	private ArrayList<Tavolo> tavoliRiservati;

	public Prenotazione(int numCommensali, Date data, String turno) {
		this.numCommensali = numCommensali;
		this.dataTimeStamp = data;
		this.tavoliRiservati = new ArrayList<Tavolo>();
	}

}

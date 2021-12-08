package it.unimib.bdf.assignment3;

import java.util.ArrayList;
import java.util.UUID;
import java.time.LocalDate;
import it.unimib.bdf.assignment3.Tavolo;

public class Prenotazione {
	private UUID idPrenotazione;
	private Integer numCommensali;
	private LocalDate giorno;
	private String turno;
	private ArrayList<Tavolo> riservato;
	
	public Prenotazione(Integer numCommensali, LocalDate giorno, String turno) {
		this.idPrenotazione = UUID.randomUUID();
		this.numCommensali = numCommensali;
		this.giorno = giorno;
		this.turno = turno;
		this.riservato = new ArrayList<>();
	}
	
	/**
	 * @param tavolo the tavolo to add to the reservation
	 */
	private void addTavolo(Tavolo tavolo) {
		if(tavolo.getOccupato()) {
			System.out.println("Il tavolo già occupato!");
		}
		else if(tavolo.getNumPosti() < this.numCommensali) {
			System.out.println("Il tavolo non ha posti a sufficienza!");
		}
		else {
			this.riservato.add(tavolo);
			tavolo.setOccupato(false);
			System.out.println("Il tavolo è stato aggiunto alla prenotazione");
		}
	}
	
	/**
	 * @return the numCommensali
	 */
	private Integer getNumCommensali() {
		return numCommensali;
	}

	/**
	 * @param numCommensali the numCommensali to set
	 */
	private void setNumCommensali(Integer numCommensali) {
		this.numCommensali = numCommensali;
	}

	/**
	 * @return the giorno
	 */
	private LocalDate getGiorno() {
		return giorno;
	}

	/**
	 * @param giorno the giorno to set
	 */
	private void setGiorno(LocalDate giorno) {
		this.giorno = giorno;
	}

	/**
	 * @return the turno
	 */
	private String getTurno() {
		return turno;
	}

	/**
	 * @param turno the turno to set
	 */
	private void setTurno(String turno) {
		this.turno = turno;
	}

	/**
	 * @return the idPrenotazione
	 */
	private UUID getIdPrenotazione() {
		return this.idPrenotazione;
	}



}

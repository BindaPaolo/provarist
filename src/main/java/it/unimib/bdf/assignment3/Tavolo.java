package it.unimib.bdf.assignment3;

import java.util.concurrent.atomic.AtomicInteger;

public class Tavolo {
	
	private static final AtomicInteger count =  new AtomicInteger(0);
	private final Integer numTavolo;
	private Integer numPosti;
	private Boolean occupato;//diventa false in seguito ad una prenotazione,
							//diventa true dopo la fine di un turno in cui è prenotato
	
	public Tavolo(Integer numPosti) {
		this.numTavolo = count.incrementAndGet();
		this.numPosti = numPosti;
		this.occupato = true;
	}

	/**
	 * @return the numPosti
	 */
	Integer getNumPosti() {
		return numPosti;
	}

	/**
	 * @param numPosti the numPosti to set
	 */
	private void setNumPosti(Integer numPosti) {
		this.numPosti = numPosti;
	}

	/**
	 * @return the occupato
	 */
	Boolean getOccupato() {
		return occupato;
	}

	/**
	 * @param occupato the occupato to set
	 */
	void setOccupato(Boolean occupato) {
		this.occupato = occupato;
	}

	/**
	 * @return the numTavolo
	 */
	private Integer getNumtavolo() {
		return this.numTavolo;
	}

}

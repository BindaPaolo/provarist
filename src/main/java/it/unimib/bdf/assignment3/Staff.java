package it.unimib.bdf.assignment3;

import java.util.UUID;
import java.time.LocalDate;

public class Staff extends Persona {

	private UUID idStaff;
	private String ruolo;
	private String residenza;
	private LocalDate dataNascita;
	private Float compensoMensile;
	
	public Staff(String cf, String nome, String cognome, String numTel) {
		super(cf, nome, cognome, numTel);
		//idStaff, insieme a codice fiscale ereditato da Persona,
		// è PK per l'entità Staff.
		this.idStaff = UUID.randomUUID();
	}

	/**
	 * @return the idStaff
	 */
	private UUID getIdStaff() {
		return this.idStaff;
	}
	
	/**
	 * @return the ruolo
	 */
	private String getRuolo() {
		return ruolo;
	}

	/**
	 * @param ruolo the ruolo to set
	 */
	private void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	/**
	 * @return the residenza
	 */
	private String getResidenza() {
		return residenza;
	}

	/**
	 * @param residenza the residenza to set
	 */
	private void setResidenza(String residenza) {
		this.residenza = residenza;
	}

	/**
	 * @return the dataNascita
	 */
	private LocalDate getDataNascita() {
		return dataNascita;
	}

	/**
	 * @param dataNascita the dataNascita to set
	 */
	private void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	/**
	 * @param compensoMensile the compensoMensile to set
	 */
	private void setCompensoMensile(Float compenso) {
		this.compensoMensile = compenso;
	}
		
	/**
	 * @return the compensoMensile
	 */
	private Float getCompensoMensile() {
		return this.compensoMensile;
	}
}

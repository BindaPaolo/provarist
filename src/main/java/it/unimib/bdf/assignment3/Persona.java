package it.unimib.bdf.assignment3;

public class Persona {
	private String codiceFiscale; //PK
	private String nome;
	private String cognome;
	private String numeroTelefono;
	
	Persona(String cf, String nome, String cognome, String numTel) {
		if(validateParam(cf, nome, cognome, numTel)) {
			this.codiceFiscale = cf;
			this.nome = nome;
			this.cognome = cognome;
			this.numeroTelefono = numTel;
		}
	}

	private Boolean validateParam(String cf, String nome, String cognome, String numTel) {
		//TODO
		return true;
	}

	/**
	 * @return the numeroTelefono
	 */
	private String getNumeroTelefono() {
		return numeroTelefono;
	}


	/**
	 * @param numeroTelefono the numeroTelefono to set
	 */
	private void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}


	/**
	 * @return the codiceFiscale
	 */
	private String getCodiceFiscale() {
		return codiceFiscale;
	}


	/**
	 * @return the nome
	 */
	private String getNome() {
		return nome;
	}


	/**
	 * @return the cognome
	 */
	private String getCognome() {
		return cognome;
	}

}

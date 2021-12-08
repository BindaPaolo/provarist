package it.unimib.bdf.assignment3;

import java.time.LocalDate;
import java.util.ArrayList;
import it.unimib.bdf.assignment3.Alimento;

public class Cliente extends Persona {
	private Cliente consigliatoDa;
	private ArrayList<Alimento> allergia;
	
	public Cliente(String cf, String nome, String cognome, String numTel) {
		super(cf, nome, cognome, numTel);
		allergia = new ArrayList<>();
	}
	
	/* La prenotazione ha
	 * esito positivo solo se c'è
	 * almeno un tavolo libero nel giorno e turno
	 * richiesti con un numero di posti maggiore o uguale
	 * al numero di commensali.
	 * 
	 * @param giorno il giorno della prenotazione
	 * @param turno il turno (pranzo o cena)
	 * @parma numCommensali il numero di commensali
	 */
	private Boolean effettuaPrenotazione(LocalDate giorno, String turno, Integer numCommensali) {
		//TODO
		return true;
	}
	
	/**
	 * @param alimento alimento to add
	 * */
	private void aggiungiAllergia(Alimento alimento) {
		this.allergia.add(alimento);
	}
	
	/**
	 * @return the array list allergia
	 */
	private ArrayList<Alimento> getAllergia(){
		return this.allergia;
	}

}

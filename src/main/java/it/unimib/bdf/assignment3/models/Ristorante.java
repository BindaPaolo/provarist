package it.unimib.bdf.assignment3.models;

import java.util.ArrayList;

public class Ristorante {

	private ArrayList<Tavolo> tavoli;

	public Ristorante() {
		Tavolo tavolo;

		for (int i = 0; i < 10; i++) {
			// 15 x tavoli da 2 posti
			tavolo = new Tavolo(2);
			tavoli.add(tavolo);

			// 10 x tavoli da 4 posti
			tavolo = new Tavolo(4);
			tavoli.add(tavolo);
		}

		for (int i = 0; i < 5; i++) {
			// 5 x tavoli da 6 posti
			tavolo = new Tavolo(6);
			tavoli.add(tavolo);
		}
	}
}

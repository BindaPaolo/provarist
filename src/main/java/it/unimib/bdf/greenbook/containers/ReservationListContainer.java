package it.unimib.bdf.greenbook.containers;

import java.util.List;

import it.unimib.bdf.greenbook.models.Reservation;

public class ReservationListContainer {

	private List<Reservation> reservations;

	public List<Reservation> getReservations() {
		return this.reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	@Override
	public String toString() {
		String s = "";
		for (Reservation r: reservations) {
			s += r.toString();
		}
		return s;
	}
}

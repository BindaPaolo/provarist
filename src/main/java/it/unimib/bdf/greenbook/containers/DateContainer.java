package it.unimib.bdf.greenbook.containers;

import java.time.LocalDate;

public class DateContainer {
	private LocalDate date;
	
	public LocalDate getDate() {
		return this.date;
	}
	
	public void setDate(String date) {
		try {
			this.date = LocalDate.parse(date);
		}catch(Exception e) {
			this.date = null;
		}
	}
}

package it.unimib.bdf.greenbook.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
public class Customer extends Person {

	@Basic(optional = false)
	@NotEmpty(message = "Il numero di telefono non puo' essere lasciato vuoto")
	private String mobileNumber;

	@ManyToOne
	private Customer recommendedBy;

	@OneToMany
	private List<Allergen> allergies;

	@ManyToMany
	private List<Reservation> reservations;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
}

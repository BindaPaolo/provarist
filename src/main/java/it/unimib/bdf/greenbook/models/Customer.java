package it.unimib.bdf.greenbook.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

@Entity
public class Customer extends Person {

	@Basic(optional = false)
	@NotEmpty(message = "Il numero di telefono non puo' essere lasciato vuoto")
	private String mobileNumber;

	@ManyToOne
	private Customer recommendedBy;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "CUSTOMER_ALLERGIES",
			joinColumns = @JoinColumn(name = "customer_id"),
			inverseJoinColumns = @JoinColumn(name = "allergen_id"))
	private Set<Allergen> allergies;

	@ManyToMany
	private List<Reservation> reservations;

	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (! (obj instanceof Customer)) {
			return false;
		}
		return this.getId() == ((Customer)obj).getId();
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Set<Allergen> getAllergies() {
		return allergies;
	}

	public void setAllergies(Set<Allergen> allergies) {
		this.allergies = allergies;
	}

}

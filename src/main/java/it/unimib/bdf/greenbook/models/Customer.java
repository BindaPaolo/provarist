package it.unimib.bdf.greenbook.models;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("C")
public class Customer extends Person {

	@Basic(optional = false)
	@NotEmpty(message = "Il numero di telefono non puo' essere lasciato vuoto")
	private String mobileNumber;

	@Email(message = "Inserire un indirizzo email valido")
	private String email;

	@OneToOne
	private Customer recommendedBy;

	@Column
	@OneToMany
	private List<Allergen> allergies;

	@Column
	@OneToMany
	private List<Reservation> reservations;
}

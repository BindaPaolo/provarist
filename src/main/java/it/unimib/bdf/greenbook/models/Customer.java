package it.unimib.bdf.greenbook.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@Data
@ToString
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="customer")
public class Customer extends Person {

	@Basic(optional = false)
	@NotEmpty(message = "Questo campo non puo' essere lasciato vuoto")
	private String mobileNumber;

	@ManyToOne
	@JoinColumn(name="recommended_by_id")
	private Customer recommendedById;
	
	@OneToMany(mappedBy = "recommendedById", cascade = CascadeType.ALL)
	private List<Customer> recommended;


	@OneToMany
	private List<Allergen> allergies;

	//@ManyToMany(mappedBy = "reservation_customers")
	//private List<Reservation> reservations;

}











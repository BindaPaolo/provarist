package it.unimib.bdf.greenbook.models;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@Entity
@Table(name="customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Basic(optional = false)
	@NotEmpty(message = "Il nome non puo' essere lasciato vuoto")
	private String firstName;

	@Basic(optional = false)
	@NotEmpty(message = "Il cognome non puo' essere lasciato vuoto")
	private String lastName;
	
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











package it.unimib.bdf.greenbook.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.FetchMode;

import org.springframework.format.annotation.DateTimeFormat;
import org.hibernate.annotations.Fetch;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
@Table(name = "reservation")
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_id")
	private long reservation_id;

	public enum shiftEnumType {
		LUNCH, DINNER
	}
	@Enumerated(EnumType.STRING)
	@NotNull(message="Seleziona il turno!")
	private shiftEnumType shiftEnum;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="Seleziona la data!")
	private LocalDate date;

	@OneToMany(fetch = FetchType.EAGER)
	private List<RestaurantTable> reservedTables;

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "reservation_customers",
			   joinColumns = { @JoinColumn(name = "reservation_id")},
			   inverseJoinColumns = {@JoinColumn(name = "customer_id")})
	@NotEmpty(message = "La lista dei  clienti non può essere vuota!")
	private List<Customer> reservation_customers = new ArrayList<Customer>();
	
	
	public void addReservationCustomer(Customer c) {
		this.reservation_customers.add(c);
	}
	
	public Customer addReservationCustomer() {
		Customer c = new Customer();
		this.reservation_customers.add(c);
		
		return c;
	}
}







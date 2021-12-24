package it.unimib.bdf.greenbook.models;

import javax.persistence.*;

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

	@Enumerated(EnumType.STRING)
	private shiftEnumType shiftEnum;
	public enum shiftEnumType {
		LUNCH, DINNER
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@OneToMany(fetch = FetchType.EAGER)
	private List<RestaurantTable> reservedTables;

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "reservation_customers",
			   joinColumns = { @JoinColumn(name = "reservation_id")},
			   inverseJoinColumns = {@JoinColumn(name = "customer_id")})
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







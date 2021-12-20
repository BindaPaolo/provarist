package it.unimib.bdf.greenbook.models;

import javax.persistence.*;

import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Fetch;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
@Entity
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

	@Basic
	@Temporal(TemporalType.DATE)
	private Date date;

	@OneToMany(fetch = FetchType.EAGER)
	private List<RestaurantTable> reservedTables;

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "reservation_customers",
			   joinColumns = { @JoinColumn(name = "reservation_id")},
			   inverseJoinColumns = {@JoinColumn(name = "customer_id")})
	private List<Customer> reservation_customers;
	
}







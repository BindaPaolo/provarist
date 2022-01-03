package it.unimib.bdf.greenbook.models;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservation")
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_id")
	private long reservation_id;

	public enum shiftEnumType {
		Pranzo, Cena
	}
	@Enumerated(EnumType.STRING)
	@NotNull(message="Seleziona il turno!")
	private shiftEnumType shiftEnum;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="Seleziona la data!")
	@FutureOrPresent(message="Data selezionata non valida!")
	private LocalDate date;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "reservation_customers",
			   joinColumns = { @JoinColumn(name = "reservation_id")},
			   inverseJoinColumns = {@JoinColumn(name = "customer_id")})
	@NotEmpty(message = "Inserisci almeno un cliente nella prenotazione!")
	private List<Customer> reservation_customers = new ArrayList<>();	
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(
			name = "reservation_waiters",
			joinColumns = @JoinColumn(name = "reservation_id"),
			inverseJoinColumns = @JoinColumn(name = "waiter_id"))
	@NotEmpty(message = "Seleziona almeno un cameriere dai seguenti:")
	private List<Employee> reservation_waiters;
	
	
	public long getReservation_id() {
		return reservation_id;
	}

	public void setReservation_id(long reservation_id) {
		this.reservation_id = reservation_id;
	}

	public void addReservationCustomer(Customer c) {
		this.reservation_customers.add(c);
	}
	
	/*
	public Customer addReservationCustomer() {
		Customer c = new Customer();
		this.reservation_customers.add(c);
		
		return c;
	}*/

	public shiftEnumType getShiftEnum() {
		return shiftEnum;
	}

	public void setShiftEnum(shiftEnumType shiftEnum) {
		this.shiftEnum = shiftEnum;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public List<Customer> getReservation_customers() {
		return reservation_customers;
	}

	public void setReservation_customers(List<Customer> reservation_customers) {
		this.reservation_customers = reservation_customers;
	}

	public List<Employee> getReservation_waiters() {
		return reservation_waiters;
	}

	public void setReservation_waiters(List<Employee> reservation_waiters) {
		this.reservation_waiters = reservation_waiters;
	}

	@Override
	public String toString() {
		return this.reservation_id + this.getReservation_customers().toString() ;
	}
}

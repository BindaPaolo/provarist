package it.unimib.bdf.greenbook.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
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

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "customer_allergies",
			joinColumns = @JoinColumn(name = "customer_id"),
			inverseJoinColumns = @JoinColumn(name = "allergen_id"))
	private Set<Allergen> allergies;

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

}











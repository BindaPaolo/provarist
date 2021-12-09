package it.unimib.bdf.greenbook.models;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("E")
public class Employee extends Person {

	@Basic(optional = false)
	@NotEmpty(message = "Il CF non puo' essere lasciato vuoto")
	private String cf;

	@Basic(optional = false)
	@NotEmpty(message = "Il ruolo non puo' essere lasciato vuoto")
	private String role;

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}

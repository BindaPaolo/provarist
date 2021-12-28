package it.unimib.bdf.greenbook.models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
@Table(name="employee")
public class Employee extends Person {

	@Basic(optional = false)
	@NotEmpty(message = "Il CF non puo' essere lasciato vuoto")
	private String cf;

	@Basic(optional = false)
	@NotEmpty(message = "Il ruolo non puo' essere lasciato vuoto")
	private String role;

}

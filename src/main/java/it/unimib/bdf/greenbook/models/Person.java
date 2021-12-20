package it.unimib.bdf.greenbook.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@MappedSuperclass
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="person_id")
	private long person_id;

	@Basic(optional = false)
	@NotEmpty(message = "Il nome non puo' essere lasciato vuoto")
	private String firstName;

	@Basic(optional = false)
	@NotEmpty(message = "Il cognome non puo' essere lasciato vuoto")
	private String lastName;

}

package it.unimib.bdf.greenbook.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class Allergen {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Basic(optional = false)
	@NotEmpty(message = "Il nome dell'allergene non puo' essere lasciato vuoto")
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

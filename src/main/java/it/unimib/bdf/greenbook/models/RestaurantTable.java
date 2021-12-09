package it.unimib.bdf.greenbook.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class RestaurantTable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Min(value = 2, message = "La capacità del singolo tavolo deve essere >=2")
    @Max(value = 8, message = "La capacità del singolo tavolo deve essere <=8")
	@NotEmpty(message = "Impostare una capacità di n persone per il tavolo!")
	private int capacity;

}

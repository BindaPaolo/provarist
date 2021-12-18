package it.unimib.bdf.greenbook.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
@Entity
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

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

}

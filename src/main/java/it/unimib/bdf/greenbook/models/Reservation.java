package it.unimib.bdf.greenbook.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Enumerated(EnumType.STRING)
	private shiftEnumType shiftEnum;
	public enum shiftEnumType {
		LUNCH, DINNER
	}

	@Basic
	@Temporal(TemporalType.DATE)
	private Date date;
	/*
	 * TO SET: temporalValues.setUtilDate(
  	 *           new SimpleDateFormat("yyyy-MM-dd").parse("2017-11-15"));
	 */

	@OneToMany
	private List<RestaurantTable> reservedTables;

	public shiftEnumType getShiftEnum() {
		return shiftEnum;
	}

	public void setShiftEnum(shiftEnumType shiftEnum) {
		this.shiftEnum = shiftEnum;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}

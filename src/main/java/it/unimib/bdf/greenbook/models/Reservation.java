package it.unimib.bdf.greenbook.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTimeStamp;
	/*
	 * TO SET: temporalValues.setUtilTimestamp( new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
	 * .parse("2017-11-15 15:30:14.332"));
	 */

	@OneToMany
	private List<RestaurantTable> reservedTables;

	@OneToMany
	private List<Customer> tableCompanions;

}

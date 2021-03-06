package it.unimib.bdf.greenbook.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@MappedSuperclass
public class Person implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Basic(optional = false)
    @NotEmpty(message = "Il nome non puo' essere lasciato vuoto")
    private String firstName;

    @Basic(optional = false)
    @NotEmpty(message = "Il cognome non puo' essere lasciato vuoto")
    private String lastName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	public Object clone() {
		try {
			return super.clone();
		}catch(Exception e){
			return null;
		}
	}
	
	
	
	


}

package it.unimib.bdf.greenbook.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
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

    public Object clone() {
    	try {
    		return super.clone();
    	}catch(Exception e) {
    		return null;
    	}
    }
}

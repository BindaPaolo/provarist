package it.unimib.bdf.greenbook.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
@ToString
@Table(name = "employee")
public class Employee extends Person {

    @Column(unique = true)
    @Basic(optional = false)
    @NotEmpty(message = "Il CF non puo' essere lasciato vuoto")
    private String cf;

    public enum roleEnumType {
        CapoChef, Chef, Cameriere, CapoSala
    }

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Seleziona il ruolo!")
    private roleEnumType role;

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Employee)) {
            return false;
        }
        return this.getId() == ((Employee) obj).getId();
    }

}

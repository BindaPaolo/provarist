package it.unimib.bdf.greenbook.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import java.util.Set;


@Entity
@Table(name = "customer")
public class Customer extends Person {

    @Column(unique=true)
    @NotEmpty(message = "Questo campo non puo' essere lasciato vuoto")
    @Pattern(regexp = "[0-9]*", message="Numero inserito non valido!")
    private String mobileNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recommended_by_id")
    private Customer recommendedBy;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "customer_allergies",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_id"))
    private Set<Allergen> allergies;
    

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Customer)) {
            return false;
        }
        return this.getId() == ((Customer) obj).getId();
    }

    @Override
    public String toString() {
    	
        return this.getId() + ", " + this.getFirstName() + ", " + this.getLastName()
                + ", " + this.getAllergies() + ", " + this.getMobileNumber();
    }


	public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Customer getRecommendedBy() {
        return recommendedBy;
    }

    public void setRecommendedBy(Customer recommendedBy) {
        this.recommendedBy = recommendedBy;
    }

    public Set<Allergen> getAllergies() {
        return allergies;
    }

    public void setAllergies(Set<Allergen> allergies) {
        this.allergies = allergies;
    }

}

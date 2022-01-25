package birthday_dater.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "dates")
public class BirthDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "birth_date")
    private Date dateString;

    @OneToMany( cascade = CascadeType.ALL,
                fetch = FetchType.EAGER)
    @JoinColumn(name = "date_id")
    private List<Person> personListD;

    public BirthDate() {
    }

    public BirthDate(Date dateString) {
        this.dateString = new java.sql.Date(dateString.getTime());
    }
    public void addPersonToBirthDate(Person person){
        if (personListD == null){
            personListD = new ArrayList<>();
        }
        personListD.add(person);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Date getDateString() {
        return new java.sql.Date(dateString.getTime());
    }

    public void setDateString(Date dateString) {
        this.dateString = dateString;
    }

    public List<Person> getPersonListD() {
        return personListD;
    }

    public void addPersonListD(Person person) {
        if (personListD == null) {
            personListD = new ArrayList<>();
        }
        personListD.add(person);
    }



    @Override
    public String toString() {
        return new java.sql.Date(dateString.getTime()).toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BirthDate)) return false;

        BirthDate birthDate = (BirthDate) o;

        return dateString.equals(birthDate.dateString);
    }

    @Override
    public int hashCode() {
        return dateString.hashCode();
    }

}

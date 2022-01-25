package birthday_dater.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "relations")
public class Relation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "relation_name")
    private String relationName;

    @OneToMany( cascade = CascadeType.ALL,
                fetch = FetchType.LAZY)
    @JoinColumn(name = "relation_id")
    private List<Person> personListR;

    public Relation() {
    }

    public Relation(String relationName) {
        this.relationName = relationName;
    }

    public void addPersonToRelation(Person person){
        if (personListR == null) {
            personListR = new ArrayList<>();
        }
        personListR.add(person);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public List<Person> getPersonListR() {
        return personListR;
    }

    public void setPersonListR(List<Person> personListR) {
        this.personListR = personListR;
    }

    @Override
    public String toString() {
        return  relationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Relation)) return false;

        Relation relation = (Relation) o;

        return relationName.equals(relation.relationName);
    }

    @Override
    public int hashCode() {
        return relationName.hashCode();
    }
}

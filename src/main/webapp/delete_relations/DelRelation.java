package delete_relations;

import birthday_dater.entity.BirthDate;
import birthday_dater.entity.Person;
import birthday_dater.entity.Relation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DelRelation {
    public static void main(String[] args) {

        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Person.class)
                .addAnnotatedClass(Relation.class)
                .addAnnotatedClass(BirthDate.class)
                .buildSessionFactory();


        Session session = null;

        try     {

            session = factory.getCurrentSession();
            session.beginTransaction();

            session.createQuery("delete from Relation").executeUpdate();

            session.getTransaction().commit();


        } finally {
            factory.close();
            session.close();
        }
    }
}

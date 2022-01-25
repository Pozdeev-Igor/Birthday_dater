package birthday_dater.logic_classes;

import birthday_dater.entity.BirthDate;
import birthday_dater.entity.Person;
import birthday_dater.entity.Relation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.*;

public class DeleteRelation {

    /**
     * Класс удаляет категорию из БД, если на нее больше не ссылается объект Person.
     */

    public void relationClear(){
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Person.class)
                .addAnnotatedClass(Relation.class)
                .addAnnotatedClass(BirthDate.class)
                .buildSessionFactory();
        Session session = null;
        try     {
            session = factory.getCurrentSession();
            session.beginTransaction();
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
            List<Relation> relations = session.createQuery("FROM Relation").list();
            for (Relation r : relations){
                if (r.getPersonListR().isEmpty()){
                    session.delete(r);
                }
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
            session.getTransaction().commit();
        } finally {
            factory.close();
            session.close();
        }
    }
}

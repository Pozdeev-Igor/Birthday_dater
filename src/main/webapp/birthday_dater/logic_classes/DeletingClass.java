package birthday_dater.logic_classes;

import birthday_dater.entity.BirthDate;
import birthday_dater.entity.Person;
import birthday_dater.entity.Relation;
import interfaces.Actionable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class DeletingClass implements Actionable {

    /**
     * Этот класс удаляет:
     * объект с именем и фамилией человека.
     * Запрос из БД происходит по имени.
     * Выводится на экран список людей с запрошенным именем и пользователь
     * может удалить ненужный объект из БД выбрав его по id
     */

    @Override
    public void action() {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Person.class)
                .addAnnotatedClass(Relation.class)
                .addAnnotatedClass(BirthDate.class)
                .buildSessionFactory();

        Session session = null;
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            System.out.println("Введите имя человека, которого нужно удалить из списка.");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String str = reader.readLine();

            List<Object[]> persons = session.createQuery("select id, firstName, lastName from Person WHERE firstName LIKE :str").setParameter("str", str).list();

            if (persons.isEmpty()) {
                System.out.println("\n---------------------------------------------------");
                System.out.println("В списке нет людей с таким именем");
                return;

            } else {
                for (Object[] p : persons) {
                    System.out.println("\n\n" + Arrays.toString(p));
                }

                System.out.println("------------------------------------------");
                System.out.println("\nВыберите id:");
                int id = Integer.parseInt(reader.readLine());

                Person person = null;
                try {
                person = session.get(Person.class, id);
                System.out.println(person.toString());
                session.delete(person);

                }catch (NullPointerException e) {
                    System.out.println("\nБыл введен некорректный id.");
                    return;
                }

                System.out.println("\nУдалено из списка.\n");
            }

            session.getTransaction().commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            factory.close();
            session.close();
        }
    }

    @Override
    public void pressEnterToContinue() {
        Actionable.super.pressEnterToContinue();
    }
}

package birthday_dater.logic_classes;

import birthday_dater.entity.BirthDate;
import birthday_dater.entity.Person;
import birthday_dater.entity.Relation;
import interfaces.Actionable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainPage implements Actionable {

    /**
     * Класс отображает главную страницу программы:
     *      - сегодняшнюю дату;
     *      - у кого из списка сегодня день рождения
     *      - троих людей из списка чей день рождения будет следующим
     *      - меню команд программы
     */

    @Override
    public void action() {
        System.out.println("\n\nВас приветствует Поздравлятор!\n");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DateFormat todayFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date today = new Date();
        System.out.println( "---------------------\n" +
                "|Сегодня: " + todayFormat.format(today) + "|\n" +
                "---------------------");

        Format formatter = new SimpleDateFormat("dd.MM");
        String s = formatter.format(today);
        String[] array = s.split("\\.");
        s = array[0]+array[1];
        List<BirthDate> birthDates;

        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Person.class)
                .addAnnotatedClass(Relation.class)
                .addAnnotatedClass(BirthDate.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
                session.beginTransaction();

            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            Query query = session.createQuery("FROM BirthDate WHERE DATE_FORMAT(dateString, '%d%m') =:s ").setParameter("s", s);
            birthDates = query.list();

            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            if (birthDates.isEmpty()) {
                System.out.println("\nСегодня ни у кого из списка нет дня рождения\n");
            }

            if (birthDates.size() == 1) {
                for (int i = 0; i < birthDates.size(); i++) {
                    for (Person person : birthDates.get(i).getPersonListD()) {
                        String name = person.getFirstName();
                        String surname = person.getLastName();
                        Relation relation = person.getRelation();
                        System.out.println("\n\nСегодня празднует день рождения " + relation + " " + name + " " + surname);
                    }
                }
            }

            if  (birthDates.size() > 1) {
                System.out.println("\n\nСегодня празднуют день рождения: ");
                for (int i = 0; i < birthDates.size(); i++){
                    for (Person person : birthDates.get(i).getPersonListD()){
                        String name = person.getFirstName();
                        String surname = person.getLastName();
                        Relation relation = person.getRelation();
                        System.out.println(relation + " " + name + " " + surname);
                    }
                }
            }

            Thread.sleep(3000);
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            String sss = array[1]+array[0];

            query = session.createQuery("FROM BirthDate WHERE DATE_FORMAT(dateString, '%m%d') >:sss ORDER BY MONTH(dateString)*100 + DAY(dateString)").setParameter("sss", sss);
            query.setMaxResults(3);

            List<BirthDate> dates = query.list();
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("\nУ кого день рождения будет следующим:\n");
            for (int i = 0; i < dates.size(); i++) {
                for (Person people : dates.get(i).getPersonListD()) {
                    String name = people.getFirstName();
                    String surname = people.getLastName();
                    Relation relation = people.getRelation();
                    System.out.println(relation + "     " + name + " " + surname + "    (" + dates.get(i) + ")");
                }
            }

            session.getTransaction().commit();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pressEnterToContinue() {
        Actionable.super.pressEnterToContinue();
        return;
    }
}

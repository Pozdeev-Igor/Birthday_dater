package birthday_dater.logic_classes;

import birthday_dater.entity.BirthDate;
import birthday_dater.entity.Person;
import birthday_dater.entity.Relation;
import interfaces.Actionable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddingClass implements Actionable {

    /**
     * Этот класс добавляет:
     *  1. Отношение человека к пользователю (друг/родственник/коллега и т.д.)
     *      При добавлении отношения проверяет: нет ли в БД уже отношения с таким именем.
     *          если есть, то использует объект из БД
     *          если нет - добавляет в БД новый объект с именем, которое ввел пользователь
     *  2. Дату дня рождения
     *
     *  3. Имя и фамилию пользователя (Все три объекта взаимосвязаны)
     */

    @Override
    public void action() {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Person.class)
                .addAnnotatedClass(Relation.class)
                .addAnnotatedClass(BirthDate.class)
                .buildSessionFactory();
        Session session = null;
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();

            print();

            String stringRelation = reader.readLine();

            while (stringRelation.matches("\\d*")
                    || stringRelation.matches("\\d*\\.\\d*")){

                System.out.println("Ошибка ввода. Введите текст\n");
                print();
                stringRelation = reader.readLine();
            }

            if (stringRelation.matches("cancel")) {
                return;
            }

            List<String> rows = session.createQuery("select relationName from Relation" +
                            " where relationName = :stringRelation")
                    .setParameter("stringRelation", stringRelation).list();
            Relation relation = null;

            if (!rows.isEmpty()) {                                                              //Выполняется проверка на наличие объекта в БД
                int id = 0;                                                                     //Если есть - использовать объект из БД
                List<Integer> integers = session.createQuery("select id from Relation " +       //Если нет - создать новый объект
                                "where relationName = :stringRelation")
                        .setParameter("stringRelation", stringRelation).list();
                for (int i : integers) {
                    id = i;
                }
                relation = session.get(Relation.class, id);
            } else {
                relation = new Relation(stringRelation);
            }

            System.out.println("\n------------------------------------------------------" +
                    "\nВведите дату рождения в формате: дд.мм.гггг или \'cancel\' чтобы отменить\n" +
                    "------------------------------------------------------");
            String stringBirth = reader.readLine();
            if (stringBirth.matches("cancel")) {
                return;
            }

            while (!stringBirth.matches("^([0-2][0-9]||3[0-1])\\.(0[0-9]||1[0-2])\\." +
                    "([0-9][0-9])?[0-9][0-9]$")) {
                System.out.println("Ошибка ввода. Введите дату заново в формате: дд.мм.гггг\n");
                stringBirth = reader.readLine();
            }

            DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            Date date = format.parse(stringBirth);
            Date today = new Date();

            while (date.getTime() > today.getTime()) {
                System.out.println("В будущее нельзя! Введите дату заново.");
                stringBirth = reader.readLine();
            }

            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            BirthDate birthDate = new BirthDate(sqlDate);

            System.out.println("\n------------------------------------------------------" +
                    "\nВведите имя и фамилию или \'cancel\' чтобы отменить\n" +
                    "------------------------------------------------------");
            String stringPerson = reader.readLine();

            while (stringPerson.matches("\\d*")
                    || stringPerson.matches("\\d*\\s\\d*")) {
                System.out.println("Введите корректные имя и фамилию.");
                stringPerson = reader.readLine();
            }

            if (stringPerson.matches("cancel")){
                return;
            }

            String[] strings = stringPerson.split(" ");

            Person person1 = new Person(strings[0], strings[1]);

            relation.addPersonToRelation(person1);
            birthDate.addPersonToBirthDate(person1);

            session.save(relation);
            session.save(birthDate);
            session.getTransaction().commit();

            System.out.println("\n------------------------------------------------------\n" +
                    person1.getFirstName() + " добавлен(а) в базу данных\n" +
                    "------------------------------------------------------");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            factory.close();
            session.close();
        }
    }

    @Override
    public void pressEnterToContinue() {
        Actionable.super.pressEnterToContinue();
    }

    static void print() {
        System.out.println("\n------------------------------------------------------" +
                "\nВведите категорию (друг/коллега/родственник и т.п. или \'cancel\' чтобы отменить)\n" +
                "------------------------------------------------------");
    }
}

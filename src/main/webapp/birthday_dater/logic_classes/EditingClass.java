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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EditingClass implements Actionable {

    /**
     * Этот класс корректирует данные в БД:
     * Корректировать за один раз можно или имя или фамилию или дату рождения.
     * Если нужно корректировать сразу несколько полей, то операцию нужно проделывать последовательно
     * (например: сперва имя, потом фамилию)
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

            System.out.println( "\n------------------------------------------------------------------" +
                    "\nВведите имя человека, данные по которому хотите отредактировать\n" +
                    "------------------------------------------------------------------");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));
            String str = reader.readLine();

            List<Object[]> persons = session.createQuery(   "select id, firstName, lastName " +
                            "from Person WHERE firstName LIKE :str")
                    .setParameter("str", str).list();
            if (persons.isEmpty()) {
                System.out.println( "\n----------------------------------" +
                        "\nВ списке нет людей с таким именем\n" +
                        "----------------------------------");
                return;
            }
            for (Object[] p : persons) {
                System.out.println("\n" + Arrays.toString(p));
            }
            System.out.println( "------------------------------------------------------------");
            System.out.println( "\nВыберите id:\n");

            int id = Integer.parseInt(reader.readLine());

            Person person = session.get(Person.class, id);
            System.out.println(person.toString());

                printCommands();

                str = reader.readLine();

            switch (str) {
                case "name":
                    System.out.println("Введите новое имя");
                    str = reader.readLine();
                    person.setFirstName(str);
                    printEdited();
                    break;
                case "surname":
                    System.out.println("Введите новую фамилию");
                    str = reader.readLine();
                    person.setLastName(str);
                    printEdited();
                    break;
                case "date":
                    System.out.println("Введите новую дату рождения в формате: дд.мм.гггг");
                    str = reader.readLine();
                    BirthDate birthDate = session.get(BirthDate.class, person.getBirthDate().getId());
                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    Date date = dateFormat.parse(str);
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    birthDate.setDateString(sqlDate);
                    printEdited();
                    break;
                    default:
                        System.out.println("Ошибка ввода: неизвестная команда.");
                        break;
            }

            session.getTransaction().commit();

        }catch (Exception e){
            System.out.println("\nБыл введен некорректный id.");
        }        finally {
            factory.close();
            session.close();
        }
    }

    @Override
    public void pressEnterToContinue() {
        Actionable.super.pressEnterToContinue();
    }

    static void printEdited(){
        System.out.println( "--------------------------\n" +
                "Данные успешно обновлены\n" +
                "--------------------------\n");
    }
    static void printCommands(){
        System.out.println( "\n-----------------------------------\n" +
                "Что будем редактировать?\n" +
                "name       - изменить имя\n" +
                "surname    - изменить фамилию\n" +
                "date       - изменить дату рождения\n" +
                "-----------------------------------");
    }
}

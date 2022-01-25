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
import java.util.List;

public class ShowingClass implements Actionable {

    /**
     * Этот класс ответственный за отображение списка людей из базы данных
     * Может отображать:
     *      - весь список людей
     *      - сгруппированный список по категориям (друг/родственник/коллега и т.д.)
     *      - сгруппированный список по датам в формате дд.ММ (дату необходимо вводить с клавиатуры)
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

            printCommands();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));
            String str = reader.readLine();

            while (!str.matches("[ard]")) {
                System.out.println("Ошибка ввода: неизвестная команда.");
                str = reader.readLine();
            }
            List<Person> people = null;

            switch (str) {
                case "a":
                    System.out.println("---------------------------------------------------------------------------------------------------------------------------------");
                    people = session.createQuery("FROM Person").list();
                    if (people.isEmpty()){
                        printNoData();
                    }else {
                        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
                        for (Person p : people) {
                            System.out.println(p);
                        }
                    }
                    break;
                case "r":
                    System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
                    List<Relation> relations = session.createQuery("FROM Relation").list();
                    System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
                    if (relations.isEmpty()) {
                        System.out.println("Данных пока нет. Нужно добавить данные");
                        return;
                    }
                    System.out.println("\nВыберите id категории:");
                    for (Relation r : relations) {
                        System.out.println("id =" + r.getId() + "   " + r);
                    }
                    int cat = Integer.parseInt(reader.readLine());
                    System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
                    relations = session.createQuery("FROM Relation WHERE id = :cat").setParameter("cat", cat).list();
                    for (Relation r : relations) {
                        for (int i = 0; i < r.getPersonListR().size(); i++) {
                            System.out.println(r.getPersonListR().get(i).toString());
                        }
                    }
                    break;
                case "d":
                    System.out.println("Введите дату дня рождения в формате: дд.ММ");
                    str = reader.readLine();
                    String[] arr = str.split("\\.");
                    str = arr[0]+arr[1];

                    List<BirthDate> birthDates = session.createQuery("FROM BirthDate WHERE DATE_FORMAT(dateString, '%d%m') =:str").setParameter("str", str).list();
                    System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
                    if (birthDates.isEmpty()){
                        System.out.println("\n\nСписок пуст. Попробуйте другой день.");
                        break;
                    }

                    else {
                        System.out.println("\n\n");
                        for (BirthDate b : birthDates) {
                            for (int i = 0; i < b.getPersonListD().size(); i++) {
                                System.out.println(b.getPersonListD().get(i).toString());
                            }
                        }
                    }
            }
        }catch (IOException e){
            System.out.println(e);
        }
        finally {
            factory.close();
            session.close();
        }
    }

    @Override
    public void pressEnterToContinue() {
        Actionable.super.pressEnterToContinue();
    }

    static void printCommands(){
        System.out.println( "\n\nсписок доступных команд:\n" +
                "-------------------------------------\n" +
                " a - показать весь список;\n" +
                " r - сгруппировать по категориям; \n" +
                " d - сгруппировать по датам\n" +
                "-------------------------------------");
    }

    static void printNoData(){
        System.out.println( "------------------------------------------" +
                "\nНет ни одной записи.\n" +
                "------------------------------------------");
    }
}

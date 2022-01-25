package birthday_dater;

import birthday_dater.logic_classes.*;
import my_enum.OptionsToDo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.TimeZone;


public class Main {
    public static void main(String[] args) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "bestuser", "bestuser");
            Statement statement = connection.createStatement();
            String sql1 = "CREATE TABLE birthday_list.relations(\n" +
                    "id int NOT NULL AUTO_INCREMENT,\n" +
                    "relation_name  varchar(30),\n" +
                    "CONSTRAINT UNIQUE (relation_name),\n" +
                    "PRIMARY KEY (id));\n";

            String sql2 = "CREATE TABLE birthday_list.dates(\n" +
                    "id int NOT NULL AUTO_INCREMENT,\n" +
                    "birth_date DATE, \n" +
                    "PRIMARY KEY (id));\n";

            String sql3 = "CREATE TABLE birthday_list.people (\n" +
                    "  id int NOT NULL AUTO_INCREMENT,\n" +
                    "  first_name varchar(20),\n" +
                    "  last_name varchar(30),\n" +
                    "  date_id int,\n" +
                    "  relation_id int,\n" +
                    "  PRIMARY KEY (id),\n" +
                    "  FOREIGN KEY (relation_id) REFERENCES birthday_list.relations(id),\n" +
                    "  FOREIGN KEY (date_id) REFERENCES birthday_list.dates(id));";
            statement.executeUpdate(sql1);
            statement.executeUpdate(sql2);
            statement.executeUpdate(sql3);
        } catch (SQLException e) {
            System.out.println("База данных уже создана. Перехвачено исключение: " +e);;
        }

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        MainPage main = new MainPage();
        Commands lines = new Commands();
        DeleteRelation deleteRelation = new DeleteRelation();
        main.action();
        main.pressEnterToContinue();
        lines.action();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine())
        {
            String s = scanner.nextLine();

            if (s.equals(OptionsToDo.MAIN.name().toLowerCase())) {
                main.action();
                main.pressEnterToContinue();
                lines.action();
                continue;
            }

            if (s.equals(OptionsToDo.ADD.name().toLowerCase())) {
                AddingClass add = new AddingClass();
                add.action();
                add.pressEnterToContinue();
                lines.action();
                continue;
            }
            if (s.equals(OptionsToDo.DELETE.name().toLowerCase())){
                DeletingClass delete = new DeletingClass();
                delete.action();
                delete.pressEnterToContinue();
                lines.action();
                continue;
            }
            if (s.equals(OptionsToDo.EDIT.name().toLowerCase())){
                 EditingClass edit = new EditingClass();
                 edit.action();
                 edit.pressEnterToContinue();
                lines.action();
                continue;
            }
            if (s.equals(OptionsToDo.SHOW.name().toLowerCase())){
                deleteRelation.relationClear();
                ShowingClass show = new ShowingClass();
                show.action();
                show.pressEnterToContinue();
                lines.action();
                continue;
            }
            if (s.equals(OptionsToDo.EXIT.name().toLowerCase())){
                new ExitClass().action();
                break;
            }
            else {
                System.out.println("Ошибка: Не существует команды \"" + s +"\"");
            }


        }scanner.close();
    }
}

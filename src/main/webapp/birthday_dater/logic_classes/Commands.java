package birthday_dater.logic_classes;

import interfaces.Actionable;

public class Commands implements Actionable {

    /**
     * Этот класс отображает меню команд.
     */

    @Override
    public void action() {

        System.out.println( "\n\n--------------------------------------------------------------");
        System.out.println( "show   - чтобы вывести весь список дней рождения \n" +
                "edit   - чтобы редактировать данные в списке \n" +
                "delete - чтобы удалить данные из списка \n" +
                "add    - чтобы добавить новый день рождения \n" +
                "exit   - для выхода из программы \n" +
                "main   - главное меню");
        System.out.println("--------------------------------------------------------------");

    }
}

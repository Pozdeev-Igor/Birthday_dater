package birthday_dater.logic_classes;

import interfaces.Actionable;

public class ExitClass implements Actionable {
    /**
     * Завершает работу программы
     */

    @Override
    public void action() {
        System.out.println("Программа завершена.");

        return;
    }
}

package interfaces;

public interface Actionable {

    public void action();

    public default void pressEnterToContinue() {

        System.out.println("\n-----------------------------------");
        System.out.println("Нажмите ENTER чтобы продолжить");
        System.out.println("-----------------------------------");
        try
        {
            System.in.read();
        }
        catch(Exception e)
        {}
    }
}

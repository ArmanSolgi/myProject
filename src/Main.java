import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            Scanner scanner = new Scanner(System.in);
            CommandHandler commandHandler = new CommandHandler();

            System.out.println("Start:");

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();

                if (input.equals("exit")) {
                    System.out.println("finished!");
                    break;
                }
                commandHandler.handleCommand(input);
            }
            scanner.close();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
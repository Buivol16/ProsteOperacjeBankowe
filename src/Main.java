import java.util.Scanner;
import login.LoginHandler;
import register.RegisterHandler;

public class Main {
  public static void main(String[] args) {
    var scanner = new Scanner(System.in);
    while (true) {
      System.out.println("Choose the way: \n1. Login\n2. Register");
      String answer = scanner.nextLine();
      try {
        if (answer.equals("1")) LoginHandler.run();
        else if (answer.equals("2")) RegisterHandler.run();
        else
          throw new IllegalArgumentException(
              "You wrote a wrong argument. You need to write only \"1\" or \"2\" in that case.");
      } catch (Exception e) {
        System.err.println(e.getMessage());
        System.out.println("Let's try again.\n\n");
      }
    }
  }
}

package register;

import static assets.ArgumentChecker.writeForArgument;
import static assets.Flags.COUNTRY;
import static assets.Flags.NOT_LONG;
import static assets.Flags.NOT_SHORT;
import static assets.Flags.ONLY_LETTERS;
import static assets.Flags.ONLY_NUMBERS;
import static assets.Flags.PESEL;

import assets.Country;
import balance.Balance;
import balance.db.BalanceDbHandler;
import exceptions.UserExistsException;
import user.User;
import user.db.UserDbHandler;

public class RegisterHandler {

  public static void run() {
    System.out.println("Let's create a new account in the system.");
    String pesel = writePesel();
    String password = writePassword();
    String name = writeName();
    String surname = writeSurname();
    Country country = Country.valueOf(writeCountry());
    String street = String.format("[%s]", writeStreet());
    var user = new User(null, pesel, password, name, surname, country, street);
    register(user);
  }

  private static void register(User user) {
    try {
      var registered = UserDbHandler.saveUser(user);
      registerBalance(registered);
    } catch (Exception e) {
    }
  }

  private static Balance registerBalance(User user) {
    var balance = new Balance(null, user, null, 100F);
    return BalanceDbHandler.saveBalance(balance);
  }

  private static String writeName() {
    String afterExceptionMessage = "Type your full name again below\nExample: Denys";

    System.out.println("Type your full name below:");

    while (true) {
      try {
        var argument = writeForArgument(ONLY_LETTERS, NOT_SHORT, NOT_LONG);
        return argument;
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println(afterExceptionMessage);
      }
    }
  }

  private static String writeSurname() {
    String afterExceptionMessage = "Type your surname again\nExample: Khmara";

    System.out.println("Now type your surname:");

    while (true) {
      try {
        var argument = writeForArgument(ONLY_LETTERS, NOT_SHORT, NOT_LONG);
        return argument;
      } catch (Exception e) {
        System.err.println(e.getMessage());
        System.out.println(afterExceptionMessage);
      }
    }
  }

  private static String writePesel() {
    String afterExceptionMessage = "Type your PESEL again\nExample: 81010200131";

    System.out.println("Now type your PESEL:");

    while (true) {
      try {
        var argument = writeForArgument(PESEL, ONLY_NUMBERS);
        if (UserDbHandler.checkPesel(argument))
          throw new UserExistsException("This PESEL is already exist");
        return argument;
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println(afterExceptionMessage);
      }
    }
  }

  private static String writePassword() {
    String afterExceptionMessage = "Type your password again";
    System.out.println("Now type your password:");

    while (true) {
      try {
        var argument = writeForArgument(NOT_SHORT, NOT_LONG);
        return argument;
      } catch (Exception e) {
        System.err.println(e.getMessage());
        System.out.println(afterExceptionMessage);
      }
    }
  }

  private static String writeCountry() {
    String afterExceptionMessage = "Type your country again\nExample: Poland";

    System.out.println("Now type your country:");

    while (true) {
      try {
        var argument = writeForArgument(ONLY_LETTERS, NOT_SHORT, NOT_LONG, COUNTRY);
        return argument;
      } catch (Exception e) {
        System.err.println(e.getMessage());
        System.out.println(afterExceptionMessage);
      }
    }
  }

  private static String writeStreet() {
    String afterExceptionMessage = "Type your street again\nExample: Lakewood street, 57/2";

    System.out.println("Now type your street where you live:");

    while (true) {
      try {
        var argument = writeForArgument(NOT_SHORT, NOT_LONG);
        if (argument.contains("'"))
          throw new IllegalArgumentException("Wrong format. Argument must not contains ' symbol");
        return argument;
      } catch (Exception e) {
        System.err.println(e.getMessage());
        System.out.println(afterExceptionMessage);
      }
    }
  }
}

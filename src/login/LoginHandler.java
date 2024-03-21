package login;

import static assets.Flags.NOT_LONG;
import static assets.Flags.NOT_SHORT;
import static assets.Flags.ONLY_NUMBERS;

import assets.Flags;
import balance.Balance;
import balance.db.BalanceDbHandler;
import exceptions.BalanceNotFoundException;
import exceptions.NotEnoughMoneyException;
import exceptions.UserNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import transactions.db.TransactionDbHandler;
import user.User;
import user.db.UserDbHandler;

public class LoginHandler {

  public static void run() {
    User user = null;
    while (user == null) {
      String username = writeUsername();
      String password = writePassword();
      var userInputProjection =
          UserInputProjection.builder().username(username).password(password).build();
      try {
        user = login(userInputProjection);
      } catch (UserNotFoundException e) {
        e.printStackTrace();
        return;
      }
    }
    System.out.println("You are successfully logged in.");
    while (true) {
      System.out.println(
          "1. About me\n2. Make transaction\n3. My bank account\n4. Transactions history\n5. Logout");
      var scanner = new Scanner(System.in);
      String answer = scanner.nextLine();
      try {
        if (answer.equals("1")) {
          aboutMe(user);
        } else if (answer.equals("2")) {
          makeTransaction(user);
        } else if (answer.equals("3")) {
          myBankAccount(user);
        } else if (answer.equals("4")) {
          transactionsHistory(user);
        } else if (answer.equals("5")) {
          break;
        } else {
          throw new IllegalArgumentException(
              "You wrote a wrong argument. You need to write only \"1\", \"2\", \"3\", or \"4\" in that case.");
        }
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Let's try again.\n\n");
      }
    }
  }

  private static void transactionsHistory(User user) throws UserNotFoundException {
    var bankAccount = BalanceDbHandler.findBalanceByUser(user);
    var history = TransactionDbHandler.findHistoryByBalance(bankAccount);
    System.out.println();
    for (var obj : history) {
      System.out.printf(
          """
    Id: %s
    From: %s
    To: %s
    Sum: %s
    At: %s

    """,
          obj.getId(),
          obj.getFrom().getAccount_number(),
          obj.getTo().getAccount_number(),
          obj.getSum(),
          obj.getAt().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm:ss")));
    }
    System.out.println();
  }

  private static Balance makeTransaction(User user) {
    try {
      var scanner = new Scanner(System.in);
      var bankAccount = BalanceDbHandler.findBalanceByUser(user);
      System.out.println("Type account number of recipient");
      String accountNum = writeForArgument(NOT_SHORT, NOT_LONG, ONLY_NUMBERS);
      System.out.println("Type sum you want to send");
      var sum = scanner.nextFloat();
      if (bankAccount.getBalance() < sum)
        throw new NotEnoughMoneyException("You have not enough money to transfer");
      var recipientAccount = BalanceDbHandler.findBalanceByAccountNumber(accountNum);
      BalanceDbHandler.transferFunds(bankAccount, recipientAccount, sum);
    } catch (BalanceNotFoundException | UserNotFoundException | NotEnoughMoneyException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static void myBankAccount(User user) {
    try {
      var bankAccount = BalanceDbHandler.findBalanceByUser(user);
      System.out.printf(
          """

Account number: %s
Balance: %s

""",
          bankAccount.getAccount_number(), bankAccount.getBalance());
    } catch (UserNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static void aboutMe(User user) {
    System.out.printf(
        """

                    Username: %s
                    Name: %s
                    Surname: %s
                    Country: %s
                    Street: %s

                    """,
        user.getUsername(),
        user.getName(),
        user.getSurname(),
        user.getCountry().name(),
        user.getStreet());
  }

  private static User login(UserInputProjection userInputProjection) throws UserNotFoundException {
    return UserDbHandler.login(userInputProjection);
  }

  private static String writeUsername() {
    String afterExceptionMessage = "Type your username again\nExample: Buivol16";

    System.out.println("Username:");

    while (true) {
      try {
        var argument = writeForArgument(NOT_SHORT, NOT_LONG);
        return argument;
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println(afterExceptionMessage);
      }
    }
  }

  private static String writePassword() {
    String afterExceptionMessage = "Type your password again";
    System.out.println("Password:");

    while (true) {
      try {
        var argument = writeForArgument(NOT_SHORT, NOT_LONG);
        return argument;
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println(afterExceptionMessage);
      }
    }
  }

  private static String writeForArgument(Flags... flags) throws IllegalArgumentException {
    var scanner = new Scanner(System.in);
    String argument;
    var list = Arrays.asList(flags);
    argument = scanner.nextLine();
    if (list.contains(NOT_SHORT) && argument.length() < 2)
      throw new IllegalArgumentException("The argument must not be quite short");
    if (list.contains(NOT_LONG) && argument.length() > 64)
      throw new IllegalArgumentException("The argument must not be quite long");
    if (list.contains(ONLY_NUMBERS) && argument.matches("\\D+"))
      throw new IllegalArgumentException("The argument must contains only numbers");
    return argument;
  }
}

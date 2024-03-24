package login;

import static assets.ArgumentChecker.writeForArgument;
import static assets.Flags.NOT_LONG;
import static assets.Flags.NOT_SHORT;
import static assets.Flags.ONLY_NUMBERS;
import static assets.Flags.PESEL;

import balance.Balance;
import balance.db.BalanceDbHandler;
import exceptions.BalanceNotFoundException;
import exceptions.NotEnoughMoneyException;
import exceptions.UserNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import transactions.db.TransactionDbHandler;
import user.User;
import user.db.UserDbHandler;

public class LoginHandler {

  public static void run() {
    User user = null;
    while (user == null) {
      String pesel = writePesel();
      String password = writePassword();
      var userInputProjection =
          UserInputProjection.builder().pesel(pesel).password(password).build();
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
        if (answer.equals("1")) aboutMe(user);
        else if (answer.equals("2")) makeTransaction(user);
        else if (answer.equals("3")) myBankAccount(user);
        else if (answer.equals("4")) transactionsHistory(user);
        else if (answer.equals("5")) break;
        else
          throw new IllegalArgumentException(
              "You wrote a wrong argument. You need to write only \"1\", \"2\", \"3\", or \"4\" in that case.");
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Let's try again.\n\n");
      }
    }
  }

  private static void transactionsHistory(User user) throws UserNotFoundException {
    var bankAccount = BalanceDbHandler.findBalanceByUser(user);
    var fromDate = writeDateFrom();
    var toDate = writeDateTo();

    var history =
        TransactionDbHandler.findHistoryByBalanceBetweenDates(bankAccount, fromDate, toDate);
    System.out.println();
    if (history == null || history.length == 0) System.out.println("There is no history...");
    else {
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
    }
    System.out.println();
  }

  private static LocalDate writeDateFrom() {
    var scanner = new Scanner(System.in);
    var dateFrom = LocalDate.MIN;
    try {
      System.out.println(
          "Write a date from which you want to see a history or leave empty row. Example: 21/03/2024");
      var answer = scanner.nextLine();
      if (answer.equals("")) return dateFrom;
      dateFrom = LocalDate.parse(answer, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      return dateFrom;
    } catch (Exception e) {
      e.printStackTrace();
      return writeDateFrom();
    }
  }

  private static LocalDate writeDateTo() {
    var scanner = new Scanner(System.in);
    var dateTo = LocalDate.MAX;
    try {
      System.out.println(
          "Write a date to which you want to see a history or leave empty row. Example: 21/03/2024");
      var answer = scanner.nextLine();
      if (answer.equals("")) return dateTo;
      dateTo = LocalDate.parse(answer, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      return dateTo;
    } catch (Exception e) {
      e.printStackTrace();
      return writeDateFrom();
    }
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

                    PESEL: %s
                    Name: %s
                    Surname: %s
                    Country: %s
                    Street: %s

                    """,
        user.getPesel(),
        user.getName(),
        user.getSurname(),
        user.getCountry().name(),
        user.getStreet());
  }

  private static User login(UserInputProjection userInputProjection) throws UserNotFoundException {
    return UserDbHandler.login(userInputProjection);
  }

  private static String writePesel() {
    String afterExceptionMessage = "Type your PESEL again\nExample: 81010200131";

    System.out.println("PESEL:");

    while (true) {
      try {
        var argument = writeForArgument(PESEL, ONLY_NUMBERS);
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
}

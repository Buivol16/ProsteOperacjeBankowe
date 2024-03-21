package balance.db;

import static assets.consts.DBFiles.BALANCE_DB;

import assets.FileManager;
import balance.Balance;
import balance.BalanceParser;
import exceptions.BalanceNotFoundException;
import exceptions.BlankFileException;
import exceptions.UserNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import transactions.Transaction;
import transactions.db.TransactionDbHandler;
import user.User;

public class BalanceDbHandler {
  private static BalanceParser balanceParser = new BalanceParser();

  private BalanceDbHandler() {}

  public static Balance[] findAll() {
    try {
      var strObjects = FileManager.readInFile(BALANCE_DB).split("\n");
      if (!strObjects[0].equals("")) return balanceParser.parseObjects(strObjects);
      else return null;
    } catch (BlankFileException e) {
      System.err.println(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public static Balance saveBalance(Balance balance) {
    var balanceId = findFreeId();
    var accountId = findFreeAccountNumber();
    balance.setId(balanceId);
    balance.setAccount_number(accountId);
    var toWrite = balance.toString();
    if (balanceId != 1) toWrite = "\n" + toWrite;
    FileManager.writeToFile(BALANCE_DB, toWrite.getBytes(StandardCharsets.UTF_8));
    return balance;
  }

  public static Balance transferFunds(Balance from, Balance to, float sum) {
    try {
      var fromClone = from.clone();
      var toClone = to.clone();
      fromClone.setBalance(from.getBalance() - sum);
      FileManager.rewriteToFile(BALANCE_DB, from.toString(), fromClone.toString());
      toClone.setBalance(to.getBalance() + sum);
      FileManager.rewriteToFile(BALANCE_DB, to.toString(), toClone.toString());
      var transaction = new Transaction(null, from, to, sum, LocalDateTime.now());
      TransactionDbHandler.saveTransaction(transaction);
    } catch (BlankFileException | CloneNotSupportedException e) {
      return null;
    }
    return from;
  }

  public static Balance findBalanceByUser(User user) throws UserNotFoundException {
    return Arrays.stream(findAll())
        .filter(balance -> balance.getOwner().equals(user))
        .findFirst()
        .orElseThrow(() -> new UserNotFoundException("User not found"));
  }

  public static Balance findBalanceByAccountNumber(String accountNumber)
      throws BalanceNotFoundException {
    return Arrays.stream(findAll())
        .filter(balance -> balance.getAccount_number().equals(accountNumber))
        .findFirst()
        .orElseThrow(() -> new BalanceNotFoundException("Recipient balance is not found"));
  }

  private static Long findFreeId() {
    var all = findAll();
    if (all != null) return (long) all.length + 1;
    else return 1L;
  }

  private static String findFreeAccountNumber() {
    var all = findAll();
    var accountNumber = 10000;
    if (all == null) return String.valueOf(accountNumber);
    else {
      var random = new Random();
      while (true) {
        var randomAccountNumber = random.nextInt(10000, 99999);
        if (!Arrays.stream(all)
            .anyMatch((obj) -> Integer.parseInt(obj.getAccount_number()) == randomAccountNumber)) {
          accountNumber = randomAccountNumber;
          break;
        }
      }
    }
    return String.valueOf(accountNumber);
  }
}

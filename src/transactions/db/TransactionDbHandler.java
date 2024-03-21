package transactions.db;

import static assets.consts.DBFiles.TRANSACTIONS_DB;

import assets.FileManager;
import balance.Balance;
import exceptions.BlankFileException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import transactions.Transaction;
import transactions.TransactionParser;

public class TransactionDbHandler {
  private static TransactionParser parser = new TransactionParser();

  private TransactionDbHandler() {}

  public static Transaction[] findAll() {
    try {
      var strObjects = FileManager.readInFile(TRANSACTIONS_DB).split("\n");
      if (!strObjects[0].equals("")) return parser.parseObjects(strObjects);
      else return null;
    } catch (BlankFileException e) {
      System.err.println(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public static Transaction[] findHistoryByBalance(Balance balance) {
    return Arrays.stream(findAll())
        .filter(
            transaction ->
                transaction.getFrom().getAccount_number().equals(balance.getAccount_number())
                    || transaction.getTo().getAccount_number().equals(balance.getAccount_number()))
        .sorted(Comparator.comparing(Transaction::getId))
        .toArray(Transaction[]::new);
  }

  public static Transaction saveTransaction(Transaction transaction) {
    var transactionId = findFreeId();
    transaction.setId(transactionId);
    var toWrite = transaction.toString();
    if (transactionId != 1) toWrite = "\n" + toWrite;
    FileManager.writeToFile(TRANSACTIONS_DB, toWrite.getBytes(StandardCharsets.UTF_8));
    return transaction;
  }

  private static Long findFreeId() {
    var all = findAll();
    if (all != null) return (long) all.length + 1;
    else return 1L;
  }
}

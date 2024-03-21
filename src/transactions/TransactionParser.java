package transactions;

import assets.ObjectParser;
import balance.BalanceParser;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class TransactionParser implements ObjectParser<Transaction> {
  private static final BalanceParser balanceParser = new BalanceParser();

  @Override
  public Transaction[] parseObjects(String[] strings) {
    var transactions = new Transaction[strings.length];
    for (int i = 0; i < strings.length; i++) {
      var pattern = Pattern.compile("(?<=\\[)(.*?)(?<=\\]\\]) \\d+ \\d+.\\d+").matcher(strings[i]);
      var datas = strings[i].split(" ");
      if (pattern.find()) {
        var transaction =
            new Transaction(
                Long.parseLong(datas[0]),
                balanceParser.parseObject(pattern.group()),
                null,
                Float.parseFloat(datas[datas.length - 2]),
                LocalDateTime.parse(datas[datas.length - 1]));
        if (pattern.find()) {
          transaction.setTo(balanceParser.parseObject(pattern.group()));
          transactions[i] = transaction;
        }
      }
    }
    return transactions;
  }

  @Override
  public Transaction parseObject(String string) {
    return null;
  }
}

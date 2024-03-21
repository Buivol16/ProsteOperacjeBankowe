package balance;

import assets.ObjectParser;
import java.util.regex.Pattern;
import user.UserParser;

public class BalanceParser implements ObjectParser<Balance> {
  private static final UserParser userParser = new UserParser();

  @Override
  public Balance[] parseObjects(String[] strings) {
    var balances = new Balance[strings.length];
    for (int i = 0; i < strings.length; i++){
      balances[i] = parseObject(strings[i]);
    }
    return balances;
  }

  @Override
  public Balance parseObject(String string) {
    var datas = string.split(" ");
    var pattern = Pattern.compile("(?<=\\[)(.*?)(?>\\])").matcher(string);
    if (pattern.find()) {
      return new Balance(
          Long.parseLong(datas[0]),
          userParser.parseObject(pattern.group()),
          datas[datas.length - 2],
          Float.parseFloat(datas[datas.length - 1]));
    }
    return null;
  }
}

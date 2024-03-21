package user;

import assets.Country;
import assets.ObjectParser;

import java.util.regex.Pattern;

public class UserParser implements ObjectParser<User> {

  @Override
  public User[] parseObjects(String[] strings) {
    var users = new User[strings.length];
    for (int i = 0; i < strings.length; i++) users[i] = parseObject(strings[i]);
    return users;
  }

  @Override
  public User parseObject(String string) {
    var datas = string.split(" ");
    var pattern = Pattern.compile("(?<=\\[)(.*?)(?=\\])").matcher(string);
    if (pattern.find()) {
      return new User(
          Long.parseLong(datas[0]),
          datas[1],
          datas[2],
          datas[3],
          datas[4],
          Country.valueOf(datas[5]),
          pattern.group());
    }
    throw new NullPointerException();
  }
}

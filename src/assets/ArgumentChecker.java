package assets;

import static assets.Flags.COUNTRY;
import static assets.Flags.NOT_LONG;
import static assets.Flags.NOT_SHORT;
import static assets.Flags.ONLY_LETTERS;
import static assets.Flags.ONLY_NUMBERS;
import static assets.Flags.PESEL;

import java.util.Arrays;
import java.util.Scanner;

public class ArgumentChecker {
  public static String writeForArgument(Flags... flags) throws IllegalArgumentException {
    var scanner = new Scanner(System.in);
    String argument;
    var list = Arrays.asList(flags);
    argument = scanner.nextLine();
    if (list.contains(ONLY_LETTERS) && !argument.matches("^\\p{L}+$"))
      throw new IllegalArgumentException("The argument must contains only letters");
    if (list.contains(NOT_SHORT) && argument.length() < 2)
      throw new IllegalArgumentException("The argument must not be quite short");
    if (list.contains(NOT_LONG) && argument.length() > 64)
      throw new IllegalArgumentException("The argument must not be quite long");
    if (list.contains(COUNTRY)) return isCountry(argument).name();
    if (list.contains(ONLY_NUMBERS) && argument.matches("\\D+"))
      throw new IllegalArgumentException("The argument must contains only numbers");
    if (list.contains(PESEL) && argument.length() != 11)
      throw new IllegalArgumentException(
          "The argument has a wrong format. It must contains only 11 numbers");
    return argument;
  }

  private static Country isCountry(String argument) {
    return Arrays.stream(Country.values())
        .filter((country) -> country.name().equalsIgnoreCase(argument))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("The argument is not equals to any country"));
  }
}

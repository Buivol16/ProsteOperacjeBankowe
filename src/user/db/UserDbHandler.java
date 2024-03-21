package user.db;

import static assets.consts.DBFiles.USERS_DB;

import assets.FileManager;
import exceptions.BlankFileException;
import exceptions.UserExistsException;
import exceptions.UserNotFoundException;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import login.UserInputProjection;
import user.User;
import user.UserParser;

public class UserDbHandler {
  private static final UserParser userParser = new UserParser();
  private static final File DB = USERS_DB;

  private UserDbHandler() {}

  public static User[] findAll() throws BlankFileException {
    var strObjects = FileManager.readInFile(DB).split("\n");
    if (!strObjects[0].equals("")) return userParser.parseObjects(strObjects);
    else throw new BlankFileException("There is no data in file");
  }

  public static User saveUser(User user) throws UserExistsException {
    if (checkUsername(user.getUsername()))
      throw new UserExistsException("This username already exists in DB");
    var userId = findFreeId();
    user.setId(userId);
    var toWrite = user.toString();
    if (userId != 1) toWrite = "\n" + toWrite;
    FileManager.writeToFile(DB, toWrite.getBytes(StandardCharsets.UTF_8));
    return user;
  }

  public static User login(UserInputProjection inputProjection) throws UserNotFoundException {
    try {
      return Arrays.stream(findAll())
          .filter(
              user ->
                  user.getUsername().equals(inputProjection.getUsername())
                      && user.getPassword().equals(inputProjection.getPassword()))
          .findFirst()
          .orElseThrow(() -> new UserNotFoundException("Wrong username or password"));
    } catch (BlankFileException e) {
      throw new UserNotFoundException("User doesn't exist");
    }
  }

  public static boolean checkUsername(String username) {
    try {
      return Arrays.stream(findAll()).anyMatch(user -> user.getUsername().equals(username));
    } catch (BlankFileException e) {
      return false;
    }
  }

  private static Long findFreeId() {
    try {
      var all = findAll();
      if (all != null) return (long) all.length + 1;
    } catch (BlankFileException e) {
    }
    return 1L;
  }
}

package login;

public class UserInputProjection {
  private String username;
  private String password;

  private UserInputProjection() {}

  private UserInputProjection(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public static UserInputProjectionBuilder builder() {
    return new UserInputProjectionBuilder();
  }

  public static class UserInputProjectionBuilder {
    private static String username = null;
    private static String password = null;

    public UserInputProjection build() {
      return new UserInputProjection(username, password);
    }

    public UserInputProjectionBuilder username(String username) {
      this.username = username;
      return this;
    }

    public UserInputProjectionBuilder password(String password) {
      this.password = password;
      return this;
    }
  }
}

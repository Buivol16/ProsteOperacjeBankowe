package login;

public class UserInputProjection {
  private String pesel;
  private String password;

  private UserInputProjection() {}

  private UserInputProjection(String pesel, String password) {
    this.pesel = pesel;
    this.password = password;
  }

  public String getPesel() {
    return pesel;
  }

  public void setPesel(String pesel) {
    this.pesel = pesel;
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
    private static String pesel = null;
    private static String password = null;

    public UserInputProjection build() {
      return new UserInputProjection(pesel, password);
    }

    public UserInputProjectionBuilder pesel(String pesel) {
      this.pesel = pesel;
      return this;
    }

    public UserInputProjectionBuilder password(String password) {
      this.password = password;
      return this;
    }
  }
}

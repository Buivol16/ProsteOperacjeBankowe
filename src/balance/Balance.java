package balance;

import user.User;

import java.util.Objects;

public class Balance implements Cloneable {
  private Long id;
  private User owner;
  private String account_number;
  private Float balance;

  public Balance(Long id, User owner, String account_number, Float balance) {
    this.id = id;
    this.owner = owner;
    this.account_number = account_number;
    this.balance = balance;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public String getAccount_number() {
    return account_number;
  }

  public void setAccount_number(String account_number) {
    this.account_number = account_number;
  }

  public Float getBalance() {
    return balance;
  }

  public void setBalance(Float balance) {
    this.balance = balance;
  }

  @Override
  public String toString() {
    return String.format("%s [%s] %s %s", id, owner.toString(), account_number, balance);
  }

  @Override
  public Balance clone() throws CloneNotSupportedException {
    return (Balance) super.clone();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Balance balance1 = (Balance) o;
    return Objects.equals(id, balance1.id)
        && Objects.equals(owner, balance1.owner)
        && Objects.equals(account_number, balance1.account_number)
        && Objects.equals(balance, balance1.balance);
  }
}

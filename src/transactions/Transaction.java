package transactions;

import balance.Balance;

import java.time.LocalDateTime;

public class Transaction {
  private Long id;
  private Balance from;
  private Balance to;
  private Float sum;
  private LocalDateTime at;

  public Transaction(Long id, Balance from, Balance to, Float sum, LocalDateTime at) {
    this.id = id;
    this.from = from;
    this.to = to;
    this.sum = sum;
    this.at = at;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Balance getFrom() {
    return from;
  }

  public void setFrom(Balance from) {
    this.from = from;
  }

  public Balance getTo() {
    return to;
  }

  public void setTo(Balance to) {
    this.to = to;
  }

  public Float getSum() {
    return sum;
  }

  public void setSum(Float sum) {
    this.sum = sum;
  }

  public LocalDateTime getAt() {
    return at;
  }

  public void setAt(LocalDateTime at) {
    this.at = at;
  }

  @Override
  public String toString() {
    return String.format(
        "%s [%s] [%s] %s %s", id, from.toString(), to.toString(), sum, at.toString());
  }
}

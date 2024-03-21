package exceptions;

public class BalanceNotFoundException extends Exception{
    public BalanceNotFoundException(String message) {
        super(message);
    }
}

package exceptions;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super("This object does not exist");
    }
}
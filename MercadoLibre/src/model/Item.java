package model;

public class Item {
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    int quantity;
    Product product;


    public Item(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    @Override
    public String toString() {
        return "Items Info:" +
                "\n Quantity=" + quantity +
                "\n Product=" + product +
                "\n";
    }
}

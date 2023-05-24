package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Order {

    private String buyerName;

    public ArrayList<Item> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Item> productList) {
        this.productList = productList;
        //calculateTotalPrice();
    }

    private ArrayList<Item> productList;
    private double totalPrice;
    private LocalDate purchaseDate;

    public Order(String buyerName) {
        this.buyerName = buyerName;
        this.productList = new ArrayList<>();
        this.totalPrice = calculateTotalPrice();
        this.purchaseDate = LocalDate.now();
    }

    public double calculateTotalPrice() {
        this.totalPrice = 0;
        for (Item item : productList) {
            this.totalPrice += item.getProduct().getPrice() * item.getQuantity();
        }
        return this.totalPrice;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return "Orders Info:" +
                "\n Buyer Name='" + buyerName + '\'' +
                "\n Product List=" + productList +
                "\n Total Price=" + totalPrice +
                "\n PurchaseDate=" + purchaseDate +
                "\n";
    }
}

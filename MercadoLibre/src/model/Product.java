package model;

import exceptions.InvalidQuantityException;

public class Product {

    private String name;
    private String description;
    private double price;
    private int quantityAvailable;
    private ProductType category;
    private int purchaseNumber;

    public Product(String name, String description, double price, int quantityAvailable, int category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
        this.category = ProductType.values()[category];
        this.purchaseNumber = 0;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public ProductType getCategory() {
        return category;
    }

    public void setCategory(ProductType category) {
        this.category = category;
    }

    public int getPurchaseNumber() {
        return purchaseNumber;
    }

    public boolean updatePurchaseNumber(int purchase){
        this.purchaseNumber+=purchase;
        this.quantityAvailable-=purchase;
        return quantityAvailable == 0;
    }

    @Override
    public String toString() {
        return "Products Info:" +
                "\n Name='" + name + '\'' +
                "\n Description='" + description + '\'' +
                "\n Price=" + price +
                "\n Quantity Available=" + quantityAvailable +
                "\n Category=" + category +
                "\n Purchase Number=" + purchaseNumber +
                "\n";
    }

    public void setPurchaseNumber(int purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }
}

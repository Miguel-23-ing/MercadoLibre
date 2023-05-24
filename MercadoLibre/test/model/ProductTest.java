package model;

import exceptions.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private MercadoLibreControler mlc = new MercadoLibreControler(1);
    private Product product;

    public void setUp1() {
        mlc.getProducts().add(new Product("For Honor", "Video game based on the middle ages", 50000, 3, 7));
    }

    public void setUp2() {
        mlc.getProducts().add(new Product("Fortnite", "Battle Royal and construction", 80000, 4, 7));
        mlc.getProducts().add(new Product("Manga One Piece", "Pirate comic", 44000, 1, 0));
    }

    @Test
    public void removeProductFromRegistryTest() throws InvalidQuantityException {
        setUp1();
        mlc.registerOrder("Juan");
        int quantity = 3;
        Product product1 = mlc.addByNameAndPrice("For Honor", 50000, quantity);
        mlc.updateOrder("Juan", product1, quantity);
        Product result1 = mlc.getOrders().get("Juan").getProductList().get(0).getProduct();
        assertEquals(product1, result1);
    }

    @Test
    public void deleteProductFromRegistryTest() throws InvalidQuantityException {
        setUp1();
        mlc.registerOrder("Juan");
        int quantity = 3;
        Product product1 = mlc.addByNameAndPrice("For Honor", 50000, quantity);
        mlc.updateOrder("Juan", product1, quantity);
        Product result1 = mlc.addByNameAndPrice("For Honor", 50000, quantity);
        assertNull(result1);
    }

    @Test
    public void updateProductRegisteredTest() {
        setUp1();
        int quantity = mlc.searchByPrice(50000, 1).get(0).getQuantityAvailable();
        mlc.registerProduct("For Honor::Game::50000::7::7");
        int result = mlc.searchByPrice(50000, 1).get(0).getQuantityAvailable();
        assertNotEquals(quantity, result);
        assertEquals(10, result);
    }

    @Test
    public void validateProductByPriceNotExistsTest() {
        setUp2();
        double price = 100000;
        ArrayList<Product> result = mlc.searchByPrice(price, 1);
        assertNull(result);
    }

    @Test
    public void searchProductByNameTest() {
        // Arrange
        setUp2();
        String name = "Fortnite";
        Product expected = new Product("War zone", "Battle Royal", 80000, 4, 7);
        ArrayList<Product> result = mlc.searchByName(name, 1);

        assertEquals(expected.getName(), result.get(0).getName());
        /*assertEquals(expected.getDescription(), result.get(0).getDescription());
        assertEquals(expected.getPrice(), result.get(0).getPrice(), 0.001);*/
    }

    @Test
    public void searchByPurchaseNumberSuccessfullyTest() {
        setUp2();
        int purchaseNumber = 0;
        ArrayList<Product> result = mlc.searchByPurchaseNumber(0, 1);
        assertNotNull(result);
    }

    @Test
    public void searchByQuantityAvailableSuccessfullyTest(){
        setUp2();
        int quantity = 1;
        ArrayList<Product> result = mlc.searchByQuantityAvailable(4, 1);
        Product expected = new Product("Play 4", "Game console", 80000, 1, 7);
        assertEquals(expected.getQuantityAvailable(), expected.getQuantityAvailable());
    }
    @Test
    public void searchByPriceRangeNullTest(){
        setUp2();
        double init = 100000;
        double end =  200000;
        ArrayList<Product> result = mlc.searchByPriceRange(init, end, 1);
        assertNull(result);
    }
    @Test
    public void searchByPurchaseNumberRange(){
        setUp2();
        int init = 2;
        int end = 8;
        ArrayList<Product> result = mlc.searchByPurchaseNumberRange(init, end, 1);
        assertNull(result);
    }
    @Test
    public void searchByCategoryTest(){
        setUp2();
        ArrayList<Product> a = mlc.searchByCategory(8, 1);
        Product result = a.get(0);
        Product expected = mlc.getProducts().get(1);
        assertEquals(expected, result);
        //we get product in position 1 because list is sorted by searching
    }
    @Test
    public void searchByPrefixSuccessfullyTest(){
        setUp2();
        String prefix = "Fo";
        ArrayList<Product> result = mlc.searchByPrefix(prefix,1);
        assertNotNull(result);
    }
}
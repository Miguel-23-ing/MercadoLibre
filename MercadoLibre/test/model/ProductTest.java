package model;


import exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;

    public void setUp1Product(){
        product = new Product("For Honor", "Video game based on the middle ages", 50000,  3, 7);
    }

    @Test
    public void editProductNameSuccesfullyTest(){
        setUp1Product();
        product.edit("For the honor");
        assertEquals(product.getName(), "For the honor");
    }

    @Test
    public void increaseTheAvailableQuantityOfAProductSuccesfullyTest(){
        setUp1Product();
        product.edit(5);
        assertEquals(product.getQuantityAvailable(), 8);
    }

    @Test
    public void removeProductFromRegistryTest() throws ProductNotFoundException {
        setUp1Product();
        product.delete("Fortnite");
        assertNull(product);
    }

}

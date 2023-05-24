package model;

import org.junit.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OrderTest {

    private MercadoLibreControler mlc = new MercadoLibreControler(1);

    public void setupStage1(){
        mlc.getProducts().add(new Product("Super Smash Bros. Ultimate", "Super Smash Bros. Ultimate is a 2018 crossover fighting game developed by Bandai Namco Studios and Sora Ltd. and published by Nintendo for the Nintendo Switch. It is the fifth installment in the Super Smash Bros. series, succeeding Super Smash Bros. for Nintendo 3DS and Wii U", 271577, 5000,7));
    }

    public void setupStage2(){
        mlc.getProducts().add(new Product("Super Mario Odyssey","Super Mario Odyssey is a 2017 platform game developed and published by Nintendo for the Nintendo Switch.", 252462, 49,7));
        mlc.getProducts().add(new Product("Mario Strikers", "Mario Strikers: Battle League is a 2022 sports video game developed by Next Level Games and published by Nintendo for the Nintendo Switch. It was released worldwide on June 10, 2022. It is the third game in the Mario Strikers series.", 205000, 4, 7));
        mlc.registerOrder("Kofi");
    }

    public void setupStage3(){
        Product p1 = new Product("Yoshi’s Crafted World", "Yoshi's Crafted World is a platform video game developed by Good-Feel and published by Nintendo for the Nintendo Switch. The eighth main installment in the Yoshi franchise, it is the spiritual successor to Yoshi's Woolly World. The game was revealed at E3 2017, and was released worldwide on March 29, 2019", 274900, 5, 7);
        Product p2 = new Product("The Legend of Zelda: Breath of the Wild", "The Legend of Zelda: Breath of the Wild is a 2017 action-adventure game developed by Nintendo EPD for the Nintendo Switch and Wii U. Set at the end of the Zelda timeline, the player controls an amnesiac Link as he sets out to save Princess Zelda and prevent Calamity Ganon from destroying Hyrule.", 250900, 1, 7);
        ArrayList<Item> arr = new ArrayList<>();
        ArrayList<Item> arr2 = new ArrayList<>();
        arr.add(new Item(1, p1));
        arr.add(new Item(1, p2));
        arr2.add(new Item(2, p1));
        mlc.getProducts().add(p1);
        mlc.getProducts().add(p2);
        mlc.getOrders().put("1", new Order("Miguel"));
        mlc.getOrders().get("1").setProductList(arr);
        mlc.getOrders().put("2", new Order("Santiago"));
        mlc.getOrders().get("2").setProductList(arr2);
    }

    @Test
    public void registerSingleOrder(){
        setupStage1();
        String name = "Kofi";
        mlc.registerOrder(name);
        String result = mlc.getOrders().get("1").getBuyerName();
        assertEquals("Kofi", result);
    }

    @Test
    public void registerOrderWithExistingOrders(){
        setupStage2();
        String name = "Miguel";
        mlc.registerOrder(name);
        String result = mlc.getOrders().get("Miguel").getBuyerName();
        assertEquals("Miguel", result);
    }

    @Test
    public void searchOrderByBuyerNameTest(){
        setupStage2();
        String name = "Kofi";
        ArrayList<Order> a = mlc.searchByBuyerName(name, 1);
        String result = a.get(0).getBuyerName();
        Order result2 = a.get(0);
        assertEquals("Kofi", result);
        assertEquals(mlc.getOrders().get("1"), result2);

    }

    @Test
    public void searchOrderByTotalPriceTest(){
        setupStage3();
        Double price = 525800.0;
        ArrayList<Order> a = mlc.searchByTotalPrice(price, 1);
        String result = a.get(0).getBuyerName();
        Order result2 = a.get(0);
        assertEquals("Miguel", result);
        assertEquals(mlc.getOrders().get("1"), result2);

    }

    @Test
    public void searchOrderByPurchaseDateTest(){
        setupStage3();
        int year =2023;
        int month = 5;
        int day = 6;
        ArrayList<Order> result = mlc.searchByPurchaseDate(year, month, day, 1);
        assertNull(result);
    }

    @Test
    public void searchOrderByTotalPriceRangeTest(){
        setupStage3();
        double init = 1000;
        double end =  2000;
        ArrayList<Order> result = mlc.searchByTotalPriceRange(init, end, 1);
        assertNull(result);
    }

    @Test
    public void searchOrderByDateRangeTest(){
        setupStage3();
        int year =2023;
        int month = 5;
        int day = 7;
        int year2 =2024;
        int month2 = 5;
        int day2 = 7;
        ArrayList<Order> a = mlc.searchByDateRange(year, month, day, year2, month2, day2, 1);
        String result = a.get(0).getBuyerName();
        Order result1 = a.get(0);
        String result2 = a.get(1).getBuyerName();
        Order result21 = a.get(1);
        assertEquals("Miguel", result);
        assertEquals(mlc.getOrders().get("1"), result1);
        assertEquals("Santiago", result2);
        assertEquals(mlc.getOrders().get("2"), result21);
    }

    @Test
    public void searchByBuyerPrefixTest(){
        setupStage3();
        String prefix = "Mi";
        ArrayList<Order> a = mlc.searchBuyerNameByPrefix(prefix, 1);
        String result = a.get(0).getBuyerName();
        Order result1 = a.get(0);
        assertEquals("Miguel", result);
        assertEquals(mlc.getOrders().get("1"), result1);
    }

    @Test
    public void searchByBuyerNamePrefixRange(){
        setupStage3();
        String prefixInit = "Si";
        String prefixEnd = "Zz";
        ArrayList<Order> result = mlc.searchByBuyerNamePrefixRange(prefixInit, prefixEnd, 1);
        assertNull(result);
    }
}
